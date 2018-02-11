package glucosense.org

import android.app.DatePickerDialog
import android.app.ProgressDialog.show
import android.app.TimePickerDialog
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import com.beust.klaxon.JsonObject
import com.beust.klaxon.Klaxon
import com.beust.klaxon.Parser
import kotlinx.android.synthetic.main.activity_injection.*
import java.text.SimpleDateFormat
import java.util.*

class InjectionActivity : AppCompatActivity() {
    class Injection(val datetime: String, val type: String, val units: String)
    var date: String? = null
    var time: String? = null
    var datetime: String? = null
    var type: String? = null
    var units: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
            type = injectionTypeInput.text.toString()
            units = injectionUnitsInput.text.toString()
            if (date != null && time != null) {
                datetime = date + " " + time
            }
            if (datetime != null) {
                Log.i("save", datetime)
            }
            if (type != null) {
                Log.i("save", type)
            }
            if (units != null) {
                Log.i("save", units)
            }
            else {
                Log.i("save", "fill in form")
            }
            Log.i("values", datetime + "\t" + type + "\t" + units)
            val parser = Parser()
            val stringBuilder = StringBuilder("{\"datetime\":\"$datetime\", \"type\":\"$type\",\"units\":\"$units\"}")
            val json: JsonObject = parser.parse(stringBuilder) as JsonObject
            Log.i("json", "${json.string("datetime")}, ${json.string("type")}, ${json.string("units")}")
        }
        cancelButton.setOnClickListener {
            val intent = Intent(this, AddActivity::class.java)
            startActivity(intent)
        }
    }
}
