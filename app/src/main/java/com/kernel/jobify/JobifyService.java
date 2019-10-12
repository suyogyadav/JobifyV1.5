package com.kernel.jobify;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;


import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class JobifyService extends Service {
    DatabaseReference databaseRef;
    public static boolean isrunning;

    public JobifyService()
    {
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

        Log.i("ABCD","Service has been Started");
        databaseRef = FirebaseDatabase.getInstance().getReference("jobs");

        databaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                ConnectivityManager cm = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
                boolean netconnected = cm.getActiveNetworkInfo().isConnected();

                NotificationCompat.Builder builder = new NotificationCompat.Builder(getBaseContext())
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle("internet connected")
                        .setContentText("internet connected")
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT);

                Intent intent = new Intent(getBaseContext(),MainActivity.class);
                PendingIntent pi = PendingIntent.getActivity(getBaseContext(), 0, intent,PendingIntent.FLAG_ONE_SHOT);
                builder.setContentIntent(pi);
                if(netconnected) {
                    NotificationManager mNotificationManager =
                            (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                    mNotificationManager.notify(0, builder.build());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return super.onStartCommand(intent, flags, startId);
    }
}
