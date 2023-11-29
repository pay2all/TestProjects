package com.demo.apppay2all.Statment;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.demo.apppay2all.BaseURL.BaseURL;
import com.demo.apppay2all.DetectConnection;
import com.demo.apppay2all.R;
import com.demo.apppay2all.Search_Recharge;
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


public class Account_Statment extends AppCompatActivity {

    RecyclerView recyclerview_account_statment;
    List <Account_Statement_Item>account_statement_items;
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
        setContentView(R.layout.activity_account__statment);
        getSupportActionBar().hide();

        floatactionbutton_filter=findViewById(R.id.floatactionbutton_filter);
        floatactionbutton_filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent=new Intent(Account_Statment.this, Search_Recharge.class);
                startActivity(intent);

            }
        });







        textview_balance_recieved=findViewById(R.id.textview_balance_recieved);




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

        recyclerview_account_statment=findViewById(R.id.recyclerview_report);

        recyclerview_account_statment.setLayoutManager(new LinearLayoutManager(Account_Statment.this));
        account_statement_items=new ArrayList<>();
        account_statemnt_cardAdaptor=new Account_Statemnt_CardAdaptor(Account_Statment.this,account_statement_items);
        recyclerview_account_statment.setAdapter(account_statemnt_cardAdaptor);

        SharedPreferences sharedPreferences=getSharedPreferences("user",0);
        username=sharedPreferences.getString("username","");
        password=sharedPreferences.getString("password","");

        if (getIntent().hasExtra("type")){

            stype=getIntent().getStringExtra("type");


        }

        if (!stype.equals("")){
            if (stype.equals("Credit")){
                textview_balance_recieved.setText("Payment Details");
            }

        }

        if (!stype.equals("")){

            textview_icdate.setVisibility(View.GONE);


        }

        if (DetectConnection.checkInternetConnection(Account_Statment.this)){
            mGetStatment(username,password,"get");
        }
        else {

            Toast.makeText(this, "No Internet Connection", Toast.LENGTH_SHORT).show();
        }

    }

    private  void  mGetStatment(final  String username, final String password, final String type) {
        class getJSONData extends AsyncTask<String, String, String> {


            HttpURLConnection urlConnection;


            String sending_url="";

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                dialog=new ProgressDialog(Account_Statment.this);
                dialog.setMessage("Please wait...");
                dialog.show();
                dialog.setCancelable(false);

                if (type.equalsIgnoreCase("get"))
                {
                   sending_url=BaseURL.BASEURL+ "application/v1/payment-report?username=9762891294&password=nil1nil1@@";
                }
                else
                {
                    sending_url= BaseURL.BASEURL+"api/v1/account-statement-search?username="+username+"&password="+password+"&fromdate="+fromdate+"&todate="+todate+"&status_id=0&number="+number;

                }

            }
            @Override
            protected String doInBackground(String... args) {

                StringBuilder result = new StringBuilder();

                try {
                    URL url = new URL(sending_url);

                    Log.e("sending","data : "+sending_url);

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


                Log.e("response ","data"+result);
                if (result.equals("")){
                    Toast.makeText(Account_Statment.this, "Server Not Responding", Toast.LENGTH_SHORT).show();
                }

                else {


                    try {


                        JSONObject jsonObject1=new JSONObject(result);
                        JSONArray jsonArray=jsonObject1.getJSONArray("reports");

                        account_statement_items.clear();
                        account_statemnt_cardAdaptor.notifyDataSetChanged();

                        for(int i=0;i<jsonArray.length();i++) {
                            Account_Statement_Item item = new Account_Statement_Item();
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            if (stype.equals("Credit")) {

                                if (!jsonObject.getString("status").equals("")) {


                                    if (jsonObject.getString("status").equalsIgnoreCase("Credit") || jsonObject.getString("status").equalsIgnoreCase("Debit")) {

                                        item.setId(jsonObject.getString("id"));
                                        item.setDate(jsonObject.getString("date"));
                                        item.setProvider(jsonObject.getString("provider"));
                                        item.setNumber(jsonObject.getString("number"));
                                        item.setTxnid(jsonObject.getString("txnid"));
                                        item.setAmount(jsonObject.getString("amount"));
                                        item.setCommisson(jsonObject.getString("commisson"));
                                        item.setTotal_balance(jsonObject.getString("total_balance"));
                                        item.setName(jsonObject.getString("name"));
                                        item.setProviderimage(jsonObject.getString("provider_image"));
                                        item.setStatus(jsonObject.getString("status"));
                                        item.setOl(jsonObject.getString("opening_balance"));

                                        if (jsonObject.has("status_icon")) {
                                            item.setStatus_icon(jsonObject.getString("status_icon"));
                                        }
                                        //                      Log.e("data","ol"+jsonObject.getString("opening_balance"));
                                        account_statement_items.add(item);
                                        account_statemnt_cardAdaptor.notifyDataSetChanged();
                                    }
                                }


                            }

                            else if (!type.equals("get")){

                                if (searchkey.equals("All")){

                                    item.setId(jsonObject.getString("id"));
                                    item.setDate(jsonObject.getString("date"));
                                    item.setProvider(jsonObject.getString("provider"));
                                    item.setNumber(jsonObject.getString("number"));
                                    item.setTxnid(jsonObject.getString("txnid"));
                                    item.setAmount(jsonObject.getString("amount"));
                                    item.setCommisson(jsonObject.getString("commisson"));
                                    item.setTotal_balance(jsonObject.getString("total_balance"));
                                    item.setName(jsonObject.getString("name"));
                                    item.setProviderimage(jsonObject.getString("provider_image"));
                                    item.setStatus(jsonObject.getString("status"));
                                    item.setOl(jsonObject.getString("opening_balance"));
                                    //                      Log.e("data","ol"+jsonObject.getString("opening_balance"));
                                    account_statement_items.add(item);
                                    account_statemnt_cardAdaptor.notifyDataSetChanged();
                                }

                                else {
                                    if (jsonObject.getString("status").equalsIgnoreCase(searchkey)){
                                        item.setId(jsonObject.getString("id"));
                                        item.setDate(jsonObject.getString("date"));
                                        item.setProvider(jsonObject.getString("provider"));
                                        item.setNumber(jsonObject.getString("number"));
                                        item.setTxnid(jsonObject.getString("txnid"));
                                        item.setAmount(jsonObject.getString("amount"));
                                        item.setCommisson(jsonObject.getString("commisson"));
                                        item.setTotal_balance(jsonObject.getString("total_balance"));
                                        item.setName(jsonObject.getString("name"));
                                        item.setProviderimage(jsonObject.getString("provider_image"));
                                        item.setStatus(jsonObject.getString("status"));
                                        item.setOl(jsonObject.getString("opening_balance"));
                                        //                      Log.e("data","ol"+jsonObject.getString("opening_balance"));
                                        account_statement_items.add(item);
                                        account_statemnt_cardAdaptor.notifyDataSetChanged();
                                    }
                                }
                            }

                            else {
                                item.setId(jsonObject.getString("id"));
                                item.setDate(jsonObject.getString("date"));
                                item.setProvider(jsonObject.getString("provider"));
                                item.setNumber(jsonObject.getString("number"));
                                item.setTxnid(jsonObject.getString("txnid"));
                                item.setAmount(jsonObject.getString("amount"));
                                item.setCommisson(jsonObject.getString("commisson"));
                                item.setTotal_balance(jsonObject.getString("total_balance"));
                                item.setName(jsonObject.getString("name"));
                                item.setProviderimage(jsonObject.getString("provider_image"));
                                item.setStatus(jsonObject.getString("status"));
                                item.setOl(jsonObject.getString("opening_balance"));
                                //                      Log.e("data","ol"+jsonObject.getString("opening_balance"));
                                account_statement_items.add(item);
                                account_statemnt_cardAdaptor.notifyDataSetChanged();

                            }
                        }
                    }

                    catch (JSONException e){
                        e.printStackTrace();
                    }

                }

            }
        }

        getJSONData getJSONData=new getJSONData();
        getJSONData.execute();
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

                     mGetStatment(username,password,"search");
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

                     mGetStatment(username,password,"search");

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

                     mGetStatment(username,password,"search");


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

                     mGetStatment(username,password,"search");

                    alertDialog.dismiss();
                }
                else if(i==R.id.radiobutton_all_date){

                     mGetStatment(username,password,"get");

                    alertDialog.dismiss();
                }

            }
        });
        final AlertDialog.Builder builder=new AlertDialog.Builder(Account_Statment.this);
        builder.setView(view);
        alertDialog=builder.create();
        alertDialog.show();


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
        final AlertDialog.Builder builder=new AlertDialog.Builder(Account_Statment.this);
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

    public void mShowsearchdialoge(){

        LayoutInflater layoutInflater=(LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view=layoutInflater.inflate(R.layout.customalert_action_bar_search,null);

        ImageView imageview_cut=view.findViewById(R.id.imageview_cut);
        imageview_cut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });

        final EditText edittext_customernumber=view.findViewById(R.id.edittext_customernumber);
        EditText edittext_operator=view.findViewById(R.id.edittext_operator);
        final TextView edittext_fromdate=view.findViewById(R.id.edittext_fromdate);
        final TextView textview_all=view.findViewById(R.id.textview_all);

        final RelativeLayout rl_all=view.findViewById(R.id.rl_all);
        rl_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popupMenu=new PopupMenu(Account_Statment.this,rl_all);
                if (stype.equals("Credit")){
                    popupMenu.getMenu().add("Credit");
                    popupMenu.getMenu().add("Debit");
                }

                else {
                    popupMenu.getMenu().add("All");
                    popupMenu.getMenu().add("Company Credit");
                    popupMenu.getMenu().add("Wallet credit");
                    popupMenu.getMenu().add("Commission credit");
                    popupMenu.getMenu().add("Credit");
                    popupMenu.getMenu().add("Debit");
                }

                popupMenu.show();
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        searchkey=menuItem.getTitle().toString();
                        textview_all.setText(searchkey);
                        return true;

                    }
                });
            }
        });
        edittext_fromdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);


                DatePickerDialog datePickerDialog = new DatePickerDialog(Account_Statment.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                 fromdate= year+ "-" + (monthOfYear + 1) + "-" + dayOfMonth;
                                 edittext_fromdate.setText(fromdate);
                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
                datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
            }
        });
        final TextView edittext_tilldate=view.findViewById(R.id.edittext_tilldate);
        edittext_tilldate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);


                DatePickerDialog datePickerDialog = new DatePickerDialog(Account_Statment.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                todate= year+ "-" + (monthOfYear + 1) + "-" + dayOfMonth;
                                edittext_tilldate.setText(todate);
                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
                datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
            }

        });
        Button  button_search=view.findViewById(R.id.button_search);
        button_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (fromdate.equals("")){

                    Toast.makeText(Account_Statment.this, "Please Select From Date", Toast.LENGTH_SHORT).show();
                }

                else if (todate.equals("")){

                    Toast.makeText(Account_Statment.this, "Please Select Till Date", Toast.LENGTH_SHORT).show();

                }

                else {
                    number=edittext_customernumber.getText().toString();
                    mGetStatment(username,password,"search");
                    alertDialog.dismiss();
                }
            }
        });








        final AlertDialog.Builder builder=new AlertDialog.Builder(Account_Statment.this);
        builder.setView(view);
        alertDialog=builder.create();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.show();


    }
}