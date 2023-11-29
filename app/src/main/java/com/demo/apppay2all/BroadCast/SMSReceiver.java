package com.demo.apppay2all.BroadCast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import com.demo.apppay2all.BaseURL.BaseURL;
import com.demo.apppay2all.DetectConnection;
import com.demo.apppay2all.SharePrefManager;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

/**
 * Created by Basant on 6/13/2018.
 */

public class SMSReceiver extends BroadcastReceiver
{
    public void onReceive(Context context, Intent intent) {


        Bundle myBundle = intent.getExtras();


        if (DetectConnection.checkInternetConnection(context)) {
            assert myBundle != null;
            String id = myBundle.getString("id");
            String apitoken = SharePrefManager.getInstance(context).mGetApiToken();

            String sending_url = BaseURL.BASEURL_B2C + "api/application/v1/notification/read-notification";
            mSubmit(sending_url, id, apitoken);
        }
    }

    protected void mSubmit(final String urll, final String id, final String apitoken) {
        class AddMemebr extends AsyncTask<String, String, String> {

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
            }

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected String doInBackground(String... strings) {

                BufferedReader reader;
                StringBuffer buffer;
                String res = null;

                try {

                    URL url = new URL(urll);
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    con.setReadTimeout(40000);
                    con.setConnectTimeout(40000);
                    con.setRequestMethod("POST");
//                    con.setRequestProperty("Content-Type", "application/json");

//                    paramaters in header
                    con.setRequestProperty("Authorization","Bearer ");
                    con.setRequestProperty("Accept","application/json");
                    con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                    con.setDoInput(true);
                    con.setDoOutput(true);

//                    parameter in body
                    Uri.Builder builder = new Uri.Builder()
                            .appendQueryParameter("id", id)
                            .appendQueryParameter("api_token", apitoken)
                            ;
                    String query = builder.build().getEncodedQuery();


                    OutputStream os = con.getOutputStream();
                    BufferedWriter writer = new BufferedWriter(
                            new OutputStreamWriter(os, "UTF-8"));
                    writer.write(query);
                    writer.flush();


                    int status = con.getResponseCode();
                    InputStream inputStream;
                    if (status == HttpURLConnection.HTTP_OK) {
                        inputStream = con.getInputStream();
                    } else {
                        inputStream = con.getErrorStream();
                    }
                    reader = new BufferedReader(new InputStreamReader(inputStream));
                    buffer = new StringBuffer();
                    String line = "";
                    while ((line = reader.readLine()) != null) {
                        buffer.append(line);
                    }
                    res = buffer.toString();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (ProtocolException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return res;
            }
        }

        AddMemebr addMemebr = new AddMemebr();
        addMemebr.execute();
    }
}