package glucosense.org

import android.content.Intent
import android.nfc.NdefMessage
import android.nfc.NdefRecord
import android.nfc.NfcAdapter
import android.util.Log

object NFCUtil {
    private fun getNDefMessages(intent: Intent): Array<NdefMessage>  {
        val rawMessage = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES)
        rawMessage?.let {
            return rawMessage.map {
                it as NdefMessage
            }.toTypedArray()
        }
        val empty = byteArrayOf()
        val record = NdefRecord(NdefRecord.TNF_UNKNOWN, empty, empty, empty)
        val msg = NdefMessage(arrayOf(record))
        return arrayOf(msg)
    }

    fun retrieveNFCMessage(intent: Intent?): String {
        Log.i("intent", intent.toString())
        intent?.let {
            if (NfcAdapter.ACTION_NDEF_DISCOVERED == intent.action) {
                val ndefMessages = getNDefMessages(intent)
                ndefMessages[0].records?.let {
                    it.forEach {
                        it?.payload.let {
                            it?.let {
                                return String(it)
                            }
                        }
                    }
                }
            }
            else {
                return "Touch NFC tag to read data, has intent"
            }
        }
        return "Touch NFC tag to read data, no intent"
    }

}
