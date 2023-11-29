package com.demo.apppay2all;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.demo.apppay2all.BaseURL.BaseURL;

import org.json.JSONException;
import org.json.JSONObject;

public class ChangePassword extends AppCompatActivity {


    ProgressDialog dialog;
    EditText ed_current_password,ed_new_password,ed_confirm_password;
    ImageButton ib_next;
    TextView tv_current_error,tv_new_error,tv_confirm_error;
    Button bt_submit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        if (getSupportActionBar()!=null)
        {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        ed_current_password=findViewById(R.id.ed_current_password);
        ed_new_password=findViewById(R.id.ed_new_password);
        ed_confirm_password=findViewById(R.id.ed_confirm_password);

        tv_current_error=findViewById(R.id.tv_current_error);
        tv_new_error=findViewById(R.id.tv_new_error);
        tv_confirm_error=findViewById(R.id.tv_confirm_error);

        bt_submit=findViewById(R.id.bt_submit);
        bt_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSubmit();
            }
        });

    }

    public void mSubmit()
    {
        if (ed_current_password.getText().toString().equals(""))
        {
            tv_current_error.setVisibility(View.VISIBLE);
            tv_current_error.setText("Please enter current password");
        }
        else if (ed_new_password.getText().toString().equals(""))
        {
            tv_new_error.setVisibility(View.VISIBLE);
            tv_new_error.setText("Please enter new password");
            tv_current_error.setVisibility(View.GONE);
        }
        else if (ed_confirm_password.getText().toString().equals(""))
        {
            tv_confirm_error.setVisibility(View.VISIBLE);
            tv_confirm_error.setText("Please confirm your new password");
            tv_new_error.setVisibility(View.GONE);
        }
        else if (!ed_new_password.getText().toString().equals(ed_confirm_password.getText().toString()))
        {
            tv_confirm_error.setVisibility(View.VISIBLE);
            tv_confirm_error.setText("Password didn't confirm");
        }
        else
        {
            tv_confirm_error.setVisibility(View.GONE);
            String current_pass=ed_current_password.getText().toString();
            String new_pass=ed_new_password.getText().toString();
            String confirm_pass=ed_confirm_password.getText().toString();

            mSubmitPassword(current_pass,new_pass,confirm_pass);
        }
    }

    @SuppressLint("StaticFieldLeak")
    protected void mSubmitPassword(String old_password,String new_password,String confirm_password)
    {

        final Uri.Builder builder=new Uri.Builder();
        builder.appendQueryParameter("api_token", SharePrefManager.getInstance(ChangePassword.this).mGetApiToken());
        builder.appendQueryParameter("old_password",old_password);
        builder.appendQueryParameter("new_password",new_password);
        builder.appendQueryParameter("confirm_password",confirm_password);

        String sending_url= BaseURL.BASEURL_B2C+ "api/application/v1/change-password";

        new CallResAPIPOSTMethod(ChangePassword.this,builder,sending_url,true,"POST") {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                dialog=new ProgressDialog(ChangePassword.this);
                dialog.setMessage("Please wait...");
                dialog.setCancelable(false);
                dialog.show();
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                dialog.dismiss();
                Log.e("response","data : "+s);

                String status="",message="";
                try {
                    JSONObject jsonObject=new JSONObject(s);
                    if (jsonObject.has("status")){
                        status=jsonObject.getString("status");
                    }
                    if (jsonObject.has("message")){
                        message=jsonObject.getString("message");
                    }

                    if (status.equalsIgnoreCase("success"))
                    {
                        if (!message.equals(""))
                        {
                            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                        }
//                        ((FormsKyc)getActivity()).mChangeFragment(new KycFragment(),"2");
                        finish();

                    }
                    else if (!message.equals(""))
                    {
                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
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
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId()==android.R.id.home)
        {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}