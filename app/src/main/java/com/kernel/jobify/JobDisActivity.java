package com.kernel.jobify;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.customtabs.CustomTabsIntent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.InputStream;

public class JobDisActivity extends AppCompatActivity {


    JobData showdata;
    ProgressBar progressBar;
    Button apply;
    TextView title;
    TextView disc;
    ImageView img;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ConnectivityManager cm = (ConnectivityManager) getBaseContext().getSystemService(getBaseContext().CONNECTIVITY_SERVICE);
        NetworkInfo info = cm.getActiveNetworkInfo();


        if (info!= null && info.isConnected()) {
            setContentView(R.layout.activity_jobdis);

            Toolbar toolbar = findViewById(R.id.jobdistoolbar);
            toolbar.setNavigationIcon(R.drawable.ic_action_back);

            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });

            showdata = (JobData) getIntent().getSerializableExtra("jobdata");

            progressBar = findViewById(R.id.progressbar);
            progressBar.setVisibility(View.VISIBLE);

            apply = findViewById(R.id.jobdisbtn);
            disc = findViewById(R.id.jobdisdis);
            title = findViewById(R.id.jobdistitle);
            img = findViewById(R.id.jobdisimg);

            if (showdata.getJobPhotoLink() != null) {
                new DownloadImageTask(img)
                        .execute(showdata.jobPhotoLink);
            }
            title.setText(showdata.getJobTitle());
            disc.setText(showdata.getJobDisc().replace("   ", "\n"));
            disc.setMovementMethod(new ScrollingMovementMethod());
        }
        else
        {
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

    public void openbowser(View view)
    {
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
