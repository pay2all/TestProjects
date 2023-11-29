package com.demo.apppay2all.FlightTicketBooking.CityAirport;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.demo.apppay2all.R;

import java.util.List;

public class CityAdapter extends RecyclerView.Adapter<CityAdapter.ViewHolder>{

    Context context;
    List<CityItems> cityItems;

    public CityAdapter(Context context,List<CityItems> cityItems)
    {
        this.context=context;
        this.cityItems=cityItems;
    }

    @Override
    public int getItemCount() {
        return cityItems==null?0:cityItems.size();
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CityItems items=cityItems.get(position);
        holder.tv_city.setText(items.getCityName());
        holder.tv_dd.setText(items.getDd());


    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.city_item_layout,parent,false);
        return new ViewHolder(view);
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        TextView tv_city,tv_dd;
        View wv_line;
        ViewHolder(View view)
        {
            super(view);
            tv_city=view.findViewById(R.id.tv_city);
            tv_dd=view.findViewById(R.id.tv_dd);
            wv_line=view.findViewById(R.id.wv_line);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ((CityFromDestination)context).mSetOnActivityResult(cityItems.get(getAdapterPosition()));
                }
            });
        }
    }

    public void UpdateList(List<CityItems> item)
    {
        cityItems =item;
        notifyDataSetChanged();
    }

}
