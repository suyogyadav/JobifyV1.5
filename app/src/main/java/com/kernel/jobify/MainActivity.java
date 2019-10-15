package com.kernel.jobify;

import android.content.Intent;
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

public class MainActivity extends AppCompatActivity {


    FirebaseDatabase database;
    DatabaseReference databaseRef;
    List<JobData> jobslist;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = new Intent(this,LoadingActivity.class);
        startActivityForResult(intent,10);

        jobslist = new ArrayList<>();

        database = FirebaseDatabase.getInstance();
        databaseRef = database.getReference("jobs");
        String[] title = {"News Title Here","News Title Here","News Title Here","News Title Here","News Title Here"};
        String[] disc = {"News Discription Here","News Discription Here","News Discription Here","News Discription Here","News Discription Here",};
        randomdata();
     //   pushjobs(title,disc);


        retrivedata();


        Log.i("ABCD",""+title[1]);
        RecyclerView res  = findViewById(R.id.rscview);
        res.setLayoutManager(new LinearLayoutManager(this));
        res.setAdapter(new NewsAdapter(title,disc));

/*        Intent intent = new Intent(this,JobifyService.class);
        startService(intent);
*/
    }

    public void randomdata()
    {
        String[] tit = {"something here","something here","something here","something here","something here"};
        String[] dis = {"something there","something there","something there","something there","something there"};

        JobData[] jobs = new JobData[tit.length];
        for (int i=0;i<tit.length;i++)
        {
            JobData job = new JobData();
            job.setJobTitle(tit[i]);
            job.setJobDisc(dis[i]);
            job.setJobLink(tit[i]);

            jobs[i] = job;
        }

        pushjobs(jobs);
    }

    public void pushjobs(JobData[] jobs)
    {
        Log.i("ABCD","push jobs call zhalay");
        for (int i=0;i<jobs.length;i++)
        {
            String id = databaseRef.push().getKey();
            Log.i("ABCD",""+id);
            databaseRef.child("job"+i).setValue(jobs[i]);
        }

    }


    public void retrivedata()
    {
        FirebaseDatabase dbinst = FirebaseDatabase.getInstance();
        databaseRef = FirebaseDatabase.getInstance().getReference("jobs");

        databaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                String[] title = {"News Title Here","News Title Here","News Title Here","News Title Here","News Title Here"};
                String[] disc = {"News Discription Here","News Discription Here","News Discription Here","News Discription Here","News Discription Here",};
                jobslist.clear(); /// remember remove this.

                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    JobData jobData = dataSnapshot1.getValue(JobData.class);
                    jobslist.add(jobData);
                    Log.i("ABCD",""+jobData.getJobTitle()+""+jobslist.size());
                }
                Log.i("ABCD",""+jobslist.size());

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

    /*public void pushjobs(String[] title,String[] disc)
    {
        Log.i("ABCD","push jobs call zhalay");
        for (int i=0;i<title.length;i++)
        {
            String id = databaseRef.push().getKey();
            Log.i("ABCD",""+id);
            databaseRef.child("job"+i).setValue(title[i]);
        }

    }*/
}
