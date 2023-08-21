package com.client.utedating.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.client.utedating.R;
import com.client.utedating.utils.MySharedPreferences;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Boolean isFirstTime = MySharedPreferences.getBooleanSharedPreference(SplashActivity.this, "isFirstTime", true);
                Intent intent;
                if(isFirstTime){
                    intent = new Intent(SplashActivity.this, OnBoardingActivity.class);
                }
                else{
                    intent = new Intent(SplashActivity.this, LoginGGActivity.class);
                }
                startActivity(intent);
                finish();
            }
        }, 3000);

    }
}