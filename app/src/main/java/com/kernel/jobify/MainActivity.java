package com.kernel.jobify;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {


    FirebaseDatabase database;
    DatabaseReference databaseRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        database = FirebaseDatabase.getInstance();
        databaseRef = database.getReference("jobs");
        String[] title = {"News Title Here","News Title Here","News Title Here","News Title Here","News Title Here"};
        String[] disc = {"News Discription Here","News Discription Here","News Discription Here","News Discription Here","News Discription Here",};
        pushjobs(title,disc);
        Log.i("ABCD","he tr dis bc");
        RecyclerView res  = findViewById(R.id.rscview);
        res.setLayoutManager(new LinearLayoutManager(this));
        res.setAdapter(new NewsAdapter(title,disc));

    }

    public void pushjobs(String[] title,String[] disc)
    {
        Log.i("ABCD","push jobs call zhalay");
        for (int i=0;i<title.length;i++)
        {
            String id = databaseRef.push().getKey();
            Log.i("ABCD",""+id);
            databaseRef.child("job"+i).setValue(title[i]);

        }

    }
}
