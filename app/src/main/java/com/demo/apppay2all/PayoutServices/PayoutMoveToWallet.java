package com.demo.apppay2all.PayoutServices;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
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
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.demo.apppay2all.R;
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

public class PayoutMoveToWallet extends AppCompatActivity implements LocationListener {

    EditText ed_amount,ed_remark,ed_login_password;

    TextView tv_amount_error,tv_login_password;

    Button bt_proceed;
    ProgressDialog dialog;
    String amount="";

    TextView tv_aeps_balance;

    EditText tv_transaction_pin;

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
        setContentView(R.layout.activity_payout_move_to_wallet);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        tv_location=findViewById(R.id.tv_location);
        locationManager=(LocationManager) getSystemService(Context.LOCATION_SERVICE);
        tv_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (ActivityCompat.checkSelfPermission(PayoutMoveToWallet.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(PayoutMoveToWallet.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(PayoutMoveToWallet.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
                }
                else{
                    mShowEnabledLocationDialog();
                }
            }
        });

        tv_aeps_balance=findViewById(R.id.tv_aeps_balance);
        tv_aeps_balance.setText(SharePrefManager.getInstance(PayoutMoveToWallet.this).mGetAeps_balance());

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
        ed_remark=findViewById(R.id.ed_remark);
        ed_login_password=findViewById(R.id.ed_login_password);

        tv_amount_error=findViewById(R.id.tv_amount_error);
        tv_login_password=findViewById(R.id.tv_login_password);

        tv_transaction_pin= findViewById(R.id.tv_transaction_pin);
        if (SharePrefManager.getInstance(PayoutMoveToWallet.this).mGetSingleData("transaction_pin").equals("1"))
        {
            tv_transaction_pin.setVisibility(View.VISIBLE);
            tv_transaction_pin.setHint("Enter Transaction PIN");
        }

        bt_proceed=findViewById(R.id.bt_proceed);
        bt_proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {

                    tv_location.setVisibility(View.GONE);
                if (DetectConnection.checkInternetConnection(PayoutMoveToWallet.this)) {
                    if (ed_amount.getText().toString().equals("")) {
                        tv_amount_error.setText("Please enter amount");
                        tv_amount_error.setVisibility(View.VISIBLE);
                    } else if (ed_login_password.getText().toString().equals("")) {
                        tv_login_password.setVisibility(View.VISIBLE);
                        tv_login_password.setText("Please enter login password");
                    } else {
                        tv_login_password.setVisibility(View.GONE);
                        tv_amount_error.setVisibility(View.GONE);
                        amount = ed_amount.getText().toString();
                        String remark = ed_remark.getText().toString();
                        String login_password = ed_login_password.getText().toString();
                        String pin=tv_transaction_pin.getText().toString();
                        mMoveToWallet(amount, remark, login_password,pin);
                    }
                }
                else
                {
                    Toast.makeText(PayoutMoveToWallet.this, "No internet connection", Toast.LENGTH_SHORT).show();
                }
                }
                else {
                    tv_location.setText("Please enable location");
                    tv_location.setTextColor(getResources().getColor(R.color.red));
                    tv_location.setVisibility(View.VISIBLE);
                    Toast.makeText(PayoutMoveToWallet.this, "Please enabled location", Toast.LENGTH_SHORT).show();
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

    @SuppressLint("StaticFieldLeak")
    protected void mMoveToWallet(String amount,String remark,String loginpassword,String transaction_pin)
    {
        Uri.Builder builder=new Uri.Builder();
        builder.appendQueryParameter("api_token", SharePrefManager.getInstance(PayoutMoveToWallet.this).mGetApiToken());
        builder.appendQueryParameter("amount",amount);
        builder.appendQueryParameter("remark",remark);
        builder.appendQueryParameter("login_password",loginpassword);
        builder.appendQueryParameter("transaction_pin",transaction_pin);

        builder.appendQueryParameter("latitude",lat+"");
        builder.appendQueryParameter("longitude",log+"");
        builder.appendQueryParameter("dupplicate_transaction",System.currentTimeMillis()+"");

        String sending_url= BaseURL.BASEURL_B2C+ "api/settlement/move-to-wallet";
        new CallResAPIPOSTMethod(PayoutMoveToWallet.this,builder,sending_url,true,"POST")
        {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                dialog=new ProgressDialog(PayoutMoveToWallet.this);
                dialog.setMessage("Please wait...");
                dialog.setCancelable(false);
                dialog.show();
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                dialog.dismiss();
                Log.e("data","response "+s);

                if (!s.equals(""))
                {
                    String status="",message="";
                    try {

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
                            mShowRecipt(status,message);
                        }
                    }
                    catch (JSONException e)
                    {
                        e.printStackTrace();
                    }

                }
                else
                {
                    Toast.makeText(PayoutMoveToWallet.this, "Server not response", Toast.LENGTH_SHORT).show();
                }

            }
        }.execute();
    }

    protected void mShowRecipt(final String status,final String message)
    {
        final AlertDialog alertDialog;
        LayoutInflater inflater2 =(LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v2=inflater2.inflate(R.layout.custome_dialog_success_recharge,null);
        ImageView imageview_checkicon=v2.findViewById(R.id.imageview_checkicon);
        TextView textview_requestsendsuccessfull=v2.findViewById(R.id.textview_requestsendsuccessfull);
        TextView textview_oper=v2.findViewById(R.id.textview_operator);
        TextView textview_mobnumber=v2.findViewById(R.id.textview_mobnumber);
        TextView textview_amount=v2.findViewById(R.id.textview_amount);
        Button button_done=v2.findViewById(R.id.button_done);
        TextView textview_mobile_title=v2.findViewById(R.id.textview_mobile_title);
        textview_mobile_title.setVisibility(View.GONE);

        textview_requestsendsuccessfull.setText(message);
        textview_oper.setText("Settlement");
        textview_mobnumber.setVisibility(View.GONE);
        textview_amount.setText("RS "+amount);

        if (status.equalsIgnoreCase("success")){
            imageview_checkicon.setBackground(getResources().getDrawable(R.drawable.checkicon));

        }

        else if (status.equalsIgnoreCase("failure")){
            imageview_checkicon.setBackground(getResources().getDrawable(R.drawable.failure));

        }

        else if (status.equalsIgnoreCase("pending")){
            imageview_checkicon.setBackground(getResources().getDrawable(R.drawable.pending));

        }

        else {
            imageview_checkicon.setBackground(getResources().getDrawable(R.drawable.pending));

        }

        final AlertDialog.Builder builder2=new AlertDialog.Builder(PayoutMoveToWallet.this);
        builder2.setCancelable(false);
        builder2.setView(v2);
        alertDialog=builder2.create();

        button_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
                finish();
            }
        });
        alertDialog.show();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId()==android.R.id.home)
        {
            finish();
        }
        return super.onOptionsItemSelected(item);
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

        mSettingsClient = LocationServices.getSettingsClient(PayoutMoveToWallet.this);

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
                                    rae.startResolutionForResult(PayoutMoveToWallet.this, REQUEST_CHECK_SETTINGS);
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