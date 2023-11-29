package com.demo.apppay2all.ROffer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.demo.apppay2all.R;
import com.demo.apppay2all.RechargesServicesDetail.DTHRecharge;
import com.demo.apppay2all.RechargesServicesDetail.PrepaidMobile;

import java.util.List;


/**
 * Created by admin on 3/29/2018.
 */

public class ROfferCardAdapter extends RecyclerView.Adapter<ROfferCardAdapter.ViewHolder>{

    Context context;
    List<Plan_Items> plan_items;
    public ROfferCardAdapter(Context context, List<Plan_Items> plan_items)
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
        holder.textview_talktime.setText("TalkTime : Rs "+item.getTalktime());
        holder.textview_amount.setText("\u20B9 "+item.getAmount());
        holder.textview_description.setText(item.getDescription());
        holder.textview_validity.setText("Validity : "+item.getValidity());

        holder.textview_amount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (item.getActivity_name().equalsIgnoreCase("prepaid")) {
                    PrepaidMobile.ed_amount.setText(item.getAmount());
                    ((R_Offer) context).finish();
                }
                else if (item.getActivity_name().equalsIgnoreCase("dth"))
                {
                    DTHRecharge.ed_amount.setText(item.getAmount());
                    ((R_Offer) context).finish();
                }
            }
        });

    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        TextView textview_talktime,textview_amount,textview_description,textview_validity;
        ViewHolder(View view)
        {
            super(view);
            textview_talktime=(TextView)view.findViewById(R.id.textview_talktime);
            textview_amount=(TextView)view.findViewById(R.id.textview_amount);
            textview_description=(TextView)view.findViewById(R.id.textview_description);
            textview_validity=(TextView)view.findViewById(R.id.textview_validity);

//            view.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    int position=getAdapterPosition();
//                    Plan_Items item= plan_items.get(position);
//
//                }
//            });
        }
    }
}
