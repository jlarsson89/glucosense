package glucosense.org

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import io.realm.Realm

import kotlinx.android.synthetic.main.activity_edit_injection.*

class EditInjection : AppCompatActivity() {
    private var injectionModel = InjectionModel()
    var realm = Realm.getDefaultInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_injection)
        injectionModel = InjectionModel()
        setSupportActionBar(toolbar)
        val input = intent.extras.get("")
        var inj = injectionModel.getInjection(realm, input.toString())
        typeInput.hint = inj?.type
        unitsInput.hint = inj?.units
        Log.i("time", inj?._ID)
        Log.i("type", inj?.type)
        Log.i("units", inj?.units)
        editButton.setOnClickListener{

        }
        cancelButton.setOnClickListener {
            val intent = Intent(this, HistoryActivity::class.java)
            startActivity(intent)
        }
    }
}
