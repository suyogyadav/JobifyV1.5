package com.kernel.jobify;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class MainActivity extends AppCompatActivity
{
    boolean firstTime;
    SharedPreferences settings;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        settings = this.getSharedPreferences("appInfo", 0);
        firstTime = settings.getBoolean("first_time", true);

        scheduleJob();

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //startActivity(new Intent(getBaseContext(),MainActivity2.class));
                if (firstTime) {
                    SharedPreferences.Editor editor = settings.edit();
                    editor.putBoolean("first_time", false);
                    editor.commit();

                    startActivity(new Intent(getBaseContext(), JobDisActivity.class));
                }
                else {
                    setContentView(R.layout.maincatlayout);
                }

                Log.i("poiu","activity sleeping");
            }
        }, 3000);

    }
    public void btnIT(View view)
    {
        Intent intent = new Intent(this,MainActivity2.class);
        intent.putExtra("CAT","IT");
        startActivity(intent);
    }
    public void btnECS(View view)
    {
        Intent intent = new Intent(this,MainActivity2.class);
        intent.putExtra("CAT","ECS");
        startActivity(intent);
    }
    public void scheduleJob()
    {
        JobInfo myjob = new JobInfo.Builder(0,new ComponentName(this,JobifyService.class))
                .setPeriodic(900000)
                .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
                .setPersisted(true)
                .build();

        JobScheduler jobScheduler = (JobScheduler) getSystemService(this.JOB_SCHEDULER_SERVICE);
        jobScheduler.schedule(myjob);
    }
}
