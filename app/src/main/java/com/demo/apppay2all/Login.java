package com.demo.apppay2all;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

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
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.demo.apppay2all.BaseURL.BaseURL;
import com.demo.apppay2all.LocationDetails.MyLocation;
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


import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class Login extends AppCompatActivity implements LocationListener {

    EditText editText_username, editText_password;
    Button button_login;
    ProgressDialog dialog;

    String username,password,device_id;
    TextView textview_forgot_password;

    AlertDialog alertDialog=null;
    EditText edittext_otp;
    Button button_submit,button_cancel;
    String id;
    String otp="";
    int resend_count=0;
    boolean pass_visible=false;

    TextView tv_referal;

    private SettingsClient mSettingsClient;
    private LocationSettingsRequest mLocationSettingsRequest;
    private static final int REQUEST_CHECK_SETTINGS = 214;
    private static final int REQUEST_ENABLE_GPS = 516;

    TextView tv_location;

    double lat=0.0,log=0.0;

    MyLocation myLocation = new MyLocation();

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        if (getSupportActionBar()!=null) {
            getSupportActionBar().hide();
        }

        tv_location=findViewById(R.id.tv_location);
        tv_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (ActivityCompat.checkSelfPermission(Login.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(Login.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(Login.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
                }
                else{
                    mShowEnabledLocationDialog();
                }
            }
        });

        editText_username = findViewById(R.id.edittext_mobileno);
        editText_password = findViewById(R.id.edittext_password);
        button_login = findViewById(R.id.button_login);

        tv_referal=findViewById(R.id.tv_referal);

        tv_referal.setText("Referal Id : "+SharePrefManager.getInstance(Login.this).mGetSingleData("referral_id")+"\n"+SharePrefManager.getInstance(Login.this).mGetSingleData("data_referal1"));

        editText_password.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_LEFT = 0;
                final int DRAWABLE_TOP = 1;
                final int DRAWABLE_RIGHT = 2;
                final int DRAWABLE_BOTTOM = 3;

                if(event.getAction() == MotionEvent.ACTION_UP) {
                    if(event.getRawX() >= (editText_password.getRight() - editText_password.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        // your action here
                        if (pass_visible)
                        {
                            editText_password.setTransformationMethod(new PasswordTransformationMethod());
                            pass_visible=false;
                        }
                        else
                        {
                            editText_password.setTransformationMethod(null);
                            pass_visible=true;
                        }
                        return true;
                    }
                }
                return false;
            }
        });

        getSupportActionBar().hide();
        textview_forgot_password= findViewById(R.id.textview_forgotpassword);
        textview_forgot_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (DetectConnection.checkInternetConnection(Login.this))
                {
                    if (editText_username.getText().toString().equals(""))
                    {
                        Toast.makeText(Login.this, "Please enter mobile number", Toast.LENGTH_SHORT).show();
                    }
                    else if (editText_username.getText().toString().length()<10)
                    {
                        Toast.makeText(Login.this, "Please enter a valid mobile number", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        username=editText_username.getText().toString();
                        ForgotPasword(username);
                    }
                }
                else
                {
                    Toast.makeText(Login.this, "No internet connection", Toast.LENGTH_SHORT).show();
                }
            }
        });

        button_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LocationManager locationManager=(LocationManager) getSystemService(Context.LOCATION_SERVICE);
                if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    if (DetectConnection.checkInternetConnection(Login.this)) {
                        if (editText_username.getText().toString().equals("")) {

                            Toast.makeText(Login.this, "Please enter username", Toast.LENGTH_SHORT).show();

                        }
                        else if (editText_username.getText().toString().length() < 10) {

                            Toast.makeText(Login.this, "Please enter a valid username", Toast.LENGTH_SHORT).show();

                        }
                        else if (editText_password.getText().toString().equals("")) {

                            Toast.makeText(Login.this, "Please enter password", Toast.LENGTH_SHORT).show();

                        }
                        else {

                            username = editText_username.getText().toString();
                            password = editText_password.getText().toString();
                            device_id = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
                            mLogin(username, password, device_id, "login");

//                        String response=SharePrefManager.getInstance(getApplicationContext()).mGetMethod("api/v1/login?username="+username+"&password="+password);
//
//                        mShowLoginDetail(response);
                        }
                    }
                    else {
                        Toast.makeText(Login.this, "No internet connection", Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    Animation animationUtils=AnimationUtils.loadAnimation(Login.this,R.anim.shake_animation);
                    animationUtils.start();
                    tv_location.setText("Please enable location");
                    tv_location.setVisibility(View.VISIBLE);
                    Toast.makeText(Login.this, "Please enable location", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    @SuppressLint("StaticFieldLeak")
    private  void  mLogin(final  String username, final String password, final String device_id, final String type) {

        String url= BaseURL.BASEURL_B2C+"api/application/v1/login";

        if (type.equals("otp"))
        {
            url=BaseURL.BASEURL_B2C+"api/application/v1/validate-login";
        }

        Log.e("sending","url "+url);

        Uri.Builder builder=new Uri.Builder();
        builder.appendQueryParameter("username",username);
        builder.appendQueryParameter("password",password);
        builder.appendQueryParameter("device_id",device_id);
        builder.appendQueryParameter("otp",otp);
        builder.appendQueryParameter("latitude",lat+"");
        builder.appendQueryParameter("longitude",log+"");

        new CallResAPIPOSTMethod(Login.this,builder,url,true,"POST") {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                dialog=new ProgressDialog(Login.this);
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

                            Toast.makeText(Login.this, username_error, Toast.LENGTH_LONG).show();

                        }
                    }

                    if (status.equalsIgnoreCase("success")) {
                        mShowLoginDetail(s);
                    }
                    else if (status.equalsIgnoreCase("pending"))
                    {
                        mShowOTPDialog();
                    }
                    else if (!message.equals(""))
                    {
                        Toast.makeText(Login.this, message, Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        Toast.makeText(Login.this, "Something went wrong...", Toast.LENGTH_SHORT).show();
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

                    SharePrefManager.getInstance(Login.this).mSaveSingleData("broadcast_status_id",status_id);
                }

                SharePrefManager.getInstance(Login.this).mSaveSingleData("broadcast",broadcast.toString());
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
                    SharePrefManager.getInstance(Login.this).mSaveSingleData("account_number",userdetail.getString("account_number"));
                }

                if (userdetail.has("ifsc_code"))
                {
                    SharePrefManager.getInstance(Login.this).mSaveSingleData("ifsc_code",userdetail.getString("ifsc_code"));
                }

                if (userdetail.has("pan_username"))
                {
                    SharePrefManager.getInstance(Login.this).mSaveSingleData("pan_username",userdetail.getString("pan_username"));
                }

                if (userdetail.has("ekyc"))
                {
                    SharePrefManager.getInstance(Login.this).mSaveSingleData("ekyc",userdetail.getString("ekyc"));
                }

                if (userdetail.has("pan_number"))
                {
                    SharePrefManager.getInstance(Login.this).mSaveSingleData("pan_number",userdetail.getString("pan_number"));
                }
                if (userdetail.has("agentonboarding"))
                {
                    SharePrefManager.getInstance(Login.this).mSaveSingleData("agentonboarding",userdetail.getString("agentonboarding"));
                }

//                JSONObject userservices=jsonObject.getJSONObject("userservices");
//                recharge=userservices.getString("recharge");
//                money=userservices.getString("money");
//
//                if (userservices.has("money_two")) {
//                    String money_two = userservices.getString("money_two");
//                    SharePrefManager.getInstance(Login.this).mSaveSingleData("money_two",money_two);
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
                    SharePrefManager.getInstance(Login.this).mSaveSingleData("color_start",color_start);
                }

                if (companydetails.has("color_end")) {
                    String color_end = companydetails.getString("color_end");
                    SharePrefManager.getInstance(Login.this).mSaveSingleData("color_end",color_end);
                }

                if (companydetails.has("transaction_pin")) {
                    String transaction_pin = companydetails.getString("transaction_pin");
                    SharePrefManager.getInstance(Login.this).mSaveSingleData("transaction_pin",transaction_pin);
                }
//
//                company_aeps=companydetails.getString("aeps");
//                company_payout=companydetails.getString("payout");

//                company_pancard=companydetails.getString("pancard");
//                company_ecommerce=companydetails.getString("ecommerce");
//
//                if (companydetails.has("upi_id")) {
//                    String upi_id = companydetails.getString("upi_id");
//                    SharePrefManager.getInstance(Login.this).mSaveSingleData("upi_id",upi_id);
//                }
//
//                if (companydetails.has("collection")) {
//                    String collection = companydetails.getString("collection");
//                    SharePrefManager.getInstance(Login.this).mSaveSingleData("collection",collection);
//                }

                JSONArray banners=jsonObject.getJSONArray("banner");

                SharePrefManager.getInstance(Login.this).mSaveUserid(user_id);
                SharePrefManager.getInstance(Login.this).mSaveUserData(username,password,first_name,last_name,email,mobile,role_id,scheme_id,
                        joing_date,permanent_address,permanent_city,permanent_district,permanent_pin_code,permanent_state,present_address,
                        present_city,present_district,present_pin_code,present_state,lien_amount,office_address,call_back_url,profile_photo,
                        shop_name,shop_photo,gst_regisration_photo,pancard_photo,cancel_cheque,address_proof,kyc_status,kyc_remark,mobile_verified,
                        lock_amount,session_id,active,reason,api_token,user_balance,aeps_balance,recharge,money,aeps,payout,pancard,ecommerce,
                        company_name,company_email,company_address,company_address_two,support_number,whatsapp_number,company_logo,company_website,
                        news,update_one,update_two,update_three,update_for,sender_id,company_recharge,company_money,company_aeps,company_payout,view_plan,company_pancard,company_ecommerce,banners.toString(),"","");

//                Intent intent = new Intent(Login.this, MainActivitySingle.class);
                Intent intent = new Intent(Login.this, MainActivity.class);
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


    @Override
    public void onBackPressed() {
        super.onBackPressed();

        finish();
        overridePendingTransition(0,0);

    }

    @SuppressLint("StaticFieldLeak")
    private  void ForgotPasword(final  String username) {

        String sending_url=BaseURL.BASEURL_B2C+"api/application/v1/forgot-password-otp";
        Uri.Builder builder=new Uri.Builder();
        builder.appendQueryParameter("mobile",username);

        new CallResAPIPOSTMethod(Login.this,builder,sending_url,true,"POST")
        {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                dialog=new ProgressDialog(Login.this);
                dialog.setMessage("Please wait...");
                dialog.setCancelable(false);
                dialog.show();
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                dialog.dismiss();

                Log.e("respose","data "+s);
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

                        if (jsonObject.has("errors")) {
                            JSONObject errors = jsonObject.getJSONObject("errors");

                            if (errors.has("mobile"))
                            {
                                String err=errors.getString("mobile").replaceAll("\\[","").replaceAll("]","").replaceAll("\"","");
                                Toast.makeText(Login.this, err, Toast.LENGTH_SHORT).show();
                            }
                        }
                        else {
                            if (!status.equals("")) {
                                if (status.equalsIgnoreCase("success")) {
                                    mShowForgotPassOTP();
                                }

                                if (!message.equals("")) {
                                    Toast.makeText(Login.this, message, Toast.LENGTH_SHORT).show();
                                }
                            } else if (!message.equals("")) {
                                Toast.makeText(Login.this, message, Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(Login.this, "Something went wrong, Please try again later", Toast.LENGTH_SHORT).show();
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

    protected void mShowForgotPassOTP()
    {
        LayoutInflater inflater2 = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v2 = inflater2.inflate(R.layout.custom_alert_forgot_password, null);

        TextView tv_title = v2.findViewById(R.id.tv_title);
        tv_title.setText("Forgot password OTP");

        button_submit = v2.findViewById(R.id.button_submit);
        button_cancel = v2.findViewById(R.id.button_cancel);

        edittext_otp= v2.findViewById(R.id.edittext_otp);

        final AlertDialog.Builder builder2 = new AlertDialog.Builder(Login.this);
        builder2.setCancelable(false);
//                builder.setMessage("Email already registered");
//
//                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
//
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//
//                    }
//                });

        builder2.setView(v2);


        alertDialog = builder2.create();
        button_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });


        button_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (DetectConnection.checkInternetConnection(Login.this)) {
                    if (edittext_otp.getText().toString().equals(""))
                    {
                        Toast.makeText(Login.this, "Please enter OTP", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        String otp=edittext_otp.getText().toString();
                        SumitresetPassword(username,otp);
                    }
                }
                else {
                    Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
                }
            }
        });

        alertDialog.show();
    }


    @SuppressLint("StaticFieldLeak")
    private  void  SumitresetPassword(final  String mobileno,final  String otp) {

        String sending_url=BaseURL.BASEURL_B2C+"api/application/v1/confirm-forgot-password";
        Uri.Builder builder=new Uri.Builder();
        builder.appendQueryParameter("mobile",mobileno);
        builder.appendQueryParameter("otp",otp);
        new CallResAPIPOSTMethod(Login.this,builder,sending_url,true,"POST")
        {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                dialog=new ProgressDialog(Login.this);
                dialog.setMessage("Please wait...");
                dialog.show();
                dialog.setCancelable(false);
            }
            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                dialog.dismiss();
                Log.e("submit","otp "+s);

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

                    if (jsonObject.has("errors")) {
                        JSONObject errors = jsonObject.getJSONObject("errors");

                        if (errors.has("otp"))
                        {
                            String err=errors.getString("otp").replaceAll("\\[","").replaceAll("]","").replaceAll("\"","");
                            edittext_otp.setError(err);
                        }

                        if (errors.has("mobile"))
                        {
                            String err=errors.getString("mobile").replaceAll("\\[","").replaceAll("]","").replaceAll("\"","");
                            Toast.makeText(Login.this, err, Toast.LENGTH_SHORT).show();
                        }
                    }

                    if (!status.equals(""))
                    {
                        if (status.equalsIgnoreCase("success"))
                        {
                            if (alertDialog!=null) {
                                alertDialog.dismiss();
                            }

                            if (!message.equals(""))
                            {
                                Toast.makeText(Login.this, message, Toast.LENGTH_SHORT).show();
                            }
                        }
                        else if (!message.equals(""))
                        {
                            Toast.makeText(Login.this, message, Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            Toast.makeText(Login.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else if (!message.equals(""))
                    {
                        Toast.makeText(Login.this, message, Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        Toast.makeText(Login.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                    }
                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }
            }

        }.execute();
    }

    protected void mGetOperators()
    {
        class getJSONData extends AsyncTask<String, String, String> {

            HttpURLConnection urlConnection;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                dialog=new ProgressDialog(Login.this);
                dialog.setMessage("Please wait...");
                dialog.show();
                dialog.setCancelable(false);
            }
            @Override
            protected String doInBackground(String... args) {

                StringBuilder result = new StringBuilder();

                try {
                    URL url = new URL(BaseURL.BASEURL_B2C+"api/v1/get-provider");
                    urlConnection = (HttpURLConnection) url.openConnection();
                    InputStream in = new BufferedInputStream(urlConnection.getInputStream());

                    BufferedReader reader = new BufferedReader(new InputStreamReader(in));

                    String line;
                    while ((line = reader.readLine()) != null) {
                        result.append(line);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    urlConnection.disconnect();
                }

                return result.toString();
            }

            @Override
            protected void onPostExecute(String result) {

                //Do something with the JSON string
                dialog.dismiss();
                if (alertDialog!=null) {
                    if (alertDialog.isShowing()) {
                        alertDialog.dismiss();
                    }
                }

//                if (!SharePrefManager.getInstance(Login.this).mKycStatus())
//                {
//                    startActivity(new Intent(Login.this, FormsKyc.class));
//                    finish();
//                }
//                else {
//                    Intent intent = new Intent(Login.this, MainActivitySingle.class);
                    Intent intent = new Intent(Login.this, MainActivity.class);
                    startActivity(intent);
                    finish();
//                }

            }
        }
        new getJSONData().execute();
    }

    protected void mShowOTPDialog()
    {
        LayoutInflater inflater2 = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v2 = inflater2.inflate(R.layout.custome_alertdialog_for_otp, null);

        Button ib_close = v2.findViewById(R.id.ib_close);
        Button ib_submit = v2.findViewById(R.id.ib_submit);

        final EditText ed_otp= v2.findViewById(R.id.ed_otp);
        final TextView tv_otp_error=v2.findViewById(R.id.tv_otp_error);
        TextView tv_resend=v2.findViewById(R.id.tv_resend);

        final AlertDialog.Builder builder2 = new AlertDialog.Builder(Login.this);
        builder2.setCancelable(false);


        builder2.setView(v2);


        ib_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (DetectConnection.checkInternetConnection(Login.this))
                {
                    if (ed_otp.getText().toString().equals(""))
                    {
                        tv_otp_error.setText("Please enter OTP");
                        tv_otp_error.setVisibility(View.VISIBLE);
                    }
                    else
                    {
                        tv_otp_error.setVisibility(View.GONE);
                        otp=ed_otp.getText().toString();
                        mLogin(username,password,device_id,"otp");
                    }
                }
            }
        });

        tv_resend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (DetectConnection.checkInternetConnection(Login.this))
                {
                    if (resend_count!=3)
                    {
                        resend_count+=1;
                        alertDialog.dismiss();
                        mLogin(username,password,device_id,"login");
                    }
                    else
                    {
                        Toast.makeText(Login.this, "Maximum limit exceeded, please try again later", Toast.LENGTH_SHORT).show();
                    }
                }
                else
                {
                    Toast.makeText(Login.this, "No internet connection", Toast.LENGTH_SHORT).show();
                }
            }
        });
        alertDialog = builder2.create();
        ib_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });

        alertDialog.show();
    }

    public void onLoginClick(View View){
        startActivity(new Intent(this,Register.class));
        overridePendingTransition(R.anim.slide_in_right,R.anim.stay);
        finish();

//        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(BaseURL.BASEURL_B2C+"register"));
//        startActivity(browserIntent);

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

    protected void mShowEnabledLocationDialog()
    {
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(new LocationRequest().setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY));
        builder.setAlwaysShow(true);
        mLocationSettingsRequest = builder.build();

        mSettingsClient = LocationServices.getSettingsClient(Login.this);


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
                                    rae.startResolutionForResult(Login.this, REQUEST_CHECK_SETTINGS);
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

        if (ActivityCompat.checkSelfPermission(Login.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(Login.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

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