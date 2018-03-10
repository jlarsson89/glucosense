package glucosense.org

import io.realm.Realm

/**
 * Created by johan on 10/03/18.
 */
class InjectionModel : InjectionInterface {
    override fun addInjection(realm: Realm, injection: Injection): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun delInjection(realm: Realm, _ID: String): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun editInjection(realm: Realm, injection: Injection): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getInjection(realm: Realm, _ID: String): Injection {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}