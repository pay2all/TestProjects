package com.demo.apppay2all.FundRequesDetails;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

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
import java.util.ArrayList;
import java.util.List;

public class Payment_request_reports extends AppCompatActivity {

    RecyclerView recyclerview_itemlist;

    List<Payment_Request_Report_Items> myJSON;

    String username,password;

    SwipeRefreshLayout swiprefresh_money_reports;

    Payment_Request_ReportCardAdapter itemListCard;

    int page=1;

    public static boolean last_array_empty=false;

    boolean swiped_refresh=false;

    TextView tv_message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recharge_reports);

        tv_message=findViewById(R.id.tv_message);
        recyclerview_itemlist=findViewById(R.id.recyclerview_recharge_reports);
        recyclerview_itemlist.setHasFixedSize(true);
        recyclerview_itemlist.setLayoutManager(new LinearLayoutManager(this));
        myJSON=new ArrayList<>();
        itemListCard=new Payment_Request_ReportCardAdapter(Payment_request_reports.this,myJSON);
        recyclerview_itemlist.setAdapter(itemListCard);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


//        getSupportActionBar().setTitle(Html.fromHtml("<font color='"+R.color.colorPrimaryDark+"'>Recharge History</font>"));

        SharedPreferences sharedPreferences=getSharedPreferences("user",0);
        username=sharedPreferences.getString("username","");
        password=sharedPreferences.getString("password","");


        swiprefresh_money_reports= findViewById(R.id.swiprefresh_recharge_reports);
        swiprefresh_money_reports.setColorSchemeResources(R.color.colorPrimaryDark);
        swiprefresh_money_reports.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (DetectConnection.checkInternetConnection(Payment_request_reports.this)) {

                    swiped_refresh=true;
                    page=1;

                    mGetData();
                }
                else {
                    Toast.makeText(Payment_request_reports.this, "No Connection", Toast.LENGTH_SHORT).show();
                    swiprefresh_money_reports.setRefreshing(false);
                }
            }
        });
//        textview_search_message= findViewById(R.id.textview_recharge_reports_message);

        if (DetectConnection.checkInternetConnection(Payment_request_reports.this)) {
            mGetData();
        }
        else {
            Toast.makeText(Payment_request_reports.this, "No Connection", Toast.LENGTH_SHORT).show();
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

                if (!myJSON.equals("")) {
                    List<Payment_Request_Report_Items> temp = new ArrayList();
                    for (Payment_Request_Report_Items d : myJSON) {
                        //or use .equal(text) with you want equal match
                        //use .toLowerCase() for better matches
                        if (d.getId().toLowerCase().contains(newText.toLowerCase()) || d.getRefrenceno().toLowerCase().contains(newText.toLowerCase()) ||
                                d.getDate().contains(newText) || d.getBank().toLowerCase().contains(newText.toLowerCase())) {
                            temp.add(d);
                        }
                    }

                    //update recyclerview
                    itemListCard.UpdateList(temp);
                }
                    return true;

            }

        });
    }


    public void mCallNextPage() {
        page+=1;
        if (DetectConnection.checkInternetConnection(Payment_request_reports.this)) {
            mGetData();
        } else {
            Toast.makeText(Payment_request_reports.this, "No Connection", Toast.LENGTH_SHORT).show();
        }
    }


    private  void  mGetData() {
        class getJSONData extends AsyncTask<String, String, String> {


            HttpURLConnection urlConnection;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();

                swiprefresh_money_reports.setRefreshing(true);
            }

            @Override
            protected String doInBackground(String... args) {

                StringBuilder result = new StringBuilder();

                try {
//                    URL url = new URL(BaseURL.BASEURL + "application/v1/fund-request-report?username="+username+"&password="+password+"&page="+page);
                    URL url = new URL(BaseURL.BASEURL_B2C +"api/fund-request/request-report?api_token="+ SharePrefManager.getInstance(Payment_request_reports.this).mGetApiToken());
//                    URL url = new URL(BaseURL.BASEURL_B2C +"api/fund-request/request-report?api_token=EOmp8MyuCGyHC5avFkYqBXUqp9nIg1R0EkKtHxM6olO8C6VBBdT4lD2ijB5G");
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
                Log.e("response","Response : "+result);

                if (swiprefresh_money_reports.isRefreshing())
                {
                    swiprefresh_money_reports.setRefreshing(false);
                }

                if (!result.equals(""))
                {
                    mShowTransactionReports(result);
                }
                else
                {
                }

            }
        }

        getJSONData getJSONData=new getJSONData();
        getJSONData.execute();
    }

    protected void mShowTransactionReports(final String finalJSON)
    {
        try {

            JSONObject jsonObject = new JSONObject(finalJSON);
            JSONArray jsonArray = jsonObject.getJSONArray("reports");

            if (swiped_refresh) {
                myJSON.clear();
            }
//                Log.e("myJSON",finalJSON);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject data = jsonArray.getJSONObject(i);

                String id="";
                String date="";
                String method="";
                String bank="";
                String refrenceno="";
                String amount="";
                String deposite_date="";
                String status="";
                String remark="";

                id=data.getString("id");

//                    JSONObject jsonObject=data.getJSONObject("created_at");

                date=data.getString("created_at");
                method=data.getString("payment_type");
                bank=data.getString("bank_name");
                refrenceno=data.getString("bankref");
                amount=data.getString("amount");
                deposite_date=data.getString("payment_date");
                remark=data.getString("bankref");

                status=data.getString("status");

                Payment_Request_Report_Items money_transfer_items = new Payment_Request_Report_Items();

                money_transfer_items.setId(id);
                money_transfer_items.setDate(date);
                money_transfer_items.setRemark(remark);
                money_transfer_items.setBank(bank);
                money_transfer_items.setMethod(method);
                money_transfer_items.setRefrenceno(refrenceno);
                money_transfer_items.setAmount(amount);
                money_transfer_items.setDeposit_date(deposite_date);
                money_transfer_items.setStatus(status);


                myJSON.add(money_transfer_items);
                itemListCard.notifyDataSetChanged();
            }

            if (myJSON.size()==0)
            {
                tv_message.setVisibility(View.VISIBLE);
                tv_message.setText("Record not found");
                recyclerview_itemlist.setVisibility(View.GONE);
            }

            if (jsonArray.length() != 0) {
                last_array_empty = false;
            } else {
                last_array_empty = true;
            }
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
    }
}