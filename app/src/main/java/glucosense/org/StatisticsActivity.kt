package glucosense.org

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.gson.Gson
import io.realm.Realm
import kotlinx.android.synthetic.main.activity_statistics.*
import java.net.URL
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class StatisticsActivity : AppCompatActivity() {
    private var injectionModel = InjectionModel()
    private var mealModel = MealModel()
    private var ingredientModel = IngredientModel()
    var realm = Realm.getDefaultInstance()
    val key: String = "NO01wqhp8hVdKJMgJRQqyu6syG9lwCUyBML6tmJE"
    //var url = "https://api.nal.usda.gov/ndb/search/?format=json&q=$food&sort=n&max=25&offset=0&api_key=$key"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_statistics)
        val today = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val formatted = today.format(formatter)
        var food: String = ""
        //val data = Executors.newSingleThreadExecutor().execute({
            //val json = URL(url).readText()
        //})
        injectionModel = InjectionModel()
        mealModel = MealModel()
        ingredientModel = IngredientModel()
        injTotalText.text = injectionModel.getInjections(realm).size.toString()
        injTodayText.text = injectionModel.getDayInjections(realm, formatted).size.toString()
        injUnitsTodayText.text = injectionModel.getDayUnits(realm, formatted)
        injUnitsTotalText.text = injectionModel.getTotalUnits(realm)
        lastMealTimeText.text = mealModel.getMeals(realm).last()?._ID
        todayMealsCarbsText.text = mealModel.getMeals(realm).size.toString()
       // food = "pasta"
        //val result = URL(url)
        food = "01009"
        Log.i("food", food)
        Log.i("key", key)
        val data = "https://api.nal.usda.gov/ndb/nutrients/?format=json&api_key=$key&nutrients=205&nutrients=204&nutrients=208&nutrients=269&ndbno=$food"
        val result1 = URL(data).query
        //Log.i("result", result.toString())
        Log.i("result1", result1.toString())
        foodid.text = food
        foodurl.text = data
        var gson = Gson()
        //val url = URL(data)
        /*val conn = url.openConnection() as HttpURLConnection
        conn.requestMethod = "GET"
        // read the response
        val in1 = BufferedInputStream(conn.inputStream)
        val response = convertStreamToString(in1)
        Log.i("data", response)*/
        backButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }
}
