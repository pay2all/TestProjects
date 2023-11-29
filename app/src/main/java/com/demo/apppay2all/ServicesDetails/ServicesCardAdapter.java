package com.demo.apppay2all.ServicesDetails;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.demo.apppay2all.AddMemberDetails.AddMember;
import com.demo.apppay2all.AllTransactionDetail.AllTransaction;
import com.demo.apppay2all.CommingSoonActivity;
import com.demo.apppay2all.DetectConnection;
import com.demo.apppay2all.MainActivitySingle;
import com.demo.apppay2all.MemberDetail.MemberList;
import com.demo.apppay2all.MoneyTransfer1.SenderActivity;
import com.demo.apppay2all.MoneyTransfer2.SenderDetailActivity;
import com.demo.apppay2all.MoneyTransferDetails.MoneyDetails;
import com.demo.apppay2all.PanCard;
import com.demo.apppay2all.PayoutServices.Payout;
import com.demo.apppay2all.ProviderDetail.Operator;
import com.demo.apppay2all.R;
import com.demo.apppay2all.RechargesServicesDetail.GooglePlayCoupon;
import com.demo.apppay2all.SharePrefManager;

import java.util.List;

public class ServicesCardAdapter extends RecyclerView.Adapter<ServicesCardAdapter.ViewHolder> {

    Context context;
    List<ServicesItems> rechargeItems;
    public ServicesCardAdapter(Context context, List<ServicesItems> rechargeItems)
    {
        this.context=context;
        this.rechargeItems=rechargeItems;
    }
    @Override
    public int getItemCount() {
        return rechargeItems==null?0:rechargeItems.size();
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        ServicesItems items=rechargeItems.get(position);
        holder.tv_name.setText(items.getName());

        if (items.getService_image().equals("")) {
            if (items.getImage()!=0) {
                holder.iv_icon.setImageDrawable(context.getResources().getDrawable(items.image));
//                if (!SharePrefManager.getInstance(context).mGetSingleData("color_start").equals("")) {
//                    holder.iv_icon.setColorFilter(Color.parseColor(SharePrefManager.getInstance(context).mGetSingleData("color_start")), android.graphics.PorterDuff.Mode.SRC_IN);
//                }
            }
        }
        else
        {
            Glide.with(context).load(items.getService_image()).into(holder.iv_icon);
        }

//        if (!SharePrefManager.getInstance(context).mGetSingleData("color_start").equals("")) {
//            holder.iv_icon.setColorFilter(Color.parseColor(SharePrefManager.getInstance(context).mGetSingleData("color_start")), android.graphics.PorterDuff.Mode.SRC_IN);
//            holder.tv_name.setTextColor(Color.parseColor(SharePrefManager.getInstance(context).mGetSingleData("color_start")));
//        }

        holder.setIsRecyclable(false);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.recharge_item_layout,parent,false);

