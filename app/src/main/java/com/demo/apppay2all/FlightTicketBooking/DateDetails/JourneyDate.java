package com.demo.apppay2all.FlightTicketBooking.DateDetails;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;

import com.demo.apppay2all.FlightTicketBooking.FlightSearch;
import com.demo.apppay2all.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class JourneyDate extends Fragment {

    CalendarView cv_date;

    String date_type="depart";
    String depart_date="";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.activity_journey_date,container,false);

        if (getArguments().containsKey("type"))
        {
            date_type=getArguments().getString("type");
        }

        if (getArguments().containsKey("depart_date"))
        {
            depart_date=getArguments().getString("depart_date");
        }

        cv_date=view.findViewById(R.id.cv_date);

//        Calendar calendar = Calendar.getInstance();
//        calendar.set(Calendar.DATE,Calendar.getInstance().getActualMinimum(Calendar.DATE));
//        long date = calendar.getTime().getTime();

        if (date_type.equalsIgnoreCase("return"))
        {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            Date date = null;
            try {
                date = formatter.parse(depart_date);
            } catch (Exception e) {
                e.printStackTrace();
            }
            long timeInMillis = date.getTime();
            cv_date.setMinDate(timeInMillis);
        }
        else {
            cv_date.setMinDate(System.currentTimeMillis());
        }

        cv_date.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int i, int i1, int i2) {

                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

//                String date=formatter.format(calendarView.getDate());

                SimpleDateFormat formatterDay = new SimpleDateFormat("EEE");
                SimpleDateFormat formatterMonth = new SimpleDateFormat("dd-MMM");

                Calendar calendar=Calendar.getInstance();
                calendar.set(i,i1,i2);
                String day=formatterDay.format(calendar.getTime());
                String month_name=formatterMonth.format(calendar.getTime());

//                Intent intent=new Intent();
//                intent.putExtra("date",i+"-"+i1+"-"+i2);
//                intent.putExtra("day",day);
//                setResult(1422,intent);
//                finish();

                String month="",month_day="";

                if (i1<9)
                {
                    month="0"+(i1+1);
                }

                else{
                   month=""+(i1+1);
                }

                if (i2<10)
                {
                    month_day="0"+i2;
                }

                else{
                   month_day=""+i2;
                }

                ((FlightSearch)getActivity()).mGetDate(i + "-" + month + "-" + month_day,day,month_name);
                getFragmentManager().popBackStack();

            }
        });

        return view;
    }

//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_journey_date);
//
//        if (getSupportActionBar()!=null)
//        {
//            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        }
//
//        if (getIntent().hasExtra("type"))
//        {
//            date_type=getIntent().getStringExtra("type");
//        }
//
//        if (getIntent().hasExtra("depart_date"))
//        {
//            depart_date=getIntent().getStringExtra("depart_date");
//        }
//
//        cv_date=findViewById(R.id.cv_date);
//
////        Calendar calendar = Calendar.getInstance();
////        calendar.set(Calendar.DATE,Calendar.getInstance().getActualMinimum(Calendar.DATE));
////        long date = calendar.getTime().getTime();
//
//        if (date_type.equalsIgnoreCase("return"))
//        {
//            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
//            Date date = null;
//            try {
//                date = formatter.parse(depart_date);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//            long timeInMillis = date.getTime();
//            cv_date.setMinDate(timeInMillis);
//        }
//        else {
//            cv_date.setMinDate(System.currentTimeMillis());
//        }
//
//        cv_date.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
//            @Override
//            public void onSelectedDayChange(@NonNull CalendarView calendarView, int i, int i1, int i2) {
//
//                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
//
////                String date=formatter.format(calendarView.getDate());
//
//                SimpleDateFormat formatterDay = new SimpleDateFormat("EEE");
//
//                Calendar calendar=Calendar.getInstance();
//                calendar.set(i,i1,i2);
//                String day=formatterDay.format(calendar.getTime());
//
//                Intent intent=new Intent();
//                intent.putExtra("date",i+"-"+i1+"-"+i2);
//                intent.putExtra("day",day);
//                setResult(1422,intent);
//                finish();
//
//            }
//        });
//
//    }

//    @Override
//    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
//        if (item.getItemId()==android.R.id.home)
//        {
//            finish();
//        }
//        return super.onOptionsItemSelected(item);
//    }
}