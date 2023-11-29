package com.demo.apppay2all.DesputDetails.ViewDisputDetail;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
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

public class ViewDispute extends AppCompatActivity {


    TextView tv_id,tv_txnid,tv_number,tv_amount,tv_provider,tv_txndate,tv_txnstatus,tv_complaint_reason,tv_complaint_status;

    RecyclerView recyclerview_chat;
    List<ChatItem> chatItems;
    ChatCardAdapter chatCardAdapter;

    EditText ed_type_message;
    ImageView iv_send;

    ProgressDialog dialog;

    String id="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_dispute);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (getIntent().hasExtra("id"))
        {
            id=getIntent().getStringExtra("id");
        }

        tv_id=findViewById(R.id.tv_id);
        tv_txnid=findViewById(R.id.tv_txnid);
        tv_number=findViewById(R.id.tv_number);
        tv_amount=findViewById(R.id.tv_amount);
        tv_provider=findViewById(R.id.tv_provider);
        tv_txndate=findViewById(R.id.tv_txndate);
        tv_txnstatus=findViewById(R.id.tv_txnstatus);
        tv_complaint_reason=findViewById(R.id.tv_complaint_reason);
        tv_complaint_status=findViewById(R.id.tv_complaint_status);


        recyclerview_chat=findViewById(R.id.recyclerview_chat);
        recyclerview_chat.setLayoutManager(new LinearLayoutManager(ViewDispute.this));
        chatItems=new ArrayList<>();
        chatCardAdapter=new ChatCardAdapter(ViewDispute.this,chatItems);
        recyclerview_chat.setAdapter(chatCardAdapter);


        ed_type_message=findViewById(R.id.ed_type_message);
        iv_send=findViewById(R.id.iv_send);

