package com.demo.apppay2all;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;

import android.app.ProgressDialog;

import android.content.Intent;
import android.net.Uri;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.demo.apppay2all.BaseURL.BaseURL;

import org.json.JSONException;
import org.json.JSONObject;

public class Register extends AppCompatActivity {

    EditText edittext_first_name,edittext_last_name,edittext_mobileno,edittext_email,edittext_shop_name,edittext_address,edittext_city,edittext_pincode;
    Button button_signup;

    TextView tv_first_name_error,tv_last_name_error,tv_mobile_error,tv_email_error,tv_shop_error,tv_address_error,tv_city_error,tv_pin_error;

    ProgressDialog dialog;

    TextView textview_login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        getSupportActionBar().hide();

        edittext_first_name= findViewById(R.id.edittext_first_name);
        edittext_last_name= findViewById(R.id.edittext_last_name);

        edittext_mobileno= findViewById(R.id.edittext_mobileno);
        edittext_email= findViewById(R.id.edittext_email);

        edittext_shop_name= findViewById(R.id.edittext_shop_name);
        edittext_address= findViewById(R.id.edittext_address);
        edittext_city= findViewById(R.id.edittext_city);
        edittext_pincode= findViewById(R.id.edittext_pincode);

        tv_first_name_error=findViewById(R.id.tv_first_name_error);
        tv_last_name_error=findViewById(R.id.tv_last_name_error);
        tv_mobile_error=findViewById(R.id.tv_mobile_error);
        tv_email_error=findViewById(R.id.tv_email_error);
        tv_shop_error=findViewById(R.id.tv_shop_error);
        tv_address_error=findViewById(R.id.tv_address_error);
        tv_city_error=findViewById(R.id.tv_city_error);
        tv_pin_error=findViewById(R.id.tv_pin_error);

        button_signup= findViewById(R.id.button_signup);

