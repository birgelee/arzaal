package com.arzaal.arzaal;

import android.Manifest;
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
import android.widget.Toast;

import com.arzaal.arzaal.contact.Contact;
import com.arzaal.arzaal.contact.ContactSyncSettings;
import com.arzaal.arzaal.systemread.SystemReader;
import com.arzaal.arzaal.systemupdate.SystemUpdater;

public class SharingScreen extends AppCompatActivity {

    final private int REQUEST_CODE_ASK_PERMISSIONS = 123;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sharing_screen);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        CheckBox facebookCheckBox = (CheckBox)findViewById(R.id.facebookCheckBox);
        CheckBox contactInfoCheckBox = (CheckBox)findViewById(R.id.contactInfoCheckBox);

        askForPermissionAndWait(new String[]{Manifest.permission.WRITE_CONTACTS, Manifest.permission.READ_PHONE_STATE, Manifest.permission.GET_ACCOUNTS});


        /*Contact c = new Contact();
        c.setName("Joe Blow3");
        c.setEmail("test@email.com");
        c.setPhone("2134531222");*/

        ContactSyncSettings settings = new ContactSyncSettings();
        if (facebookCheckBox.isChecked()) {
            settings.setFacebook(true);
        } else {
            settings.setFacebook(false);
        }
        if (contactInfoCheckBox.isChecked()) {
            settings.setPhoneContactInfo(true);
        } else {
            settings.setPhoneContactInfo(false);
        }


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
