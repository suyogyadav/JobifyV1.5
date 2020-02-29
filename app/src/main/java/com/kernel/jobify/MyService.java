package com.kernel.jobify;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

public class MyService extends FirebaseMessagingService {

    SharedPreferences tockengenerated;
    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
        Log.i("alan","inside newtoken");
        tockengenerated = getSharedPreferences("tockengen",0);
        tockengenerated.edit().putBoolean("avilable",true).commit();
        Log.i("alan","status put to true");
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        Map<String, String> abcd = remoteMessage.getData();
        Log.i("DEVINE", "on message called");
    }
}
