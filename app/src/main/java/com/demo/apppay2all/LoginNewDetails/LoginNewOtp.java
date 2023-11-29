package com.demo.apppay2all.LoginNewDetails;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.demo.apppay2all.BaseURL.BaseURL;
import com.demo.apppay2all.CallResAPIPOSTMethod;
import com.demo.apppay2all.DetectConnection;
import com.demo.apppay2all.MainActivity;
import com.demo.apppay2all.R;
import com.demo.apppay2all.SharePrefManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class LoginNewOtp extends AppCompatActivity {

    ImageView iv_back;
    
    String lat="",log="",username="",password="",device_id="",otp_message="";
    
    ProgressDialog dialog;

    EditText ed_otp;
    TextView tv_resend;

    AppCompatButton buttonContinue;

    TextView tv_otp_error,tv_otp_message;
    
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_confirm);
        if (getSupportActionBar()!=null)
        {
            getSupportActionBar().hide();
        }

        tv_otp_message=findViewById(R.id.tv_otp_message);
        tv_otp_error=findViewById(R.id.tv_otp_error);

        if (getIntent().hasExtra("number"))
        {
            username=getIntent().getStringExtra("number");
        }

        if (getIntent().hasExtra("message"))
        {
            otp_message=getIntent().getStringExtra("message");
            tv_otp_message.setText(otp_message);
        }

        if (getIntent().hasExtra("password"))
        {
            password =getIntent().getStringExtra("password");
        }

        if (getIntent().hasExtra("device_id"))
        {
            device_id=getIntent().getStringExtra("device_id");
        }

        if (getIntent().hasExtra("lat"))
        {
            lat=getIntent().getStringExtra("lat");
        }

        if (getIntent().hasExtra("log"))
        {
            log=getIntent().getStringExtra("log");
        }

        iv_back=findViewById(R.id.iv_back);
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        ed_otp=findViewById(R.id.ed_otp);
        tv_resend=findViewById(R.id.tv_resend);
        tv_resend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (DetectConnection.checkInternetConnection(LoginNewOtp.this)) {
                    mLogin(username, password, device_id, "login");
                }
                else {
                    Toast.makeText(LoginNewOtp.this, "No internet connection", Toast.LENGTH_SHORT).show();
                }
            }
        });

        buttonContinue=findViewById(R.id.buttonContinue);
        buttonContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (DetectConnection.checkInternetConnection(LoginNewOtp.this)) {
                    if (ed_otp.getText().toString().equals(""))
                    {
                        tv_otp_error.setVisibility(View.VISIBLE);
                        tv_otp_error.setText("Please enter OTP");
                    }
                    else {
                        tv_otp_error.setVisibility(View.GONE);
                        mLogin(username, password, device_id, "otp");
                    }
                }
                else {
                    Toast.makeText(LoginNewOtp.this, "No Internet connection", Toast.LENGTH_SHORT).show();
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
        builder.appendQueryParameter("otp",ed_otp.getText().toString());
        builder.appendQueryParameter("latitude",lat+"");
        builder.appendQueryParameter("longitude",log+"");

        new CallResAPIPOSTMethod(LoginNewOtp.this,builder,url,true,"POST") {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                dialog=new ProgressDialog(LoginNewOtp.this);
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

                            Toast.makeText(LoginNewOtp.this, username_error, Toast.LENGTH_LONG).show();

                        }
                    }

                    if (status.equalsIgnoreCase("success"))
                    {
                        mShowLoginDetail(s);
                    }
