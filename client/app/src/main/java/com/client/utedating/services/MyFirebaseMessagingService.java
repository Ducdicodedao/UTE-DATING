package com.client.utedating.services;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.client.utedating.MyApplication;
import com.client.utedating.R;
import com.client.utedating.activities.MainActivity;
import com.client.utedating.models.User;
import com.client.utedating.models.UserModel;
import com.client.utedating.retrofit.RetrofitClient;
import com.client.utedating.retrofit.UserApiService;
import com.client.utedating.utils.MySharedPreferences;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.e("TAG", "onMessageReceived");
        Log.d("TAG", "From: " + remoteMessage.getFrom());

        // Kiểm tra xem thông báo có dữ liệu không
        if (remoteMessage.getData().size() > 0) {
            Log.d("TAG", "Message data payload: " + remoteMessage.getData());
        }

        // Kiểm tra xem thông báo có chứa thông điệp không
        if (remoteMessage.getNotification() != null) {
            showNotification(remoteMessage.getNotification().getTitle(), remoteMessage.getNotification().getBody());
        }
    }

    @Override
    public void onNewToken(@NonNull String token) {
        super.onNewToken(token);
        User user = MySharedPreferences.getUserInfo(getApplicationContext(), "user");
        if(user != null){
            user.setToken(token);
            MySharedPreferences.putUserInfo(getApplicationContext(),"user", user);
            UserApiService userApiService = RetrofitClient.getInstance().create(UserApiService.class);
            userApiService.updateInfo(user.get_id(), user).enqueue(new Callback<UserModel>() {
                @Override
                public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                    if (response.isSuccessful()) {
                        Log.e("TAG", "Token Updated");
                    }
                }

                @Override
                public void onFailure(Call<UserModel> call, Throwable t) {
                    Log.e("TAG", t.getMessage());
                }
            });
        }
    }

    private void showNotification(String title, String body) {
        Intent i = new Intent(this, MainActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, i, PendingIntent.FLAG_ONE_SHOT);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), MyApplication.CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_baseline_android_24)
                .setContentTitle(title)
                .setContentText(body)
                .setAutoCancel(true)
                .setVibrate(new long[]{1000, 1000, 1000, 1000})
                .setOnlyAlertOnce(true)
                .setContentIntent(pendingIntent)
                ;
        Notification notification = builder.build();
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(1, notification);
    }

    private RemoteViews customView(String title, String body){
         RemoteViews remoteViews = new RemoteViews(getApplicationContext().getPackageName(), R.layout.notification_message);
         remoteViews.setTextViewText(R.id.textViewNotificationTitle, title);
         remoteViews.setTextViewText(R.id.textViewNotificationBody, body);
         remoteViews.setImageViewResource(R.id.imageViewNotification, R.drawable.ic_baseline_android_24);
         return remoteViews;
    }

}
