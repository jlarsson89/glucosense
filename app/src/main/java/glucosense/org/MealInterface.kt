package glucosense.org

import io.realm.Realm
import io.realm.RealmResults

interface MealInterface {
    fun addMeal(realm: Realm, meal: Meal): Boolean
    fun delMeal(realm: Realm, _ID: String): Boolean
    fun editMeal(realm: Realm, meal: Meal): Boolean
    fun getMeal(realm: Realm, _ID: String): Meal
    fun getMeals(realm: Realm): RealmResults<Meal>
}