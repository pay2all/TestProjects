package com.demo.apppay2all.MoneyTransferDetails;

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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.demo.apppay2all.BaseURL.BaseURL;
import com.demo.apppay2all.DetectConnection;
import com.demo.apppay2all.R;
import com.demo.apppay2all.SharePrefManager;

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
import java.util.List;

public class MoneyDetails extends AppCompatActivity {

    RecyclerView recyclerview_reports;
    List<Money_Detail_Items> report_items;
    Money_Detail_CardAdaptor report_cardAdaptor;

    ProgressDialog dialog;

    String username,password;

    AlertDialog alertDialog;

    RadioGroup radiogroup_group;

    TextView textview_icdate;

    ImageView imageView_storage;

    int page=1,pages=1;

    public static boolean last_array_empty=false;
    boolean isfirst=true;

    private int mYear, mMonth, mDay;

    TextView tv_from_date,tv_to_date;
    String from_date="",to_date="";
    Button button_get;

    LinearLayout ll_date;

    String api_url="";
    String title="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_money_details);

        if (getIntent().hasExtra("url"))
        {
            api_url=getIntent().getStringExtra("url");
        }

        if (getIntent().hasExtra("title"))
        {
            title=getIntent().getStringExtra("title");
        }

        if (getSupportActionBar()!=null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(title);
        }

        ll_date=findViewById(R.id.ll_date);
        ll_date.setVisibility(View.VISIBLE);
        tv_from_date=findViewById(R.id.tv_from_date);
        tv_from_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(MoneyDetails.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                from_date= year+ "-" + (monthOfYear + 1) + "-" + dayOfMonth;
                                tv_from_date.setText(from_date);
                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
                datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
            }
        });

        tv_to_date=findViewById(R.id.tv_to_date);
        tv_to_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(MoneyDetails.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                to_date= year+ "-" + (monthOfYear + 1) + "-" + dayOfMonth;
                                tv_to_date.setText(to_date);
                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
                datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
            }
        });

        Calendar calendar=Calendar.getInstance();
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");

        from_date=sdf.format(calendar.getTime());
        to_date=sdf.format(calendar.getTime());

        tv_from_date.setText(from_date);
        tv_to_date.setText(to_date);

        Log.e("date","Time: " + sdf.format(calendar.getTime()));

        button_get=findViewById(R.id.button_get);
        button_get.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (DetectConnection.checkInternetConnection(MoneyDetails.this)) {
                    if (from_date.equals("")) {
                        Toast.makeText(MoneyDetails.this, "Please select from date", Toast.LENGTH_SHORT).show();
                    } else if (to_date.equals("")) {
                        Toast.makeText(MoneyDetails.this, "Please select to date", Toast.LENGTH_SHORT).show();
                    } else {
                        isfirst=true;
                        mStatment();
                    }
                }
                else
                {
                    Toast.makeText(MoneyDetails.this, "No internet connection", Toast.LENGTH_SHORT).show();
                }
            }
        });

        textview_icdate=findViewById(R.id.textview_icdate);
        textview_icdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ll_date.getVisibility()==View.VISIBLE)
                {
                    ll_date.setVisibility(View.GONE);
                }
                else
                {
                    ll_date.setVisibility(View.VISIBLE);
                }
            }
        });

