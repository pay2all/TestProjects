package com.demo.apppay2all.AepsReportDetails;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.demo.apppay2all.R;

import java.util.List;

public class AepsReportCardadadapter extends RecyclerView.Adapter<AepsReportCardadadapter.ViewHolder>{

    Context context;
    List<AepsReportItem> aepsReportItems;
    public AepsReportCardadadapter(Context context, List<AepsReportItem> aepsReportItems)
    {
        this.context=context;
        this.aepsReportItems=aepsReportItems;
    }

    @Override
    public int getItemCount() {
        return aepsReportItems==null?0:aepsReportItems.size();
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        AepsReportItem item=aepsReportItems.get(position);
        holder.tv_id.setText("Id : "+item.getId());
        holder.tv_date.setText("Date : "+item.getCreated_at());
        holder.tv_txnid.setText("Txn Id : "+item.getTxnid());
        holder.tv_desc.setText(""+item.getDescription());
        holder.tv_op.setText("OB : "+item.getOpening_balance());
        holder.tv_debit.setText("Debit : "+item.getDebit());
        holder.tv_credit.setText("Credit : "+item.getCredit());
        holder.tv_profit.setText("Profit : "+item.getProfit());
        holder.tv_cl.setText("Total Blance : "+item.getTotal_balance());

        if (item.getType().equals("aeps"))
        {
            holder.tv_cl.setText("Txn Type : "+item.getProvider());
            holder.tv_op.setVisibility(View.GONE);

            holder.tv_debit.setVisibility(View.GONE);

            holder.tv_desc.setText("Aaadhaar No : "+item.getDescription());
        }

        holder.setIsRecyclable(false);

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.aeps_ladger_item_layout,parent,false);
        return new ViewHolder(view);
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        TextView tv_id,tv_date,tv_txnid,tv_desc,tv_op,tv_debit,tv_credit,tv_profit,tv_cl;
        ViewHolder(View view)
        {
            super(view);
            tv_id=view.findViewById(R.id.tv_id);
            tv_date=view.findViewById(R.id.tv_date);
            tv_txnid=view.findViewById(R.id.tv_txnid);
            tv_desc=view.findViewById(R.id.tv_desc);
            tv_op=view.findViewById(R.id.tv_op);
            tv_debit=view.findViewById(R.id.tv_debit);
            tv_credit=view.findViewById(R.id.tv_credit);
            tv_profit=view.findViewById(R.id.tv_profit);
            tv_cl=view.findViewById(R.id.tv_cl);
        }
    }
}
