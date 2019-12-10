package com.kernel.jobify;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;

import androidx.browser.customtabs.CustomTabsIntent;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;

import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;

import java.io.InputStream;

public class JobDisActivity extends AppCompatActivity {

    JobData showdata;
    ProgressBar progressBar;
    Button apply;
    TextView title;
    TextView disc;
    ImageView img;

    private InterstitialAd interstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        MobileAds.initialize(this, "ca-app-pub-3940256099942544~3347511713");
        interstitialAd = new InterstitialAd(this);
        interstitialAd.setAdUnitId("ca-app-pub-3940256099942544/1033173712");
        AdRequest adRequest = new AdRequest.Builder().build();
        interstitialAd.loadAd(adRequest);

        ConnectivityManager cm = (ConnectivityManager) getBaseContext().getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo info = cm.getActiveNetworkInfo();

        Log.i("jobcheck", "oncreate");


        if (info != null && info.isConnected()) {
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
            progressBar = findViewById(R.id.progressbar);
            progressBar.setVisibility(View.VISIBLE);

            apply = findViewById(R.id.jobdisbtn);
            disc = findViewById(R.id.jobdisdis);
            title = findViewById(R.id.jobdistitle);
            img = findViewById(R.id.jobdisimg);

            if (showdata1.getJobPhotoLink() != null) {
                new DownloadImageTask(img)
                        .execute(showdata1.jobPhotoLink);
            } else {
                img.setImageResource(R.mipmap.ic_launcher);
                progressBar.setVisibility(View.GONE);
            }
            title.setText(showdata1.getJobTitle());
            disc.setText(showdata1.getJobDisc().replace("_n", "\n"));
            disc.setMovementMethod(new ScrollingMovementMethod());
            showdata = showdata1;
        } else {
            setContentView(R.layout.blnt);
            Toolbar toolbar = findViewById(R.id.blnttoolbar);
            toolbar.setNavigationIcon(R.drawable.ic_action_back);

            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }
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
        CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
        builder.setToolbarColor(getResources().getColor(R.color.colorPrimary));
        builder.addDefaultShareMenuItem();
        builder.setShowTitle(true);

        CustomTabsIntent customTabsIntent = builder.build();
        customTabsIntent.launchUrl(this, Uri.parse(showdata.jobLink));

    }

    class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
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
            progressBar.setVisibility(View.GONE);
            bmImage.setImageBitmap(result);
        }
    }
}
