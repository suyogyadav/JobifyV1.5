package com.kernel.jobify;

import android.app.NotificationChannel;
import android.app.NotificationManager;

import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.app.job.JobParameters;
import android.app.job.JobService;

import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;

import androidx.annotation.NonNull;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import android.util.Log;


import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class JobifyService extends JobService {

    int ct;
    int newchildcount;

    public static boolean isrunning;
    List<JobData> jobslist;
    IntentFilter intentFilter;

    @Override
    public void onCreate() {
        intentFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        super.onCreate();
    }



        public void Dbscan() {
            newchildcount = 0;
            isrunning = true;
            jobslist = new ArrayList<>();

            Log.i("ABCD", "Service Call Zhali");

            final SharedPreferences catcount = this.getSharedPreferences("catcount",0);
            final SharedPreferences catpref = this.getSharedPreferences("catpref",0);

            int c = catcount.getInt("count",0);
            for (int i=0;i<c;i++) {
                jobslist.clear();
                final String cat = catpref.getString("cat"+i,"null");
                Log.i("TYUI","called "+cat);
                if (cat != null && !cat.equals("null"))
                {
                    getntification(cat);
                }
            }
     }

    public void shownotification(String title,String disc,String link,String photolink,int id)
    {
        Log.i("newtest","shownotifcation called");
        Log.i("newtest",title);
        createchannel(title);
        JobData jobData = new JobData();
        jobData.setJobTitle(title);
        jobData.setJobDisc(disc);
        jobData.setJobLink(link);
        jobData.setJobPhotoLink(photolink);
        Intent intent = new Intent(this,JobDisActivity.class);
        intent.putExtra("jobdata",jobData);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addNextIntent(intent);

        PendingIntent pendingIntent = stackBuilder.getPendingIntent(0,PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), "notification")
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentIntent(pendingIntent)
                .setContentTitle(title)
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.notify(id, builder.build());
    }

    public void createchannel(String title)
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            NotificationChannel notificationChannel = new NotificationChannel("notification",title,NotificationManager.IMPORTANCE_DEFAULT);

            NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(notificationChannel);
            Log.i("newtest","notifcation channel created for "+title);
        }

    }

    public void getntification(String cat1)
    {
        final SharedPreferences oldcount1 = this.getSharedPreferences("oldcount",0);
        final SharedPreferences oldpointer1 = this.getSharedPreferences("oldpointer",0);
        final String cat = cat1;
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                final int oldcount = oldcount1.getInt(cat, 0);
                final int oldpointer = oldpointer1.getInt(cat, 0);
                DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference(cat);
                databaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        newchildcount = (int) dataSnapshot.getChildrenCount();
                        Log.i("TYUI", "on datachange is called");
                        Log.i("TYUI", "old pointer " + oldpointer);
                        Log.i("TYUI", "old count " + oldcount);
                        Log.i("TYUI", "new child count " + newchildcount);

                        if (newchildcount > oldcount) {
                            Log.i("TYUI", "New child count is greater than oldcount");
                            for (int i = oldpointer+1; i < newchildcount; i++) {
                                Log.i("TYUI", "Itrating through the avilable list of items");
                                String str = "" + i;
                                str = String.format("%03d", Integer.parseInt(str));
                                Log.i("ERTY", str);
                                JobData jobData = dataSnapshot.child("job" + str).getValue(JobData.class);
                                shownotification(jobData.getJobTitle(),jobData.getJobDisc(),jobData.getJobLink(),jobData.getJobPhotoLink(), ct++);
                            }
                            oldcount1.edit().putInt(cat, newchildcount).commit();
                            Log.i("TYUI",""+oldcount1.getInt(cat,0));
                            oldpointer1.edit().putInt(cat, newchildcount-1).commit();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });
    }

    @Override
    public boolean onStartJob(JobParameters params) {
        ct =0;
        ConnectivityManager cm = (ConnectivityManager) getBaseContext().getSystemService(getBaseContext().CONNECTIVITY_SERVICE);
        NetworkInfo info = cm.getActiveNetworkInfo();

        if (info!= null && info.isConnected()) {

            Log.i("qwer","JobService Called");

            Dbscan();
        }
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        if (jobslist!=null)
        {
            jobFinished(params,true );
            Log.i("qwer","JobService Finished");
        }
        Log.i("qwer","JobService unregisted");
        return true;
    }
}

