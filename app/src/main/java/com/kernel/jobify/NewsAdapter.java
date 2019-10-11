package com.kernel.jobify;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.newsviewholder>
{
    public String Title[];
    public String Disc[];
    public NewsAdapter(String title[] , String disc[])
    {
        Log.i("ABCD","ata he tr dis bc");
        this.Title = title;
        this.Disc = disc;
    }

    @NonNull
    @Override
    public newsviewholder onCreateViewHolder(ViewGroup viewGroup, int i)
    {
        Log.i("ABCD","hr dist tr ka bc");
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View view = inflater.inflate(R.layout.news_space,viewGroup,false);
        Log.i("ABCD","ky tr dis bc");
        return new newsviewholder(view);
    }

    @Override
    public void onBindViewHolder(newsviewholder viewHolder, int i)
    {
        String title = Title[i];
        String disc = Disc[i];
        viewHolder.title.setText(title);
        viewHolder.disc.setText(disc);
    }

    @Override
    public int getItemCount() {
        return Title.length;
    }

    public class newsviewholder extends RecyclerView.ViewHolder
    {
        ImageView imgicon;
        TextView title;
        TextView disc;
        public newsviewholder(View itemView) {
            super(itemView);
            imgicon = itemView.findViewById(R.id.newsimg);
            title = itemView.findViewById(R.id.newstitle);
            disc = itemView.findViewById(R.id.newsdis);
        }
    }
}
