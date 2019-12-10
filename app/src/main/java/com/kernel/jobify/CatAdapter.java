package com.kernel.jobify;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class CatAdapter extends RecyclerView.Adapter<CatAdapter.CatViewHolder> {

    String[][] catlist =  {{"IT","ECS"},{"MECH","CIVIL"},{"GOVT","INTERN"}};
    String[][] showlist =  {{"IT","Electronics and EnTC"},{"Mechanical","Civil"},{"Government","Internship"}};
    int[][] catlogo = {{R.drawable.catlogo01,R.drawable.catlogo02},{R.drawable.catlogo03,R.drawable.catlogo04},{R.drawable.catlogo05,R.drawable.catlogo06}};
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
        return new CatAdapter.CatViewHolder(view,ctx);

    }

    @Override
    public void onBindViewHolder(@NonNull CatViewHolder catViewHolder, int i) {


        catViewHolder.catimg1.setImageBitmap(getRoundedCornerBitmap(BitmapFactory.decodeResource(ctx.getResources(),catlogo[i][0])));
        catViewHolder.catimg2.setImageBitmap(getRoundedCornerBitmap(BitmapFactory.decodeResource(ctx.getResources(),catlogo[i][1])));
        catViewHolder.catname1.setText(showlist[i][0]);
        catViewHolder.catname2.setText(showlist[i][1]);
    }

    public static Bitmap getRoundedCornerBitmap(Bitmap bitmap) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
                bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);
        final float roundPx = 12;

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        return output;
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
        public CatViewHolder(@NonNull View itemView, final Context ctx) {
            super(itemView);
            catimg1 = itemView.findViewById(R.id.catimg);
            catimg2 = itemView.findViewById(R.id.catimg2);
            catname1 = itemView.findViewById(R.id.catname);
            catname2 = itemView.findViewById(R.id.catname2);
            frame1 = itemView.findViewById(R.id.frame1);
            frame2 = itemView.findViewById(R.id.frame2);

            frame1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(ctx,MainActivity2.class);
                    intent.putExtra("CAT",catlist[getAdapterPosition()][0]);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    ctx.startActivity(intent);
                }
            });

            frame2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(ctx,MainActivity2.class);
                    intent.putExtra("CAT",catlist[getAdapterPosition()][1]);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    ctx.startActivity(intent);
                }
            });
        }
    }
}
