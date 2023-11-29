package com.demo.apppay2all.AppSettings.ReferalDetails;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.demo.apppay2all.BaseURL.BaseURL;
import com.demo.apppay2all.CallResAPIGetMethod;
import com.demo.apppay2all.CallResAPIPOSTMethod;
import com.demo.apppay2all.DetectConnection;
import com.demo.apppay2all.R;
import com.demo.apppay2all.SharePrefManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class NewMemberLIst extends AppCompatActivity {


    TextView ed_referral_code;
    TextView tv_copy;

    TextView tv_total_refers,tv_total_earning;

    RecyclerView rv_referals;

    TextView tv_message;

    ProgressDialog dialog;

    List<ReferalItems> referalItems;
    ReferalCardAdapter referalCardAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_member_list);

        if (getSupportActionBar()!=null)
        {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        ed_referral_code=findViewById(R.id.ed_referral_code);
        tv_copy=findViewById(R.id.tv_copy);
        tv_copy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText( "",ed_referral_code.getText().toString());
                clipboard.setPrimaryClip(clip);

                Toast.makeText(NewMemberLIst.this, ed_referral_code.getText().toString()+"Successfully Copied", Toast.LENGTH_SHORT).show();
            }
        });

        tv_total_refers=findViewById(R.id.tv_total_refers);
        tv_total_earning=findViewById(R.id.tv_total_earning);

        rv_referals=findViewById(R.id.rv_referals);
        rv_referals.setLayoutManager(new LinearLayoutManager(NewMemberLIst.this));
        referalItems =new ArrayList<>();
        referalCardAdapter =new ReferalCardAdapter(NewMemberLIst.this,referalItems);
        rv_referals.setAdapter(referalCardAdapter);

        tv_message=findViewById(R.id.tv_message);

        if (DetectConnection.checkInternetConnection(NewMemberLIst.this))
        {
            mGetDetails();
        }
        else
        {
            tv_message.setText("No internet connection");
            tv_message.setVisibility(View.VISIBLE);
        }

    }


    @SuppressLint("StaticFieldLeak")
    protected void mGetDetails()
    {
        String sending_url= BaseURL.BASEURL_B2C+"api/referral/details?api_token="+ SharePrefManager.getInstance(NewMemberLIst.this).mGetApiToken();
        new CallResAPIGetMethod(NewMemberLIst.this,sending_url)
        {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                dialog=new ProgressDialog(NewMemberLIst.this);
                dialog.setMessage("Please wait...");
                dialog.show();
                dialog.setCancelable(false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                dialog.dismiss();
                Log.e("data","response "+s);

                String status="",message="";
                try {
                    JSONObject jsonObject=new JSONObject(s);

                    if (jsonObject.has("total_refers"))
                    {
                        tv_total_refers.setText(jsonObject.getString("total_refers"));
                    }

                    if (jsonObject.has("total_earnings"))
                    {
                        tv_total_earning.setText(jsonObject.getString("total_earnings"));
                    }

                    if (jsonObject.has("referral_code"))
                    {
                        ed_referral_code.setText("https://play.google.com/store/apps/details?id="+getPackageName()+"&referrer="+jsonObject.getString("referral_code"));
                        tv_copy.setVisibility(View.VISIBLE);

                        SharePrefManager.getInstance(NewMemberLIst.this).mSaveSingleData("referral_code",jsonObject.getString("referral_code"));
                    }


                    if (jsonObject.has("status"))
                    {
                        status=jsonObject.getString("status");
                    }
                    if (jsonObject.has("message"))
                    {
                        message=jsonObject.getString("message");
                    }

                    if (!status.equals("success"))
                    {
                        if (!message.equals(""))
                        {
                            tv_message.setText(message);
                            tv_message.setVisibility(View.VISIBLE);
                        }
                    }

                    if (jsonObject.has("memberList"))
                    {
                        JSONArray jsonArray=jsonObject.getJSONArray("memberList");

                        for (int i=0; i<jsonArray.length(); i++)
                        {
                            JSONObject data=jsonArray.getJSONObject(i);
                            ReferalItems items=new ReferalItems();
                            items.setUser_name(data.getString("user_name"));
                            items.setNumber(data.getString("number"));

                            referalItems.add(items);
                            referalCardAdapter.notifyDataSetChanged();
                        }

                        if (referalItems.size()==0)
                        {
                            tv_message.setVisibility(View.VISIBLE);
                            tv_message.setText("Referrals not found");
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

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId()==android.R.id.home)
        {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}