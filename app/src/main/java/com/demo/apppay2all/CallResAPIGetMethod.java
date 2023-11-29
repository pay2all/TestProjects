package com.demo.apppay2all;

import android.content.Context;
import android.os.AsyncTask;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;

public abstract class CallResAPIGetMethod extends AsyncTask<String, String, String> {

    WeakReference<Context> mWeakActivity;
    HttpURLConnection urlConnection;
    String weburl = "";

    public CallResAPIGetMethod(Context activity, String url) {
        this.mWeakActivity = new WeakReference<>(activity);
        this.weburl = url;
    }

    /* access modifiers changed from: protected */
    @Override
    protected String doInBackground(String... args) {

        StringBuilder result = new StringBuilder();

        try {
            URL url = new URL(weburl);
            urlConnection = (HttpURLConnection) url.openConnection();
            InputStream in = new BufferedInputStream(urlConnection.getInputStream());

            BufferedReader reader = new BufferedReader(new InputStreamReader(in));

            String line;
            while ((line = reader.readLine()) != null) {
                result.append(line);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            urlConnection.disconnect();
        }
        return result.toString();
    }
}
