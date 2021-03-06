package glucosense.org

import android.util.Log
import io.realm.Realm
import io.realm.RealmResults

class MealModel : MealInterface {
    override fun addMeal(realm: Realm, meal: Meal): Boolean {
        try {
            realm.beginTransaction()
            realm.copyToRealmOrUpdate(meal)
            realm.commitTransaction()
            return true
        }
        catch (e: Exception) {
            return false
        }
    }

    override fun delMeal(realm: Realm, _ID: String): Boolean {
        try {
            realm.beginTransaction()
            realm.where(Meal::class.java).equalTo("_ID", _ID).findFirst()?.deleteFromRealm()
            realm.commitTransaction()
            return true
        }
        catch (e: Exception) {
            return false
        }    }

    override fun editMeal(realm: Realm, meal: Meal): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getMeal(realm: Realm, _ID: String): Meal? {
        return realm.where(Meal::class.java).equalTo("_ID", _ID).findFirst()
    }

    override fun getMeals(realm: Realm): RealmResults<Meal> {
        return realm.where(Meal::class.java).findAll()
    }

    override fun getDayMeals(realm: Realm, _ID: String): RealmResults<Meal> {
        return realm.where(Meal::class.java).beginsWith("_ID", _ID).findAll()
    }
}