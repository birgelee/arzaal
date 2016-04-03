package com.arzaal.arzaal;

import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NfcAdapter;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.arzaal.arzaal.contact.Contact;
import com.arzaal.arzaal.systemupdate.SystemUpdater;

public class AcceptContact extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accept_contact);
    }

    @Override
    protected void onResume(){
        super.onResume();
        Intent intent = getIntent();
        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(intent.getAction())) {
            Parcelable[] rawMessages = intent.getParcelableArrayExtra(
                    NfcAdapter.EXTRA_NDEF_MESSAGES);

            NdefMessage message = (NdefMessage) rawMessages[0]; // only one message transferred
            SystemUpdater.addContactToSystem(Contact.deserialize(new String(message.getRecords()[0].getPayload())), this);

        } else {
            Toast.makeText(this, "Sorry, you don't have NFC.", Toast.LENGTH_LONG).show();
        }

    }
}