        return new ViewHolder(view);
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        TextView tv_name;
        ImageView iv_icon;
        ViewHolder (View view)
        {
            super(view);
            tv_name=view.findViewById(R.id.tv_name);
            iv_icon=view.findViewById(R.id.iv_icon);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (rechargeItems.get(getAdapterPosition()).getId().equalsIgnoreCase("upi"))
                    {
//                        context.startActivity(new Intent(context, UPIQRCode.class));
                    }

                    else if (rechargeItems.get(getAdapterPosition()).getId().equalsIgnoreCase("googleplay"))
                    {
                        context.startActivity(new Intent(context, GooglePlayCoupon.class));
                    }
                    else if (rechargeItems.get(getAdapterPosition()).getId().equalsIgnoreCase("Money Transfer 1"))
                    {
                        context.startActivity(new Intent(context, SenderActivity.class));
                    }
                    else if (rechargeItems.get(getAdapterPosition()).getId().equalsIgnoreCase("Money Transfer 2"))
                    {
                        context.startActivity(new Intent(context, SenderDetailActivity.class));
                    }
                    else if (rechargeItems.get(getAdapterPosition()).getId().equalsIgnoreCase("Report"))
                    {
                        context.startActivity(new Intent(context, MoneyDetails.class));
                    }
                    else if (rechargeItems.get(getAdapterPosition()).getId().equalsIgnoreCase("matm_enquiry"))
                    {
                        if (DetectConnection.checkInternetConnection(context)) {
                            ((MainActivitySingle)context).mGetOutletIdForMatm("be", "Balance Enquiry");
                        } else {
                            Toast.makeText(context, "No internet connection", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else if (rechargeItems.get(getAdapterPosition()).getId().equalsIgnoreCase("matm_withdrawal"))
                    {
                        if (DetectConnection.checkInternetConnection(context)) {
                            ((MainActivitySingle)context). mGetOutletIdForMatm("cw", "Cash Withdrawal");
                        } else {
                            Toast.makeText(context, "No internet connection", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else if (rechargeItems.get(getAdapterPosition()).getId().equalsIgnoreCase("Enquiry"))
                    {
                        if (DetectConnection.checkInternetConnection(context)) {
                            ((MainActivitySingle)context).mGetOutletId("be");
                        } else {
                            Toast.makeText(context, "No internet connection", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else if (rechargeItems.get(getAdapterPosition()).getId().equalsIgnoreCase("Withdrawal"))
                    {
                        if (DetectConnection.checkInternetConnection(context)) {
                            ((MainActivitySingle)context).mGetOutletId("cw");
                        } else {
                            Toast.makeText(context, "No internet connection", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else if (rechargeItems.get(getAdapterPosition()).getId().equalsIgnoreCase("Mini Statement"))
                    {
                        if (DetectConnection.checkInternetConnection(context)) {
                            ((MainActivitySingle)context).mGetOutletId("mst");
                        } else {
                            Toast.makeText(context, "No internet connection", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else if (rechargeItems.get(getAdapterPosition()).getId().equalsIgnoreCase("Aadhaar Pay"))
                    {
                        if (DetectConnection.checkInternetConnection(context)) {
                            ((MainActivitySingle)context).mGetOutletId("ap");
                        } else {
                            Toast.makeText(context, "No internet connection", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else if (rechargeItems.get(getAdapterPosition()).getId().equalsIgnoreCase("Payout"))
                    {
                        if (DetectConnection.checkInternetConnection(context)) {
                            context.startActivity(new Intent(context, Payout.class));
                        } else {
                            Toast.makeText(context, "No internet connection", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else if (rechargeItems.get(getAdapterPosition()).getId().equalsIgnoreCase("PAN Card"))
                    {
                        if (DetectConnection.checkInternetConnection(context)) {
                            context.startActivity(new Intent(context, PanCard.class));
                        } else {
                            Toast.makeText(context, "No internet connection", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else if (rechargeItems.get(getAdapterPosition()).getId().equalsIgnoreCase("Bus"))
                    {
                        if (DetectConnection.checkInternetConnection(context)) {
                            context.startActivity(new Intent(context, CommingSoonActivity.class));
                        }
                        else {
                            Toast.makeText(context, "No internet connection", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else if (rechargeItems.get(getAdapterPosition()).getId().equalsIgnoreCase("Train"))
                    {
                        if (DetectConnection.checkInternetConnection(context)) {
                            context.startActivity(new Intent(context, CommingSoonActivity.class));
                        } else {
                            Toast.makeText(context, "No internet connection", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else if (rechargeItems.get(getAdapterPosition()).getId().equalsIgnoreCase("Flight"))
                    {
                        if (DetectConnection.checkInternetConnection(context)) {
                            context.startActivity(new Intent(context, CommingSoonActivity.class));
                        } else {
                            Toast.makeText(context, "No internet connection", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else if (rechargeItems.get(getAdapterPosition()).getId().equalsIgnoreCase("Hotel"))
                    {
                        if (DetectConnection.checkInternetConnection(context)) {
                            context.startActivity(new Intent(context, CommingSoonActivity.class));
                        } else {
                            Toast.makeText(context, "No internet connection", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else if (rechargeItems.get(getAdapterPosition()).getId().equalsIgnoreCase("Add Member"))
                    {
                        if (DetectConnection.checkInternetConnection(context)) {
                            context.startActivity(new Intent(context, AddMember.class));
                        } else {
                            Toast.makeText(context, "No internet connection", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else if (rechargeItems.get(getAdapterPosition()).getId().equalsIgnoreCase("Fund Transfer"))
                    {
                        if (DetectConnection.checkInternetConnection(context)) {
                            context.startActivity(new Intent(context, MemberList.class));
                        } else {
                            Toast.makeText(context, "No internet connection", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else if (rechargeItems.get(getAdapterPosition()).getId().equalsIgnoreCase("All Transactions"))
                    {
                        if (DetectConnection.checkInternetConnection(context)) {
                            context.startActivity(new Intent(context, AllTransaction.class));
                        } else {
                            Toast.makeText(context, "No internet connection", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else if (!rechargeItems.get(getAdapterPosition()).getId().equalsIgnoreCase("12")
                            ||!rechargeItems.get(getAdapterPosition()).getId().equalsIgnoreCase("13")
                            ||!rechargeItems.get(getAdapterPosition()).getId().equalsIgnoreCase("15")
                            ||!rechargeItems.get(getAdapterPosition()).getId().equalsIgnoreCase("16")
                            ||!rechargeItems.get(getAdapterPosition()).getId().equalsIgnoreCase("upi")) {

                        Intent intent=new Intent(context, Operator.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("from","home");
                        bundle.putSerializable("DATA", rechargeItems.get(getAdapterPosition()));
                        intent.putExtras(bundle);
                        context.startActivity(intent);

                    }
                }
            });
        }
    }
}
