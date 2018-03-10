package glucosense.org

import io.realm.Realm

/**
 * Created by johan on 10/03/18.
 */
interface GlucoseInterface {
    fun addGlucose(realm: Realm, injection: Injection): Boolean
    fun delGlucose(realm: Realm, _ID: String): Boolean
    fun editGlucose(realm: Realm, injection: Injection): Boolean
    fun getGlucose(realm: Realm, _ID: String): Injection
}