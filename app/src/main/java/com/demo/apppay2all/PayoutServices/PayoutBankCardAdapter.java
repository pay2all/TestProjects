package com.demo.apppay2all.PayoutServices;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import com.demo.apppay2all.BaseURL.BaseURL;
import com.demo.apppay2all.DetectConnection;
import com.demo.apppay2all.R;
import com.demo.apppay2all.Review_Activity;

public class PayoutBankCardAdapter extends RecyclerView.Adapter<PayoutBankCardAdapter.ViewHolder> {

    Context context;
    List<PayoutBankListItem> payoutBankListItems;
    AlertDialog alertDialog;
    String username,password,ifsc;

    ProgressDialog dialog;
    public PayoutBankCardAdapter(Context context, List<PayoutBankListItem> payoutBankListItems)
    {
        this.context=context;
        this.payoutBankListItems=payoutBankListItems;
    }

    @Override
    public int getItemCount() {
        return payoutBankListItems==null ?0:payoutBankListItems.size();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.payout_bank_list_item,parent,false);
        ViewHolder viewHolder=new ViewHolder(view);
        SharedPreferences sharedPreferences=context.getSharedPreferences("user",0);
        username=sharedPreferences.getString("username","");
        password=sharedPreferences.getString("password","");
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        final PayoutBankListItem items=payoutBankListItems.get(position);
        holder.textview_name.setText(items.getName());
        holder.textview_accountno.setText(items.getAccount());
        holder.textview_bank.setText(items.getBank_name());
        holder.textview_mobile.setText(items.getMobile_number());

        holder.button_transfer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ifsc=items.getIfsc();
                mShowCustomeDialog(username,password,items.getName(),items.getAccount(),items.getBank_name());
            }
        });

        holder.button_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (DetectConnection.checkInternetConnection(context)) {
                    mDeleteBeneficiary(username,position);
                }
                else
                {
                    Toast.makeText(context, "No Internet Connection", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    public class ViewHolder extends RecyclerView.ViewHolder
    {
        TextView textview_name,textview_accountno,textview_bank,textview_mobile;
        Button button_transfer,button_delete;
        ViewHolder(View view)
        {
            super(view);
            textview_name=view.findViewById(R.id.textview_name);
            textview_accountno=view.findViewById(R.id.textview_accountno);
            textview_bank=view.findViewById(R.id.textview_bank);
            textview_mobile=view.findViewById(R.id.textview_mobile);
            button_transfer=view.findViewById(R.id.button_transfer);
            button_delete=view.findViewById(R.id.button_delete);
        }
    }

    //    to show custome dialog
    protected void mShowCustomeDialog(final String username, final String password, final String name, final String account, final String bank)
    {

        LayoutInflater inflater2 = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v2 = inflater2.inflate(R.layout.custome_alert_dialog_payout_normal_wallet_transfer, null);

        TextView textview_title = v2.findViewById(R.id.textview_title);
        textview_title.setText(name+" - "+account);
        LinearLayout ll_remark = v2.findViewById(R.id.ll_remark);
        ll_remark.setVisibility(View.VISIBLE);
        TextView textview_name = v2.findViewById(R.id.textview_name);
        textview_name.setText(bank);
        Button button_transfer = v2.findViewById(R.id.button_transfer);
        Button button_cancel = v2.findViewById(R.id.button_cancel);

        final EditText edittext_amount= v2.findViewById(R.id.edittext_amount);
        final EditText edittext_remark= v2.findViewById(R.id.edittext_remark);

        final AlertDialog.Builder builder2 = new AlertDialog.Builder(context);
        builder2.setCancelable(false);
//                builder.setMessage("Email already registered");
//
//                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
//
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//
//                    }
//                });

        builder2.setView(v2);


        alertDialog = builder2.create();
        button_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });

        button_transfer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (DetectConnection.checkInternetConnection(context)) {
                    if (edittext_amount.getText().toString().equals(""))
                    {
                        Toast.makeText(context, "Please enter transfer amount", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        mBankAccountTransfer(username,password,edittext_amount.getText().toString(),edittext_remark.getText().toString().replaceAll(" ","%20"),account,name,ifsc);
                    }
                } else {
                    Toast.makeText(context, "No Internet Connection", Toast.LENGTH_SHORT).show();
                }
            }
        });

        alertDialog.show();
    }
    
