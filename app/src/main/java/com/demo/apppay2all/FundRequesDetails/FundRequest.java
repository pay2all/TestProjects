package com.demo.apppay2all.FundRequesDetails;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.demo.apppay2all.BaseURL.BaseURL;
import com.demo.apppay2all.CallResAPIPOSTMethod;
import com.demo.apppay2all.DetectConnection;
import com.demo.apppay2all.LocationDetails.MyLocation;
import com.demo.apppay2all.NumberToWord;
import com.demo.apppay2all.PanCard;
import com.demo.apppay2all.R;
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
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class FundRequest extends AppCompatActivity implements LocationListener {

    EditText editText_fundrequest_amount,edittext_fundrequest_bankreferencenumber;
    Button button_fundrequest_proceed_to_pay;
    String username,password,method_id="";
    PopupMenu popupMenu_method;
    String amount,bank_reference_number;

    String myJSON,bankdetail="";
    String string_payment_mode;
    String mode_of_payment_code="";

    ArrayAdapter arrayAdapter;

    List<String> mode_of_payment;
    List<String> mode_code;

    ProgressDialog dialog;
    String paybank;
    RelativeLayout rl_deposit_date;
    TextView textview_deposit_date;

    private int mYear, mMonth, mDay;
    TextView textView_note;

    RecyclerView recyclerview_banks;
    BankDetailCardAdapter bankDetailCardAdapter;
    List<BankDetailItem>bankDetailItem;

    RelativeLayout rl_select_bank,rl_select_method;
    TextView textview_bankname,textview_method;
    PopupMenu popupMenu;
    String bankid="";

    TextInputLayout textinputlayout_reference;

    TextView tv_amount_error,tv_select_bank_error,tv_select_method_error,tv_reference,tv_date_error;




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
        setContentView(R.layout.activity_fund_request);

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

                if (ActivityCompat.checkSelfPermission(FundRequest.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(FundRequest.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(FundRequest.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
                }
                else{
                    mShowEnabledLocationDialog();
                }
            }
        });

        editText_fundrequest_amount= findViewById(R.id.edittext_fundrequest_amount);
        TextView tv_amount_word=findViewById(R.id.tv_amount_word);
        editText_fundrequest_amount.addTextChangedListener(new TextWatcher() {
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

        edittext_fundrequest_bankreferencenumber= findViewById(R.id.edittext_fundrequest_bankreferencenumber);
        button_fundrequest_proceed_to_pay= findViewById(R.id.button_fundrequest_proceed_to_pay);

        textinputlayout_reference=findViewById(R.id.textinputlayout_reference);

        tv_amount_error=findViewById(R.id.tv_amount_error);
        tv_select_bank_error=findViewById(R.id.tv_select_bank_error);
        tv_select_method_error=findViewById(R.id.tv_select_method_error);
        tv_reference=findViewById(R.id.tv_reference);
        tv_date_error=findViewById(R.id.tv_date_error);

        rl_select_method=findViewById(R.id.rl_select_method);
        popupMenu_method=new PopupMenu(FundRequest.this,rl_select_method);
        rl_select_method.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (popupMenu_method!=null){

                    popupMenu_method.show();
                }

                popupMenu_method.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        method_id= menuItem.getGroupId()+"";
                        textview_method.setText(menuItem.getTitle().toString());
                        return true;
                    }
                });
            }
        });
        textview_method=findViewById(R.id.textview_method);

        rl_deposit_date=findViewById(R.id.rl_deposit_date);
        textview_deposit_date=findViewById(R.id.textview_deposit_date);
        rl_deposit_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);


                DatePickerDialog datePickerDialog = new DatePickerDialog(FundRequest.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                String fromdate= year+ "-" + (monthOfYear + 1) + "-" + dayOfMonth;
                                textview_deposit_date.setText(fromdate);
                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
                datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
            }
        });


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        rl_select_bank=findViewById(R.id.rl_select_bank);
        textview_bankname=findViewById(R.id.textview_bankname);

        popupMenu=new PopupMenu(FundRequest.this,rl_select_bank);
        rl_select_bank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (popupMenu!=null)
                {
                    popupMenu.show();
                }

                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        textview_bankname.setText(menuItem.getTitle());
                        bankid=menuItem.getGroupId()+"";
                        return true;
                    }
                });

            }
        });

        textView_note= findViewById(R.id.textView_note);
