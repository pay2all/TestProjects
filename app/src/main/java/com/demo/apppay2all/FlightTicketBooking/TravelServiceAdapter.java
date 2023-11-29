package com.demo.apppay2all.FlightTicketBooking;

import android.content.Context;
import android.content.Intent;
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

public class TravelServiceAdapter extends RecyclerView.Adapter<TravelServiceAdapter.ViewHolder>{

    Context context;
    List<TravelItems> travelItemsList;
    public TravelServiceAdapter(Context context, List<TravelItems> travelItemsList)
    {
        this.context=context;
        this.travelItemsList=travelItemsList;
    }
    @Override
    public int getItemCount() {
        return travelItemsList==null?0:travelItemsList.size();
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        TravelItems item=travelItemsList.get(position);

        if (!item.getImage_url().equals(""))
        {
            Glide.with(context).load(item.getImage_url()).into(holder.iv_icon);
        }
        else if (item.getImage()!=0)
        {
            holder.iv_icon.setImageResource(item.getImage());
        }

        holder.tv_name.setText(item.getName());
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.travel_service_item_layout,parent,false);
        return new ViewHolder(view);
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        TextView tv_name;
        ImageView iv_icon;

        ViewHolder (View view)
        {
            super(view);
            tv_name=view.findViewById(R.id.tv_name);
            iv_icon=view.findViewById(R.id.iv_icon);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    TravelItems item=travelItemsList.get(getAdapterPosition());
                    if (item.getId().equalsIgnoreCase("flight"))
                    {
                        Intent intent=new Intent(context, FlightSearch.class);
                        context.startActivity(intent);
                    }
                }
            });
        }
    }
}