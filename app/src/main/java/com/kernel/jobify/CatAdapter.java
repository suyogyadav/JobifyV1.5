package com.kernel.jobify;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

public class CatAdapter extends RecyclerView.Adapter<CatAdapter.CatViewHolder> {

    static int fori;
    String[][] catlist =  {{"IT","ECS"},{"MECH","CIVIL"},{"GOVT","INTERN"}};
    String[][] showlist =  {{"IT","Electronics and EnTC"},{"Mechanical","civil"},{"Government","Internship"}};
    Context ctx;

    public CatAdapter(Context ctx)
    {
        this.ctx=ctx;
    }

    @NonNull
    @Override
    public CatViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View view = inflater.inflate(R.layout.catlistitem,viewGroup,false);
        return new CatAdapter.CatViewHolder(view,ctx,i);
    }

    @Override
    public void onBindViewHolder(@NonNull CatViewHolder catViewHolder, int i) {
        catViewHolder.catimg1.setImageResource(R.mipmap.ic_launcher);
        catViewHolder.catimg2.setImageResource(R.mipmap.ic_launcher);
        catViewHolder.catname1.setText(showlist[i][0]);
        catViewHolder.catname2.setText(showlist[i][1]);
        fori=i;
        catViewHolder.frame1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ctx,MainActivity2.class);
                intent.putExtra("CAT",catlist[fori][0]);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                ctx.startActivity(intent);
            }
        });

        catViewHolder.frame2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ctx,MainActivity2.class);
                intent.putExtra("CAT",catlist[fori][1]);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                ctx.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return 3;
    }

    class CatViewHolder extends RecyclerView.ViewHolder
    {
        ImageView catimg1;
        ImageView catimg2;
        TextView catname1;
        TextView catname2;
        FrameLayout frame1;
        FrameLayout frame2;
        public CatViewHolder(@NonNull View itemView, final Context ctx,int i) {
            super(itemView);
            fori = i;
            catimg1 = itemView.findViewById(R.id.catimg);
            catimg2 = itemView.findViewById(R.id.catimg2);
            catname1 = itemView.findViewById(R.id.catname);
            catname2 = itemView.findViewById(R.id.catname2);
            frame1 = itemView.findViewById(R.id.frame1);
            frame2 = itemView.findViewById(R.id.frame2);
        }
    }
}
