package glucosense.org

import android.nfc.NdefRecord
import android.text.util.Linkify
import android.widget.TextView
import android.view.ViewGroup
import android.view.LayoutInflater
import android.app.Activity
import android.net.Uri
import android.support.v4.util.Preconditions
import android.view.View
import android.widget.LinearLayout
import glucosense.org.R.id.text
import java.nio.charset.Charset
import java.util.*
//import javax.swing.UIManager.put



/**
 * Created by johan on 26/02/18.
 */
class UriRecord private constructor(uri: Uri) : ParsedNdefRecord {

    //val uri: Uri

    init {
        //this.uri = Preconditions.checkNotNull(uri)
    }

    override fun getView(activity: Activity, inflater: LayoutInflater, parent: ViewGroup, offset: Int): View {
        //val text = inflater.inflate(R.layout.tag_text, parent, false) as TextView
        //text.autoLinkMask = Linkify.ALL
        //text.setText(uri.toString())
        //return text
        return LinearLayout(activity)
    }

    companion object {

        private val TAG = "UriRecord"

        val RECORD_TYPE = "UriRecord"

        /**
         * NFC Forum "URI Record Type Definition"
         *
         * This is a mapping of "URI Identifier Codes" to URI string prefixes,
         * per section 3.2.2 of the NFC Forum URI Record Type Definition document.
         */
        /*private val URI_PREFIX_MAP = ImmutableBiMap.builder()
                .put(0x00.toByte(), "")
                .put(0x01.toByte(), "http://www.")
                .put(0x02.toByte(), "https://www.")
                .put(0x03.toByte(), "http://")
                .put(0x04.toByte(), "https://")
                .put(0x05.toByte(), "tel:")
                .put(0x06.toByte(), "mailto:")
                .put(0x07.toByte(), "ftp://anonymous:anonymous@")
                .put(0x08.toByte(), "ftp://ftp.")
                .put(0x09.toByte(), "ftps://")
                .put(0x0A.toByte(), "sftp://")
                .put(0x0B.toByte(), "smb://")
                .put(0x0C.toByte(), "nfs://")
                .put(0x0D.toByte(), "ftp://")
                .put(0x0E.toByte(), "dav://")
                .put(0x0F.toByte(), "news:")
                .put(0x10.toByte(), "telnet://")
                .put(0x11.toByte(), "imap:")
                .put(0x12.toByte(), "rtsp://")
                .put(0x13.toByte(), "urn:")
                .put(0x14.toByte(), "pop:")
                .put(0x15.toByte(), "sip:")
                .put(0x16.toByte(), "sips:")
                .put(0x17.toByte(), "tftp:")
                .put(0x18.toByte(), "btspp://")
                .put(0x19.toByte(), "btl2cap://")
                .put(0x1A.toByte(), "btgoep://")
                .put(0x1B.toByte(), "tcpobex://")
                .put(0x1C.toByte(), "irdaobex://")
                .put(0x1D.toByte(), "file://")
                .put(0x1E.toByte(), "urn:epc:id:")
                .put(0x1F.toByte(), "urn:epc:tag:")
                .put(0x20.toByte(), "urn:epc:pat:")
                .put(0x21.toByte(), "urn:epc:raw:")
                .put(0x22.toByte(), "urn:epc:")
                .put(0x23.toByte(), "urn:nfc:")
                .build()*/

        /**
         * Convert [android.nfc.NdefRecord] into a [android.net.Uri].
         * This will handle both TNF_WELL_KNOWN / RTD_URI and TNF_ABSOLUTE_URI.
         *
         * @throws IllegalArgumentException if the NdefRecord is not a record
         * containing a URI.
         */
        fun parse(record: NdefRecord): UriRecord {
            val tnf = record.tnf
            if (tnf == NdefRecord.TNF_WELL_KNOWN) {
                return parseWellKnown(record)
            } else if (tnf == NdefRecord.TNF_ABSOLUTE_URI) {
                return parseAbsolute(record)
            }
            throw IllegalArgumentException("Unknown TNF " + tnf)
        }

        /** Parse and absolute URI record  */
        private fun parseAbsolute(record: NdefRecord): UriRecord {
            val payload = record.payload
            val uri = Uri.parse(String(payload, Charset.forName("UTF-8")))
            return UriRecord(uri)
        }

        /** Parse an well known URI record  */
        private fun parseWellKnown(record: NdefRecord): UriRecord {
            //Preconditions.checkArgument(Arrays.equals(record.type, NdefRecord.RTD_URI))
            val payload = record.payload
            /*
         * payload[0] contains the URI Identifier Code, per the
         * NFC Forum "URI Record Type Definition" section 3.2.2.
         *
         * payload[1]...payload[payload.length - 1] contains the rest of
         * the URI.
         */
            //val prefix = URI_PREFIX_MAP.get(payload[0])
            //val fullUri = Bytes.concat(prefix.getBytes(Charset.forName("UTF-8")), Arrays.copyOfRange(payload, 1,
                    //payload.size))
            //val uri = Uri.parse(String(fullUri, Charset.forName("UTF-8")))
            //return UriRecord(uri)
            return parseWellKnown(NdefRecord.createApplicationRecord(""))
        }

        fun isUri(record: NdefRecord): Boolean {
            try {
                parse(record)
                return true
            } catch (e: IllegalArgumentException) {
                return false
            }

        }

        private val EMPTY = ByteArray(0)
    }
}