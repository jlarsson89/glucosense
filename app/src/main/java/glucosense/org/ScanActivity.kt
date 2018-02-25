package glucosense.org

import android.app.PendingIntent
import android.content.Intent
import android.nfc.NdefMessage
import android.nfc.NfcAdapter
import android.nfc.tech.NfcV
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_scan.*
import android.nfc.NdefRecord
import android.nfc.Tag
import android.nfc.tech.NfcA
import android.os.Parcelable
import android.icu.util.ULocale.getLanguage
import java.nio.charset.Charset
import java.util.*
import java.nio.file.Files.size
import android.widget.LinearLayout
import android.view.LayoutInflater
//import android.support.test.runner.internal.deps.aidl.Codecs.writeStrongBinder
import android.os.Parcel
import android.nfc.tech.MifareClassic
import android.os.IBinder
import android.nfc.tech.MifareUltralight
import kotlin.experimental.and
import kotlin.experimental.or


class ScanActivity : AppCompatActivity() {

    private var nfcAdapter: NfcAdapter? = null
    private var nfcPendingIntent: PendingIntent? = null
    private val mNdefPushMessage: NdefMessage? = null
    private val tags = ArrayList<Tag?>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scan)
        Log.i("scan", "launched")
        nfcAdapter = NfcAdapter.getDefaultAdapter(this)
        nfcPendingIntent = PendingIntent.getActivity(this, 0,
                Intent(this, javaClass).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0)
        if (intent != null) {
            //Log.i("found intent", intent.action.toString())
            Log.i("intent", intent.toString())
            processIntent(intent)
        }
        Log.i("nfc supported", (nfcAdapter != null).toString())
        Log.i("nfc enabled", (nfcAdapter?.isEnabled.toString()))
        backButton.setOnClickListener {
            Log.d("test", "works")
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)        }
    }

    private fun dumpTagData(tag: Tag?): String {
        var tag = tag
        val sb = StringBuilder()
        val id = tag!!.id
        sb.append("ID (hex): ").append(toHex(id)).append('\n')
        sb.append("ID (reversed hex): ").append(toReversedHex(id)).append('\n')
        sb.append("ID (dec): ").append(toDec(id)).append('\n')
        sb.append("ID (reversed dec): ").append(toReversedDec(id)).append('\n')

        val prefix = "android.nfc.tech."
        sb.append("Technologies: ")
        for (tech in tag.techList) {
            sb.append(tech.substring(prefix.length))
            sb.append(", ")
        }
        sb.delete(sb.length - 2, sb.length)
        for (tech in tag!!.techList) {
            if (tech == MifareClassic::class.java.name) {
                sb.append('\n')
                var type = "Unknown"
                try {
                    var mifareTag: MifareClassic
                    try {
                        mifareTag = MifareClassic.get(tag)
                    } catch (e: Exception) {
                        // Fix for Sony Xperia Z3/Z5 phones
                        tag = cleanupTag(tag)
                        mifareTag = MifareClassic.get(tag)
                    }

                    when (mifareTag.type) {
                        MifareClassic.TYPE_CLASSIC -> type = "Classic"
                        MifareClassic.TYPE_PLUS -> type = "Plus"
                        MifareClassic.TYPE_PRO -> type = "Pro"
                    }
                    sb.append("Mifare Classic type: ")
                    sb.append(type)
                    sb.append('\n')

                    sb.append("Mifare size: ")
                    sb.append(mifareTag.size.toString() + " bytes")
                    sb.append('\n')

                    sb.append("Mifare sectors: ")
                    sb.append(mifareTag.sectorCount)
                    sb.append('\n')

                    sb.append("Mifare blocks: ")
                    sb.append(mifareTag.blockCount)
                } catch (e: Exception) {
                    sb.append("Mifare classic error: " + e.message)
                }

            }

            if (tech == MifareUltralight::class.java.name) {
                sb.append('\n')
                val mifareUlTag = MifareUltralight.get(tag)
                var type = "Unknown"
                when (mifareUlTag.type) {
                    MifareUltralight.TYPE_ULTRALIGHT -> type = "Ultralight"
                    MifareUltralight.TYPE_ULTRALIGHT_C -> type = "Ultralight C"
                }
                sb.append("Mifare Ultralight type: ")
                sb.append(type)
            }
        }

        return sb.toString()
    }

    private fun cleanupTag(oTag: Tag?): Tag? {
        if (oTag == null)
            return null

        val sTechList = oTag.techList

        val oParcel = Parcel.obtain()
        oTag.writeToParcel(oParcel, 0)
        oParcel.setDataPosition(0)

        val len = oParcel.readInt()
        var id: ByteArray? = null
        if (len >= 0) {
            id = ByteArray(len)
            oParcel.readByteArray(id)
        }
        val oTechList = IntArray(oParcel.readInt())
        oParcel.readIntArray(oTechList)
        val oTechExtras = oParcel.createTypedArray(Bundle.CREATOR)
        val serviceHandle = oParcel.readInt()
        val isMock = oParcel.readInt()
        val tagService: IBinder?
        if (isMock == 0) {
            tagService = oParcel.readStrongBinder()
        } else {
            tagService = null
        }
        oParcel.recycle()

        var nfca_idx = -1
        var mc_idx = -1
        var oSak: Short = 0
        var nSak: Short = 0

        for (idx in sTechList.indices) {
            if (sTechList[idx] == NfcA::class.java.name) {
                if (nfca_idx == -1) {
                    nfca_idx = idx
                    if (oTechExtras[idx] != null && oTechExtras[idx].containsKey("sak")) {
                        oSak = oTechExtras[idx].getShort("sak")
                        nSak = oSak
                    }
                } else {
                    if (oTechExtras[idx] != null && oTechExtras[idx].containsKey("sak")) {
                        nSak = (nSak or oTechExtras[idx].getShort("sak")).toShort()
                    }
                }
            } else if (sTechList[idx] == MifareClassic::class.java.name) {
                mc_idx = idx
            }
        }

        var modified = false

        if (oSak != nSak) {
            oTechExtras[nfca_idx].putShort("sak", nSak)
            modified = true
        }

        if (nfca_idx != -1 && mc_idx != -1 && oTechExtras[mc_idx] == null) {
            oTechExtras[mc_idx] = oTechExtras[nfca_idx]
            modified = true
        }

        if (!modified) {
            return oTag
        }

        val nParcel = Parcel.obtain()
        nParcel.writeInt(id!!.size)
        nParcel.writeByteArray(id)
        nParcel.writeInt(oTechList.size)
        nParcel.writeIntArray(oTechList)
        nParcel.writeTypedArray(oTechExtras, 0)
        nParcel.writeInt(serviceHandle)
        nParcel.writeInt(isMock)
        if (isMock == 0) {
            nParcel.writeStrongBinder(tagService)
        }
        nParcel.setDataPosition(0)

        val nTag = Tag.CREATOR.createFromParcel(nParcel)

        nParcel.recycle()

        return nTag
    }

    private fun toHex(bytes: ByteArray): String {
        val sb = StringBuilder()
        for (i in bytes.indices.reversed()) {
            //val b = bytes[i] and 0xff
            val b = bytes[i].toInt()
            if (b < 0x10)
                sb.append('0')
            sb.append(Integer.toHexString(b))
            if (i > 0) {
                sb.append(" ")
            }
        }
        return sb.toString()
    }

    private fun toReversedHex(bytes: ByteArray): String {
        val sb = StringBuilder()
        for (i in bytes.indices) {
            if (i > 0) {
                sb.append(" ")
            }
            //val b = bytes[i] and 0xff
            val b = bytes[i].toInt()
            if (b < 0x10)
                sb.append('0')
            sb.append(Integer.toHexString(b))
        }
        return sb.toString()
    }

    private fun toDec(bytes: ByteArray): Long {
        var result: Long = 0
        var factor: Long = 1
        for (i in bytes.indices) {
            //val value = bytes[i] and 0xffL
            val value = bytes[i].toInt()
            result += value * factor
            factor *= 256L
        }
        return result
    }

    private fun toReversedDec(bytes: ByteArray): Long {
        var result: Long = 0
        var factor: Long = 1
        for (i in bytes.indices.reversed()) {
            //val value = bytes[i] and 0xffL
            val value = bytes[i].toInt()
            result += value * factor
            factor *= 256L
        }
        return result
    }

    fun buildTagViews(msgs: Array<NdefMessage>?) {
        if (msgs == null || msgs.size == 0) {
            return
        }
        /*val inflater = LayoutInflater.from(this)
        val content = mTagContent

        // Parse the first message in the list
        // Build views for all of the sub records
        val now = Date()
        val records = NdefMessageParser.parse(msgs[0])
        val size = records.size
        for (i in 0 until size) {
            val timeView = TextView(this)
            timeView.setText(TIME_FORMAT.format(now))
            content.addView(timeView, 0)
            val record = records.get(i)
            content.addView(record.getView(this, inflater, content, i), 1 + i)
            content.addView(inflater.inflate(R.layout.tag_divider, content, false), 2 + i)
        }*/
    }

    private fun newTextRecord(text: String, locale: Locale, encodeInUtf8: Boolean): NdefRecord {
        val langBytes = locale.language.toByteArray()
        //val langBytes = locale.getLanguage().getBytes(Charset.forName("US-ASCII"))
        val utfEncoding = if (encodeInUtf8) Charset.forName("UTF-8") else Charset.forName("UTF-16")
        //val textBytes = text.getBytes(utfEncoding)
        val textBytes = text.toByteArray()
        val utfBit = if (encodeInUtf8) 0 else 1 shl 7
        val status = (utfBit + langBytes.size).toChar()
        val x = langBytes.size + textBytes.size + 1
        var data = ByteArray(1 + langBytes.size + textBytes.size)
        data[0] = status.toByte()
        System.arraycopy(langBytes, 0, data, 1, langBytes.size)
        System.arraycopy(textBytes, 0, data, 1 + langBytes.size, textBytes.size)
        data = ByteArray(0)
        return NdefRecord(NdefRecord.TNF_WELL_KNOWN, NdefRecord.RTD_TEXT, ByteArray(0), data)
    }

    override fun onResume() {
        super.onResume()
        nfcAdapter?.enableForegroundDispatch(this, nfcPendingIntent, null, null)
    }

    override fun onPause() {
        super.onPause()
        nfcAdapter?.disableForegroundDispatch(this)
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        if (intent != null) {
            processIntent(intent)
        }
    }

    /*private fun resolveIntent(intent: Intent) {
        val action = intent.action
        if (NfcAdapter.ACTION_TAG_DISCOVERED == action
                || NfcAdapter.ACTION_TECH_DISCOVERED == action
                || NfcAdapter.ACTION_NDEF_DISCOVERED == action) {
            val rawMsgs = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES)
            val msgs: Array<NdefMessage>
            if (rawMsgs != null) {
                msgs = arrayOfNulls(rawMsgs.size)
                for (i in rawMsgs.indices) {
                    msgs[i] = rawMsgs[i] as NdefMessage
                }
            } else {
                // Unknown tag type
                val empty = ByteArray(0)
                val id = intent.getByteArrayExtra(NfcAdapter.EXTRA_ID)
                val tag = intent.getParcelableExtra<Parcelable>(NfcAdapter.EXTRA_TAG) as Tag
                val payload = dumpTagData(tag).getBytes()
                val record = NdefRecord(NdefRecord.TNF_UNKNOWN, empty, id, payload)
                val msg = NdefMessage(arrayOf(record))
                msgs = arrayOf(msg)
                mTags.add(tag)
            }
            // Setup the views
            buildTagViews(msgs)
        }
    }*/


    private fun processIntent(checkIntent: Intent) {
        val action = checkIntent.action
        Log.i("process intent", "runs")
        if (NfcAdapter.ACTION_TAG_DISCOVERED == action) {
            Log.i("action", action)
        }
        /*if (/*checkIntent.action == NfcAdapter.ACTION_NDEF_DISCOVERED || */checkIntent.action == NfcAdapter.ACTION_TAG_DISCOVERED/* || checkIntent.action == NfcAdapter.ACTION_TECH_DISCOVERED*/) {
            Log.i("new ndef intent", checkIntent.toString())
            val rawMessages3 = NfcV.get(checkIntent.getParcelableExtra(NfcAdapter.EXTRA_NDEF_MESSAGES))
            val rawMessages2 = checkIntent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES)
            val rawMessages = NfcV.get(checkIntent.getParcelableExtra(NfcAdapter.EXTRA_TAG))
            //val extras = checkIntent.extras
            val test = NfcV.get(checkIntent.getParcelableExtra(NfcAdapter.EXTRA_TAG))
            Log.i("rawmessages intent", rawMessages.toString())
            Log.i("rawmessages id", rawMessages.dsfId.toString())
            //Log.i("rawmessages2", rawMessages2.toString())
            //Log.i("rawmessages3", rawMessages3.toString())
            Log.i("test intent", test.toString())
            val test2 = test.tag.techList
            Log.i("test2", test2.toString())
            Log.i("test2 size", test2.size.toString())
            val label = TextView(this)
            //label.text = extras.toString()
            scanActivity.addView(label)
            val test3 = rawMessages.responseFlags
            Log.i("test3", test3.toString())
            //val rawMessages = checkIntent.getParcelableArrayExtra(NfcAdapter.EXTRA_TAG)
            //Log.i("raw messages", rawMessages.size.toString())
            if (test3 != null) {
                //val msg = arrayOfNulls<NdefMessage?>(test2.size)
                //val msgs = arrayOf(test2)
                val msg = arrayOf(test3)
                Log.i("msg", msg.toString())
                for (i in msg.indices) {
                    //val m = msg[i]?.records
                    val a = msg.toString()
                    Log.i("arrayof", a)
                    //val n = msgs?.get(rawMessages.dsfId.toInt())
                    //Log.i("msg loop", i.toString())
                    //Log.i("m", n.toString())
                }
                for (i in test2.indices) {
                    //val m = test2[i] as NdefMessage
                    Log.i("i", i.toString())
                    //Log.i("i2", m.toString())

                }
                //processNdefMessages(msg)
            }
            if (rawMessages != null) {

                //val messages = arrayOf<NdefMessage?>()
                //val messages = arrayOfNulls<NdefMessage?>(rawMessages.size)
                //for (i in rawMessages.indices) {
                    //val msg = rawMessages[i] as NdefMessage
                    //Log.i("msg", msg.toString())
                //}
                //processNdefMessages(messages)
            }
        }*/
    }

    private fun processNdefMessages(ndefMessages: Array<NdefMessage?>) {
        for (curMsg in ndefMessages) {
            if (curMsg != null) {
                //Log.i("msg", curMsg.toString())
                Log.i("msgsize", curMsg.records.size.toString())
                for (curRecord in curMsg.records) {
                    if (curRecord.toUri() != null) {
                        Log.i("recorduri", curRecord.toUri().toString())
                    }
                    else {
                        Log.i("recordcontent", curRecord.payload.contentToString())
                    }
                }
            }
        }
    }
}
