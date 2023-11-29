package com.demo.apppay2all.MoneyTransfer1;

import android.annotation.SuppressLint;
import android.app.Activity;
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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.demo.apppay2all.BaseURL.BaseURL;
import com.demo.apppay2all.CallResAPIPOSTMethod;
import com.demo.apppay2all.R;
import com.demo.apppay2all.SharePrefManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by Basant on 6/10/2017.
 */

public class Beneficiry_CardAdapterPaytm extends RecyclerView.Adapter<Beneficiry_CardAdapterPaytm.ViewHolder>{
    List<Beneficiary_ItemsPaytm> beneficiary_items;
    private Context context;

    String myJSON;
    ProgressDialog dialog;
    String message;

    public Beneficiry_CardAdapterPaytm(Context context, List<Beneficiary_ItemsPaytm> beneficiary_items)
    {
        super();
        this.context=context;
        this.beneficiary_items=beneficiary_items;
    }

    @Override
    public int getItemCount() {
        return beneficiary_items==null ?0:beneficiary_items.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.saved_beneficiary_items,parent, false);
        ViewHolder viewHolder=new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final Beneficiary_ItemsPaytm items=beneficiary_items.get(position);
        holder.textView_beneficiary_name.setText(items.getBeneficiry_name());


        if (items.getAccountno().length()>4) {
            holder.textView_acountno.setText(items.getAccountno().substring(0,items.getAccountno().length()-3)+"XXX");
        }
        else
        {
            holder.textView_acountno.setText(items.getAccountno());
        }


        holder.textView_bankname.setText(items.getBank());
        holder.textView_ifsc.setText(items.getIfsc());
        holder.textView_recepient_id.setText(items.getRecepient_id());
//        holder.imageView_dots.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                showPopupMenu(holder.imageView_dots,position);
//            }
//        });

        holder.button_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Delete_Beneficiary(items.getSender_number(),items.getRecepient_id(),position);

                String sending_url=BaseURL.BASEURL_B2C+"api/dmt/v1/delete-beneficiary";
                Uri.Builder builder=new Uri.Builder();
                builder.appendQueryParameter("api_token", SharePrefManager.getInstance(context).mGetApiToken());
                builder.appendQueryParameter("mobile_number",items.getSender_number());
                builder.appendQueryParameter("recipient_id",items.getRecepient_id());

                mCheckSender(builder,sending_url,position);
            }
        });

    }

    class ViewHolder extends RecyclerView.ViewHolder
    {

        public TextView textView_beneficiary_name;
        public TextView textView_acountno;
        public TextView textView_bankname;
        public ImageView imageView_dots;
        public TextView textView_ifsc;
        public TextView textView_recepient_id;

        Button button_transfer,button_delete;

        public ViewHolder(View view)
        {
            super(view);
            textView_beneficiary_name= view.findViewById(R.id.textview_beneficiary_name);
            textView_acountno= view.findViewById(R.id.textview_acountno);
            textView_bankname= view.findViewById(R.id.textview_bankname);
            imageView_dots= view.findViewById(R.id.imageView_dots);
            textView_ifsc= view.findViewById(R.id.textView_ifsc);
            textView_recepient_id= view.findViewById(R.id.textView_recepient_id);
            button_transfer= view.findViewById(R.id.button_transfer);
            button_delete= view.findViewById(R.id.button_delete);


            button_transfer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, Money_Transaction.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("DATA",beneficiary_items.get(getAdapterPosition()));
                    bundle.putString("senderid", Saved_benificiary_FragmentPaytm.senderid);
                    bundle.putString("sender_number", Saved_benificiary_FragmentPaytm.sender_number);
                    intent.putExtras(bundle);
                    context.startActivity(intent);
                }
            });
        }
    }

    protected void ShowDelete_Status(String data ,final int position)
    {
        String status="";
        try
        {
            JSONObject jsonObject=new JSONObject(data);
            status=jsonObject.getString("status");
            message=jsonObject.getString("message");

            if (status.equalsIgnoreCase("success"))
            {
                beneficiary_items.remove(position);
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                notifyDataSetChanged();
            }
            else if (!status.equalsIgnoreCase("success"))
            {
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
            }
            else
            {
                Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show();
            }

        }

        catch (JSONException e)
        {
            e.printStackTrace();
        }
    }

    @SuppressLint("StaticFieldLeak")
    protected void mCheckSender(Uri.Builder builder, String sending_url, final int position)
    {

        new CallResAPIPOSTMethod((Activity) context,builder,sending_url,true,"POST"){
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
                Log.e("delete","response "+s);
                ShowDelete_Status(s,position);

            }
        }.execute();
    }

}