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
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_meal.*
import java.io.File
import java.util.*

class MealActivity : AppCompatActivity() {

    class Meal {
        var datetime: String? = null
        var ingredients: Pair<String, String>? = null
        constructor(datetime: String, ingredient: Pair<String, String>) {
            this.datetime = datetime
            this.ingredients = ingredient
        }
    }
    var date: String? = null
    var time: String? = null
    var datetime: String? = null
    var ingredient1_name: String? = null
    var ingredient1_qty: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_meal)
        //val ingredient1name = findViewById<View>(R.id.ingredient_1_name) as EditText
        //ingredient1_name = ingredient1name.text.toString()
        //val ingredient1qty = findViewById<View>(R.id.ingredient_1_quantity) as EditText
        //ingredient1_qty = ingredient1qty.text.toString()
        //val ingredient2row = findViewById<View>(R.id.ingredient_2_row)
        //if (ingredient1_name != "" && ingredient1_qty != "") {
        //ingredient2row.visibility = View.VISIBLE
        //}
        val table = TableLayout(this)
        contentLayout.addView(table)
        val row1 = TableRow(this)
        val label1 = TextView(this)
        val input1 = EditText(this)
        val row2 = TableRow(this)
        val label2 = TextView(this)
        val input2 = EditText(this)
        label1.setText("blah")
        input1.hint = "quantity"
        row1.addView(label1)
        row1.addView(input1)
        if (input1.text.isNotEmpty()) {
            label2.setText("meh")
            input2.hint = "qty"
            row2.addView(label2)
            row2.addView(input2)
            table.addView(row2)
        }
        table.addView(row1)

        backButton.setOnClickListener {
                //Log.i("values", ingredient1_name)
                //Log.i("values", ingredient1_qty)
            val intent = Intent(this, AddActivity::class.java)
            startActivity(intent)
        }
            /*val editDate = findViewById<View>(R.id.datePicker)
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
            }*/
            /*saveButton.setOnClickListener {
                var type = injectionTypeInput.text.toString()
                var units = injectionUnitsInput.text.toString()
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
                if (units != null) {
                    Log.i("save", units)
                }
                else {
                    Log.i("save", "fill in form")
                }
                Log.i("values", datetime + "\t" + type + "\t" + units)
                val path = filesDir.absolutePath+"/meals.json"
                Log.i("path", path)
                val gson = Gson()
                val injection = Meal(datetime, ingredients)
                var towrite: String = gson.toJson(injection)
                val file = File(path)
                Log.i("file", file.toString())
                file.writeText(towrite)
            }
            cancelButton.setOnClickListener {
                val intent = Intent(this, AddActivity::class.java)
                startActivity(intent)
            }*/
    }
}
