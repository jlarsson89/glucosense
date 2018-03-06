package glucosense.org;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.NfcV;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

public class ScanActivity extends Activity {
    private NfcAdapter nfcAdapter;
    private String reading, buffer;
    private float currentGlucose = 0f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);
        nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        Button backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(ScanActivity.this, MainActivity.class);
                ScanActivity.this.startActivity(intent);
            }
        });
        if (nfcAdapter == null) {
            finish();
            return;
        }
        handleIntent(getIntent());
    }

    @Override
    protected void onResume() {
        super.onResume();
        setupForegroundDispatch(this, nfcAdapter);
    }

    @Override
    protected void onPause() {
        stopForegroundDispatch(this, nfcAdapter);
        super.onPause();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        String action = intent.getAction();
        if (NfcAdapter.ACTION_TECH_DISCOVERED.equals(action)) {
            Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
            String[] techList = tag.getTechList();
            String searchedTech = NfcV.class.getName();
            new NfcVReaderTask().execute(tag);
        }
    }

    public static void setupForegroundDispatch(final Activity activity, NfcAdapter adapter) {
        final Intent intent = new Intent(activity.getApplicationContext(), activity.getClass());
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        final PendingIntent pendingIntent = PendingIntent.getActivity(activity.getApplicationContext(), 0, intent, 0);
        IntentFilter[] filters = new IntentFilter[1];
        String[][] techList = new String[][]{};
        filters[0] = new IntentFilter();
        filters[0].addAction(NfcAdapter.ACTION_NDEF_DISCOVERED);
        filters[0].addCategory(Intent.CATEGORY_DEFAULT);
        adapter.enableForegroundDispatch(activity, pendingIntent, filters, techList);
    }

    public static void stopForegroundDispatch(final Activity activity, NfcAdapter adapter) {
        adapter.disableForegroundDispatch(activity);
    }

    final protected static char[] hexArray = "0123456789ABCDEF".toCharArray();
    public static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for ( int j = 0; j < bytes.length; j++ ) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }

    private class NfcVReaderTask extends AsyncTask<Tag, Void, String> {

        @Override
        protected void onPostExecute(String result) {
        }

        @Override
        protected String doInBackground(Tag... params) {
            Tag tag = params[0];
            NfcV nfcvTag = NfcV.get(tag);
            try {
                nfcvTag.connect();
            } catch (IOException e) {

            }

            reading = "";
            byte[][] bloques = new byte[40][8];
            try {
                byte[] cmd = new byte[] {
                        (byte)0x00,
                        (byte)0x2B
                };
                byte[] systeminfo = nfcvTag.transceive(cmd);
                systeminfo = Arrays.copyOfRange(systeminfo, 2, systeminfo.length - 1);
                for(int i=3; i <= 40; i++) {
                    cmd = new byte[] {
                            (byte)0x00,
                            (byte)0x20,
                            (byte)i
                    };
                    byte[] oneBlock = nfcvTag.transceive(cmd);
                    oneBlock = Arrays.copyOfRange(oneBlock, 1, oneBlock.length);
                    bloques[i-3] = Arrays.copyOf(oneBlock, 8);
                    reading = reading + bytesToHex(oneBlock)+"\r\n";
                }

                String s = "";
                for(int i=0;i<40;i++) {
                    s = s + bytesToHex(bloques[i]);
                }
                int current = Integer.parseInt(s.substring(4, 6), 16);
                String[] block1 = new String[16];
                String[] block2 = new String[32];
                int ii=0;
                for (int i=8; i< 8+15*12; i+=12)
                {
                    block1[ii] = s.substring(i,i+12);
                    final String g = s.substring(i+2,i+4)+s.substring(i,i+2);
                    if (current == ii) {
                        currentGlucose = glucoseReading(Integer.parseInt(g,16));
                    }
                    ii++;
                }
                reading = reading + "Current approximate glucose " + currentGlucose;
                ii=0;
                for (int i=188; i< 188+31*12; i+=12)
                {
                    block2[ii] = s.substring(i,i+12);
                    ii++;
                }

            } catch (IOException e) {
                ScanActivity.this.runOnUiThread(new Runnable() {
                    public void run() {
                    }
                });
                return null;
            }
            addText(reading);
            try {
                nfcvTag.close();
            } catch (IOException e) {

            }
            Date date = new Date() ;
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss") ;
            File file = new File("/sdcard/fsl_"+dateFormat.format(date) + ".log");
            try {
                file.createNewFile();
                FileOutputStream fOut = new FileOutputStream(file);
                OutputStreamWriter myOutWriter =new OutputStreamWriter(fOut);
                myOutWriter.append(reading);
                myOutWriter.close();
                fOut.close();
            }
            catch (Exception e) {
            }
            return null;
        }
    }

    private void addText(final String s)
    {
        ScanActivity.this.runOnUiThread(new Runnable() {
            public void run() {
            }
        });
    }

    private void GetTime(Long minutes){
        Long t3 = minutes;
        Long t4 = t3/1440;
        Long t5 = t3-(t4*1440);
        Long t6 = (t5/60);
        Long t7 = t5-(t6*60);
    }

    private float glucoseReading(int val) {
        int bitmask = 0x0FFF;
        return Float.valueOf( Float.valueOf((val & bitmask) / 6) - 37);
    }
}