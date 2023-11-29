package com.demo.apppay2all.MemberDetail;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.demo.apppay2all.R;

import java.util.List;

public class MemberCardAdapter extends RecyclerView.Adapter<MemberCardAdapter.ViewHolder>{

    Context context;
    List<MemberItems> memberItems;
    public MemberCardAdapter(Context context,List<MemberItems> memberItems)
    {
        this.context=context;
        this.memberItems=memberItems;
    }
    @Override
    public int getItemCount() {
        return memberItems==null?0:memberItems.size();
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final MemberItems items=memberItems.get(position);
        holder.tv_name.setText(items.getName());
        holder.tv_email.setText("Email "+items.getEmail());

        if (items.getMobile().length()>9) {
            String before_num=items.getMobile().substring(0,4);
            String after_num=items.getMobile().substring(7,items.getMobile().length());

            holder.tv_mobile.setText(before_num+"***"+after_num);
        }

//        holder.tv_mobile.setText("Mobile "+items.getMobile());
        holder.tv_balance.setText("BAL "+items.getBalance()+" Rs");
        holder.tv_role.setText(items.getRole());


        holder.tb_send_money.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context,FundTransfer.class);
                Bundle bundle=new Bundle();
                bundle.putSerializable("DATA",items);
                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        });
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(context).inflate(R.layout.member_item_layout,parent,false);
        return new ViewHolder(view);
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        TextView tv_name,tv_mobile,tv_email,tv_role,tv_balance;
        Button tb_send_money;
        ViewHolder(View view)
        {
            super(view);
            tv_name=view.findViewById(R.id.tv_name);
            tv_mobile=view.findViewById(R.id.tv_mobile);
            tv_email=view.findViewById(R.id.tv_email);
            tv_role=view.findViewById(R.id.tv_role);
            tv_balance=view.findViewById(R.id.tv_balance);
            tb_send_money=view.findViewById(R.id.tb_send_money);
        }
    }

    public void UpdateList(List<MemberItems> item)
    {
        memberItems =item;
        notifyDataSetChanged();
    }
}
