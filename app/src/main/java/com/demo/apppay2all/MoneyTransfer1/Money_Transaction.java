package com.demo.apppay2all.MoneyTransfer1;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
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
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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
import com.demo.apppay2all.RechargesServicesDetail.PrepaidMobile;
import com.demo.apppay2all.Review_Activity;
import com.demo.apppay2all.SharePrefManager;
import com.demo.apppay2all.TransactionRecept.Receipt;
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


public class Money_Transaction extends AppCompatActivity implements LocationListener {

    TextView textView_ifsc,textView_send_money_beneficiary_name,textView_send_money_account_number,textView_send_money_bank_name;
    String amount;

//    Spinner spinner_send_money_mode;

    EditText editText_send_money_amount;
    Button button_send_money_send_now;

    RadioGroup radioGroup;
    RadioButton radiobutton_imps,radiobutton_neft;

    StringBuilder transfer_mode;
    private RadioButton radioButton;

    ProgressDialog dialog;

    String myJSON;

    String username,password,balance,beneciary_id,senderid,sender_number,recepient_id,ifsc,account_number,channel;

    Beneficiary_ItemsPaytm beneficiary_items;

    String bank_name,receipint_name;

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
        setContentView(R.layout.activity_transaction);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        tv_location=findViewById(R.id.tv_location);
        locationManager=(LocationManager) getSystemService(Context.LOCATION_SERVICE);
        tv_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (ActivityCompat.checkSelfPermission(Money_Transaction.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(Money_Transaction.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(Money_Transaction.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
                }
                else{
                    mShowEnabledLocationDialog();
                }
            }
        });

        textView_send_money_beneficiary_name= findViewById(R.id.textView_send_money_beneficiary_name);
        textView_send_money_account_number= findViewById(R.id.textView_send_money_account_number);
        textView_send_money_bank_name= findViewById(R.id.textView_send_money_bank_name);

