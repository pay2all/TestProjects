package com.demo.apppay2all.EarningDetail;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.demo.apppay2all.R;

import java.util.List;

public class EarningCardAdaptor extends RecyclerView.Adapter<EarningCardAdaptor.ViewHolder>{

    Context context;
    List<EarningItem> earningItems;


    public EarningCardAdaptor(Context context,List<EarningItem> earningItems)
    {
        this.context=context;
        this.earningItems=earningItems;
    }

    @Override
    public int getItemCount() {
        return earningItems==null?0:earningItems.size();
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        EarningItem items=earningItems.get(position);

        holder.textview_provider.setText(items.getProvider_name());
        holder.textview_total.setText(items.getTotal_amount());
        holder.textview_commission.setText(items.getTotal_profit());
        if (position!=0){
            String pos=String.valueOf(position);
            if (pos.endsWith("0")||pos.endsWith("2")||pos.endsWith("4")||pos.endsWith("6")||pos.endsWith("8"))
            {
            }
            else{
                holder.ll_earning_item.setBackgroundColor(context.getResources().getColor(R.color.light_gray));
            }
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(context).inflate(R.layout.earning_item,parent,false);
        return new ViewHolder(view);
    }

    class ViewHolder extends RecyclerView.ViewHolder
    {

        LinearLayout ll_earning_item;
        TextView textview_provider,textview_total,textview_commission;
        ViewHolder (View view)
        {
            super(view);
            textview_provider=view.findViewById(R.id.textview_provider);
            textview_total=view.findViewById(R.id.textview_total);
            textview_commission=view.findViewById(R.id.textview_commission);
            ll_earning_item=view.findViewById(R.id.ll_earning_item);
        }
    }
}
