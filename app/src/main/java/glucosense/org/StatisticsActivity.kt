package glucosense.org

import android.content.Intent
import android.opengl.Visibility
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.View
import com.beust.klaxon.*
import com.squareup.moshi.KotlinJsonAdapterFactory
import com.squareup.moshi.Moshi
import io.realm.Realm
import kotlinx.android.synthetic.main.activity_statistics.*
import org.jetbrains.anko.custom.async
import java.io.BufferedReader
import java.io.File
import java.net.URL
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.concurrent.Executors

class StatisticsActivity : AppCompatActivity() {
    private var injectionModel = InjectionModel()
    private var mealModel = MealModel()
    private var ingredientModel = IngredientModel()
    var weight = "" // get proper value
    var realm = Realm.getDefaultInstance()
    val key: String = "NO01wqhp8hVdKJMgJRQqyu6syG9lwCUyBML6tmJE"

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
        if (injectionModel.getDayUnits(realm, formatted).toInt() == 0) {
            stats_injection.text = "You have not injected anything today."
        }
        else if (injectionModel.getDayUnits(realm, formatted).toInt() < (0.5 * weight.toInt())) {
            stats_injection.text = "You have not yet injected enough units today."
            injUnitsTodayText.setTextColor(Integer.parseUnsignedInt("ffffbb33",16))
        }
        else if (injectionModel.getDayUnits(realm, formatted).toInt() > (0.5 * weight.toInt()) &&
                injectionModel.getDayUnits(realm, formatted).toInt() < (0.8 * weight.toInt())) {
            stats_injection.text = "You are injecting within your range."
            injUnitsTodayText.setTextColor(Integer.parseUnsignedInt("ff669900",16))
        }
        else {
            stats_injection.text = "You have injected over your recommended limit based on your weight."
            injUnitsTodayText.setTextColor(Integer.parseUnsignedInt("ffcc0000", 16))
        }
        injUnitsTotalText.text = injectionModel.getTotalUnits(realm)
        if (mealModel.getMeals(realm).isNotEmpty()) {
            lastMealTimeText.text = mealModel.getMeals(realm).last()?._ID
            todayMealsCountText.text = mealModel.getDayMeals(realm, formatted).size.toString()
        }
        if (mealModel.getDayMeals(realm, formatted).size == 0) {
            stats_meal.text = "You have not eaten anything yet today."
        }
        val food = "01009"
        val data = "https://api.nal.usda.gov/ndb/nutrients/?format=json&api_key=$key&nutrients=205&nutrients=204&nutrients=208&nutrients=269&ndbno=$food"
        val executor = Executors.newScheduledThreadPool(4)
        async(executor) {
            val json = URL(data).readText()
            val moshi = Moshi.Builder()
                    .add(DefaultOnDataMismatchAdapter.newFactory(Report::class.java, null))
                    .add(DefaultOnDataMismatchAdapter.newFactory(Food::class.java, null))
                    .add(DefaultOnDataMismatchAdapter.newFactory(Nutrient::class.java, null))
                    .add(KotlinJsonAdapterFactory())
                    .build()
            println(json)
            //class Food (val ndbno: String, val name: String)
            class Person (val name: String, var age: Int = 23)
            val str = """{
                "name": "John Smith",
                }"""
            val result = Klaxon().parse<Person>(str)
            println(result?.name)
            val klaxon = Klaxon().parse<Response>(json)
            println(klaxon.toString())
            println(moshi)
        }
        backButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }
}
