package com.demo.apppay2all;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.demo.apppay2all.BaseURL.BaseURL;
import com.demo.apppay2all.Statment.Account_Statement_Item;
import com.demo.apppay2all.Statment.Account_Statemnt_CardAdaptor;
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
import java.util.Calendar;
import java.util.List;

public class History extends AppCompatActivity {

    RecyclerView recyclerview_history;
    List<Account_Statement_Item>account_statement_items;
    Account_Statemnt_CardAdaptor account_statemnt_cardAdaptor;
    ProgressDialog dialog;
    String number="";

    String stype="";
    String searchkey="All";

    private int mYear, mMonth, mDay;

    String username,password;


    AlertDialog alertDialog;
    FloatingActionButton floatactionbutton_filter;


    RadioGroup radiogroup_group;

    TextView textview_icdate,radiobutton_today;

    TextView textview_balance_recieved;

    ImageView imageView_storage;

    String fromdate="",todate="";




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        if (getSupportActionBar()!=null){

            getSupportActionBar().hide();
        }
        floatactionbutton_filter=findViewById(R.id.floatactionbutton_filter);
        floatactionbutton_filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent=new Intent(com.demo.apppay2all.History.this, Search_Recharge.class);
                startActivity(intent);

            }
        });


        username=SharePrefManager.getInstance(com.demo.apppay2all.History.this).mGetUsername();
        password=SharePrefManager.getInstance(com.demo.apppay2all.History.this).getPassword();

        recyclerview_history=findViewById(R.id.recyclerview_history);
        recyclerview_history.setLayoutManager(new LinearLayoutManager(com.demo.apppay2all.History.this));
        account_statement_items=new ArrayList<>();
        account_statemnt_cardAdaptor=new Account_Statemnt_CardAdaptor(com.demo.apppay2all.History.this,account_statement_items);
        recyclerview_history.setAdapter(account_statemnt_cardAdaptor);
        if (DetectConnection.checkInternetConnection(this)){

            getData(BaseURL.BASEURL+"api/reports/recharge-report?api_token="+SharePrefManager.getInstance(com.demo.apppay2all.History.this).mGetApiToken());

        }

        textview_icdate=findViewById(R.id.textview_icdate);
        textview_icdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mShowAlertDialogSearchByDate();
            }
        });


        imageView_storage=findViewById(R.id.imageView_storage);
        imageView_storage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                mShowAlertDialogSearchByStatus();


            }
        });
    }





    private  void  getData(final String sendingurl) {
        class getJSONData extends AsyncTask<String, String, String> {


            HttpURLConnection urlConnection;


            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                dialog=new ProgressDialog(com.demo.apppay2all.History.this);
                dialog.setMessage("Please wait...");
                dialog.show();
                dialog.setCancelable(false);
            }
            @Override
            protected String doInBackground(String... args) {

                StringBuilder result = new StringBuilder();

                try {
                    URL url=new URL(sendingurl);
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

                dialog.dismiss();
                Log.e("response","data "+result);

                if (!result.equals("")) {
                    mShowData(result);
                }

                else {

                    Toast.makeText(com.demo.apppay2all.History.this, "Server Not Responding", Toast.LENGTH_SHORT).show();
                }
            }

        }

        getJSONData getJSONData=new getJSONData();
        getJSONData.execute();

    }

    private void mShowData(String jsoandata){

        try{

            JSONObject jsonObject=new JSONObject(jsoandata);
            JSONArray jsonArray= jsonObject.getJSONArray("reports");
            for (int i=0;i<jsonArray.length();i++){

                JSONObject data=jsonArray.getJSONObject(i);
                Account_Statement_Item item=new Account_Statement_Item();
                item.setId(data.getString("id"));
                item.setDate(data.getString("created_at"));
                item.setProvider(data.getString("provider"));
                item.setNumber(data.getString("number"));
                item.setTxnid(data.getString("txnid"));
                item.setAmount(data.getString("amount"));
                item.setCommisson(data.getString("commisson"));
                item.setTotal_balance(data.getString("total_balance"));
                item.setName(data.getString("name"));
                item.setProviderimage(data.getString("provider_image"));
                item.setStatus(data.getString("status"));
                item.setOl(data.getString("opening_balance"));
                //                      Log.e("data","ol"+jsonObject.getString("opening_balance"));
                account_statement_items.add(item);
                account_statemnt_cardAdaptor.notifyDataSetChanged();
            }
        }

        catch (JSONException e) {
            e.printStackTrace();
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
    protected void mShowAlertDialogSearchByStatus(){
        LayoutInflater layoutInflater=(LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view=layoutInflater.inflate(R.layout.custom_alert_dialouge_2,null);
        RadioButton radiobutton_all=view.findViewById(R.id.radiobutton_all);
        RadioButton radiobutton_success=view.findViewById(R.id.radiobutton_success);
        RadioButton radiobutton_pedning=view.findViewById(R.id.radiobutton_pedning);
        RadioButton radiobutton_failed=view.findViewById(R.id.radiobutton_failed);
        RadioButton radiobutton_disputed=view.findViewById(R.id.radiobutton_disputed);

        if (!stype.equals("")){

            radiobutton_all.setVisibility(View.GONE);
            radiobutton_success.setVisibility(View.GONE);
            radiobutton_pedning.setVisibility(View.GONE);
            radiobutton_failed.setVisibility(View.GONE);
            radiobutton_disputed.setVisibility(View.GONE);

        }


        radiogroup_group=view.findViewById(R.id.radiogroup_group);
        radiogroup_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {

                if(i==R.id.radiobutton_all){
                    mSearch("");

                    alertDialog.dismiss();
                }

                else if(i==R.id.radiobutton_success){
                    mSearch("success");

                    alertDialog.dismiss();
                }

                else if(i==R.id.radiobutton_pedning){

                    mSearch("pending");

                    alertDialog.dismiss();
                }

                else if(i==R.id.radiobutton_failed){

                    mSearch("failure");
                    alertDialog.dismiss();
                }

                else if(i==R.id.radiobutton_disputed){

                    mSearch("disput");
                    alertDialog.dismiss();
                }
                else if(i==R.id.radiobutton_credit){

                    mSearch("credit");
                    alertDialog.dismiss();
                }
                else if(i==R.id.radiobutton_debit){

                    mSearch("debit");
                    alertDialog.dismiss();
                }

            }
        });
        final AlertDialog.Builder builder=new AlertDialog.Builder(com.demo.apppay2all.History.this);
        builder.setView(view);
        alertDialog=builder.create();
        alertDialog.show();

    }
    protected void mShowAlertDialogSearchByDate(){
        LayoutInflater layoutInflater=(LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view=layoutInflater.inflate(R.layout.custom_alert_dialouge_1,null);
        radiogroup_group=view.findViewById(R.id.radiogroup_group);
        radiogroup_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {


                if(i==R.id.radiobutton_today){
//                     mSearch("credit");

                    Calendar calendar=Calendar.getInstance();
                    int day=calendar.get(Calendar.DAY_OF_MONTH);
                    int month=calendar.get(Calendar.MONTH);
                    int year=calendar.get(Calendar.YEAR);
                    fromdate= year+"-"+month+"-"+day;
                    todate=fromdate;

//                    mGetStatment(username,password,"search");
                    alertDialog.dismiss();
                }
                else if(i==R.id.radiobutton_yesterday){

                    Calendar calendar=Calendar.getInstance();

                    int to_day=calendar.get(Calendar.DAY_OF_MONTH);
                    int to_month=calendar.get(Calendar.MONTH);
                    int to_year=calendar.get(Calendar.YEAR);

                    todate=to_year+"-"+to_month+"-"+to_day;

                    calendar.add(Calendar.DAY_OF_MONTH, -1);
                    int day=calendar.get(Calendar.DAY_OF_MONTH);
                    int month=calendar.get(Calendar.MONTH);
                    int year=calendar.get(Calendar.YEAR);
                    fromdate= year+"-"+month+"-"+day;

//                    mGetStatment(username,password,"search");

                    alertDialog.dismiss();
                }

                else if(i==R.id.radiobutton_1weak){

                    Calendar calendar=Calendar.getInstance();

                    int to_day=calendar.get(Calendar.DAY_OF_MONTH);
                    int to_month=calendar.get(Calendar.MONTH);
                    int to_year=calendar.get(Calendar.YEAR);

                    todate=to_year+"-"+to_month+"-"+to_day;

                    calendar.add(Calendar.DAY_OF_MONTH, -7);
                    int day=calendar.get(Calendar.DAY_OF_MONTH);
                    int month=calendar.get(Calendar.MONTH);
                    int year=calendar.get(Calendar.YEAR);
                    fromdate= year+"-"+month+"-"+day;

//                    mGetStatment(username,password,"search");


                    alertDialog.dismiss();
                }

                else if(i==R.id.radiobutton_1month){

                    Calendar calendar=Calendar.getInstance();

                    int to_day=calendar.get(Calendar.DAY_OF_MONTH);
                    int to_month=calendar.get(Calendar.MONTH);
                    int to_year=calendar.get(Calendar.YEAR);

                    todate=to_year+"-"+to_month+"-"+to_day;

                    calendar.add(Calendar.MONTH, -1);
                    int day=calendar.get(Calendar.DAY_OF_MONTH);
                    int month=calendar.get(Calendar.MONTH);
                    int year=calendar.get(Calendar.YEAR);
                    fromdate= year+"-"+month+"-"+day;

//                    mGetStatment(username,password,"search");

                    alertDialog.dismiss();
                }
                else if(i==R.id.radiobutton_all_date){

//                    mGetStatment(username,password,"get");

                    alertDialog.dismiss();
                }

            }
        });
        final AlertDialog.Builder builder=new AlertDialog.Builder(com.demo.apppay2all.History.this);
        builder.setView(view);
        alertDialog=builder.create();
        alertDialog.show();


    }
    protected void mSearch(String newText){

        if (account_statement_items!=null) {
            List<Account_Statement_Item> temp = new ArrayList();
            for (Account_Statement_Item d : account_statement_items) {
                //or use .equal(text) with you want equal match
                //use .toLowerCase() for better matches
                if (d.getStatus().toLowerCase().contains(newText.toLowerCase())) {
                    temp.add(d);
                }
            }
            //update recyclerview
            account_statemnt_cardAdaptor.UpdateList(temp);
            Log.e("key","="+newText);
        }
    }

    public void mSubmitComplaint(String username, String password, String recharge_id, String reason_id, String message){
        String method="POST";
        String sendingurl=BaseURL.BASEURL+"application/v1/complaint-book";
        Uri.Builder builder=new Uri.Builder();
        builder.appendQueryParameter("username",username);
        builder.appendQueryParameter("password",password);
        builder.appendQueryParameter("recharge_id",recharge_id);
        builder.appendQueryParameter("reason_id",reason_id);
        builder.appendQueryParameter("message",message);


        CallResAPIPOSTMethod callResAPIPOSTMethod=new CallResAPIPOSTMethod(com.demo.apppay2all.History.this,builder,sendingurl,true,method) {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                dialog=new ProgressDialog(com.demo.apppay2all.History.this);
                dialog.setMessage("Please Wait...");
                dialog.show();
                dialog.setCancelable(false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                dialog.dismiss();
                if (!s.equals("")){
                    String status="",message="";
                    try {

                        JSONObject jsonObject=new JSONObject(s);
                        if (jsonObject.has("status")){
                            status=jsonObject.getString("status");
                        }
                        if (jsonObject.has("message")){
                            message=jsonObject.getString("message");
                        }

                        if (!status.equals("")){

                            Intent intent=new Intent(com.demo.apppay2all.History.this,Review_Activity.class);
                            intent.putExtra("status",status);
                            intent.putExtra("message",message);
                            intent.putExtra("activity","complaint");
                            startActivity(intent);
                        }

                        else {
                            Toast.makeText(com.demo.apppay2all.History.this, "Something Went Wrong", Toast.LENGTH_SHORT).show();
                        }

                    }

                    catch (JSONException e){
                        e.printStackTrace();
                    }
                }
                else {
                    Toast.makeText(com.demo.apppay2all.History.this, "Server Not Responding", Toast.LENGTH_SHORT).show();
                }
            }
        };

        callResAPIPOSTMethod.execute();
    }


}
