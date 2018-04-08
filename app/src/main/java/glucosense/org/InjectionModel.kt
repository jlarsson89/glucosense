package glucosense.org

import android.util.Log
import io.realm.Realm
import io.realm.RealmResults

class InjectionModel : InjectionInterface {
    override fun addInjection(realm: Realm, injection: Injection): Boolean {
        try {
            realm.beginTransaction()
            realm.copyToRealmOrUpdate(injection)
            realm.commitTransaction()
            return true
        }
        catch (e: Exception) {
            Log.i("exception", e.toString())
            return false
        }
    }

    override fun delInjection(realm: Realm, _ID: String): Boolean {
        try {
            realm.beginTransaction()
            realm.where(Injection::class.java).equalTo("_ID", _ID).findFirst()?.deleteFromRealm()
            realm.commitTransaction()
            return true
        }
        catch (e: Exception) {
            Log.i("exception", e.toString())
            return false
        }
    }

    override fun editInjection(realm: Realm, injection: Injection): Boolean {
        try {
            realm.beginTransaction()
            realm.insertOrUpdate(injection)
            realm.commitTransaction()
            return true
        }
        catch (e: Exception) {
            Log.i("exception", e.toString())
            return false
        }
    }

    override fun getInjection(realm: Realm, _ID: String): Injection? {
        return realm.where(Injection::class.java).equalTo("_ID", _ID).findFirst()
    }

    override fun getInjections(realm: Realm): RealmResults<Injection> {
        return realm.where(Injection::class.java).findAll()
    }

    override fun getDayInjections(realm: Realm, _ID: String): RealmResults<Injection> {
        return realm.where(Injection::class.java).beginsWith("_ID", _ID).findAll()
    }

    override fun getDayUnits(realm: Realm, _ID: String): String {
        var total = 0
        val injections = realm.where(Injection::class.java).beginsWith("_ID", _ID).findAll()
        for (i in injections) {
            total += i.units.toInt()
        }
        return total.toString()
    }
}