        textview_login= findViewById(R.id.textview_login);
        textview_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Register.this,Login.class));
                overridePendingTransition(R.anim.slide_in_left,android.R.anim.slide_out_right);
                finish();
            }
        });

        button_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (DetectConnection.checkInternetConnection(Register.this))
                {
                    if (edittext_first_name.getText().toString().equals(""))
                    {
                        tv_first_name_error.setVisibility(View.VISIBLE);
                        tv_first_name_error.setText("Please enter first name");
                    }
                    else if (edittext_last_name.getText().toString().equals(""))
                    {

                        tv_last_name_error.setVisibility(View.VISIBLE);
                        tv_last_name_error.setText("Please enter last name");
                    }
                    else if (edittext_mobileno.getText().toString().equals(""))
                    {

                        tv_mobile_error.setVisibility(View.VISIBLE);
                        tv_mobile_error.setText("Please enter Mobile Number");
                    }

                    else if (edittext_mobileno.getText().toString().length()<10)
                    {

                        tv_mobile_error.setVisibility(View.VISIBLE);
                        tv_mobile_error.setText("Please enter a valid Mobile Number");
                    }
                    else if (edittext_email.getText().toString().equals(""))
                    {
                        tv_email_error.setVisibility(View.VISIBLE);
                        tv_email_error.setText("Please enter email");
                    }
                    else if (edittext_shop_name.getText().toString().equals(""))
                    {
                        tv_shop_error.setVisibility(View.VISIBLE);
                        tv_shop_error.setText("Please enter shop name");
                    }
                    else if (edittext_address.getText().toString().equals(""))
                    {
                        tv_address_error.setVisibility(View.VISIBLE);
                        tv_address_error.setText("Please enter address");
                    }
                    else if (edittext_city.getText().toString().equals(""))
                    {
                        tv_city_error.setVisibility(View.VISIBLE);
                        tv_city_error.setText("Please enter city name");
                    }
                    else if (edittext_pincode.getText().toString().equals(""))
                    {
                        tv_pin_error.setVisibility(View.VISIBLE);
                        tv_pin_error.setText("Please enter PIN Code");
                    }
                    else if (edittext_pincode.getText().toString().length()<6)
                    {
                        tv_pin_error.setVisibility(View.VISIBLE);
                        tv_pin_error.setText("Please enter PIN Code");
                    }
                    else
                    {
                        tv_first_name_error.setVisibility(View.GONE);
                        tv_last_name_error.setVisibility(View.GONE);
                        tv_mobile_error.setVisibility(View.GONE);
                        tv_email_error.setVisibility(View.GONE);
                        tv_shop_error.setVisibility(View.GONE);
                        tv_address_error.setVisibility(View.GONE);
                        tv_city_error.setVisibility(View.GONE);
                        tv_pin_error.setVisibility(View.GONE);
                        userSignup();
                    }
                }

                else
                {
                    Toast.makeText(Register.this,"No Internet Connection", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    @SuppressLint("StaticFieldLeak")
    private  void  userSignup() {

        String sending_url=BaseURL.BASEURL_B2C+"api/application/v1/sign-up";
        Uri.Builder builder=new Uri.Builder();
        builder.appendQueryParameter("first_name",edittext_first_name.getText().toString());
        builder.appendQueryParameter("last_name",edittext_last_name.getText().toString());
        builder.appendQueryParameter("email",edittext_email.getText().toString());
        builder.appendQueryParameter("mobile",edittext_mobileno.getText().toString());
        builder.appendQueryParameter("shop_name",edittext_shop_name.getText().toString());
        builder.appendQueryParameter("address",edittext_address.getText().toString());
        builder.appendQueryParameter("city",edittext_city.getText().toString());
        builder.appendQueryParameter("pin_code",edittext_pincode.getText().toString());
        builder.appendQueryParameter("referral_code",SharePrefManager.getInstance(Register.this).mGetSingleData("data_referal1"));

        new CallResAPIPOSTMethod(Register.this,builder,sending_url,true,"POST")
        {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                dialog=new ProgressDialog(Register.this);
                dialog.setMessage("Please wait...");
                dialog.show();
                dialog.setCancelable(false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                dialog.dismiss();
                Log.e("response","data "+s);

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

                    if (jsonObject.has("errors"))
                    {
                        JSONObject errors=jsonObject.getJSONObject("errors");
                        if (errors.has("first_name"))
                        {
                            String err=errors.getString("first_name").replaceAll("\\[","").replaceAll("]","").replaceAll("\"","");
                            tv_first_name_error.setVisibility(View.VISIBLE);
                            tv_first_name_error.setText(err);
                        }
                        if (errors.has("last_name"))
                        {
                            String err=errors.getString("last_name").replaceAll("\\[","").replaceAll("]","").replaceAll("\"","");
                            tv_last_name_error.setVisibility(View.VISIBLE);
                            tv_last_name_error.setText(err);
                        }

                        if (errors.has("email"))
                        {
                            String err=errors.getString("email").replaceAll("\\[","").replaceAll("]","").replaceAll("\"","");
                            tv_email_error.setVisibility(View.VISIBLE);
                            tv_email_error.setText(err);
                        }

                        if (errors.has("mobile"))
                        {
                            String err=errors.getString("mobile").replaceAll("\\[","").replaceAll("]","").replaceAll("\"","");
                            tv_mobile_error.setVisibility(View.VISIBLE);
                            tv_mobile_error.setText(err);
                        }
                        if (errors.has("shop_name"))
                        {
                            String err=errors.getString("shop_name").replaceAll("\\[","").replaceAll("]","").replaceAll("\"","");
                            tv_shop_error.setVisibility(View.VISIBLE);
                            tv_shop_error.setText(err);
                        }

                        if (errors.has("address"))
                        {
                            String err=errors.getString("address").replaceAll("\\[","").replaceAll("]","").replaceAll("\"","");
                            tv_address_error.setVisibility(View.VISIBLE);
                            tv_address_error.setText(err);
                        }

                        if (errors.has("city"))
                        {
                            String err=errors.getString("city").replaceAll("\\[","").replaceAll("]","").replaceAll("\"","");
                            tv_city_error.setVisibility(View.VISIBLE);
                            tv_city_error.setText(err);
                        }
                        if (errors.has("pin_code"))
                        {
                            String err=errors.getString("city").replaceAll("\\[","").replaceAll("]","").replaceAll("\"","");
                            tv_pin_error.setVisibility(View.VISIBLE);
                            tv_pin_error.setText(err);
                        }
                    }

                    else
                    {
                        if (!status.equals(""))
                        {
                            if (status.equalsIgnoreCase("success"))
                            {
                                if (!message.equals(""))
                                {
                                    Toast.makeText(Register.this, message, Toast.LENGTH_SHORT).show();
                                }

                                startActivity(new Intent(Register.this,Login.class));
                                overridePendingTransition(R.anim.slide_in_left,android.R.anim.slide_out_right);
                                finish();
                            }
                            else if (!status.equalsIgnoreCase("success"))
                            {
                                if (!message.equals(""))
                                {
                                    Toast.makeText(Register.this, message, Toast.LENGTH_SHORT).show();
                                }
                                else
                                {
                                    Toast.makeText(Register.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                                }
                            }
                            else if (!message.equals(""))
                            {
                                Toast.makeText(Register.this, message, Toast.LENGTH_SHORT).show();
                            }
                            else
                            {
                                Toast.makeText(Register.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                            }
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

    public static boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }

    public void onLoginClick(View view){
        startActivity(new Intent(this,Login.class));
        overridePendingTransition(R.anim.slide_in_left,android.R.anim.slide_out_right);
        finish();
    }
}