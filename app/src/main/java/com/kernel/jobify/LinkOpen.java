package com.kernel.jobify;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
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


    DatabaseReference dataref;
    ProgressBar bar;
    Context ctx;
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
                Uri deepLink = null;
                if (pendingDynamicLinkData != null)
                {
                    deepLink = pendingDynamicLinkData.getLink();
                    String cat = deepLink.getQueryParameter("cat");
                    String id = deepLink.getQueryParameter("id");
                    dataref = FirebaseDatabase.getInstance().getReference(cat+"/"+id);

                    dataref.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            JobData newjob = snapshot.getValue(JobData.class);
                            newjob.setJobKey(snapshot.getKey());
                            newjob.setJobCat(cat);
                            Intent intent = new Intent(ctx,JobDisActivity.class);
                            intent.putExtra("jobTitle", newjob.getJobTitle());
                            intent.putExtra("jobDisc", newjob.getJobDisc());
                            intent.putExtra("jobLink", newjob.getJobLink());
                            intent.putExtra("jobPhotoLink", newjob.getJobPhotoLink());
                            intent.putExtra("jobKey",newjob.getJobKey());
                            intent.putExtra("jobCat",newjob.getJobCat());
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            bar.setVisibility(View.INVISIBLE);
                            ctx.startActivity(intent);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(ctx, "Link Expired", Toast.LENGTH_SHORT).show();
                            ctx.startActivity(new Intent(ctx,MainActivity.class));
                        }
                    });

                    //Toast.makeText(LinkOpen.this, ""+cat+""+id, Toast.LENGTH_SHORT).show();
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
}