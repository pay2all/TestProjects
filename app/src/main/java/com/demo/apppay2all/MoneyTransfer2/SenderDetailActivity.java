package com.demo.apppay2all.MoneyTransfer2;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.demo.apppay2all.BaseURL.BaseURL;
import com.demo.apppay2all.CallResAPIPOSTMethod;
import com.demo.apppay2all.DetectConnection;
import com.demo.apppay2all.R;
import com.demo.apppay2all.SharePrefManager;

import org.json.JSONException;
import org.json.JSONObject;

public class SenderDetailActivity extends AppCompatActivity {

    EditText editText_mobileno;

    LinearLayout ll_contain_fname_lname;
    LinearLayout ll_otp;
    LinearLayout ll_verification_pendding_message;
    LinearLayout ll_display_success_message;
    Button button_submit;

    //    button for cintinue
    Button button_continue;

    //    store registration status
    String regiteration_status;

    //    edittext for firstname and lastname
    EditText editText_fname, editText_lname;

    //    edittext field for otp
    EditText editText_otp;

    //    number to be check
    String number_tobecheck;

    ProgressDialog dialog;

    //    to json of check status number is register or not and store it in string
    String myJSON;

    //    to json of register number and store it in string
    String myJSON_register;

    //    to json of otp status of register a number store it in string
    String myJSON_otp;

    //    to store benificiary list json data
    String myJSON_benificary;

    TextView textView_otp_onusermobilenumber;

    LinearLayout ll_mobile_number,ll_pin_addres_state;

    EditText edittext_pincode,edittext_address,edittext_state;

    String activity="",balance="";

