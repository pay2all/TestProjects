package com.demo.apppay2all.DesputDetails;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.demo.apppay2all.BaseURL.BaseURL;
import com.demo.apppay2all.CallResAPIPOSTMethod;
import com.demo.apppay2all.DetectConnection;
import com.demo.apppay2all.R;
import com.demo.apppay2all.SharePrefManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class DisputeActivity extends AppCompatActivity {

    ProgressDialog dialog;

    RecyclerView recyclerview_dispute;
    List<DisputeItems> disputeItems;
    DisputeCardAdapter disputeCardAdapter;

    String type="";



    TextView tv_message;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_desput);

        if (getIntent().hasExtra("type"))
        {
            type=getIntent().getStringExtra("type");
        }

        if (getSupportActionBar()!=null)
        {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

            if (!type.equals(""))
            {
                if (type.equalsIgnoreCase("pending"))
                {
                    getSupportActionBar().setTitle("Pending Complaint");
                }
                else
                {
                    getSupportActionBar().setTitle("Solved Complaint");
                }
            }
        }

        tv_message=findViewById(R.id.tv_message);
        recyclerview_dispute=findViewById(R.id.recyclerview_dispute);
        recyclerview_dispute.setLayoutManager(new LinearLayoutManager(DisputeActivity.this));
        disputeItems=new ArrayList<>();
        disputeCardAdapter=new DisputeCardAdapter(DisputeActivity.this,disputeItems);
        recyclerview_dispute.setAdapter(disputeCardAdapter);

        if (DetectConnection.checkInternetConnection(DisputeActivity.this))
        {
            mGetPendingDispute();
        }
        else
        {
            Toast.makeText(this, "No internet connection", Toast.LENGTH_SHORT).show();
        }
    }

    @SuppressLint("StaticFieldLeak")
    protected void mGetPendingDispute()
    {
        String sending_url= BaseURL.BASEURL_B2C+ "api/dispute/pending-dispute";
        if (!type.equalsIgnoreCase("pending"))
        {
            sending_url=BaseURL.BASEURL_B2C+"api/dispute/solve-dispute";
        }
        Uri.Builder builder=new Uri.Builder();

        Log.e("sending ","url "+sending_url);
        builder.appendQueryParameter("api_token", SharePrefManager.getInstance(DisputeActivity.this).mGetApiToken());
//        builder.appendQueryParameter("api_token", "EOmp8MyuCGyHC5avFkYqBXUqp9nIg1R0EkKtHxM6olO8C6VBBdT4lD2ijB5G");
        new CallResAPIPOSTMethod(DisputeActivity.this,builder,sending_url,true,"POST")
        {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                dialog=new ProgressDialog(DisputeActivity.this);
                dialog.setMessage("Please wait...");
                dialog.show();
                dialog.setCancelable(false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                dialog.dismiss();
                Log.e("pending","dispute "+s);

                if (!s.equals(""))
                {
                    String status="",message="";
                    try
                    {
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
                            JSONArray jsonArray=jsonObject.getJSONArray("reports");
                            for (int i=0; i<jsonArray.length(); i++)
                            {
                                JSONObject data=jsonArray.getJSONObject(i);
                                DisputeItems items=new DisputeItems();
                                items.setTicket_id(data.getString("ticket_id"));
                                items.setUser(data.getString("user"));
                                items.setDate(data.getString("date"));
                                items.setProvider(data.getString("provider"));
                                items.setNumber(data.getString("number"));
                                items.setReason(data.getString("reason"));
                                items.setMessage(data.getString("message"));
                                items.setStatus(data.getString("status"));
                                items.setActivity(type);

                                disputeItems.add(items);
                                disputeCardAdapter.notifyDataSetChanged();
                            }

                            if (disputeItems.size()==0)
                            {
                                tv_message.setText("Record not found");
                                tv_message.setVisibility(View.VISIBLE);
                                recyclerview_dispute.setVisibility(View.GONE);
                            }
                        }
                    }
                    catch (JSONException e)
                    {
                        e.printStackTrace();
                    }
                }
                else
                {
                    Toast.makeText(DisputeActivity.this, "server not responding", Toast.LENGTH_SHORT).show();
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
}