//    to transfer bank account

    //     to normal wallet transfer
    private  void mBankAccountTransfer(final String username, final String password, final String amount, final String remark, final String account, final String name, final String ifsc) {
        class getJSONData extends AsyncTask<String, String, String> {

            HttpURLConnection urlConnection;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                dialog=new ProgressDialog(context);
                dialog.setMessage("Please wait...");
                dialog.show();
            }
            @Override
            protected String doInBackground(String... args) {

                StringBuilder result = new StringBuilder();

                try {
                    URL url = new URL(BaseURL.BASEURL+"api/v1/payout-transaction-now?username="+username+"&password="+password+"&amount="+amount+"&remark="+remark+"&account="+account+"&name="+name+"&ifsc="+ifsc);
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
                Log.e("response",result);

                if (!result.equals(""))
                {
                    alertDialog.dismiss();
                    String status="";
                    String message="";
                    try {
                        JSONObject jsonObject=new JSONObject(result);
                        status=jsonObject.getString("status");
                        message=jsonObject.getString("message");

                        if (status.equalsIgnoreCase("success"))
                        {
                            Intent intent=new Intent(context, Review_Activity.class);
                            Bundle bundle1=new Bundle();
                            bundle1.putString("status",status);
                            bundle1.putString("message",message);
                            bundle1.putString("activity","payout_wallet");
                            intent.putExtras(bundle1);
                            context.startActivity(intent);
                        }
                        else if (status.equalsIgnoreCase("failure"))
                        {
                            Intent intent=new Intent(context, Review_Activity.class);
                            Bundle bundle1=new Bundle();
                            bundle1.putString("status",status);
                            bundle1.putString("message",message);
                            bundle1.putString("activity","payout_wallet");
                            intent.putExtras(bundle1);
                            context.startActivity(intent);
                        }
                        else if (status.equalsIgnoreCase("")&&message.equalsIgnoreCase(""))
                        {
                            Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            Toast.makeText(context, status+"\n"+message, Toast.LENGTH_SHORT).show();
                        }
                    }
                    catch (JSONException e)
                    {
                        e.printStackTrace();
                    }
                }
                else
                {
                    Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show();
                }
            }
        }

        getJSONData getJSONData=new getJSONData();
        getJSONData.execute();
    }
     //     to delete beneficiary
    private  void mDeleteBeneficiary(final String username, final int position) {
        class getJSONData extends AsyncTask<String, String, String> {

            HttpURLConnection urlConnection;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                dialog=new ProgressDialog(context);
                dialog.setMessage("Please wait...");
                dialog.show();
            }
            @Override
            protected String doInBackground(String... args) {

                StringBuilder result = new StringBuilder();

                try {
                    URL url = new URL(BaseURL.BASEURL+"api/v1/payout-delete-beneficiary?mobile="+username);
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
                Log.e("delete response",result);

                if (!result.equals(""))
                {
                    String status="";
                    String message="";
                    try {
                        JSONObject jsonObject=new JSONObject(result);
                        status=jsonObject.getString("status");
                        message=jsonObject.getString("message");

                        if (status.equalsIgnoreCase("success"))
                        {
                            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                            payoutBankListItems.remove(position);
                            notifyItemRemoved(position);
                            notifyItemRangeChanged(position, payoutBankListItems.size());
                        }
                        else
                        {
                            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                        }
                    }
                    catch (JSONException e)
                    {
                        e.printStackTrace();
                    }
                }
                else
                {
                    Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show();
                }
            }
        }

        getJSONData getJSONData=new getJSONData();
        getJSONData.execute();
    }
}