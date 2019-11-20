package com.kernel.jobify;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Intent;

import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.IBinder;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.telecom.ConnectionService;
import android.util.Log;


import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class JobifyService extends JobService {
    DatabaseReference databaseRef;
    public static boolean isrunning;
    public int oldsize = 5;
    List<JobData> jobslist;
    JobifyBrodcastReciever myreciever;
    IntentFilter intentFilter;

    @Override
    public void onCreate() {
        myreciever = new JobifyBrodcastReciever();
        intentFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        super.onCreate();
    }



        public void Dbscan() {
            isrunning = true;
            jobslist = new ArrayList<>();

            Log.i("ABCD", "Service Call Zhali");


            FirebaseDatabase dbinst = FirebaseDatabase.getInstance();
            databaseRef = FirebaseDatabase.getInstance().getReference("jobs");

            databaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    jobslist.clear(); /// remember remove this.

                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                        JobData jobData = dataSnapshot1.getValue(JobData.class);
                        jobslist.add(jobData);

                        Log.i("newABCD",""+dataSnapshot1.getKey());
                        Log.i("newABCD",""+jobData.getJobTitle()+""+jobslist.size());
                    }
                    Log.i("newABCD",""+jobslist.size());

                    if (jobslist.size()>oldsize)
                    {
                        int j=0;
                        for(int i=oldsize;i<jobslist.size();i++)
                        {

                            shownotification(jobslist.get(i).getJobTitle(), jobslist.get(i).getJobDisc(),j);
                            j++;
                        }
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
     }

    public void shownotification(String title, String text,int id)
    {
        createchannel(title,text);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), "notification")
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle(title)
                .setContentText(text)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getApplicationContext());
        notificationManager.notify(id, builder.build());
    }

    public void createchannel(String title,String text)
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            NotificationChannel notificationChannel = new NotificationChannel("notification",title,NotificationManager.IMPORTANCE_DEFAULT);
            notificationChannel.setDescription(text);

            NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(notificationChannel);
        }

    }

    @Override
    public boolean onStartJob(JobParameters params) {
        ConnectivityManager cm = (ConnectivityManager) getBaseContext().getSystemService(getBaseContext().CONNECTIVITY_SERVICE);
        NetworkInfo info = cm.getActiveNetworkInfo();

        if (info!= null && info.isConnected()) {

            Log.i("qwer","JobService Called");

            Dbscan();
            if (jobslist!=null && jobslist.size()>oldsize)
            {
                jobFinished(params,true );
                Log.i("qwer","JobService Finished");
            }

        }
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        Log.i("qwer","JobService unregisted");
        return true;
    }
}

