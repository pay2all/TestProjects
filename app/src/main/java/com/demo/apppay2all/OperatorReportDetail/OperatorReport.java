package com.demo.apppay2all.OperatorReportDetail;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
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

public class OperatorReport extends AppCompatActivity {

    RecyclerView  recyclerview;
    List<OperatorReportItem> operatorReportItemList;
    OperatorReportCardAdaptor operatorReportCardAdaptor;
    String username,password;
    ProgressDialog dialog;

    TextView tv_message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_operator_report);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        tv_message=findViewById(R.id.tv_message);

        recyclerview=findViewById(R.id.recyclerview);
        recyclerview.setLayoutManager(new LinearLayoutManager(OperatorReport.this));
        operatorReportItemList =new ArrayList<>();
        operatorReportCardAdaptor =new OperatorReportCardAdaptor(OperatorReport.this, operatorReportItemList);
        recyclerview.setAdapter(operatorReportCardAdaptor);

        SharedPreferences sharedPreferences=getSharedPreferences("user",0);
        username=sharedPreferences.getString("username","");
        password=sharedPreferences.getString("password","");

        if (DetectConnection.checkInternetConnection(OperatorReport.this)){
            mStatment(username,password);
        }
        else {

            Toast.makeText(this, "No Internet Connection", Toast.LENGTH_SHORT).show();
        }


    }


    private  void  mStatment(final  String username,final String password) {
        class getJSONData extends AsyncTask<String, String, String> {


            HttpURLConnection urlConnection;


            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                dialog=new ProgressDialog(OperatorReport.this);
                dialog.setMessage("Please wait...");
                dialog.show();
                dialog.setCancelable(false);
            }
            @Override
            protected String doInBackground(String... args) {

                StringBuilder result = new StringBuilder();

                try {
                    URL url = new URL(BaseURL.BASEURL_B2C+"api/reports/v1/operator-report?api_token="+ SharePrefManager.getInstance(OperatorReport.this).mGetApiToken());
//                    URL url = new URL(BaseURL.BASEURL_B2C+"api/reports/operator-report?api_token=EOmp8MyuCGyHC5avFkYqBXUqp9nIg1R0EkKtHxM6olO8C6VBBdT4lD2ijB5G");
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

                //Do something with the JSON string
                dialog.dismiss();


                Log.e("response ","data"+result);
                if (result.equals("")){
                    Toast.makeText(OperatorReport.this, "Server Not Responding", Toast.LENGTH_SHORT).show();
                }

                else {


                    try {


                       JSONObject jsonObject_report=new JSONObject(result);
                        JSONArray jsonArray=jsonObject_report.getJSONArray("report");
                        for(int i=0;i<jsonArray.length();i++){
                            OperatorReportItem item=new OperatorReportItem();
                            JSONObject jsonObject=jsonArray.getJSONObject(i);
                            item.setProvider_name(jsonObject.getString("provider"));
                            item.setTotal_amount(jsonObject.getString("amount"));
                            item.setTotal_profit(jsonObject.getString("profit"));
                            operatorReportItemList.add(item);
                            operatorReportCardAdaptor.notifyDataSetChanged();

                        }


                        if (operatorReportItemList.size()!=0)
                        {

                            recyclerview.setVisibility(View.VISIBLE);
                            tv_message.setVisibility(View.GONE);
                        }
                        else
                        {
                            recyclerview.setVisibility(View.GONE);
                            tv_message.setVisibility(View.VISIBLE);
                            tv_message.setText("No record found");

                        }
                    }

                    catch (JSONException e){
                        e.printStackTrace();
                    }
                }

            }
        }

        getJSONData getJSONData=new getJSONData();
        getJSONData.execute();
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
