package glucosense.org

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.google.gson.Gson
import io.realm.Realm
import kotlinx.android.synthetic.main.activity_injection.*
import java.io.File
import java.util.*

class InjectionActivity : AppCompatActivity() {
    class Injection {
        var datetime: String? = null
        var type: String? = null
        var units: String? = null
        constructor(datetime: String, type: String, units: String) {
            this.datetime = datetime
            this.type = type
            this.units = units
        }
    }
    var date: String? = null
    var time: String? = null
    var datetime: String? = null
    var type: String? = null
    var units: String? = null
    val validUnits: String = "^(\\d[0-9]{1,3})$"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Realm.init(this)
        setContentView(R.layout.activity_injection)
        val editDate = findViewById<View>(R.id.datePicker)
        val editTime = findViewById<View>(R.id.timePicker)
        val cal = Calendar.getInstance()
        val dateSetListener = DatePickerDialog.OnDateSetListener { _, year, month, day ->
            cal.set(Calendar.YEAR, year)
            cal.set(Calendar.MONTH, month)
            cal.set(Calendar.DAY_OF_MONTH, day)
            Log.i("date", year.toString() + "-" + month + "-" + day)
            date = year.toString() + "-" + (month+1).toString() + "-" + day.toString()
            Log.i("verify", date)
        }
        editDate.setOnClickListener {
            DatePickerDialog(this@InjectionActivity, dateSetListener,
                    cal.get(Calendar.YEAR),
                    cal.get(Calendar.MONTH),
                    cal.get(Calendar.DAY_OF_MONTH)).show()
        }
        val timeSetListener = TimePickerDialog.OnTimeSetListener { view, hour, minute ->
            cal.set(Calendar.HOUR_OF_DAY, hour)
            cal.set(Calendar.MINUTE, minute)
            val format = "HH:MM"
            Log.i("time", hour.toString() + ":" + minute)
            time = hour.toString() + ":" + minute.toString()
            Log.i("verify", time)
        }
        editTime.setOnClickListener {
            TimePickerDialog(this@InjectionActivity, timeSetListener,
                    cal.get(Calendar.HOUR_OF_DAY),
                    cal.get(Calendar.MINUTE),
                    true).show()
        }
        saveButton.setOnClickListener {
            val type = injectionTypeInput.text.toString()
            val units = injectionUnitsInput.text.toString()
            var datetime = ""
            if (date != null && time != null) {
                datetime = date + " " + time
            }
            if (datetime != null) {
                Log.i("save", datetime)
            }
            if (type != null) {
                Log.i("save", type)
            }
            if (units != null && units.matches(validUnits.toRegex())) {
                Log.i("save", units)
            }
            else {
                Log.i("save", "fill in form")
            }
            Log.i("values", datetime + "\t" + type + "\t" + units)
            val path = filesDir.absolutePath+"/injections.json"
            Log.i("path", path)
            val gson = Gson()
            val injection = Injection(datetime, type, units)
            val towrite: String = gson.toJson(injection)
            val file = File(path)
            Log.i("file", file.toString())
            file.appendText(towrite)
            //file.writeText(towrite)
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
        cancelButton.setOnClickListener {
            val intent = Intent(this, AddActivity::class.java)
            startActivity(intent)
        }
    }
}
