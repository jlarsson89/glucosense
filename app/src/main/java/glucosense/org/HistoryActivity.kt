package glucosense.org

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import com.google.gson.Gson
import io.realm.Realm
import kotlinx.android.synthetic.main.activity_history.*
import java.io.BufferedReader
import java.io.File

class HistoryActivity : AppCompatActivity() {
    var injectionModel = InjectionModel()
    var realm = Realm.getDefaultInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)
        injectionModel = InjectionModel()
        var results = injectionModel.getInjections(realm)
        results.forEach { result ->
            Log.i("result", result._ID + " " + result.type + " " + result.units)
        }
        backButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }
}
