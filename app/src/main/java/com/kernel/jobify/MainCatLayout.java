package com.kernel.jobify;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import static com.kernel.jobify.MainActivity.getRoundedCornerBitmap;

public class MainCatLayout extends AppCompatActivity {

    int[] catlogo = {R.drawable.catlogo01, R.drawable.catlogo02, R.drawable.catlogo03, R.drawable.catlogo04, R.drawable.catlogo05, R.drawable.catlogo06};
    String[] showlist = {"IT", "Electronics", "Mechanical", "Civil", "Government", "Internship"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.maincatlayout);

        MobileAds.initialize(this, "ca-app-pub-8714980968157209~5975555023");

        Toolbar toolbar = findViewById(R.id.newstoolbar);
        toolbar.setNavigationIcon(R.drawable.ic_action_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        ImageView imageView1 = findViewById(R.id.catimg1);
        imageView1.setImageBitmap(getRoundedCornerBitmap(BitmapFactory.decodeResource(getResources(), catlogo[0])));
        ImageView imageView2 = findViewById(R.id.catimg2);
        imageView2.setImageBitmap(getRoundedCornerBitmap(BitmapFactory.decodeResource(getResources(), catlogo[1])));
        ImageView imageView3 = findViewById(R.id.catimg3);
        imageView3.setImageBitmap(getRoundedCornerBitmap(BitmapFactory.decodeResource(getResources(), catlogo[2])));
        ImageView imageView4 = findViewById(R.id.catimg4);
        imageView4.setImageBitmap(getRoundedCornerBitmap(BitmapFactory.decodeResource(getResources(), catlogo[3])));
        ImageView imageView5 = findViewById(R.id.catimg5);
        imageView5.setImageBitmap(getRoundedCornerBitmap(BitmapFactory.decodeResource(getResources(), catlogo[4])));
        ImageView imageView6 = findViewById(R.id.catimg6);
        imageView6.setImageBitmap(getRoundedCornerBitmap(BitmapFactory.decodeResource(getResources(), catlogo[5])));

        TextView textView1 = findViewById(R.id.catname1);
        textView1.setText(showlist[0]);
        TextView textView2 = findViewById(R.id.catname2);
        textView2.setText(showlist[1]);
        TextView textView3 = findViewById(R.id.catname3);
        textView3.setText(showlist[2]);
        TextView textView4 = findViewById(R.id.catname4);
        textView4.setText(showlist[3]);
        TextView textView5 = findViewById(R.id.catname5);
        textView5.setText(showlist[4]);
        TextView textView6 = findViewById(R.id.catname6);
        textView6.setText(showlist[5]);

        AdView adView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);
    }

    public void btnIT(View view) {
        Intent intent = new Intent(this, MainActivity2.class);
        intent.putExtra("CAT", "IT");
        startActivity(intent);
    }

    public void btnECS(View view) {
        Intent intent = new Intent(this, MainActivity2.class);
        intent.putExtra("CAT", "ECS");
        startActivity(intent);
    }

    public void btnMECH(View view) {
        Intent intent = new Intent(this, MainActivity2.class);
        intent.putExtra("CAT", "MECH");
        startActivity(intent);
    }

    public void btnCIVIL(View view) {
        Intent intent = new Intent(this, MainActivity2.class);
        intent.putExtra("CAT", "CIVIL");
        startActivity(intent);
    }

    public void btnGOVT(View view) {
        Intent intent = new Intent(this, MainActivity2.class);
        intent.putExtra("CAT", "GOVT");
        startActivity(intent);
    }

    public void btnINTERN(View view) {
        Intent intent = new Intent(this, MainActivity2.class);
        intent.putExtra("CAT", "INTERN");
        startActivity(intent);
    }
}
