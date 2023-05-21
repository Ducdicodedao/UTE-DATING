package com.client.utedating.retrofit;


import com.client.utedating.models.NotificationReceived;
import com.client.utedating.models.NotificationSend;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface NotificationApiService {
    @Headers({
            "Content-Type: application/json",
            "Authorization: key=AAAAzC9I0fI:APA91bFktxatDyjWAW9yDrcKlTwsduWHLqSQMixSVQ9TDPVrZpg3fN_oRSOXKBmH-8Rrfj1E4pn8kq3qq3Eswzemu9Layn0HSE4FHJh5SAnrClsgaP6Cqi1t9td_6-fGZOXT3s-ufuxE"
    })
    @POST("fcm/send")
    Call<NotificationReceived> sendNotification(@Body NotificationSend body);
}
