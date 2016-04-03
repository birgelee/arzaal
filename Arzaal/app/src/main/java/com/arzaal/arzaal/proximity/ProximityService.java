package com.arzaal.arzaal.proximity;

import android.app.Service;
import android.content.Intent;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.cardemulation.HostApduService;
import android.os.Bundle;
import android.os.IBinder;
import android.widget.Toast;

public class ProximityService extends HostApduService {
    public ProximityService() {
    }

    @Override
    public byte[] processCommandApdu(byte[] commandApdu, Bundle extras) {
        System.out.println("Got some APDU data!!!");
        Toast.makeText(this, "Data!", Toast.LENGTH_LONG).show();
        return new byte[0];
    }

    @Override
    public void onDeactivated(int reason) {
        System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!! deactivated.");
        Toast.makeText(this, "Service deactivated", Toast.LENGTH_LONG).show();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // Let it continue running until it is stopped.
        Toast.makeText(this, "Service Started", Toast.LENGTH_LONG).show();
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!! destroyed.");
        Toast.makeText(this, "Service Destroyed", Toast.LENGTH_LONG).show();
    }
}
