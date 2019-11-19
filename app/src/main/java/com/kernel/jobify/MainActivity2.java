package com.kernel.jobify;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity2 extends AppCompatActivity {

    FirebaseDatabase database;
    DatabaseReference databaseRef;
    List<JobData> jobslist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_2);

        database = FirebaseDatabase.getInstance();
        databaseRef = database.getReference("jobs");

        jobslist = new ArrayList<>();
        retrivedata();

    }

    public void retrivedata()
    {
        FirebaseDatabase dbinst = FirebaseDatabase.getInstance();
        databaseRef = FirebaseDatabase.getInstance().getReference("jobs");

        databaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                jobslist.clear(); /// remember remove this.

                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    JobData jobData = dataSnapshot1.getValue(JobData.class);
                    jobslist.add(jobData);

                    Log.i("ABCD",""+dataSnapshot1.getKey());
                    Log.i("ABCD",""+jobData.getJobTitle()+""+jobslist.size());
                }
                Log.i("ABCD",""+jobslist.size());

                String[] title = new String[jobslist.size()];
                String[] disc = new String[jobslist.size()];

                for (int i=0;i<jobslist.size();i++)
                {
                    title[i] = jobslist.get(i).getJobTitle();
                    disc[i] = jobslist.get(i).getJobDisc();
                }

                RecyclerView res  = findViewById(R.id.rscview);
                res.setLayoutManager(new LinearLayoutManager(getBaseContext()));
                res.setAdapter(new NewsAdapter(title,disc));

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

}
