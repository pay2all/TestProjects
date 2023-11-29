package com.demo.apppay2all.LedgerDetail;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.demo.apppay2all.R;

import java.util.List;

public class LedgerCardAdaptor extends RecyclerView.Adapter <LedgerCardAdaptor.ViewHolder> {

    Context context;
    List<Ledger_Item> ledger_items;

    public LedgerCardAdaptor(Context context,List<Ledger_Item> ledger_items)
    {
        this.context=context;
        this.ledger_items=ledger_items;
    }

    @Override
    public int getItemCount() {
        return ledger_items==null?0:ledger_items.size();
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Ledger_Item items=ledger_items.get(position);
        holder.textview_time.setText("Date : "+items.getDate());
        holder.textview_bal_txn_id.setText("Id : "+items.getId());
        holder.textview_particulars.setText(items.getDescription());
        if (items.getCredit().equals("0")){
            holder.textview_debit_credit.setText(items.getDebit());
            holder.textview_status.setText("Dr");
            holder.textview_status.setTextColor(context.getResources().getColor(R.color.red));

        }
        else {
            holder.textview_debit_credit.setText(items.getCredit());
            holder.textview_status.setText("Cr");
            holder.textview_status.setTextColor(context.getResources().getColor(R.color.green));
        }
        holder.textview_opening.setText("Opening Balance : "+items.getOpening_balance());
        holder.textview_closing.setText("Closing Balance : "+items.getClosing_balance());
        holder.textview_commission.setText("Profit : "+items.getCommission());

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.landscape_laser,parent,false);
        return new ViewHolder(view);
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView textview_time,textview_bal_txn_id,textview_particulars,textview_debit_credit,textview_status,textview_opening,textview_closing,textview_commission;

        ViewHolder(View view){
            super(view);
            textview_time=view.findViewById(R.id.textview_time);
            textview_bal_txn_id=view.findViewById(R.id.textview_bal_txn_id);
            textview_particulars=view.findViewById(R.id.textview_particulars);
            textview_debit_credit=view.findViewById(R.id.textview_debit_credit);
            textview_status=view.findViewById(R.id.textview_status);
            textview_opening=view.findViewById(R.id.textview_opening);
            textview_closing=view.findViewById(R.id.textview_closing);
            textview_commission=view.findViewById(R.id.textview_commission);

        }
    }

    public void UpdateList(List<Ledger_Item> item)
    {
        ledger_items
                =item;
        notifyDataSetChanged();
    }

}