//        textview_icdate=findViewById(R.id.textview_icdate);
//        textview_icdate.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                mShowAlertDialogSearchByStatus();
//            }
//        });

        imageView_storage=findViewById(R.id.imageView_storage);
        imageView_storage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mShowAlertDialogSearchByStatus();
            }
        });

        recyclerview_reports =findViewById(R.id.recyclerview_report);
        recyclerview_reports.setLayoutManager(new GridLayoutManager(MoneyDetails.this,3));

        recyclerview_reports.setLayoutManager(new LinearLayoutManager(MoneyDetails.this));
        report_items =new ArrayList<>();
        report_cardAdaptor =new com.demo.apppay2all.MoneyTransferDetails.Money_Detail_CardAdaptor(MoneyDetails.this, report_items);
        recyclerview_reports.setAdapter(report_cardAdaptor);

        SharedPreferences sharedPreferences=getSharedPreferences("user",0);
        username=sharedPreferences.getString("username","");
        password=sharedPreferences.getString("password","");

        if (DetectConnection.checkInternetConnection(MoneyDetails.this)){
            mStatment();
        }
        else {
            Toast.makeText(this, "No Internet Connection", Toast.LENGTH_SHORT).show();
        }
    }

    private  void  mStatment() {

        String sending_url=BaseURL.BASEURL_B2C+"api/reports/money-transfer?api_token="+ SharePrefManager.getInstance(MoneyDetails.this).mGetApiToken()+"&fromdate="+from_date+"&todate="+to_date+"&page="+page;
        if (!api_url.equals(""))
        {
            sending_url=api_url+"?api_token="+ SharePrefManager.getInstance(MoneyDetails.this).mGetApiToken()+"&fromdate="+from_date+"&todate="+to_date+"&page="+page;
        }

        String finalSending_url = sending_url;
        class getJSONData extends AsyncTask<String, String, String> {

            HttpURLConnection urlConnection;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                if (isfirst) {
                    dialog=new ProgressDialog(MoneyDetails.this);
                    dialog.setMessage("Please wait...");
                    dialog.show();
                    dialog.setCancelable(false);
                }
            }

            @Override
            protected String doInBackground(String... args) {

                StringBuilder result = new StringBuilder();

                try {
                    URL url = new URL(finalSending_url);
                    Log.e("sending data",url.toString());
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

                if (dialog.isShowing()) {
                    dialog.dismiss();
                    report_items.clear();
                    isfirst=false;
                }

                Log.e("response ","data"+result);
                if (result.equals("")){
                    Toast.makeText(MoneyDetails.this, "Server Not Responding", Toast.LENGTH_SHORT).show();
                }

                else {

                    try {

                        JSONObject jsonObject_report=new JSONObject(result);

                        if (jsonObject_report.has("pages"))
                        {
                            pages=jsonObject_report.getInt("pages");
                        }

                        JSONArray jsonArray=jsonObject_report.getJSONArray("reports");
                        for(int i=0;i<jsonArray.length();i++){
                            com.demo.apppay2all.MoneyTransferDetails.Money_Detail_Items item=new com.demo.apppay2all.MoneyTransferDetails.Money_Detail_Items();
                            JSONObject jsonObject=jsonArray.getJSONObject(i);
                            item.setId(jsonObject.getString("id"));
                            item.setDate(jsonObject.getString("created_at"));
                            item.setProvider(jsonObject.getString("provider"));
                            item.setTxnid(jsonObject.getString("txnid"));
                            item.setAmount(jsonObject.getString("amount"));
                            item.setCommisson(jsonObject.getString("profit"));
                            item.setTotal_balance("");
                            item.setName("");
                            item.setProviderimage("");
                            item.setStatus(jsonObject.getString("status"));
                            item.setNumber(jsonObject.getString("number"));
                            item.setOl("");

                            if (jsonObject.has("remiter_number"))
                            {
                                item.setRemiter_number(jsonObject.getString("remiter_number"));
                            }

                            if (jsonObject.has("bene_name"))
                            {
                                item.setBene_name(jsonObject.getString("bene_name"));
                            }

                            if (jsonObject.has("bank_name"))
                            {
                                item.setBank_name(jsonObject.getString("bank_name"));
                            }

                            report_items.add(item);
                            report_cardAdaptor.notifyDataSetChanged();

                        }

                        if (jsonArray.length() != 0) {
                            last_array_empty = false;
                        } else {
                            last_array_empty = true;
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
        final AlertDialog.Builder builder=new AlertDialog.Builder(MoneyDetails.this);
        builder.setView(view);
        alertDialog=builder.create();
        alertDialog.show();

    }


    protected void mSearch(String newText){

        if (report_items.size()!=0) {
            List<com.demo.apppay2all.MoneyTransferDetails.Money_Detail_Items> temp = new ArrayList();
            for (com.demo.apppay2all.MoneyTransferDetails.Money_Detail_Items d : report_items) {
                //or use .equal(text) with you want equal match
                //use .toLowerCase() for better matches
                if (d.getStatus().toLowerCase().contains(newText.toLowerCase())) {
                    temp.add(d);
                }
            }
            //update recyclerview
            report_cardAdaptor.UpdateList(temp);
            Log.e("key","="+newText);
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
                    List<com.demo.apppay2all.MoneyTransferDetails.Money_Detail_Items> temp = new ArrayList();
                    for (com.demo.apppay2all.MoneyTransferDetails.Money_Detail_Items d : report_items) {
                        //or use .equal(text) with you want equal match
                        //use .toLowerCase() for better matches
                        if (d.getProvider().toLowerCase().contains(newText.toLowerCase()) || d.getNumber().toLowerCase().contains(newText.toLowerCase()) || d.getAmount().toLowerCase().contains(newText.toLowerCase()) ||
                                d.getDate().contains(newText) || d.getStatus().toLowerCase().contains(newText.toLowerCase()) || d.getBene_name().toLowerCase().contains(newText.toLowerCase())) {
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

    public void mCallNextPage() {
        if (pages>page) {
            page += 1;
            if (DetectConnection.checkInternetConnection(this)) {
                mStatment();
            }
            else {
                Toast.makeText(this, "No Connection", Toast.LENGTH_SHORT).show();
            }
        }
    }
}