package com.demo.apppay2all.EarningDetail;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.demo.apppay2all.BaseURL.BaseURL;
import com.demo.apppay2all.CallResAPIGetMethod;
import com.demo.apppay2all.DetectConnection;
import com.demo.apppay2all.R;
import com.demo.apppay2all.SharePrefManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class Earning extends AppCompatActivity {

    ProgressDialog dialog;
    TextView textview_today_sale,textview_total_profit,textview_date_to_date;
    ImageView imageView_perviousdate,imageView_next_date;
    private int mYear, mMonth, mDay;
    String dateaction="from";
    String fromdate="";
    String todate="";
    String username="",password="";
    String changedateaction="";
    Calendar c = Calendar.getInstance();
    RecyclerView recyclerview_operator_list;
    List<EarningItem> earningItems;
    EarningCardAdaptor earningCardAdaptor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_earning);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        SimpleDateFormat sdf =new SimpleDateFormat("yyyy-MM-dd");

        fromdate=sdf.format(c.getTime());
        todate=sdf.format(c.getTime());

        recyclerview_operator_list=findViewById(R.id.recyclerview_operator_list);
        recyclerview_operator_list.setLayoutManager(new LinearLayoutManager(Earning.this));
        earningItems=new ArrayList<>();
        earningCardAdaptor=new EarningCardAdaptor(Earning.this,earningItems);
        recyclerview_operator_list.setAdapter(earningCardAdaptor);




        textview_today_sale=findViewById(R.id.textview_today_sale);
        textview_total_profit=findViewById(R.id.textview_total_profit);
        textview_date_to_date=findViewById(R.id.textview_date_to_date);
        textview_date_to_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mShowDateDialog();
            }
        });




        imageView_perviousdate=findViewById(R.id.imageView_perviousdate);
        imageView_perviousdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changedateaction="pre";

                mChangeDate();
            }
        });

        imageView_next_date=findViewById(R.id.imageView_next_date);
        imageView_next_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changedateaction="next";

                mChangeDate();
            }
        });
        textview_date_to_date.setText(fromdate+" to "+todate);
        username= SharePrefManager.getInstance(Earning.this).mGetUsername();
        password=SharePrefManager.getInstance(Earning.this).getPassword();
        if (DetectConnection.checkInternetConnection(Earning.this)){
            mGetDetail(username,password,fromdate,todate);
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

    protected void mGetDetail(String username, String password, String fromdate, String todate){

        String sendingurl= BaseURL.BASEURL+ "application/v1/operator-report?username="+username+"&password="+password+"&fromdate="+fromdate+"&todate="+todate;
        Log.e("sendingdata",sendingurl);
        CallResAPIGetMethod callResAPIGetMethod=new CallResAPIGetMethod(Earning.this,sendingurl) {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                dialog=new ProgressDialog(Earning.this);
                dialog.setMessage("Please wait...");
                dialog.setCancelable(false);
                dialog.show();
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                dialog.dismiss();
                Log.e("data","response "+s);
                try{

                    String status="",message="";
                    JSONObject jsonObject=new JSONObject(s);
                    if (jsonObject.has("status")){
                        status=jsonObject.getString("status");
                    }
                    if (jsonObject.has("message")){
                        message=jsonObject.getString("message");
                    }

                    if (status.equalsIgnoreCase("success")){
                        earningItems.clear();
                        JSONObject sales=jsonObject.getJSONObject("sales");
                        Log.e("sales","data "+sales);
                        if (sales.has("date")){
                            textview_date_to_date.setText(sales.getString("date"));
                        }

                        if (sales.has("total_sales")){
                            textview_today_sale.setText("\u20B9 "+sales.getString("total_sales"));
                        }

                        if (sales.has("total_profit")){

                            textview_total_profit.setText("\u20B9 "+sales.getString("total_profit"));
                        }

                        JSONArray jsonArray=jsonObject.getJSONArray("report");
                        for (int i=0; i<jsonArray.length(); i++)
                        {
                            JSONObject data=jsonArray.getJSONObject(i);
                            EarningItem items=new EarningItem();
                            items.setProvider_name(data.getString("provider_name"));
                            items.setTotal_amount(data.getString("total_amount"));
                            items.setTotal_profit(data.getString("total_profit"));

                            earningItems.add(items);
                            earningCardAdaptor.notifyDataSetChanged();

                        }


                    }
                }

                catch (JSONException e){
                    e.printStackTrace();
                }
            }
        };
        callResAPIGetMethod.execute();
    }

    protected void mShowDateDialog(){
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);


        DatePickerDialog datePickerDialog = new DatePickerDialog(Earning.this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        if (dateaction.equalsIgnoreCase("from")) {
                            fromdate = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
                            dateaction="to";
                            mShowDateDialog();
                        }




                        else {

                            todate = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
                            textview_date_to_date.setText(fromdate+" to "+todate);

                            if (DetectConnection.checkInternetConnection(Earning.this)) {
                                mGetDetail(username, password, fromdate, todate);
                            }
                        }
                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.show();
        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
    }

    protected void mChangeDate(){

        Calendar calendar=Calendar.getInstance();
        SimpleDateFormat sdf =new SimpleDateFormat("yyyy-MM-dd");
        try {
            if (changedateaction.equalsIgnoreCase("pre")) {
                calendar.setTime(sdf.parse(fromdate));
                calendar.add(Calendar.DATE,-1);

                fromdate=calendar.get(Calendar.YEAR)+"-"+((calendar.get(Calendar.MONTH))+1+"-"+calendar.get(Calendar.DAY_OF_MONTH));
            }

            else {
                calendar.setTime(sdf.parse(todate));
                calendar.add(Calendar.DATE,1);
                todate=calendar.get(Calendar.YEAR)+"-"+((calendar.get(Calendar.MONTH))+1+"-"+calendar.get(Calendar.DAY_OF_MONTH));


            }

            textview_date_to_date.setText(fromdate+" to "+todate);
            if (DetectConnection.checkInternetConnection(Earning.this)){
                mGetDetail(username,password,fromdate,todate);
            }

        }

        catch (ParseException e){
            e.printStackTrace();
        }

    }



}
