package com.kernel.jobify;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
{

    Context context;
    boolean firstTime;
    SharedPreferences settings;
    SharedPreferences catpref;
    SharedPreferences catcount;
    int temp;
    int fori;

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
        final ArrayList<String> lst = ListAdapterPref.msg();
        if (lst.size()==0)
        {
            Toast.makeText(this,"Please Select At Least One Preference",Toast.LENGTH_SHORT).show();
        }
        else {
            editor1.putInt("count", lst.size());
            editor1.commit();
            temp = 0;
            for (fori = 0; fori < lst.size(); fori++) {
                editor.putString("cat" + fori, lst.get(fori));
                Log.i("TYUI", lst.get(fori));
                runthread(lst.get(fori));
            }
            editor.commit();
            setContentView(R.layout.maincatlayout);
        }
    }
    public void runthread(final String abcd)
    {
        final SharedPreferences oldcount1 = this.getSharedPreferences("oldcount",0);
        final SharedPreferences oldpointer1 = this.getSharedPreferences("oldpointer",0);
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(abcd);
                databaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        int count =(int) dataSnapshot.getChildrenCount();
                        oldcount1.edit().putInt(abcd,count).commit();
                        oldpointer1.edit().putInt(abcd,count).commit();
                        Log.i("ERTY",abcd+"put shared pref called");
                        temp++;
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });
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

    public void btnMECH(View view)
    {
        Intent intent = new Intent(this,MainActivity2.class);
        intent.putExtra("CAT","MECH");
        startActivity(intent);
    }

    public void btnCIVIL(View view)
    {
        Intent intent = new Intent(this,MainActivity2.class);
        intent.putExtra("CAT","CIVIL");
        startActivity(intent);
    }

    public void btnGOVT(View view)
    {
        Intent intent = new Intent(this,MainActivity2.class);
        intent.putExtra("CAT","GOVT");
        startActivity(intent);
    }

    public void btnINTERN(View view)
    {
        Intent intent = new Intent(this,MainActivity2.class);
        intent.putExtra("CAT","INTERN");
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
