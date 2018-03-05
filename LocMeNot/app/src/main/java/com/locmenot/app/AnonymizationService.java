package com.locmenot.app;

import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

public class AnonymizationService extends Service {

    LocationAnonymizer locationAnonymizer;
    private final IBinder mBinder = new LocalBinder();

    public class LocalBinder extends Binder {
        AnonymizationService getService() {
            return AnonymizationService.this;
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        locationAnonymizer.stopMockLocs();
        //stopService();
        Toast.makeText(this, "Anonymization Service Stopped", Toast.LENGTH_SHORT).show();
    }

    @Override
    public IBinder onBind(Intent intent) {
        Toast.makeText(this, "Anonymization Started", Toast.LENGTH_SHORT).show();
        showNotif();

        try {
            int kValue = intent.getIntExtra("kValue", 1);
            locationAnonymizer = new LocationAnonymizer(this, this, kValue);
            Log.i("locationAnonymizer", "Alive and Well");
        }
        catch (Exception e) {
            String msg = (e.getMessage() == null)?"Failed to init.":e.getMessage();
            Log.i("Init error", msg);
        }
        return mBinder;
    }

    // Stop the service from running
    public void stopService() {
        locationAnonymizer.stopMockLocs();
    }

    private void showNotif() {
        //Intent intent = new Intent(INTENT_FILTER);
        //intent.putExtra("action", "stopService");

        Notification notif = new Notification.Builder(this)
                .setContentTitle("LPAnon")
                .setSmallIcon(R.drawable.common_full_open_on_phone)
                .build();

        // Do not let user clear notification
        notif.flags |= Notification.FLAG_NO_CLEAR;

        startForeground(1395, notif);
    }
}
