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
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add)
        nextButton.setOnClickListener {
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
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }
}
