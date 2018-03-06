package glucosense.org

import android.app.PendingIntent
import android.content.Intent
import android.nfc.NfcAdapter
import android.nfc.tech.NfcV
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import kotlinx.android.synthetic.main.activity_scan.*

class ScanActivity : AppCompatActivity() {
    private var nfcAdapter: NfcAdapter? = null
    private var nfcPendingIntent: PendingIntent? = null
    protected val hexArray = "0123456789ABCDEF".toCharArray()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scan)
        Log.i("scan", "launched")
        nfcAdapter = NfcAdapter.getDefaultAdapter(this)
        nfcPendingIntent = PendingIntent.getActivity(this, 0,
                Intent(this, javaClass).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0)
        if (intent != null) {
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
            val rawMessages = NfcV.get(checkIntent.getParcelableExtra(NfcAdapter.EXTRA_TAG))
            rawMessages.connect()
            if (rawMessages.isConnected) {
                Log.i("connected", rawMessages.isConnected.toString())
                var data = byteArrayOf(0x00.toByte(), 0x2B.toByte())
                val msgs = rawMessages.responseFlags
                val tag = rawMessages.tag
                val techlist = tag.techList
                val maxlength = rawMessages.maxTransceiveLength
                val dsfid = rawMessages.dsfId
                val sysinfo: ByteArray = rawMessages.transceive(data)
                //sysinfo = Arrays.copyOfRange(sysinfo, 2, sysinfo.size - 1)
                //var memorySize = byteArrayOf(sysinfo[6], sysinfo[5])
                //Log.i("Memory Size: ",bytesToHex(memorySize) + " / " + Integer.parseInt(bytesToHex(memorySize).trim({ it <= ' ' }), 16))
                //var blocks = byteArrayOf(sysinfo[8])
                //Log.i("blocks: ", bytesToHex(blocks) + " / " + Integer.parseInt(bytesToHex(blocks).trim({ it <= ' ' }), 16))
                //var totalBlocks = Integer.parseInt(bytesToHex(blocks).trim({ it <= ' ' }), 16)
                //Log.i("total blocks:", totalBlocks.toString())
                Log.i("intent", rawMessages.toString())
                Log.i("id", dsfid.toString())
                Log.i("flags", msgs.toString())
                Log.i("tag", tag.toString())
                Log.i("length", maxlength.toString())
                Log.i("list", techlist.size.toString())
                Log.i("sysinfo size", sysinfo.size.toString())
            }
            rawMessages.close()
        }
    }

    private fun bytesToHex(bytes: ByteArray): String {
        val hexChars = CharArray(bytes.size * 2)
        for (j in bytes.indices) {
            //var v = bytes[j] and 0xFF
            val v = bytes[j].toInt() and 255
            hexChars[j * 2] = hexArray[v.ushr(4)]
            hexChars[j * 2 + 1] = hexArray[v and 0x0F]
        }
        return String(hexChars)
    }
}