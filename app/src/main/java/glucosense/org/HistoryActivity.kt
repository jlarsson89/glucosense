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
        /*val gson = Gson()
        var bufferedReader: BufferedReader = File(filesDir.absolutePath+"/injections.json").bufferedReader()
        var inputString = bufferedReader.use { it.readText() }
        val injection = gson.fromJson(inputString, InjectionActivity.Injection::class.java)
        injection.datetime?.forEach {
            val label = TextView(injectionsList.context)
            label.setText(injection.datetime + ", " + injection.type + ", " + injection.units)
            injectionsList.addView(label)
            Log.i("time", injection.datetime)
            Log.i("type", injection.type)
            Log.i("units", injection.units)
        }
        bufferedReader = File(filesDir.absolutePath+"/meals.json").bufferedReader()
        inputString = bufferedReader.use { it.readText()}
        val meals = gson.fromJson(inputString, MealActivity.Meal::class.java)*/
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
