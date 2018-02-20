package glucosense.org

import android.content.Context
import android.content.Intent
import android.nfc.NdefMessage
import android.nfc.NdefRecord
import android.nfc.NfcAdapter
import android.nfc.tech.NfcA
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import kotlinx.android.synthetic.main.activity_scan.*

class ScanActivity : AppCompatActivity() {

    private var mNfcAdapter: NfcAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scan)
        Log.i("scan", "launched")
        mNfcAdapter = NfcAdapter.getDefaultAdapter(this)
        Log.i("nfc", NFCUtil.retrieveNFCMessage(this.intent))
        Log.i("enable", mNfcAdapter?.isEnabled.toString())
        backButton.setOnClickListener {
            Log.d("test", "works")
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)        }
    }
}
