package com.kernel.jobify;

import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.ads.AdView;
import com.squareup.picasso.Picasso;

import java.util.List;

public class NewsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    List<Object> jobslist;
    Context ctx;

    public NewsAdapter(List<Object> jobslist, Context ctx) {
        this.jobslist = jobslist;
        this.ctx = ctx;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        if (getItemViewType(i) == 0) {
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
        if (getItemViewType(i) == 0) {
            Log.i("Joblist", "news bind view " + i);
            newsviewholder newsviewholder = (newsviewholder) viewHolder;
            JobData jobData = (JobData) jobslist.get(i);
            Picasso.get().load(jobData.getJobPhotoLink())
                    .error(R.drawable.errorimg)
                    .into(newsviewholder.imgicon);
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

        ImageView imgicon;
        TextView title;
        Context ctx;

        public newsviewholder(View itemView, Context ctx) {
            super(itemView);
            this.ctx = ctx;
            itemView.setOnClickListener(this);
            imgicon = itemView.findViewById(R.id.newsimg);
            title = itemView.findViewById(R.id.newstitle);
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
            intent.putExtra("jobKey", newjob.getJobKey());
            Log.i("DIVINE", "newadapter" + newjob.getJobKey());
            intent.putExtra("jobCat", newjob.getJobCat());
            intent.putExtra("shareLink", newjob.getShareLink());
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            ctx.startActivity(intent);
        }
    }

    public class Adviewolder extends RecyclerView.ViewHolder {
        public Adviewolder(View itemView) {
            super(itemView);
        }

    }
}
