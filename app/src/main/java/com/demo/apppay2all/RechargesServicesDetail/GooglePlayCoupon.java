package com.demo.apppay2all.RechargesServicesDetail;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.bumptech.glide.Glide;
import com.demo.apppay2all.BaseURL.BaseURL;
import com.demo.apppay2all.CallResAPIPOSTMethod;
import com.demo.apppay2all.DetectConnection;
import com.demo.apppay2all.LocationDetails.MyLocation;
import com.demo.apppay2all.NumberToWord;
import com.demo.apppay2all.ProviderDetail.Operator;
import com.demo.apppay2all.R;
import com.demo.apppay2all.ReceiptDetail.Receipt;
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

public class GooglePlayCoupon extends AppCompatActivity implements LocationListener {

    static LinearLayout ll_number,ll_amount;
    static TextView tv_number1;

    public static EditText ed_number,ed_amount;

    Button bt_proceed, bt_Verify;

    public static TextView tv_provider;

    public static String provider_id="384",provider_name="Google Play Vouchers",provider_image="";
//    ServicesItems rechargeItems;

    static ProgressDialog dialog;
    TextView tv_number_error,tv_operator_error,tv_amount_error;

    LinearLayout ll_bill_detail;

    String number="",amount="",plan_price="";

//    static String service_id="";

    static Activity activity;

    TextView textview_mobile_number,textview_monthly,textview_balance,textview_customer_name;

    LinearLayout ll_dthinfo;

    boolean once_verify=false;

    ImageView iv_ope_icon;
    TextView tv_operator;

    AlertDialog alertDialog=null;



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
        setContentView(R.layout.activity_google_play_coupon);


        tv_location=findViewById(R.id.tv_location);
        locationManager=(LocationManager) getSystemService(Context.LOCATION_SERVICE);
        tv_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (ActivityCompat.checkSelfPermission(GooglePlayCoupon.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(GooglePlayCoupon.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(GooglePlayCoupon.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
                }
                else{
                    mShowEnabledLocationDialog();
                }
            }
        });

        Log.e("act","dth");
        activity=this;
//        Intent intent=getIntent();
//        if (intent.hasExtra("provider_id"))
//        {
//            provider_id=intent.getStringExtra("provider_id");
//        }
//
//        if (intent.hasExtra("provider_name"))
//        {
//            provider_name=intent.getStringExtra("provider_name");
//        }
//        if (intent.hasExtra("provider_image"))
//        {
//            provider_image=intent.getStringExtra("provider_image");
//        }

//        rechargeItems=(ServicesItems) getIntent().getSerializableExtra("DATA");

//        service_id=rechargeItems.getId();
        bt_proceed=findViewById(R.id.bt_proceed);
        bt_Verify =findViewById(R.id.bt_Verify);

        ll_amount=findViewById(R.id.ll_amount);
        
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

        tv_number1=findViewById(R.id.tv_number1);

        if (getSupportActionBar()!=null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Google Play Vouchers");
        }

        ll_number=findViewById(R.id.ll_number);
        ed_number=findViewById(R.id.ed_number);

        iv_ope_icon=findViewById(R.id.iv_ope_icon);
        if (!provider_image.equals(""))
        {
            Glide.with(GooglePlayCoupon.this).load(provider_image).error(Glide.with(iv_ope_icon).load(R.drawable.photo)).into(iv_ope_icon);
        }
        else
        {
            iv_ope_icon.setBackground(getResources().getDrawable(R.drawable.logo));
        }

        tv_operator=findViewById(R.id.tv_operator);
        tv_operator.setText(provider_name);


        tv_number_error=findViewById(R.id.tv_number_error);
        tv_operator_error=findViewById(R.id.tv_operator_error);
        tv_amount_error=findViewById(R.id.tv_amount_error);

        ll_bill_detail=findViewById(R.id.ll_bill_detail);

