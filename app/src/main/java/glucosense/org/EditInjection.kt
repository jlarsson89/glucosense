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
    private var id: String = ""
    private var type: String = ""
    private var units: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_injection)
        injectionModel = InjectionModel()
        setSupportActionBar(toolbar)
        val input = intent.extras.get("")
        val inj = injectionModel.getInjection(realm, input.toString())
        id = intent.extras.get("").toString()
        typeInput.hint = inj?.type
        unitsInput.hint = inj?.units
        editButton.setOnClickListener{
            if (typeInput.text.isNullOrBlank()) {
                type = inj!!.type
            }
            else {
                type = typeInput.text.toString()
            }
            if (unitsInput.text.isNullOrBlank()) {
                units = inj!!.units
            }
            else {
                units = unitsInput.text.toString()
            }
            val newInj = glucosense.org.Injection(
                    _ID = id,
                    type = type,
                    units = units
            )
            injectionModel.addInjection(realm, newInj)
            val intent = Intent(this, HistoryActivity::class.java)
            startActivity(intent)
        }
        cancelButton.setOnClickListener {
            val intent = Intent(this, HistoryActivity::class.java)
            startActivity(intent)
        }
    }
}
