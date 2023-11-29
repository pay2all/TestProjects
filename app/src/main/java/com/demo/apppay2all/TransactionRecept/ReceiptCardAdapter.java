package com.demo.apppay2all.TransactionRecept;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.demo.apppay2all.R;

import java.util.List;


public class ReceiptCardAdapter extends RecyclerView.Adapter<ReceiptCardAdapter.ViewHolder> {


    Context context;
    List<ReceiptItem> receiptItems;
    public ReceiptCardAdapter(Context context, List<ReceiptItem> receiptItems)
    {
        this.context=context;
        this.receiptItems=receiptItems;
    }

    @Override
    public int getItemCount() {
        return receiptItems==null ?0:receiptItems.size();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.receiptitems,parent,false);
        ViewHolder viewHolder=new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ReceiptItem item=receiptItems.get(position);
        holder.textview_id.setText("Id : "+item.getReceipt_id());
        holder.textview_bank_reference.setText(item.getReceipt_bank_ref());
        holder.textview_amount.setText("Rs "+item.getReceipt_amount());
        holder.textview_status.setText(item.getReceipt_status());
        if (item.getReceipt_status().equalsIgnoreCase("success"))
        {
            holder.textview_status.setTextColor(Color.parseColor("#FF21B326"));
        }
        else if (item.getReceipt_status().equalsIgnoreCase("failure"))
        {
            holder.textview_status.setTextColor(Color.parseColor("#FFD7411F"));
        }
        else
        {
            holder.textview_status.setTextColor(Color.parseColor("#FFDF8F33"));
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        TextView textview_id,textview_bank_reference,textview_amount,textview_profit,textview_status;
        ViewHolder(View view)
        {
            super(view);
            textview_id=view.findViewById(R.id.textview_id);
            textview_bank_reference=view.findViewById(R.id.textview_bank_reference);
            textview_amount=view.findViewById(R.id.textview_amount);
            textview_status=view.findViewById(R.id.textview_status);
        }
    }
}
