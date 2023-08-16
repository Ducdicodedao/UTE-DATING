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

    public void setJWT(String key, String jwt){
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(MY_SHARE_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, jwt);
        editor.apply();
    }

    public String getJWT(String key){
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(MY_SHARE_PREFERENCES, Context.MODE_PRIVATE);
        String jwt = sharedPreferences.getString(key, "");
        return jwt;
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

    public void setCardCount(String key, int count){
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(MY_SHARE_PREFERENCES, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = gson.toJson(count);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, json);
        editor.apply();
    }

    public int getCardCount(String key){
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(MY_SHARE_PREFERENCES, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString(key, null);
        int count = gson.fromJson(json, Integer.class);
        return count;
    }
}