//        String note="Note :";
//        String note_text="<b>"+"Note :"+"</b>"+" For adding fund in your wallet, make your payment in company's account but Bank Transfer, UPI Transfer or Cash Deposit and then submit fund request. \n";
//        textView_note.setText(Html.fromHtml(note_text));


        setTitleColor(Color.WHITE);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        SharedPreferences sharedPreferences=getSharedPreferences("user", Context.MODE_PRIVATE);
        username=sharedPreferences.getString("username","");
        password=sharedPreferences.getString("password","");



        mode_of_payment=new ArrayList<>();
        mode_code=new ArrayList<>();

        mode_of_payment.add("Select Payment Mode");
        mode_code.add("");

        arrayAdapter=new ArrayAdapter(this,android.R.layout.simple_spinner_dropdown_item,mode_of_payment);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        button_fundrequest_proceed_to_pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    tv_location.setVisibility(View.GONE);
                if (DetectConnection.checkInternetConnection(FundRequest.this))
                {
                    if (editText_fundrequest_amount.getText().toString().equals(""))
                    {
                        tv_amount_error.setVisibility(View.VISIBLE);
                        tv_amount_error.setText("Please enter amount");
                    }
                   else if (bankid.equals(""))
                    {
                        tv_select_bank_error.setText("Please select bank name");
                        tv_select_bank_error.setVisibility(View.VISIBLE);
                    }
                    else if (method_id.equals(""))
                    {
                        tv_select_method_error.setText("Please select payment method");
                        tv_select_method_error.setVisibility(View.VISIBLE);
                    }
                    else if (edittext_fundrequest_bankreferencenumber.getText().toString().equals(""))
                    {
                        tv_reference.setText("Please reference number");
                        tv_reference.setVisibility(View.VISIBLE);
                    }
                    else if (textview_deposit_date.getText().toString().equalsIgnoreCase("yyyy-mm-dd"))
                    {
                        tv_date_error.setVisibility(View.VISIBLE);
                        tv_date_error.setText("Please select payment date");
                    }
                    else {

                        tv_amount_error.setVisibility(View.GONE);
                        tv_select_bank_error.setVisibility(View.GONE);
                        tv_select_method_error.setVisibility(View.GONE);

                        tv_reference.setVisibility(View.GONE);
                        tv_date_error.setVisibility(View.GONE);

                         amount=editText_fundrequest_amount.getText().toString();
                         bank_reference_number=edittext_fundrequest_bankreferencenumber.getText().toString();
                         String payment_date=textview_deposit_date.getText().toString();
                         Fund_Request(amount,bank_reference_number,method_id,payment_date,bankid);
                    }
                }
                else
                {
                    Toast.makeText(FundRequest.this, "No Connection", Toast.LENGTH_SHORT).show();
                }
                }
                else {
                    tv_location.setVisibility(View.VISIBLE);
                    tv_location.setText("Please Enable Location");
                    tv_location.setTextColor(getResources().getColor(R.color.red));
                    Toast.makeText(FundRequest.this, "Please enable location", Toast.LENGTH_SHORT).show();
                }

            }
        });


        recyclerview_banks=(RecyclerView)findViewById(R.id.recyclerview_banks);
        recyclerview_banks.setHasFixedSize(true);
        recyclerview_banks.setItemViewCacheSize(20);
        recyclerview_banks.setDrawingCacheEnabled(true);
        recyclerview_banks.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        recyclerview_banks.setLayoutManager(new LinearLayoutManager(FundRequest.this));
        bankDetailItem=new ArrayList<>();
        bankDetailCardAdapter=new BankDetailCardAdapter(FundRequest.this,bankDetailItem);
        recyclerview_banks.setAdapter(bankDetailCardAdapter);
        if (DetectConnection.checkInternetConnection(FundRequest.this))
        {
            mGetMethod();
//            getData(BaseURL.BASEURL_B2C+"api/fund-request/bank-list?api_token=EOmp8MyuCGyHC5avFkYqBXUqp9nIg1R0EkKtHxM6olO8C6VBBdT4lD2ijB5G");
            getData(BaseURL.BASEURL_B2C+"api/fund-request/bank-list?api_token="+SharePrefManager.getInstance(FundRequest.this).mGetApiToken());
        }

        else
        {
            Toast.makeText(this, "No Internet Connection", Toast.LENGTH_SHORT).show();
        }
    }

    @SuppressLint("StaticFieldLeak")
    protected void Fund_Request( final String amount, final String bank_reference_number, final String mode_of_payment, final String payment_date, final String paybank)
    {
        String sending_url=BaseURL.BASEURL_B2C+"api/fund-request/payment-request-now";
        Uri.Builder builder=new Uri.Builder();
        builder.appendQueryParameter("api_token",SharePrefManager.getInstance(FundRequest.this).mGetApiToken());
        builder.appendQueryParameter("bankdetail_id",paybank);
        builder.appendQueryParameter("paymentmethod_id",mode_of_payment);
        builder.appendQueryParameter("payment_date",payment_date);
        builder.appendQueryParameter("amount",amount);
        builder.appendQueryParameter("bankref",bank_reference_number);


        builder.appendQueryParameter("latitude",lat+"");
        builder.appendQueryParameter("longitude",log+"");
        builder.appendQueryParameter("dupplicate_transaction",System.currentTimeMillis()+"");

        new CallResAPIPOSTMethod(FundRequest.this,builder,sending_url,true,"POST")
        {
            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                dialog.dismiss();

                Log.e("request ","data "+s);
                myJSON=s;
                Show();
            }

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                dialog=new ProgressDialog(FundRequest.this);
                dialog.setMessage("Please wait...");
                dialog.setCancelable(false);
                dialog.show();
            }
        }.execute();

