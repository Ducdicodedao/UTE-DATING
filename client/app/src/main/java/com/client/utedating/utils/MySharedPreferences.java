package com.client.utedating.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.client.utedating.models.User;
import com.google.gson.Gson;

public class MySharedPreferences {
    private static final String APP_SHARED_PREFERENCE = "MY_SHARE_PREFERENCES";
//    public static final String USER ="user";
//    public static final String JWT ="jwt";

    public static void setStringSharedPreference(Context context, String Key, String Value) {
        SharedPreferences.Editor editor = context.getSharedPreferences(APP_SHARED_PREFERENCE, 0).edit();
        editor.putString(Key, Value);
        editor.apply();
    }

    public static String getStringSharedPreference(Context context, String Key) {
        SharedPreferences pref = context.getSharedPreferences(APP_SHARED_PREFERENCE, 0);
        if (pref.contains(Key)) {
            return pref.getString(Key, "");
        }
        return null;
    }
    public static String getStringSharedPreference(Context context, String Key, String defaultValue) {
        SharedPreferences pref = context.getSharedPreferences(APP_SHARED_PREFERENCE, 0);
        if (pref.contains(Key)) {
            return pref.getString(Key, "");
        }
        return defaultValue;
    }
    public static boolean getBooleanSharedPreference(Context context, String Key, Boolean defaultValue) {
        SharedPreferences pref = context.getSharedPreferences(APP_SHARED_PREFERENCE, 0);
        if (pref.contains(Key)) {

            return pref.getBoolean(Key, defaultValue);
        }
        return defaultValue;
    }
    public static void setBooleanSharedPreference(Context context, String key, boolean value) {
        SharedPreferences.Editor editor = context.getSharedPreferences(APP_SHARED_PREFERENCE, 0).edit();
        editor.putBoolean(key, value);
        editor.apply();
    }
    public static void setIntSharedPreference(Context context, String Key, Integer value) {
        SharedPreferences.Editor editor = context.getSharedPreferences(APP_SHARED_PREFERENCE, 0).edit();
        editor.putInt(Key, value);
        editor.apply();
    }
    public static int getIntSharedPreference(Context context, String Key) {
        SharedPreferences pref = context.getSharedPreferences(APP_SHARED_PREFERENCE, 0);
        if (pref.contains(Key)) {
            return pref.getInt(Key, 0);
        }
        return 0;
    }
    public static void putUserInfo(Context context, String key, User user){
        SharedPreferences.Editor editor = context.getSharedPreferences(APP_SHARED_PREFERENCE, 0).edit();
        Gson gson = new Gson();
        String json = gson.toJson(user);
        editor.putString(key, json);
        editor.apply();
    }

    public static User getUserInfo(Context context, String key){
        SharedPreferences sharedPreferences = context.getSharedPreferences(APP_SHARED_PREFERENCE, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString(key, null);
        User user = gson.fromJson(json, User.class);
        return user;
    }
}
