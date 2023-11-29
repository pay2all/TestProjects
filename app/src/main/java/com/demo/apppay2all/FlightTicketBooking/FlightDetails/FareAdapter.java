package com.demo.apppay2all.FlightTicketBooking.FlightDetails;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.demo.apppay2all.R;

import java.util.List;

public class FareAdapter extends RecyclerView.Adapter<FareAdapter.ViewHolder>{

    Context context;
    List<FlightDetailsModel.FareRule> fareRuleList;

    boolean mIsExpand=false;

    public FareAdapter(Context context, List<FlightDetailsModel.FareRule> flightLists)
    {
        this.context=context;
        this.fareRuleList=flightLists;
    }

    @Override
    public int getItemCount() {
        return fareRuleList==null?0:fareRuleList.size();
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        FlightDetailsModel.FareRule fareRule=fareRuleList.get(position);
        holder.tv_airline.setText(fareRule.getAirline());
        holder.tv_origin.setText(fareRule.getOrigin());
        holder.tv_destination.setText(fareRule.getDestination());
        holder.tv_detail.setText(Html.fromHtml(fareRule.getFareRuleDetail()));

        holder.iv_expand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mIsExpand)
                {
                    String faredetail="";
                    if (fareRule.getFareRuleDetail().length()>500)
                    {
                         faredetail=fareRule.getFareRuleDetail().substring(0,500);
                    }
                    else {
                        faredetail=fareRule.getFareRuleDetail();
                    }

                    holder.tv_detail.setText(faredetail);

                    mIsExpand=false;

                    holder.iv_expand.setImageResource(R.drawable.arrow_right);
                }
                else {
                    mIsExpand=true;

                    holder.iv_expand.setImageResource(R.drawable.arrow_down);

                    holder.tv_detail.setText(fareRule.getFareRuleDetail());
                }
            }
        });

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.fare_item_layout,parent,false);
        return new ViewHolder(view);
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        RadioButton rb_select;
        TextView tv_airline,tv_origin,tv_destination,tv_detail;
        ImageView iv_expand;


        ViewHolder(View view)
        {
            super(view);
            rb_select=view.findViewById(R.id.rb_select);
            tv_airline=view.findViewById(R.id.tv_airline);
            tv_origin=view.findViewById(R.id.tv_origin);
            tv_destination=view.findViewById(R.id.tv_destination);
            tv_detail=view.findViewById(R.id.tv_detail);
            iv_expand=view.findViewById(R.id.iv_expand);

        }
    }
}