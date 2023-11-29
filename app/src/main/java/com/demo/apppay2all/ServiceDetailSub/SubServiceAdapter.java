package com.demo.apppay2all.ServiceDetailSub;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.demo.apppay2all.AEPSList;
import com.demo.apppay2all.MoneyTransfer1.SenderActivity;
import com.demo.apppay2all.MoneyTransfer2.SenderDetailActivity;
import com.demo.apppay2all.PanCard;
import com.demo.apppay2all.PayoutServices.Payout;
import com.demo.apppay2all.PayoutServices.PayoutMoveToWallet;
import com.demo.apppay2all.PayoutServices.PayoutNew;
import com.demo.apppay2all.ProviderDetail.Operator;
import com.demo.apppay2all.R;

import java.util.List;

public class SubServiceAdapter extends RecyclerView.Adapter<SubServiceAdapter.ViewHolder>{

    Context context;
    List<SubServiceItem> subServiceItemList;
    public SubServiceAdapter(Context context,List<SubServiceItem> subServiceItems)
    {
        this.context=context;
        this.subServiceItemList=subServiceItems;
    }
    @Override
    public int getItemCount() {
        return subServiceItemList==null?0:subServiceItemList.size();
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        SubServiceItem item=subServiceItemList.get(position);
        if (!item.getService_image().equals(""))
        {
            Glide.with(context).load(item.getService_image()).into(holder.iv_icon);
        }

        holder.tv_name.setText(item.getService_name());
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.sub_service_item_layout,parent,false);
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
                public void onClick(View view) {
                    SubServiceItem item=subServiceItemList.get(getAdapterPosition());
                    if (item.getBbps().equals("1"))
                    {
                        Intent intent=new Intent(context, Operator.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("from","home");
                        bundle.putSerializable("DATA", subServiceItemList.get(getAdapterPosition()));
                        intent.putExtras(bundle);
                        context.startActivity(intent);
                    }
                    else if (item.getService_id().equals("1"))
                    {
                        Intent intent=new Intent(context, Operator.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("from","home");
                        bundle.putSerializable("DATA", subServiceItemList.get(getAdapterPosition()));
                        intent.putExtras(bundle);
                        context.startActivity(intent);
                    }
                    else if (item.getService_id().equals("2"))
                    {
                        Intent intent=new Intent(context, Operator.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("from","home");
                        bundle.putSerializable("DATA", subServiceItemList.get(getAdapterPosition()));
                        intent.putExtras(bundle);
                        context.startActivity(intent);
                    }
                    else if (item.getService_id().equals("3"))
                    {
                        Intent intent=new Intent(context, Operator.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("from","home");
                        bundle.putSerializable("DATA", subServiceItemList.get(getAdapterPosition()));
                        intent.putExtras(bundle);
                        context.startActivity(intent);
                    }
                    else if (item.getService_id().equals("17"))
                    {
                        context.startActivity(new Intent(context, SenderActivity.class));
                    }
                    else if (item.getService_id().equals("18"))
                    {
                        context.startActivity(new Intent(context, SenderDetailActivity.class));
                    }
                    else if (item.getService_id().equals("19"))
                    {
                        Intent intent=new Intent(context, AEPSList.class);
                        Bundle bundle=new Bundle();
                        bundle.putSerializable("DATA",item);
                        intent.putExtras(bundle);
                        context.startActivity(intent);
                    }
                    else if (item.getService_id().equals("20"))
                    {
                        context.startActivity(new Intent(context, PayoutMoveToWallet.class));
                    }
                    else if (item.getService_id().equals("21"))
                    {
                        context.startActivity(new Intent(context, PayoutNew.class));
                    }
                    else if (item.getService_id().equals("22"))
                    {
                        context.startActivity(new Intent(context, PanCard.class));
                    }

                }
            });
        }
    }
}