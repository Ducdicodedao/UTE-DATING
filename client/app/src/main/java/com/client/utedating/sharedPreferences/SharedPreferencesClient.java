package com.client.utedating.sharedPreferences;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.client.utedating.models.User;
import com.google.gson.Gson;

public class SharedPreferencesClient {
    private static String MY_SHARE_PREFERENCES = "MY_SHARE_PREFERENCES";
    private Context mContext;
    public SharedPreferencesClient(Context mContext) {
        this.mContext = mContext;
    }

    public void putUserInfo(String key, User user){
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(MY_SHARE_PREFERENCES, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = gson.toJson(user);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, json);
        editor.apply();
    }

    public User getUserInfo(String key){
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(MY_SHARE_PREFERENCES, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString(key, null);
        User user = gson.fromJson(json, User.class);
        return user;
    }
}
