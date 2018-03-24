package glucosense.org

import io.realm.Realm
import io.realm.RealmList
import io.realm.RealmResults

interface IngredientInterface {
    fun addIngredient(realm: Realm, ingredient: Ingredient): Boolean
    fun delIngredient(realm: Realm, _ID: String): Boolean
    fun editIngredient(realm: Realm, ingredient: Ingredient): Boolean
    fun getIngredient(realm: Realm, _ID: String): Ingredient
    fun getIngredients(realm: Realm, parent: String): RealmResults<Ingredient>
}