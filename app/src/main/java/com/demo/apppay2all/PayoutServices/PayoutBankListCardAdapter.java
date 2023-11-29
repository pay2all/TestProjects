package com.demo.apppay2all.PayoutServices;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.demo.apppay2all.R;

import java.util.List;


/**
 * Created by admin on 3/29/2018.
 */

public class PayoutBankListCardAdapter extends RecyclerView.Adapter<com.demo.apppay2all.PayoutServices.PayoutBankListCardAdapter.ViewHolder>{

    Context context;
    List<BankListItems> operators_items;
    public PayoutBankListCardAdapter(Context context, List<BankListItems> operators_items)
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
        final BankListItems item=operators_items.get(position);
        holder.textview_operator.setText(item.getBank());

//            holder.textview_capital_latter_operator.setVisibility(View.VISIBLE);
//            holder.textview_capital_latter_operator.setText(item.getOperator_name().substring(0,1).toUpperCase());
//            holder.imageview_operator_icon.setVisibility(View.GONE);

//        if (item.getOperator_image().equals(""))
//        {
            holder.textview_capital_latter_operator.setVisibility(View.VISIBLE);
            holder.textview_capital_latter_operator.setText(item.getBank().substring(0,1).toUpperCase());
            holder.imageview_operator_icon.setVisibility(View.GONE);
//        }
//        else {
//            Picasso.with(context).load("https://pay2all.in/"+item.getOperator_image()).into(holder.imageview_operator_icon);
//            holder.textview_capital_latter_operator.setVisibility(View.GONE);
//            holder.imageview_operator_icon.setVisibility(View.VISIBLE);
//        }
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
                    BankListItems item=operators_items.get(position);

                    if (com.demo.apppay2all.PayoutServices.PayoutBankListBottomSheet3DialogFragment.activity_name.equals("aeps"))
                    {
                        com.demo.apppay2all.PayoutServices.PayoutBankListBottomSheet3DialogFragment.dialogFragment.dismiss();
                    }
                    else {
//                        PayoutAddBeneficiary.textview_bank_name.setText(item.getBank());
//                        PayoutAddBeneficiary.bank_id = item.getId();
//                        PayoutAddBeneficiary.editext_ifcs.setText(item.getIfsc());
                        ((com.demo.apppay2all.PayoutServices.PayoutAddBeneficiary)context).mGetBankDetail(item);
                        com.demo.apppay2all.PayoutServices.PayoutBankListBottomSheet3DialogFragment.dialogFragment.dismiss();
                    }
                }
            });
        }
    }

    public void UpdateList(List<BankListItems> item)
    {
        operators_items=item;
        notifyDataSetChanged();
    }
}