//        class Request extends AsyncTask<String, String, String>
//        {
//            HttpURLConnection urlConnection;
//            @Override
//            protected void onPreExecute() {
//                super.onPreExecute();
//                dialog=new ProgressDialog(FundRequest.this);
//                dialog.setMessage("Please wait");
//                dialog.show();
//                dialog.show();
//            }
//
//            @Override
//            protected void onPostExecute(String s) {
//                super.onPostExecute(s);
//                dialog.dismiss();
//                myJSON=s;
//                Show();
//                Log.e("fnd rqst rspns",s);
//            }
//
//            @Override
//            protected String doInBackground(String... params) {
//                StringBuilder result=new StringBuilder();
//                try {
//                    URL url = new URL(BaseURL.BASEURL+"application/v1/fund-request-now?username="+username+"&password="+password+"&bank_id="+bankid+"&method_id="+mode_of_payment+"&amount="+amount+"&payment_date="+payment_date+"&bankref="+bank_reference_number);
//
//                    urlConnection = (HttpURLConnection) url.openConnection();
//                    InputStream in = new BufferedInputStream(urlConnection.getInputStream());
//
//                    BufferedReader reader = new BufferedReader(new InputStreamReader(in));
//
//                    String line;
//                    while ((line = reader.readLine()) != null) {
//                        result.append(line);
//                    }
//
//                } catch (Exception e) {
//                    e.printStackTrace();
//                } finally {
//                    urlConnection.disconnect();
//                }
//
//                return result.toString();
//            }
//        }

