package com.demo.apppay2all.PayoutServices;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.demo.apppay2all.BaseURL.BaseURL;
import com.demo.apppay2all.CallResAPIPOSTMethod;
import com.demo.apppay2all.DetectConnection;
import com.demo.apppay2all.NumberToWord;
import com.demo.apppay2all.R;
import com.demo.apppay2all.SharePrefManager;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

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
import java.util.List;

public class PayoutNew extends AppCompatActivity {

    TextView textview_name,textview_account_number,textview_ifsc,textview_mobile_number;
    EditText edittext_amount;
    Button button_submit;
    ProgressDialog dialog;
    String username="",password="",amount="";
    String name="";
    String account_number="";
    String ifsc="";
    String mobile_number="";

    TextView tv_aeps_balance,tv_message;
    EditText ed_number;
    Button bt_search;
    RecyclerView recyclerview_bene;
    FloatingActionButton ab_add;

    List<BeneficiaryItems> beneficiaryItems;
    BeneficiaryCardAdapter beneficiaryCardAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payout_new);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        tv_aeps_balance=findViewById(R.id.tv_aeps_balance);
        tv_aeps_balance.setText("AEPS Balance : "+SharePrefManager.getInstance(com.demo.apppay2all.PayoutServices.PayoutNew.this).mGetAeps_balance());

        tv_message=findViewById(R.id.tv_message);
        ab_add=findViewById(R.id.ab_add);
        ab_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PayoutNew.this,PayoutAddBeneficiary.class));
            }
        });

        ed_number=findViewById(R.id.ed_number);
        ed_number.setText(SharePrefManager.getInstance(com.demo.apppay2all.PayoutServices.PayoutNew.this).mGetUsername());
        bt_search=findViewById(R.id.bt_search);
        bt_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (DetectConnection.checkInternetConnection(com.demo.apppay2all.PayoutServices.PayoutNew.this)) {
                    mGetBeneLIst();
                }
                else
                {
                    Toast.makeText(com.demo.apppay2all.PayoutServices.PayoutNew.this, "No internet connection", Toast.LENGTH_SHORT).show();
                }
            }
        });
        recyclerview_bene=findViewById(R.id.recyclerview_bene);
        recyclerview_bene.setLayoutManager(new LinearLayoutManager(com.demo.apppay2all.PayoutServices.PayoutNew.this));
        beneficiaryItems=new ArrayList<>();
        beneficiaryCardAdapter=new BeneficiaryCardAdapter(com.demo.apppay2all.PayoutServices.PayoutNew.this,beneficiaryItems);
        recyclerview_bene.setAdapter(beneficiaryCardAdapter);

        textview_name=findViewById(R.id.textview_name);
        textview_account_number=findViewById(R.id.textview_account_number);
        textview_ifsc=findViewById(R.id.textview_ifsc);
        textview_mobile_number=findViewById(R.id.textview_mobile_number);

        edittext_amount=findViewById(R.id.edittext_amount);
        TextView tv_amount_word=findViewById(R.id.tv_amount_word);
        edittext_amount.addTextChangedListener(new TextWatcher() {
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


        username= SharePrefManager.getInstance(com.demo.apppay2all.PayoutServices.PayoutNew.this).mGetUsername();
        password=SharePrefManager.getInstance(com.demo.apppay2all.PayoutServices.PayoutNew.this).getPassword();

        if(DetectConnection.checkInternetConnection(com.demo.apppay2all.PayoutServices.PayoutNew.this)){

//            getData(username,password);

        }

        else {
            Toast.makeText(com.demo.apppay2all.PayoutServices.PayoutNew.this, "No Internet Connection", Toast.LENGTH_SHORT).show();

        }


        button_submit=findViewById(R.id.button_submit);
        button_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (DetectConnection.checkInternetConnection(com.demo.apppay2all.PayoutServices.PayoutNew.this)){

                    if (edittext_amount.getText().toString().equals("")){

                        Toast.makeText(com.demo.apppay2all.PayoutServices.PayoutNew.this, "Please Enter Amount", Toast.LENGTH_SHORT).show();

                    }

                    else {

                         amount=edittext_amount.getText().toString();
                        mTransferNow(username,password,name,account_number,ifsc,mobile_number,amount);

                    }
                }

                else {

                    Toast.makeText(com.demo.apppay2all.PayoutServices.PayoutNew.this, "No Internet Connection", Toast.LENGTH_SHORT).show();

                }

            }
        });



    }


    private  void  getData(final String username, final String password) {
        class getJSONData extends AsyncTask<String, String, String> {


            HttpURLConnection urlConnection;


            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                dialog=new ProgressDialog(com.demo.apppay2all.PayoutServices.PayoutNew.this);
                dialog.setMessage("Please wait...");
                dialog.show();
                dialog.setCancelable(false);
            }
            @Override
            protected String doInBackground(String... args) {

                StringBuilder result = new StringBuilder();

                try {
                    URL url = new URL(BaseURL.BASEURL +"application/v1/payout-get-beneficiary-list?username="+username+"&password="+password);
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
                dialog.dismiss();
                Log.e("response","data "+result);
                if (!result.equals("")){

                    String status="";

                    try {

                        JSONObject jsonObject=new JSONObject(result);
                        if (jsonObject.has("status")){
                            status=jsonObject.getString("status");
                        }

                        if (status.equalsIgnoreCase("success")){
                            JSONObject data=jsonObject.getJSONObject("details");
                            name=data.getString("name");
                            account_number=data.getString("account_number");
                            ifsc=data.getString("ifsc");
                            mobile_number=data.getString("mobile_number");



                            textview_name.setText(name);
                            textview_mobile_number.setText("Mobile : "+mobile_number);
                            textview_account_number.setText("A/c : "+account_number);
                            textview_ifsc.setText("IFSC : "+ifsc);
                        }


                    }

                    catch (JSONException e){
                        e.printStackTrace();

                    }
                }

                else {

                    Toast.makeText(com.demo.apppay2all.PayoutServices.PayoutNew.this, "Server Not Responding", Toast.LENGTH_SHORT).show();
                }
            }
        }

        getJSONData getJSONData=new getJSONData();
        getJSONData.execute();
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

    public void mTransferNow(String username, String password, String beneficiary_name, String beneficiary_account_number, String beneficiary_ifsc, String beneficiary_mobile_number, String amount){
        String method="POST";
        String sendingurl=BaseURL.BASEURL+"application/v1/payout-transfer-now";
        Uri.Builder builder=new Uri.Builder();
        builder.appendQueryParameter("username",username);
        builder.appendQueryParameter("password",password);
        builder.appendQueryParameter("beneficiary_name",beneficiary_name);
        builder.appendQueryParameter("beneficiary_account_number",beneficiary_account_number);
        builder.appendQueryParameter("beneficiary_ifsc",beneficiary_ifsc);
        builder.appendQueryParameter("beneficiary_mobile_number",beneficiary_mobile_number);
        builder.appendQueryParameter("amount",amount);


        @SuppressLint("StaticFieldLeak")
        CallResAPIPOSTMethod callResAPIPOSTMethod=new CallResAPIPOSTMethod(com.demo.apppay2all.PayoutServices.PayoutNew.this,builder,sendingurl,true,method) {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                dialog=new ProgressDialog(com.demo.apppay2all.PayoutServices.PayoutNew.this);
                dialog.setMessage("Please Wait...");
                dialog.show();
                dialog.setCancelable(false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                dialog.dismiss();
                if (!s.equals("")){

                    String status="", message="";

                    try {

                        JSONObject jsonObject=new JSONObject(s);
                        if (jsonObject.has("status")){

                            status=jsonObject.getString("status");
                        }
                        if (jsonObject.has("message")){

                            message=jsonObject.getString("message");
                        }

                        mShowRecipt(status,message);
                    }
                    catch (JSONException e){

                        e.printStackTrace();
                    }

                }

                else {

                    Toast.makeText(com.demo.apppay2all.PayoutServices.PayoutNew.this, "Server Not Responding", Toast.LENGTH_SHORT).show();
                }

            }
        };

        callResAPIPOSTMethod.execute();
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
        TextView textview_mobile_title=v2.findViewById(R.id.textview_mobile_title);
        textview_mobile_title.setText("Account Number");
        TextView textview_amount=v2.findViewById(R.id.textview_amount);
        Button button_done=v2.findViewById(R.id.button_done);

        textview_requestsendsuccessfull.setText(message);
        textview_oper.setText("Payout");
        textview_mobnumber.setText(account_number);
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

        final AlertDialog.Builder builder2=new AlertDialog.Builder(com.demo.apppay2all.PayoutServices.PayoutNew.this);
        builder2.setCancelable(false);
        builder2.setView(v2);
        alertDialog=builder2.create();

        button_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
                finish();
            }
        });
        alertDialog.show();
    }

    @SuppressLint("StaticFieldLeak")
    protected void mGetBeneLIst()
    {
        String sending_url=BaseURL.BASEURL_B2C+"api/settlement/beneficiary-list";
        Uri.Builder builder=new Uri.Builder();
        builder.appendQueryParameter("api_token",SharePrefManager.getInstance(com.demo.apppay2all.PayoutServices.PayoutNew.this).mGetApiToken());
        builder.appendQueryParameter("mobile_number",SharePrefManager.getInstance(com.demo.apppay2all.PayoutServices.PayoutNew.this).mGetUsername());

        new CallResAPIPOSTMethod(com.demo.apppay2all.PayoutServices.PayoutNew.this,builder,sending_url,true,"POST")
        {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                dialog=new ProgressDialog(com.demo.apppay2all.PayoutServices.PayoutNew.this);
                dialog.setMessage("Please wait...");
                dialog.setCancelable(false);
                dialog.show();
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                dialog.dismiss();
                Log.e("bene","list "+s);

                if (!s.equals(""))
                {
                    String status="";
                    try {
                        JSONObject jsonObject=new JSONObject(s);
                        if (jsonObject.has("status"))
                        {
                            status=jsonObject.getString("status");
                        }

                        if (status.equalsIgnoreCase("success"))
                        {
                            beneficiaryItems.clear();
                            JSONArray jsonArray=jsonObject.getJSONArray("beneficiary_list");

                            for (int i=0; i<jsonArray.length(); i++)
                            {
                                JSONObject data=jsonArray.getJSONObject(i);
                                BeneficiaryItems items=new BeneficiaryItems();
                                items.setSr_no(data.getString("sr_no"));
                                items.setBeneficiary_id(data.getString("beneficiary_id"));
                                items.setMobile_number(data.getString("mobile_number"));
                                items.setAccount_number(data.getString("account_number"));
                                items.setHolder_name(data.getString("holder_name"));
                                items.setBank_name(data.getString("bank_name"));
                                items.setIfsc_code(data.getString("ifsc_code"));
                                items.setStatus_id(data.getString("status_id"));

                                beneficiaryItems.add(items);
                                beneficiaryCardAdapter.notifyDataSetChanged();

                            }
                        }

                        if (jsonObject.length()==0)
                        {
                            tv_message.setText("Beneficiary not found");
                            tv_message.setVisibility(View.VISIBLE);
                        }
                        else
                        {
                            tv_message.setVisibility(View.GONE);
                        }
                    }
                    catch (JSONException e)
                    {
                        e.printStackTrace();
                    }
                }
                else
                {
                    Toast.makeText(com.demo.apppay2all.PayoutServices.PayoutNew.this, "server not responding", Toast.LENGTH_SHORT).show();
                }
            }
        }
        .execute();
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (DetectConnection.checkInternetConnection(com.demo.apppay2all.PayoutServices.PayoutNew.this))
        {
            mGetBeneLIst();
        }
    }
}