package com.demo.apppay2all.PayoutServices;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.demo.apppay2all.BaseURL.BaseURL;
import com.demo.apppay2all.CallResAPIPOSTMethod;
import com.demo.apppay2all.DetectConnection;
import com.demo.apppay2all.LocationDetails.MyLocation;
import com.demo.apppay2all.NumberToWord;
import com.demo.apppay2all.PanCard;
import com.demo.apppay2all.R;
import com.demo.apppay2all.ReceiptDetail.Receipt;
import com.demo.apppay2all.RechargesServicesDetail.PrepaidMobile;
import com.demo.apppay2all.SharePrefManager;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import org.json.JSONException;
import org.json.JSONObject;

public class PayoutMoveToBank extends AppCompatActivity implements LocationListener {

    TextView textView_ifsc,textView_send_money_beneficiary_name,textView_send_money_account_number,textView_send_money_bank_name;

    EditText ed_amount,ed_login_password;

    TextView tv_amount_error,tv_login_password;
    Button bt_transfer_now;

    ProgressDialog dialog;

    EditText tv_transaction_pin;
    BeneficiaryItems beneficiaryItems;

    TextView tv_location;

    LocationManager locationManager=null;

    private SettingsClient mSettingsClient;
    private LocationSettingsRequest mLocationSettingsRequest;
    private static final int REQUEST_CHECK_SETTINGS = 214;
    private static final int REQUEST_ENABLE_GPS = 516;
    MyLocation myLocation=new MyLocation();

    double lat=0.0,log=0.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payout_type);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        tv_location=findViewById(R.id.tv_location);
        locationManager=(LocationManager) getSystemService(Context.LOCATION_SERVICE);
        tv_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (ActivityCompat.checkSelfPermission(PayoutMoveToBank.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(PayoutMoveToBank.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(PayoutMoveToBank.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
                }
                else{
                    mShowEnabledLocationDialog();
                }
            }
        });

        beneficiaryItems=(com.demo.apppay2all.PayoutServices.BeneficiaryItems)getIntent().getSerializableExtra("DATA");

        
        ed_amount=findViewById(R.id.ed_amount);
        TextView tv_amount_word=findViewById(R.id.tv_amount_word);
        ed_amount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                if (!editable.toString().equals(""))
                {
                    tv_amount_word.setText(new NumberToWord().convert(Integer.parseInt(editable.toString())));
                }
                else
                {
                    tv_amount_word.setText("");
                }
            }
        });
        ed_login_password=findViewById(R.id.ed_login_password);

        tv_amount_error=findViewById(R.id.tv_amount_error);
        tv_login_password=findViewById(R.id.tv_login_password);

        bt_transfer_now=findViewById(R.id.bt_transfer_now);

        textView_send_money_beneficiary_name= findViewById(R.id.textView_send_money_beneficiary_name);
        textView_send_money_beneficiary_name.setText(beneficiaryItems.getHolder_name());

        textView_send_money_account_number= findViewById(R.id.textView_send_money_account_number);
        textView_send_money_account_number.setText(beneficiaryItems.getAccount_number());

        textView_send_money_bank_name= findViewById(R.id.textView_send_money_bank_name);
        textView_send_money_bank_name.setText(beneficiaryItems.getBank_name());


        textView_ifsc= findViewById(R.id.textView_ifsc);
        textView_ifsc.setText(beneficiaryItems.getIfsc_code());

         tv_transaction_pin= findViewById(R.id.tv_transaction_pin);
        if (SharePrefManager.getInstance(PayoutMoveToBank.this).mGetSingleData("transaction_pin").equals("1"))
        {
            tv_transaction_pin.setVisibility(View.VISIBLE);
            tv_transaction_pin.setHint("Enter Transaction PIN");
        }



        bt_transfer_now.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {

                    tv_location.setVisibility(View.GONE);
                    if (DetectConnection.checkInternetConnection(PayoutMoveToBank.this)) {
                        if (ed_amount.getText().toString().equals("")) {
                            tv_amount_error.setText("Please enter amount");
                            tv_amount_error.setVisibility(View.VISIBLE);
                        } else if (ed_login_password.getText().toString().equals("")) {
                            tv_login_password.setVisibility(View.VISIBLE);
                            tv_login_password.setText("Please enter login password");
                        } else if (tv_transaction_pin.getVisibility() == View.VISIBLE && tv_transaction_pin.getText().toString().equals("")) {
                            Toast.makeText(PayoutMoveToBank.this, "Please enter transaction PIN", Toast.LENGTH_SHORT).show();
                        } else {
                            String amount = ed_amount.getText().toString();
                            String password = ed_login_password.getText().toString();
                            String pin = tv_transaction_pin.getText().toString();

                            mTransfer(amount, password, pin);
                        }
                    } else {
                        Toast.makeText(PayoutMoveToBank.this, "No internet connection", Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    tv_location.setText("PLease enable location");
                    tv_location.setTextColor(getResources().getColor(R.color.red));
                    tv_location.setVisibility(View.VISIBLE);
                    Toast.makeText(PayoutMoveToBank.this, "Please enabled location", Toast.LENGTH_SHORT).show();
                }
            }
        });