        editText_send_money_amount= findViewById(R.id.editText_send_money_amount);
        TextView tv_amount_word=findViewById(R.id.tv_amount_word);
        editText_send_money_amount.addTextChangedListener(new TextWatcher() {
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

        textView_ifsc= findViewById(R.id.textView_ifsc);
        radioGroup= findViewById(R.id.radioGroup);
        radiobutton_imps= findViewById(R.id.radiobutton_imps);
        radiobutton_neft= findViewById(R.id.radiobutton_neft);

        transfer_mode=new StringBuilder();
//        to get data from recyclerview using make serializable beneficiary_items class
         beneficiary_items=(Beneficiary_ItemsPaytm)getIntent().getExtras().getSerializable("DATA");
        textView_send_money_beneficiary_name.setText(beneficiary_items.getBeneficiry_name());
        textView_send_money_bank_name.setText(beneficiary_items.getBank());
        textView_ifsc.setText("IFSC "+beneficiary_items.getIfsc());
        textView_send_money_account_number.setText("A/c "+beneficiary_items.getAccountno());

        getSupportActionBar().setTitle("Sender ("+beneficiary_items.getSender_number()+")");

        beneciary_id=beneficiary_items.getRecepient_id();
        senderid=getIntent().getStringExtra("senderid");
        sender_number=getIntent().getStringExtra("sender_number");
        bank_name=beneficiary_items.getBank().replaceAll(" ","%20");
        receipint_name=beneficiary_items.getBeneficiry_name().replaceAll(" ","%20");

//        to set checked IMPS by default
        radiobutton_imps.setChecked(true);

//        to get username from sharedpreference
        SharedPreferences sharedPreferences=getSharedPreferences("user", Context.MODE_PRIVATE);
        username=sharedPreferences.getString("username","");
        password=sharedPreferences.getString("password","");
        balance=sharedPreferences.getString("balance","");

        button_send_money_send_now= findViewById(R.id.button_send_money_send_now);

        button_send_money_send_now.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {

                    tv_location.setVisibility(View.GONE);

                    if (DetectConnection.checkInternetConnection(Money_Transaction.this)) {

                        int selectedId = radioGroup.getCheckedRadioButtonId();

                        if (editText_send_money_amount.getText().toString().equals("")) {
                            Toast.makeText(Money_Transaction.this, "Enter amount", Toast.LENGTH_SHORT).show();
                        } else {
                            ifsc = beneficiary_items.getIfsc();
                            sender_number = beneficiary_items.getSender_number();
                            recepient_id = beneficiary_items.getRecepient_id();
                            account_number = beneficiary_items.getAccountno();

                            radioButton = findViewById(selectedId);

                            if (radioButton.getText().toString().equals("NEFT")) {
                                channel = "1";
                            } else {
                                channel = "2";
                            }
                            amount = editText_send_money_amount.getText().toString();


                            mShowDialog();
//                        mShowDialog("You want to Transfer Rs "+amount+" To Account number "+beneficiary_items.getAccountno()+" of "+beneficiary_items.getBank());
                        }
                    } else {
                        Toast.makeText(Money_Transaction.this, "No Connection", Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    tv_location.setVisibility(View.VISIBLE);
                    tv_location.setText("Please enable location");
                    tv_location.setTextColor(getResources().getColor(R.color.red));
                    Toast.makeText(Money_Transaction.this, "Please enable location", Toast.LENGTH_SHORT).show();
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


    protected void SendMoney(final String username, final String password, final String ifsc, final String beneficiaryid,
                             final String amount, final String senderid, final String account,
                             final String sender_number, final String channel, final String bank_name, final String receipnt_name)
    {
        class Send extends AsyncTask<String, String, String>
        {
            HttpURLConnection httpURLConnection;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                dialog=new ProgressDialog(Money_Transaction.this);
                dialog.setMessage("Please Wait and don't press the back or Refresh...");
                dialog.show();
                dialog.setCancelable(false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                myJSON=s;
                Log.e("transaction response",s);
                ShowTransferMessage(s);
                dialog.dismiss();
            }

            @Override
            protected String doInBackground(String... params) {
                StringBuilder result = new StringBuilder();
                try {
                    URL url = new URL(BaseURL.BASEURL+"app/v2/transfer-now?username="+username+
                            "&password="+password+"&mobile_number="+sender_number+"&recipient_id="+
                            beneficiaryid+"&recipient_bank="+bank_name+"&recipient_mobile="+sender_number+
                            "&recipient_name="+receipnt_name+"&recipient_ifsc="+ifsc+"&recipient_account="+account+
                            "&channel="+channel+"&amount="+amount);
                    httpURLConnection = (HttpURLConnection) url.openConnection();
                    InputStream in = new BufferedInputStream(httpURLConnection.getInputStream());

                    BufferedReader reader = new BufferedReader(new InputStreamReader(in));

                    String line;
                    while ((line = reader.readLine()) != null) {
                        result.append(line);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    httpURLConnection.disconnect();
                }

                return result.toString();
            }
        }

        Send send=new Send();
        send.execute();
    }

    protected void ShowTransferMessage(String data)
    {
        String status="";
        String message="";
        try
        {
            JSONObject jsonObject=new JSONObject(data);
            if (jsonObject.has("status")) {
                status = jsonObject.getString("status");
            }

            if (jsonObject.has("message")) {
                message = jsonObject.getString("message");
            }


            if (jsonObject.has("errors"))
            {
                JSONObject errors=jsonObject.getJSONObject("errors");

                if (errors.has("amount"))
                {
                    String err=errors.getString("amount").replaceAll("\\[","").replaceAll("]","").replaceAll("\"","");
                    Toast.makeText(this, err, Toast.LENGTH_SHORT).show();
                }

                if (errors.has("transaction_pin"))
                {
                    String err=errors.getString("transaction_pin").replaceAll("\\[","").replaceAll("]","").replaceAll("\"","");
                    Toast.makeText(getApplicationContext(), err, Toast.LENGTH_LONG).show();
                }

            }
            else {
                if (!status.equals("")) {
                    if (status.equalsIgnoreCase("success")) {

                        if (alertDialog!=null)
                        {
                            if (alertDialog.isShowing())
                            {
                                alertDialog.dismiss();
                            }
                        }

                        Intent intent = new Intent(Money_Transaction.this, Receipt.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("status", status);
                        bundle.putString("message", message);
                        bundle.putString("data", data);
                        bundle.putString("activity", "transaction");
                        bundle.putString("amount", amount);
                        bundle.putString("name", beneficiary_items.getBeneficiry_name());
                        intent.putExtras(bundle);
                        startActivity(intent);
                        finish();
                    }
                    else if (status.equalsIgnoreCase("failure") || status.equalsIgnoreCase("fail")) {

                        if (alertDialog!=null)
                        {
                            if (alertDialog.isShowing())
                            {
                                alertDialog.dismiss();
                            }
                        }


                        Intent intent = new Intent(Money_Transaction.this, Review_Activity.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("status", status);
                        bundle.putString("data", data);
                        bundle.putString("activity", "transaction");
                        bundle.putString("amount", amount);
                        bundle.putString("message", message);
                        bundle.putString("name", beneficiary_items.getBeneficiry_name());
                        intent.putExtras(bundle);
                        startActivity(intent);
                        finish();
                    }
                    else {
                        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
                    }
                }
                else
                {
                    Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show();
                }
            }

        }
        catch (JSONException e)
        {
            e.printStackTrace();
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


    protected void mShowDialog()
    {

        LayoutInflater inflater2 =(LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v2=inflater2.inflate(R.layout.custome_dialog_confirm_dmt,null);
        TextView textview_oper=v2.findViewById(R.id.textview_operator);
        TextView textview_mobnumber=v2.findViewById(R.id.textview_mobnumber);
        TextView textview_amount=v2.findViewById(R.id.textview_amount);
        Button button_submit= v2.findViewById(R.id.button_confirm);
        Button button_cancel= v2.findViewById(R.id.button_cancel);

        textview_oper.setText(bank_name.replaceAll("%20"," "));
        textview_mobnumber.setText(account_number);
        textview_amount.setText("Rs "+amount);
        final AlertDialog.Builder builder2=new AlertDialog.Builder(Money_Transaction.this);
        builder2.setCancelable(false);
        builder2.setView(v2);
        alertDialog=builder2.create();

        EditText tv_transaction_pin= v2.findViewById(R.id.tv_transaction_pin);
        if (SharePrefManager.getInstance(Money_Transaction.this).mGetSingleData("transaction_pin").equals("1"))
        {
            tv_transaction_pin.setVisibility(View.VISIBLE);
            tv_transaction_pin.setHint("Enter Transaction PIN");
        }

        button_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (DetectConnection.checkInternetConnection(Money_Transaction.this)){

//                    SendMoney(username,password,ifsc,beneciary_id,amount,senderid,account_number,sender_number,channel,bank_name,receipint_name);

                    if (tv_transaction_pin.getVisibility()==View.VISIBLE&&tv_transaction_pin.getText().toString().equals(""))
                    {
                        Toast.makeText(Money_Transaction.this, "Please enter transaction PIN", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        String sending_url = BaseURL.BASEURL_B2C + "api/dmt/v1/transfer-now";
                        Uri.Builder builder = new Uri.Builder();
                        builder.appendQueryParameter("api_token", SharePrefManager.getInstance(Money_Transaction.this).mGetApiToken());
                        builder.appendQueryParameter("recipient_id", beneciary_id);
                        builder.appendQueryParameter("account_number", account_number);
                        builder.appendQueryParameter("ifsc_code", ifsc);
                        builder.appendQueryParameter("mode", channel);
                        builder.appendQueryParameter("mobile_number", sender_number);
                        builder.appendQueryParameter("amount", amount);
                        builder.appendQueryParameter("transaction_pin",tv_transaction_pin.getText().toString());


                        builder.appendQueryParameter("latitude",lat+"");
                        builder.appendQueryParameter("longitude",log+"");
                        builder.appendQueryParameter("dupplicate_transaction",System.currentTimeMillis()+"");

                        mCheckSender(builder, sending_url);


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

    @SuppressLint("StaticFieldLeak")
    protected void mCheckSender( Uri.Builder builder, String sending_url)
    {

        new CallResAPIPOSTMethod(Money_Transaction.this,builder,sending_url,true,"POST"){
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                dialog=new ProgressDialog(Money_Transaction.this);
                dialog.setMessage("Please wait...");
                dialog.setCancelable(false);
                dialog.show();
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                dialog.dismiss();
                Log.e("check sender","response "+s);
                ShowTransferMessage(s);
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

        mSettingsClient = LocationServices.getSettingsClient(Money_Transaction.this);

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
                                    rae.startResolutionForResult(Money_Transaction.this, REQUEST_CHECK_SETTINGS);
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
