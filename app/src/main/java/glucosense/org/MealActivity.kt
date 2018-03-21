package glucosense.org

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.opengl.Visibility
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import io.realm.Realm
import io.realm.RealmConfiguration
import kotlinx.android.synthetic.main.activity_meal.*
import java.util.*

class MealActivity : AppCompatActivity() {
    var mealModel = MealModel()
    var realm = Realm.getDefaultInstance()
    var date: String? = null
    var time: String? = null
    private val validDatetime: String = "([12]\\d{3}-(0?[1-9]|1[0-2])-(0[1-9]|[12]\\d|3[01])) ([0-9]|0?[0-9]|1[0-9]|2[0-3]):[0-5]?[0-9]\$"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_meal)
        RealmConfiguration.Builder().deleteRealmIfMigrationNeeded()
        Realm.init(applicationContext)
        val realm = Realm.getDefaultInstance()
        mealModel = MealModel()
        val ing1Name = findViewById<EditText>(R.id.ingredient1Name)
        val ing1Qty = findViewById<EditText>(R.id.ingredient1Qty)
        val ing2Row = findViewById<LinearLayout>(R.id.ingredient2Row)
        refreshButton.setOnClickListener {
            if (ing1Name.text.toString().isNotEmpty() && ing1Qty.text.toString().isNotEmpty()) {
                ing2Row.visibility = View.VISIBLE
            }
        }
        backButton.setOnClickListener {
            val intent = Intent(this, AddActivity::class.java)
            startActivity(intent)
        }
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
            DatePickerDialog(this@MealActivity, dateSetListener,
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
            TimePickerDialog(this@MealActivity, timeSetListener,
                    cal.get(Calendar.HOUR_OF_DAY),
                    cal.get(Calendar.MINUTE),
                    true).show()
        }
        saveButton.setOnClickListener {
            val datetime = date + " " + time
            if (datetime.matches(validDatetime.toRegex())) {
                val meal = glucosense.org.Meal(
                        _ID = datetime
                )
                mealModel.addMeal(realm, meal)
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }
        }
    }
}
