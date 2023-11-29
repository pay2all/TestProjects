package com.demo.apppay2all.DesputDetails;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.demo.apppay2all.DesputDetails.ViewDisputDetail.ViewDispute;
import com.demo.apppay2all.R;

import java.util.List;

public class DisputeCardAdapter extends RecyclerView.Adapter<DisputeCardAdapter.ViewHolder>{


    Context context;
    List<DisputeItems> disputeItems;

    public DisputeCardAdapter(Context context,List<DisputeItems> disputeItems)
    {
        this.context=context;
        this.disputeItems=disputeItems;
    }
    @Override
    public int getItemCount() {
        return disputeItems==null?0:disputeItems.size();
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final DisputeItems items=disputeItems.get(position);

        holder.tv_token.setText(items.getTicket_id());
        holder.tv_date.setText(items.getDate());
        holder.tv_disput_date.setText(items.getDate());
        holder.tv_provider.setText(items.getProvider());
        holder.tv_number.setText(items.getNumber());
        holder.tv_reason.setText(items.getReason());
        holder.tv_message.setText(items.getMessage());
        holder.tv_status.setText(items.getStatus());

        if (items.getActivity().equalsIgnoreCase("pending"))
        {
            holder.rl_chat.setVisibility(View.VISIBLE);
        }
        else
        {
            holder.rl_chat.setVisibility(View.GONE);
        }
        holder.rl_chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, ViewDispute.class);
                intent.putExtra("id",items.getTicket_id());
                context.startActivity(intent);
            }
        });
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.desput_item_layout,parent,false);
        return new ViewHolder(view);
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        TextView tv_token,tv_date,tv_disput_date,tv_provider,tv_number,tv_reason,tv_message,tv_status,tv_action;
        RelativeLayout rl_chat;
        ViewHolder (View view)
        {
            super(view);
            tv_token=view.findViewById(R.id.tv_token);
            tv_date=view.findViewById(R.id.tv_date);
            tv_disput_date=view.findViewById(R.id.tv_disput_date);
            tv_provider=view.findViewById(R.id.tv_provider);
            tv_number=view.findViewById(R.id.tv_number);
            tv_reason=view.findViewById(R.id.tv_reason);
            tv_message=view.findViewById(R.id.tv_message);
            tv_status=view.findViewById(R.id.tv_status);
            tv_action=view.findViewById(R.id.tv_action);
            rl_chat=view.findViewById(R.id.rl_chat);
        }

    }
}
