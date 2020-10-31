package com.kernel.jobify;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;

import androidx.annotation.NonNull;
import androidx.browser.customtabs.CustomTabsIntent;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import android.widget.ProgressBar;

import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;

import com.google.android.gms.ads.MobileAds;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    int versionflag = 0;
    DatabaseReference databaseRef;
    List<Object> jobslist;
    Context context;
    ProgressBar prgbar;
    boolean firstTime;
    SharedPreferences settings;
    SharedPreferences catpref;
    SharedPreferences catcount;
    int fori;
    NavigationView navigationView;
    DrawerLayout drawerLayout;
    FirebaseAnalytics analytics;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);
        FirebaseMessaging.getInstance().setAutoInitEnabled(true);
        //scheduleJob();
        CheckVersion();
        analytics = FirebaseAnalytics.getInstance(this);
        Bundle param = new Bundle();
        param.putString(FirebaseAnalytics.Event.APP_OPEN, "Main Activity");
        analytics.logEvent(FirebaseAnalytics.Event.APP_OPEN, param);

        MobileAds.initialize(this, "ca-app-pub-8714980968157209~5975555023");
        settings = this.getSharedPreferences("appInfo", 0);
        firstTime = settings.getBoolean("first_time", true);
        catcount = getSharedPreferences("catcount", 0);
        context = this;

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (versionflag == 0) {
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
                            //setContentView(R.layout.maincatlayout);
                            SharedPreferences tockengen = getSharedPreferences("tockengen", 0);
                            boolean avilable = tockengen.getBoolean("avilable", false);
                            if (avilable) {
                                Log.i("alan", "status avilable");
                                sendpreftoserver();
                                tockengen.edit().putBoolean("avilable", false).commit();
                            } else {
                                Log.i("alan", "send to server not called");
                            }

                            ConnectivityManager cm = (ConnectivityManager) getBaseContext().getSystemService(getBaseContext().CONNECTIVITY_SERVICE);
                            NetworkInfo info = cm.getActiveNetworkInfo();


                            if (info != null && info.isConnected()) {
                                CheckVersion();
                                //CheckVersion cv = new CheckVersion();
                                //if (cv.versionMatch(getApplicationContext())) {
                                setContentView(R.layout.activity_main);
                                navigationView = findViewById(R.id.nav_view);
                                drawerLayout = findViewById(R.id.drawer_layout);

                                prgbar = findViewById(R.id.prgbar);
                                prgbar.setVisibility(View.VISIBLE);

                                navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
                                    @Override
                                    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                                        switch (menuItem.getItemId()) {
                                            case R.id.navigation_cat:
                                                drawerLayout.closeDrawers();
                                                startActivity(new Intent(context, MainCatLayout.class));
                                                return true;

                                            case R.id.navigation_bookmark:
                                                drawerLayout.closeDrawers();
                                                startActivity(new Intent(context, BookMark.class));
                                                return true;

                                            case R.id.navigation_about_us:
                                                drawerLayout.closeDrawers();
                                                startActivity(new Intent(context, AboutUs.class));
                                                return true;

                                            case R.id.navigation_privacy:
                                                drawerLayout.closeDrawers();
                                                openbowser("https://jobify.co.in/privacy.html");
                                                return true;

                                            case R.id.navigation_dis:
                                                drawerLayout.closeDrawers();
                                                openbowser("https://jobify.co.in/dis.html");
                                                return true;
                                        }
                                        return false;
                                    }
                                });

                                jobslist = new ArrayList<>();
                                retrivedata(getcat());
