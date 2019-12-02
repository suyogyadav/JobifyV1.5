package com.kernel.jobify;

import android.app.NotificationChannel;
import android.app.NotificationManager;

import android.app.job.JobParameters;
import android.app.job.JobService;

import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;

import android.support.annotation.NonNull;

import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
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

            SharedPreferences catcount = this.getSharedPreferences("catcount",0);
            SharedPreferences catpref = this.getSharedPreferences("catpref",0);

            int c = catcount.getInt("count",0);
            for (int i=0;i<c;i++) {
                jobslist.clear();
                final String cat = catpref.getString("cat"+i,"null");
                Log.i("TYUI","called "+cat);
                if (cat != null && !cat.equals("null"))
                {
                    getntification(cat);
                   /* databaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            jobslist.clear(); /// remember remove this.

                            for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                JobData jobData = dataSnapshot1.getValue(JobData.class);
                                jobslist.add(jobData);

                                Log.i("newABCD", "" + dataSnapshot1.getKey());
                                Log.i("newABCD", "" + jobData.getJobTitle() + "" + jobslist.size());
                            }
                            Log.i("newABCD", "" + jobslist.size());

                            if (jobslist.size() > oldsize) {
                                int j = 0;
                                for (int i = oldsize; i < jobslist.size(); i++) {

                                    shownotification(jobslist.get(i).getJobTitle(), jobslist.get(i).getJobDisc(), j);
                                    j++;
                                }
                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });*/

                }
            }
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
                        Log.i("TYUI", "on datachange is it called");
                        Log.i("TYUI", "old pointer " + oldpointer);
                        Log.i("TYUI", "old count " + oldcount);
                        Log.i("TYUI", "new child count " + newchildcount);

                        if (newchildcount > oldcount) {
                            Log.i("TYUI", "New child count is greater than oldcount");
                            for (int i = oldpointer; i < newchildcount; i++) {
                                Log.i("TYUI", "Itrating through the avilable list of items");
                                String str = "" + i;
                                str = String.format("%03d", Integer.parseInt(str));
                                Log.i("ERTY", str);
                                JobData jobData = dataSnapshot.child("job" + str).getValue(JobData.class);
                                shownotification(jobData.getJobTitle(), jobData.getJobDisc(), ct++);
                            }
                            oldcount1.edit().putInt(cat, newchildcount).commit();
                            Log.i("TYUI",""+oldcount1.getInt(cat,0));
                            oldpointer1.edit().putInt(cat, newchildcount).commit();
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
            if (jobslist!=null)
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

