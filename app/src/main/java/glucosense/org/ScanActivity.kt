package glucosense.org

import android.content.Context
import android.content.Intent
import android.nfc.NdefMessage
import android.nfc.NdefRecord
import android.nfc.NfcAdapter
import android.nfc.tech.NfcA
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.View
//import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_scan.*

class ScanActivity : AppCompatActivity() {

    private var mNfcAdapter: NfcAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scan)
        Log.i("scan", "launched")
        //val toolbar: Toolbar = findViewById(R.id.toolbar) as Toolbar
        //setSupportActionBar(toolbar)
        //toolbar.setTitle("Scan")
        mNfcAdapter = NfcAdapter.getDefaultAdapter(this)
        Log.i("nfc", NFCUtil.retrieveNFCMessage(this.intent))
        backButton.setOnClickListener {
            Log.d("test", "works")
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }
}
