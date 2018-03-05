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
                val data = ByteArray(40)
                val sysinfo = rawMessages.transceive(data)
                Log.i("intent", rawMessages.toString())
                Log.i("id", dsfid.toString())
                Log.i("flags", msgs.toString())
                Log.i("tag", tag.toString())
                Log.i("length", maxlength.toString())
                Log.i("list", techlist.size.toString())
                Log.i("sysinfo", sysinfo.first().toString())
            }
            rawMessages.close()
        }
    }
}