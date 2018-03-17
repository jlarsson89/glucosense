package glucosense.org

import android.util.Log
import io.realm.Realm
import io.realm.RealmResults
import io.realm.kotlin.delete
import io.realm.kotlin.where

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
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getInjection(realm: Realm, _ID: String): Injection? {
        return realm.where(Injection::class.java).equalTo("_ID", _ID).findFirst()
    }

    override fun getInjections(realm: Realm): RealmResults<Injection> {
        return realm.where(Injection::class.java).findAll()
    }
}