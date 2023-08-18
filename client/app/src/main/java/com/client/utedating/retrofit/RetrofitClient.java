package com.client.utedating.retrofit;

import androidx.annotation.NonNull;

import com.client.utedating.MyApplication;
import com.client.utedating.activities.LoginGGActivity;
import com.client.utedating.sharedPreferences.SharedPreferencesClient;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import hu.akarnokd.rxjava3.retrofit.RxJava3CallAdapterFactory;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    static HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY);
    static Interceptor jwtInterceptor = new Interceptor(){
        @NonNull
        @Override
        public Response intercept(@NonNull Chain chain) throws IOException {
            Request original = chain.request();
            // Lấy JWT từ lưu trữ (SharedPreferences hoặc Redux store)
            SharedPreferencesClient sharedPreferencesClient = new SharedPreferencesClient(MyApplication.getContext());

            String jwt = sharedPreferencesClient.getJWT("jwt");
            // Thêm Header Authorization
            Request request = original.newBuilder()
                    .header("Authorization", "Bearer " + jwt)
                    .build();
            return chain.proceed(request);
        }
    };
    static OkHttpClient.Builder okBuilder = new OkHttpClient.Builder()
            .readTimeout(30, TimeUnit.SECONDS)
            .connectTimeout(30, TimeUnit.SECONDS)
            .retryOnConnectionFailure(true)
            .addInterceptor(loggingInterceptor)
            .addInterceptor(jwtInterceptor) ;


    private static Retrofit instance;
    public static Retrofit getInstance(){
        if(instance == null)
            instance =  new Retrofit.Builder()
                    .baseUrl("http://10.0.2.2:8800/api/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                    .client(okBuilder.build())
                    .build();
        return instance;
    }
}
