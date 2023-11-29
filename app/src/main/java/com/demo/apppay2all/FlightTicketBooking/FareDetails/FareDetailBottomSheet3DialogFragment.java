package com.demo.apppay2all.FlightTicketBooking.FareDetails;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.widget.NestedScrollView;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.demo.apppay2all.DetectConnection;
import com.demo.apppay2all.FlightTicketBooking.FlightDetails.FareAdapter;
import com.demo.apppay2all.FlightTicketBooking.FlightDetails.FlightDetailViewModel;
import com.demo.apppay2all.FlightTicketBooking.FlightDetails.FlightDetailsModel;
import com.demo.apppay2all.R;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

/**
 * Created by Basant on 3/29/2018.
 */

public class FareDetailBottomSheet3DialogFragment extends BottomSheetDialogFragment {

    private BottomSheetBehavior mBehavior;

    public static FareDetailBottomSheet3DialogFragment dialogFragment;

    private BottomSheetBehavior.BottomSheetCallback mBottomSheetBehaviorCallback = new BottomSheetBehavior.BottomSheetCallback() {

        @Override
        public void onStateChanged(@NonNull View bottomSheet, int newState) {
            if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                dismiss();
            }
        }
        @Override
        public void onSlide(@NonNull View bottomSheet, float slideOffset) {
        }
    };

    FlightDetailViewModel flightDetailViewModel;

    NestedScrollView nsv_detail;

    ImageView iv_flight_icon;
    TextView tv_flight_name,tv_flight_no;

    TextView tv_from_city,tv_to_city;
    TextView tv_depart_time,tv_time_duration,tv_arrive_time;
    TextView tv_depart_date,tv_arrive_date;
    TextView tv_depart_airport,tv_arrive_airport;

    RecyclerView rv_fare;

    ImageView iv_loader;
    TextView tv_baggage,tv_cabine_baggage;

    String tracid="",adult="1",child="0",infant="",indexid="";

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        BottomSheetDialog dialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);

        View view = View.inflate(getContext(), R.layout.bottom_sheet_layout_flight_detail, null);

        LinearLayout linearLayout = view.findViewById(R.id.root);
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) linearLayout.getLayoutParams();
        params.height = Resources.getSystem().getDisplayMetrics().heightPixels;
        linearLayout.setLayoutParams(params);

        if (getArguments().containsKey("tracid"))
        {
            tracid=getArguments().getString("tracid");
        }

        if (getArguments().containsKey("adult"))
        {
            adult=getArguments().getString("adult");
        }

        if (getArguments().containsKey("child"))
        {
            child=getArguments().getString("child");
        }

        if (getArguments().containsKey("infant"))
        {
            infant=getArguments().getString("infant");
        }

        if (getArguments().containsKey("indexid"))
        {
            indexid=getArguments().getString("indexid");
        }

        dialogFragment=this;

        view.findViewById(R.id.fakeShadow).setVisibility(View.VISIBLE);

        dialog.setContentView(view);
        mBehavior = BottomSheetBehavior.from((View) view.getParent());

        dialog.getActionBar();


        nsv_detail=view.findViewById(R.id.nsv_detail);
        iv_flight_icon=view.findViewById(R.id.iv_flight_icon);

        tv_flight_name=view.findViewById(R.id.tv_flight_name);
        tv_flight_no=view.findViewById(R.id.tv_flight_no);

        tv_from_city=view.findViewById(R.id.tv_from_city);
        tv_to_city=view.findViewById(R.id.tv_to_city);

        tv_depart_time=view.findViewById(R.id.tv_depart_time);
        tv_time_duration=view.findViewById(R.id.tv_time_duration);
        tv_arrive_time=view.findViewById(R.id.tv_arrive_time);

        tv_depart_date=view.findViewById(R.id.tv_depart_date);
        tv_arrive_date=view.findViewById(R.id.tv_arrive_date);

        tv_depart_airport=view.findViewById(R.id.tv_depart_airport);
        tv_arrive_airport=view.findViewById(R.id.tv_arrive_airport);

        rv_fare=view.findViewById(R.id.rv_fare);

        iv_loader=view.findViewById(R.id.iv_loader);

        tv_baggage=view.findViewById(R.id.tv_baggage);
        tv_cabine_baggage=view.findViewById(R.id.tv_cabine_baggage);

        flightDetailViewModel=new ViewModelProvider(getActivity()).get(FlightDetailViewModel.class);
        flightDetailViewModel.getMutableLiveData().observe(getActivity(), new Observer<FlightDetailsModel>() {
            @Override
            public void onChanged(FlightDetailsModel flightDetailsModel) {

                nsv_detail.setVisibility(View.VISIBLE);
                iv_loader.setVisibility(View.GONE);
                iv_loader.setImageBitmap(null);

                if (flightDetailsModel!=null)
                {
                    rv_fare.setAdapter(new FareAdapter(getActivity(),flightDetailsModel.getFareRules()));
                }

                if (flightDetailsModel.getFlightDetails()!=null) {
                    tv_flight_name.setText(flightDetailsModel.getFlightDetails().getAirline().getAirlineName());

                    String airlinecode = flightDetailsModel.getFlightDetails().getAirline().getAirlineCode();
                    String flightno = flightDetailsModel.getFlightDetails().getAirline().getFlightNumber();

                    tv_flight_no.setText(airlinecode + "-" + flightno);

                    tv_from_city.setText(flightDetailsModel.getFlightDetails().getDeparture().getCityName());
                    tv_to_city.setText(flightDetailsModel.getFlightDetails().getArrival().getCityName());

                    tv_depart_time.setText(flightDetailsModel.getFlightDetails().getDeparture().getDepDateTime().getTime());
                    tv_arrive_time.setText(flightDetailsModel.getFlightDetails().getArrival().getArrDateTime().getTime());

                    tv_time_duration.setText(flightDetailsModel.getFlightDetails().getDuration());

                    tv_depart_date.setText(flightDetailsModel.getFlightDetails().getDeparture().getDepDateTime().getDate());
                    tv_arrive_date.setText(flightDetailsModel.getFlightDetails().getArrival().getArrDateTime().getDate());

                    String terminal_depa=flightDetailsModel.getFlightDetails().getDeparture().getTerminal();
                    String terminal_arr=flightDetailsModel.getFlightDetails().getArrival().getTerminal();

                    if (!terminal_depa.equals(""))
                    {
                        tv_depart_airport.setText("(T"+terminal_depa+")"+flightDetailsModel.getFlightDetails().getDeparture().getAirportName());
                    }
                    else {
                        tv_depart_airport.setText(flightDetailsModel.getFlightDetails().getDeparture().getAirportName());
                    }

                    if (!terminal_arr.equals(""))
                    {
                        tv_arrive_airport.setText("(T"+terminal_arr+")"+flightDetailsModel.getFlightDetails().getArrival().getAirportName());
                    }
                    else {
                        tv_arrive_airport.setText(flightDetailsModel.getFlightDetails().getArrival().getAirportName());
                    }

                }
            }
        });

        if (DetectConnection.checkInternetConnection(getActivity()))
        {
            mGetDetail();
        }
        else {
            dialogFragment.dismiss();
            Toast.makeText(getContext(), "No internet connection", Toast.LENGTH_SHORT).show();
        }

        return dialog;
    }

    @SuppressLint("RestrictedApi")
    @Override
    public void setupDialog(Dialog dialog, int style) {
        super.setupDialog(dialog, style);

    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);

    }

    protected void mGetDetail()
    {
        String token="BGfDenjFWiOgiVW67m8LI7Y24xPmhuY0X6IFVCg49U9iExFwMPLVYjbQLhRh";
        flightDetailViewModel.mCallAPI(token,tracid,indexid,adult,child,infant);
        nsv_detail.setVisibility(View.GONE);
        iv_loader.setVisibility(View.VISIBLE);
        Glide.with(getActivity()).load(R.drawable.flight_flying).into(iv_loader);
    }
}