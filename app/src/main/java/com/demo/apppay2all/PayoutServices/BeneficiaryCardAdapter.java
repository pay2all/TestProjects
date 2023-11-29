package com.demo.apppay2all.PayoutServices;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.demo.apppay2all.BaseURL.BaseURL;
import com.demo.apppay2all.CallResAPIPOSTMethod;
import com.demo.apppay2all.DetectConnection;
import com.demo.apppay2all.R;
import com.demo.apppay2all.SharePrefManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class BeneficiaryCardAdapter extends RecyclerView.Adapter<com.demo.apppay2all.PayoutServices.BeneficiaryCardAdapter.ViewHolder>{

    Context context;
    List<com.demo.apppay2all.PayoutServices.BeneficiaryItems> beneficiaryItems;
    ProgressDialog dialog;
    public BeneficiaryCardAdapter(Context context,List<com.demo.apppay2all.PayoutServices.BeneficiaryItems> beneficiaryItems)
    {
        this.context=context;
        this.beneficiaryItems=beneficiaryItems;
    }

    @Override
    public int getItemCount() {
        return beneficiaryItems==null?0:beneficiaryItems.size();
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        final com.demo.apppay2all.PayoutServices.BeneficiaryItems items=beneficiaryItems.get(position);
        holder.textview_name.setText(items.getHolder_name());
        holder.textview_ifsc.setText("IFSC  "+items.getIfsc_code());
        holder.textview_account_number.setText("A/c "+items.getAccount_number());
        holder.textview_bank_name.setText(items.getBank_name());

        holder.button_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (DetectConnection.checkInternetConnection(context))
                {
                    mDeleteBeneficiary(items.getBeneficiary_id(),items.getMobile_number(),position);
                }
                else
                {
                    Toast.makeText(context, "No internet connection", Toast.LENGTH_SHORT).show();
                }
            }
        });

        if (items.getStatus_id().equalsIgnoreCase("1"))
        {
            holder.button_transfer.setVisibility(View.VISIBLE);
            holder.tv_status.setVisibility(View.GONE);
        }
        else
        {
            holder.button_transfer.setVisibility(View.GONE);
            holder.tv_status.setVisibility(View.VISIBLE);
            if (items.getStatus_id().equalsIgnoreCase("3"))
            {
                holder.tv_status.setTextColor(context.getResources().getColor(R.color.orange));
                holder.tv_status.setText("Pending");

            }
            else if (items.getStatus_id().equals("2"))
            {
                holder.tv_status.setTextColor(context.getResources().getColor(R.color.red));
                holder.tv_status.setText("Rejected");
            }
        }
        holder.button_transfer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, PayoutMoveToBank.class);
                Bundle bundle=new Bundle();
                bundle.putSerializable("DATA",items);
                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        });
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(context).inflate(R.layout.payout_beneficiary_item,parent,false);
        return new ViewHolder(view);
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        TextView textview_name,textview_ifsc,textview_bank_name,textview_account_number,tv_status;
        ImageButton button_delete;
        Button button_transfer;
        ViewHolder(View view)
        {
            super(view);

            textview_name=view.findViewById(R.id.textview_name);
            textview_ifsc=view.findViewById(R.id.textview_ifsc);
            textview_bank_name=view.findViewById(R.id.textview_bank_name);
            textview_account_number=view.findViewById(R.id.textview_account_number);
            tv_status=view.findViewById(R.id.tv_status);


            button_delete=view.findViewById(R.id.button_delete);
            button_transfer=view.findViewById(R.id.button_transfer);
        }
    }

    @SuppressLint("StaticFieldLeak")
    protected void mDeleteBeneficiary(String recipient_id, String mobile_number, final int position)
    {
        String sending_url= BaseURL.BASEURL_B2C+ "api/settlement/delete-beneficiary";
        Uri.Builder builder=new Uri.Builder();
        builder.appendQueryParameter("api_token", SharePrefManager.getInstance(context).mGetApiToken());
        builder.appendQueryParameter("mobile_number",mobile_number );
        builder.appendQueryParameter("recipient_id",recipient_id);

        new CallResAPIPOSTMethod((Activity) context,builder,sending_url,true,"POST")
        {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                dialog=new ProgressDialog(context);
                dialog.setMessage("Please wait...");
                dialog.setCancelable(false);
                dialog.show();
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                dialog.dismiss();
                Log.e("data","delete "+s);
                if (!s.equals(""))
                {
                    String status="",message="";
                    try {
                        JSONObject jsonObject=new JSONObject(s);

                        if (jsonObject.has("status"))
                        {
                            status=jsonObject.getString("status");
                        }
                        if (jsonObject.has("message"))
                        {
                            message=jsonObject.getString("message");
                        }

                        mShowRecipt(status,message,position);
                    }
                    catch (JSONException e)
                    {
                        e.printStackTrace();
                    }
                }
                else
                {
                    Toast.makeText(context, "Server not responding", Toast.LENGTH_SHORT).show();
                }
            }
        }.execute();
    }

    protected void mShowRecipt(final String status, final String message, final int position)
    {
        final AlertDialog alertDialog;
        LayoutInflater inflater2 =(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v2=inflater2.inflate(R.layout.custom_alertdalog_for_message,null);

        ImageView imageview_messase_image=v2.findViewById(R.id.imageview_messase_image);

        TextView textview_message=v2.findViewById(R.id.textview_message);
        Button button_ok=v2.findViewById(R.id.button_ok);

        textview_message.setText(message);

        if (status.equalsIgnoreCase("success")){
            imageview_messase_image.setBackground(context.getResources().getDrawable(R.drawable.checkicon));

        }
        else if (status.equalsIgnoreCase("failure")){
            imageview_messase_image.setBackground(context.getResources().getDrawable(R.drawable.failure));

        }
        else if (status.equalsIgnoreCase("pending")){
            imageview_messase_image.setBackground(context.getResources().getDrawable(R.drawable.pending));

        }
        else {
            imageview_messase_image.setBackground(context.getResources().getDrawable(R.drawable.pending));
        }

        final AlertDialog.Builder builder2=new AlertDialog.Builder(context);
        builder2.setCancelable(false);
        builder2.setView(v2);
        alertDialog=builder2.create();

        button_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
                if (status.equalsIgnoreCase("success"))
                {
                    beneficiaryItems.remove(position);
                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                    notifyDataSetChanged();
                }
            }
        });
        alertDialog.show();
    }

}
