package glucosense.org

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

/**
 * Created by johan on 10/03/18.
 */
open class Glucose(
        @PrimaryKey open var _ID: String = "",
        open var value: String = ""
)
    : RealmObject() {
    fun copy(
            _ID: String = this._ID,
            value: String = this.value
    ) = Glucose(_ID, value)
}