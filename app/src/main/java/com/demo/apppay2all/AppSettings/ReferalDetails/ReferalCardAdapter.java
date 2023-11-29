package com.demo.apppay2all.AppSettings.ReferalDetails;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.demo.apppay2all.R;

import java.util.List;

public class ReferalCardAdapter extends RecyclerView.Adapter<ReferalCardAdapter.ViewHolder>{

    Context context;
    List<ReferalItems> referalItems;

    public ReferalCardAdapter(Context context,List<ReferalItems> referalItems)
    {
        this.context=context;
        this.referalItems=referalItems;
    }

    @Override
    public int getItemCount() {
        return referalItems==null?0:referalItems.size();
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ReferalItems items=referalItems.get(position);
        holder.tv_name.setText(items.getUser_name());
        holder.tv_number.setText(items.getNumber());
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.referal_item_layout,parent,false);
        return new ViewHolder(view);
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        TextView tv_name,tv_number;
        ViewHolder(View view)
        {
            super(view);
            tv_name=view.findViewById(R.id.tv_name);
            tv_number=view.findViewById(R.id.tv_number);
        }
    }

}