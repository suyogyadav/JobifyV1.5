package com.kernel.jobify;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
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

public class NewsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
{
    List<Object> jobslist;
    Context ctx;
    private static  final int ITEAM_JOB=0;
    private static  final int ITEAM_BANNER_AD=1;
    boolean flag = true;

    public NewsAdapter(List<Object> jobslist,Context ctx)
    {
        this.jobslist = jobslist;
        this.ctx = ctx;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i)
    {
        switch (getItemViewType(i))
        {
            case ITEAM_JOB:
                LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
                View view = inflater.inflate(R.layout.news_space,viewGroup,false);
                return new newsviewholder(view,ctx);

            case ITEAM_BANNER_AD:

                default:
                    LayoutInflater inflater1 = LayoutInflater.from(viewGroup.getContext());
                    View view1 = inflater1.inflate(R.layout.adcardview,viewGroup,false);
                    return new Adviewolder(view1);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i)
    {
        Log.i("ABCD","Position"+i);
        switch (getItemViewType(i))
        {
            case ITEAM_JOB:
                newsviewholder newsviewholder = (newsviewholder) viewHolder;
                JobData jobData =(JobData) jobslist.get(i);
                if (jobData.getJobPhotoLink()!=null) {
                    new DownloadImageTask(newsviewholder.imgicon,newsviewholder.progressBar)
                            .execute(jobData.getJobPhotoLink());
                }else
                {
                    newsviewholder.imgicon.setImageResource(R.mipmap.ic_launcher);
                    newsviewholder.progressBar.setVisibility(View.GONE);
                }
                newsviewholder.title.setText( jobData.getJobTitle());
                break;

            case ITEAM_BANNER_AD:
                default:
                    Adviewolder adviewolder = (Adviewolder) viewHolder;
                    AdView adView = (AdView) jobslist.get(i);
                    Log.i("ABCD","ADCASTDONEON "+i);
                    ViewGroup adcardview = (ViewGroup) adviewolder.itemView;

                    if (adcardview.getChildCount()>0)
                    {
                        adcardview.removeAllViews();
                    }
                    if (adcardview.getParent()!=null)
                    {
                        ((ViewGroup)adView.getParent()).removeView(adView);
                    }
                    adcardview.addView(adView);
                    break;
        }
    }

    @Override
    public int getItemCount() {
        return jobslist.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (jobslist.get(position)instanceof AdView)
        {
            return ITEAM_BANNER_AD;
        }
        else
        {
                return ITEAM_JOB;
        }
    }

    public class newsviewholder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        ProgressBar progressBar;
        ImageView imgicon;
        TextView title;
        Context ctx;
        public newsviewholder(View itemView,Context ctx) {
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
            JobData newjob =(JobData)jobslist.get(pos);
            Intent intent = new Intent(ctx,JobDisActivity.class);
            intent.putExtra("jobTitle",newjob.getJobTitle());
            intent.putExtra("jobDisc",newjob.getJobDisc());
            intent.putExtra("jobLink",newjob.getJobLink());
            intent.putExtra("jobPhotoLink",newjob.getJobPhotoLink());
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            ctx.startActivity(intent);
        }
    }

    public class Adviewolder extends RecyclerView.ViewHolder
    {

        public Adviewolder(@NonNull View itemView) {
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

        public DownloadImageTask(ImageView bmImage,ProgressBar progressBar) {
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

        protected void onPostExecute(Bitmap result){
            progressBar.setVisibility(View.GONE);
            bmImage.setImageBitmap(result);
        }
    }


}
