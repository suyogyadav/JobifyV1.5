package com.kernel.jobify;

import android.content.Context;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity2 extends AppCompatActivity {

    DatabaseReference databaseRef;
    List<JobData> jobslist;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_2);

        context = this;

        jobslist = new ArrayList<>();
        retrivedata(getIntent().getStringExtra("CAT"));

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
                    setContentView(R.layout.blnt);
                }
                else{
                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {

                        JobData jobData = dataSnapshot1.getValue(JobData.class);
                        jobslist.add(jobData);

                        Log.i("ABCD", "" + dataSnapshot1.getKey());
                        Log.i("ABCD", "" + jobData.getJobTitle() + "" + jobslist.size());
                    }
                    Log.i("ABCD", "" + jobslist.size());

                    String[] title = new String[jobslist.size()];
                    String[] disc = new String[jobslist.size()];
                    String[] photolink = new String[jobslist.size()];

                    int j = 0;

                    for (int i = jobslist.size() - 1; i >= 0; i--) {
                        title[i] = jobslist.get(j).getJobTitle();
                        disc[i] = jobslist.get(j).getJobDisc();
                        photolink[i] = jobslist.get(j).getJobPhotoLink();
                        j++;
                    }

                    RecyclerView res = findViewById(R.id.rscview);
                    res.setLayoutManager(new LinearLayoutManager(getBaseContext()));
                    res.setAdapter(new NewsAdapter(title, disc, photolink));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

}

