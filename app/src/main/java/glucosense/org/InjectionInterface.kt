package glucosense.org

import io.realm.Realm
import io.realm.RealmResults

interface InjectionInterface {
    fun addInjection(realm: Realm, injection: Injection): Boolean
    fun delInjection(realm: Realm, _ID: String): Boolean
    fun editInjection(realm: Realm, injection: Injection): Boolean
    fun getInjection(realm: Realm, _ID: String): Injection?
    fun getInjections(realm: Realm): RealmResults<Injection>
}