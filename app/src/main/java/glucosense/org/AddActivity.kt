package glucosense.org

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.SpinnerAdapter
import kotlinx.android.synthetic.main.activity_add.*

class AddActivity : AppCompatActivity() {
    var choice: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add)
        /*spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }
        }*/
        nextButton.setOnClickListener {
            Log.i("press", "next")
            if (spinner.selectedItem.toString().equals("Injection")) {
                val intent = Intent(this, InjectionActivity::class.java)
                startActivity(intent)
            }
            if (spinner.selectedItem.toString().equals("Meal")) {
                val intent = Intent(this, MealActivity::class.java)
                startActivity(intent)
            }
        }
        backButton.setOnClickListener {
            Log.i("press", "back")
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }
}
