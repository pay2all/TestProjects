package com.demo.apppay2all.LoginNewDetails;

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
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
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
import com.demo.apppay2all.MainActivity;
import com.demo.apppay2all.R;
import com.demo.apppay2all.Register;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class LoginNew extends AppCompatActivity implements LocationListener{

    EditText ed_mobile,ed_password;
    TextView tv_login;
    TextView tv_forget_password,tv_register;

    private SettingsClient mSettingsClient;
    private LocationSettingsRequest mLocationSettingsRequest;
    private static final int REQUEST_CHECK_SETTINGS = 214;
    private static final int REQUEST_ENABLE_GPS = 516;
    TextView tv_location;

    double lat=0.0,log=0.0;


    MyLocation myLocation = new MyLocation();
    
    String username="",password="",device_id="";

    ProgressDialog dialog;

    TextView tv_message;

    TextView tv_password_error,tv_username_error;
    
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_new);

        if (getSupportActionBar()!=null)
        {
            getSupportActionBar().hide();
        }

        tv_password_error=findViewById(R.id.tv_password_error);
        tv_username_error=findViewById(R.id.tv_username_error);

        tv_message=findViewById(R.id.tv_message);

        tv_location=findViewById(R.id.tv_location);
        tv_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (ActivityCompat.checkSelfPermission(LoginNew.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(LoginNew.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(LoginNew.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
                }
                else{
                    mShowEnabledLocationDialog();
                }
            }
        });

        ed_mobile=findViewById(R.id.ed_mobile);
        ed_password=findViewById(R.id.ed_password);
        
        tv_login=findViewById(R.id.tv_login);
        tv_login .setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LocationManager locationManager=(LocationManager) getSystemService(Context.LOCATION_SERVICE);
                if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    if (DetectConnection.checkInternetConnection(LoginNew.this)) {
                        if (ed_mobile.getText().toString().equals("")) {

                            tv_username_error.setText("Please enter mobile number");
                            tv_username_error.setVisibility(View.VISIBLE);

                        } else if (ed_mobile.getText().toString().length() < 10) {

                            tv_username_error.setText("Please enter a valid mobile number");
                            tv_username_error.setVisibility(View.VISIBLE);

                        } else if (ed_password.getText().toString().equals("")) {

                            tv_password_error.setText("Please enter password");
                            tv_password_error.setVisibility(View.VISIBLE);

                        }
                        else {

                            tv_username_error.setVisibility(View.GONE);
                            tv_password_error.setVisibility(View.GONE);
                            username = ed_mobile.getText().toString();
                            password = ed_password.getText().toString();
                            device_id = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
                            mLogin(username, password, device_id, "login");

//                        String response=SharePrefManager.getInstance(getApplicationContext()).mGetMethod("api/v1/login?username="+username+"&password="+password);
//
//                        mShowLoginDetail(response);
                        }
                    } else {
                        Toast.makeText(LoginNew.this, "No internet connection", Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    Animation animationUtils= AnimationUtils.loadAnimation(LoginNew.this,R.anim.shake_animation);
                    animationUtils.start();
                    tv_location.setText("Please enable location");
                    tv_location.setVisibility(View.VISIBLE);
                    Toast.makeText(LoginNew.this, "Please enable location", Toast.LENGTH_SHORT).show();
                }
            }
        });


        tv_forget_password=findViewById(R.id.tv_forget_password);
        tv_forget_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginNew.this,ForgetPassword.class));
            }
        });
        
        tv_register=findViewById(R.id.tv_register);
        tv_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginNew.this, Register.class));
            }
        });
    }

    @SuppressLint("StaticFieldLeak")
    private  void  mLogin(final  String username, final String password, final String device_id, final String type) {

        String url= BaseURL.BASEURL_B2C+"api/application/v1/login";

//        if (type.equals("otp"))
//        {
//            url=BaseURL.BASEURL_B2C+"api/application/v1/validate-login";
//        }

        Log.e("sending","url "+url);

        Uri.Builder builder=new Uri.Builder();
        builder.appendQueryParameter("username",username);
        builder.appendQueryParameter("password",password);
        builder.appendQueryParameter("device_id",device_id);
//        builder.appendQueryParameter("otp",otp);
        builder.appendQueryParameter("latitude",lat+"");
        builder.appendQueryParameter("longitude",log+"");

        new CallResAPIPOSTMethod(LoginNew.this,builder,url,true,"POST") {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                dialog=new ProgressDialog(LoginNew.this);
                dialog.setMessage("Please wait...");
                dialog.setCancelable(false);
                dialog.show();
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                if (dialog!=null)
                {
                    if (dialog.isShowing())
                    {
                        dialog.dismiss();

                    }
                }
                Log.e("response","data "+s);

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
                        JSONObject jsonObject1=jsonObject.getJSONObject("errors");

                        if (jsonObject1.has("username"))
                        {
                            String username_error=jsonObject1.getString("username");

                            if (username_error.contains("["))
                            {
                                username_error=username_error.replaceAll("\\[","").replaceAll("]","").replaceAll("\"","");
                            }

                            tv_username_error.setVisibility(View.VISIBLE);
                            tv_username_error.setText(username_error);
                            Toast.makeText(LoginNew.this, username_error, Toast.LENGTH_LONG).show();

                        }
                        if (jsonObject1.has("password"))
                        {
                            String err=jsonObject1.getString("password");

                            if (err.contains("["))
                            {
                                err=err.replaceAll("\\[","").replaceAll("]","").replaceAll("\"","");
                            }

                            tv_password_error.setVisibility(View.VISIBLE);
                            tv_password_error.setText(err);
                            Toast.makeText(LoginNew.this, err, Toast.LENGTH_LONG).show();

                        }
                    }

                    if (status.equalsIgnoreCase("success")) 
                    {
                        mShowLoginDetail(s);
                    }
                    else if (status.equalsIgnoreCase("pending"))
                    {
                        Intent intent=new Intent(LoginNew.this, LoginNewOtp.class);
                        intent.putExtra("number",username);
                        intent.putExtra("password",password);
                        intent.putExtra("device_id",device_id);
                        intent.putExtra("message",message);
                        intent.putExtra("lat",lat);
                        intent.putExtra("log",log);
                        startActivity(intent);
                    }
                    else if (!message.equals(""))
                    {
                        tv_message.setText(message);
                        Toast.makeText(LoginNew.this, message, Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        Toast.makeText(LoginNew.this, "Something went wrong...", Toast.LENGTH_SHORT).show();
                    }
                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }
            }
        }.execute();
    }

    protected void mShowLoginDetail(final String response)
    {
        try
        {
            String message="";
            String status="";
            String user_id="";
            String first_name="";
            String last_name="";
            String email="";
            String mobile="";
            String role_id="";
            String scheme_id="";
            String joing_date="";

            String permanent_address="";
            String permanent_state="";
            String permanent_city="";
            String permanent_district="";
            String permanent_pin_code="";


            String present_address="";
            String present_city="";
            String present_state="";
            String present_district="";
            String present_pin_code="";


            String shop_name="";
            String office_address="";
            String call_back_url="";
            String profile_photo="";
            String shop_photo="";
            String gst_regisration_photo="";
            String pancard_photo="";
            String cancel_cheque="";

            String address_proof="";
            String kyc_status="";
            String kyc_remark="";
            String mobile_verified="";
            String lock_amount="";

            String session_id="";
            String active="";
            String reason="";
            String api_token="";
            String user_balance="";
            String aeps_balance="";
            String lien_amount="";
            String recharge="";
            String money="";
            String aeps="";
            String payout="";
            String pancard="";
            String ecommerce="";

            String company_name="";
            String company_email="";
            String company_address="";
            String company_address_two="";
            String support_number="";
            String whatsapp_number="";
            String company_logo="";
            String company_website="";

            String news="";
            String update_one="";
            String update_two="";
            String update_three="";
            String update_for="";
            String sender_id="";
            String company_recharge="";
            String company_money="";
            String company_aeps="";
            String company_payout="";
            String view_plan="";
            String company_pancard="";
            String company_ecommerce="";

            JSONObject jsonObject=new JSONObject(response);

            if (jsonObject.has("message"))
            {
                message=jsonObject.getString("message");
            }

            if (jsonObject.has("status"))
            {
                status=jsonObject.getString("status");
            }

            if (jsonObject.has("broadcast"))
            {
                JSONObject broadcast=jsonObject.getJSONObject("broadcast");
                Log.e("broadcast","details "+broadcast);

                if (broadcast.has("status_id"))
                {
                    String status_id=broadcast.getString("status_id");

                    SharePrefManager.getInstance(LoginNew.this).mSaveSingleData("broadcast_status_id",status_id);
                }

                SharePrefManager.getInstance(LoginNew.this).mSaveSingleData("broadcast",broadcast.toString());
            }


            if (status.equalsIgnoreCase("success"))
            {
                JSONObject userdetail=jsonObject.getJSONObject("userdetails");

                if (userdetail.has("user_id")) {
                    user_id = userdetail.getString("user_id");
                }
                first_name=userdetail.getString("first_name");
                last_name=userdetail.getString("last_name");
                email=userdetail.getString("email");
                mobile=userdetail.getString("mobile");
                role_id=userdetail.getString("role_id");
                scheme_id=userdetail.getString("scheme_id");

                if (userdetail.has("joing_date")) {
                    joing_date = userdetail.getString("joing_date");
                }

                permanent_address=userdetail.getString("address");
                permanent_city=userdetail.getString("city");
                permanent_state=userdetail.getString("state_id");
                permanent_district=userdetail.getString("district_id");
                permanent_pin_code=userdetail.getString("pin_code");

//                present_address=userdetail.getString("present_address");
//                present_city=userdetail.getString("present_city");
//                present_state=userdetail.getString("present_state");
//                present_district=userdetail.getString("present_district");
//                present_pin_code=userdetail.getString("present_pin_code");

                shop_name=userdetail.getString("shop_name");
                office_address=userdetail.getString("office_address");
                call_back_url=userdetail.getString("call_back_url");
                profile_photo=userdetail.getString("profile_photo");
                shop_photo=userdetail.getString("shop_photo");
                gst_regisration_photo=userdetail.getString("gst_regisration_photo");
                pancard_photo=userdetail.getString("pancard_photo");
                cancel_cheque=userdetail.getString("cancel_cheque");
                address_proof=userdetail.getString("address_proof");
                kyc_status=userdetail.getString("kyc_status");
                kyc_remark=userdetail.getString("kyc_remark");
                mobile_verified=userdetail.getString("mobile_verified");
                lock_amount=userdetail.getString("lock_amount");
                session_id=userdetail.getString("session_id");
                active=userdetail.getString("active");
                reason=userdetail.getString("reason");
                api_token=userdetail.getString("api_token");
                user_balance=userdetail.getString("user_balance");
                aeps_balance=userdetail.getString("aeps_balance");
                lien_amount=userdetail.getString("lien_amount");

                if (userdetail.has("account_number"))
                {
                    SharePrefManager.getInstance(LoginNew.this).mSaveSingleData("account_number",userdetail.getString("account_number"));
                }

                if (userdetail.has("ifsc_code"))
                {
                    SharePrefManager.getInstance(LoginNew.this).mSaveSingleData("ifsc_code",userdetail.getString("ifsc_code"));
                }

                if (userdetail.has("pan_username"))
                {
                    SharePrefManager.getInstance(LoginNew.this).mSaveSingleData("pan_username",userdetail.getString("pan_username"));
                }

                if (userdetail.has("ekyc"))
                {
                    SharePrefManager.getInstance(LoginNew.this).mSaveSingleData("ekyc",userdetail.getString("ekyc"));
                }

                if (userdetail.has("pan_number"))
                {
                    SharePrefManager.getInstance(LoginNew.this).mSaveSingleData("pan_number",userdetail.getString("pan_number"));
                }
                if (userdetail.has("agentonboarding"))
                {
                    SharePrefManager.getInstance(LoginNew.this).mSaveSingleData("agentonboarding",userdetail.getString("agentonboarding"));
                }

//                JSONObject userservices=jsonObject.getJSONObject("userservices");
//                recharge=userservices.getString("recharge");
//                money=userservices.getString("money");
//
//                if (userservices.has("money_two")) {
//                    String money_two = userservices.getString("money_two");
//                    SharePrefManager.getInstance(LoginNew.this).mSaveSingleData("money_two",money_two);
//                }
//
//                aeps=userservices.getString("aeps");
//                payout=userservices.getString("payout");
//                pancard=userservices.getString("pancard");
//                ecommerce=userservices.getString("ecommerce");

                JSONObject companydetails=jsonObject.getJSONObject("companydetails");
                company_name=companydetails.getString("company_name");
                company_email=companydetails.getString("company_email");
                company_address=companydetails.getString("company_address");
                company_address_two=companydetails.getString("company_address_two");
                support_number=companydetails.getString("support_number");
                whatsapp_number=companydetails.getString("whatsapp_number");
                company_logo=companydetails.getString("company_logo");
                company_website=companydetails.getString("company_website");
                news=companydetails.getString("news");
                sender_id=companydetails.getString("sender_id");
                view_plan=companydetails.getString("view_plan");
////                update_one=companydetails.getString("update_one");
////                update_two=companydetails.getString("update_two");
////                update_three=companydetails.getString("update_three");
////                update_for=companydetails.getString("update_for");

//                company_recharge=companydetails.getString("recharge");
//                company_money=companydetails.getString("money");
//
                if (companydetails.has("color_start")) {
                    String color_start = companydetails.getString("color_start");
                    SharePrefManager.getInstance(LoginNew.this).mSaveSingleData("color_start",color_start);
                }

                if (companydetails.has("color_end")) {
                    String color_end = companydetails.getString("color_end");
                    SharePrefManager.getInstance(LoginNew.this).mSaveSingleData("color_end",color_end);
                }

                if (companydetails.has("transaction_pin")) {
                    String transaction_pin = companydetails.getString("transaction_pin");
                    SharePrefManager.getInstance(LoginNew.this).mSaveSingleData("transaction_pin",transaction_pin);
                }
//
//                company_aeps=companydetails.getString("aeps");
//                company_payout=companydetails.getString("payout");

//                company_pancard=companydetails.getString("pancard");
//                company_ecommerce=companydetails.getString("ecommerce");
//
//                if (companydetails.has("upi_id")) {
//                    String upi_id = companydetails.getString("upi_id");
//                    SharePrefManager.getInstance(LoginNew.this).mSaveSingleData("upi_id",upi_id);
//                }
//
//                if (companydetails.has("collection")) {
//                    String collection = companydetails.getString("collection");
//                    SharePrefManager.getInstance(LoginNew.this).mSaveSingleData("collection",collection);
//                }

                JSONArray banners=jsonObject.getJSONArray("banner");

                SharePrefManager.getInstance(LoginNew.this).mSaveUserid(user_id);
                SharePrefManager.getInstance(LoginNew.this).mSaveUserData(username,password,first_name,last_name,email,mobile,role_id,scheme_id,
                        joing_date,permanent_address,permanent_city,permanent_district,permanent_pin_code,permanent_state,present_address,
                        present_city,present_district,present_pin_code,present_state,lien_amount,office_address,call_back_url,profile_photo,
                        shop_name,shop_photo,gst_regisration_photo,pancard_photo,cancel_cheque,address_proof,kyc_status,kyc_remark,mobile_verified,
                        lock_amount,session_id,active,reason,api_token,user_balance,aeps_balance,recharge,money,aeps,payout,pancard,ecommerce,
                        company_name,company_email,company_address,company_address_two,support_number,whatsapp_number,company_logo,company_website,
                        news,update_one,update_two,update_three,update_for,sender_id,company_recharge,company_money,company_aeps,company_payout,view_plan,company_pancard,company_ecommerce,banners.toString(),"","");

//                Intent intent = new Intent(LoginNew.this, MainActivitySingle.class);
                Intent intent = new Intent(LoginNew.this, MainActivity.class);
                startActivity(intent);
                finish();
//                mGetOperators();

            }
            else if (!message.equals(""))
            {
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
            }
            else
            {
                Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }

    }

    protected void mShowEnabledLocationDialog()
    {
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(new LocationRequest().setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY));
        builder.setAlwaysShow(true);
        mLocationSettingsRequest = builder.build();

        mSettingsClient = LocationServices.getSettingsClient(LoginNew.this);


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
                                    rae.startResolutionForResult(LoginNew.this, REQUEST_CHECK_SETTINGS);
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
    public void onLocationChanged(@NonNull Location location) {
        Log.e("lat"+location.getLatitude(),"longitude "+location.getLongitude());
    }

    @Override
    public void onLocationChanged(@NonNull List<Location> locations) {
        LocationListener.super.onLocationChanged(locations);
    }

    @Override
    public void onFlushComplete(int requestCode) {
        LocationListener.super.onFlushComplete(requestCode);
    }

    @Override
    public void onProviderEnabled(@NonNull String provider) {
        LocationListener.super.onProviderEnabled(provider);
    }

    @Override
    public void onProviderDisabled(@NonNull String provider) {
        LocationListener.super.onProviderDisabled(provider);
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

    public MyLocation.LocationResult locationResult = new MyLocation.LocationResult() {

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

    @Override
    protected void onResume() {

        if (ActivityCompat.checkSelfPermission(LoginNew.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(LoginNew.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

        }
        else {
            LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                boolean r = myLocation.getLocation(getApplicationContext(),
                        locationResult);
                if (r) {
                    Log.e("location", "found");
                } else {
                    Log.e("location", "Not found");
                }
            }
        }

        super.onResume();
    }
}