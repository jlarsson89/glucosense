package glucosense.org

import android.util.Log
import io.realm.Realm
import io.realm.RealmResults

class IngredientModel : IngredientInterface {
    override fun addIngredient(realm: Realm, ingredient: Ingredient): Boolean {
        try {
            realm.beginTransaction()
            realm.copyToRealmOrUpdate(ingredient)
            realm.commitTransaction()
            return true
        }
        catch (e: Exception) {
            return false
        }
    }

    override fun delIngredient(realm: Realm, _ID: String): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun editIngredient(realm: Realm, ingredient: Ingredient): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getIngredient(realm: Realm, _ID: String): Ingredient {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getIngredients(realm: Realm, parent: String): RealmResults<Ingredient> {
        return realm.where(Ingredient::class.java).equalTo("parent", parent).findAll()
    }
}