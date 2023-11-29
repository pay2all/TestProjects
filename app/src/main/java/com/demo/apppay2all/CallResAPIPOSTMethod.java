package com.demo.apppay2all;

import android.app.Activity;
import android.net.Uri.Builder;
import android.os.AsyncTask;
import android.util.Log;

import com.demo.apppay2all.BaseURL.SharePrefManagerCheck;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

public abstract class CallResAPIPOSTMethod extends AsyncTask<String, String, String> {
    Builder builder;
    boolean isbodyavaialbel;
    WeakReference<Activity> mWeakActivity;
    String mMethod;
    HttpURLConnection urlConnection;
    String weburl = "";

    String result="";

    Activity mActivity=null;

    public CallResAPIPOSTMethod(Activity activity, Builder builder2, String url, boolean isbody, String method) {
        mWeakActivity = new WeakReference<>(activity);
        builder = builder2;
        weburl = url;
        isbodyavaialbel = isbody;
        mMethod = method;
        mActivity=activity;

//        if (SharePrefManagerCheck.getInstance(mActivity).mGetError().equals("yes"))
//        {
//           if (weburl.startsWith("http"))
//           {
//               Log.e("web 1","url "+weburl.substring(4,weburl.length()));
//               weburl="https"+weburl.substring(4,weburl.length());
//           }
//           else
//           {
//               Log.e("web 2","url "+weburl.substring(6,weburl.length()));
//               weburl="http"+weburl.substring(5,weburl.length());
//           }
//        }

        Log.e("web","url "+weburl);

    }

    public String doInBackground(String... strArr) {
        InputStream inputStream;
        try {
            HttpURLConnection httpURLConnection = (HttpURLConnection) new URL(this.weburl).openConnection();
            httpURLConnection.setReadTimeout(40000);
            httpURLConnection.setConnectTimeout(40000);
            httpURLConnection.setRequestMethod(mMethod);
            httpURLConnection.setRequestProperty("Accept", "application/json");
            httpURLConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            httpURLConnection.setDoInput(true);
            httpURLConnection.setDoOutput(true);
            Log.e("sending data", this.builder.toString());
            if (isbodyavaialbel) {
                String encodedQuery = this.builder.build().getEncodedQuery();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(httpURLConnection.getOutputStream(), "UTF-8"));
                bufferedWriter.write(encodedQuery);
                bufferedWriter.flush();
            }
            if (httpURLConnection.getResponseCode() == 200) {
                inputStream = httpURLConnection.getInputStream();
            }
            else {
                inputStream = httpURLConnection.getErrorStream();
//                SharePrefManagerCheck.getInstance(mActivity).mSaveError("yes");
            }

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuffer stringBuffer = new StringBuffer();
            while (true) {
                String readLine = bufferedReader.readLine();
                if (readLine == null) {
                    return stringBuffer.toString();
                }
                stringBuffer.append(readLine);
            }
        }
        catch (Exception e2) {
            e2.printStackTrace();
            return e2.getMessage();
        }
    }
}