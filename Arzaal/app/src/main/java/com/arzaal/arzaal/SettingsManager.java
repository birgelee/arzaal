package com.arzaal.arzaal;

import android.app.Activity;
import android.content.SharedPreferences;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.arzaal.arzaal.contact.ContactSyncSettings;

/**
 * Created by henry on 4/2/16.
 */
public class SettingsManager {

    public static final String PREFS_NAME = "MainSettings";

    public static ContactSyncSettings manageSettings(Activity activity) {
        CheckBox facebookCheckBox = (CheckBox)activity.findViewById(R.id.facebookCheckBox);
        CheckBox contactInfoCheckBox = (CheckBox)activity.findViewById(R.id.contactInfoCheckBox);
        CheckBox googleCheckBox = (CheckBox)activity.findViewById(R.id.googleCheckBox);



        final ContactSyncSettings settings = new ContactSyncSettings();
        final SharedPreferences filePreferences = activity.getSharedPreferences(PREFS_NAME, 0);
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

        return settings;
    }
}
