package glucosense.org

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Menu
import android.view.MenuItem

import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        addButton.setOnClickListener {
            Log.i("pressed", "add")
            val intent = Intent(this, AddActivity::class.java)
            startActivity(intent)
        }
        scanButton.setOnClickListener {
            Log.i("pressed", "scan")
            val intent = Intent(this, ScanActivity::class.java)
            startActivity(intent)
        }
        historyButton.setOnClickListener {
            Log.i("pressed", "history")
            val intent = Intent(this, HistoryActivity::class.java)
            startActivity(intent)
        }
        statisticsButton.setOnClickListener {
            Log.i("pressed", "statistics")
            val intent = Intent(this, StatisticsActivity::class.java)
            startActivity(intent)
        }
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
        when (item.itemId) {
            R.id.action_settings -> {
                Log.i("menu", "settings")
                val intent = Intent(this, SettingsActivity::class.java)
                startActivity(intent)
            }
        }
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }
}
