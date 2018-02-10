package glucosense.org

import android.app.DatePickerDialog
import android.app.ProgressDialog.show
import android.app.TimePickerDialog
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_injection.*
import java.text.SimpleDateFormat
import java.util.*

class InjectionActivity : AppCompatActivity() {
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
            if (date != null && time != null) {
                datetime = date + " " + time
            }
            if (datetime != null) {
                Log.i("save", datetime)
            }
            else {
                Log.i("save", "fill in form")
            }
        }
    }
}
