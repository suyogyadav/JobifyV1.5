package com.kernel.jobify;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyService extends FirebaseMessagingService {

    SharedPreferences settings;
    boolean firstTime;
    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
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
