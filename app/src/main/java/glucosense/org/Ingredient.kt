package glucosense.org

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class Ingredient(
        @PrimaryKey open var _name: String = "",
        open var quantity: String = ""
)
    : RealmObject() {
    fun copy(
            _name: String = this._name,
            quantity: String = this.quantity
    ) = Injection(_name, quantity)
}