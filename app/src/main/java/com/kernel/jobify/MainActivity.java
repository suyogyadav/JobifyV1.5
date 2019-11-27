package com.kernel.jobify;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
{

    Context context;
    boolean firstTime;
    SharedPreferences settings;
    SharedPreferences catpref;
    SharedPreferences catcount;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        scheduleJob();

        settings = this.getSharedPreferences("appInfo", 0);
        firstTime = settings.getBoolean("first_time", true);

        context = this;

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (firstTime) {
                    SharedPreferences.Editor editor = settings.edit();
                    editor.putBoolean("first_time", false);
                    editor.commit();

                    setContentView(R.layout.preference_layout);
                    RecyclerView res = findViewById(R.id.preflist);
                    res.setLayoutManager(new LinearLayoutManager(getBaseContext()));
                    res.setAdapter(new ListAdapterPref(getBaseContext()));
                }
                else {
                    setContentView(R.layout.maincatlayout);
                }

                Log.i("poiu","activity sleeping");
            }
        }, 3000);

    }

    public void PrefSubmit(View view)
    {
        catpref = this.getSharedPreferences("catpref",0);
        catcount = this.getSharedPreferences("catcount",0);
        SharedPreferences.Editor editor = catpref.edit();
        SharedPreferences.Editor editor1 = catcount.edit();
        ArrayList<String> lst = ListAdapterPref.msg();
        editor1.putInt("count",lst.size());
        editor1.commit();
        for (int i=0;i<lst.size();i++)
        {
                editor.putString("cat"+i, lst.get(i));
                Log.i("TYUI",lst.get(i));
        }
        editor.commit();
        setContentView(R.layout.maincatlayout);
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
