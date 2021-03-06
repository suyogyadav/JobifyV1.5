package com.kernel.jobify;

import android.content.Context;

import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;

import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;

import android.view.MenuItem;
import android.view.View;

import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import com.google.android.material.navigation.NavigationView;
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
    List<Object> jobslist;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        MobileAds.initialize(this, "ca-app-pub-8714980968157209~5975555023");
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


    public void retrivedata(final String CAT) {
        databaseRef = FirebaseDatabase.getInstance().getReference(CAT);

        databaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                jobslist.clear();

                if (dataSnapshot.getChildrenCount() == 0) {
                    Toast.makeText(context, "Sorry No Jobs Avilable At Time", Toast.LENGTH_SHORT).show();
                } else {
                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                        JobData jobData = dataSnapshot1.getValue(JobData.class);
                        jobData.setJobKey(dataSnapshot1.getKey());
                        jobData.setJobCat(CAT);
                        jobslist.add(jobData);

                        Log.i("ABCD", "" + dataSnapshot1.getKey());
                        Log.i("DIVINE", "" + jobData.getJobKey());
                        Log.i("ABCD", "" + jobData.getJobTitle());
                    }

                    Collections.reverse(jobslist);
                    jobslist = getbannerad(jobslist);
                    jobslist = loadads(jobslist);
                    printjoblist(jobslist);
                    RecyclerView res = findViewById(R.id.rscview);

                    GridLayoutManager manager = new GridLayoutManager(getBaseContext(), 2, GridLayoutManager.VERTICAL, false);
                    manager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                        @Override
                        public int getSpanSize(int position) {
                            switch ((position + 1) % 5) {
                                case 0:
                                    return 2;
                                case 1:
                                case 2:
                                case 3:
                                case 4:
                                    return 1;

                                default:
                                    return 1;
                            }
                        }
                    });
                    res.setLayoutManager(manager);
                    res.setAdapter(new NewsAdapter(jobslist, getBaseContext()));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


    public void printjoblist(List<Object> lst) {
        for (int i = 0; i < lst.size(); i++) {
            Object item = lst.get(i);
            if (item instanceof AdView) {
                Log.i("Joblist", "adview " + i);
            } else {
                Log.i("Joblist", "newsview " + i);
            }
        }
    }

    public List<Object> getbannerad(List<Object> jobslist) {

        for (int i = 4; i < jobslist.size(); i = i + 5) {
            final AdView adView = new AdView(this);
            adView.setAdSize(new AdSize(300, 250));
            adView.setAdUnitId("ca-app-pub-8714980968157209/3058581792");
            Log.i("ABCD", "AD IS GETTING PLACED" + i);
            jobslist.add(i, adView);
        }
        return jobslist;

    }

    public List<Object> loadads(List<Object> jobslist) {
        for (int i = 0; i < jobslist.size(); i++) {
            Object item = jobslist.get(i);
            if (item instanceof AdView) {
                final AdView adView = (AdView) item;
                adView.loadAd(new AdRequest.Builder().build());
            }
        }
        return jobslist;
    }


}

