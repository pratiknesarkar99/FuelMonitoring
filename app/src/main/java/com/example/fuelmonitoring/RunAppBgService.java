package com.example.fuelmonitoring;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.widget.Toast;

import com.MyApp;

public class RunAppBgService extends Service {
    public RunAppBgService() {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        onTaskRemoved(intent);

        //Toast.makeText(getApplicationContext(), "Fuel Monitoring service is  running...", Toast.LENGTH_SHORT).show();

        return START_STICKY;
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {

        Intent restartService = new Intent(MyApp.getContext(),  this.getClass());
        restartService.setPackage(getPackageName());
        startService(restartService);

        super.onTaskRemoved(rootIntent);
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
