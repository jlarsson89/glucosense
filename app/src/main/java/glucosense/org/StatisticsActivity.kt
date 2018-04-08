package glucosense.org

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import io.realm.Realm
import kotlinx.android.synthetic.main.activity_statistics.*
import java.net.URL
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.concurrent.Executors

class StatisticsActivity : AppCompatActivity() {
    private var injectionModel = InjectionModel()
    private var mealModel = MealModel()
    private var ingredientModel = IngredientModel()
    var realm = Realm.getDefaultInstance()
    val key: String = ""
    var food: String = ""
    var url = "https://api.nal.usda.gov/ndb/search/?format=json&q=$food&sort=n&max=25&offset=0&api_key=$key"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_statistics)
        val today = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val formatted = today.format(formatter)
        //val data = Executors.newSingleThreadExecutor().execute({
            //val json = URL(url).readText()
        //})
        injectionModel = InjectionModel()
        mealModel = MealModel()
        ingredientModel = IngredientModel()
        injTotalText.text = injectionModel.getInjections(realm).size.toString()
        injTodayText.text = injectionModel.getDayInjections(realm, formatted).size.toString()
        Log.i("units today", injectionModel.getDayUnits(realm, formatted))
        injUnitsTodayText.text = injectionModel.getDayUnits(realm, formatted)
        lastMealTimeText.text = mealModel.getMeals(realm).last()?._ID
        todayMealsCarbsText.text = mealModel.getMeals(realm).size.toString()
        food = "pasta"
        val result = URL(url)
        Log.i("result", result.toString())
    }
}
