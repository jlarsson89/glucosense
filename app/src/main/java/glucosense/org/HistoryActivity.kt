package glucosense.org

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import com.google.gson.Gson
import io.realm.Realm
import kotlinx.android.synthetic.main.activity_history.*
import java.io.BufferedReader
import java.io.File

class HistoryActivity : AppCompatActivity() {
    private var injectionModel = InjectionModel()
    private var mealModel = MealModel()
    var realm = Realm.getDefaultInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)
        injectionModel = InjectionModel()
        val injResults = injectionModel.getInjections(realm).sort("_ID").asReversed()
        if (injResults.isNotEmpty()) {
            injResults.forEach { result ->
                val layout = LinearLayout(this)
                layout.orientation = LinearLayout.HORIZONTAL
                val delButton = Button(this)
                delButton.text = "Delete"
                val editButton = Button(this)
                editButton.text = "Edit"
                val label = TextView(this)
                label.setText(result._ID + " " + result.type + " " + result.units)
                layout.addView(delButton)
                layout.addView(editButton)
                layout.addView(label)
                injectionsList.addView(layout)
                delButton.setOnClickListener {
                    injectionModel.delInjection(realm, result._ID)
                    val intent = Intent(this, HistoryActivity::class.java)
                    startActivity(intent)
                }
                editButton.setOnClickListener {
                    val intent = Intent(this, EditInjection::class.java)
                    intent.putExtra("", result._ID)
                    startActivity(intent)
                }
            }
        }
        mealModel = MealModel()
        /*var mealResults = mealModel.getMeals(realm)
        mealResults.forEach { result ->
            Log.i("result", result._ID)
        }*/
        backButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }
}
