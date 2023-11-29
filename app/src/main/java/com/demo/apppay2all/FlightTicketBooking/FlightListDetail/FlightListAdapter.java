package com.demo.apppay2all.FlightTicketBooking.FlightListDetail;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.demo.apppay2all.DetectConnection;
import com.demo.apppay2all.FlightTicketBooking.BookDetails.Book;
import com.demo.apppay2all.FlightTicketBooking.FlightDetails.FlightDetailBottomSheet3DialogFragment;
import com.demo.apppay2all.FlightTicketBooking.FlightListDetail.MultiStopDetail.MultiStopAdapter;
import com.demo.apppay2all.R;
import com.google.gson.Gson;


import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class FlightListAdapter extends RecyclerView.Adapter<FlightListAdapter.ViewHolder>{

    Context context;
    List<Result> flightListItemKotlins;

    public FlightListAdapter(Context context,List<Result> flightListItemKotlins)
    {
        this.context=context;
        this.flightListItemKotlins=flightListItemKotlins;
    }

    @Override
    public int getItemCount() {
        return flightListItemKotlins==null?0:flightListItemKotlins.size();
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Result items=flightListItemKotlins.get(position);

        holder.tv_flight_detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (holder.tv_flight_detail.getText().toString().equals("Flight Detail"))
                {
                    mShowBreakPointFlight(items.getSegments(),holder,true);
                    holder.tv_flight_detail.setText("Hide Details");
                    holder.tv_flight_detail.setCompoundDrawablesWithIntrinsicBounds(null,null,context.getDrawable(R.drawable.arrow_up_side),null);
                }
                else {
                    mShowBreakPointFlight(items.getSegments(),holder,false);
                    holder.tv_flight_detail.setText("Flight Detail");
                    holder.tv_flight_detail.setCompoundDrawablesWithIntrinsicBounds(null,null,context.getDrawable(R.drawable.arrow_down),null);
                }
            }
        });


        if (items.getSegments().size()>1)
        {
            mShowMultipleStops(items,holder);
        }
        else{
            mShowNonStop(items,holder);

        }

        holder.bt_book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context, Book.class));
            }
        });

        holder.tv_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (DetectConnection.checkInternetConnection(context))
                    {
                        FlightDetailBottomSheet3DialogFragment dialogFragment=new FlightDetailBottomSheet3DialogFragment();
                        Bundle bundle=new Bundle();
                        bundle.putString("tracid",((FlightList)context).mGetTracid());
                        bundle.putString("adult",((FlightList)context).mGetAdult());
                        bundle.putString("child",((FlightList)context).mGetChild());
                        bundle.putString("infant",((FlightList)context).mGetInfant());
                        bundle.putString("indexid",flightListItemKotlins.get(position).getResultIndex());
                        dialogFragment.setArguments(bundle);
                        dialogFragment.show(((FlightList) context).getSupportFragmentManager(), dialogFragment.getTag());
                    }
                    else {
                        Toast.makeText(context, "No internet connection", Toast.LENGTH_SHORT).show();
                    }
            }
        });
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.flight_list_item_layout,parent,false);
        return new ViewHolder(view);
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        TextView tv_airline_name,tv_flight_no,tv_price;
        TextView tv_from,tv_terminal,tv_depart_time;
        TextView tv_travel_time;
        TextView tv_to,tv_to_terminal,tv_arrival_time;
        TextView tv_available_seat;
        TextView tv_layover_time;
        TextView tv_flight_detail;
        TextView tv_refundable;
        TextView tv_break_point;
        TextView tv_overlay,tv_view;
        RecyclerView rv_multi_stop;
        Button bt_book;
        ViewHolder(View view)
        {
            super(view);

            tv_airline_name=view.findViewById(R.id.tv_airline_name);
            tv_flight_no=view.findViewById(R.id.tv_flight_no);
            tv_price=view.findViewById(R.id.tv_price);
            tv_from=view.findViewById(R.id.tv_from);
            tv_terminal=view.findViewById(R.id.tv_terminal);
            tv_depart_time=view.findViewById(R.id.tv_depart_time);
            tv_travel_time=view.findViewById(R.id.tv_travel_time);
            tv_to=view.findViewById(R.id.tv_to);
            tv_to_terminal=view.findViewById(R.id.tv_to_terminal);
            tv_arrival_time=view.findViewById(R.id.tv_arrival_time);
            tv_available_seat=view.findViewById(R.id.tv_available_seat);
            tv_layover_time=view.findViewById(R.id.tv_layover_time);
            tv_refundable=view.findViewById(R.id.tv_refundable);
            tv_flight_detail=view.findViewById(R.id.tv_flight_detail);
            tv_break_point=view.findViewById(R.id.tv_break_point);
            tv_overlay=view.findViewById(R.id.tv_overlay);
            rv_multi_stop=view.findViewById(R.id.rv_multi_stop);
            bt_book=view.findViewById(R.id.bt_book);
            tv_view=view.findViewById(R.id.tv_view);


//            view.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    if (DetectConnection.checkInternetConnection(context))
//                    {
//                        FlightDetailBottomSheet3DialogFragment dialogFragment=new FlightDetailBottomSheet3DialogFragment();
//                        Bundle bundle=new Bundle();
//                        bundle.putString("tracid",((FlightList)context).mGetTracid());
//                        bundle.putString("adult",((FlightList)context).mGetAdult());
//                        bundle.putString("child",((FlightList)context).mGetChild());
//                        bundle.putString("infant",((FlightList)context).mGetInfant());
//                        bundle.putString("indexid",flightListItemKotlins.get(getAdapterPosition()).getResultIndex());
//                        dialogFragment.setArguments(bundle);
//                        dialogFragment.show(((FlightList) context).getSupportFragmentManager(), dialogFragment.getTag());
//                    }
//                    else {
//                        Toast.makeText(context, "No internet connection", Toast.LENGTH_SHORT).show();
//                    }
//                }
//            });
        }
    }

    private String getparsedDate(String date) throws Exception {
        DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.US);
        String s1 = date;
        String s2 = null;
        Date d;
        try {
            d = sdf.parse(s1);
            s2 = (new SimpleDateFormat("dd MMM")).format(d);

        } catch (ParseException e) {

            e.printStackTrace();
        }

        return s2;
    }


    private void mShowNonStop(Result items, ViewHolder holder) {

        holder.tv_airline_name.setText(items.getSegments().get(0).getAirline().getAirlineName());
        holder.tv_flight_no.setText(items.getSegments().get(0).getAirline().getAirlineCode()+""+items.getSegments().get(0).getAirline().getFlightNumber());

        try {
            Fare fare=items.getFare();
            double price=fare.getBaseFare()+fare.getTax()+fare.getYqtax()+fare.getAdditionalTxnFeeOfrd()+fare.getAdditionalTxnFeePub()+fare.getPgcharge()+fare.getOtherCharges();
            holder.tv_price.setText("Rs " + price);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        if (items.getSegments().size()==1)
        {
            Long travel_time=items.getSegments().get(0).getDuration();

//            holder.tv_travel_time.setText(items.getSegments().get(0).getDuration()+"");

            Long h = travel_time/ 60;
            Long m = travel_time % 60;
            holder.tv_travel_time.setText(h+"h "+m+"m");
//            holder.tv_break_point.setTypeface(holder.tv_break_point.getTypeface(), Typeface.BOLD);
//            holder.tv_break_point.setTextColor(context.getResources().getColor(R.color.black));
            holder.tv_break_point.setText("Non Stop");
        }


        holder.tv_from.setText(items.getSegments().get(0).getOrigin().getAirport().getAirportCode());
        if (!items.getSegments().get(0).getOrigin().getAirport().getTerminal().equals(""))
        {
            holder.tv_terminal.setText("T"+items.getSegments().get(0).getOrigin().getAirport().getTerminal());
        }


        String dep_date=items.getSegments().get(0).getOrigin().getDepTime();

        if (!dep_date.equals(""))
        {

            String formatedtime=dep_date.substring(dep_date.length()-8,dep_date.length()-3);

            try {
                String parsedDate=getparsedDate(dep_date);

                holder.tv_depart_time.setText(formatedtime+" ("+parsedDate+")");

            } catch (Exception e) {
                throw new RuntimeException(e);
            }

        }
        else {
            holder.tv_depart_time.setText(dep_date);
        }

        holder.tv_to.setText(items.getSegments().get(0).getDestination().getAirport().getAirportCode());

        if (!items.getSegments().get(0).getDestination().getAirport().getTerminal().equals(""))
        {
            holder.tv_to_terminal.setText("T"+items.getSegments().get(0).getDestination().getAirport().getTerminal());
        }

        String arrival_date=items.getSegments().get(0).getDestination().getArrTime();

        if (!arrival_date.equals("")) {

            String formatedtime=arrival_date.substring(arrival_date.length()-8,arrival_date.length()-3);

            try {
                String parsedDate=getparsedDate(arrival_date);

                holder.tv_arrival_time.setText(formatedtime+" ("+parsedDate+")");

            } catch (Exception e) {
                throw new RuntimeException(e);
            }

        }
        else {
            holder.tv_arrival_time.setText(arrival_date);
        }

        if (items.isRefundable()) {
            holder.tv_refundable.setText("Refundable");
        }
        else {
            holder.tv_refundable.setText("Non-Refundable");
        }

        if (items.getSegments().get(0).getNoOfSeatAvailable()<5)
        {
            holder.tv_available_seat.setVisibility(View.VISIBLE);

            if (items.getSegments().get(0).getNoOfSeatAvailable()>1) {
                holder.tv_available_seat.setText(items.getSegments().get(0).getNoOfSeatAvailable() + " Seats Available");
            }
            else {
                holder.tv_available_seat.setText(items.getSegments().get(0).getNoOfSeatAvailable() + " Seat Available");
            }
        }
        else {
            holder.tv_available_seat.setVisibility(View.GONE);
        }



        holder.tv_overlay.setText("");

    }


    private void mShowMultipleStops(Result items, ViewHolder holder) {

        Log.e("data","multiple "+new Gson().toJson(items));

        holder.tv_airline_name.setText(items.getSegments().get(0).getAirline().getAirlineName());
        holder.tv_flight_no.setText(items.getSegments().get(0).getAirline().getAirlineCode()+""+items.getSegments().get(0).getAirline().getFlightNumber());

        try {
            Fare fare=items.getFare();
            double price=fare.getBaseFare()+fare.getTax()+fare.getYqtax()+fare.getAdditionalTxnFeeOfrd()+fare.getAdditionalTxnFeePub()+fare.getPgcharge()+fare.getOtherCharges();
            holder.tv_price.setText("Rs " + price);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }



        holder.tv_from.setText(items.getSegments().get(0).getOrigin().getAirport().getAirportCode());
        if (!items.getSegments().get(0).getOrigin().getAirport().getTerminal().equals(""))
        {
            holder.tv_terminal.setText("T"+items.getSegments().get(0).getOrigin().getAirport().getTerminal());
        }


        String dep_date=items.getSegments().get(0).getOrigin().getDepTime();

        if (!dep_date.equals(""))
        {

            String formatedtime=dep_date.substring(dep_date.length()-8,dep_date.length()-3);

            try {
                String parsedDate=getparsedDate(dep_date);

                holder.tv_depart_time.setText(formatedtime+" ("+parsedDate+")");

            } catch (Exception e) {
                throw new RuntimeException(e);
            }

        }
        else {
            holder.tv_depart_time.setText(dep_date);
        }



        StringBuilder stringBuilder=new StringBuilder();

        int travel_time = 0;

        int total_on_ground=0;

        ArrayList over_lay_time=new ArrayList();
        ArrayList over_lay_name=new ArrayList();

        for (int i=0; i<items.getSegments().size(); i++)
        {
            Segment segment=items.getSegments().get(i);
//            segment.getAccumulatedDuration();
            Log.e("multi stop ","Segment "+i+(new Gson().toJson(segment)));

//            Log.e("dest "+i,"names in multi "+segment.getDestination().getAirport().getAirportCode());

            if (i!=items.getSegments().size()-1)
            {

                stringBuilder.append(" - ").append(segment.getDestination().getAirport().getAirportCode()).append(" - ");
            }

            Log.e("dura "+i,"seg "+segment.getDuration());

            travel_time+=segment.getDuration();

            travel_time+=segment.getGroundTime();

            total_on_ground+=segment.getGroundTime();



            if (segment.getGroundTime()!=0) {
                int ground_h = (total_on_ground / 60);
                int ground_m = (total_on_ground % 60);

                over_lay_time.add(ground_h + "h " + ground_m + "m");
                over_lay_name.add(segment.getOrigin().getAirport().getCityName());
            }

        }


        holder.tv_break_point.setText(stringBuilder.toString());


        int h = travel_time/ 60;
        int m = travel_time % 60;
        holder.tv_travel_time.setText(h+"h "+m+"m");



//        int ground_h = travel_time/ 60;
//        int ground_m = travel_time % 60;

        StringBuilder stringBuilder1=new StringBuilder();

        for (int i=0; i<over_lay_time.size(); i++)
        {
            stringBuilder1.append(over_lay_time.get(i)).append(" Layover at "+over_lay_name.get(i));
        }
        holder.tv_overlay.setText(stringBuilder1);



        List<Segment> segmentList=items.getSegments();
        Segment segment=items.getSegments().get(segmentList.size()-1);

        if (segmentList.size()>0) {

            holder.tv_to.setText(segment.getDestination().getAirport().getAirportCode());

            if (!segment.getDestination().getAirport().getTerminal().equals(""))
            {
                holder.tv_to_terminal.setText("T"+segment.getDestination().getAirport().getTerminal());
            }

            String arrival_date = segment.getDestination().getArrTime();

            if (!arrival_date.equals("")) {

                String formatedtime = arrival_date.substring(arrival_date.length() - 8, arrival_date.length() - 2);

                try {
                    String parsedDate = getparsedDate(arrival_date);

                    holder.tv_arrival_time.setText(formatedtime + " (" + parsedDate + ")");
                }
                catch (Exception e) {
                    throw new RuntimeException(e);
                }

            } else {
                holder.tv_arrival_time.setText(arrival_date);
            }

            if (items.isRefundable()) {
                holder.tv_refundable.setText("Refundable");
            }
            else {
                holder.tv_refundable.setText("Non-Refundable");
            }

            if (segment.getNoOfSeatAvailable() < 5) {
                holder.tv_available_seat.setVisibility(View.VISIBLE);

                if (segment.getNoOfSeatAvailable() > 1) {
                    holder.tv_available_seat.setText(segment.getNoOfSeatAvailable() + " Seats Available");
                } else {
                    holder.tv_available_seat.setText(segment.getNoOfSeatAvailable() + " Seat Available");
                }
            }
            else {
                holder.tv_available_seat.setVisibility(View.GONE);
            }
        }
    }

    protected void mShowBreakPointFlight(List<Segment> segment, ViewHolder holder, boolean isShow)
    {
        if (isShow)
        {
            holder.rv_multi_stop.setLayoutManager(new LinearLayoutManager(context));
            holder.rv_multi_stop.setAdapter(new MultiStopAdapter(context,segment));
            holder.rv_multi_stop.setVisibility(View.VISIBLE);
        }
        else {
            holder.rv_multi_stop.setVisibility(View.GONE);
        }
    }
}