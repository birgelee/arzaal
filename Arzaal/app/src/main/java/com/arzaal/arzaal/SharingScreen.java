package com.arzaal.arzaal;

import android.Manifest;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.arzaal.arzaal.contact.Contact;
import com.arzaal.arzaal.contact.ContactSyncSettings;
import com.arzaal.arzaal.systemread.SystemReader;
import com.arzaal.arzaal.systemupdate.SystemUpdater;

public class SharingScreen extends AppCompatActivity {

    final private int REQUEST_CODE_ASK_PERMISSIONS = 123;

    public static final String PREFS_NAME = "MainSettings";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sharing_screen);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        CheckBox facebookCheckBox = (CheckBox)findViewById(R.id.facebookCheckBox);
        CheckBox contactInfoCheckBox = (CheckBox)findViewById(R.id.contactInfoCheckBox);
        CheckBox googleCheckBox = (CheckBox)findViewById(R.id.googleCheckBox);


        askForPermissionAndWait(new String[]{Manifest.permission.WRITE_CONTACTS, Manifest.permission.READ_PHONE_STATE, Manifest.permission.GET_ACCOUNTS});

        final ContactSyncSettings settings = new ContactSyncSettings();
        final SharedPreferences filePreferences = getSharedPreferences(PREFS_NAME, 0);
        settings.setGoogle(filePreferences.getBoolean("google", false));
        settings.setFacebook(filePreferences.getBoolean("facebook", false));
        settings.setPhoneContactInfo(filePreferences.getBoolean("contactInfo", true));

        facebookCheckBox.setChecked(settings.isFacebook());
        googleCheckBox.setChecked(settings.isGoogle());
        contactInfoCheckBox.setChecked(settings.isPhoneContactInfo());

        facebookCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                settings.setFacebook(isChecked);
                filePreferences.edit().putBoolean("facebook", isChecked).commit();
            }
        });
        googleCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                settings.setGoogle(isChecked);
                filePreferences.edit().putBoolean("google", isChecked).commit();
            }
        });
        contactInfoCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                settings.setPhoneContactInfo(isChecked);
                filePreferences.edit().putBoolean("contactInfo", isChecked).commit();
            }
        });


        Contact c = SystemReader.readSystemContact(settings, this);


        SystemUpdater.addContactToSystem(c, this);

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
            requestPermissions(permissionNames,
                    REQUEST_CODE_ASK_PERMISSIONS);
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
        for (String permissionName : permissionNames) {
            int permission = checkSelfPermission(permissionName);
            if (permission != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;

    }
}
