package glucosense.org

import android.app.PendingIntent
import android.content.Intent
import android.nfc.NfcAdapter
import android.nfc.tech.NfcV
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.nfc.Tag
import java.util.*
//import android.support.test.runner.internal.deps.aidl.Codecs.writeStrongBinder
import kotlin.experimental.and
//import android.support.test.internal.runner.junit4.statement.UiThreadStatement.runOnUiThread
//import sun.text.normalizer.UTF16.append
//import android.support.test.runner.intent.IntentStubberRegistry.reset
import android.media.MediaPlayer
import android.media.MediaPlayer.OnCompletionListener
import android.widget.Toast
import android.os.Vibrator
import android.os.AsyncTask
import android.app.Activity
import android.content.IntentFilter
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStreamWriter
import java.text.SimpleDateFormat


class ScanActivity : AppCompatActivity() {
    val MIME_TEXT_PLAIN = "text/plain"

    private var mNfcAdapter: NfcAdapter? = null

    private var lectura: String? = null
    private val buffer: String? = null
    private var currentGlucose = 0f
    private var tvResult: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scan)

        tvResult = findViewById(R.id.result) as TextView

        mNfcAdapter = NfcAdapter.getDefaultAdapter(this)

        if (mNfcAdapter == null) {
            // Stop here, we definitely need NFC
            Toast.makeText(this, "This device doesn't support NFC.", Toast.LENGTH_LONG).show()
            finish()
            return

        }

        if (!mNfcAdapter!!.isEnabled) {
            Toast.makeText(this, "NFC is disabled.", Toast.LENGTH_LONG).show()
        }

        handleIntent(intent)
    }

    override fun onResume() {
        super.onResume()

        /**
         * It's important, that the activity is in the foreground (resumed). Otherwise
         * an IllegalStateException is thrown.
         */
        setupForegroundDispatch(this, mNfcAdapter)
    }

    override fun onPause() {
        /**
         * Call this before onPause, otherwise an IllegalArgumentException is thrown as well.
         */
        stopForegroundDispatch(this, mNfcAdapter)

        super.onPause()
    }

    override fun onNewIntent(intent: Intent) {
        /**
         * This method gets called, when a new Intent gets associated with the current activity instance.
         * Instead of creating a new activity, onNewIntent will be called. For more information have a look
         * at the documentation.
         *
         * In our case this method gets called, when the user attaches a Tag to the device.
         */
        handleIntent(intent)
    }

    private fun handleIntent(intent: Intent) {
        val action = intent.action
        if (NfcAdapter.ACTION_TECH_DISCOVERED == action) {

            Log.i("socialdiabetes", "NfcAdapter.ACTION_TECH_DISCOVERED")
            // In case we would still use the Tech Discovered Intent
            val tag = intent.getParcelableExtra<Tag>(NfcAdapter.EXTRA_TAG)
            val techList = tag.techList
            val searchedTech = NfcV::class.java.name
            NfcVReaderTask().execute(tag)

        }
    }

    /**
     * @param activity The corresponding [Activity] requesting the foreground dispatch.
     * @param adapter The [NfcAdapter] used for the foreground dispatch.
     */
    fun setupForegroundDispatch(activity: Activity, adapter: NfcAdapter?) {
        val intent = Intent(activity.applicationContext, activity.javaClass)
        intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP

        val pendingIntent = PendingIntent.getActivity(activity.applicationContext, 0, intent, 0)

        val filters = arrayOfNulls<IntentFilter>(1)
        val techList = arrayOf<Array<String>>()

        // Notice that this is the same filter as in our manifest.
        filters[0] = IntentFilter()
        filters[0]?.addAction(NfcAdapter.ACTION_NDEF_DISCOVERED)
        filters[0]?.addCategory(Intent.CATEGORY_DEFAULT)

        adapter!!.enableForegroundDispatch(activity, pendingIntent, filters, techList)
    }

    /**
     * @param activity The corresponding [BaseActivity] requesting to stop the foreground dispatch.
     * @param adapter The [NfcAdapter] used for the foreground dispatch.
     */
    fun stopForegroundDispatch(activity: Activity, adapter: NfcAdapter?) {
        adapter!!.disableForegroundDispatch(activity)
    }

    protected val hexArray = "0123456789ABCDEF".toCharArray()
    fun bytesToHex(bytes: ByteArray): String {
        val hexChars = CharArray(bytes.size * 2)
        for (j in bytes.indices) {
            //val v = bytes[j] and 0xFF
            val v = bytes[j].toInt()
            hexChars[j * 2] = hexArray[v.ushr(4)]
            hexChars[j * 2 + 1] = hexArray[v and 0x0F]
        }
        return String(hexChars)
    }

    /**
     *
     * Background task for reading the data. Do not block the UI thread while reading.
     *
     */
    private inner class NfcVReaderTask : AsyncTask<Tag, Void, String>() {

        override fun onPostExecute(result: String) {
            //val vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
            //vibrator.vibrate(1000)
            //Abbott.this.finish();
        }

        override fun doInBackground(vararg params: Tag): String? {
            val tag = params[0]

            val nfcvTag = NfcV.get(tag)
            Log.i("socialdiabetes", "Enter NdefReaderTask: " + nfcvTag.toString())

            Log.i("socialdiabetes", "Tag ID: " + tag.id)


            try {
                nfcvTag.connect()
            } catch (e: IOException) {
                this@ScanActivity.runOnUiThread(java.lang.Runnable { Toast.makeText(applicationContext, "Error opening NFC connection!", Toast.LENGTH_SHORT).show() })

                return null
            }

            lectura = ""

            val bloques = Array(40) { ByteArray(8) }
            val allBlocks = ByteArray(40 * 8)


            Log.i("socialdiabetes", "---------------------------------------------------------------")
            Log.i("socialdiabetes", "nfcvTag ID: " + nfcvTag.getDsfId());

            Log.i("socialdiabetes", "getMaxTransceiveLength: " + nfcvTag.getMaxTransceiveLength());
            try {

                // Get system information (0x2B)
                var cmd = byteArrayOf(0x00.toByte(), // Flags
                        0x2B.toByte() // Command: Get system information
                )
                var systeminfo = nfcvTag.transceive(cmd)

                //Log.d("socialdiabetes", "systeminfo: "+systeminfo.toString()+" - "+systeminfo.length);
                Log.i("socialdiabetes", "systeminfo HEX: " + bytesToHex(systeminfo));

                systeminfo = Arrays.copyOfRange(systeminfo, 2, systeminfo.size - 1)

                val memorySize = byteArrayOf(systeminfo[6], systeminfo[5])
                Log.i("socialdiabetes", "Memory Size: " + bytesToHex(memorySize) + " / " + Integer.parseInt(bytesToHex(memorySize).trim { it <= ' ' }, 16))

                val blocks = byteArrayOf(systeminfo[8])
                Log.i("socialdiabetes", "blocks: " + bytesToHex(blocks) + " / " + Integer.parseInt(bytesToHex(blocks).trim { it <= ' ' }, 16))

                val totalBlocks = Integer.parseInt(bytesToHex(blocks).trim { it <= ' ' }, 16)

                for (i in 3..40) { // Leer solo los bloques que nos interesan
                    /*
	                cmd = new byte[] {
	                    (byte)0x00, // Flags
	                    (byte)0x23, // Command: Read multiple blocks
	                    (byte)i, // First block (offset)
	                    (byte)0x01  // Number of blocks
	                };
	                */
                    // Read single block
                    cmd = byteArrayOf(0x00.toByte(), // Flags
                            0x20.toByte(), // Command: Read multiple blocks
                            i.toByte() // block (offset)
                    )

                    var oneBlock = nfcvTag.transceive(cmd)
                    Log.i("socialdiabetes", "userdata: " + oneBlock.toString() + " - " + oneBlock.size)
                    oneBlock = Arrays.copyOfRange(oneBlock, 1, oneBlock.size)
                    bloques[i - 3] = Arrays.copyOf(oneBlock, 8)


                    Log.i("socialdiabetes", "userdata HEX: " + bytesToHex(oneBlock))

                    lectura = lectura + bytesToHex(oneBlock) + "\r\n"
                }

                var s = ""
                for (i in 0..39) {
                    Log.i("socialdiabetes", bytesToHex(bloques[i]))
                    s = s + bytesToHex(bloques[i])
                }

                Log.i("socialdiabetes", "S: " + s)

                Log.i("socialdiabetes", "Next read: " + s.substring(4, 6))
                val current = Integer.parseInt(s.substring(4, 6), 16)
                Log.i("socialdiabetes", "Next read: " + current)
                Log.i("socialdiabetes", "Next historic read " + s.substring(6, 8))

                val bloque1 = arrayOfNulls<String>(16)
                val bloque2 = arrayOfNulls<String>(32)
                Log.i("socialdiabetes", "--------------------------------------------------")
                var ii = 0
                run {
                    var i = 8
                    while (i < 8 + 15 * 12) {
                        Log.i("socialdiabetes", s.substring(i, i + 12))
                        bloque1[ii] = s.substring(i, i + 12)

                        val g = s.substring(i + 2, i + 4) + s.substring(i, i + 2)

                        if (current == ii) {
                            currentGlucose = glucoseReading(Integer.parseInt(g, 16))
                        }
                        ii++
                        i += 12


                    }
                }
                lectura = lectura + "Current approximate glucose " + currentGlucose
                Log.i("socialdiabetes", "Current approximate glucose " + currentGlucose)

                Log.i("socialdiabetes", "--------------------------------------------------")
                ii = 0
                var i = 188
                while (i < 188 + 31 * 12) {
                    Log.i("socialdiabetes", s.substring(i, i + 12))
                    bloque2[ii] = s.substring(i, i + 12)
                    ii++
                    i += 12
                }
                Log.i("socialdiabetes", "--------------------------------------------------")

            } catch (e: IOException) {
                this@ScanActivity.runOnUiThread(java.lang.Runnable { Toast.makeText(applicationContext, "Error reading NFC!", Toast.LENGTH_SHORT).show() })

                return null
            }

            addText(lectura)

            try {
                nfcvTag.close()
            } catch (e: IOException) {
                /*
                Abbott.this.runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(getApplicationContext(), "Error closing NFC connection!", Toast.LENGTH_SHORT).show();
                    }
                });

                return null;
                */
            }


            /*val mp: MediaPlayer
            mp = MediaPlayer.create(this@ScanActivity, R.raw.notification)
            mp.setOnCompletionListener { mp ->
                var mp = mp
                // TODO Auto-generated method stub
                mp!!.reset()
                mp!!.release()
                mp = null
            }
            mp.start()*/

            val date = Date()
            val dateFormat = SimpleDateFormat("yyyy-MM-dd_HH-mm-ss")
            val myFile = File("/sdcard/fsl_" + dateFormat.format(date) + ".log")
            try {
                myFile.createNewFile()
                val fOut = FileOutputStream(myFile)
                val myOutWriter = OutputStreamWriter(fOut)
                myOutWriter.append(lectura)
                myOutWriter.close()
                fOut.close()
            } catch (e: Exception) {
            }



            return null
        }


    }

    private fun addText(s: String?) {
        this@ScanActivity.runOnUiThread(java.lang.Runnable { tvResult!!.text = s })

    }

    private fun GetTime(minutes: Long?) {
        val t4 = minutes!! / 1440
        val t5 = minutes - t4 * 1440
        val t6 = t5 / 60
        val t7 = t5 - t6 * 60
    }

    private fun glucoseReading(`val`: Int): Float {
        // ((0x4531 & 0xFFF) / 6) - 37;
        val bitmask = 0x0FFF
        return java.lang.Float.valueOf(java.lang.Float.valueOf(((`val` and bitmask) / 6).toFloat())!! - 37)!!
    }
}


