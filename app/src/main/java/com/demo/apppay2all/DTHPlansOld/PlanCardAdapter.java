package com.demo.apppay2all.DTHPlansOld;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.demo.apppay2all.R;
import com.demo.apppay2all.RechargesServicesDetail.DTHRecharge;

import java.util.List;

public class PlanCardAdapter extends RecyclerView.Adapter<PlanCardAdapter.ViewHolder> {

    Context context;
    List<PlanItems> planItems;
    public PlanCardAdapter(Context context,List<PlanItems> planItems)
    {
        this.context=context;
        this.planItems=planItems;
    }

    @Override
    public int getItemCount() {
        return planItems==null?0:planItems.size();
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        final PlanItems items=planItems.get(position);
        holder.tv_planname.setText(items.getPlan_name());
        holder.tv_desc.setText(items.getDesc());
        if (!items.getOne_m().equals("")) {
            holder.tv_onem.setText("Rs "+items.getOne_m()+" ( 1 Month)");
        }
        else
        {
            holder.tv_onem.setVisibility(View.GONE);
        }

        if (!items.getThree_m().equals("")) {
            holder.tv_threem.setText("Rs "+items.getThree_m()+" ( 3 Months)");
        }
        else
        {
            holder.tv_threem.setVisibility(View.GONE);
        }

        if (!items.getSix_m().equals("")) {
            holder.tv_sixm.setText("Rs "+items.getSix_m()+"( 6 Months)");
        }
        else
        {
            holder.tv_sixm.setVisibility(View.GONE);
        }

        if (!items.getOne_y().equals("")) {
            holder.tv_oney.setText("Rs "+items.getOne_y()+" (1 Year)");
        }
        else
        {
            holder.tv_oney.setVisibility(View.GONE);
        }

        holder.tv_onem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DTHRecharge.ed_amount.setText(items.getOne_m());
                ((Plans)context).finish();
            }
        });
        holder.tv_threem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DTHRecharge.ed_amount.setText(items.getThree_m());
                ((Plans)context).finish();
            }
        });
        holder.tv_sixm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DTHRecharge.ed_amount.setText(items.getSix_m());
                ((Plans)context).finish();
            }
        });
        holder.tv_oney.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DTHRecharge.ed_amount.setText(items.getOne_y());
                ((Plans)context).finish();
            }
        });
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=LayoutInflater.from(context).inflate(R.layout.dthplan_item_layout,parent,false);

        return new ViewHolder(view);
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView tv_planname,tv_desc,tv_onem,tv_threem,tv_sixm,tv_oney;
        ViewHolder(View view)
        {
            super(view);
            tv_planname=view.findViewById(R.id.tv_planname);
            tv_desc=view.findViewById(R.id.tv_desc);
            tv_onem=view.findViewById(R.id.tv_onem);
            tv_threem=view.findViewById(R.id.tv_threem);
            tv_sixm=view.findViewById(R.id.tv_sixm);
            tv_oney=view.findViewById(R.id.tv_oney);

        }
    }
    public void UpdateList(List<PlanItems> item)
    {
        planItems =item;
        notifyDataSetChanged();
    }
}