package com.kernel.jobify;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

class CheckVersion
{
    DatabaseReference ref;
    String abcd;
    CheckVersion()
    {
        ref  = FirebaseDatabase.getInstance().getReference("AppVersion");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                abcd = snapshot.getValue(String.class);
                if (abcd !=null) {
                    Log.i("APP", abcd);
                }else {
                    Log.i("APP", "NULL");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public boolean versionMatch(Context ctx)
    {
        String strver = ctx.getString(R.string.version);
        if(strver.equals(abcd))
        {
            return true;
        }
        else {
            return false;
        }
    }
}
