package com.kernel.jobify;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.PendingDynamicLinkData;

public class LinkOpen extends AppCompatActivity {


    int versionflag = 0;
    DatabaseReference dataref;
    ProgressBar bar;
    Context ctx;
    Uri deepLink = null;
    String CAT="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_link_open);
        ctx = this;
        bar = findViewById(R.id.bar);
        bar.setVisibility(View.VISIBLE);
        FirebaseDynamicLinks.getInstance().getDynamicLink(getIntent()).addOnSuccessListener(this, new OnSuccessListener<PendingDynamicLinkData>() {
            @Override
            public void onSuccess(PendingDynamicLinkData pendingDynamicLinkData) {

                if (pendingDynamicLinkData != null) {
                    CheckVersion();
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (versionflag == 0) {
                                deepLink = pendingDynamicLinkData.getLink();
                                String cat="",id="";
                                try{
                                    cat = deepLink.getQueryParameter("cat");
                                    id = deepLink.getQueryParameter("id");
                                    CAT = cat;
                                }catch (NullPointerException exp)
                                {
                                    ctx.startActivity(new Intent(ctx,MainActivity.class));
                                    finish();
                                }

                                dataref = FirebaseDatabase.getInstance().getReference(cat + "/" + id);

                                dataref.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        JobData newjob = snapshot.getValue(JobData.class);
                                        newjob.setJobKey(snapshot.getKey());
                                        newjob.setJobCat(CAT);
                                        Intent intent = new Intent(ctx, JobDisActivity.class);
                                        intent.putExtra("jobTitle", newjob.getJobTitle());
                                        intent.putExtra("jobDisc", newjob.getJobDisc());
                                        intent.putExtra("jobLink", newjob.getJobLink());
                                        intent.putExtra("jobPhotoLink", newjob.getJobPhotoLink());
                                        intent.putExtra("jobKey", newjob.getJobKey());
                                        intent.putExtra("jobCat", newjob.getJobCat());
                                        intent.putExtra("shareLink", newjob.getShareLink());
                                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

                                        bar.setVisibility(View.INVISIBLE);
                                        ctx.startActivity(intent);
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {
                                        Toast.makeText(ctx, "Link Expired", Toast.LENGTH_SHORT).show();
                                        ctx.startActivity(new Intent(ctx, MainActivity.class));
                                    }
                                });
                            } else {
                                setContentView(R.layout.version_update);
                            }
                        }
                    }, 3000);
                    //Toast.makeText(LinkOpen.this, ""+cat+""+id, Toast.LENGTH_SHORT).show();
                }
                else
                {
                    ctx.startActivity(new Intent(ctx,MainActivity.class));
                }
            }
        })
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(LinkOpen.this, "Link Expired", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void openPlayStore(View view) {
        final String packname = getPackageName();
        try {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + packname)));
        } catch (ActivityNotFoundException enf) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + packname)));
        }
    }

    public void CheckVersion() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("AppVersion");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String abcd = snapshot.getValue(String.class);
                if (abcd != null && abcd.equals(getString(R.string.version))) {
                    Log.i("APP", abcd);
                    versionflag = 0;
                } else {
                    Log.i("APP", "NULL");
                    versionflag = 1;
                    // setContentView(R.layout.version_update);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}