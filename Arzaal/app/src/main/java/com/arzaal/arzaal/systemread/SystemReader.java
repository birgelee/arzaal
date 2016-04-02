package com.arzaal.arzaal.systemread;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.telephony.TelephonyManager;

import com.arzaal.arzaal.contact.Contact;
import com.arzaal.arzaal.contact.ContactSyncSettings;

/**
 * Created by henry on 4/2/16.
 */
public class SystemReader {
    private Contact readSystemContactPrivate(ContactSyncSettings settings, Activity activity) {
        String name = "";
        String phoneNumber = "";


        Cursor phones = activity.getContentResolver().query(ContactsContract.Profile.CONTENT_URI, null,null,null, null);
        while (phones.moveToNext())
        {
            name = phones.getString(phones.getColumnIndex(ContactsContract.Profile.DISPLAY_NAME));

        }
        phones.close();

        TelephonyManager tMgr = (TelephonyManager)activity.getSystemService(Context.TELEPHONY_SERVICE);
        phoneNumber = tMgr.getLine1Number();


        Contact res = new Contact();
        res.setPhone(phoneNumber);
        if (settings.isPhoneContactInfo()) {
            res.setName(name);
        } else {
            res.setName("fake name");
        }

        if (settings.isFacebook()) {
            res.setFacebookName("test");
        }

        // Not used, might be used later to add accounts.
        AccountManager am = AccountManager.get(activity);
        Account[] accounts = am.getAccounts();

        for (Account ac : accounts) {
            String acname = ac.name;
            String actype = ac.type;
            // Take your time to look at all available accounts
            System.out.println("Accounts : " + acname + ", " + actype);
        }
        return res;
    }
    public static Contact readSystemContact(ContactSyncSettings settings, Activity activity) {
        return (new SystemReader()).readSystemContactPrivate(settings, activity);
    }
}