/*
    private var nfcAdapter: NfcAdapter? = null
    private var nfcPendingIntent: PendingIntent? = null
    private val mNdefPushMessage: NdefMessage? = null
    private val tags = ArrayList<Tag?>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scan)
        Log.i("scan", "launched")
        nfcAdapter = NfcAdapter.getDefaultAdapter(this)
        nfcPendingIntent = PendingIntent.getActivity(this, 0,
                Intent(this, javaClass).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0)
        if (intent != null) {
            //Log.i("found intent", intent.action.toString())
            Log.i("intent", intent.toString())
            processIntent(intent)
        }
        Log.i("nfc supported", (nfcAdapter != null).toString())
        Log.i("nfc enabled", (nfcAdapter?.isEnabled.toString()))
        backButton.setOnClickListener {
            Log.d("test", "works")
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)        }
    }

    override fun onResume() {
        super.onResume()
        nfcAdapter?.enableForegroundDispatch(this, nfcPendingIntent, null, null)
    }

    override fun onPause() {
        super.onPause()
        nfcAdapter?.disableForegroundDispatch(this)
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        if (intent != null) {
            processIntent(intent)
        }
    }

    private fun processIntent(checkIntent: Intent) {
        val action = checkIntent.action
        Log.i("process intent", "runs")
        if (NfcAdapter.ACTION_TAG_DISCOVERED.equals(action)) {
            Log.i("action", action)
            //var rawMessages = intent.getParcelableArrayExtra(NfcAdapter.ACTION_TAG_DISCOVERED)
            //var rawMessages = checkIntent.getParcelableArrayExtra(NfcAdapter.EXTRA_TAG)
            val rawMessages = NfcV.get(checkIntent.getParcelableExtra(NfcAdapter.EXTRA_TAG))
            val msgs = rawMessages.responseFlags
            val msgs2 = rawMessages.tag
            val techlist = msgs2.techList
            val msgs3 = rawMessages.maxTransceiveLength
            val msgs4 = rawMessages.dsfId
            Log.i("intent", rawMessages.toString())
            Log.i("flags", msgs.toString())
            Log.i("tag", msgs2.toString())
            Log.i("length", msgs3.toString())
            Log.i("list", techlist.size.toString())
            for (i in techlist) {
                Log.i("i", i.toString())
            }
            Log.i("id", msgs4.toString())
            //Log.i("raw", rawMessages.size.toString())
            //var rawMessages = NfcV.get(intent.getParcelableExtra(NfcAdapter.EXTRA_NDEF_MESSAGES))
            /*if (rawMessages != null) {
                var msgs = arrayOfNulls<NdefMessage?>(rawMessages.size)
                Log.i("works", rawMessages.size.toString())
            }*/
        }
        /*if (/*checkIntent.action == NfcAdapter.ACTION_NDEF_DISCOVERED || */checkIntent.action == NfcAdapter.ACTION_TAG_DISCOVERED/* || checkIntent.action == NfcAdapter.ACTION_TECH_DISCOVERED*/) {
            Log.i("new ndef intent", checkIntent.toString())
            val rawMessages3 = NfcV.get(checkIntent.getParcelableExtra(NfcAdapter.EXTRA_NDEF_MESSAGES))
            val rawMessages2 = checkIntent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES)
            val rawMessages = NfcV.get(checkIntent.getParcelableExtra(NfcAdapter.EXTRA_TAG))
            //val extras = checkIntent.extras
            val test = NfcV.get(checkIntent.getParcelableExtra(NfcAdapter.EXTRA_TAG))
            Log.i("rawmessages intent", rawMessages.toString())
            Log.i("rawmessages id", rawMessages.dsfId.toString())
            //Log.i("rawmessages2", rawMessages2.toString())
            //Log.i("rawmessages3", rawMessages3.toString())
            Log.i("test intent", test.toString())
            val test2 = test.tag.techList
            Log.i("test2", test2.toString())
            Log.i("test2 size", test2.size.toString())
            val label = TextView(this)
            //label.text = extras.toString()
            scanActivity.addView(label)
            val test3 = rawMessages.responseFlags
            Log.i("test3", test3.toString())
            //val rawMessages = checkIntent.getParcelableArrayExtra(NfcAdapter.EXTRA_TAG)
            //Log.i("raw messages", rawMessages.size.toString())
            if (test3 != null) {
                //val msg = arrayOfNulls<NdefMessage?>(test2.size)
                //val msgs = arrayOf(test2)
                val msg = arrayOf(test3)
                Log.i("msg", msg.toString())
                for (i in msg.indices) {
                    //val m = msg[i]?.records
                    val a = msg.toString()
                    Log.i("arrayof", a)
                    //val n = msgs?.get(rawMessages.dsfId.toInt())
                    //Log.i("msg loop", i.toString())
                    //Log.i("m", n.toString())
                }
                for (i in test2.indices) {
                    //val m = test2[i] as NdefMessage
                    Log.i("i", i.toString())
                    //Log.i("i2", m.toString())

                }
                //processNdefMessages(msg)
            }
            if (rawMessages != null) {

                //val messages = arrayOf<NdefMessage?>()
                //val messages = arrayOfNulls<NdefMessage?>(rawMessages.size)
                //for (i in rawMessages.indices) {
                    //val msg = rawMessages[i] as NdefMessage
                    //Log.i("msg", msg.toString())
                //}
                //processNdefMessages(messages)
            }
        }*/