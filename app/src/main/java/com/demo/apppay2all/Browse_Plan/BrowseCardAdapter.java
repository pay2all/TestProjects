package com.demo.apppay2all.Browse_Plan;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.demo.apppay2all.R;
import com.demo.apppay2all.RechargesServicesDetail.PrepaidMobile;

import java.util.List;

/**
 * Created by admin on 3/29/2018.
 */

public class BrowseCardAdapter extends RecyclerView.Adapter<BrowseCardAdapter.ViewHolder>{

    Context context;
    List<Plan_Items> plan_items;
    public BrowseCardAdapter(Context context, List<Plan_Items> plan_items)
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

        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.browse_plan_item,parent,false);
        ViewHolder viewHolder=new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Plan_Items item= plan_items.get(position);
        holder.textview_talktime.setText("Talktime : Rs "+item.getTalktime());
        holder.textview_amount.setText("\u20B9 "+item.getAmount());
        holder.textview_description.setText(item.getDescription());
        holder.textview_validity.setText("Validity : "+item.getValidity());

    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        TextView textview_talktime,textview_amount,textview_description,textview_validity;
        ViewHolder(View view)
        {
            super(view);
            textview_talktime= view.findViewById(R.id.textview_talktime);
            textview_amount= view.findViewById(R.id.textview_amount);
            textview_description= view.findViewById(R.id.textview_description);
            textview_validity= view.findViewById(R.id.textview_validity);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PrepaidMobile.ed_amount.setText(plan_items.get(getAdapterPosition()).getAmount());
                    ((BrowsePlan)context).finish();
                }
            });
        }
    }
}
