package com.demo.apppay2all.FundRequesDetails;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.demo.apppay2all.R;

import java.util.List;


/**
 * Created by Basant on 2/5/2018.
 */

public class Payment_Request_ReportCardAdapter extends RecyclerView.Adapter<Payment_Request_ReportCardAdapter.ViewHolder> {

    List<Payment_Request_Report_Items> payment_request_report_items;
    Context context;


    public Payment_Request_ReportCardAdapter(Context context, List<Payment_Request_Report_Items> payment_request_report_items)
    {
        this.payment_request_report_items=payment_request_report_items;
        this.context=context;
    }

    @Override
    public int getItemCount() {
        return  payment_request_report_items==null ? 0: payment_request_report_items.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.payment_request_report_items,parent,false);
        ViewHolder viewHolder=new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        final Payment_Request_Report_Items item=payment_request_report_items.get(position);

         holder.textview_id.setText("Id : "+item.getId());
         holder.textview_date.setText("Date : "+item.getDate());
         holder.textview_method.setText("Mode : "+item.getMethod());
         holder.textview_bankname.setText("Bank : "+item.getBank());
         holder.textview_refrenceno.setText("Ref no : "+item.getRefrenceno());
         holder.textview_amount.setText("Amount : Rs "+item.getAmount());
         holder.textview_amount_deposit_date.setText("Deposit Date : "+item.getDeposit_date());
//         holder.textview_remark.setText(item.getRemark());
         holder.textview_status.setText(item.getStatus());

        if (!item.getStatus().equals(""))
        {
            if (item.getStatus().equalsIgnoreCase("success")||item.getStatus().equalsIgnoreCase("credit"))
            {
                holder.textview_status.setTextColor(context.getResources().getColor(R.color.green));
            }
            else if (item.getStatus().equalsIgnoreCase("refunded"))
            {
                holder.textview_status.setTextColor(context.getResources().getColor(R.color.orange));
            }
            else if (item.getStatus().equalsIgnoreCase("debit"))
            {
                holder.textview_status.setTextColor(context.getResources().getColor(R.color.red));
            }
            else if (item.getStatus().equalsIgnoreCase("failure"))
            {
                holder.textview_status.setTextColor(context.getResources().getColor(R.color.red));
            }
        }



        holder.setIsRecyclable(false);

//        if (position==payment_request_report_items.size()-1)
//        {
//            if (Payment_request_reports.last_array_empty)
//            {
//
//            }
//            else
//            {
//                ((Payment_request_reports)context).mCallNextPage();
//            }
//        }



    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        TextView textview_date,textview_method,textview_bankname,textview_refrenceno,textview_amount,textview_amount_deposit_date,textview_status,textview_id,textview_remark;

        public ViewHolder (View view)
        {
            super(view);

            textview_id=view.findViewById(R.id.textview_id);
            textview_date= view.findViewById(R.id.textview_date);
            textview_method= view.findViewById(R.id.textview_method);
            textview_bankname= view.findViewById(R.id.textview_bankname);
            textview_refrenceno= view.findViewById(R.id.textview_refrenceno);
            textview_amount= view.findViewById(R.id.textview_amount);
            textview_amount_deposit_date= view.findViewById(R.id.textview_amount_deposit_date);
//            textview_remark= view.findViewById(R.id.textview_remark);
            textview_status= view.findViewById(R.id.textview_status);
        }
    }

    public void UpdateList(List<Payment_Request_Report_Items> item)
    {
        payment_request_report_items=item;
        notifyDataSetChanged();
    }

}
