package glucosense.org

import android.app.PendingIntent
import android.content.Intent
import android.nfc.NfcAdapter
import android.nfc.tech.NfcV
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.nfc.Tag
import android.nfc.NdefMessage
import kotlinx.android.synthetic.main.activity_scan.*

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
            startActivity(intent)
        }
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

    private fun processIntent(checkIntent: Intent) {
        val action = checkIntent.action
        Log.i("process intent", "runs")
        if (NfcAdapter.ACTION_TAG_DISCOVERED.equals(action)) {
            Log.i("action", action)
            //var rawMessages = intent.getParcelableArrayExtra(NfcAdapter.ACTION_TAG_DISCOVERED)
            //var rawMessages = checkIntent.getParcelableArrayExtra(NfcAdapter.EXTRA_TAG)
            val rawMessages = NfcV.get(checkIntent.getParcelableExtra(NfcAdapter.EXTRA_TAG))
            rawMessages.connect()
            if (rawMessages.isConnected) {
                Log.i("connected", rawMessages.isConnected.toString())
                val msgs = rawMessages.responseFlags
                val tag = rawMessages.tag
                val techlist = tag.techList
                val maxlength = rawMessages.maxTransceiveLength
                val dsfid = rawMessages.dsfId
                Log.i("intent", rawMessages.toString())
                Log.i("id", dsfid.toString())
                Log.i("flags", msgs.toString())
                Log.i("tag", tag.toString())
                Log.i("length", maxlength.toString())
                Log.i("list", techlist.size.toString())
                for (i in techlist.indices) {
                    Log.i("i", i.toString())
                    val x = techlist.get(i).toString()
                    Log.i("x", x)
                }
                //Log.i("raw", rawMessages.size.toString())
                //var rawMessages = NfcV.get(intent.getParcelableExtra(NfcAdapter.EXTRA_NDEF_MESSAGES))
                /*if (rawMessages != null) {
                    var msgs = arrayOfNulls<NdefMessage?>(rawMessages.size)
                    Log.i("works", rawMessages.size.toString())
                }*/
            }
            rawMessages.close()
        }
    }
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