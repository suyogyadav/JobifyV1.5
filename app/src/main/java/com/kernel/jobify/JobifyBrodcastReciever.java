package com.kernel.jobify;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

public class JobifyBrodcastReciever extends BroadcastReceiver
{
    @Override
    public void onReceive(Context context, Intent intent)
    {
        Intent intent1 = new Intent(context,JobifyService.class);

        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(context.CONNECTIVITY_SERVICE);
        NetworkInfo info = cm.getActiveNetworkInfo();

        if (info != null && info.isConnected()) {
            context.startService(intent1);
            Log.i("ABCD","Internet is connected");
        }
        else
        {
            Log.i("ABCD","Internet is NOT connected");
            if (JobifyService.isrunning)
            {
                Log.i("ABCD","Stopping the service from broadcast");
                context.stopService(intent1);
            }
        }
    }
}
