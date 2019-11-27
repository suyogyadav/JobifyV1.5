package com.kernel.jobify;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.InputStream;

public class JobDisActivity extends AppCompatActivity {


    JobData showdata;
    Button apply;
    TextView disc;
    ImageView img;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jobdis);

        showdata = (JobData) getIntent().getSerializableExtra("jobdata");
        apply = findViewById(R.id.jobdisbtn);
        disc = findViewById(R.id.jobdisdis);
        img = findViewById(R.id.jobdisimg);
        new DownloadImageTask(img)
                .execute(showdata.jobPhotoLink);
        disc.setText(showdata.getJobDisc());

    }

    public void setdata(View view)
    {
        Toast.makeText(this,"Apply Button Clicked",Toast.LENGTH_SHORT).show();
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
            bmImage.setImageBitmap(result);
        }
    }
}
