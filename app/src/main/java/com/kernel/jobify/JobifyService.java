package com.kernel.jobify;


import android.app.Service;

import android.content.Intent;

import android.os.IBinder;

import android.support.annotation.Nullable;

import android.util.Log;


import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class JobifyService extends Service {
    DatabaseReference databaseRef;
    public static boolean isrunning;

    public JobifyService() {
        isrunning = false;
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        isrunning = true;

        Log.i("ABCD", "Service has been Started");
        FirebaseDatabase dbinst = FirebaseDatabase.getInstance();
        databaseRef = FirebaseDatabase.getInstance().getReference("jobs");


        return super.onStartCommand(intent, flags, startId);
    }
}
