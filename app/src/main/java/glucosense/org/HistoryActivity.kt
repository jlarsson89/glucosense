package glucosense.org

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_history.*
import java.io.BufferedReader
import java.io.File

class HistoryActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)
        val gson = Gson()
        val bufferedReader: BufferedReader = File(filesDir.absolutePath+"/injections.json").bufferedReader()
        val inputString = bufferedReader.use { it.readText() }
        val injection = gson.fromJson(inputString, InjectionActivity.Injection::class.java)
        val stringBuilder = StringBuilder("Injections:")
        Log.i("time: ", injection.datetime)
        Log.i("type: ", injection.type)
        Log.i("units: ", injection.units)
        stringBuilder.append("\ntype: ", injection.type)
        stringBuilder.append("\nunits: ", injection.units)
        backButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }
}
