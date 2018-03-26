package glucosense.org

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import io.realm.Realm
import kotlinx.android.synthetic.main.activity_statistics.*
import java.util.*

class StatisticsActivity : AppCompatActivity() {
    private var injectionModel = InjectionModel()
    private var mealModel = MealModel()
    private var ingredientModel = IngredientModel()
    var realm = Realm.getDefaultInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_statistics)
        val cal = Calendar.getInstance()
        cal.set(2018, 0, 0) // shows incorrect date but correct format
        val currentTime = cal.get(Calendar.YEAR).toString() + "-" + cal.get(Calendar.MONTH + 1) + "-" + cal.get((Calendar.DAY_OF_MONTH))
        Log.i("time", (cal.get(Calendar.YEAR).toString()) + "-" + cal.get(Calendar.MONTH + 1).toString() + "-" + cal.get(Calendar.DAY_OF_MONTH))
        Log.i("currenttime", currentTime)
        injectionModel = InjectionModel()
        mealModel = MealModel()
        ingredientModel = IngredientModel()
        injTotalText.text = injectionModel.getInjections(realm).size.toString()
        lastMealTimeText.text = mealModel.getMeals(realm).last()?._ID
    }
}
