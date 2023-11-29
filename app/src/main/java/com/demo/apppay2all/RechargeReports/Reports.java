package com.demo.apppay2all.RechargeReports;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.demo.apppay2all.BaseURL.BaseURL;
import com.demo.apppay2all.CallResAPIGetMethod;
import com.demo.apppay2all.DetectConnection;
import com.demo.apppay2all.R;
import com.demo.apppay2all.SharePrefManager;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class Reports extends AppCompatActivity {

    RecyclerView recyclerview_reports;
    List<Account_Statement_Item> report_items;
    Account_Statemnt_CardAdaptor report_cardAdaptor;

    ProgressDialog dialog;

    String username,password;

    AlertDialog alertDialog;

    RadioGroup radiogroup_group;

    TextView textview_icdate,radiobutton_today;

    ImageView imageView_storage;

    RelativeLayout rl_from,rl_to,rl_status;
    TextView tv_from,tv_to,tv_status;
    EditText ed_number;
    Button bt_search;

    private int mYear, mMonth, mDay;

    String fromdate="",todate="";
    String searchkey="All";


    String number="";

    PopupMenu popupMenu;
    String status_id="";

    String api_url="";
    String title="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reports);


        if (getIntent().hasExtra("url"))
        {
            api_url=getIntent().getStringExtra("url");
        }

        if (getIntent().hasExtra("title"))
        {
            title=getIntent().getStringExtra("title");
        }

        if (getSupportActionBar()!=null)
        {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(title);
        }

        rl_from=findViewById(R.id.rl_from);
        rl_from.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(Reports.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                fromdate= year+ "-" + (monthOfYear + 1) + "-" + dayOfMonth;
                                tv_from.setText(fromdate);
                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
                datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
            }
        });

        rl_to=findViewById(R.id.rl_to);
        rl_to.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(Reports.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                todate= year+ "-" + (monthOfYear + 1) + "-" + dayOfMonth;
                                tv_to.setText(todate);
                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
                datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
            }
        });

        rl_status=findViewById(R.id.rl_status);
        popupMenu=new PopupMenu(Reports.this,rl_status);
        rl_status.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                PopupMenu popupMenu=new PopupMenu(Reports.this,rl_status);

