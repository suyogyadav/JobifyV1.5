package com.kernel.jobify;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class BookMark extends AppCompatActivity {

    SharedPreferences bookmarks;
    List<Object> jobslist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookmark);
        jobslist = new ArrayList<>();
        bookmarks = getSharedPreferences("bookmarks", 0);
        String abcd = bookmarks.getString("book", "book");
        if (!abcd.equals("book")) {
            String[] xyz = abcd.split("^^");
            if (xyz.length > 0) {
                for (int i = 0; i < xyz.length; i++) {
                    String[] params = xyz[i].split("##");
                    JobData jobData = new JobData();
                    jobData.setJobTitle(params[0]);
                    jobData.setJobDisc(params[1]);
                    jobData.setJobLink(params[2]);
                    jobData.setJobPhotoLink(params[3]);
                    jobslist.add(jobData);
                }
                RecyclerView recyclerView = findViewById(R.id.recycleviewbook);
                GridLayoutManager manager = new GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false);
                recyclerView.setLayoutManager(manager);
                recyclerView.setAdapter(new NewsAdapter(jobslist, this));
            } else {
                Toast.makeText(this, "No Bookmarks added yet", Toast.LENGTH_LONG).show();
            }
        }
        else
        {
            Toast.makeText(this, "No Bookmarks added yet", Toast.LENGTH_LONG).show();
        }
    }
}
