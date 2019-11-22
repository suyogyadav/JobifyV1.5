package com.kernel.jobify;

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
import android.widget.TextView;

import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.newsviewholder>
{
    public String Title[];
    public String Disc[];
    public String PhotoLink[];

    public NewsAdapter(String title[] , String disc[], String photoLink[])
    {
        Log.i("ABCD","ata he tr dis bc");
        this.Title = title;
        this.Disc = disc;
        this.PhotoLink = photoLink;
    }

    @NonNull
    @Override
    public newsviewholder onCreateViewHolder(ViewGroup viewGroup, int i)
    {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View view = inflater.inflate(R.layout.news_space,viewGroup,false);
        return new newsviewholder(view);
    }

    @Override
    public void onBindViewHolder(newsviewholder viewHolder, int i)
    {
        Log.i("not",""+PhotoLink[i]);
        if (PhotoLink[i]!=null) {
            new DownloadImageTask(viewHolder.imgicon)
                    .execute(PhotoLink[i]);
        }
        else
        {
            viewHolder.imgicon.setImageResource(R.drawable.ic_launcher_foreground);
        }
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

