package glucosense.org

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.beust.klaxon.JsonReader
import com.beust.klaxon.Klaxon
import com.squareup.moshi.Moshi
import io.realm.Realm
import kotlinx.android.synthetic.main.activity_statistics.*
import org.jetbrains.anko.custom.async
import org.json.JSONArray
import org.json.JSONObject
import java.io.StringReader
import java.net.URL
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.concurrent.Executors

class StatisticsActivity : AppCompatActivity() {
    private var injectionModel = InjectionModel()
    private var mealModel = MealModel()
    private var ingredientModel = IngredientModel()
    var realm = Realm.getDefaultInstance()
    val key: String = "NO01wqhp8hVdKJMgJRQqyu6syG9lwCUyBML6tmJE"
    //var url = "https://api.nal.usda.gov/ndb/search/?format=json&q=$food&sort=n&max=25&offset=0&api_key=$key"
    data class Nutrient(val name: String, val value: String)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_statistics)
        val today = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val formatted = today.format(formatter)
        injectionModel = InjectionModel()
        mealModel = MealModel()
        ingredientModel = IngredientModel()
        injTotalText.text = injectionModel.getInjections(realm).size.toString()
        injTodayText.text = injectionModel.getDayInjections(realm, formatted).size.toString()
        injUnitsTodayText.text = injectionModel.getDayUnits(realm, formatted)
        injUnitsTotalText.text = injectionModel.getTotalUnits(realm)
        lastMealTimeText.text = mealModel.getMeals(realm).last()?._ID
        todayMealsCarbsText.text = mealModel.getMeals(realm).size.toString()
        var food = "01009"
        Log.i("food", food)
        Log.i("key", key)
        val data = "https://api.nal.usda.gov/ndb/nutrients/?format=json&api_key=$key&nutrients=205&nutrients=204&nutrients=208&nutrients=269&ndbno=$food"
        foodid.text = food
        foodurl.text = data
        val executor = Executors.newScheduledThreadPool(4)
        async(executor) {
            val result2 = URL(data).readText()
            Log.i("result2", result2)
            parseItem(result2)
        }
        backButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }

    fun parseItem(json: String) {
        val moshi = Moshi.Builder().build()
        val klaxon = Klaxon()
        //val jsonAdapter = moshi.adapter<Array<Report>>(Array<Report>::class.java)
        val jsonObj = JSONObject(json)
        JsonReader(StringReader(jsonObj.toString())).use { reader ->
            val results = arrayListOf<Nutrient>()
            reader.beginArray {
                while (reader.hasNext()) {
                    val nutrient = klaxon.parse<Nutrient>(reader)
                    Log.i("nutrient", nutrient.toString())
                }
            }
        }
        Log.i("jsonobj", jsonObj.toString())
        //Log.i("jsonadapter", jsonAdapter.toString())
        val ingredients: Array<Report>? = null
        val report: JSONArray = jsonObj.getJSONArray("report")
        val foods: JSONArray = jsonObj.getJSONArray("foods")
        for (i in 0 until report.length()) {
            val item = report.getJSONObject(i)
            Log.i("report", item.toString())
        }
        for (i in 0 until foods.length()) {
            val item = foods.getJSONObject(i)
            Log.i("foods", item.toString())
        }
        /*try {
            //ingredients = jsonAdapter.fromJson(json)
        }
        catch (e: Exception) {
            e.stackTrace
        }
        Log.i("ingredients", ingredients?.size.toString())
        if (ingredients == null) {
            Log.i("ingredients", "null")
        }
        for (i in ingredients!!) {
            Log.i("i", i.toString())
        }*/
    }
}