        bt_proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {

                    tv_location.setVisibility(View.GONE);
                    if (DetectConnection.checkInternetConnection(GooglePlayCoupon.this)) {
                        if (ed_number.getText().toString().equals("")) {
                            tv_number_error.setVisibility(View.VISIBLE);
                            tv_number_error.setText("Please enter Mobile Number");
                        } else if (provider_id.equals("")) {
                            tv_operator_error.setText("Please select operator");
                            tv_operator_error.setVisibility(View.VISIBLE);
                        } else if (ed_amount.getText().toString().equals("")) {
                            tv_amount_error.setVisibility(View.VISIBLE);
                            tv_amount_error.setText("Please enter amount");
                        } else {

                            tv_number_error.setVisibility(View.GONE);
                            tv_operator_error.setVisibility(View.GONE);
                            tv_amount_error.setVisibility(View.GONE);

                            mShowConfirmDialog(ed_number.getText().toString());

                        }
                    } else {
                        Toast.makeText(GooglePlayCoupon.this, "No internet connection", Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    tv_location.setVisibility(View.VISIBLE);
                    tv_location.setText("Pleas enable location");
                    tv_location.setTextColor(getResources().getColor(R.color.red));
                    Toast.makeText(GooglePlayCoupon.this, "Please enable location", Toast.LENGTH_SHORT).show();
                }
            }
        });

        tv_provider=findViewById(R.id.tv_provider);

//        tv_number1.setText("Customer Id");
//        ed_number.setHint("Customer Id");

        ll_dthinfo=findViewById(R.id.ll_dthinfo);
        textview_mobile_number=findViewById(R.id.textview_mobile_number);
        textview_monthly=findViewById(R.id.textview_monthly);
        textview_balance=findViewById(R.id.textview_balance);
        textview_customer_name=findViewById(R.id.textview_customer_name);