//        to get location
        boolean r = myLocation.getLocation(getApplicationContext(),
                location);
        if (r) {
            Log.e("location", "found");
        } else {
            Log.e("location", "Not found");
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
    protected void mTransfer(final String amount, String password, String transaction_pin)
    {
        String sending_url= BaseURL.BASEURL_B2C+ "api/settlement/transfer-now";
        Uri.Builder builder=new Uri.Builder();
        builder.appendQueryParameter("api_token", SharePrefManager.getInstance(com.demo.apppay2all.PayoutServices.PayoutMoveToBank.this).mGetApiToken());
        builder.appendQueryParameter("mobile_number",beneficiaryItems.getMobile_number());
        builder.appendQueryParameter("account_number",beneficiaryItems.getAccount_number());
        builder.appendQueryParameter("holder_name",beneficiaryItems.getHolder_name());
        builder.appendQueryParameter("bank_name",beneficiaryItems.getBank_name());
        builder.appendQueryParameter("ifsc_code",beneficiaryItems.getIfsc_code());
        builder.appendQueryParameter("recipient_id",beneficiaryItems.getBeneficiary_id());
        builder.appendQueryParameter("amount",amount);
        builder.appendQueryParameter("password",password);
        builder.appendQueryParameter("transaction_pin",transaction_pin);


        builder.appendQueryParameter("latitude",lat+"");
        builder.appendQueryParameter("longitude",log+"");
        builder.appendQueryParameter("dupplicate_transaction",System.currentTimeMillis()+"");

        new CallResAPIPOSTMethod(com.demo.apppay2all.PayoutServices.PayoutMoveToBank.this,builder,sending_url,true,"POST")
        {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                dialog=new ProgressDialog(com.demo.apppay2all.PayoutServices.PayoutMoveToBank.this);
                dialog.setMessage("Please wat...");
                dialog.setCancelable(false);
                dialog.show();
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                dialog.dismiss();
                Log.e("data","transfer "+s);

                try {
                    String status="",message="";
                    JSONObject jsonObject=new JSONObject(s);
                    if (jsonObject.has("status"))
                    {
                        status=jsonObject.getString("status");
                    }
                    if (jsonObject.has("message"))
                    {
                        message=jsonObject.getString("message");
                    }


                    if (jsonObject.has("errors"))
                    {
                        JSONObject errors=jsonObject.getJSONObject("errors");


                        if (errors.has("amount"))
                        {
                            String err=errors.getString("amount").replaceAll("\\[","").replaceAll("]","").replaceAll("\"","");
                            tv_amount_error.setVisibility(View.VISIBLE);
                            tv_amount_error.setText(err);
                        }
                        if (errors.has("password"))
                        {
                            String err=errors.getString("password").replaceAll("\\[","").replaceAll("]","").replaceAll("\"","");
                            tv_login_password.setVisibility(View.VISIBLE);
                            tv_login_password.setText(err);
                        }

                        if (errors.has("transaction_pin"))
                        {
                            String err=errors.getString("transaction_pin").replaceAll("\\[","").replaceAll("]","").replaceAll("\"","");
                            Toast.makeText(getApplicationContext(), err, Toast.LENGTH_LONG).show();
                        }
                    }
                    else {

                        if (status.equalsIgnoreCase("success")) {
                            Intent intent = new Intent(com.demo.apppay2all.PayoutServices.PayoutMoveToBank.this, Receipt.class);
                            intent.putExtra("status", status);
                            intent.putExtra("message", message);
                            intent.putExtra("number", beneficiaryItems.getAccount_number());
                            intent.putExtra("amount", amount);
                            intent.putExtra("type", "money");
                            intent.putExtra("pay_from", "Pay from AEPS Wallet");
                            intent.putExtra("provider", beneficiaryItems.getBank_name());
                            startActivity(intent);
                            finish();
                        } else if (!message.equals("")) {
                            Toast.makeText(com.demo.apppay2all.PayoutServices.PayoutMoveToBank.this, message, Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }

            }
        }.execute();
    }



    @Override
    public void onLocationChanged(@NonNull Location location) {
        lat=location.getLatitude();
        log=location.getLongitude();
    }


    MyLocation.LocationResult location= new MyLocation.LocationResult() {

        @Override
        public void gotLocation(Location location) {
            double Longitude = location.getLongitude();
            double Latitude = location.getLatitude();

//            Toast.makeText(getApplicationContext(), "Got Location",
//                    Toast.LENGTH_LONG).show();

            tv_location.setText("Got Location");
            tv_location.setTextColor(getResources().getColor(R.color.green));

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    tv_location.setTextColor(getResources().getColor(R.color.red));
                    tv_location.setVisibility(View.GONE);
                }
            },2000);

            lat=Latitude;
            log=Longitude;


//            tv_lat.setText("Latitude : "+lat);
//            tv_long.setText("Longitude : "+log);
        }
    };


    protected void mShowEnabledLocationDialog()
    {
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(new LocationRequest().setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY));
        builder.setAlwaysShow(true);
        mLocationSettingsRequest = builder.build();

        mSettingsClient = LocationServices.getSettingsClient(PayoutMoveToBank.this);

        mSettingsClient
                .checkLocationSettings(mLocationSettingsRequest)
                .addOnSuccessListener(new OnSuccessListener<LocationSettingsResponse>() {
                    @Override
                    public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                        //Success Perform Task Here
                        tv_location.setVisibility(View.GONE);
                    }
                })

                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        int statusCode = ((ApiException) e).getStatusCode();
                        switch (statusCode) {
                            case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                                try {
                                    ResolvableApiException rae = (ResolvableApiException) e;
                                    rae.startResolutionForResult(PayoutMoveToBank.this, REQUEST_CHECK_SETTINGS);
                                } catch (IntentSender.SendIntentException sie) {
                                    Log.e("GPS","Unable to execute request.");
                                }
                                break;
                            case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                                Log.e("GPS","Location settings are inadequate, and cannot be fixed here. Fix in Settings.");
                        }
                    }
                })
                .addOnCanceledListener(new OnCanceledListener() {
                    @Override
                    public void onCanceled() {
                        Log.e("GPS","checkLocationSettings -> onCanceled");
                    }
                });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CHECK_SETTINGS) {
            switch (resultCode) {
                case Activity.RESULT_OK:
                    //Success Perform Task Here
                    break;
                case Activity.RESULT_CANCELED:
                    Log.e("GPS","User denied to access location");
                    openGpsEnableSetting();
                    break;
            }
        } else if (requestCode == REQUEST_ENABLE_GPS) {
            LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            boolean isGpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

            if (!isGpsEnabled) {
                openGpsEnableSetting();
            } else {
                tv_location.setVisibility(View.GONE);
            }
        }
    }

    private void openGpsEnableSetting() {
        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        startActivityForResult(intent, REQUEST_ENABLE_GPS);
    }

}