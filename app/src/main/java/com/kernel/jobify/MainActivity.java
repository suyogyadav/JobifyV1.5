package com.kernel.jobify;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.AsyncTask;
import android.os.Handler;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;


import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    Context context;
    boolean firstTime;
    SharedPreferences settings;
    SharedPreferences catpref;
    SharedPreferences catcount;
    int fori;
    int[] catlogo = {R.drawable.catlogo01, R.drawable.catlogo02, R.drawable.catlogo03, R.drawable.catlogo04, R.drawable.catlogo05, R.drawable.catlogo06};
    String[] showlist = {"IT", "Electronics", "Mechanical", "Civil", "Government", "Internship"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FirebaseMessaging.getInstance().setAutoInitEnabled(true);
        scheduleJob();
        MobileAds.initialize(this, "ca-app-pub-3940256099942544~3347511713");

        settings = this.getSharedPreferences("appInfo", 0);
        firstTime = settings.getBoolean("first_time", true);
        catcount = getSharedPreferences("catcount", 0);
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
                } else {
                    if (catcount.getInt("count", 0) == 0) {
                        setContentView(R.layout.preference_layout);
                        RecyclerView res = findViewById(R.id.preflist);
                        res.setLayoutManager(new LinearLayoutManager(getBaseContext()));
                        res.setAdapter(new ListAdapterPref(getBaseContext()));
                    } else {
                        setContentView(R.layout.maincatlayout);

                        SharedPreferences tockengen = getSharedPreferences("tockengen",0);
                        boolean avilable = tockengen.getBoolean("avilable",false);
                        if(avilable)
                        {
                            Log.i("alan","status avilable");
                            sendpreftoserver();
                            tockengen.edit().putBoolean("avilable",false).commit();
                        }
                        else {
                            Log.i("alan","send to server not called");
                        }

                        ImageView imageView1 = findViewById(R.id.catimg1);
                        imageView1.setImageBitmap(getRoundedCornerBitmap(BitmapFactory.decodeResource(getResources(), catlogo[0])));
                        ImageView imageView2 = findViewById(R.id.catimg2);
                        imageView2.setImageBitmap(getRoundedCornerBitmap(BitmapFactory.decodeResource(getResources(), catlogo[1])));
                        ImageView imageView3 = findViewById(R.id.catimg3);
                        imageView3.setImageBitmap(getRoundedCornerBitmap(BitmapFactory.decodeResource(getResources(), catlogo[2])));
                        ImageView imageView4 = findViewById(R.id.catimg4);
                        imageView4.setImageBitmap(getRoundedCornerBitmap(BitmapFactory.decodeResource(getResources(), catlogo[3])));
                        ImageView imageView5 = findViewById(R.id.catimg5);
                        imageView5.setImageBitmap(getRoundedCornerBitmap(BitmapFactory.decodeResource(getResources(), catlogo[4])));
                        ImageView imageView6 = findViewById(R.id.catimg6);
                        imageView6.setImageBitmap(getRoundedCornerBitmap(BitmapFactory.decodeResource(getResources(), catlogo[5])));

                        TextView textView1 = findViewById(R.id.catname1);
                        textView1.setText(showlist[0]);
                        TextView textView2 = findViewById(R.id.catname2);
                        textView2.setText(showlist[1]);
                        TextView textView3 = findViewById(R.id.catname3);
                        textView3.setText(showlist[2]);
                        TextView textView4 = findViewById(R.id.catname4);
                        textView4.setText(showlist[3]);
                        TextView textView5 = findViewById(R.id.catname5);
                        textView5.setText(showlist[4]);
                        TextView textView6 = findViewById(R.id.catname6);
                        textView6.setText(showlist[5]);

                        AdView adView = findViewById(R.id.adView);
                        AdRequest adRequest = new AdRequest.Builder().build();
                        adView.loadAd(adRequest);
                    }
                }

                Log.i("poiu", "activity sleeping");
            }
        }, 3000);

    }

    public void PrefSubmit(View view) {
        catpref = this.getSharedPreferences("catpref", 0);
        catcount = this.getSharedPreferences("catcount", 0);
        SharedPreferences.Editor editor = catpref.edit();
        SharedPreferences.Editor editor1 = catcount.edit();
        final ArrayList<String> lst = ListAdapterPref.msg();
        if (lst.size() == 0) {
            Toast.makeText(this, "Please Select At Least One Preference", Toast.LENGTH_SHORT).show();
        } else {
            editor1.putInt("count", lst.size());
            editor1.commit();
            for (fori = 0; fori < lst.size(); fori++) {
                editor.putString("cat" + fori, lst.get(fori));
                Log.i("TYUI", lst.get(fori));
                runthread(lst.get(fori));
            }
            editor.commit();
            SharedPreferences tockengen = getSharedPreferences("tockengen",0);
            boolean avilable = tockengen.getBoolean("avilable",false);
            if(avilable)
            {
                Log.i("alan","status avilable");
                sendpreftoserver();
                tockengen.edit().putBoolean("avilable",false).commit();
            }
            else {
                Log.i("alan","send to server not called");
            }
            setContentView(R.layout.maincatlayout);

            ImageView imageView1 = findViewById(R.id.catimg1);
            imageView1.setImageBitmap(getRoundedCornerBitmap(BitmapFactory.decodeResource(getResources(), catlogo[0])));
            ImageView imageView2 = findViewById(R.id.catimg2);
            imageView2.setImageBitmap(getRoundedCornerBitmap(BitmapFactory.decodeResource(getResources(), catlogo[1])));
            ImageView imageView3 = findViewById(R.id.catimg3);
            imageView3.setImageBitmap(getRoundedCornerBitmap(BitmapFactory.decodeResource(getResources(), catlogo[2])));
            ImageView imageView4 = findViewById(R.id.catimg4);
            imageView4.setImageBitmap(getRoundedCornerBitmap(BitmapFactory.decodeResource(getResources(), catlogo[3])));
            ImageView imageView5 = findViewById(R.id.catimg5);
            imageView5.setImageBitmap(getRoundedCornerBitmap(BitmapFactory.decodeResource(getResources(), catlogo[4])));
            ImageView imageView6 = findViewById(R.id.catimg6);
            imageView6.setImageBitmap(getRoundedCornerBitmap(BitmapFactory.decodeResource(getResources(), catlogo[5])));

            TextView textView1 = findViewById(R.id.catname1);
            textView1.setText(showlist[0]);
            TextView textView2 = findViewById(R.id.catname2);
            textView2.setText(showlist[1]);
            TextView textView3 = findViewById(R.id.catname3);
            textView3.setText(showlist[2]);
            TextView textView4 = findViewById(R.id.catname4);
            textView4.setText(showlist[3]);
            TextView textView5 = findViewById(R.id.catname5);
            textView5.setText(showlist[4]);
            TextView textView6 = findViewById(R.id.catname6);
            textView6.setText(showlist[5]);

            AdView adView = findViewById(R.id.adView);
            AdRequest adRequest = new AdRequest.Builder().build();
            adView.loadAd(adRequest);


        }
    }

    public void runthread(final String abcd) {
        final SharedPreferences oldcount1 = this.getSharedPreferences("oldcount", 0);
        final SharedPreferences oldpointer1 = this.getSharedPreferences("oldpointer", 0);
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(abcd);
                databaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        int count = (int) dataSnapshot.getChildrenCount();
                        oldcount1.edit().putInt(abcd, count).commit();
                        oldpointer1.edit().putInt(abcd, count - 1).commit();
                        Log.i("ERTY", abcd + "put shared pref called");
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });
    }

    public void btnIT(View view) {
        Intent intent = new Intent(this, MainActivity2.class);
        intent.putExtra("CAT", "IT");
        startActivity(intent);
    }

    public void btnECS(View view) {
        Intent intent = new Intent(this, MainActivity2.class);
        intent.putExtra("CAT", "ECS");
        startActivity(intent);
    }

    public void btnMECH(View view) {
        Intent intent = new Intent(this, MainActivity2.class);
        intent.putExtra("CAT", "MECH");
        startActivity(intent);
    }

    public void btnCIVIL(View view) {
        Intent intent = new Intent(this, MainActivity2.class);
        intent.putExtra("CAT", "CIVIL");
        startActivity(intent);
    }

    public void btnGOVT(View view) {
        Intent intent = new Intent(this, MainActivity2.class);
        intent.putExtra("CAT", "GOVT");
        startActivity(intent);
    }

    public void btnINTERN(View view) {
        Intent intent = new Intent(this, MainActivity2.class);
        intent.putExtra("CAT", "INTERN");
        startActivity(intent);
    }

    public static Bitmap getRoundedCornerBitmap(Bitmap bitmap) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
                bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);
        final float roundPx = 12;

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        return output;
    }

    public void scheduleJob() {
        JobInfo myjob = new JobInfo.Builder(0, new ComponentName(this, JobifyService.class))
                .setPeriodic(900000)
                .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
                .setPersisted(true)
                .build();

        JobScheduler jobScheduler = (JobScheduler) getSystemService(this.JOB_SCHEDULER_SERVICE);
        jobScheduler.schedule(myjob);
    }

    public void  sendpreftoserver()
    {
        Log.i("alan","inside send to server");
        Log.i("DKBOSE","sendpreftoserver");

        String s =FirebaseInstanceId.getInstance().getToken();


        SharedPreferences catpref = getSharedPreferences("catpref", 0);
        SharedPreferences catcount = getSharedPreferences("catcount", 0);

        int count = catcount.getInt("count", 0);

        for (int i = 0; i < count; i++) {
            String cat = catpref.getString("cat" + i, "nopref");
            if (!cat.equals("nopref")) {
                Log.i("DKBOSE","inside for loop");
                Log.i("DKBOSE","cat - "+cat);

                DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("Users/" + cat);
                String uid = myRef.push().getKey();
                Log.i("DKBOSE","uid - "+uid);
                Log.i("DKBOSE","token - "+s);
                myRef.child(uid).setValue(s);
            }
        }
    }
}
