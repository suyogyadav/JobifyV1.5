package com.kernel.jobify;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BookMark extends AppCompatActivity {

    SharedPreferences bookmarks;
    List<Object> jobslist;
    RecyclerView recyclerView;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookmark);
        context = this;
        Toolbar toolbar = findViewById(R.id.booktoolbar);
        toolbar.setNavigationIcon(R.drawable.ic_action_back);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        recyclerView = findViewById(R.id.recycleviewbook);
        GridLayoutManager manager = new GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(manager);

        jobslist = new ArrayList<>();
        bookmarks = getSharedPreferences("bookmarks", 0);
        String abcd = bookmarks.getString("book", "nobooks");
        DatabaseReference ref;
        if (!abcd.equals("nobooks") && !abcd.equals("")) {
            String[] xyz = abcd.split(";;");
            if (xyz.length > 0) {
                Log.i("DIVINE", "" + xyz.length);
                for (int i = 0; i < xyz.length; i++) {
                    final String[] newstr = xyz[i].split("##");
                    Log.i("DIVINE", "" + newstr[0]);
                    Log.i("DIVINE", "" + newstr[1]);
                    ref = FirebaseDatabase.getInstance().getReference(newstr[0] + "/" + newstr[1]);
                    ref.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            JobData data = dataSnapshot.getValue(JobData.class);
                            if (data != null) {
                                data.setJobCat(newstr[0]);
                                data.setJobKey(newstr[1]);
                                Log.i("DIVINE", "" + data.getJobTitle());
                            } else {
                                Log.i("DIVINE", "data is null");
                            }
                            Log.i("DIVINE", "" + dataSnapshot.getChildrenCount());
                            jobslist.add(data);
                            Collections.reverse(jobslist);
                            recyclerView.setAdapter(new NewsAdapter(jobslist, context));
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

//                    String[] params = xyz[i].split("##");
//                    JobData jobData = new JobData();
//                    jobData.setJobTitle(params[0]);
//                    Log.i("DIVINE",""+params[0]);
//                    jobData.setJobDisc(params[1]);
//                    Log.i("DIVINE",""+params[1]);
//                    jobData.setJobLink(params[2]);
//                    Log.i("DIVINE",""+params[2]);
//                    jobData.setJobPhotoLink(params[3]);
//                    Log.i("DIVINE",""+params[3]);
//                    jobslist.add(jobData);
                }
                Log.i("DIVINE", "" + jobslist.size());
//                TextView textView = findViewById(R.id.booktext);
//                textView.setText(test);
            } else {
                Toast.makeText(this, "No Bookmarks added yet", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(this, "No Bookmarks added yet", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        recyclerView = findViewById(R.id.recycleviewbook);
        GridLayoutManager manager = new GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(manager);

        jobslist = new ArrayList<>();
        bookmarks = getSharedPreferences("bookmarks", 0);
        String abcd = bookmarks.getString("book", "nobooks");
        DatabaseReference ref;
        if (!abcd.equals("nobooks") && !abcd.equals("")) {
            String[] xyz = abcd.split(";;");
            if (xyz.length > 0) {
                Log.i("DIVINE", "" + xyz.length);
                for (int i = 0; i < xyz.length; i++) {
                    final String[] newstr = xyz[i].split("##");
                    Log.i("DIVINE", "" + newstr[0]);
                    Log.i("DIVINE", "" + newstr[1]);
                    ref = FirebaseDatabase.getInstance().getReference(newstr[0] + "/" + newstr[1]);
                    ref.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            JobData data = dataSnapshot.getValue(JobData.class);
                            if (data != null) {
                                data.setJobCat(newstr[0]);
                                data.setJobKey(newstr[1]);
                                Log.i("DIVINE", "" + data.getJobTitle());
                            } else {
                                Log.i("DIVINE", "data is null");
                            }
                            Log.i("DIVINE", "" + dataSnapshot.getChildrenCount());
                            jobslist.add(data);
                            Collections.reverse(jobslist);
                            recyclerView.setAdapter(new NewsAdapter(jobslist, context));
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

//                    String[] params = xyz[i].split("##");
//                    JobData jobData = new JobData();
//                    jobData.setJobTitle(params[0]);
//                    Log.i("DIVINE",""+params[0]);
//                    jobData.setJobDisc(params[1]);
//                    Log.i("DIVINE",""+params[1]);
//                    jobData.setJobLink(params[2]);
//                    Log.i("DIVINE",""+params[2]);
//                    jobData.setJobPhotoLink(params[3]);
//                    Log.i("DIVINE",""+params[3]);
//                    jobslist.add(jobData);
                }
                Log.i("DIVINE", "" + jobslist.size());
//                TextView textView = findViewById(R.id.booktext);
//                textView.setText(test);
            } else {
                jobslist.clear();
                recyclerView.setAdapter(new NewsAdapter(jobslist, context));
                Toast.makeText(this, "No Bookmarks added yet", Toast.LENGTH_LONG).show();
            }
        } else {
            jobslist.clear();
            recyclerView.setAdapter(new NewsAdapter(jobslist, context));
            Toast.makeText(this, "No Bookmarks added yet", Toast.LENGTH_LONG).show();

        }
    }
}
