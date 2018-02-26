package glucosense.org

import android.app.ActionBar
import kotlin.text.Charsets
import android.nfc.NdefRecord
//import javax.swing.UIManager.put
//import android.support.test.espresso.core.internal.deps.guava.collect.ImmutableMap
//import android.support.test.espresso.core.internal.deps.guava.collect.Iterables
import android.widget.LinearLayout
import android.view.ViewGroup
import android.view.LayoutInflater
import android.app.Activity
//import sun.font.TextRecord
import android.nfc.FormatException
import android.nfc.NdefMessage
import android.support.v4.util.Preconditions
import android.view.View
import java.util.*


/**
 * Created by johan on 26/02/18.
 */
class SmartPoster private constructor(uri: UriRecord,
        /**
         * NFC Forum Smart Poster Record Type Definition section 3.2.1.
         *
         * "The Title record for the service (there can be many of these in
         * different languages, but a language MUST NOT be repeated). This record is
         * optional."
         */
                                      /**
                                       * Returns the title of the smart poster. This may be `null`.
                                       */
                                      val title: TextRecord?, action: RecommendedAction,
                                      /**
                                       * NFC Forum Smart Poster Record Type Definition section 3.2.1.
                                       *
                                       * "The Type record. If the URI references an external entity (e.g., via a
                                       * URL), the Type record may be used to declare the MIME type of the entity.
                                       * This can be used to tell the mobile device what kind of an object it can
                                       * expect before it opens the connection. The Type record is optional."
                                       */
                                      private val mType: String) : ParsedNdefRecord {

    /**
     * NFC Forum Smart Poster Record Type Definition section 3.2.1.
     *
     * "The URI record. This is the core of the Smart Poster, and all other
     * records are just metadata about this record. There MUST be one URI record
     * and there MUST NOT be more than one."
     */
    //val uriRecord: UriRecord

    /**
     * NFC Forum Smart Poster Record Type Definition section 3.2.1.
     *
     * "The Action record. This record describes how the service should be
     * treated. For example, the action may indicate that the device should save
     * the URI as a bookmark or open a browser. The Action record is optional.
     * If it does not exist, the device may decide what to do with the service.
     * If the action record exists, it should be treated as a strong suggestion;
     * the UI designer may ignore it, but doing so will induce a different user
     * experience from device to device."
     */
    //private val mAction: RecommendedAction

    init {
        //uriRecord = Preconditions.checkNotNull(uri)
        //mAction = Preconditions.checkNotNull(action)
    }

    override fun getView(activity: Activity, inflater: LayoutInflater, parent: ViewGroup, offset: Int): View {
        if (title != null) {
            // Build a container to hold the title and the URI
            val container = LinearLayout(activity)
            container.orientation = LinearLayout.VERTICAL
            container.layoutParams = ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT,
                    ActionBar.LayoutParams.WRAP_CONTENT)
            container.addView(title!!.getView(activity, inflater, container, offset))
            //inflater.inflate(R.layout.tag_divider, container)
            //container.addView(uriRecord.getView(activity, inflater, container, offset))
            return container
        } else {
            // Just a URI, return a view for it directly
            //return uriRecord.getView(activity, inflater, parent, offset)
            return LinearLayout(activity)
        }
    }

    private enum class RecommendedAction private constructor(private val byte: Byte) {
        UNKNOWN((-1).toByte()), DO_ACTION(0.toByte()), SAVE_FOR_LATER(1.toByte()), OPEN_FOR_EDITING(
                2.toByte());


        companion object {

            //private val LOOKUP: ImmutableMap<Byte, RecommendedAction>

            init {
                //val builder = ImmutableMap.builder()
                for (action in RecommendedAction.values()) {
                   // builder.put(action.byte, action)
                }
                //LOOKUP = builder.build()
            }
        }
    }

    companion object {

        fun parse(record: NdefRecord): SmartPoster {
            //Preconditions.checkArgument(record.tnf == NdefRecord.TNF_WELL_KNOWN)
            //Preconditions.checkArgument(Arrays.equals(record.type, NdefRecord.RTD_SMART_POSTER))
            try {
                val subRecords = NdefMessage(record.payload)
                return parse(subRecords.records)
            } catch (e: FormatException) {
                throw IllegalArgumentException(e)
            }

        }

        fun parse(recordsRaw: Array<NdefRecord>): SmartPoster {
            try {
                val records = NdefMessageParser.getRecords(recordsRaw)
                //val uri = Iterables.getOnlyElement(Iterables.filter(records, UriRecord::class.java!!))
                val title = getFirstIfExists<TextRecord>(records, TextRecord::class.java)
                val action = parseRecommendedAction(recordsRaw)
                //val type = parseType(recordsRaw)
                //return SmartPoster(uri, title, action, type)
            } catch (e: NoSuchElementException) {
                throw IllegalArgumentException(e)
            }
            return parse(recordsRaw)
        }

        fun isPoster(record: NdefRecord): Boolean {
            try {
                parse(record)
                return true
            } catch (e: IllegalArgumentException) {
                return false
            }

        }

        /**
         * Returns the first element of `elements` which is an instance of
         * `type`, or `null` if no such element exists.
         */
        private fun <T> getFirstIfExists(elements: Iterable<*>, type: Class<T>): T? {
            //val filtered = Iterables.filter(elements, type)
            var instance: T? = null
            //if (!Iterables.isEmpty(filtered)) {
                //instance = Iterables.get(filtered, 0)
            //}
            return instance
        }

        private fun getByType(type: ByteArray, records: Array<NdefRecord>): NdefRecord? {
            for (record in records) {
                if (Arrays.equals(type, record.type)) {
                    return record
                }
            }
            return null
        }

        private val ACTION_RECORD_TYPE = byteArrayOf('a'.toByte(), 'c'.toByte(), 't'.toByte())

        private fun parseRecommendedAction(records: Array<NdefRecord>): RecommendedAction {
            val record = getByType(ACTION_RECORD_TYPE, records) ?: return RecommendedAction.UNKNOWN
            val action = record.payload[0]
            return RecommendedAction.DO_ACTION
            //return if (RecommendedAction.LOOKUP.containsKey(action)) {
                //RecommendedAction.LOOKUP.get(action)
            } //else RecommendedAction.UNKNOWN
        }

        private val TYPE_TYPE = byteArrayOf('t'.toByte())

        private fun parseType(records: Array<NdefRecord>): String? {
            val type = getByType(TYPE_TYPE, records) ?: return null
            return String(type.payload, Charsets.UTF_8)
        }
    }
//}