//                    else if (status.equalsIgnoreCase("pending"))
//                    {
//                        tv_otp_message.setText(message);
//                    }
                    else if (!message.equals(""))
                    {
                        Toast.makeText(LoginNewOtp.this, message, Toast.LENGTH_SHORT).show();
                        tv_otp_message.setText(message);
                    }
                    else
                    {
                        Toast.makeText(LoginNewOtp.this, "Something went wrong...", Toast.LENGTH_SHORT).show();
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

                    SharePrefManager.getInstance(LoginNewOtp.this).mSaveSingleData("broadcast_status_id",status_id);
                }

                SharePrefManager.getInstance(LoginNewOtp.this).mSaveSingleData("broadcast",broadcast.toString());

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
                    SharePrefManager.getInstance(LoginNewOtp.this).mSaveSingleData("account_number",userdetail.getString("account_number"));
                }

                if (userdetail.has("ifsc_code"))
                {
                    SharePrefManager.getInstance(LoginNewOtp.this).mSaveSingleData("ifsc_code",userdetail.getString("ifsc_code"));
                }

                if (userdetail.has("pan_username"))
                {
                    SharePrefManager.getInstance(LoginNewOtp.this).mSaveSingleData("pan_username",userdetail.getString("pan_username"));
                }

                if (userdetail.has("ekyc"))
                {
                    SharePrefManager.getInstance(LoginNewOtp.this).mSaveSingleData("ekyc",userdetail.getString("ekyc"));
                }

                if (userdetail.has("pan_number"))
                {
                    SharePrefManager.getInstance(LoginNewOtp.this).mSaveSingleData("pan_number",userdetail.getString("pan_number"));
                }
                if (userdetail.has("agentonboarding"))
                {
                    SharePrefManager.getInstance(LoginNewOtp.this).mSaveSingleData("agentonboarding",userdetail.getString("agentonboarding"));
                }

//                JSONObject userservices=jsonObject.getJSONObject("userservices");
//                recharge=userservices.getString("recharge");
//                money=userservices.getString("money");
//
//                if (userservices.has("money_two")) {
//                    String money_two = userservices.getString("money_two");
//                    SharePrefManager.getInstance(LoginNewOtp.this).mSaveSingleData("money_two",money_two);
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
                    SharePrefManager.getInstance(LoginNewOtp.this).mSaveSingleData("color_start",color_start);
                }

                if (companydetails.has("color_end")) {
                    String color_end = companydetails.getString("color_end");
                    SharePrefManager.getInstance(LoginNewOtp.this).mSaveSingleData("color_end",color_end);
                }

                if (companydetails.has("transaction_pin")) {
                    String transaction_pin = companydetails.getString("transaction_pin");
                    SharePrefManager.getInstance(LoginNewOtp.this).mSaveSingleData("transaction_pin",transaction_pin);
                }
//
//                company_aeps=companydetails.getString("aeps");
//                company_payout=companydetails.getString("payout");

//                company_pancard=companydetails.getString("pancard");
//                company_ecommerce=companydetails.getString("ecommerce");
//
//                if (companydetails.has("upi_id")) {
//                    String upi_id = companydetails.getString("upi_id");
//                    SharePrefManager.getInstance(LoginNewOtp.this).mSaveSingleData("upi_id",upi_id);
//                }
//
//                if (companydetails.has("collection")) {
//                    String collection = companydetails.getString("collection");
//                    SharePrefManager.getInstance(LoginNewOtp.this).mSaveSingleData("collection",collection);
//                }

                JSONArray banners=jsonObject.getJSONArray("banner");

                SharePrefManager.getInstance(LoginNewOtp.this).mSaveUserid(user_id);
                SharePrefManager.getInstance(LoginNewOtp.this).mSaveUserData(username,password,first_name,last_name,email,mobile,role_id,scheme_id,
                        joing_date,permanent_address,permanent_city,permanent_district,permanent_pin_code,permanent_state,present_address,
                        present_city,present_district,present_pin_code,present_state,lien_amount,office_address,call_back_url,profile_photo,
                        shop_name,shop_photo,gst_regisration_photo,pancard_photo,cancel_cheque,address_proof,kyc_status,kyc_remark,mobile_verified,
                        lock_amount,session_id,active,reason,api_token,user_balance,aeps_balance,recharge,money,aeps,payout,pancard,ecommerce,
                        company_name,company_email,company_address,company_address_two,support_number,whatsapp_number,company_logo,company_website,
                        news,update_one,update_two,update_three,update_for,sender_id,company_recharge,company_money,company_aeps,company_payout,view_plan,company_pancard,company_ecommerce,banners.toString(),"","");

//                Intent intent = new Intent(LoginNewOtp.this, MainActivitySingle.class);
                Intent intent = new Intent(LoginNewOtp.this, MainActivity.class);
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

}
