package com.demo.apppay2all.Fund_Transfer;

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
import android.widget.ImageView;
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

public class FundTransferCardAdapter extends RecyclerView.Adapter<FundTransferCardAdapter.ViewHolder> {


    Context context;
    List<FundTransferItem> fundTransferItems;
    AlertDialog alertDialog;

    String username,password;

    Button button_fund_transfer;
    ImageView imageview_cancel;
    EditText edittext_amount;
    ProgressDialog dialog;
    public FundTransferCardAdapter(Context context, List<FundTransferItem> fundTransferItems)
    {
        this.context=context;
        this.fundTransferItems=fundTransferItems;
    }

    @Override
    public int getItemCount() {
        return fundTransferItems==null ? 0: fundTransferItems.size();
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.fundtransferitems,parent,false);
        ViewHolder viewHolder=new ViewHolder(view);

        SharedPreferences sharedPreferences=context.getSharedPreferences("user",0);
        username=sharedPreferences.getString("username","");
        password=sharedPreferences.getString("password","");
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final FundTransferItem items=fundTransferItems.get(position);
        holder.textview_name.setText(items.getName());
        holder.textview_mobileno.setText("Mobile : "+items.getMobile());
        holder.textview_balance.setText("Balance : Rs."+items.getBalance());

        holder.button_transfer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent=new Intent(context,FunTransfer.class);
//                Bundle bundle=new Bundle();
//                bundle.putString("childid",items.getId());
//                bundle.putString("name",items.getName());
//                bundle.putString("mobile",items.getMobile());
//                intent.putExtras(bundle);
//                context.startActivity(intent);
                mShowTransferDialog(items.getName(),items.getMobile(),items.getId());
            }
        });
    }

    class ViewHolder extends RecyclerView.ViewHolder
    {
        TextView textview_name,textview_mobileno,textview_balance;
        Button button_transfer;

        ViewHolder(View view)
        {
            super(view);
            textview_name=(TextView)view.findViewById(R.id.textview_name);
            textview_mobileno=(TextView)view.findViewById(R.id.textview_mobileno);
            textview_balance=(TextView)view.findViewById(R.id.textview_balance);
            button_transfer=(Button) view.findViewById(R.id.button_transfer);
        }
    }

    public void UpdateList(List<FundTransferItem> fund_item)
    {
        fundTransferItems=fund_item;
        notifyDataSetChanged();
    }

    protected void mShowTransferDialog(final String name, final String mobile, final String childid)
    {
        LayoutInflater inflater2 = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v2 = inflater2.inflate(R.layout.custome_layout_fund_transfer, null);

        button_fund_transfer = v2.findViewById(R.id.button_fund_transfer);
        imageview_cancel = v2.findViewById(R.id.imageview_cancel);

        TextView textview_name= v2.findViewById(R.id.textview_name);
        textview_name.setText("Name : "+name);
        TextView textview_mobileno= v2.findViewById(R.id.textview_mobileno);
        textview_mobileno.setText("Mobile : "+mobile);
        edittext_amount= v2.findViewById(R.id.edittext_amount);


        final AlertDialog.Builder builder2 = new AlertDialog.Builder(context);
        builder2.setCancelable(false);
        builder2.setView(v2);

        alertDialog = builder2.create();
        imageview_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });

        button_fund_transfer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (DetectConnection.checkInternetConnection(context))
                {
                    if (edittext_amount.getText().toString().equals(""))
                    {
                        Toast.makeText(context, "No Internet Connection", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        mFundTransfer(username,password,childid,edittext_amount.getText().toString());
                    }
                }
                else
                {
                    Toast.makeText(context, "No Internet Connection", Toast.LENGTH_SHORT).show();
                }
            }
        });



        alertDialog.show();
    }

    private  void  mFundTransfer(final String username, final String password, final String childid, final String amount) {
        class getJSONData extends AsyncTask<String, String, String> {


            HttpURLConnection urlConnection;


            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                dialog=new ProgressDialog(context);
                dialog.setMessage("Please wait...");
                dialog.show();
                dialog.setCancelable(false);
            }
            @Override
            protected String doInBackground(String... args) {

                StringBuilder result = new StringBuilder();

                try {
                    URL url = new URL(BaseURL.BASEURL +"api/v1/transfer-now?username="+username+"&password="+password+"&child_id="+childid+"&amount="+amount);
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
                Log.e("fund trensfer",result);

                String status="";
                String message="";

                try
                {

                    JSONObject jsonObject=new JSONObject(result);
                    status=jsonObject.getString("status");
                    message=jsonObject.getString("message");
                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }


                if (result.equals(""))
                {
                    Toast.makeText(context, "Server not responding please try again", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    if (status.equals(""))
                    {
                        Toast.makeText(context, "Something went wrong please try again later", Toast.LENGTH_SHORT).show();
                    }

                    else
                    {
                        Intent intent=new Intent(context, Review_Activity.class);
                        Bundle bundle1=new Bundle();
                        bundle1.putString("status",status);
                        bundle1.putString("message",message);
                        bundle1.putString("activity","fund");
                        intent.putExtras(bundle1);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);
                        alertDialog.dismiss();

                    }
                }
            }
        }

        getJSONData getJSONData=new getJSONData();
        getJSONData.execute();
    }
}