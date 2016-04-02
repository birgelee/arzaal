package com.arzaal.arzaal.systemread;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.telephony.TelephonyManager;

import com.arzaal.arzaal.contact.Contact;

/**
 * Created by henry on 4/2/16.
 */
public class SystemReader {
    private Contact readSystemContactPrivate(Activity activity) {
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
        res.setName(name);
        return res;
    }
    public static Contact readSystemContact(Activity activity) {
        return (new SystemReader()).readSystemContactPrivate(activity);
    }
}
