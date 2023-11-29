package com.demo.apppay2all.CommissionDetail.MyComission;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.demo.apppay2all.R;

import java.util.List;

public class CommissionServiceCardAdapter extends RecyclerView.Adapter<CommissionServiceCardAdapter.ViewHolder>{


    Context context;
    List<CommissionServiceItems> commissionServiceItems;
    public CommissionServiceCardAdapter(Context context,List<CommissionServiceItems> commissionServiceItems)
    {
        this.context=context;
        this.commissionServiceItems=commissionServiceItems;
    }
    @Override
    public int getItemCount() {
        return commissionServiceItems==null?0:commissionServiceItems.size();
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CommissionServiceItems items=commissionServiceItems.get(position);

        if (!items.getService_image().equals(""))
        {
            Glide.with(context).load(items.getService_image()).into(holder.iv_logo);
        }
        holder.tv_service.setText(items.getService_name());
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.commission_service_item_layout,parent,false);
        return new ViewHolder(view);
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        ImageView iv_logo;
        TextView tv_service;
        ViewHolder(View view)
        {
            super(view);

            iv_logo=view.findViewById(R.id.iv_logo);
            tv_service=view.findViewById(R.id.tv_service);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(context,My_Comission.class);
                    Bundle bundle=new Bundle();
                    bundle.putSerializable("DATA",commissionServiceItems.get(getAdapterPosition()));
                    intent.putExtras(bundle);
                    context.startActivity(intent);
                }
            });
        }
    }
}
