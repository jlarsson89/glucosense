package glucosense.org

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import io.realm.Realm
import io.realm.RealmConfiguration
import kotlinx.android.synthetic.main.activity_injection.*
import java.util.*

class InjectionActivity : AppCompatActivity() {
    private var date: String? = null
    private var time: String? = null
    private val validUnits: String = "^(\\d[0-9]{0,2})$"
    private val validDatetime: String = "([12]\\d{3}-(0?[1-9]|1[0-2])-(0[1-9]|[12]\\d|3[01])) ([0-9]|0?[0-9]|1[0-9]|2[0-3]):[0-5]?[0-9]\$"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_injection)
        RealmConfiguration.Builder().deleteRealmIfMigrationNeeded()
        Realm.init(applicationContext)
        val realm = Realm.getDefaultInstance()
        val injectionModel = InjectionModel()
        val editDate = findViewById<View>(R.id.datePicker)
        val editTime = findViewById<View>(R.id.timePicker)
        val cal = Calendar.getInstance()
        val dateSetListener = DatePickerDialog.OnDateSetListener { _, year, month, day ->
            cal.set(Calendar.YEAR, year)
            cal.set(Calendar.MONTH, month)
            cal.set(Calendar.DAY_OF_MONTH, day)
            date = year.toString() + "-" + (month+1).toString() + "-" + day.toString()
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
            time = hour.toString() + ":" + minute.toString()
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
            val datetime = date + " " + time
            if (type.isNotEmpty() && units.isNotEmpty() && units.matches(validUnits.toRegex()) && datetime.matches(validDatetime.toRegex())) {
                val inj = glucosense.org.Injection(
                        _ID = datetime,
                        type = type,
                        units = units
                )
                injectionModel.addInjection(realm, inj)
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }
        }
        cancelButton.setOnClickListener {
            val intent = Intent(this, AddActivity::class.java)
            startActivity(intent)
        }
    }
}
