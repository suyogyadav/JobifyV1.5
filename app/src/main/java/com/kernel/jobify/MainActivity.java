package com.kernel.jobify;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String[] title = {"News Title Here","News Title Here","News Title Here","News Title Here","News Title Here"};
        String[] disc = {"News Discription Here","News Discription Here","News Discription Here","News Discription Here","News Discription Here",};
        Log.i("ABCD","he tr dis bc");
        RecyclerView res  = findViewById(R.id.rscview);
        res.setLayoutManager(new LinearLayoutManager(this));
        res.setAdapter(new NewsAdapter(title,disc));

    }
}
