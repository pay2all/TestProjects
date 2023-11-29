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
import com.demo.apppay2all.Login;
import com.demo.apppay2all.R;

import org.json.JSONException;
import org.json.JSONObject;

public class ForgetPasswordConfirm extends AppCompatActivity {

    ProgressDialog dialog;
    
    String number="",otp_message="";
    
    EditText ed_otp;
    
    AppCompatButton buttonContinue;
    TextView tv_resend_otp;

    ImageView iv_back;
    TextView tv_otp_error;

    TextView tv_message;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password_otp_verify);
        
        if (getSupportActionBar()!=null)
        {
            getSupportActionBar().hide();
        }

        iv_back=findViewById(R.id.iv_back);
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        tv_message=findViewById(R.id.tv_message);
        if (getIntent().hasExtra("number"))
        {
            number=getIntent().getStringExtra("number");
        }

        if (getIntent().hasExtra("message"))
        {
            otp_message=getIntent().getStringExtra("message");
            tv_message.setText(otp_message);
        }

        ed_otp=findViewById(R.id.ed_otp);
        tv_otp_error=findViewById(R.id.tv_otp_error);



        tv_resend_otp=findViewById(R.id.tv_resend_otp);
        tv_resend_otp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ForgotPasword(number);
            }
        });
        
        buttonContinue=findViewById(R.id.buttonContinue);
        buttonContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (DetectConnection.checkInternetConnection(ForgetPasswordConfirm.this))
                {
                    if (ed_otp.getText().toString().equals(""))
                    {
                        tv_otp_error.setVisibility(View.VISIBLE);
                        tv_otp_error.setText("Please enter OTP");
                    }
                    else {
                        tv_otp_error.setVisibility(View.GONE);
                        mConfirmOTP(number,ed_otp.getText().toString());
                    }
                }
                else {
                    Toast.makeText(ForgetPasswordConfirm.this, "No internet connection", Toast.LENGTH_SHORT).show();
                }
            }
        });
        
    }

    @SuppressLint("StaticFieldLeak")
    private  void  mConfirmOTP(final  String mobileno,final  String otp) {

        String sending_url= BaseURL.BASEURL_B2C+"api/application/v1/confirm-forgot-password";
        Uri.Builder builder=new Uri.Builder();
        builder.appendQueryParameter("mobile",mobileno);
        builder.appendQueryParameter("otp",otp);

        new CallResAPIPOSTMethod(ForgetPasswordConfirm.this,builder,sending_url,true,"POST")
        {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                dialog=new ProgressDialog(ForgetPasswordConfirm.this);
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
                            tv_otp_error.setError(err);
                            Toast.makeText(ForgetPasswordConfirm.this, err, Toast.LENGTH_SHORT).show();
                        }

                        if (errors.has("mobile"))
                        {
                            String err=errors.getString("mobile").replaceAll("\\[","").replaceAll("]","").replaceAll("\"","");
                            Toast.makeText(ForgetPasswordConfirm.this, err, Toast.LENGTH_SHORT).show();
                        }
                    }

                    if (!status.equals(""))
                    {
                        if (status.equalsIgnoreCase("success"))
                        {

                            if (!message.equals(""))
                            {
                                Toast.makeText(ForgetPasswordConfirm.this, message, Toast.LENGTH_SHORT).show();
                            }
                            finish();
                        }
                        else if (!message.equals(""))
                        {
                            Toast.makeText(ForgetPasswordConfirm.this, message, Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            Toast.makeText(ForgetPasswordConfirm.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else if (!message.equals(""))
                    {
                        Toast.makeText(ForgetPasswordConfirm.this, message, Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        Toast.makeText(ForgetPasswordConfirm.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                    }
                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }
            }

        }.execute();
    }

    @SuppressLint("StaticFieldLeak")
    private  void ForgotPasword(final  String username) {

        String sending_url= BaseURL.BASEURL_B2C+"api/application/v1/forgot-password-otp";
        Uri.Builder builder=new Uri.Builder();
        builder.appendQueryParameter("mobile",username);

        new CallResAPIPOSTMethod(ForgetPasswordConfirm.this,builder,sending_url,true,"POST")
        {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                dialog=new ProgressDialog(ForgetPasswordConfirm.this);
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
                                Toast.makeText(ForgetPasswordConfirm.this, err, Toast.LENGTH_SHORT).show();
                            }
                        }
                        else {
                            if (!status.equals("")) {

                                if (!message.equals("")) {
                                    Toast.makeText(ForgetPasswordConfirm.this, message, Toast.LENGTH_SHORT).show();
                                }
                            }
                            else if (!message.equals("")) {
                                Toast.makeText(ForgetPasswordConfirm.this, message, Toast.LENGTH_SHORT).show();
                            }
                            else {
                                Toast.makeText(ForgetPasswordConfirm.this, "Something went wrong, Please try again later", Toast.LENGTH_SHORT).show();
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

}