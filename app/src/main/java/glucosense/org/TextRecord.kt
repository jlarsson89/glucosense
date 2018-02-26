package glucosense.org

import android.nfc.NdefRecord
import android.widget.TextView
import android.view.ViewGroup
import android.view.LayoutInflater
import android.app.Activity
import android.support.v4.util.Preconditions
import android.view.View
import android.widget.LinearLayout
import glucosense.org.R.id.text
import java.io.UnsupportedEncodingException
import java.util.*
import kotlin.experimental.and


/**
 * Created by johan on 26/02/18.
 */
class TextRecord private constructor(languageCode: String, text: String) : ParsedNdefRecord {

    /** ISO/IANA language code  */
    /**
     * Returns the ISO/IANA language code associated with this text element.
     */
    //val languageCode: String

    //val text: String

    init {
        //this.languageCode = Preconditions.checkNotNull(languageCode)
        //this.text = Preconditions.checkNotNull(text)
    }

    override fun getView(activity: Activity, inflater: LayoutInflater, parent: ViewGroup, offset: Int): View {
        //val text = inflater.inflate(R.layout.tag_text, parent, false) as TextView
        //text.text = this.text
        //return text
        return LinearLayout(activity)
    }

    companion object {

        // TODO: deal with text fields which span multiple NdefRecords
        fun parse(record: NdefRecord): TextRecord {
            //Preconditions.checkArgument(record.tnf == NdefRecord.TNF_WELL_KNOWN)
            //var checkArgument: Any = Preconditions.checkArgument(Arrays.equals(record.type, NdefRecord.RTD_TEXT))
            try {
                val payload = record.payload
                /*
             * payload[0] contains the "Status Byte Encodings" field, per the
             * NFC Forum "Text Record Type Definition" section 3.2.1.
             *
             * bit7 is the Text Encoding Field.
             *
             * if (Bit_7 == 0): The text is encoded in UTF-8 if (Bit_7 == 1):
             * The text is encoded in UTF16
             *
             * Bit_6 is reserved for future use and must be set to zero.
             *
             * Bits 5 to 0 are the length of the IANA language code.
             */
                //val textEncoding = if (payload[0] and 128 == 0) "UTF-8" else "UTF-16"
                val languageCodeLength = payload[0] and 63
                //val languageCode = String(payload, 1, languageCodeLength, "US-ASCII")
                //val text = String(payload, languageCodeLength + 1,
                        //payload.size - languageCodeLength - 1, textEncoding)
                //return TextRecord(languageCode, text)
                return parse(NdefRecord.createApplicationRecord(""))
            } catch (e: UnsupportedEncodingException) {
                // should never happen unless we get a malformed tag.
                throw IllegalArgumentException(e)
            }

        }

        fun isText(record: NdefRecord): Boolean {
            try {
                parse(record)
                return true
            } catch (e: IllegalArgumentException) {
                return false
            }

        }
    }
}