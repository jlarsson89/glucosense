package glucosense.org

import android.app.PendingIntent
import android.content.Intent
import android.nfc.NdefMessage
import android.nfc.NfcAdapter
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import kotlinx.android.synthetic.main.activity_scan.*

class ScanActivity : AppCompatActivity() {

    private var nfcAdapter: NfcAdapter? = null
    private var nfcPendingIntent: PendingIntent? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scan)
        Log.i("scan", "launched")
        nfcAdapter = NfcAdapter.getDefaultAdapter(this)
        nfcPendingIntent = PendingIntent.getActivity(this, 0,
                Intent(this, javaClass).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0)
        if (intent != null) {
            //Log.i("found intent", intent.action.toString())
            processIntent(intent)
        }
        Log.i("nfc supported", (nfcAdapter != null).toString())
        Log.i("nfc enabled", (nfcAdapter?.isEnabled.toString()))
        backButton.setOnClickListener {
            Log.d("test", "works")
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)        }
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
        if (checkIntent.action == NfcAdapter.ACTION_NDEF_DISCOVERED) {
            Log.i("new ndef intent", checkIntent.toString())
            val rawMessages = checkIntent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES)
            Log.i("raw messages", rawMessages.size.toString())
            if (rawMessages != null) {
                val messages = arrayOfNulls<NdefMessage?>(rawMessages.size)
                for (i in rawMessages.indices) {
                    val msg = rawMessages[i] as NdefMessage
                    Log.i("msg", msg.toString())
                }
                processNdefMessages(messages)
            }
        }
    }

    private fun processNdefMessages(ndefMessages: Array<NdefMessage?>) {
        for (curMsg in ndefMessages) {
            if (curMsg != null) {
                Log.i("msg", curMsg.toString())
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