//        ed_amount.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable editable) {
//                number=ed_number.getText().toString();
//                if (!once_verify) {
//                    mGetDTHPlanInfo(number);
//                }
//            }
//        });



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
            if (getSupportFragmentManager().getBackStackEntryCount()!=0)
            {
                getSupportFragmentManager().popBackStackImmediate();
            }
            else {
                finish();
            }
        }
        return super.onOptionsItemSelected(item);
    }


    @SuppressLint("StaticFieldLeak")
    protected void mProceedPayment(String transaction_pin)
    {
        Uri.Builder builder=new Uri.Builder();
        builder.appendQueryParameter("api_token",SharePrefManager.getInstance(GooglePlayCoupon.this).mGetApiToken());

        number=ed_number.getText().toString();
        builder.appendQueryParameter("mobile_number", number);

        amount=ed_amount.getText().toString();
        builder.appendQueryParameter("amount",amount);
        builder.appendQueryParameter("provider_id",provider_id);
        builder.appendQueryParameter("transaction_pin",transaction_pin);


        builder.appendQueryParameter("latitude",lat+"");
        builder.appendQueryParameter("longitude",log+"");
        builder.appendQueryParameter("dupplicate_transaction",System.currentTimeMillis()+"");

        String sending_url= BaseURL.BASEURL_B2C+"api/vouchers/pay-now";

        new CallResAPIPOSTMethod(GooglePlayCoupon.this,builder,sending_url,true,"POST")
        {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                dialog=new ProgressDialog(GooglePlayCoupon.this);
                dialog.setMessage("Please wait....");
                dialog.setCancelable(false);
                dialog.show();
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                dialog.dismiss();
                Log.e("response","data recharge "+s);

                try {
                    String status="",message="",payid="";
                    JSONObject jsonObject=new JSONObject(s);

                    if (jsonObject.has("status"))
                    {
                        status=jsonObject.getString("status");
                    }

                    if (jsonObject.has("message"))
                    {
                        message=jsonObject.getString("message");
                    }

                    if (jsonObject.has("payid"))
                    {
                        payid=jsonObject.getString("payid");
                    }

                    if (jsonObject.has("transaction_details"))
                    {
                        JSONObject transaction_details=jsonObject.getJSONObject("transaction_details");

                        if (transaction_details.has("status"))
                        {
                            status=transaction_details.getString("status");
                        }

                        if (transaction_details.has("operator_ref"))
                        {
                            message=transaction_details.getString("operator_ref");
                        }

                        if (transaction_details.has("operator_ref"))
                        {
                            payid=transaction_details.getString("operator_ref");
                        }
                    }

                    if (jsonObject.has("errors"))
                    {
                        JSONObject errors=jsonObject.getJSONObject("errors");

                        if (errors.has("mobile_number"))
                        {
                            String err=errors.getString("mobile_number").replaceAll("\\[","").replaceAll("]","").replaceAll("\"","");
                            tv_number_error.setText(err);
                            tv_number_error.setVisibility(View.VISIBLE);
                        }

                        if (errors.has("amount"))
                        {
                            String err=errors.getString("amount").replaceAll("\\[","").replaceAll("]","").replaceAll("\"","");
                            tv_amount_error.setVisibility(View.VISIBLE);
                            tv_amount_error.setText(err);
                        }

                        if (errors.has("provider_id"))
                        {
                            String err=errors.getString("provider_id").replaceAll("\\[","").replaceAll("]","").replaceAll("\"","");
                            Toast.makeText(GooglePlayCoupon.this, err, Toast.LENGTH_SHORT).show();
                        }

                        if (errors.has("transaction_pin"))
                        {
                            String err=errors.getString("transaction_pin").replaceAll("\\[","").replaceAll("]","").replaceAll("\"","");
                            Toast.makeText(getApplicationContext(), err, Toast.LENGTH_LONG).show();
                        }
                    }
                    else {

                        if (alertDialog != null) {
                            if (alertDialog.isShowing()) {
                                alertDialog.dismiss();
                            }
                        }

                        Intent intent = new Intent(GooglePlayCoupon.this, Receipt.class);
                        intent.putExtra("status", status);
                        intent.putExtra("message", message);
                        intent.putExtra("number", number);
                        intent.putExtra("amount", amount);
                        intent.putExtra("payid", payid);
                        intent.putExtra("type", "recharge");
                        intent.putExtra("provider", provider_name);
                        startActivity(intent);

                        if (Operator.mOperatorActivity != null) {
                            Operator.mOperatorActivity.finish();
                        }

                        finish();
                    }
                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }
            }
        }.execute();
    }


    protected void mShowDTHDetail(String result)
    {
        try{

            String message="";
            final JSONObject jsonObject=new JSONObject(result);

            if (jsonObject.has("message")
            ) {

                message=jsonObject.getString("message");
            }

            if (jsonObject.getString("status").equalsIgnoreCase("success"))

            {
                plan_price=jsonObject.getString("MonthlyRecharge");
                if (jsonObject.has("customerName")){
                    ll_dthinfo.setVisibility(View.VISIBLE);
                    String customername=jsonObject.getString("customerName");
                    textview_customer_name.setText(customername);
                }

                if (jsonObject.has("mobile_number")){

                    String tel=jsonObject.getString("mobile_number");
                    textview_mobile_number.setText(tel);
                }

                if (jsonObject.has("MonthlyRecharge")){

                    String monthlyrecharge=jsonObject.getString("MonthlyRecharge");
                    textview_monthly.setText(monthlyrecharge);
                }

                if (jsonObject.has("Balance")){

                    String balance=jsonObject.getString("Balance");
                    textview_balance.setText(balance);
                }
//
//
//                        final AlertDialog alertDialog;
//                        LayoutInflater inflater2 =(LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//                        View v2=inflater2.inflate(R.layout.custome_dialog_show_dth_verify,null);
//                        TextView textview_name=v2.findViewById(R.id.textview_name);
//                        TextView textview_operator=v2.findViewById(R.id.textview_operator);
//                        TextView textview_number=v2.findViewById(R.id.textview_number);
//                        TextView textview_planname=v2.findViewById(R.id.textview_planname);
//                        TextView textview_monthely_recharge=v2.findViewById(R.id.textview_monthely_recharge);
//                        TextView textview_NextRechargeDate=v2.findViewById(R.id.textview_NextRechargeDate);
//                        Button button_ok=(Button) v2.findViewById(R.id.button_ok);
//
//                        textview_name.setText("User Name : "+jsonObject.getString("customerName"));
//                        textview_operator.setText("Operator : "+jsonObject.getString("operator"));
//                        textview_number.setText("Number : "+jsonObject.getString("tel"));
//                        textview_planname.setText("Plan Name : "+jsonObject.getString("planname"));
//                        textview_monthely_recharge.setText("Monthly Recharge : "+jsonObject.getString("MonthlyRecharge"));
//                        textview_NextRechargeDate.setText("Next Recharge Date : "+jsonObject.getString("NextRechargeDate"));
//
//                        final AlertDialog.Builder builder2=new AlertDialog.Builder(Dth.this);
//                        builder2.setCancelable(false);
//                        builder2.setView(v2);
//                        alertDialog=builder2.create();
//
//                        button_ok.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View view) {
//                                alertDialog.dismiss();
//                                if (!plan_price.equalsIgnoreCase("null")) {
//                                    edittext_amount.setText(plan_price);
//                                }
//                            }
//                        });
//
//                        alertDialog.show();
            }

            else
            {
                Toast.makeText(GooglePlayCoupon.this, "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        }

        catch (JSONException e)
        {
            e.printStackTrace();
        }

    }

    protected void mShowConfirmDialog(String number)
    {

        LayoutInflater inflater2 =(LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v2=inflater2.inflate(R.layout.custome_dialog_confirm_recharge,null);
        TextView textview_servicename=v2.findViewById(R.id.textview_servicename);
        TextView textview_oper=v2.findViewById(R.id.textview_operator);
        TextView textview_mobnumber=v2.findViewById(R.id.textview_mobnumber);
        TextView textview_amount=v2.findViewById(R.id.textview_amount);
        Button button_submit=(Button) v2.findViewById(R.id.button_confirm);
        Button button_cancel=(Button) v2.findViewById(R.id.button_cancel);

        EditText tv_transaction_pin= v2.findViewById(R.id.tv_transaction_pin);
        if (SharePrefManager.getInstance(GooglePlayCoupon.this).mGetSingleData("transaction_pin").equals("1"))
        {
            tv_transaction_pin.setVisibility(View.VISIBLE);
            tv_transaction_pin.setHint("Enter Transaction PIN");
        }

        textview_servicename.setText("Google Play Vouchers");
        textview_oper.setText(provider_name);
        textview_mobnumber.setText(number);
        textview_amount.setText("Rs "+ed_amount.getText().toString());
        final AlertDialog.Builder builder2=new AlertDialog.Builder(GooglePlayCoupon.this);
        builder2.setCancelable(false);
        builder2.setView(v2);
        alertDialog=builder2.create();

        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        button_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (DetectConnection.checkInternetConnection(GooglePlayCoupon.this)){

                    if (tv_transaction_pin.getVisibility()==View.VISIBLE&&tv_transaction_pin.getText().toString().equals(""))
                    {
                        Toast.makeText(GooglePlayCoupon.this, "Please enter transaction PIN", Toast.LENGTH_SHORT).show();
                    }
                    else {

                        alertDialog.dismiss();
                        mProceedPayment(tv_transaction_pin.getText().toString());

                    }

                }
                else
                {
                    Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
                }
            }
        });

        button_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });


        alertDialog.show();
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

        mSettingsClient = LocationServices.getSettingsClient(GooglePlayCoupon.this);

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
                                    rae.startResolutionForResult(GooglePlayCoupon.this, REQUEST_CHECK_SETTINGS);
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
}