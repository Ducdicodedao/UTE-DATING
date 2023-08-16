package com.client.utedating;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;

public class MyApplication extends Application {
    public static final String CHANNEL_ID = "CHANNEL_ID";
    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        createChanelNotification();
    }

    public void createChanelNotification() {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            if (ContextCompat.checkSelfPermission(getApplicationContext(), POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
//                ActivityCompat.requestPermissions((Activity) getApplicationContext(), new String[]{POST_NOTIFICATIONS},101);
//            }
//            else {
        NotificationChannel channel = new NotificationChannel(CHANNEL_ID, "CHANNEL_ID", NotificationManager.IMPORTANCE_DEFAULT);
        NotificationManager manager = getSystemService(NotificationManager.class);
        manager.createNotificationChannel(channel);
//            }
    }
    public static Context getContext() {
        return context;
    }

}
