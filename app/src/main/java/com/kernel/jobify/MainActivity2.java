package com.kernel.jobify;

import android.content.Context;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity2 extends AppCompatActivity {

    DatabaseReference databaseRef;
    List<JobData> jobslist;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        context = this;

        ConnectivityManager cm = (ConnectivityManager) getBaseContext().getSystemService(getBaseContext().CONNECTIVITY_SERVICE);
        NetworkInfo info = cm.getActiveNetworkInfo();


        if (info!= null && info.isConnected()) {
            setContentView(R.layout.activity_main_2);

            Toolbar toolbar = findViewById(R.id.newstoolbar);
            toolbar.setNavigationIcon(R.drawable.ic_action_back);

            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });


            jobslist = new ArrayList<>();
            retrivedata(getIntent().getStringExtra("CAT"));
        }
        else
        {
            setContentView(R.layout.blnt);
            Toolbar toolbar = findViewById(R.id.blnttoolbar);
            toolbar.setNavigationIcon(R.drawable.ic_action_back);

            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }

    }

    public void retrivedata(String CAT)
    {
        databaseRef = FirebaseDatabase.getInstance().getReference(CAT);

        databaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                jobslist.clear(); /// remember remove this.

                if(dataSnapshot.getChildrenCount()==1)
                {
                    Toast.makeText(context,"Sorry No Jobs Avilable At Time",Toast.LENGTH_SHORT).show();
                }
                else{
                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {

                        JobData jobData = dataSnapshot1.getValue(JobData.class);
                        jobslist.add(jobData);

                        Log.i("ABCD", "" + dataSnapshot1.getKey());
                        Log.i("ABCD", "" + jobData.getJobTitle() + "" + jobslist.size());
                    }
                    Log.i("ABCD", "" + jobslist.size());

                    Collections.reverse(jobslist);

                    RecyclerView res = findViewById(R.id.rscview);
                    res.setLayoutManager(new LinearLayoutManager(getBaseContext()));
                    res.setAdapter(new NewsAdapter(jobslist,getBaseContext()));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

}

