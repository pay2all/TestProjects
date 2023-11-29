package com.demo.apppay2all.AepsReportDetails;

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

public class AepsReports extends AppCompatActivity {

    ProgressDialog dialog;
    RecyclerView recyclerview_aeps_ledger;
    List<com.demo.apppay2all.AepsReportDetails.AepsReportItem> aepsReportItems;
    com.demo.apppay2all.AepsReportDetails.AepsReportCardadadapter aepsReportCardadadapter;
//    String type="";

    TextView tv_message;

    String api_url="";
    String title="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aeps_reports);
//        type=getIntent().getStringExtra("type");

        if (getIntent().hasExtra("url"))
        {
            api_url=getIntent().getStringExtra("url");
        }

        if (getIntent().hasExtra("title"))
        {
            title=getIntent().getStringExtra("title");
        }

        tv_message=findViewById(R.id.tv_message);
        if (getSupportActionBar()!=null)
        {
//            if (type.equalsIgnoreCase("aeps"))
//            {
//                getSupportActionBar().setTitle("Aeps Report");
//            }
//            else
//            {
//                getSupportActionBar().setTitle("Aeps Ledger Report");
//            }
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(title);
        }

        recyclerview_aeps_ledger=findViewById(R.id.recyclerview_aeps_ledger);
        recyclerview_aeps_ledger.setLayoutManager(new LinearLayoutManager(AepsReports.this));
        aepsReportItems=new ArrayList<>();
        aepsReportCardadadapter=new com.demo.apppay2all.AepsReportDetails.AepsReportCardadadapter(AepsReports.this,aepsReportItems);
        recyclerview_aeps_ledger.setAdapter(aepsReportCardadadapter);

        if (DetectConnection.checkInternetConnection(AepsReports.this))
        {
//            String sending_url= BaseURL.BASEURL_B2C+ "api/reports/aeps-ledger-report?api_token="+ SharePrefManager.getInstance(AepsReports.this).mGetApiToken();
//            if (type.equalsIgnoreCase("aeps"))
//            {
//                sending_url=BaseURL.BASEURL_B2C+ "api/reports/aeps-report?api_token="+ SharePrefManager.getInstance(AepsReports.this).mGetApiToken();
//            }
            api_url=api_url+"?api_token="+SharePrefManager.getInstance(AepsReports.this).mGetApiToken();
            mGetReports(api_url);
        }
        else
        {
            Toast.makeText(this, "No internet connection", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId()==android.R.id.home)
        {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressLint("StaticFieldLeak")
    protected void mGetReports(String sending_url)
    {

        Log.e("sending","url "+sending_url);
        new CallResAPIGetMethod(AepsReports.this,sending_url)
        {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                dialog=new ProgressDialog(AepsReports.this);
                dialog.setMessage("Please wait...");
                dialog.show();
                dialog.setCancelable(false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                dialog.dismiss();
                Log.e("apes","report "+s);

                if (!s.equals(""))
                {
                    String status="",message="";
                    try{
                        JSONObject jsonObject=new JSONObject(s);
                        if (jsonObject.has("status"))
                        {
                            status=jsonObject.getString("status");
                        }
                        if (jsonObject.has("message"))
                        {
                            message=jsonObject.getString("message");
                        }

                        if (status.equalsIgnoreCase("success"))
                        {
                            JSONArray jsonArray=jsonObject.getJSONArray("reports");
                            for (int i=0; i<jsonArray.length(); i++)
                            {
                                JSONObject data=jsonArray.getJSONObject(i);
                                com.demo.apppay2all.AepsReportDetails.AepsReportItem item=new com.demo.apppay2all.AepsReportDetails.AepsReportItem();
                                item.setId(data.getString("id"));
                                item.setProvider_icon(data.getString("provider_icon"));
                                item.setCreated_at(data.getString("created_at"));
                                item.setTxnid(data.getString("txnid"));

                                if (data.has("description")) {
                                    item.setDescription(data.getString("description"));
                                }
                                if (data.has("provider")) {
                                    item.setProvider(data.getString("provider"));
                                }
//                                if (data.has("provider")) {
//                                    item.setProvider(data.getString("provider"));
//                                }

                                if (data.has("opening_balance")) {
                                    item.setOpening_balance(data.getString("opening_balance"));
                                }

                                if (data.has("debit")) {
                                    item.setDebit(data.getString("debit"));
                                }

                                if (data.has("credit")) {
                                    item.setCredit(data.getString("credit"));
                                }
                                if (data.has("amount")) {
                                    item.setCredit(data.getString("amount"));
                                }

                                if (data.has("profit")) {
                                    item.setProfit(data.getString("profit"));
                                }

                                if (data.has("total_balance")) {
                                    item.setTotal_balance(data.getString("total_balance"));
                                }

                                if (data.has("aadhar_number"))
                                {
                                    item.setDescription(data.getString("aadhar_number"));
                                }

                                item.setType(api_url);

                                aepsReportItems.add(item);
                                aepsReportCardadadapter.notifyDataSetChanged();
                            }

                            if (aepsReportItems.size()!=0)
                            {

                                recyclerview_aeps_ledger.setVisibility(View.VISIBLE);
                                tv_message.setVisibility(View.GONE);
                            }
                            else
                            {
                                recyclerview_aeps_ledger.setVisibility(View.GONE);
                                tv_message.setVisibility(View.VISIBLE);
                                tv_message.setText("No record found");

                            }
                        }
                    }
                    catch (JSONException e)
                    {
                        e.printStackTrace();
                    }
                }
                else
                {
                    Toast.makeText(AepsReports.this, "Server not responding", Toast.LENGTH_SHORT).show();
                }
            }
        }.execute();
    }
}