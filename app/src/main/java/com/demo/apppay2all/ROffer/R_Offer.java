package com.demo.apppay2all.ROffer;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.demo.apppay2all.BaseURL.BaseURL;
import com.demo.apppay2all.DetectConnection;
import com.demo.apppay2all.R;
import com.demo.apppay2all.SharePrefManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by Basant on 3/29/2018.
 */

public class R_Offer extends AppCompatActivity {

    List<Plan_Items> plan_items;
    ROfferCardAdapter rOfferCardAdapter;

    LinearLayout ll_contain_listview;
    LinearLayout ll_contain_norecepient_found;

    RecyclerView recyclerview_plan;
    RecyclerView.LayoutManager layoutManager;

    ProgressDialog dialog;

    String number,provider;
    String activity="prepaid";

    String state="";
    String sending_url="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.r_offer_activity);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        number=getIntent().getStringExtra("number");
        provider=getIntent().getStringExtra("provider");
        activity=getIntent().getStringExtra("activity");

        if (getIntent().hasExtra("state")) {
            state = getIntent().getStringExtra("state");
        }



        recyclerview_plan = (RecyclerView) findViewById(R.id.recyclerview_plan);
        recyclerview_plan.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(R_Offer.this);
        recyclerview_plan.setLayoutManager(layoutManager);
        plan_items=new ArrayList<>();
        rOfferCardAdapter=new ROfferCardAdapter(R_Offer.this,plan_items);
        recyclerview_plan.setAdapter(rOfferCardAdapter);

        ll_contain_listview = findViewById(R.id.ll_contain_listview);
        ll_contain_norecepient_found = findViewById(R.id.ll_contain_norecepient_found);

        sending_url=BaseURL.BASEURL_B2C+"api/plans/v1/roffer-plan?api_token="+SharePrefManager.getInstance(R_Offer.this).mGetApiToken()+"&provider_id="+provider+"&state_id="+state+"&mobile_number="+number;


        if (activity.equalsIgnoreCase("dth"))
        {
            sending_url=BaseURL.BASEURL_B2C+"api/plans/v1/dth-roffer-plan?api_token="+SharePrefManager.getInstance(R_Offer.this).mGetApiToken()+"&provider_id="+provider+"&number="+number;
        }

        if (DetectConnection.checkInternetConnection(R_Offer.this))
        {
            mGetROffer(sending_url);
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

    private  void  mGetROffer(String sending_url) {
        class getJSONData extends AsyncTask<String, String, String> {

            HttpURLConnection urlConnection;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                dialog=new ProgressDialog(R_Offer.this);
                dialog.setMessage("Please wait...");
                dialog.show();
                dialog.setCancelable(false);
            }
            @Override
            protected String doInBackground(String... args) {

                StringBuilder result = new StringBuilder();

                try {
                    Log.e("sending","url "+sending_url);
                    URL url = new URL(sending_url);
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

            @Override
            protected void onPostExecute(String result) {

                Log.e("r-offer","data "+result);
                //Do something with the JSON string
                dialog.dismiss();


                if (!result.equals(""))
                {
                    String status="",message="";
                    try {

                        JSONObject jsonObject=new JSONObject(result);
                        JSONArray jsonArray=jsonObject.getJSONArray("plans");
                        for (int i=0; i<jsonArray.length(); i++)
                        {
                            JSONObject data=jsonArray.getJSONObject(i);
                            Plan_Items items=new Plan_Items();
                            items.setAmount(data.getString("rs"));

                            if (data.has("talktime")) {
                                items.setTalktime(data.getString("talktime"));
                            }

                            if (data.has("desc")) {
                                items.setDescription(data.getString("desc"));
                            }

                            if (data.has("validity")) {
                                items.setValidity(data.getString("validity"));
                            }

                            items.setActivity_name(activity);

                            plan_items.add(items);
                            rOfferCardAdapter.notifyDataSetChanged();
                        }

                    }
                    catch (JSONException e)
                    {
                        e.printStackTrace();
                    }
                }
                else
                {
                    Toast.makeText(getApplicationContext(), "Server not responding", Toast.LENGTH_SHORT).show();
                }
            }

        }

        getJSONData getJSONData=new getJSONData();
        getJSONData.execute();
    }

}