    TextView textview_balance,tv_resend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_money_transfer_paytm);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        tv_resend=findViewById(R.id.tv_resend);
        tv_resend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String sending_url=BaseURL.BASEURL_B2C+"api/dmt/v2/resend-otp";
                String firstname = editText_fname.getText().toString();
                String lastname = editText_lname.getText().toString();
                String pincode = edittext_pincode.getText().toString();
                String address = edittext_address.getText().toString();
                String state = edittext_state.getText().toString();

                Uri.Builder builder=new Uri.Builder();
                builder.appendQueryParameter("api_token", SharePrefManager.getInstance(SenderDetailActivity.this).mGetApiToken());
                builder.appendQueryParameter("mobile_number",number_tobecheck);
                builder.appendQueryParameter("first_name",firstname);
                builder.appendQueryParameter("last_name",lastname);
                builder.appendQueryParameter("pin_code",pincode);
                builder.appendQueryParameter("address",address);
                builder.appendQueryParameter("state",state);

                mCheckSender("register",builder,sending_url);

            }
        });

        ll_mobile_number = findViewById(R.id.ll_mobile_number);


        button_submit=findViewById(R.id.button_submit);
        button_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (DetectConnection.checkInternetConnection(SenderDetailActivity.this)){


                        if (editText_mobileno.getText().toString().equals("")){

                            Toast.makeText(SenderDetailActivity.this, "Please enter mobile number", Toast.LENGTH_SHORT).show();


                        }
                        else if (editText_mobileno.getText().toString().length()<10){

                            Toast.makeText(SenderDetailActivity.this, "Please enter a valid mobile number", Toast.LENGTH_SHORT).show();

                        }
                    else {

                            number_tobecheck= editText_mobileno.getText().toString();
//                            CheckRegistrationStatus(number_tobecheck);

                            String sending_url=BaseURL.BASEURL_B2C+"api/dmt/v2/get-customer";
                            Uri.Builder builder=new Uri.Builder();
                            builder.appendQueryParameter("api_token", SharePrefManager.getInstance(SenderDetailActivity.this).mGetApiToken());
                            builder.appendQueryParameter("mobile_number",number_tobecheck);

                            mCheckSender("check",builder,sending_url);

                        }
                }

                else {
                    Toast.makeText(SenderDetailActivity.this, "No Connection", Toast.LENGTH_SHORT).show();
                }
            }
        });

        SharedPreferences sharedPreferences=getSharedPreferences("user",0);
        balance=sharedPreferences.getString("balance","");
        textview_balance=findViewById(R.id.textview_balance);
        textview_balance.setText("Rs "+balance);


        activity=getIntent().getStringExtra("activity");

        ll_pin_addres_state = findViewById(R.id.ll_pin_addres_state);
        edittext_pincode = findViewById(R.id.edittext_pincode);
        edittext_address = findViewById(R.id.edittext_address);
        edittext_state = findViewById(R.id.edittext_state);

        textView_otp_onusermobilenumber = findViewById(R.id.textView_otp_onusermobile);
        ll_contain_fname_lname = findViewById(R.id.ll_contain_fname_lname);
        ll_otp = findViewById(R.id.ll_otp);
        ll_verification_pendding_message = findViewById(R.id.ll_pendding_verification_message);
        ll_display_success_message= findViewById(R.id.ll_display_success_message);

        editText_fname = findViewById(R.id.edittext_fname);
        editText_lname = findViewById(R.id.edittext_lname);

        editText_otp= findViewById(R.id.edittext_otp);

        button_continue = findViewById(R.id.button_continue);

        button_continue.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                if (regiteration_status.equalsIgnoreCase("Verification pending")) {
                    Toast.makeText(SenderDetailActivity.this, "Verification pending", Toast.LENGTH_SHORT).show();
                }
                else if (regiteration_status.equalsIgnoreCase("have to be enter otp"))
                {

                    if (DetectConnection.checkInternetConnection(SenderDetailActivity.this)) {
                        if (editText_otp.getText().toString().equals("")) {
                            Toast.makeText(SenderDetailActivity.this, "Enter OTP", Toast.LENGTH_SHORT).show();
                            textView_otp_onusermobilenumber.setText("Check your message inbox we have sent you a otp on number:" + number_tobecheck);

                        } else {
                            String string_otp = editText_otp.getText().toString();
//                            Otp_verification(number_tobecheck, string_otp);

                            String sending_url=BaseURL.BASEURL_B2C+"api/dmt/v2/sender-confirmation";
                            Uri.Builder builder=new Uri.Builder();
                            builder.appendQueryParameter("api_token", SharePrefManager.getInstance(SenderDetailActivity.this).mGetApiToken());
                            builder.appendQueryParameter("mobile_number",number_tobecheck);
                            builder.appendQueryParameter("otp",string_otp);

                            mCheckSender("otp",builder,sending_url);
                        }
                    }

                    else
                    {
                        Toast.makeText(SenderDetailActivity.this, "No Connection", Toast.LENGTH_SHORT).show();
                    }

                } else if (regiteration_status.equalsIgnoreCase("not register"))
                {
                    if (DetectConnection.checkInternetConnection(SenderDetailActivity.this))

                    {
                        if (editText_fname.getText().toString().equals("")) {
                            Toast.makeText(SenderDetailActivity.this, "Enter first name", Toast.LENGTH_SHORT).show();
                        }
                        else if (editText_lname.getText().toString().equals(""))
                        {
                            Toast.makeText(SenderDetailActivity.this, "Enter Last name", Toast.LENGTH_SHORT).show();
                        }
                        else if (edittext_pincode.getText().toString().equals(""))
                        {
                            Toast.makeText(SenderDetailActivity.this, "Enter PIN Code", Toast.LENGTH_SHORT).show();
                        }
                        else if (edittext_address.getText().toString().equals(""))
                        {
                            Toast.makeText(SenderDetailActivity.this, "Please Enter address", Toast.LENGTH_SHORT).show();
                        }
                        else if (edittext_state.getText().toString().equals(""))
                        {
                            Toast.makeText(SenderDetailActivity.this, "Please Enter State name", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            String firstname = editText_fname.getText().toString();
                            String lastname = editText_lname.getText().toString();
                            String pincode = edittext_pincode.getText().toString();
                            String address = edittext_address.getText().toString();
                            String state = edittext_state.getText().toString();

                            String sending_url=BaseURL.BASEURL_B2C+"api/dmt/v2/add-sender";
                            Uri.Builder builder=new Uri.Builder();
                            builder.appendQueryParameter("api_token", SharePrefManager.getInstance(SenderDetailActivity.this).mGetApiToken());
                            builder.appendQueryParameter("mobile_number",number_tobecheck);
                            builder.appendQueryParameter("first_name",firstname);
                            builder.appendQueryParameter("last_name",lastname);
                            builder.appendQueryParameter("pin_code",pincode);
                            builder.appendQueryParameter("address",address);
                            builder.appendQueryParameter("state",state);

                            mCheckSender("register",builder,sending_url);

//                            Register_user(number_tobecheck, firstname, lastname,pincode,address,state);
                        }
                    }

                    else
                    {
                        Toast.makeText(SenderDetailActivity.this, "No Connection", Toast.LENGTH_SHORT).show();
                    }

                }

            }
        });
        editText_mobileno = findViewById(R.id.edittext_mobileno);

    }

    protected void showRegistrationStatus(String response) {
        String message = "";
        String status="";
        String name="";
        String available_limit="";
        String total_spend="";
        String sender_id="";
        try {
            JSONObject jsonObject = new JSONObject(response);

            if (jsonObject.has("status"))
            {
                status=jsonObject.getString("status");
            }

            if (jsonObject.has("message"))
            {
                message=jsonObject.getString("message");
            }


            if (jsonObject.has("name"))
            {
                name=jsonObject.getString("name");
            }

            if (jsonObject.has("total_limit"))
            {
                available_limit=jsonObject.getString("total_limit");
            }

            if (jsonObject.has("sender_id"))
            {
                sender_id=jsonObject.getString("sender_id");
            }


//            JSONArray jsonArray=jsonObject1.getJSONArray("limit");

//            to get only limit available data from pipe 5 which is mention in json
//            if (jsonArray.length()>4)
//            {
//                JSONObject jsonObject2=jsonArray.getJSONObject(4);
//                available_limit=jsonObject2.getString("remaining");
//                total_spend=jsonObject2.getString("used");
//            }


        } catch (JSONException e) {
            e.printStackTrace();
        }


        if (status.equalsIgnoreCase("success")) {
            regiteration_status = "register";
            if (ll_contain_fname_lname.getVisibility() == View.VISIBLE) {
                ll_contain_fname_lname.setVisibility(View.GONE);

            }

            Bundle bundle=new Bundle();
            Intent intent=new Intent(SenderDetailActivity.this, Sender_allready_RegisterPaytm.class);
            bundle.putString("sender_number",number_tobecheck);
            bundle.putString("name",name);
            bundle.putString("status",status);
            bundle.putString("available_limit",available_limit);
            bundle.putString("total_spend",total_spend);
            bundle.putString("sender_id",sender_id);
            bundle.putInt("tab",1);
            intent.putExtras(bundle);
            startActivity(intent);
            finish();

        } else if (message.equalsIgnoreCase("Verification pending")) {
            regiteration_status = "not register";

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Your number "+number_tobecheck+" is not registered");
            builder.setCancelable(false);
            builder.setPositiveButton("Register", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    ll_contain_fname_lname.setVisibility(View.VISIBLE);
                    button_continue.setVisibility(View.VISIBLE);

                button_submit.setVisibility(View.GONE);
                }


            });

            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
