package com.demo.apppay2all.RechargesServicesDetail;

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
import com.demo.apppay2all.R;

import java.util.List;

public class RechargesDetailCardAdapter extends RecyclerView.Adapter<RechargesDetailCardAdapter.ViewHolder> {

    Context context;
    List<RechargeItems> rechargeItems;
    public RechargesDetailCardAdapter(Context context,List<RechargeItems> rechargeItems)
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

        RechargeItems items=rechargeItems.get(position);
        holder.tv_name.setText(items.getName());

        if (items.getId().equals("999")||items.getId().equals("998")||items.getId().equals("997")) {
            holder.iv_icon.setBackgroundResource(items.image);
        }
        else
        {
            Glide.with(context).load(items.getService_image()).into(holder.iv_icon);
        }
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
                    if (!rechargeItems.get(getAdapterPosition()).getId().equalsIgnoreCase("12")||!rechargeItems.get(getAdapterPosition()).getId().equalsIgnoreCase("13")||!rechargeItems.get(getAdapterPosition()).getId().equalsIgnoreCase("15")||!rechargeItems.get(getAdapterPosition()).getId().equalsIgnoreCase("16")) {
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("DATA", rechargeItems.get(getAdapterPosition()));
                        Intent intent = new Intent(context, RechargeActivity.class);
                        intent.putExtras(bundle);
                        context.startActivity(intent);
                    }
                }
            });
        }
    }
}
