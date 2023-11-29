package com.demo.apppay2all.CommissionDetail.MyComission;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.demo.apppay2all.DetectConnection;
import com.demo.apppay2all.R;
import com.demo.apppay2all.BaseURL.BaseURL;
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


public class My_Comission extends AppCompatActivity {

    RecyclerView recyclerview_comission;
    TextView textview_messag;
    ComissionCardAdaptor comissionCardAdaptor;

    List <ComissionItems>comissionItems;
    ProgressDialog dialog;
    String username,password,serviceid;

    CommissionServiceItems  serviceItems;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my__comission);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        serviceItems=(CommissionServiceItems)getIntent().getSerializableExtra("DATA");

        serviceid=serviceItems.getService_id();
        getSupportActionBar().setTitle(serviceItems.getService_name());



        comissionItems=new ArrayList<>();


        recyclerview_comission=findViewById(R.id.recyclerview_comission);
        textview_messag=findViewById(R.id.textview_messag);
        recyclerview_comission.setLayoutManager(new LinearLayoutManager(My_Comission.this));
        comissionCardAdaptor=new ComissionCardAdaptor(My_Comission.this,comissionItems);
        recyclerview_comission.setAdapter(comissionCardAdaptor);



        SharedPreferences sharedPreferences=getSharedPreferences("user", Context.MODE_PRIVATE);
        username=sharedPreferences.getString("username","");
        password=sharedPreferences.getString("password","");
        if (DetectConnection.checkInternetConnection(this))
        {
            mycommission(username,password);
        }
        else
        {
            Toast.makeText(this, "No Connection", Toast.LENGTH_SHORT).show();
        }
    }


    protected void mycommission( final String username, final String password)
    {
        class commission extends AsyncTask<String,String,String>
        {
            HttpURLConnection urlConnection;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                dialog=new ProgressDialog(My_Comission.this);
                dialog.setMessage("Please wait...");
                dialog.show();
                dialog.setCancelable(true);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                Log.e("response",s);
                ShowData(s);
                if (dialog!=null)
                {
                    if (dialog.isShowing())
                    {
                        dialog.dismiss();
                    }
                }
            }

            @Override
            protected String doInBackground(String... params) {
                StringBuilder result=new StringBuilder();
                try {
                    URL url = new URL(BaseURL.BASEURL_B2C+"api/commission/providers?api_token="+ SharePrefManager.getInstance(My_Comission.this).mGetApiToken() +"&service_id="+serviceid);
//                    URL url = new URL(BaseURL.BASEURL_B2C+"api/commission/providers?api_token=EOmp8MyuCGyHC5avFkYqBXUqp9nIg1R0EkKtHxM6olO8C6VBBdT4lD2ijB5G&service_id="+serviceid);
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
        commission commission=new commission();
        commission.execute();
    }

    protected void ShowData(final String myJSON)
    {

        try
        {
            JSONObject jsonObject=new JSONObject(myJSON);
            JSONArray jsonArray=jsonObject.getJSONArray("providers");
            for (int i=0; i<jsonArray.length(); i++)
            {
                JSONObject data=jsonArray.getJSONObject(i);
                ComissionItems items=new ComissionItems();
                items.setProvider_id(data.getString("provider_id"));
                items.setProvider_name(data.getString("provider_name"));
                items.setService_id(data.getString("service_id"));
                items.setService_name(data.getString("service_name"));
                items.setProvider_icon(data.getString("provider_icon"));
                items.setService_id(serviceid);
                comissionItems.add(items);
                comissionCardAdaptor.notifyDataSetChanged();


            }
        }

        catch (JSONException e)
        {
            e.printStackTrace();
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
}
