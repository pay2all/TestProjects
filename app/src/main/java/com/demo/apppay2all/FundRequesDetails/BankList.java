package com.demo.apppay2all.FundRequesDetails;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

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

import java.util.ArrayList;
import java.util.List;

public class BankList extends AppCompatActivity {


    RecyclerView recyclerview_bank_list;
    List<BankListItems> bankListItems;
    BankLIstCardAdapter bankLIstCardAdapter;

    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bank__details);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        recyclerview_bank_list=findViewById(R.id.recyclerview_bank_list);
        recyclerview_bank_list.setLayoutManager(new LinearLayoutManager(BankList.this));
        bankListItems=new ArrayList<>();
        bankLIstCardAdapter=new BankLIstCardAdapter(BankList.this,bankListItems);
        recyclerview_bank_list.setAdapter(bankLIstCardAdapter);

//        ll_bankdetails=findViewById(R.id.ll_bankdetails);

        if (DetectConnection.checkInternetConnection(BankList.this))
        {
            mGetBankLIst();
        }
        else
        {
            Toast.makeText(this, "No internet connection", Toast.LENGTH_SHORT).show();
        }

    }

    @SuppressLint("StaticFieldLeak")
    protected void mGetBankLIst()
    {
//        String sending_url= BaseURL.BASEURL_B2C+ "api/fund-request/bank-list?api_token=EOmp8MyuCGyHC5avFkYqBXUqp9nIg1R0EkKtHxM6olO8C6VBBdT4lD2ijB5G";
        String sending_url= BaseURL.BASEURL_B2C+ "api/fund-request/bank-list?api_token="+ SharePrefManager.getInstance(BankList.this).mGetApiToken();
        new CallResAPIGetMethod(BankList.this,sending_url)
        {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();

                dialog=new ProgressDialog(BankList.this);
                dialog.setMessage("Please wait...");
                dialog.setCancelable(true);
                dialog.show();
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                dialog.dismiss();

                Log.e("response","data "+s);

                if (!s.equals(""))
                {
                    String status="",message="";
                    try{
                        JSONObject jsonObject=new JSONObject(s);
                        if (jsonObject.has("status"))
                        {
                            status=jsonObject.getString("status");
                        }
                        if (jsonObject.has("message"))
                        {
                            message=jsonObject.getString("message");
                        }

                        if (status.equalsIgnoreCase("success"))
                        {
                            JSONArray jsonArray=jsonObject.getJSONArray("banks");
                            for (int i=0; i<jsonArray.length(); i++)
                            {
                                JSONObject data=jsonArray.getJSONObject(i);
                                BankListItems items=new BankListItems();
                                items.setBankdetail_id(data.getString("bankdetail_id"));
                                items.setBank_name(data.getString("bank_name"));
                                items.setAccount_number(data.getString("account_number"));
                                items.setIfsc_code(data.getString("ifsc_code"));
                                items.setBranch(data.getString("branch"));


                                bankListItems.add(items);
                                bankLIstCardAdapter.notifyDataSetChanged();
                            }
                        }
                        else if (!message.equals(""))
                        {
                            Toast.makeText(BankList.this, message, Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            Toast.makeText(BankList.this, "Something went wrong, please try again later", Toast.LENGTH_SHORT).show();
                        }
                    }
                    catch (JSONException e)
                    {
                        e.printStackTrace();
                    }
                }
                else
                {
                    Toast.makeText(BankList.this, "Unable to get Detail please try again later", Toast.LENGTH_SHORT).show();
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
}
