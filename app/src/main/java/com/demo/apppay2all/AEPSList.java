package com.demo.apppay2all;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.demo.apppay2all.BaseURL.BaseURL;
import com.demo.apppay2all.ServiceDetailSub.SubServiceItem;
//import com.pay2all.aeps.AEPS_Service;

import org.json.JSONException;
import org.json.JSONObject;

public class AEPSList extends AppCompatActivity {

    LinearLayout ll_enquiry,ll_withdrawal,ll_aadhaar_pay,ll_mini;

    ImageView iv_icon1,iv_icon2,iv_icon3,iv_icon4;

    TextView tv_title;

    SubServiceItem item=null;

    ProgressDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aepslist);

        item=(SubServiceItem) getIntent().getSerializableExtra("DATA");
        if (getSupportActionBar()!=null)
        {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

            if (item!=null) {
                getSupportActionBar().setTitle(item.getServicesItems().getName());
            }
        }

        tv_title=findViewById(R.id.tv_title);
        if (item!=null)
        {
            tv_title.setText(item.getServicesItems().getName());
        }

        ll_enquiry=findViewById(R.id.ll_enquiry);
        ll_withdrawal=findViewById(R.id.ll_withdrawal);
        ll_aadhaar_pay=findViewById(R.id.ll_aadhaar_pay);
        ll_mini=findViewById(R.id.ll_mini);

        iv_icon1=findViewById(R.id.iv_icon1);
        iv_icon2=findViewById(R.id.iv_icon2);
        iv_icon3=findViewById(R.id.iv_icon3);
        iv_icon4=findViewById(R.id.iv_icon4);

        if (item!=null)
        {
            if (!item.getService_image().equals(""))
            {
                Glide.with(AEPSList.this).load(item.getService_image()).into(iv_icon1);
                Glide.with(AEPSList.this).load(item.getService_image()).into(iv_icon2);
                Glide.with(AEPSList.this).load(item.getService_image()).into(iv_icon3);
                Glide.with(AEPSList.this).load(item.getService_image()).into(iv_icon4);
            }
        }

        ll_enquiry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mGetOutletId("be");
            }
        });
        ll_withdrawal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mGetOutletId("cw");
            }
        });
        ll_aadhaar_pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mGetOutletId("ap");
            }
        });
        ll_mini.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mGetOutletId("mst");
            }
        });
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
    protected void mGetOutletId(final String service)
    {
        Uri.Builder builder=new Uri.Builder();
        builder.appendQueryParameter("api_token",SharePrefManager.getInstance(AEPSList.this).mGetApiToken());
        String sending_url= BaseURL.BASEURL_B2C+"api/application/v1/aeps-outlet-id";
        new CallResAPIPOSTMethod(AEPSList.this,builder,sending_url,true,"POST"){

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                dialog=new ProgressDialog(AEPSList.this);
                dialog.setMessage("Please wait...");
                dialog.show();
                dialog.setCancelable(false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                dialog.dismiss();
                Log.e("response","id "+s);

                if (!s.equals(""))
                {
                    String status="",message="",icici_agent_id="",outlet_id="";
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

                        if (status.equalsIgnoreCase("success")) {
                            if (jsonObject.has("icici_agent_id")) {
                                icici_agent_id = jsonObject.getString("icici_agent_id");

                                SharePrefManager.getInstance(AEPSList.this).mSaveSingleData("icici_agent_id", icici_agent_id);

                            }

                            if (jsonObject.has("outlet_id")) {
                                outlet_id = jsonObject.getString("outlet_id");

                                SharePrefManager.getInstance(AEPSList.this).mSaveSingleData("outlet_id", outlet_id);

                            }

                            mCallServices(service);

                        }
                        else
                        {
                            if (!message.equals(""))
                            {
                                Toast.makeText(AEPSList.this, message, Toast.LENGTH_SHORT).show();
                            }
                            else
                            {
                                Toast.makeText(AEPSList.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                            }
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

    public void mCallServices(String service){

        if (SharePrefManager.getInstance(AEPSList.this).mGetOutletId().equals("")){
            Toast.makeText(this, "OutLet ID Not Found", Toast.LENGTH_SHORT).show();
        }

        else {
//            Intent intent = new Intent(AEPSList.this, AEPS_Service.class);
//            intent.putExtra("outlet_id", SharePrefManager.getInstance(AEPSList.this).mGetOutletId());
//            intent.putExtra("mobile", SharePrefManager.getInstance(AEPSList.this).mGetUsername());
//            intent.putExtra("name", SharePrefManager.getInstance(AEPSList.this).mGetName());
//            intent.putExtra("service", service);
//            startActivity(intent);
        }
    }
}