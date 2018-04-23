package glucosense.org

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import com.beust.klaxon.*
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.squareup.moshi.Moshi
import io.realm.Realm
import kotlinx.android.synthetic.main.activity_statistics.*
import org.jetbrains.anko.custom.async
import org.jetbrains.anko.defaultSharedPreferences
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
import android.content.SharedPreferences
import com.github.kittinunf.forge.Forge
import com.github.kittinunf.forge.core.*
import com.github.kittinunf.forge.util.create
import com.google.gson.GsonBuilder


class StatisticsActivity : AppCompatActivity() {
    private var injectionModel = InjectionModel()
    private var mealModel = MealModel()
    private var ingredientModel = IngredientModel()
    var weight = "" // get proper value
    var realm = Realm.getDefaultInstance()
    val key: String = "NO01wqhp8hVdKJMgJRQqyu6syG9lwCUyBML6tmJE"
    //var url = "https://api.nal.usda.gov/ndb/search/?format=json&q=$food&sort=n&max=25&offset=0&api_key=$key"
    data class MEntity<T>(
            var report: T? = null
    )

    data class Food(
            var name: String,
            var ndbno: String,
            var nutrients: List<Nutrient>
    )

    data class Nutrient(
            var nutrient_id: String,
            var nutrient: String,
            var unit: String,
            var value: String,
            var gm: String
    )
    /*data class User(val id: Int,
                    val name: String,
                    val age: Int,
                    val email: String?,
                    val friends: List<User>,
                    val dogs: List<Dog>?)

    data class Dog(val name: String, val breed: String, val male: Boolean)

    fun userDeserializer(json: JSON) =
            ::User.create.
                    map(json at "id").
                    apply(json at "name").
                    apply(json at "age").
                    apply(json maybeAt "email").
                    apply(json.list("friends", ::userDeserializer)).  //userDeserializer is a function, use :: as a function reference
                    apply(json.maybeList("dogs", dogDeserializer))  //dogDeserializer is a lambda, use it directly

    val dogDeserializer = { json: JSON ->
        ::Dog.create.
                map(json at "name").
                apply(json at "breed").
                apply(json at "is_male")
    }*/
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_statistics)
        val SP = PreferenceManager.getDefaultSharedPreferences(baseContext)
        weight = SP.getString("user_weight", "NA")
        val today = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val formatted = today.format(formatter)
        injectionModel = InjectionModel()
        mealModel = MealModel()
        ingredientModel = IngredientModel()
        injTotalText.text = injectionModel.getInjections(realm).size.toString()
        injTodayText.text = injectionModel.getDayInjections(realm, formatted).size.toString()
        injUnitsTodayText.text = injectionModel.getDayUnits(realm, formatted)
        if (injectionModel.getDayUnits(realm, formatted).toInt() < (0.5 * weight.toInt())) {
            Log.i("today", injectionModel.getDayUnits(realm, formatted))
            Log.i("weight", weight)
            Log.i("weight", "you're injecting within your range")
            stats_injection.text = "You have not yet injected enough units today."
            injUnitsTodayText.setTextColor(Integer.parseUnsignedInt("ffffbb33",16))
        }
        if (injectionModel.getDayUnits(realm, formatted).toInt() > (0.5 * weight.toInt()) &&
                injectionModel.getDayUnits(realm, formatted).toInt() < (0.8 * weight.toInt())) {
            Log.i("today", injectionModel.getDayUnits(realm, formatted))
            Log.i("weight", weight)
            Log.i("weight", "you're injecting within your range")
            stats_injection.text = "You are injecting within your range."
            injUnitsTodayText.setTextColor(Integer.parseUnsignedInt("ff669900",16))
        }
        else {
            Log.i("today", injectionModel.getDayUnits(realm, formatted))
            Log.i("weight", weight)
            Log.i("weight", "you're injecting too much")
            stats_injection.text = "You have injected over your recommended limit based on your weight."
            injUnitsTodayText.setTextColor(Integer.parseUnsignedInt("ffcc0000", 16))
        }
        injUnitsTotalText.text = injectionModel.getTotalUnits(realm)
        lastMealTimeText.text = mealModel.getMeals(realm).last()?._ID
        todayMealsCountText.text = mealModel.getDayMeals(realm, formatted).size.toString()
        if (mealModel.getDayMeals(realm, formatted).size == 0) {
            stats_meal.text = "You have not eaten anything yet today."
        }
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
            //parseItem(result2)
            val gson: Gson = Gson()
            val report1: Report = gson.fromJson(result2, Report::class.java)
            val food1: Foods = gson.fromJson(result2, Foods::class.java)
            val nutrient1: Nutrients = gson.fromJson(result2, Nutrients::class.java)
            Log.i("report1", report1.toString())
            println(report1)
            Log.i("food1", food1.toString())
            println(food1)
            Log.i("nutrients1", nutrient1.toString())
            println(nutrient1)
            val json = """{"name": "Kolineer", "age": "26", "messages" : ["Master Kotlin","At Kolination"]}"""
            val person1 : Person = gson.fromJson(json, Person::class.java)
            println(person1)
            val jsonList = """[{"name": "Kolineer", "age": "26", "messages" : ["Master Kotlin","At Kolination"]},
			{"name":"Kolineer Master","age":30,"messages":["I am Kotlin Master","still learning Kotlin at Kotlination"]}]"""
            val gson1 = GsonBuilder().setPrettyPrinting().create()

            println("=== List from JSON ===")
            var personList: List<Person> = gson1.fromJson(jsonList, object : TypeToken<List<Person>>() {}.type)
            personList.forEach { println(it) }

            println("=== List to JSON ===")
            val jsonPersonList: String = gson1.toJson(personList)
            println(jsonPersonList)
            var personMap: Map<String, Any> = gson1.fromJson(json, object : TypeToken<Map<String, Any>>() {}.type)
            personMap.forEach { println(it) }

            println("=== Map to JSON ===")
            val jsonPersonMap: String = gson1.toJson(personMap)
            println(jsonPersonMap)
            val gson2 = GsonBuilder().setPrettyPrinting().create()
            var nutrientlist: List<Report> = gson2.fromJson(result2, object : TypeToken<List<Report>>() {}.type)
            Log.i("list", nutrientlist.size.toString())
            nutrientlist.forEach { println(it)}
            /*
            val jsonObj = JSONObject(result2)
            val jsonString: String = gson.toJson(jsonObj)
            gson.fromJson<MEntity<Foods>>(result2, object : TypeToken<MEntity<Foods>>() {}.type)
            val userMEntity = gson.fromJsonToGeneric<MEntity<Foods>>(result2)
            Log.i("test", userMEntity.toString())
            Log.i("gson", jsonString)
            Log.i("jsonobj", jsonObj.names().toString())*/
            /*val result = Forge.modelFromJson(result2, ::foodDeserializer)

            when (result) {
                DeserializedResult.Success -> {
                    val food = result.value
                    //success, do something with user
                }

                DeserializedResult.Failure -> {
                    val error = result.error
                    //failure, do something with error
                }
            }*/
        }
        backButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }

    /*fun foodDeserializer(json: String) =
            ::Foods.create.
                    map(json at "ndbno").
                    apply(json at "name").
                    apply(json.list("nutrients", ::nutrientDeserializer))

    val nutrientDeserializer = { json: JSON ->
        ::Nutrient.create.map(json at "nutrient_id").
                apply(json at "nutrient").
                apply(json at "unit").
                apply(json at "value").
                apply(json at "gm")
    }*/

    /*fun parseItem(json: String) {
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
        gson.fromJson<MEntity<Foods>>(json, object : TypeToken<MEntity<Foods>>() {}.type)
        // try arraylist
        // implement warnings if user injects more than recommended in a day, eats over a certain amount etc
        // user should inject roughly 1 units per 10 to 15 grams of carbs
        val userMEntity = gson.fromJsonToGeneric<MEntity<Foods>>(json)
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
    }*/
}
