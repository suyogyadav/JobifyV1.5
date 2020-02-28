package com.kernel.jobify;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;

import androidx.browser.customtabs.CustomTabsIntent;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;


import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;

import java.io.InputStream;
import java.util.List;

public class JobDisActivity extends AppCompatActivity {

    JobData showdata;
    ProgressBar progressBar;
    Button apply;
    TextView title;
    TextView disc;
    ImageView img;
    ImageButton bookm;
    SharedPreferences bookmarks;
    String[] test;


    private InterstitialAd interstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        MobileAds.initialize(this, "ca-app-pub-3940256099942544~3347511713");
        interstitialAd = new InterstitialAd(this);
        interstitialAd.setAdUnitId("ca-app-pub-3940256099942544/1033173712");
        AdRequest adRequest = new AdRequest.Builder().build();
        interstitialAd.loadAd(adRequest);

        Log.i("jobcheck", "oncreate");

        setContentView(R.layout.activity_jobdis);

        showdata = new JobData();
        Toolbar toolbar = findViewById(R.id.jobdistoolbar);
        toolbar.setNavigationIcon(R.drawable.ic_action_back);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        JobData showdata1 = new JobData();
        showdata1.setJobTitle(getIntent().getStringExtra("jobTitle"));
        showdata1.setJobDisc(getIntent().getStringExtra("jobDisc"));
        showdata1.setJobLink(getIntent().getStringExtra("jobLink"));
        showdata1.setJobPhotoLink(getIntent().getStringExtra("jobPhotoLink"));
        showdata1.setJobKey(getIntent().getStringExtra("jobKey"));
        Log.i("DIVINE", "jobdisc" + getIntent().getStringExtra("jobKey"));
        showdata1.setJobCat(getIntent().getStringExtra("jobCat"));
        progressBar = findViewById(R.id.progressbar);
        apply = findViewById(R.id.jobdisbtn);
        disc = findViewById(R.id.jobdisdis);
        title = findViewById(R.id.jobdistitle);
        img = findViewById(R.id.jobdisimg);
        bookm = findViewById(R.id.bookmarkbtn);
        if (showdata1.getJobPhotoLink() != null) {
            new DownloadImageTask(img, progressBar)
                    .execute(showdata1.getJobPhotoLink());
        } else {
            img.setImageResource(R.mipmap.ic_launcher);
            progressBar.setVisibility(View.GONE);
        }
        title.setText(showdata1.getJobTitle());
        disc.setText(showdata1.getJobDisc().replace("_n", "\n"));
        showdata = showdata1;
        Log.i("DIVINE", "jobdisc" + showdata1.getJobCat());
        Log.i("DIVINE", "jobdisc" + showdata1.getJobKey());
        if (isbookmarked(showdata1.getJobCat(),showdata1.getJobKey()))
        {
            bookm.setImageDrawable(getDrawable(R.drawable.ic_bookmark_fill));
        }
    }

    private Boolean isbookmarked(String cat,String key) {
        SharedPreferences bookmarks = getSharedPreferences("bookmarks", 0);
        String abcd = bookmarks.getString("book", "nobooks");
        if (!abcd.equals("nobooks")) {
            test = abcd.split(";;");
            if (test.length > 0) {
                for (int i = 0; i < test.length; i++) {
                    String[]xyz = test[i].split("##");
                    if (xyz[0].equals(cat) && xyz[1].equals(key))
                    {
                        Log.i("DIVINE", "jobdisc" + true);
                        return true;
                    }
                }
                Log.i("DIVINE", "jobdisc" + false);
                return false;
            }
        }
        Log.i("DIVINE", "jobdisc" + false);
        return false;
    }

    @Override
    protected void onRestart() {
        Log.i("jobcheck", "onRestart");
        if (interstitialAd.isLoaded()) {
            interstitialAd.show();
            Log.i("jobcheck", "ad shows");
        } else {
            Log.i("jobcheck", "ad is not loaded yet");
        }
        super.onRestart();
    }

    public void openbowser(View view) {

        if (showdata.getJobLink() != null) {
            CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
            builder.setToolbarColor(getResources().getColor(R.color.colorPrimary));
            builder.addDefaultShareMenuItem();
            builder.setShowTitle(true);

            CustomTabsIntent customTabsIntent = builder.build();
            customTabsIntent.launchUrl(this, Uri.parse(showdata.getJobLink()));
        } else {
            Toast.makeText(this, "No Apply Link Avilable", Toast.LENGTH_SHORT).show();
        }

    }

    public void bookMark(View view) {
        Log.i("dell",""+!isbookmarked(showdata.getJobCat(),showdata.getJobKey()));
        if (!isbookmarked(showdata.getJobCat(),showdata.getJobKey())) {

            ImageButton imgbtn = findViewById(R.id.bookmarkbtn);
            imgbtn.setImageDrawable(getDrawable(R.drawable.ic_bookmark_fill));

            bookmarks = getSharedPreferences("bookmarks", 0);
            StringBuilder builder = new StringBuilder();
            Log.i("DIVINE", "jobdisc" + showdata.getJobCat());
            Log.i("DIVINE", "jobdisc" + showdata.getJobKey());
            builder.append(showdata.getJobCat())
                    .append("##")
                    .append(showdata.getJobKey())
                    .append(";;");

            String test = bookmarks.getString("book", "nobooks");

            if (test.equals("")||test.equals("nobooks")) {
                bookmarks.edit().putString("book", builder.toString()).apply();
            } else {
                bookmarks.edit().putString("book", test + builder.toString()).apply();
            }

        }
        else {
            bookm.setImageDrawable(getDrawable(R.drawable.ic_bookmark));
            SharedPreferences bookmarks = getSharedPreferences("bookmarks", 0);
            String abcd = bookmarks.getString("book", "nobooks");
            Log.i("loveyouzindagi",abcd);
            if (!abcd.equals("nobooks"))
            {
                String qwerty = abcd.replace((showdata.getJobCat()+"##"+showdata.getJobKey()+";;"),"");
                Log.i("loveyouzindagi",qwerty);
                bookmarks.edit().putString("book",qwerty).commit();
            }
        }
    }

    class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;
        ProgressBar progressBar;

        @Override
        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
            super.onPreExecute();
        }

        public DownloadImageTask(ImageView bmImage, ProgressBar progressBar) {
            this.progressBar = progressBar;
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            if (result == null) {
                Log.i("DIVINE", "Faild to load image");
                progressBar.setVisibility(View.GONE);
                bmImage.setImageDrawable(getDrawable(R.drawable.ic_noimage));
            } else {
                progressBar.setVisibility(View.GONE);
                bmImage.setImageBitmap(result);
            }
        }
    }
}
