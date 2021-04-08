package com.example.nasaimageoftheday;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Calendar;
import java.util.Date;

public class PrefConfig {
    private static final String MY_SHARED_PREFS = "com.example.nasaimageoftheday";
    private static final String PREF_DATE = "pref_date";

    public static void saveDateInPref(Context context, String date){
        SharedPreferences pref = context.getSharedPreferences(MY_SHARED_PREFS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(PREF_DATE, date);
        editor.apply();

    }
    public static String loadDateInPref(Context context){
        SharedPreferences pref = context.getSharedPreferences(MY_SHARED_PREFS, Context.MODE_PRIVATE);
        return pref.getString(PREF_DATE, "");
    }
}
