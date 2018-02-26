package glucosense.org

import android.widget.TextView
import android.view.ViewGroup
import android.view.LayoutInflater
import android.app.Activity
//import sun.font.TextRecord
import android.nfc.NdefRecord
import android.nfc.NdefMessage
import android.view.View


/**
 * Created by johan on 26/02/18.
 */
object NdefMessageParser {

    /** Parse an NdefMessage  */
    fun parse(message: NdefMessage): List<ParsedNdefRecord> {
        return getRecords(message.records)
    }

    fun getRecords(records: Array<NdefRecord>): List<ParsedNdefRecord> {
        val elements = ArrayList<ParsedNdefRecord>()
        for (record in records) {
            if (UriRecord.isUri(record)) {
                elements.add(UriRecord.parse(record))
            } else if (TextRecord.isText(record)) {
                elements.add(TextRecord.parse(record))
            } else if (SmartPoster.isPoster(record)) {
                elements.add(SmartPoster.parse(record))
            } else {
                //elements.add(object : ParsedNdefRecord() {
                    /*override fun getView(activity: Activity, inflater: LayoutInflater, parent: ViewGroup, offset: Int): View {
                        val text = inflater.inflate(R.layout.tag_text, parent, false) as TextView
                        text.text = String(record.payload)
                        return text
                    }*/

                //})
            }
        }
        return elements
    }
}// Utility class