//                popupMenu.getMenu().add("All");
//                popupMenu.getMenu().add("Success");
//                popupMenu.getMenu().add("Failure");
//                popupMenu.getMenu().add("Pending");
//                popupMenu.getMenu().add("Dispute");
//                popupMenu.getMenu().add("Profit");
//                popupMenu.getMenu().add("Debit");


                if (popupMenu.getMenu().size()!=0) {
                    popupMenu.show();
                }

                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {

                        searchkey=menuItem.getTitle().toString();

                        status_id=menuItem.getItemId()+"";

                        tv_status.setText(searchkey);

                        return true;
                    }
                });
            }
        });

        tv_from=findViewById(R.id.tv_from);
        tv_to=findViewById(R.id.tv_to);

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
         fromdate = formatter.format(new Date(System.currentTimeMillis()));
         todate = formatter.format(new Date(System.currentTimeMillis()));

        tv_from.setText(fromdate);
        tv_to.setText(todate);

        tv_status=findViewById(R.id.tv_status);
        ed_number=findViewById(R.id.ed_number);
        bt_search=findViewById(R.id.bt_search);
        bt_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fromdate.equals("")){

                    Toast.makeText(Reports.this, "Please Select From Date", Toast.LENGTH_SHORT).show();
                }

                else if (todate.equals("")){

                    Toast.makeText(Reports.this, "Please Select To Date", Toast.LENGTH_SHORT).show();

                }
                else {
                    number=ed_number.getText().toString();
                    mStatment("search");

                }
            }
        });


        textview_icdate=findViewById(R.id.textview_icdate);
        textview_icdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mShowAlertDialogSearchByStatus();
            }
        });

        imageView_storage=findViewById(R.id.imageView_storage);
        imageView_storage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mShowAlertDialogSearchByStatus();
            }
        });

        recyclerview_reports =findViewById(R.id.recyclerview_report);

        recyclerview_reports.setLayoutManager(new LinearLayoutManager(Reports.this));
        report_items =new ArrayList<>();
        report_cardAdaptor =new Account_Statemnt_CardAdaptor(Reports.this, report_items);
        recyclerview_reports.setAdapter(report_cardAdaptor);

        SharedPreferences sharedPreferences=getSharedPreferences("user",0);
        username=sharedPreferences.getString("username","");
        password=sharedPreferences.getString("password","");

        if (DetectConnection.checkInternetConnection(Reports.this)){
            mStatment("get");

            if (SharePrefManager.getInstance(Reports.this).mGetStatusList().equals("")) {
                mGetStatusId();
            }
            else
            {
                mShowStatus(SharePrefManager.getInstance(Reports.this).mGetStatusList());
            }
        }
        else {

            Toast.makeText(this, "No Internet Connection", Toast.LENGTH_SHORT).show();
        }

    }

    private  void  mStatment(String type) {

        String sending_url=api_url+"?api_token="+ SharePrefManager.getInstance(Reports.this).mGetApiToken();

        if (type.equalsIgnoreCase("search"))
        {
            sending_url=api_url+"?api_token="+ SharePrefManager.getInstance(Reports.this).mGetApiToken()+"&fromdate="+fromdate+"&todate="+todate+"&status_id="+status_id+"&number="+number+"&provider_id=";
        }
        String finalSending_url = sending_url;
        class getJSONData extends AsyncTask<String, String, String> {

            HttpURLConnection urlConnection;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                dialog=new ProgressDialog(Reports.this);
                dialog.setMessage("Please wait...");
                dialog.show();
                dialog.setCancelable(false);
            }
            @Override
            protected String doInBackground(String... args) {

                StringBuilder result = new StringBuilder();

                try {
                    URL url = new URL(finalSending_url);
//                    URL url = new URL(BaseURL.BASEURL_B2C+"api/reports/recharge-report?api_token=EOmp8MyuCGyHC5avFkYqBXUqp9nIg1R0EkKtHxM6olO8C6VBBdT4lD2ijB5G");
                    Log.e("sending data", finalSending_url);
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
                    Toast.makeText(Reports.this, "Server Not Responding", Toast.LENGTH_SHORT).show();
                }

                else {
                    try {
                        JSONObject jsonObject_report=new JSONObject(result);
                        JSONArray jsonArray=jsonObject_report.getJSONArray("reports");

                        if (type.equalsIgnoreCase("search"))
                        {
                            report_items.clear();
                        }

                        for(int i=0;i<jsonArray.length();i++){
                            Account_Statement_Item item=new Account_Statement_Item();
                            JSONObject jsonObject=jsonArray.getJSONObject(i);
                            item.setId(jsonObject.getString("id"));
                            item.setDate(jsonObject.getString("created_at"));
                            item.setProvider(jsonObject.getString("provider"));
//                            item.setName(jsonObject.getString("provider"));
                            item.setNumber(jsonObject.getString("number"));
                            item.setTxnid(jsonObject.getString("txnid"));
                            item.setAmount(jsonObject.getString("amount"));
                            item.setCommisson(jsonObject.getString("profit"));
                            item.setTotal_balance("");
                            item.setName("");
                            item.setProviderimage(jsonObject.getString("provider_icon"));
                            item.setStatus(jsonObject.getString("status"));
                            item.setNumber(jsonObject.getString("number"));
                            item.setOl("");
                            item.setType("recharge");
                            report_items.add(item);
                            report_cardAdaptor.notifyDataSetChanged();
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

    protected void mShowAlertDialogSearchByStatus(){
        LayoutInflater layoutInflater=(LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view=layoutInflater.inflate(R.layout.custom_alert_dialouge_2,null);
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

            }
        });
        final AlertDialog.Builder builder=new AlertDialog.Builder(Reports.this);
        builder.setView(view);
        alertDialog=builder.create();
        alertDialog.show();
    }

    protected void mSearch(String newText){

        if (report_items.size()!=0) {
            List<Account_Statement_Item> temp = new ArrayList();
            for (Account_Statement_Item d : report_items) {
                //or use .equal(text) with you want equal match
                //use .toLowerCase() for better matches
                if (d.getStatus().toLowerCase().contains(newText.toLowerCase())) {
                    temp.add(d);
                }
            }
            //update recyclerview
            report_cardAdaptor.UpdateList(temp);
            Log.e("key","=" +newText);
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId()==android.R.id.home)
        {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_menu, menu);

        MenuItem search = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) search.getActionView();
        search(searchView);
        return true;

    }

    private void search(SearchView searchView) {

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                if (report_items.size()!=0) {
                    List<Account_Statement_Item> temp = new ArrayList();
                    for (Account_Statement_Item d : report_items) {
                        //or use .equal(text) with you want equal match
                        //use .toLowerCase() for better matches
                        if (d.getProvider().toLowerCase().contains(newText.toLowerCase()) || d.getNumber().toLowerCase().contains(newText.toLowerCase()) || d.getAmount().toLowerCase().contains(newText.toLowerCase()) ||
                                d.getDate().contains(newText) || d.getStatus().toLowerCase().contains(newText.toLowerCase()) || d.getId().toLowerCase().contains(newText.toLowerCase())) {
                            temp.add(d);
                        }
                    }
                    //update recyclerview
                    report_cardAdaptor.UpdateList(temp);
                }
                return true;
            }

        });
    }

    @SuppressLint("StaticFieldLeak")
    protected void mGetStatusId()
    {
        String sending_url=BaseURL.BASEURL_B2C+"api/reports/status-type?api_token="+SharePrefManager.getInstance(Reports.this).mGetApiToken();
        new CallResAPIGetMethod(Reports.this,sending_url)
        {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();

            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                Log.e("response","status id "+s);

                try {
                    JSONObject jsonObject=new JSONObject(s);
                    if (jsonObject.has("status"))
                    {
                        JSONArray jsonArray=jsonObject.getJSONArray("status");
                        for (int i=0; i<jsonArray.length(); i++)
                        {
                            JSONObject data=jsonArray.getJSONObject(i);
                            popupMenu.getMenu().add(data.getInt("status_id"),data.getInt("status_id"),data.getInt("status_id"),data.getString("status"));
                        }

                        if (jsonArray.length()>0)
                        {
                            SharePrefManager.getInstance(Reports.this).mSaveStatusList(s);
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

    protected void mShowStatus(String s)
    {
        try {
            JSONObject jsonObject=new JSONObject(s);
            if (jsonObject.has("status"))
            {
                JSONArray jsonArray=jsonObject.getJSONArray("status");
                for (int i=0; i<jsonArray.length(); i++)
                {
                    JSONObject data=jsonArray.getJSONObject(i);
                    popupMenu.getMenu().add(data.getInt("status_id"),data.getInt("status_id"),data.getInt("status_id"),data.getString("status"));
                }
            }
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
    }
}