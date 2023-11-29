package com.demo.apppay2all.AccountValidationReportDetails;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.demo.apppay2all.BaseURL.BaseURL;
import com.demo.apppay2all.CallResAPIGetMethod;
import com.demo.apppay2all.DetectConnection;
import com.demo.apppay2all.R;
import com.demo.apppay2all.SharePrefManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class AccountValidationReport extends AppCompatActivity {

    RecyclerView recyclerview_account_validation;
    List<AccountValidationItem> accountValidationItems;
    AccountValidationCardAdapter accountValidationCardAdapter;

    ProgressDialog dialog;

    TextView tv_message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_validation_report);

        if (getSupportActionBar()!=null)
        {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        tv_message=findViewById(R.id.tv_message);

        recyclerview_account_validation=findViewById(R.id.recyclerview_account_validation);
        recyclerview_account_validation.setLayoutManager(new LinearLayoutManager(AccountValidationReport.this));

        accountValidationItems=new ArrayList<>();
        accountValidationCardAdapter=new AccountValidationCardAdapter(AccountValidationReport.this,accountValidationItems);
        recyclerview_account_validation.setAdapter(accountValidationCardAdapter);

        if (DetectConnection.checkInternetConnection(AccountValidationReport.this))
        {
            mGetData();
        }
        else
        {
            Toast.makeText(this, "No internet connection", Toast.LENGTH_SHORT).show();
        }

    }

    @SuppressLint("StaticFieldLeak")
    protected void mGetData()
    {
        String sending_url= BaseURL.BASEURL_B2C+"api/reports/account-verification?api_token="+ SharePrefManager.getInstance(AccountValidationReport.this).mGetApiToken();
//        String sending_url= BaseURL.BASEURL_B2C+"api/reports/account-verification?api_token=EOmp8MyuCGyHC5avFkYqBXUqp9nIg1R0EkKtHxM6olO8C6VBBdT4lD2ijB5G";
        new CallResAPIGetMethod(AccountValidationReport.this,sending_url)
        {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                dialog=new ProgressDialog(AccountValidationReport.this);
                dialog.setMessage("Please wait...");
                dialog.setCancelable(false);
                dialog.show();
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                dialog.dismiss();

                Log.e("response","data "+s);

                if (!s.equals(""))
                {
                 try{
                     JSONObject jsonObject=new JSONObject(s);
                     JSONArray jsonArray=jsonObject.getJSONArray("reports");
                     for (int i=0; i<jsonArray.length(); i++)
                     {
                         JSONObject data=jsonArray.getJSONObject(i);
                         AccountValidationItem item=new AccountValidationItem();
                         item.setId(data.getString("id"));
                         item.setCreated_at(data.getString("created_at"));
                         item.setProvider(data.getString("provider"));
                         item.setNumber(data.getString("number"));
                         item.setTxnid(data.getString("txnid"));
                         item.setAmount(data.getString("amount"));
                         item.setProfit(data.getString("profit"));
                         item.setStatus(data.getString("status"));

                         accountValidationItems.add(item);
                         accountValidationCardAdapter.notifyDataSetChanged();

                     }

                     if (accountValidationItems.size()!=0)
                     {

                         recyclerview_account_validation.setVisibility(View.VISIBLE);
                         tv_message.setVisibility(View.GONE);
                     }
                     else
                     {
                         recyclerview_account_validation.setVisibility(View.GONE);
                         tv_message.setVisibility(View.VISIBLE);
                         tv_message.setText("No record found");

                     }
                 }
                 catch (JSONException e)
                 {
                     e.printStackTrace();
                 }

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


                if (accountValidationItems!=null) {
                    List<AccountValidationItem> temp = new ArrayList();
                    for (AccountValidationItem d : accountValidationItems) {
                        //or use .equal(text) with you want equal match
                        //use .toLowerCase() for better matches
                        if (d.getProvider().toLowerCase().contains(newText.toLowerCase()) || d.getTxnid().toLowerCase().contains(newText.toLowerCase()) || d.getProfit().toLowerCase().contains(newText.toLowerCase()) ||
                                d.getCreated_at().contains(newText) || d.getAmount().toLowerCase().contains(newText.toLowerCase()) || d.getNumber().toLowerCase().contains(newText.toLowerCase())) {
                            temp.add(d);
                        }
                    }
                    //update recyclerview
                    accountValidationCardAdapter.UpdateList(temp);
                }
                return true;
            }

        });
    }

}