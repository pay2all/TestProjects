package com.demo.apppay2all;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.demo.apppay2all.BaseURL.BaseURL;
import com.demo.apppay2all.Statment.Account_Statement_Item;
import com.demo.apppay2all.Statment.Account_Statemnt_CardAdaptor;

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
import java.util.Objects;

public class Search_Recharge extends AppCompatActivity implements AdapterView.OnItemSelectedListener {


    String[] country = { "ALL","Success","Pending","Failed","Disputed"};

    ProgressDialog dialog;
    String number="";
    private int mYear, mMonth, mDay;

    String stype="",provider_id="";


    List<Account_Statement_Item> account_statement_items;
    Account_Statemnt_CardAdaptor account_statemnt_cardAdaptor;

    String fromdate="",todate="";

   Button button_search;

    String username,password;


    String searchkey="All";

    RecyclerView recyclerview;

    EditText edittext_customernumber;
    AutoCompleteTextView edittext_operator;
    ArrayList <String> operatorlist=new ArrayList<>();
    ArrayList <String> shortaddlist=new ArrayList<>();

    List<Operators_Items>operators;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search__recharge);
        Objects.requireNonNull(getSupportActionBar()).hide();

        SharedPreferences sharedPreferences=getSharedPreferences("user",0);
        username=sharedPreferences.getString("username","");
        password=sharedPreferences.getString("password","");

        String alldata= SharePrefManager.getInstance(com.demo.apppay2all.Search_Recharge.this).mGetOperators();

        if (alldata.equals(""))
        {
            if (DetectConnection.checkInternetConnection(com.demo.apppay2all.Search_Recharge.this))
            {
                mGetOperators();
            }
        }
        Log.e("oper","ters "+alldata);
        operators=new ArrayList<>();

        try {

            JSONObject jsonObject=new JSONObject(alldata);
            JSONArray jsonArray=jsonObject.getJSONArray("providers");
            for(int i=0;i<jsonArray.length();i++){

                JSONObject data=jsonArray.getJSONObject(i);
                if (data.getString("service_id").equals("1")){

                    Operators_Items items=new Operators_Items();
                    items.setOperator_id(data.getString("provider_id"));
                    items.setOperator_name(data.getString("provider_name"));
                    operators.add(items);
                    operatorlist.add(data.getString("provider_name"));

                }
            }
        }

        catch (JSONException e){
            e.printStackTrace();
        }

        recyclerview=findViewById(R.id.recyclerview);
        recyclerview.setLayoutManager(new LinearLayoutManager(com.demo.apppay2all.Search_Recharge.this));
        account_statement_items=new ArrayList<>();
        account_statemnt_cardAdaptor=new Account_Statemnt_CardAdaptor(com.demo.apppay2all.Search_Recharge.this,account_statement_items);
        recyclerview.setAdapter(account_statemnt_cardAdaptor);


        button_search=findViewById(R.id.button_search);
        ImageView imageview_cut=findViewById(R.id.imageview_cut);
        imageview_cut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

         edittext_customernumber=findViewById(R.id.edittext_customernumber);


         ArrayAdapter<String> adapter = new ArrayAdapter<String>
                (this,android.R.layout.select_dialog_item,operatorlist);

         edittext_operator=findViewById(R.id.edittext_operator);
         edittext_operator.setThreshold(1);
         edittext_operator.setAdapter(adapter);
         edittext_operator.setOnItemClickListener(new AdapterView.OnItemClickListener() {
             @Override
             public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                 for (int j =0; j<operators.size(); j++)
                 {
                     if (operators.get(j).getOperator_name().equalsIgnoreCase(edittext_operator.getAdapter().getItem(i).toString()))
                     {
                         provider_id=operators.get(j).getOperator_id();
                     }
                 }
             }
         });

        final TextView edittext_fromdate=findViewById(R.id.edittext_fromdate);
        final TextView textview_all=findViewById(R.id.textview_all);

        final RelativeLayout rl_all=findViewById(R.id.rl_all);
        rl_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popupMenu=new PopupMenu(Search_Recharge.this,rl_all);

                popupMenu.getMenu().add("All");
                popupMenu.getMenu().add("Success");
                popupMenu.getMenu().add("Failure");
                popupMenu.getMenu().add("Pending");
                popupMenu.getMenu().add("Dispute");
                popupMenu.getMenu().add("Profit");
                popupMenu.getMenu().add("Debit");

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


                DatePickerDialog datePickerDialog = new DatePickerDialog(com.demo.apppay2all.Search_Recharge.this,
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

        final TextView edittext_tilldate=findViewById(R.id.edittext_tilldate);
        edittext_tilldate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(com.demo.apppay2all.Search_Recharge.this,
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
        Button  button_search=findViewById(R.id.button_search);
        button_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (fromdate.equals("")){

                    Toast.makeText(com.demo.apppay2all.Search_Recharge.this, "Please Select From Date", Toast.LENGTH_SHORT).show();
                }

                else if (todate.equals("")){

                    Toast.makeText(com.demo.apppay2all.Search_Recharge.this, "Please Select Till Date", Toast.LENGTH_SHORT).show();

                }
                else {
                    number=edittext_customernumber.getText().toString();
                    mGetStatment(username,password,"search");

                }
            }
        });
    }


    //Performing action onItemSelected and onNothing selected
    @Override
    public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long id) {
        Toast.makeText(getApplicationContext(),country[position] , Toast.LENGTH_LONG).show();
    }
    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub
    }

    private  void  mGetStatment(final  String username, final String password, final String type) {
        class getJSONData extends AsyncTask<String, String, String> {


            HttpURLConnection urlConnection;


            String sending_url="";

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                dialog=new ProgressDialog(com.demo.apppay2all.Search_Recharge.this);
                dialog.setMessage("Please wait...");
                dialog.show();
                dialog.setCancelable(false);

                if (type.equalsIgnoreCase("get"))
                {
                    sending_url= BaseURL.BASEURL_B2C+ "api/reports/all-transaction-report?api_token="+SharePrefManager.getInstance(com.demo.apppay2all.Search_Recharge.this)+"&fromdate=&todate=&status_id=&provider_id=&number=";
                }
                else
                {
                    sending_url= BaseURL.BASEURL_B2C+"api/reports/all-transaction-report?api_token="+SharePrefManager.getInstance(com.demo.apppay2all.Search_Recharge.this).mGetApiToken()+"&fromdate="+fromdate+"&todate="+todate+"&status_id=0&number="+number+"&provider_id="+provider_id;

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
                    Toast.makeText(com.demo.apppay2all.Search_Recharge.this, "Server Not Responding", Toast.LENGTH_SHORT).show();
                }

                else {

                    JSONArray jsonArray;


                    try {

                        if (type.equalsIgnoreCase("get")){
                            jsonArray=new JSONArray(result);

                        }

                        else {

                            JSONObject jsonObject=new JSONObject(result);
                            jsonArray=jsonObject.getJSONArray("reports");
                        }


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
                                        //                      Log.e("data","ol"+jsonObject.getString("opening_balance"));
                                        account_statement_items.add(item);
                                        account_statemnt_cardAdaptor.notifyDataSetChanged();
                                    }
                                }


                            }

                            else if (!type.equals("get")){

                                if (searchkey.equals("All")){

                                    item.setId(jsonObject.getString("id"));
                                    item.setDate(jsonObject.getString("created_at"));
                                    item.setProvider(jsonObject.getString("provider"));
                                    item.setNumber(jsonObject.getString("number"));
                                    item.setTxnid(jsonObject.getString("txnid"));
                                    item.setAmount(jsonObject.getString("amount"));
                                    item.setCommisson(jsonObject.getString("profit"));
                                    item.setTotal_balance(jsonObject.getString("total_balance"));
                                    if (jsonObject.has("name")) {
                                        item.setName(jsonObject.getString("name"));
                                    }
                                    item.setProviderimage(jsonObject.getString("provider_icon"));
                                    item.setStatus(jsonObject.getString("status"));
                                    item.setOl(jsonObject.getString("opening_balance"));
                                    //                      Log.e("data","ol"+jsonObject.getString("opening_balance"));
                                    account_statement_items.add(item);
                                    account_statemnt_cardAdaptor.notifyDataSetChanged();
                                }

                                else {
                                    if (jsonObject.getString("status").equalsIgnoreCase(searchkey)){
                                        item.setId(jsonObject.getString("id"));
                                        item.setDate(jsonObject.getString("created_at"));
                                        item.setProvider(jsonObject.getString("provider"));
                                        item.setNumber(jsonObject.getString("number"));
                                        item.setTxnid(jsonObject.getString("txnid"));
                                        item.setAmount(jsonObject.getString("amount"));
                                        item.setCommisson(jsonObject.getString("profit"));
                                        item.setTotal_balance(jsonObject.getString("total_balance"));
                                        if (jsonObject.has("name")) {
                                            item.setName(jsonObject.getString("name"));
                                        }
                                        item.setProviderimage(jsonObject.getString("provider_icon"));
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
                                item.setDate(jsonObject.getString("created_at"));
                                item.setProvider(jsonObject.getString("provider"));
                                item.setNumber(jsonObject.getString("number"));
                                item.setTxnid(jsonObject.getString("txnid"));
                                item.setAmount(jsonObject.getString("amount"));
                                item.setCommisson(jsonObject.getString("profit"));
                                item.setTotal_balance(jsonObject.getString("total_balance"));
                                if (jsonObject.has("name")) {
                                    item.setName(jsonObject.getString("name"));
                                }
                                item.setProviderimage(jsonObject.getString("provider_icon"));
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
    @SuppressLint("StaticFieldLeak")
    protected void mGetOperators()
    {
        String sending= BaseURL.BASEURL_B2C+"api/application/v1/get-provider";
        Uri.Builder builder=new Uri.Builder();
        builder.appendQueryParameter("api_token", SharePrefManager.getInstance(com.demo.apppay2all.Search_Recharge.this).mGetApiToken());

        new CallResAPIPOSTMethod(com.demo.apppay2all.Search_Recharge.this,builder,sending,true,"POST")
        {
            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                Log.e("response","data "+s);

                SharePrefManager.getInstance(com.demo.apppay2all.Search_Recharge.this).mSaveOperators(s);
                if (!s.equals(""))
                {
                    try {

                        JSONObject jsonObject=new JSONObject(s);
                        JSONArray jsonArray=jsonObject.getJSONArray("providers");
                        for(int i=0;i<jsonArray.length();i++){

                            JSONObject data=jsonArray.getJSONObject(i);
                            if (data.getString("service_id").equals("1")){

                                Operators_Items items=new Operators_Items();
                                items.setOperator_id(data.getString("provider_id"));
                                items.setOperator_name(data.getString("provider_name"));
                                operators.add(items);
                                operatorlist.add(data.getString("provider_name"));

                            }
                        }
                    }

                    catch (JSONException e){
                        e.printStackTrace();
                    }

                }

            }

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }
        }.execute();
    }


}
