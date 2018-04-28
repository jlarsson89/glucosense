package glucosense.org

import io.realm.Realm

class GlucoseModel : GlucoseInterface {
    override fun addGlucose(realm: Realm, glucose: Glucose): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun delGlucose(realm: Realm, _ID: String): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun editGlucose(realm: Realm, glucose: Glucose): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getGlucose(realm: Realm, _ID: String): Injection {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}