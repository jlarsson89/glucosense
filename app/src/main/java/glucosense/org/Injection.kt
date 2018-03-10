package glucosense.org

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

/**
 * Created by johan on 10/03/18.
 */
open class Injection(
        @PrimaryKey open var _ID: String = "",
        open var type: String = "",
        open var units: String = ""
)
    : RealmObject() {
    fun copy(
            _ID: String = this._ID,
            type: String = this.type,
            units: String = this.units
    ) = Injection(_ID, type, units)
}