package com.demo.apppay2all.ui.notifications;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.demo.apppay2all.R;

import java.util.List;

public class NotificationCardAdapter extends RecyclerView.Adapter<NotificationCardAdapter.ViewHolder>{

    Context context;
    List<NotificationItems> notificationItems;

    public NotificationCardAdapter(Context context,List<NotificationItems> notificationItems)
    {
        this.context=context;
        this.notificationItems=notificationItems;
    }
    @Override
    public int getItemCount() {
        return notificationItems==null?0:notificationItems.size();
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        NotificationItems items=notificationItems.get(position);
        holder.textview_title.setText(items.getTitle());
        holder.textview_message.setText(items.getMessage());
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.notification_item_layout,parent,false);
        return new ViewHolder(view);
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {

        TextView textview_title,textview_message;
        ViewHolder(View view)
        {
            super(view);
            textview_title=view.findViewById(R.id.textview_title);
            textview_message=view.findViewById(R.id.textview_message);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(context,ViewNotification.class);
                    Bundle bundle=new Bundle();
                    bundle.putSerializable("DATA",notificationItems.get(getAdapterPosition()));
                    intent.putExtras(bundle);
                    context.startActivity(intent);

                }
            });
        }
    }
}
