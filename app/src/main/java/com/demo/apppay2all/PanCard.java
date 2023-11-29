package com.demo.apppay2all;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;


import com.demo.apppay2all.BaseURL.BaseURL;
import com.demo.apppay2all.LocationDetails.MyLocation;
import com.demo.apppay2all.RechargesServicesDetail.PrepaidMobile;
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

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class PanCard extends AppCompatActivity implements LocationListener {


    Button button_generate;
    EditText editText_userid;
    TextView textview_amount;

    String username,password;

    ProgressDialog dialog;

    RelativeLayout rl_select_quantity;
    TextView textView_quantity;
    String quantity;

    Button button_psa_login;

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
        setContentView(R.layout.activity_pan_card);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        tv_location=findViewById(R.id.tv_location);
        locationManager=(LocationManager) getSystemService(Context.LOCATION_SERVICE);
        tv_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (ActivityCompat.checkSelfPermission(PanCard.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(PanCard.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(PanCard.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
                }
                else{
                    mShowEnabledLocationDialog();
                }
            }
        });

        SharedPreferences sharedPreferences=getSharedPreferences("user",0);
        username=sharedPreferences.getString("username","");
        password=sharedPreferences.getString("password","");

        button_generate=findViewById(R.id.button_generate);
        editText_userid=findViewById(R.id.editText_userid);
        textview_amount=findViewById(R.id.textview_amount);

        button_psa_login=findViewById(R.id.button_psa_login);
        button_psa_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                startActivity(new Intent(PanCard.this,PSALogin.class));

                Intent httpIntent = new Intent(Intent.ACTION_VIEW);
                httpIntent.setData(Uri.parse("https://www.psaonline.utiitsl.com/psaonline/"));
                startActivity(httpIntent);
            }
        });

        rl_select_quantity=findViewById(R.id.rl_select_quantity);
        rl_select_quantity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu=new PopupMenu(PanCard.this,rl_select_quantity);

               for (int i=1;i<31;i++){
                   popupMenu.getMenu().add(i+"");
               }

                popupMenu.show();

                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        quantity=item.getTitle().toString();

                        textView_quantity.setText(quantity);
                        return false;
                    }
                });
            }
        });
        textView_quantity=findViewById(R.id.textView_quantity);


        tv_transaction_pin= findViewById(R.id.tv_transaction_pin);
        if (SharePrefManager.getInstance(PanCard.this).mGetSingleData("transaction_pin").equals("1"))
        {
            tv_transaction_pin.setVisibility(View.VISIBLE);
            tv_transaction_pin.setHint("Enter Transaction PIN");
        }

        button_generate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {

                    tv_location.setVisibility(View.GONE);
                if (DetectConnection.checkInternetConnection(PanCard.this))
                {
                    if (editText_userid.getText().toString().equals(""))
                    {
                        Toast.makeText(PanCard.this, "Please enter user id", Toast.LENGTH_SHORT).show();
                    }
                    else if (quantity.equalsIgnoreCase(""))
                    {
                        Toast.makeText(PanCard.this, "Please select quantity", Toast.LENGTH_SHORT).show();
                    }
                    else if (tv_transaction_pin.getVisibility()==View.VISIBLE&&tv_transaction_pin.getText().toString().equals(""))
                    {
                        Toast.makeText(PanCard.this, "Please enter transaction PIN", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        String userid=editText_userid.getText().toString();
//                        mGenerateToken(username,password,userid,quantity);

                        String pin=tv_transaction_pin.getText().toString();
                        mPanCard(userid,quantity,pin);
                    }
                }
                else
                {
                    Toast.makeText(PanCard.this, "No INternet Connection", Toast.LENGTH_SHORT).show();
                }

                }
                else {
                    tv_location.setVisibility(View.VISIBLE);
                    tv_location.setText("Please Enable Location");
                    tv_location.setTextColor(getResources().getColor(R.color.red));
                    Toast.makeText(PanCard.this, "Please enable location", Toast.LENGTH_SHORT).show();
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
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressLint("StaticFieldLeak")
    protected void mPanCard(final String username, String qty, String transaction_pin)
    {
        String sending_url= BaseURL.BASEURL_B2C+"api/pancard/purchase-coupons";
        Uri.Builder builder=new Uri.Builder();
        builder.appendQueryParameter("api_token",SharePrefManager.getInstance(PanCard.this).mGetApiToken());
        builder.appendQueryParameter("username",username);
        builder.appendQueryParameter("quantity",qty);
        builder.appendQueryParameter("transaction_pin",transaction_pin);


        builder.appendQueryParameter("latitude",lat+"");
        builder.appendQueryParameter("longitude",log+"");
        builder.appendQueryParameter("dupplicate_transaction",System.currentTimeMillis()+"");

        new CallResAPIPOSTMethod(PanCard.this,builder,sending_url,true,"POST")
        {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                dialog=new ProgressDialog(PanCard.this);
                dialog.setMessage("Please wait...");
                dialog.setCancelable(false);
                dialog.show();
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                dialog.dismiss();
                Log.e("data","response "+s);

                String status="";
                String message="";
                try
                {
                    JSONObject jsonObject=new JSONObject(s);

                    if (jsonObject.has("status")) {
                        status = jsonObject.getString("status");
                    }

                    if (jsonObject.has("message")) {
                        message = jsonObject.getString("message");
                    }

                    if (jsonObject.has("errors"))
                    {
                        JSONObject errors=jsonObject.getJSONObject("errors");


                        if (errors.has("username"))
                        {
                            String err=errors.getString("username").replaceAll("\\[","").replaceAll("]","").replaceAll("\"","");
                            Toast.makeText(PanCard.this, err, Toast.LENGTH_SHORT).show();
                        }

                        if (errors.has("quantity"))
                        {
                            String err=errors.getString("quantity").replaceAll("\\[","").replaceAll("]","").replaceAll("\"","");
                            Toast.makeText(PanCard.this, err, Toast.LENGTH_SHORT).show();
                        }


                        if (errors.has("transaction_pin"))
                        {
                            String err=errors.getString("transaction_pin").replaceAll("\\[","").replaceAll("]","").replaceAll("\"","");
                            Toast.makeText(getApplicationContext(), err, Toast.LENGTH_LONG).show();
                        }
                    }
                    else {
                        if (!s.equals(""))
                        {
                            if (status.equals("1")||status.equals("success"))
                            {

                                CustomAlertDialogForStatus customAlertDialogForStatus=new CustomAlertDialogForStatus();
                                customAlertDialogForStatus.mShowDialog(PanCard.this,message,status,username,"N/A","PAN card");

                            }
                            else if (status.equals("0")||status.equals("failure"))
                            {
                                CustomAlertDialogForStatus customAlertDialogForStatus=new CustomAlertDialogForStatus();
                                customAlertDialogForStatus.mShowDialog(PanCard.this,message,status,username,"N/A","PAN card");
                            }
                            else
                            {
                                Toast.makeText(PanCard.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                            }
                        }
                        else
                        {
                            Toast.makeText(PanCard.this, "Server not responding", Toast.LENGTH_SHORT).show();
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

        mSettingsClient = LocationServices.getSettingsClient(PanCard.this);

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
                                    rae.startResolutionForResult(PanCard.this, REQUEST_CHECK_SETTINGS);
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
