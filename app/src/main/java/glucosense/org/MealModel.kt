package glucosense.org

import io.realm.Realm
import io.realm.RealmResults

class MealModel : MealInterface {
    override fun addMeal(realm: Realm, meal: Meal): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun delMeal(realm: Realm, _ID: String): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun editMeal(realm: Realm, meal: Meal): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getMeal(realm: Realm, _ID: String): Meal {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getMeals(realm: Realm): RealmResults<Meal> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}