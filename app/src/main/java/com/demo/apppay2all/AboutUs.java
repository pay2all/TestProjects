package com.demo.apppay2all;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.demo.apppay2all.BaseURL.BaseURL;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import static android.text.Layout.JUSTIFICATION_MODE_INTER_WORD;

public class AboutUs extends AppCompatActivity {

    SwipeRefreshLayout swiperefresh_about;
    LinearLayout ll_about;
    TextView textview_about;
    TextView textview_message;

    String username,password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        SharedPreferences sharedPreferences=getSharedPreferences("user",0);
        username=sharedPreferences.getString("username","");
        password=sharedPreferences.getString("password","");

        swiperefresh_about=findViewById(R.id.swiperefresh_about);
        ll_about=findViewById(R.id.ll_about);
        textview_about=findViewById(R.id.textview_about);
        textview_message=findViewById(R.id.textview_message);

        swiperefresh_about.setColorSchemeColors(getResources().getColor(R.color.colorPrimary));
        swiperefresh_about.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (DetectConnection.checkInternetConnection(AboutUs.this))
                {
                    mGetAboutusData(username,password);
                    ll_about.setVisibility(View.VISIBLE);
                    textview_message.setVisibility(View.GONE);
                }
                else
                {
                    ll_about.setVisibility(View.GONE);
                    textview_message.setText("No Internet Connection");
                    textview_message.setVisibility(View.VISIBLE);
                }
            }
        });

        if (DetectConnection.checkInternetConnection(AboutUs.this))
        {
            mGetAboutusData(username,password);
        }
        else
        {
            ll_about.setVisibility(View.GONE);
            textview_message.setText("No Internet Connection");
            textview_message.setVisibility(View.VISIBLE);
        }

       }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private  void  mGetAboutusData(final String username, final String password) {
        class getJSONData extends AsyncTask<String, String, String> {


            HttpURLConnection urlConnection;


            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                swiperefresh_about.setRefreshing(true);
            }
            @Override
            protected String doInBackground(String... args) {

                StringBuilder result = new StringBuilder();

                try {
                    URL url = new URL(BaseURL.BASEURL +"api/application/v1/page-content");
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

            @SuppressLint("WrongConstant")
            @Override
            protected void onPostExecute(String result) {

                //Do something with the JSON string
                swiperefresh_about.setRefreshing(false);
                Log.e("about response",result);

                String about="";
                if (!result.equals("")) {
                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        about=jsonObject.getString("about");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    textview_about.setText(about);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        textview_about.setJustificationMode(JUSTIFICATION_MODE_INTER_WORD);
                    }
                }
                else
                {
                    ll_about.setVisibility(View.GONE);
                    textview_message.setText("Something went wrong");
                    textview_message.setVisibility(View.VISIBLE);
                }
            }
        }

        getJSONData getJSONData=new getJSONData();
        getJSONData.execute();
    }



}
