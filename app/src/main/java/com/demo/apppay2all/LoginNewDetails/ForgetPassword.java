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

public class ForgetPassword extends AppCompatActivity {

    EditText ed_number;
    AppCompatButton buttonContinue;
    ImageView iv_back;

    ProgressDialog dialog;
    TextView tv_number_error;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);
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

        ed_number=findViewById(R.id.ed_number);
        tv_number_error=findViewById(R.id.tv_number_error);
        
        buttonContinue=findViewById(R.id.buttonContinue);
        buttonContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (DetectConnection.checkInternetConnection(ForgetPassword.this))
                {
                    if (ed_number.getText().toString().equals(""))
                    {
                        tv_number_error.setVisibility(View.VISIBLE);
                        tv_number_error.setText("Please enter mobile number");
                    }
                    else if (ed_number.getText().toString().length()<10)
                    {
                        tv_number_error.setVisibility(View.VISIBLE);
                        tv_number_error.setText("Please enter a valid mobile number");
                    }
                    else {
                        ForgotPasword(ed_number.getText().toString());
                    }
                }
                else {
                    Toast.makeText(ForgetPassword.this, "No internet connection", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    @SuppressLint("StaticFieldLeak")
    private  void ForgotPasword(final  String username) {

        String sending_url= BaseURL.BASEURL_B2C+"api/application/v1/forgot-password-otp";
        Uri.Builder builder=new Uri.Builder();
        builder.appendQueryParameter("mobile",username);

        new CallResAPIPOSTMethod(ForgetPassword.this,builder,sending_url,true,"POST")
        {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                dialog=new ProgressDialog(ForgetPassword.this);
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
                                Toast.makeText(ForgetPassword.this, err, Toast.LENGTH_SHORT).show();
                                tv_number_error.setText(err);
                                tv_number_error.setVisibility(View.VISIBLE);
                            }
                        }
                        else {
                            if (!status.equals("")) {
                                if (status.equalsIgnoreCase("success")) {
                                    Intent intent=new Intent(ForgetPassword.this,ForgetPasswordConfirm.class);
                                    intent.putExtra("number",ed_number.getText().toString());
                                    intent.putExtra("message",message);
                                    startActivity(intent);
                                    finish();
                                }

                                if (!message.equals("")) {
                                    Toast.makeText(ForgetPassword.this, message, Toast.LENGTH_SHORT).show();
                                }
                            }
                            else if (!message.equals("")) {
                                Toast.makeText(ForgetPassword.this, message, Toast.LENGTH_SHORT).show();
                            }
                            else {
                                Toast.makeText(ForgetPassword.this, "Something went wrong, Please try again later", Toast.LENGTH_SHORT).show();
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