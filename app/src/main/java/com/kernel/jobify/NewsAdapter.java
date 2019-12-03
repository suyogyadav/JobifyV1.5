package com.kernel.jobify;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;


import java.io.InputStream;
import java.util.List;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.newsviewholder>
{
    List<JobData> jobslist;
    Context ctx;

    public NewsAdapter(List<JobData> jobslist, Context ctx)
    {
        this.jobslist = jobslist;
        this.ctx = ctx;
    }

    @NonNull
    @Override
    public newsviewholder onCreateViewHolder(ViewGroup viewGroup, int i)
    {

        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View view = inflater.inflate(R.layout.news_space,viewGroup,false);
        return new newsviewholder(view,ctx);
    }

    @Override
    public void onBindViewHolder(newsviewholder viewHolder, int i)
    {
        if (jobslist.get(i).getJobPhotoLink()!=null) {
            new DownloadImageTask(viewHolder.imgicon,viewHolder.progressBar)
                    .execute(jobslist.get(i).getJobPhotoLink());
        }
        else
        {
            viewHolder.imgicon.setImageResource(R.drawable.ic_launcher_foreground);
        }

        viewHolder.title.setText( jobslist.get(i).getJobTitle());
    }

    @Override
    public int getItemCount() {
        return jobslist.size();
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
            Intent intent = new Intent(ctx,JobDisActivity.class);
            intent.putExtra("jobdata",jobslist.get(pos));
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            ctx.startActivity(intent);
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
