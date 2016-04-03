package com.arzaal.arzaal;

import android.Manifest;
import android.app.Activity;
import android.net.nsd.NsdServiceInfo;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.NfcEvent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.arzaal.arzaal.contact.Contact;
import com.arzaal.arzaal.systemread.SystemReader;
import com.arzaal.arzaal.systemupdate.SystemUpdater;


public class SharingScreen extends Activity {

    final private int REQUEST_CODE_ASK_PERMISSIONS = 123;


    NsdHelper mNsdHelper;

    private Handler mUpdateHandler;

    public static final String TAG = "NsdChat";

    public ChatConnection mConnection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sharing_screen);

        askForPermissionAndWait(new String[]{Manifest.permission.WRITE_CONTACTS, Manifest.permission.READ_PHONE_STATE, Manifest.permission.GET_ACCOUNTS});




        mUpdateHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                String chatLine = msg.getData().getString("msg");

                SystemUpdater.addContactToSystem(Contact.deserialize(chatLine), SharingScreen.this);
                Toast.makeText(SharingScreen.this, chatLine, Toast.LENGTH_LONG).show();
            }
        };

        mConnection = new ChatConnection(mUpdateHandler);




        mNsdHelper = new NsdHelper(this);
        mNsdHelper.initializeNsd();

        initComs();

        mHandler = new Handler();
        startRepeatingTask();
    }


    public void advertise() {
        // Register service
        if(mConnection.getLocalPort() > -1) {
            mNsdHelper.registerService(mConnection.getLocalPort());
        } else {
            Log.d(TAG, "ServerSocket isn't bound.");
        }
    }

    public void discover() {
        mNsdHelper.discoverServices();
    }

    public boolean connect() {
        NsdServiceInfo service = mNsdHelper.getChosenServiceInfo();
        if (service != null) {
            Log.d(TAG, "Connecting.");
            mConnection.connectToServer(service.getHost(),
                    service.getPort());
            return true;
        } else {
            Log.d(TAG, "No service to connect to!");
            return false;
        }
    }

    public void sendText(String text) {

            String messageString =  text;
            if (!messageString.isEmpty()) {
                mConnection.sendMessage(messageString);
            }
    }


    public void clickAdvertise(View v) {
        // Register service
        if(mConnection.getLocalPort() > -1) {
            mNsdHelper.registerService(mConnection.getLocalPort());
        } else {
            Log.d(TAG, "ServerSocket isn't bound.");
        }
    }

    public void clickDiscover(View v) {
        mNsdHelper.discoverServices();
    }

    public void clickConnect(View v) {
        NsdServiceInfo service = mNsdHelper.getChosenServiceInfo();
        if (service != null) {
            Log.d(TAG, "Connecting.");
            mConnection.connectToServer(service.getHost(),
                    service.getPort());
        } else {
            Log.d(TAG, "No service to connect to!");
        }
    }

    public void clickSend(View v) {

        Contact c = SystemReader.readSystemContact(SettingsManager.manageSettings(this), this);
        sendText(c.serialize());



    }



    @Override
    protected void onPause() {
        if (mNsdHelper != null) {
            mNsdHelper.stopDiscovery();
        }
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mNsdHelper != null) {
            mNsdHelper.discoverServices();
        }
    }

    @Override
    protected void onDestroy() {
        mNsdHelper.tearDown();
        mConnection.tearDown();
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


    public void initComs() {
        advertise();

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


    private int mInterval = 500; // 5 seconds by default, can be changed later
    private Handler mHandler;



    Runnable mStatusChecker = new Runnable() {
        @Override
        public void run() {
            try {
                if (!mConnection.isConnected()) {
                    connect();
                }

            } finally {
                // 100% guarantee that this always happens, even if
                // your update method throws an exception
                mHandler.postDelayed(mStatusChecker, mInterval);
            }
        }
    };

    void startRepeatingTask() {
        mStatusChecker.run();
    }

    void stopRepeatingTask() {
        mHandler.removeCallbacks(mStatusChecker);
    }


}
