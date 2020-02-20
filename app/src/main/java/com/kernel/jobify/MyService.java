package com.kernel.jobify;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyService extends FirebaseMessagingService {

    SharedPreferences tockengenerated;
    boolean firstTime;
    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
        Log.i("alan","inside newtoken");
        tockengenerated = getSharedPreferences("tockengen",0);
        tockengenerated.edit().putBoolean("avilable",true).commit();
        Log.i("alan","status put to true");

//        settings = this.getSharedPreferences("appInfo", 0);
//        firstTime = settings.getBoolean("first_time", true);
//        if(!firstTime) {
//            Log.i("DKBOSE","inside my service not first time");
//            MainActivity main = new MainActivity();
//            main.sendpreftoserver();
//        }
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.i("DEVINE", "on message called");
    }
}
