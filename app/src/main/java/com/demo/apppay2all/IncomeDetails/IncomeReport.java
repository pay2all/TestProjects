package com.demo.apppay2all.IncomeDetails;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.demo.apppay2all.BaseURL.BaseURL;
import com.demo.apppay2all.CallResAPIGetMethod;
import com.demo.apppay2all.DetectConnection;
import com.demo.apppay2all.R;
import com.demo.apppay2all.SharePrefManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class IncomeReport extends AppCompatActivity {

    RecyclerView recyclerview_account_validation;
    List<com.demo.apppay2all.IncomeDetails.IncomeReportItem> accountValidationItems;
    IncomeReportCardAdapter accountValidationCardAdapter;
    ProgressDialog dialog;

    TextView tv_message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_income_report);

        if (getSupportActionBar()!=null)
        {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        tv_message=findViewById(R.id.tv_message);
        recyclerview_account_validation=findViewById(R.id.recyclerview_account_validation);
        recyclerview_account_validation.setLayoutManager(new LinearLayoutManager(IncomeReport.this));

        accountValidationItems=new ArrayList<>();
        accountValidationCardAdapter=new IncomeReportCardAdapter(IncomeReport.this,accountValidationItems);
        recyclerview_account_validation.setAdapter(accountValidationCardAdapter);

        if (DetectConnection.checkInternetConnection(IncomeReport.this))
        {
            mGetData();
        }
        else
        {
            Toast.makeText(this, "No internet connection", Toast.LENGTH_SHORT).show();
        }
    }

    @SuppressLint("StaticFieldLeak")
    protected void mGetData()
    {
        String sending_url= BaseURL.BASEURL_B2C+"api/reports/v1/income-report?api_token="+ SharePrefManager.getInstance(IncomeReport.this).mGetApiToken();
        new CallResAPIGetMethod(IncomeReport.this,sending_url)
        {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                dialog=new ProgressDialog(IncomeReport.this);
                dialog.setMessage("Please wait...");
                dialog.setCancelable(false);
                dialog.show();
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                dialog.dismiss();

                Log.e("response","data "+s);

                if (!s.equals(""))
                {
                 try{
                     JSONObject jsonObject=new JSONObject(s);
                     JSONArray jsonArray=jsonObject.getJSONArray("report");
                     for (int i=0; i<jsonArray.length(); i++)
                     {
                         JSONObject data=jsonArray.getJSONObject(i);
                         com.demo.apppay2all.IncomeDetails.IncomeReportItem item=new com.demo.apppay2all.IncomeDetails.IncomeReportItem();

                         item.setId(data.getString("id"));
                         item.setName(data.getString("name"));
                         item.setOpening_balance(data.getString("opening_balance"));
                         item.setCredit_amount(data.getString("credit_amount"));
                         item.setDebit_amount(data.getString("debit_amount"));
                         item.setSales(data.getString("sales"));
                         item.setProfit(data.getString("profit"));
                         item.setCharges(data.getString("charges"));
                         item.setPending(data.getString("pending"));

                         item.setClosing_bal(data.getString("closing_bal"));


                         accountValidationItems.add(item);
                         accountValidationCardAdapter.notifyDataSetChanged();

                     }

                     if (accountValidationItems.size()!=0)
                     {

                         recyclerview_account_validation.setVisibility(View.VISIBLE);
                         tv_message.setVisibility(View.GONE);
                     }
                     else
                     {
                         recyclerview_account_validation.setVisibility(View.GONE);
                         tv_message.setVisibility(View.VISIBLE);
                         tv_message.setText("No record found");

                     }
                 }
                 catch (JSONException e)
                 {
                     e.printStackTrace();
                 }
                }
            }
        }.execute();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId()==android.R.id.home)
        {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}