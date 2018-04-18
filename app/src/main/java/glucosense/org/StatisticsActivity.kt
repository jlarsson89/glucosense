package glucosense.org

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.beust.klaxon.*
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.squareup.moshi.Moshi
import io.realm.Realm
import kotlinx.android.synthetic.main.activity_statistics.*
import org.jetbrains.anko.custom.async
import org.json.JSONArray
import org.json.JSONObject
import java.io.Reader
import java.io.StringReader
import java.net.URL
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.concurrent.Executors
import java.util.logging.Level.parse
import java.util.regex.Pattern

class StatisticsActivity : AppCompatActivity() {
    private var injectionModel = InjectionModel()
    private var mealModel = MealModel()
    private var ingredientModel = IngredientModel()
    val weight = R.string.pref_title_weight.toString()
    var realm = Realm.getDefaultInstance()
    val key: String = "NO01wqhp8hVdKJMgJRQqyu6syG9lwCUyBML6tmJE"
    //var url = "https://api.nal.usda.gov/ndb/search/?format=json&q=$food&sort=n&max=25&offset=0&api_key=$key"
    data class MEntity<T>(
            var report: T? = null
    )

    data class Food(
            var name: String? = null,
            var ndbno: String? = null,
            var nutrients: MEntity<Nutrient>? = null
    )

    data class Nutrient(
            var nutrient_id: String? = null,
            var nutrient: String? = null,
            var unit: String? = null,
            var value: String? = null,
            var gm: String?
    )
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
        /*JsonReader(StringReader(jsonObj.toString())).use { reader ->
            val results = arrayListOf<Nutrient>()
            reader.beginArray {
                while (reader.hasNext()) {
                    val nutrient = klaxon.parse<Nutrient>(reader)
                    Log.i("nutrient", nutrient.toString())
                }
            }
        }*/
        val gson: Gson = Gson()
        val jsonString: String = gson.toJson(jsonObj)
        gson.fromJson<MEntity<Food>>(json, object : TypeToken<MEntity<Food>>() {}.type)
        // try arraylist
        // implement warnings if user injects more than recommended in a day, eats over a certain amount etc
        // user should inject roughly 1 units per 10 to 15 grams of carbs
        val userMEntity = gson.fromJsonToGeneric<MEntity<Food>>(json)
        Log.i("test", userMEntity.toString())
        Log.i("gson", jsonString)
        //val report = gson?.fromJson(jsonString, Reports.Report::class.java)
        Log.i("jsonobj", jsonObj.names().toString())
        //Log.i("test", jsonObj.getString("nutrients"))
        //Log.i("report", report.toString())
        //val test: JsonArray<JsonObject> = parse(gson.toJson())
        /*val berkeley = test.filter {
            it.obj("nutrient_id")?.string("nutrient") == "Carbohydrate, by difference"
        }.map {
            it.string("last")
        }*/
        //Log.i("jsonadapter", jsonAdapter.toString())
        //val ingredients: Array<Report>? = null
        //val report: JSONArray = jsonObj.getJSONArray("report")
        //val foods: JSONArray = jsonObj.getJSONArray("foods")
        /*Log.i("blah", jsonObj.names().toString())
        val nutrients: JSONArray = jsonObj.getJSONArray("reports")
        Log.i("nutrients", nutrients.toString())
        for (i in 0 until nutrients.length()) {
            Log.i(i.toString(), nutrients.toString())
        }*/
        //Log.i("test", "$berkeley")
        /*for (i in 0 until report.length()) {
            val item = report.getJSONObject(i)
            Log.i("report", item.toString())
        }
        for (i in 0 until foods.length()) {
            val item = foods.getJSONObject(i)
            Log.i("foods", item.toString())
        }*/
        val pathMatcher = object : PathMatcher {
            override fun pathMatches(path: String) = Pattern.matches(".*report.*foods.*nutrient.*", path)

            override fun onMatch(path: String, value: Any) {
                println("Adding $path = $value")
            }
        }
        Klaxon().pathMatcher(pathMatcher).parseJsonObject(JsonReader(jsonObj.toString().reader()))
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
    inline fun <reified T> Gson.fromJsonToGeneric(json: String): T {
        return fromJson(json, object : TypeToken<T>() {}.type)
    }
}