//        Request request=new Request();
//        request.execute();
    }
    protected void Show()
    {
        String status="";
        String message="";
        try
        {
            JSONObject jsonObject=new JSONObject(myJSON);
            status=jsonObject.getString("status");

            if (jsonObject.has("message"))
            {
                message=jsonObject.getString("message");
            }

            if (jsonObject.has("errors"))
            {
                JSONObject errors=jsonObject.getJSONObject("errors");
                if (errors.has("bankref"))
                {
                    message=errors.getString("bankref");
                    message=message.replaceAll("\\[","").replaceAll("]","").replaceAll("\"","");
                    tv_reference.setVisibility(View.VISIBLE);
                    tv_reference.setText(message);
                    Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
                }
            }
            else
            {
                mShowRecipt(status,message);
            }
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }

//        Intent intent=new Intent(FundRequest.this, Review_Activity.class);
//        Bundle bundle=new Bundle();
//        bundle.putString("status",status);
//        bundle.putString("message",message);
//        bundle.putString("activity","fundrequest");
//        intent.putExtras(bundle);
//        startActivity(intent);
//        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.action_report:
                Intent intent=new Intent(FundRequest.this,Payment_request_reports.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private  void  getData(final String sendingurl) {
        class getJSONData extends AsyncTask<String, String, String> {

            HttpURLConnection urlConnection;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                dialog=new ProgressDialog(FundRequest.this);
                dialog.setMessage("Please wait...");
                dialog.show();
                dialog.setCancelable(false);
            }
            @Override
            protected String doInBackground(String... args) {

                StringBuilder result = new StringBuilder();

                try {
                    URL url = new URL(sendingurl);
                    urlConnection = (HttpURLConnection) url.openConnection();
                    InputStream in = new BufferedInputStream(urlConnection.getInputStream());

                    BufferedReader reader = new BufferedReader(new InputStreamReader(in));

                    String line;
                    while ((line = reader.readLine()) != null) {
                        result.append(line);
                    }

                }
                catch (Exception e) {
                    e.printStackTrace();
                }
                finally {
                    urlConnection.disconnect();
                }
                return result.toString();
            }

            @Override
            protected void onPostExecute(String result) {

                //Do something with the JSON string

                if (dialog!=null) {
                    if (dialog.isShowing()) {
                        dialog.dismiss();
                    }
                }

                Log.e("response","data "+result);
                bankdetail=result;
                mShowbanklist(result);
                if (!result.equals("")){

                    String status="";

                    try{

                        JSONObject jsonObject=new JSONObject(result);

                        if (jsonObject.has("status")){
                            status=jsonObject.getString("status");
                        }

                        if (status.equals("success")){

                            if (jsonObject.has("banks")){
                                JSONArray jsonArray=jsonObject.getJSONArray("banks");
                                for (int i=0;i<jsonArray.length();i++){
                                    JSONObject data=jsonArray.getJSONObject(i);
                                    popupMenu.getMenu().add(data.getInt("bankdetail_id"),0,0,data.getString("bank_name"));
                                }

                            }
                        }
                    }

                    catch (JSONException e){
                        e.printStackTrace();
                    }

                }


            }

        }

        getJSONData getJSONData=new getJSONData();
        getJSONData.execute();

    }

protected void mShowbanklist(String bankdetail){

    if (!bankdetail.equals("")){

        String status="";

        try{

            JSONObject jsonObject=new JSONObject(bankdetail);

            if (jsonObject.has("status")){
                status=jsonObject.getString("status");
            }

            if (status.equals("success")){

                if (jsonObject.has("banks")){
                    JSONArray jsonArray=jsonObject.getJSONArray("banks");
                    for (int i=0;i<jsonArray.length();i++){
                        JSONObject data=jsonArray.getJSONObject(i);
                        BankDetailItem item=new BankDetailItem();
                        item.setBank_account_name("");
                        item.setBank_account_number(data.getString("account_number"));
                        item.setBank_name(data.getString("bank_name"));
                        item.setBank_ifsc(data.getString("ifsc_code"));
                        item.setBank_branch(data.getString("branch"));
                        bankDetailItem.add(item);
                        bankDetailCardAdapter.notifyDataSetChanged();


                    }

                }
            }
        }

        catch (JSONException e){
            e.printStackTrace();
        }

    }
}
    private  void  mGetMethod() {
        class getJSONData extends AsyncTask<String, String, String> {


            HttpURLConnection urlConnection;


            @Override
            protected void onPreExecute() {
                super.onPreExecute();

            }
            @Override
            protected String doInBackground(String... args) {

                StringBuilder result = new StringBuilder();

                try {
//                    URL url = new URL(BaseURL.BASEURL_B2C+"api/fund-request/payment-method?api_token=EOmp8MyuCGyHC5avFkYqBXUqp9nIg1R0EkKtHxM6olO8C6VBBdT4lD2ijB5G");
                    URL url = new URL(BaseURL.BASEURL_B2C+"api/fund-request/payment-method?api_token="+SharePrefManager.getInstance(FundRequest.this).mGetApiToken());
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
                Log.e("payment","mode "+result);
                if (!result.equals("")){
                    String status="";
                    try{

                        JSONObject jsonObject=new JSONObject(result);
                        if (jsonObject.has("status")){
                            status=jsonObject.getString("status");
                        }
                        if (status.equalsIgnoreCase("success")){
                            JSONArray jsonArray=jsonObject.getJSONArray("methods");
                            for (int i=0;i<jsonArray.length();i++){
                                JSONObject data=jsonArray.getJSONObject(i);
                                popupMenu_method.getMenu().add(data.getInt("paymentmethod_id"),0,0,data.getString("payment_type"));
                            }
                        }
                    }


                    catch (JSONException e){
                        e.printStackTrace();
                    }
                }

            }

        }

        getJSONData getJSONData=new getJSONData();
        getJSONData.execute();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.payment_report_menu, menu);

        return true;
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
        textview_mobile_title.setText("Reference No : ");

        textview_requestsendsuccessfull.setText(message);
        textview_oper.setText("Fund Request");
        textview_mobnumber.setText(bank_reference_number);
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

        final AlertDialog.Builder builder2=new AlertDialog.Builder(FundRequest.this);
        builder2.setCancelable(false);
        builder2.setView(v2);
        alertDialog=builder2.create();

        button_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
                if (status.equalsIgnoreCase("success")){
                    finish();
                }
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

        mSettingsClient = LocationServices.getSettingsClient(FundRequest.this);

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
                                    rae.startResolutionForResult(FundRequest.this, REQUEST_CHECK_SETTINGS);
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