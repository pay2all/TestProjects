package com.demo.apppay2all;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;


import com.demo.apppay2all.BaseURL.BaseURL;
import com.pay2all.microatm.MicroATMLaunch;

import org.json.JSONException;
import org.json.JSONObject;


public class MicroAtm extends AppCompatActivity {

    ProgressDialog dialog;
    LinearLayout ll_micro_atm_enquiry,ll_withdrwal;
    String mobile="";
    String outlet_id="";
    String message="";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_micro_atm);

        if (getSupportActionBar()!=null)
        {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        if (!CheckStoragePermission(Manifest.permission.BLUETOOTH))
        {
            message=message+"Bluetooth permission not allowed";
        }

         if (!CheckStoragePermission(Manifest.permission.BLUETOOTH_ADMIN))
        {
         message=message+" \n Bluetooth Admin permission not allowed";
        }

         if (!CheckStoragePermission(Manifest.permission.READ_PHONE_STATE))
        {
            message=message+" \n READ PHONE State permission not allowed";
        }

//        Toast.makeText(this, message, Toast.LENGTH_LONG).show();

        outlet_id=SharePrefManager.getInstance(MicroAtm.this).mGetOutletId();
        ll_micro_atm_enquiry=findViewById(R.id.ll_micro_enquiry);
        mobile=SharePrefManager.getInstance(MicroAtm.this).mGetUsername();
        ll_micro_atm_enquiry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (DetectConnection.checkInternetConnection(MicroAtm.this))
                {
                    mGetOutletId("be","Balance Enquiry");
                }
                else {
                    Toast.makeText(MicroAtm.this, "No internet connection", Toast.LENGTH_SHORT).show();
                }
            }
        });
        ll_withdrwal=findViewById(R.id.ll_micro_cash_withdrawal);
        ll_withdrwal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (DetectConnection.checkInternetConnection(MicroAtm.this))
                {

                    mGetOutletId("cw","Cash Withdrawal");
                }
                else
                {
                    Toast.makeText(MicroAtm.this, "No Internet connection", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
//        else if (item.getItemId()==R.id.action_pair)
//        {
//            startActivity(new Intent(MicroAtm.this, BluetoothDeviceList.class));
//        }
        return super.onOptionsItemSelected(item);
    }

    protected void mCallServices(String service_code,String remark)
    {
        if (!outlet_id.equals("")) {
            int INTENT_CODE = 1421;
            Intent intent = new Intent(MicroAtm.this, MicroATMLaunch.class);
            intent.putExtra("outlet_id", outlet_id);
            intent.putExtra("service", service_code);
            intent.putExtra("mobile", SharePrefManager.getInstance(MicroAtm.this).mGetUsername());
            intent.putExtra("remark", remark);
            startActivityForResult(intent, INTENT_CODE);
        }
        else
        {
            Toast.makeText(this, "Merchant id not found, please contact to admin for same", Toast.LENGTH_SHORT).show();
        }
    }

    @SuppressLint("StaticFieldLeak")
    protected void mGetOutletId(String service,String remark)
    {
        Uri.Builder builder=new Uri.Builder();
        builder.appendQueryParameter("api_token",SharePrefManager.getInstance(MicroAtm.this).mGetApiToken());
        String sending_url= BaseURL.BASEURL_B2C+"api/application/v1/aeps-outlet-id";
        new CallResAPIPOSTMethod(MicroAtm.this,builder,sending_url,true,"POST"){
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                dialog=new ProgressDialog(MicroAtm.this);
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
                                SharePrefManager.getInstance(MicroAtm.this).mSaveSingleData("icici_agent_id", icici_agent_id);
                            }

                            if (jsonObject.has("outlet_id")) {
                                outlet_id = jsonObject.getString("outlet_id");
                                SharePrefManager.getInstance(MicroAtm.this).mSaveSingleData("outlet_id", outlet_id);
                            }
                            mCallServices(service,remark);

                        }
                        else
                        {
                            if (!message.equals(""))
                            {
                                Toast.makeText(MicroAtm.this, message, Toast.LENGTH_SHORT).show();
                            }
                            else
                            {
                                Toast.makeText(MicroAtm.this, "Something went wrong", Toast.LENGTH_SHORT).show();
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

//        if (data.hasExtra("data")) {
//            Toast.makeText(this, data.getDataString(), Toast.LENGTH_SHORT).show();
//        }
//        else
//        {
//            Toast.makeText(this, data.getDataString(), Toast.LENGTH_SHORT).show();
//        }
    }

    private boolean CheckStoragePermission(String permission)
    {
        int result = ContextCompat.checkSelfPermission(getApplicationContext(), permission);
        if (result== PackageManager.PERMISSION_GRANTED)
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    private void RequescameraPermission(String permission)
    {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, permission))
        {
        }
        else {
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.CAMERA},1);
        }
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.add_bt_menu,menu);
//
//        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
//
//        if (mBluetoothAdapter.isEnabled()
//                && mBluetoothAdapter.getProfileConnectionState(BluetoothHeadset.HEADSET) == BluetoothHeadset.STATE_CONNECTED)
//        {
//            menu.findItem(R.id.action_pair).setTitle("Bluetooth Connected");
//        }
//        else
//        {
//            menu.findItem(R.id.action_pair).setTitle("Bluetooth not Connected");
//        }
//
//        return super.onCreateOptionsMenu(menu);
//    }
}
