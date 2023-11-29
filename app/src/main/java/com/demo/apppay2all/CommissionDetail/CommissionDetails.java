package com.demo.apppay2all.CommissionDetail;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.demo.apppay2all.BaseURL.BaseURL;
import com.demo.apppay2all.CallResAPIGetMethod;
import com.demo.apppay2all.DetectConnection;
import com.demo.apppay2all.SharePrefManager;
import com.demo.apppay2all.CommissionDetail.MyComission.CommissionServiceCardAdapter;
import com.demo.apppay2all.CommissionDetail.MyComission.CommissionServiceItems;
import com.demo.apppay2all.CommissionDetail.MyComission.My_Comission;
import com.demo.apppay2all.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class CommissionDetails extends AppCompatActivity {

    RelativeLayout rl_commission_mobile_prepaid,rl_commission_mobile_postpaid,rl_commission_mobile_dth,rl_commission_mobile_landline,rl_commission_mobile_electricity,rl_commission_mobile_insurance,rl_commission_mobile_broadband,rl_commission_mobile_gas,rl_commission_mobile_water,rl_commission_mobile_utilities;

    RecyclerView recyclerview_commission_service;
    List<CommissionServiceItems> commissionServiceItems;
    CommissionServiceCardAdapter commissionServiceCardAdapter;

    ProgressDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_commission_details);

        recyclerview_commission_service=findViewById(R.id.recyclerview_commission_service);
        recyclerview_commission_service.setLayoutManager(new LinearLayoutManager(CommissionDetails.this));
        commissionServiceItems=new ArrayList<>();
        commissionServiceCardAdapter=new CommissionServiceCardAdapter(CommissionDetails.this,commissionServiceItems);
        recyclerview_commission_service.setAdapter(commissionServiceCardAdapter);

        if (DetectConnection.checkInternetConnection(CommissionDetails.this))
        {
            mGetCommssionService();
        }


        rl_commission_mobile_prepaid=findViewById(R.id.rl_commission_mobile_prepaid);
        rl_commission_mobile_prepaid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent=new Intent(CommissionDetails.this, My_Comission.class);
                intent.putExtra("serviceid","1");
                startActivity(intent);

            }
        });

        rl_commission_mobile_postpaid=findViewById(R.id.rl_commission_mobile_postpaid);
        rl_commission_mobile_postpaid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(CommissionDetails.this, My_Comission.class);
                intent.putExtra("serviceid","4");
                startActivity(intent);
            }
        });


        rl_commission_mobile_dth=findViewById(R.id.rl_commission_mobile_dth);
        rl_commission_mobile_dth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(CommissionDetails.this, My_Comission.class);
                intent.putExtra("serviceid","2");
                startActivity(intent);
            }
        });

        rl_commission_mobile_landline=findViewById(R.id.rl_commission_mobile_landline);
        rl_commission_mobile_landline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(CommissionDetails.this, My_Comission.class);
                intent.putExtra("serviceid","6");
                startActivity(intent);
            }
        });

        rl_commission_mobile_electricity=findViewById(R.id.rl_commission_mobile_electricity);
        rl_commission_mobile_electricity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(CommissionDetails.this, My_Comission.class);
                intent.putExtra("serviceid","5");
                startActivity(intent);
            }
        });

        rl_commission_mobile_insurance=findViewById(R.id.rl_commission_mobile_insurance);
        rl_commission_mobile_insurance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(CommissionDetails.this, My_Comission.class);
                intent.putExtra("serviceid","18");
                startActivity(intent);

            }
        });

        rl_commission_mobile_broadband=findViewById(R.id.rl_commission_mobile_broadband);
        rl_commission_mobile_broadband.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(CommissionDetails.this,My_Comission.class);
                intent.putExtra("serviceid","17");
                startActivity(intent);
            }
        });

        rl_commission_mobile_gas=findViewById(R.id.rl_commission_mobile_gas);
        rl_commission_mobile_gas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(CommissionDetails.this, My_Comission.class);
                intent.putExtra("serviceid","8");
                startActivity(intent);
            }
        });

        rl_commission_mobile_water=findViewById(R.id.rl_commission_mobile_water);
        rl_commission_mobile_water.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent=new Intent(CommissionDetails.this, My_Comission.class);
                intent.putExtra("serviceid","7");

                startActivity(intent);
            }
        });

        rl_commission_mobile_utilities=findViewById(R.id.rl_commission_mobile_utilities);
        rl_commission_mobile_utilities.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


            }
        });

    }

    @SuppressLint("StaticFieldLeak")
    protected void mGetCommssionService()
    {
//        String sending_url= BaseURL.BASEURL_B2C+ "api/commission/service-list?api_token=EOmp8MyuCGyHC5avFkYqBXUqp9nIg1R0EkKtHxM6olO8C6VBBdT4lD2ijB5G";
        String sending_url= BaseURL.BASEURL_B2C+ "api/commission/service-list?api_token="+ SharePrefManager.getInstance(CommissionDetails.this).mGetApiToken();
     new CallResAPIGetMethod(CommissionDetails.this,sending_url)
     {
         @Override
         protected void onPreExecute() {
             super.onPreExecute();
             dialog=new ProgressDialog(CommissionDetails.this);
             dialog.setMessage("Please wait...");
             dialog.setCancelable(false);
             dialog.show();
         }

         @Override
         protected void onPostExecute(String s) {
             super.onPostExecute(s);
             dialog.dismiss();
             Log.e("response","commission "+s);

             if (!s.equals(""))
             {
                 String status="",message="";
                 try
                 {
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
                         JSONArray jsonArray=jsonObject.getJSONArray("services");
                         for (int i=0; i<jsonArray.length(); i++)
                         {
                             JSONObject data=jsonArray.getJSONObject(i);
                             CommissionServiceItems items=new CommissionServiceItems();
                             items.setService_id(data.getString("service_id"));
                             items.setService_name(data.getString("service_name"));
                             items.setService_image(data.getString("service_image"));
                             items.setBbps(data.getString("bbps"));

                             commissionServiceItems.add(items);
                             commissionServiceCardAdapter.notifyDataSetChanged();
                         }
                     }
                     else if (!message.equals(""))
                     {
                         Toast.makeText(CommissionDetails.this, message, Toast.LENGTH_SHORT).show();
                     }
                     else
                     {
                         Toast.makeText(CommissionDetails.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                     }
                 }
                 catch (JSONException e)
                 {
                     e.printStackTrace();
                 }
             }
             else
             {
                 Toast.makeText(CommissionDetails.this, "Something went wrong please try again later", Toast.LENGTH_SHORT).show();
             }
         }
     }.execute();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
