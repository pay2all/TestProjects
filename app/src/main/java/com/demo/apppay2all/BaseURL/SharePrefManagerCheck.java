package com.demo.apppay2all.BaseURL;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;

import java.lang.ref.WeakReference;

public class SharePrefManagerCheck {
    private static final String SHARE_PREFRERENCE="https_valid";
    private static SharePrefManagerCheck mInstance;
    private static Context mContext;
    String mResponse="";

    ProgressDialog dialog;

    private SharePrefManagerCheck(Context context)
    {
        mContext=context;
    }

    public static synchronized SharePrefManagerCheck getInstance(Context context)
    {
        if (mInstance==null)
        {
            mInstance=new SharePrefManagerCheck(context);
        }
        return mInstance;
    }

    public void mSaveError(String value)
    {
        SharedPreferences sharedPreferences=mContext.getSharedPreferences(SHARE_PREFRERENCE,0);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putString("error_save",value);
        editor.apply();
    }

    public String mGetError()
    {
        SharedPreferences sharedPreferences=mContext.getSharedPreferences(SHARE_PREFRERENCE,0);
        String aeps_balance=sharedPreferences.getString("error_save","");
        return aeps_balance;
    }
}