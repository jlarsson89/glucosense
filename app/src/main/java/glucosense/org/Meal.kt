package glucosense.org

import io.realm.RealmList
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class Meal(
        @PrimaryKey open var _ID: String = "",
        open var ingredients: RealmList<Ingredient> = RealmList()
)
    : RealmObject() {
    fun copy(
            _ID: String = this._ID,
            ingredients: RealmList<Ingredient> = this.ingredients
    ) = Meal(_ID, ingredients)
}