package glucosense.org

import io.realm.RealmObject
import io.realm.RealmResults
import io.realm.annotations.LinkingObjects
import io.realm.annotations.PrimaryKey

open class Ingredient(
        @PrimaryKey open var _name: String = "",
        open var parent: String = "",
        open var quantity: String = ""
)
    : RealmObject() {
    fun copy(
            _name: String = this._name,
            parent: String = this.parent,
            quantity: String = this.quantity
    ) = Ingredient(_name, parent, quantity)
}