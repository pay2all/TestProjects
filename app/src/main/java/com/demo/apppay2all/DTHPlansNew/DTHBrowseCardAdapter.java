package com.demo.apppay2all.DTHPlansNew;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.demo.apppay2all.R;
import com.demo.apppay2all.RechargesServicesDetail.DTHRecharge;

import java.util.List;

public class DTHBrowseCardAdapter extends RecyclerView.Adapter<DTHBrowseCardAdapter.ViewHolder>{

    Context context;
    List<DTHPlanItems> plan_items;
    public DTHBrowseCardAdapter(Context context, List<DTHPlanItems> plan_items)
    {
        this.context=context;
        this.plan_items = plan_items;
    }

    @Override
    public int getItemCount() {
        return plan_items ==null ? 0: plan_items.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.dth_browse_plan_item,parent,false);
        ViewHolder viewHolder=new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final DTHPlanItems item= plan_items.get(position);
        holder.textview_plan_name.setText("Plan Name : "+item.getPlan_name());
        holder.textview_talktime.setText("Talktime : Rs "+item.getTalktime());
        holder.textview_amount.setText("\u20B9 "+item.getAmount());
        holder.textview_description.setText(item.getDescription());
        holder.textview_validity.setText("Validity : "+item.getValidity());

    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        TextView textview_plan_name,textview_talktime,textview_amount,textview_description,textview_validity;
        ViewHolder(View view)
        {
            super(view);
            textview_plan_name= view.findViewById(R.id.textview_plan_name);
            textview_talktime= view.findViewById(R.id.textview_talktime);
            textview_amount= view.findViewById(R.id.textview_amount);
            textview_description= view.findViewById(R.id.textview_description);
            textview_validity= view.findViewById(R.id.textview_validity);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DTHRecharge.ed_amount.setText(plan_items.get(getAdapterPosition()).getAmount());
                    ((DTHBrowsePlanType)context).finish();
                }
            });
        }
    }
}
