package com.demo.apppay2all.ProviderDetail;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.demo.apppay2all.R;
import com.demo.apppay2all.RechargeServicesNew.PrepaidRecharge;
import com.demo.apppay2all.RechargesServicesDetail.DTHRecharge;
import com.demo.apppay2all.RechargesServicesDetail.PrepaidMobile;
import com.demo.apppay2all.RechargesServicesDetail.RechargeActivity;

import java.util.List;

/**
 * Created by admin on 3/29/2018.
 */

public class OperatorsCardAdapter extends RecyclerView.Adapter<OperatorsCardAdapter.ViewHolder>{

    Context context;
    List<Operators_Items> operators_items;
    public OperatorsCardAdapter(Context context, List<Operators_Items> operators_items)
    {
        this.context=context;
        this.operators_items=operators_items;
    }

    @Override
    public int getItemCount() {
        return operators_items==null ? 0: operators_items.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.operator_items,parent,false);
        ViewHolder viewHolder=new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Operators_Items item=operators_items.get(position);
        holder.textview_operator.setText(item.getOperator_name());

//            holder.textview_capital_latter_operator.setVisibility(View.VISIBLE);
//            holder.textview_capital_latter_operator.setText(item.getOperator_name().substring(0,1).toUpperCase());
//            holder.imageview_operator_icon.setVisibility(View.GONE);
//
        if (item.getOperator_image().equals(""))
        {
            holder.textview_capital_latter_operator.setVisibility(View.VISIBLE);
            holder.textview_capital_latter_operator.setText(item.getOperator_name().substring(0,1).toUpperCase());
            holder.imageview_operator_icon.setVisibility(View.GONE);
        }
        else {
            Glide.with(context).load(item.getOperator_image()).into(holder.imageview_operator_icon);
            holder.textview_capital_latter_operator.setVisibility(View.GONE);
            holder.imageview_operator_icon.setVisibility(View.VISIBLE);
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        ImageView imageview_operator_icon;
        TextView textview_operator,textview_capital_latter_operator;
        ViewHolder(View view)
        {
            super(view);
            imageview_operator_icon=(ImageView)view.findViewById(R.id.imageview_operator_icon);
            textview_operator=(TextView)view.findViewById(R.id.textview_operator);
            textview_capital_latter_operator=(TextView)view.findViewById(R.id.textview_capital_latter_operator);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position=getAdapterPosition();
                    Operators_Items item=operators_items.get(position);

                    if (item.getFrom().equalsIgnoreCase("activity")) {
                        RechargeActivity.mGetOperatorDetail(item.getOperator_name(),item.getOperator_id());
                        ((Operator)context).finish();
                    }
                    else if (item.getService_id().equalsIgnoreCase("1"))
                    {
                        Intent intent=new Intent(context, PrepaidMobile.class);
//                        Intent intent=new Intent(context, PrepaidRecharge.class);
                        Bundle bundle=new Bundle();
                        bundle.putSerializable("DATA",item.getData());
                        bundle.putString("provider_id",item.getOperator_id());
                        bundle.putString("provider_name",item.getOperator_name());
                        bundle.putString("provider_image",item.getOperator_image());
                        intent.putExtras(bundle);
                        context.startActivity(intent);
//                        ((Operator)context).finish();
                    }
                    else if (item.getService_id().equalsIgnoreCase("2"))
                    {
                        Intent intent=new Intent(context, DTHRecharge.class);
                        Bundle bundle=new Bundle();
                        bundle.putSerializable("DATA",item.getData());
                        bundle.putString("provider_id",item.getOperator_id());
                        bundle.putString("provider_name",item.getOperator_name());
                        bundle.putString("provider_image",item.getOperator_image());
                        intent.putExtras(bundle);
                        context.startActivity(intent);
//                        ((Operator)context).finish();
                    }
                    else
                    {
                        Intent intent=new Intent(context, RechargeActivity.class);
                        Bundle bundle=new Bundle();
                        bundle.putSerializable("DATA",item.getData());
                        bundle.putString("provider_id",item.getOperator_id());
                        bundle.putString("provider_name",item.getOperator_name());
                        bundle.putString("provider_image",item.getOperator_image());
                        intent.putExtras(bundle);
                        context.startActivity(intent);
//                        ((Operator)context).finish();
                    }
//                    ((RechargeActivity) context).getSupportFragmentManager().popBackStackImmediate();
//                    ((Operator) context).finish();
                }
            });
        }
    }

    public void UpdateList(List<Operators_Items> item)
    {
        operators_items=item;
        notifyDataSetChanged();
    }
}
