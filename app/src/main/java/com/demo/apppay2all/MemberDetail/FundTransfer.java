package com.demo.apppay2all.MemberDetail;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
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
import com.demo.apppay2all.FundRequesDetails.FundRequest;
import com.demo.apppay2all.LocationDetails.MyLocation;
import com.demo.apppay2all.NumberToWord;
import com.demo.apppay2all.R;
import com.demo.apppay2all.Review_Activity;
import com.demo.apppay2all.SharePrefManager;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
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

public class FundTransfer extends AppCompatActivity implements LocationListener {

    EditText ed_amount,ed_remark,ed_password;
    Button bt_send;
    TextView tv_name,tv_mobile,tv_email,tv_role,tv_balance,tv_amount_error,tv_password_error;

    ProgressDialog dialog;

    MemberItems memberItems;





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
        setContentView(R.layout.activity_fund_transfer);

        if (getSupportActionBar()!=null)
        {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }



//        to get location
        boolean r = myLocation.getLocation(getApplicationContext(),
                location);
        if (r) {
            Log.e("location", "found");
        } else {
            Log.e("location", "Not found");
        }

        tv_location=findViewById(R.id.tv_location);
        locationManager=(LocationManager) getSystemService(Context.LOCATION_SERVICE);
        tv_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (ActivityCompat.checkSelfPermission(FundTransfer.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(FundTransfer.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(FundTransfer.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
                }
                else{
                    mShowEnabledLocationDialog();
                }
            }
        });


        memberItems=(MemberItems)getIntent().getSerializableExtra("DATA");

        tv_name=findViewById(R.id.tv_name);
        tv_mobile=findViewById(R.id.tv_mobile);
        tv_email=findViewById(R.id.tv_email);
        tv_role=findViewById(R.id.tv_role);
        tv_balance=findViewById(R.id.tv_balance);
        bt_send=findViewById(R.id.bt_send);
        
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
        tv_amount_error=findViewById(R.id.tv_amount_error);
        ed_remark=findViewById(R.id.ed_remark);

        tv_name.setText(memberItems.getName());
        tv_email.setText("Email "+memberItems.getEmail());
        tv_mobile.setText("Mobile "+memberItems.getMobile());
        tv_balance.setText("Rs "+memberItems.getBalance());
        tv_role.setText(memberItems.getRole());

        ed_password=findViewById(R.id.ed_password);
        tv_password_error=findViewById(R.id.tv_password_error);

        bt_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    tv_location.setVisibility(View.GONE);
                if (DetectConnection.checkInternetConnection(FundTransfer.this))
                {
                    if (ed_amount.getText().toString().equals(""))
                    {
                        tv_amount_error.setText("Please enter amount");
                        tv_amount_error.setVisibility(View.VISIBLE);
                    }
                    else if (ed_password.getText().toString().equals(""))
                    {
                        tv_password_error.setVisibility(View.VISIBLE);
                        tv_password_error.setText("Please enter your login password");
                    }
                    else
                    {
                        tv_password_error.setVisibility(View.GONE);
                        tv_amount_error.setVisibility(View.GONE);
                        mSendAmount(memberItems.getId());
                    }
                }
                else
                {
                    Toast.makeText(FundTransfer.this, "No internet connection", Toast.LENGTH_SHORT).show();
                }
                }
                else {
                    tv_location.setVisibility(View.VISIBLE);
                    tv_location.setText("Please Enable Location");
                    tv_location.setTextColor(getResources().getColor(R.color.red));
                    Toast.makeText(FundTransfer.this, "Please enable location", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @SuppressLint("StaticFieldLeak")

    protected void mSendAmount(String id)
    {
        Uri.Builder builder=new Uri.Builder();
        builder.appendQueryParameter("api_token", SharePrefManager.getInstance(FundTransfer.this).mGetApiToken());
        builder.appendQueryParameter("id",id);
        builder.appendQueryParameter("remark",ed_remark.getText().toString());
        builder.appendQueryParameter("amount",ed_amount.getText().toString());
        builder.appendQueryParameter("password",ed_password.getText().toString());


        builder.appendQueryParameter("latitude",lat+"");
        builder.appendQueryParameter("longitude",log+"");
        builder.appendQueryParameter("dupplicate_transaction",System.currentTimeMillis()+"");

        String sending_url= BaseURL.BASEURL_B2C +"api/admin/balance-transfer";
        new CallResAPIPOSTMethod(FundTransfer.this,builder,sending_url,true,"POST")
        {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                dialog=new ProgressDialog(FundTransfer.this);
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

                        Intent intent=new Intent(FundTransfer.this, Review_Activity.class);
                        intent.putExtra("status",status);
                        intent.putExtra("message",message);
                        intent.putExtra("activity","fund");
                        startActivity(intent);
                        finish();
                    }
                    catch (JSONException e)
                    {
                        e.printStackTrace();
                    }
                }
                else
                {
                    Toast.makeText(FundTransfer.this, "No internet connection", Toast.LENGTH_SHORT).show();
                }
            }
        }
        .execute();
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

        mSettingsClient = LocationServices.getSettingsClient(FundTransfer.this);

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
                                    rae.startResolutionForResult(FundTransfer.this, REQUEST_CHECK_SETTINGS);
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