//                    Toast.makeText(Money_Transfer_Reports.this, "You Click on Cancel button", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();

                }
            });
            AlertDialog alertDialog = builder.create();
            alertDialog.show();




        } else if (status.equalsIgnoreCase("failure")) {
            regiteration_status = "not register";

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Your number "+number_tobecheck+" is not registered");
            builder.setCancelable(false);
            builder.setPositiveButton("Register", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    ll_contain_fname_lname.setVisibility(View.VISIBLE);
                    button_continue.setVisibility(View.VISIBLE);

                    button_submit.setVisibility(View.GONE);
                    editText_mobileno.setVisibility(View.GONE);
                    getSupportActionBar().setTitle("DMT Registration");

                    ll_pin_addres_state.setVisibility(View.VISIBLE);

                }
            });

            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
//                    Toast.makeText(Money_Transfer_Reports.this, "You Click on Cancel button", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                    finish();
                }
            });
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        }
    }


//  to show registration message

    protected void showRegistrationMessage() {
        String status = "";
        String message = "";
        try {
            JSONObject jsonObject = new JSONObject(myJSON_register);
            status = jsonObject.getString("status");
            message = jsonObject.getString("message");

        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (status.equalsIgnoreCase("success")) {
            regiteration_status = "have to be enter otp";

            textView_otp_onusermobilenumber.setText("We have sent you a otp on number:" + number_tobecheck);
            ll_otp.setVisibility(View.VISIBLE);
            ll_mobile_number.setVisibility(View.GONE);
            ll_contain_fname_lname.setVisibility(View.GONE);

            ll_pin_addres_state.setVisibility(View.GONE);

        }

        else if (status.equalsIgnoreCase("failure"))
        {
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        }
        else
        {
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show();
        }
    }

//    to verify otp


    protected void OTPverificationMessage()
    {
        String status="";
        String message="";
        try
        {
            JSONObject jsonObject=new JSONObject(myJSON_otp);
            status=jsonObject.getString("status");
            message=jsonObject.getString("message");

        }

        catch (JSONException e)
        {
            e.printStackTrace();
        }
        if (status.equalsIgnoreCase("success"))
        {
            ll_display_success_message.setVisibility(View.VISIBLE);
            ll_otp.setVisibility(View.GONE);
            ll_mobile_number.setVisibility(View.GONE);
            ll_contain_fname_lname.setVisibility(View.GONE);
            ll_verification_pendding_message.setVisibility(View.GONE);

//            Intent intent=new Intent(Money_Transfer.this, Review_Activity.class);
//            Bundle bundle1=new Bundle();
//            bundle1.putString("activity","money_transfer");
//            intent.putExtras(bundle1);
//            startActivity(intent);
//            finish();
            if (DetectConnection.checkInternetConnection(SenderDetailActivity.this))
            {
                String sending_url=BaseURL.BASEURL_B2C+"api/dmt/v2/get-customer";
                Uri.Builder builder=new Uri.Builder();
                builder.appendQueryParameter("api_token", SharePrefManager.getInstance(SenderDetailActivity.this).mGetApiToken());
                builder.appendQueryParameter("mobile_number",number_tobecheck);

                mCheckSender("check",builder,sending_url);
            }
            else
            {
                Toast.makeText(this, "No internet connection", Toast.LENGTH_SHORT).show();
            }
        }
        else if (status.equalsIgnoreCase("failure"))
        {
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        }

        else
        {
            Toast.makeText(this, "Something went wrong please try after sometime", Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

//    @Override
//    public void onBackPressed() {
//        super.onBackPressed();
//        Intent intent=new Intent(Money_Transfer_Reports.this, MainActivity.class);
//        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
//        startActivity(intent);
//        finish();
//    }


    @SuppressLint("StaticFieldLeak")
    protected void mCheckSender(final String type, Uri.Builder builder, String sending_url)
    {

        new CallResAPIPOSTMethod(SenderDetailActivity.this,builder,sending_url,true,"POST"){
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                dialog=new ProgressDialog(SenderDetailActivity.this);
                dialog.setMessage("Please wait...");
                dialog.setCancelable(false);
                dialog.show();
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                dialog.dismiss();
                Log.e("check sender v2 ","response "+s);

                if (type.equalsIgnoreCase("check")) {
                    showRegistrationStatus(s);
                }
                else if (type.equalsIgnoreCase("register"))
                {
                    myJSON_register=s;
                    showRegistrationMessage();
                }
                else if (type.equalsIgnoreCase("otp"))
                {
                    myJSON_otp=s;
                    OTPverificationMessage();
                }
            }
        }.execute();
    }
}