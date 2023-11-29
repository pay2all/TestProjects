package com.demo.apppay2all.TRansactionPINDetails;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import com.demo.apppay2all.BaseURL.BaseURL;
import com.demo.apppay2all.CallResAPIPOSTMethod;
import com.demo.apppay2all.DetectConnection;
import com.demo.apppay2all.R;
import com.demo.apppay2all.SharePrefManager;

public class TransactionPIN extends AppCompatActivity {

    EditText ed_pin;
    Button bt_submit;
    ProgressDialog dialog;
    EditText ed_password,ed_otp;
    TextView tv_resend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_pin);

        if (getSupportActionBar()!=null)
        {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        ed_password=findViewById(R.id.ed_password);

        ed_pin=findViewById(R.id.ed_pin);
        ed_otp=findViewById(R.id.ed_otp);

        tv_resend=findViewById(R.id.tv_resend);
        tv_resend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (DetectConnection.checkInternetConnection(TransactionPIN.this))
                {
                    mSendOTP();
                }
                else {
                    Toast.makeText(TransactionPIN.this, "No internet connection", Toast.LENGTH_SHORT).show();
                }
            }
        });

        bt_submit=findViewById(R.id.bt_submit);

        bt_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (DetectConnection.checkInternetConnection(TransactionPIN.this))
                {
                    if (ed_password.getText().toString().equals(""))
                    {
                        Toast.makeText(TransactionPIN.this, "Please enter your login password", Toast.LENGTH_SHORT).show();
                    }
                    else if (ed_pin.getText().toString().equals(""))
                    {
                        Toast.makeText(TransactionPIN.this, "Please enter your PIN", Toast.LENGTH_SHORT).show();
                    }
                    else if (ed_pin.getText().toString().length()<4)
                    {
                        Toast.makeText(TransactionPIN.this, "Transaction should not be less than 4 digits", Toast.LENGTH_SHORT).show();
                    }
                    else if (ed_otp.getText().toString().equals(""))
                    {
                        Toast.makeText(TransactionPIN.this, "Please enter OTP", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        mGenerate();
                    }
                }
                else
                {
                    Toast.makeText(TransactionPIN.this, "No internet connection", Toast.LENGTH_SHORT).show();
                }
            }
        });

        if (DetectConnection.checkInternetConnection(TransactionPIN.this))
        {
            mSendOTP();
        }
        else {
            Toast.makeText(this, "No internet connection", Toast.LENGTH_SHORT).show();
            tv_resend.setVisibility(View.VISIBLE);
        }

    }

    @SuppressLint("StaticFieldLeak")
    protected void mGenerate()
    {
        String sending_url=BaseURL.BASEURL+"api/application/v1/pin-generate";
        Uri.Builder builder=new Uri.Builder();
        builder.appendQueryParameter("api_token",SharePrefManager.getInstance(TransactionPIN.this).mGetApiToken());
        builder.appendQueryParameter("password",ed_password.getText().toString());
        builder.appendQueryParameter("transaction_pin",ed_pin.getText().toString());
        builder.appendQueryParameter("otp",ed_otp.getText().toString());

        new CallResAPIPOSTMethod(TransactionPIN.this,builder,sending_url,true,"POST")
        {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                dialog=new ProgressDialog(TransactionPIN.this);
                dialog.setMessage("Please wait...");
                dialog.show();
                dialog.setCancelable(false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                dialog.dismiss();
                Log.e("pin","response "+s);

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
                        if (errors.has("transaction_pin")) {

                            String err =errors.getString("transaction_pin");
                            err = err.replaceAll("\\[", "").replaceAll("]", "").replaceAll("\"", "");
                            ed_pin.setError(err);
                        }
                        if (errors.has("password")) {

                            String err =errors.getString("password");
                            err = err.replaceAll("\\[", "").replaceAll("]", "").replaceAll("\"", "");
                            ed_password.setError(err);
                        }
                        if (errors.has("otp")) {

                            String err =errors.getString("otp");
                            err = err.replaceAll("\\[", "").replaceAll("]", "").replaceAll("\"", "");
                            ed_otp.setError(err);
                        }

                        Toast.makeText(TransactionPIN.this, message, Toast.LENGTH_SHORT).show();
                    }
                    else {
                        if (status.equalsIgnoreCase("success")) {
                            finish();
                        }
                        if (!message.equals("")) {
                            Toast.makeText(TransactionPIN.this, message, Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(TransactionPIN.this, "Something went wrong", Toast.LENGTH_SHORT).show();
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

    @SuppressLint("StaticFieldLeak")
    protected void mSendOTP()
    {
        Uri.Builder builder=new Uri.Builder();
        builder.appendQueryParameter("api_token",SharePrefManager.getInstance(TransactionPIN.this).mGetApiToken());

        String sending_url=BaseURL.BASEURL_B2C+"api/application/v1/send-transaction-pin-otp";
        new CallResAPIPOSTMethod(TransactionPIN.this,builder,sending_url,true,"POST")
        {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                dialog=new ProgressDialog(TransactionPIN.this);
                dialog.setMessage("Please wait...");
                dialog.show();
                dialog.setCancelable(false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                dialog.dismiss();
                Log.e("otp","response "+s);

                try {
                    JSONObject jsonObject=new JSONObject(s);

                    String status="",message="";

                    if (jsonObject.has("status"))
                    {
                        status=jsonObject.getString("status");
                    }

                    if (jsonObject.has("message"))
                    {
                        message=jsonObject.getString("message");
                    }

                    if (!status.equals(""))
                    {
                        if (status.equalsIgnoreCase("success"))
                        {
                            tv_resend.setVisibility(View.VISIBLE);
                        }
                        else if (!status.equalsIgnoreCase("success"))
                        {
                            if (!message.equals(""))
                            {
                                Toast.makeText(TransactionPIN.this, message, Toast.LENGTH_SHORT).show();
                            }
                        }

                    }
                    else
                    {
                        Toast.makeText(TransactionPIN.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                    }

                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }
            }

        }.execute();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull @NotNull MenuItem item) {
        if (item.getItemId()==android.R.id.home)
        {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

}