package com.demo.apppay2all.PayoutServices;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.demo.apppay2all.BaseURL.BaseURL;
import com.demo.apppay2all.CallResAPIPOSTMethod;
import com.demo.apppay2all.DetectConnection;
import com.demo.apppay2all.R;
import com.demo.apppay2all.SharePrefManager;

import org.json.JSONException;
import org.json.JSONObject;


public class PayoutAddBeneficiary extends AppCompatActivity {

    String bank_id="";
    public static TextView textview_bank_name;
    Button button_add_beneficiary;

    String username,password,email;


    String emailpattern="[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

    ProgressDialog dialog;

    RelativeLayout rl_bank;
    TextView tv_select_bank;
    EditText ed_ifsc,ed_account_number,ed_beneficiary_name;
    TextView tv_bank_error,tv_ifsc_error,tv_account_error,tv_name_error;
    Button bt_validate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payout_add_beneficiary);

        SharedPreferences sharedPreferences=getSharedPreferences("user",0);
        username=sharedPreferences.getString("username","");
        password=sharedPreferences.getString("password","");
        email=sharedPreferences.getString("email","");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        tv_select_bank=findViewById(R.id.tv_select_bank);
        ed_ifsc=findViewById(R.id.ed_ifsc);
        ed_account_number=findViewById(R.id.ed_account_number);
        ed_beneficiary_name=findViewById(R.id.ed_beneficiary_name);

        tv_bank_error=findViewById(R.id.tv_bank_error);
        tv_ifsc_error=findViewById(R.id.tv_ifsc_error);
        tv_account_error=findViewById(R.id.tv_account_error);
        tv_name_error=findViewById(R.id.tv_first_name_error);

        bt_validate=findViewById(R.id.bt_validate);
        bt_validate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (DetectConnection.checkInternetConnection(com.demo.apppay2all.PayoutServices.PayoutAddBeneficiary.this))
                {
                    if (bank_id.equals(""))
                    {
                        tv_bank_error.setText("Please select bank name");
                        tv_bank_error.setVisibility(View.VISIBLE);
                    }
                    else if (ed_ifsc.getText().toString().equals(""))
                    {
                        tv_ifsc_error.setVisibility(View.VISIBLE);
                        tv_ifsc_error.setText("Please enter IFSC");
                    }
                    else if (ed_account_number.getText().toString().equals(""))
                    {
                        tv_account_error.setText("Please enter account number");
                        tv_account_error.setVisibility(View.VISIBLE);
                    }
                    else
                    {
                        tv_bank_error.setVisibility(View.GONE);
                        tv_ifsc_error.setVisibility(View.GONE);
                        tv_account_error.setVisibility(View.GONE);

                        String ifsc=ed_ifsc.getText().toString();
                        String account=ed_account_number.getText().toString();
                        mValidate(ifsc,account);
                    }
                }
                else
                {
                    Toast.makeText(com.demo.apppay2all.PayoutServices.PayoutAddBeneficiary.this, "NO internet connection", Toast.LENGTH_SHORT).show();
                }
            }
        });

        rl_bank=findViewById(R.id.rl_bank);
        rl_bank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PayoutBankListBottomSheet3DialogFragment bottomSheetDialogFragment = new PayoutBankListBottomSheet3DialogFragment();
                Bundle bundle=new Bundle();
                bundle.putString("type","1");
                bundle.putString("activity","payout");
                bottomSheetDialogFragment.setArguments(bundle);
                bottomSheetDialogFragment.show(getSupportFragmentManager(), bottomSheetDialogFragment.getTag());
            }
        });





        button_add_beneficiary=findViewById(R.id.button_add_beneficiary);
        button_add_beneficiary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (DetectConnection.checkInternetConnection(com.demo.apppay2all.PayoutServices.PayoutAddBeneficiary.this))
                {
                    if (bank_id.equals(""))
                    {
                        tv_bank_error.setText("Please select bank name");
                        tv_bank_error.setVisibility(View.VISIBLE);
                    }
                    else if (ed_ifsc.getText().toString().equals(""))
                    {
                        tv_ifsc_error.setVisibility(View.VISIBLE);
                        tv_ifsc_error.setText("Please enter IFSC");
                    }
                    else if (ed_account_number.getText().toString().equals(""))
                    {
                        tv_account_error.setText("Please enter account number");
                        tv_account_error.setVisibility(View.VISIBLE);
                    }
                    else if (ed_beneficiary_name.getText().toString().equals(""))
                    {
                        tv_name_error.setVisibility(View.VISIBLE);
                        tv_name_error.setText("Please validate account number to verify name");
                    }
                    else
                    {

                        tv_bank_error.setVisibility(View.GONE);
                        tv_ifsc_error.setVisibility(View.GONE);
                        tv_account_error.setVisibility(View.GONE);
                        tv_name_error.setVisibility(View.GONE);

                        String ifsc=ed_ifsc.getText().toString();
                        String account=ed_account_number.getText().toString();
                        String name=ed_beneficiary_name.getText().toString();
                        mAddBeneficiary(ifsc,account,name);

                    }
                }
                else
                {
                    Toast.makeText(com.demo.apppay2all.PayoutServices.PayoutAddBeneficiary.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    @SuppressLint("StaticFieldLeak")
    protected void mValidate(String ifsc, String account)
    {
        String sending_url= BaseURL.BASEURL_B2C+ "api/settlement/account-validate";
        Uri.Builder builder=new Uri.Builder();
        builder.appendQueryParameter("api_token", SharePrefManager.getInstance(com.demo.apppay2all.PayoutServices.PayoutAddBeneficiary.this).mGetApiToken());
        builder.appendQueryParameter("bank_id",bank_id);
        builder.appendQueryParameter("ifsc_code",ifsc);
        builder.appendQueryParameter("account_number",account);
        builder.appendQueryParameter("mobile_number",SharePrefManager.getInstance(com.demo.apppay2all.PayoutServices.PayoutAddBeneficiary.this).mGetUsername());

        new CallResAPIPOSTMethod(com.demo.apppay2all.PayoutServices.PayoutAddBeneficiary.this,builder,sending_url,true,"POST")
        {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                dialog=new ProgressDialog(com.demo.apppay2all.PayoutServices.PayoutAddBeneficiary.this);
                dialog.setMessage("Please wait...");
                dialog.setCancelable(false);
                dialog.show();
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                dialog.dismiss();
                Log.e("repone","valid "+s);

                if (!s.equals(""))
                {
                    String status="",message="",beneficiary_name="";
                    try
                    {
                        JSONObject jsonObject=new JSONObject(s);
                        if (jsonObject.has("status"))
                        {
                            status=jsonObject.getString("status");
                        }
                        if (jsonObject.has("message"))
                        {
                            message=jsonObject.getString("message");
                        }
                        if (jsonObject.has("beneficiary_name"))
                        {
                            beneficiary_name=jsonObject.getString("beneficiary_name");
                        }

                        if (status.equalsIgnoreCase("success"))
                        {
                            ed_beneficiary_name.setText(beneficiary_name);
                            button_add_beneficiary.setVisibility(View.VISIBLE);
                        }
                        else if (!message.equals(""))
                        {
                            Toast.makeText(com.demo.apppay2all.PayoutServices.PayoutAddBeneficiary.this, message, Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            Toast.makeText(com.demo.apppay2all.PayoutServices.PayoutAddBeneficiary.this, "Something went wrong, please try again later", Toast.LENGTH_SHORT).show();
                        }
                    }
                    catch (JSONException e)
                    {
                        e.printStackTrace();
                    }
                }
                else
                {
                    Toast.makeText(com.demo.apppay2all.PayoutServices.PayoutAddBeneficiary.this, "Server not responding", Toast.LENGTH_SHORT).show();
                }
            }
        }.execute();
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

    public void mGetBankDetail(BankListItems items)
    {
        tv_select_bank.setText(items.getBank());
        bank_id=items.getId();
        ed_ifsc.setText(items.getIfsc());
    }

    @SuppressLint("StaticFieldLeak")
    protected void mAddBeneficiary(String ifsc,String account,String name)
    {
        String sending_url= BaseURL.BASEURL_B2C+"api/settlement/add-beneficiary";
        Uri.Builder builder=new Uri.Builder();
        builder.appendQueryParameter("api_token", SharePrefManager.getInstance(com.demo.apppay2all.PayoutServices.PayoutAddBeneficiary.this).mGetApiToken());
        builder.appendQueryParameter("bank_id",bank_id);
        builder.appendQueryParameter("ifsc_code",ifsc);
        builder.appendQueryParameter("account_number",account);
        builder.appendQueryParameter("mobile_number",SharePrefManager.getInstance(com.demo.apppay2all.PayoutServices.PayoutAddBeneficiary.this).mGetUsername());
        builder.appendQueryParameter("beneficiary_name",name);

        new CallResAPIPOSTMethod(com.demo.apppay2all.PayoutServices.PayoutAddBeneficiary.this,builder,sending_url,true,"POST")
        {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                dialog=new ProgressDialog(com.demo.apppay2all.PayoutServices.PayoutAddBeneficiary.this);
                dialog.setMessage("Please wait...");
                dialog.setCancelable(false);
                dialog.show();
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                dialog.dismiss();

                Log.e("Reponse","aad b "+s);
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

                        mShowRecipt(status,message);
                    }
                    catch (JSONException e)
                    {
                        e.printStackTrace();
                    }
                }
                else
                {
                    Toast.makeText(com.demo.apppay2all.PayoutServices.PayoutAddBeneficiary.this, "Server not responding", Toast.LENGTH_SHORT).show();
                }
            }
        }.execute();
    }

    protected void mShowRecipt(final String status,final String message)
    {
        final AlertDialog alertDialog;
        LayoutInflater inflater2 =(LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v2=inflater2.inflate(R.layout.custom_alertdalog_for_message,null);

        ImageView imageview_messase_image=v2.findViewById(R.id.imageview_messase_image);

        TextView textview_message=v2.findViewById(R.id.textview_message);
        Button button_ok=v2.findViewById(R.id.button_ok);

        textview_message.setText(message);

        if (status.equalsIgnoreCase("success")){
            imageview_messase_image.setBackground(getResources().getDrawable(R.drawable.checkicon));

        }
        else if (status.equalsIgnoreCase("failure")){
            imageview_messase_image.setBackground(getResources().getDrawable(R.drawable.failure));

        }
        else if (status.equalsIgnoreCase("pending")){
            imageview_messase_image.setBackground(getResources().getDrawable(R.drawable.pending));

        }
        else {
            imageview_messase_image.setBackground(getResources().getDrawable(R.drawable.pending));

        }

        final AlertDialog.Builder builder2=new AlertDialog.Builder(com.demo.apppay2all.PayoutServices.PayoutAddBeneficiary.this);
        builder2.setCancelable(false);
        builder2.setView(v2);
        alertDialog=builder2.create();

        button_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
                if (status.equalsIgnoreCase("success"))
                {
                    finish();
                }
            }
        });
        alertDialog.show();
    }
}
