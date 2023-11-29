package com.demo.apppay2all;
import android.content.Context;
import android.content.SharedPreferences;

public class PreferenceManager {

    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Context _context;

    // shared pref mode


    // Shared preferences file name




    public PreferenceManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences("firstlaunch", 0);
        editor = pref.edit();
    }

    public void setFirstTimeLaunch(boolean isFirstTime) {
        editor.putBoolean("first", isFirstTime);
        editor.commit();
    }

    public boolean isFirstTimeLaunch() {
        return pref.getBoolean("first", true);
    }
}