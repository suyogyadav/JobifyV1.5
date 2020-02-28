package com.kernel.jobify;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.ads.AdView;

import java.io.InputStream;
import java.util.List;

public class NewsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    List<Object> jobslist;
    Context ctx;
    private static final int ITEAM_JOB = 0;
    private static final int ITEAM_BANNER_AD = 1;
    boolean flag = true;

    public NewsAdapter(List<Object> jobslist, Context ctx) {
        this.jobslist = jobslist;
        this.ctx = ctx;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        if (getItemViewType(i) == ITEAM_JOB) {
            LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
            View view = inflater.inflate(R.layout.news_space, viewGroup, false);
            return new newsviewholder(view, ctx);
        } else {
            LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
            View view = inflater.inflate(R.layout.adcardview, viewGroup, false);
            return new Adviewolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        Log.i("ABCD", "Position" + i);
        if (getItemViewType(i) == ITEAM_JOB) {
            Log.i("Joblist", "news bind view " + i);
            newsviewholder newsviewholder = (newsviewholder) viewHolder;
            JobData jobData = (JobData) jobslist.get(i);
            if (jobData.getJobPhotoLink() != null) {
                new DownloadImageTask(newsviewholder.imgicon, newsviewholder.progressBar)
                        .execute(jobData.getJobPhotoLink());
            } else {
                newsviewholder.imgicon.setImageResource(R.mipmap.ic_launcher);
                newsviewholder.progressBar.setVisibility(View.GONE);
            }
            newsviewholder.title.setText(jobData.getJobTitle());
        } else {
            //Adviewolder adviewolder = (Adviewolder) viewHolder;
            AdView adView = (AdView) jobslist.get(i);
            Log.i("ABCD", "ADCASTDONEON " + i);
            ViewGroup adcardview = (ViewGroup) viewHolder.itemView;

            if (adcardview.getChildCount() > 0) {
                adcardview.removeAllViews();
            }
            if (adcardview.getParent() != null) {
                ((ViewGroup) adView.getParent()).removeView(adView);
            }

            adcardview.addView(adView);
        }
    }

    @Override
    public int getItemCount() {
        return jobslist.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (jobslist.get(position) instanceof JobData) {
            return 0;
        } else {
            return 1;
        }
    }

    public class newsviewholder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ProgressBar progressBar;
        ImageView imgicon;
        TextView title;
        Context ctx;

        public newsviewholder(View itemView, Context ctx) {
            super(itemView);
            this.ctx = ctx;
            itemView.setOnClickListener(this);
            imgicon = itemView.findViewById(R.id.newsimg);
            title = itemView.findViewById(R.id.newstitle);
            progressBar = itemView.findViewById(R.id.newsprogressbar);
        }


        @Override
        public void onClick(View v) {
            int pos = getAdapterPosition();
            JobData newjob = (JobData) jobslist.get(pos);
            Intent intent = new Intent(ctx, JobDisActivity.class);
            intent.putExtra("jobTitle", newjob.getJobTitle());
            intent.putExtra("jobDisc", newjob.getJobDisc());
            intent.putExtra("jobLink", newjob.getJobLink());
            intent.putExtra("jobPhotoLink", newjob.getJobPhotoLink());
            intent.putExtra("jobKey",newjob.getJobKey());
            Log.i("DIVINE", "newadapter" + newjob.getJobKey());
            intent.putExtra("jobCat",newjob.getJobCat());
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            ctx.startActivity(intent);
        }
    }

    public class Adviewolder extends RecyclerView.ViewHolder {
        public Adviewolder(View itemView) {
            super(itemView);
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
                Log.i("DIVINE","Faild to load image");
                progressBar.setVisibility(View.GONE);
                bmImage.setImageDrawable(ctx.getDrawable(R.drawable.ic_noimage));
            } else {
                progressBar.setVisibility(View.GONE);
                bmImage.setImageBitmap(result);
            }
        }
    }


}