//                            } else {
//                                setContentView(R.layout.version_update);
//                            }

                            } else {
                                setContentView(R.layout.blnt);
                            }

                        }
                    }
                } else {
                    setContentView(R.layout.version_update);
                }
                Log.i("poiu", "activity sleeping");
            }
        }, 3000);

    }

    public void openPlayStore(View view) {
        final String packname = getPackageName();
        try {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + packname)));
        } catch (ActivityNotFoundException enf) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + packname)));
        }
    }

    public void CheckVersion() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("AppVersion");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String abcd = snapshot.getValue(String.class);
                if (abcd != null && abcd.equals(getString(R.string.version))) {
                    Log.i("APP", abcd);
                    versionflag = 0;
                } else {
                    Log.i("APP", "NULL");
                    versionflag = 1;
                    // setContentView(R.layout.version_update);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void openbowser(String link) {
        Bundle param = new Bundle();
        param.putString("Job_Apply_Clicked", "apply clicked");
        analytics.logEvent("Job_Apply_Clicked", param);

        CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
        builder.setToolbarColor(getResources().getColor(R.color.colorPrimary));
        builder.addDefaultShareMenuItem();
        builder.setShowTitle(true);
        CustomTabsIntent customTabsIntent = builder.build();
        customTabsIntent.launchUrl(this, Uri.parse(link));
    }

    public void openDrawer(View view) {
        drawerLayout = findViewById(R.id.drawer_layout);
        drawerLayout.openDrawer(GravityCompat.START);
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
                        jobData.setJobCat(CAT);
                        jobData.setJobKey(dataSnapshot1.getKey());
                        jobslist.add(jobData);

                        Log.i("ABCD", "" + dataSnapshot1.getKey());
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
                    prgbar.setVisibility(View.INVISIBLE);
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


    public String getcat() {
        SharedPreferences catpref = getSharedPreferences("catpref", 0);
        SharedPreferences catcount = getSharedPreferences("catcount", 0);
        int count = catcount.getInt("count", 0);
        for (int i = 0; i < count; i++) {
            String cat = catpref.getString("cat" + i, "nopref");
            if (!cat.equals("nopref")) {
                return cat;
            } else {
                return "IT";
            }
        }
        return "IT";
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
                Bundle param = new Bundle();
                param.putString("Category_Selected", "" + lst.get(fori));
                analytics.logEvent("Category_Selected", param);
                //runthread(lst.get(fori));
            }
            editor.commit();
            SharedPreferences tockengen = getSharedPreferences("tockengen", 0);
            boolean avilable = tockengen.getBoolean("avilable", false);
            if (avilable) {
                Log.i("alan", "status avilable");
                sendpreftoserver();
                tockengen.edit().putBoolean("avilable", false).commit();
            } else {
                Log.i("alan", "send to server not called");
            }

            ConnectivityManager cm = (ConnectivityManager) getBaseContext().getSystemService(getBaseContext().CONNECTIVITY_SERVICE);
            NetworkInfo info = cm.getActiveNetworkInfo();

            if (info != null && info.isConnected()) {
                setContentView(R.layout.activity_main);
                navigationView = findViewById(R.id.nav_view);
                drawerLayout = findViewById(R.id.drawer_layout);

                prgbar = findViewById(R.id.prgbar);
                prgbar.setVisibility(View.VISIBLE);

                navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                        switch (menuItem.getItemId()) {
                            case R.id.navigation_cat:
                                drawerLayout.closeDrawers();
                                startActivity(new Intent(context, MainCatLayout.class));
                                return true;

                            case R.id.navigation_bookmark:
                                drawerLayout.closeDrawers();
                                startActivity(new Intent(context, BookMark.class));
                                return true;

                            case R.id.navigation_about_us:
                                drawerLayout.closeDrawers();
                                startActivity(new Intent(context, AboutUs.class));
                                return true;

                            case R.id.navigation_privacy:
                                drawerLayout.closeDrawers();
                                openbowser("https://jobify.co.in/privacy.html");
                                return true;

                            case R.id.navigation_dis:
                                drawerLayout.closeDrawers();
                                openbowser("https://jobify.co.in/dis.html");
                                return true;
                        }
                        return false;
                    }
                });
                jobslist = new ArrayList<>();
                retrivedata(getcat());
            } else {
                setContentView(R.layout.blnt);
            }

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

    public void sendpreftoserver() {
        Log.i("alan", "inside send to server");
        Log.i("DKBOSE", "sendpreftoserver");

        String s = FirebaseInstanceId.getInstance().getToken();


        SharedPreferences catpref = getSharedPreferences("catpref", 0);
        SharedPreferences catcount = getSharedPreferences("catcount", 0);

        int count = catcount.getInt("count", 0);

        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("Users/All_User");
        String unid = myRef.push().getKey();
        myRef.child(unid).setValue(s);

        for (int i = 0; i < count; i++) {
            String cat = catpref.getString("cat" + i, "nopref");
            if (!cat.equals("nopref")) {
                Log.i("DKBOSE", "inside for loop");
                Log.i("DKBOSE", "cat - " + cat);

                DatabaseReference Ref = FirebaseDatabase.getInstance().getReference("Users/" + cat);
                String uid = Ref.push().getKey();
                Log.i("DKBOSE", "uid - " + uid);
                Log.i("DKBOSE", "token - " + s);
                Ref.child(uid).setValue(s);
            }
        }
    }
}
