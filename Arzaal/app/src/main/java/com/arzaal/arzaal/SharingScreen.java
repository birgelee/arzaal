package com.arzaal.arzaal;

import android.Manifest;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.NfcEvent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.arzaal.arzaal.contact.Contact;
import com.arzaal.arzaal.systemread.SystemReader;
import com.arzaal.arzaal.systemupdate.SystemUpdater;

public class SharingScreen extends AppCompatActivity implements NfcAdapter.CreateNdefMessageCallback {

    final private int REQUEST_CODE_ASK_PERMISSIONS = 123;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sharing_screen);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        askForPermissionAndWait(new String[]{Manifest.permission.WRITE_CONTACTS, Manifest.permission.READ_PHONE_STATE, Manifest.permission.GET_ACCOUNTS});

        Contact c = SystemReader.readSystemContact(SettingsManager.manageSettings(this), this);

        SystemUpdater.addContactToSystem(c, this);

        NfcAdapter mAdapter = NfcAdapter.getDefaultAdapter(this);
        if (mAdapter == null) {
            Toast.makeText(this, "Sorry, you don't have NFC.", Toast.LENGTH_LONG).show();
            return;
        }

        if (!mAdapter.isEnabled()) {
            Toast.makeText(this, "Please enable NFC via Settings.", Toast.LENGTH_LONG).show();
        }

        mAdapter.setNdefPushMessageCallback(this, this);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_sharing_screen, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void askForPermissionAndWait(String[] permissionNames) {
        if (!hasPermission(permissionNames)) {
            //requestPermissions(permissionNames,REQUEST_CODE_ASK_PERMISSIONS); REQUIRED IN SDK 23
        }
        while (!hasPermission(permissionNames)) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private boolean hasPermission(String[] permissionNames) {
        return true; // Must be removed in SDK 23
        /*for (String permissionName : permissionNames) {
            int permission = checkSelfPermission(permissionName);
            if (permission != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;*/

    }

    /**
     * Ndef Record that will be sent over via NFC
     * @param nfcEvent
     * @return
     */
    @Override
    public NdefMessage createNdefMessage(NfcEvent nfcEvent) {
        String message = SystemReader.readSystemContact(SettingsManager.manageSettings(this), this).serialize();
        NdefRecord ndefRecord = NdefRecord.createMime("text/plain", message.getBytes());
        NdefMessage ndefMessage = new NdefMessage(ndefRecord);
        return ndefMessage;
    }
}
