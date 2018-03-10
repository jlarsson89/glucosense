package glucosense.org

import io.realm.Realm

/**
 * Created by johan on 10/03/18.
 */
interface InjectionInterface {
    fun addInjection(realm: Realm, injection: Injection): Boolean
    fun delInjection(realm: Realm, _ID: String): Boolean
    fun editInjection(realm: Realm, injection: Injection): Boolean
    fun getInjection(realm: Realm, _ID: String): Injection
}