        iv_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (DetectConnection.checkInternetConnection(ViewDispute.this))
                {
                    if (ed_type_message.getText().toString().equals(""))
                    {

                    }
                    else
                    {
                        mSendMessage(ed_type_message.getText().toString());
                    }
                }
            }
        });

        if (DetectConnection.checkInternetConnection(ViewDispute.this))
        {
            mGetData(id);
        }
        else
        {
            Toast.makeText(this, "No internet connection", Toast.LENGTH_SHORT).show();
        }
    }

    @SuppressLint("StaticFieldLeak")
    protected void mGetData(String id)
    {
        String sending_url= BaseURL.BASEURL_B2C+ "api/dispute/view-dispute-details";
        Uri.Builder builder=new Uri.Builder();
//        builder.appendQueryParameter("api_token", SharePrefManager.getInstance(ViewDispute.this).mGetApiToken());
        builder.appendQueryParameter("api_token", "EOmp8MyuCGyHC5avFkYqBXUqp9nIg1R0EkKtHxM6olO8C6VBBdT4lD2ijB5G");
        builder.appendQueryParameter("id", id);

        new CallResAPIPOSTMethod(ViewDispute.this,builder,sending_url,true,"POST")
        {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                dialog=new ProgressDialog(ViewDispute.this);
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
                    String status="";
                    try{
                        JSONObject jsonObject=new JSONObject(s);
                        if (jsonObject.has("status"))
                        {
                            status=jsonObject.getString("status");
                        }

                        if (status.equalsIgnoreCase("success"))
                        {
                            JSONObject recharge=jsonObject.getJSONObject("recharge");
                            tv_id.setText("Report Id - "+recharge.getString("report_id"));
                            tv_provider.setText("Provider - "+recharge.getString("provider"));
                            tv_amount.setText("Amount - "+recharge.getString("amount"));
                            tv_txnid.setText("Txn Id - "+recharge.getString("txnid"));
                            tv_txndate.setText("Txn Date - "+recharge.getString("transaction_date"));
                            tv_txnstatus.setText("Txn Status - "+recharge.getString("transaction_status"));
                            tv_number.setText("Number - "+recharge.getString("number"));
                            tv_complaint_reason.setText("Complaint Reason - "+recharge.getString("complaint_reason"));
                            tv_complaint_status.setText("Complaint Status - "+recharge.getString("complaint_status"));
                        }
                    }
                    catch (JSONException e)
                    {
                        e.printStackTrace();
                    }
                }
                else
                {
                    Toast.makeText(ViewDispute.this, "Server not responding", Toast.LENGTH_SHORT).show();
                }

                if (DetectConnection.checkInternetConnection(ViewDispute.this))
                {
                    mGetChatDetail();
                }
            }
        }.execute();
    }

    @SuppressLint("StaticFieldLeak")
    protected void mGetChatDetail()
    {
        String sending_url=BaseURL.BASEURL_B2C+"api/dispute/view-conversation";
        Uri.Builder builder=new Uri.Builder();
        builder.appendQueryParameter("api_token",SharePrefManager.getInstance(ViewDispute.this).mGetApiToken());
        builder.appendQueryParameter("dispute_id",id);

        new CallResAPIPOSTMethod(ViewDispute.this,builder,sending_url,true,"POST")
        {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();

            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                Log.e("data","chat "+s);

                if (!s.equals(""))
                {
                    String status="";
                    try{
                        JSONObject jsonObject=new JSONObject(s);
                        if (jsonObject.has("status"))
                        {
                            status=jsonObject.getString("status");
                        }


                        if (status.equalsIgnoreCase("success"))
                        {
                            chatItems.clear();
                            JSONArray jsonArray=jsonObject.getJSONArray("chat");
                            for (int i=0; i<jsonArray.length(); i++)
                            {
                                JSONObject data=jsonArray.getJSONObject(i);
                                ChatItem item=new ChatItem();
                                item.setId(data.getString("id"));
                                item.setUser_id(data.getString("user_id"));
                                item.setDispute_id(data.getString("dispute_id"));
                                item.setMessage(data.getString("message"));
                                item.setCreated_at(data.getString("created_at"));
                                item.setIs_read(data.getString("is_read"));

                                chatItems.add(item);
                                chatCardAdapter.notifyDataSetChanged();
                            }
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

    @SuppressLint("StaticFieldLeak")
    protected void mSendMessage(final String message)
    {
        String sending_url=BaseURL.BASEURL_B2C+"api/dispute/send-chat-message";
        Uri.Builder builder=new Uri.Builder();
        builder.appendQueryParameter("api_token",SharePrefManager.getInstance(ViewDispute.this).mGetApiToken());
        builder.appendQueryParameter("chat_message",message);
        builder.appendQueryParameter("dispute_id",id);

        new CallResAPIPOSTMethod(ViewDispute.this,builder,sending_url,true,"POST")
        {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);

                if (!s.equals(""))
                {
                    String status="",r_message="";
                    try{
                        JSONObject jsonObject=new JSONObject(s);

                        if (jsonObject.has("status"))
                        {
                            status=jsonObject.getString("status");
                        }

                        if (jsonObject.has("message"))
                        {
                            r_message=jsonObject.getString("message");
                        }

                        if (status.equalsIgnoreCase("success"))
                        {
                            ed_type_message.setText("");
                            ChatItem item=new ChatItem();
                            item.setId("");
                            item.setUser_id(SharePrefManager.getInstance(ViewDispute.this).mGetId());
                            item.setMessage(message);
                            item.setDispute_id(id);
                            item.setIs_read("0");
                            chatItems.add(item);
                            chatCardAdapter.notifyDataSetChanged();
                        }
                        else if (!r_message.equals(""))
                        {
                            Toast.makeText(ViewDispute.this, r_message, Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            Toast.makeText(ViewDispute.this, "Unable to send message, please try again later", Toast.LENGTH_SHORT).show();
                        }
                    }
                    catch (JSONException e)
                    {
                        e.printStackTrace();
                    }
                }
                else
                {
                    Toast.makeText(ViewDispute.this, "Unable to send message, please try again later", Toast.LENGTH_SHORT).show();
                }
            }
        }.execute();
    }
}