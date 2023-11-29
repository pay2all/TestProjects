package com.demo.apppay2all.FlightTicketBooking;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.demo.apppay2all.DetectConnection;
import com.demo.apppay2all.FlightTicketBooking.CityAirport.CityFromDestination;
import com.demo.apppay2all.FlightTicketBooking.CityAirport.CityItems;
import com.demo.apppay2all.FlightTicketBooking.DateDetails.JourneyDate;
import com.demo.apppay2all.FlightTicketBooking.FlightListDetail.FlightList;
import com.demo.apppay2all.R;

import java.text.SimpleDateFormat;
import java.util.Date;

public class FlightSearch extends AppCompatActivity {

    RadioGroup rg_way;

    RadioButton rb_one_way,rb_return,rb_multi,rb_advance,rb_special_return;

    ImageView iv_back;

    String city_type="from";
    String date_type="depart";

    LinearLayout ll_from,ll_destination;
    TextView tv_from_code,tv_from_name;
    TextView tv_dest_code,tv_dest_name;

    String from_code="DEL",dest_code="BOM";

    LinearLayout ll_depart_date,ll_return;

    TextView tv_depart,tv_depart_day;
    TextView tv_return_date,tv_return_day;

    String depart_date="";
    String return_date="";

    String journey_type="1";

    LinearLayout ll_travellers;
    TextView tv_travellers;

    int adult=1,child=0,infant=0;

    String travel="";

    String travel_class="1";

    LinearLayout ll_class;
    TextView tv_class;

    Button bt_search;

    String month_name_depart="",month_name_return="";

    RelativeLayout rl_exchange;
    String change_value="",change_text="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flight_search);

        if (getSupportActionBar()!=null)
        {
            getSupportActionBar().hide();
        }

        rl_exchange=findViewById(R.id.rl_exchange);
        rl_exchange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                change_value=from_code;
                from_code=dest_code;
                dest_code=change_value;

                change_text=tv_from_name.getText().toString();
                tv_from_name.setText(tv_dest_name.getText().toString());
                tv_dest_name.setText(change_text);

                tv_from_code.setText(from_code);
                tv_dest_code.setText(dest_code);
            }
        });

        ll_from=findViewById(R.id.ll_from);
        ll_from.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                city_type="from";
                startActivityForResult(new Intent(FlightSearch.this, CityFromDestination.class),1421);
            }
        });

        ll_destination=findViewById(R.id.ll_destination);
        ll_destination.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                city_type="dest";
                startActivityForResult(new Intent(FlightSearch.this, CityFromDestination.class),1421);
            }
        });

        tv_from_code=findViewById(R.id.tv_from_code);
        tv_from_name=findViewById(R.id.tv_from_name);
        tv_dest_code=findViewById(R.id.tv_dest_code);
        tv_dest_name=findViewById(R.id.tv_dest_name);

        iv_back=findViewById(R.id.iv_back);
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        rg_way=findViewById(R.id.rg_way);
        rg_way.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {

            }
        });

        rb_one_way=findViewById(R.id.rb_one_way);
        rb_return=findViewById(R.id.rb_return);
        rb_multi=findViewById(R.id.rb_multi);
        rb_advance=findViewById(R.id.rb_advance);
        rb_special_return=findViewById(R.id.rb_special_return);

        tv_depart=findViewById(R.id.tv_depart);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

        depart_date = formatter.format(new Date(System.currentTimeMillis()));
        tv_depart.setText(depart_date);

        SimpleDateFormat formatterMonth = new SimpleDateFormat("dd-MMM");
        month_name_depart=formatterMonth.format(new Date(System.currentTimeMillis()));

        tv_depart_day=findViewById(R.id.tv_depart_day);

        ll_depart_date=findViewById(R.id.ll_depart_date);
        ll_depart_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                date_type="depart";
//                Intent intent=new Intent(FlightSearch.this, JourneyDate.class);
//                intent.putExtra("type",date_type);
//                startActivityForResult(intent,1422);

                Fragment fragment=new JourneyDate();
                Bundle bundle=new Bundle();
                bundle.putString("type",date_type);
                bundle.putString("depart_date",depart_date);
                fragment.setArguments(bundle);

                FragmentManager fragmentManager=getSupportFragmentManager();

                fragmentManager.beginTransaction().add(R.id.ll_root,fragment).addToBackStack(null).show(fragment).commit();
            }
        });

        ll_return=findViewById(R.id.ll_return);
        ll_return.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                date_type="return";
                rb_return.setSelected(true);
//                Intent intent=new Intent(FlightSearch.this, JourneyDate.class);
//                intent.putExtra("type",date_type);
//                intent.putExtra("depart_date",depart_date);
//                startActivityForResult(intent,1422);

                Fragment fragment=new JourneyDate();
                Bundle bundle=new Bundle();
                bundle.putString("type",date_type);
                bundle.putString("depart_date",depart_date);
                fragment.setArguments(bundle);

                FragmentManager fragmentManager=getSupportFragmentManager();

                fragmentManager.beginTransaction().add(R.id.ll_root,fragment).addToBackStack(null).commit();
            }
        });

        tv_return_date=findViewById(R.id.tv_return_date);
        tv_return_day=findViewById(R.id.tv_return_day);

        ll_travellers=findViewById(R.id.ll_travellers);
        ll_travellers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mShowSelectTraveler();
            }
        });

        tv_travellers=findViewById(R.id.tv_travellers);

        ll_class=findViewById(R.id.ll_class);
        tv_class=findViewById(R.id.tv_class);
        ll_class.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mShowClass();
            }
        });

        bt_search=findViewById(R.id.bt_search);
        bt_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (DetectConnection.checkInternetConnection(FlightSearch.this))
                {

                    Intent intent=new Intent(FlightSearch.this, FlightList.class);
                    intent.putExtra("from_city",tv_from_name.getText().toString());
                    intent.putExtra("to_city",tv_dest_name.getText().toString());
                    intent.putExtra("from",from_code);
                    intent.putExtra("to",dest_code);
                    intent.putExtra("date",depart_date);
                    intent.putExtra("adult",adult);
                    intent.putExtra("child",child);
                    intent.putExtra("infant",infant);
                    intent.putExtra("class",travel_class);
                    intent.putExtra("journey_type",journey_type);
                    intent.putExtra("depart_month",month_name_depart);
                    intent.putExtra("return_month",month_name_return);
                    startActivity(intent);

                    Log.e("journey","date "+month_name_depart);

                }
                else {
                    Toast.makeText(FlightSearch.this, "No internet connection", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode==1421)
        {
            CityItems items=(CityItems)data.getSerializableExtra("DATA");
            if (city_type.equals("from"))
            {
                tv_from_name.setText(items.getCityName());
                tv_from_code.setText(items.getAirportcode());
                from_code=items.getAirportcode();
            }
            else {
                tv_dest_name.setText(items.getCityName());
                tv_dest_code.setText(items.getAirportcode());
                dest_code=items.getAirportcode();
            }

        }
       else if (requestCode==1422)
        {
            if (data.hasExtra("date")) {
                String date = data.getStringExtra("date");
                String day = data.getStringExtra("day");

                Log.e("date", "selected " + date);

                if (date_type.equalsIgnoreCase("return")) {
                    tv_return_date.setText(date);
                    tv_return_day.setText(day);
                    return_date = date;
                }
                else {
                    tv_depart.setText(date);
                    tv_depart_day.setText(day);

                    depart_date = date;
                }
            }
//            if (city_type.equals("from"))
//            {
//                tv_from_name.setText(items.getCityName());
//                tv_from_code.setText(items.getAirportcode());
//            }
//            else {
//                tv_dest_name.setText(items.getCityName());
//                tv_dest_code.setText(items.getAirportcode());
//            }
        }

    }

//    public void mGetCityName(CityItems cityItems)
//    {
//        if ()
//    }

    protected void mShowSelectTraveler()
    {
        LayoutInflater inflater2 = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater2.inflate(R.layout.custome_dialog_traveler_detail_layout, null);


        TextView tv_adlt=v.findViewById(R.id.tv_adlt);

        TextView tv_adlt_count=v.findViewById(R.id.tv_adlt_count);
        ImageView iv_adlt_add=v.findViewById(R.id.iv_adlt_add);

        TextView tv_children=v.findViewById(R.id.tv_children);
        ImageView iv_chil_minus=v.findViewById(R.id.iv_chil_minus);
        TextView tv_chil_count=v.findViewById(R.id.tv_chil_count);
        ImageView iv_chil_add=v.findViewById(R.id.iv_chil_add);

        TextView tv_infant=v.findViewById(R.id.tv_infant);
        ImageView iv_infant_minus=v.findViewById(R.id.iv_infant_minus);
        TextView tv_infant_count=v.findViewById(R.id.tv_infant_count);
        ImageView iv_infant_add=v.findViewById(R.id.iv_infant_add);


        tv_adlt.setText(adult+" Adult");
        tv_adlt_count.setText(adult+"");

        tv_children.setText(child+" Children");
        tv_chil_count.setText(child+"");

        tv_infant.setText(infant+" Infant");
        tv_infant_count.setText(infant+"");

        ImageView iv_adlt_minus=v.findViewById(R.id.iv_adlt_minus);
        iv_adlt_minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (adult>1)
                {
                    adult=adult-1;
                }

                tv_adlt.setText(adult+" Adult");
                tv_adlt_count.setText(adult+"");
            }
        });

        iv_adlt_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (adult<9)
                {
                    adult=adult+1;
                }


                tv_adlt.setText(adult + " Adult");
                tv_adlt_count.setText(adult+"");
            }
        });

        iv_chil_minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (child>0)
                {
                    child=child-1;
                }

                tv_children.setText(child+" Children");
                tv_chil_count.setText(child+"");
            }
        });

        iv_chil_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (child<9)
                {
                    child=child+1;
                }


                tv_children.setText(child+" Children");
                tv_chil_count.setText(child+"");
            }
        });

        iv_infant_minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (infant>0)
                {
                    infant=infant-1;
                }

                tv_infant.setText(infant+" Infant");
                tv_infant_count.setText(infant+"");
            }
        });
        iv_infant_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (infant<9)
                {
                    infant=infant+1;
                }

                tv_infant.setText(infant+" Infant");
                tv_infant_count.setText(infant+"");
            }
        });


        Button bt_cancel=v.findViewById(R.id.bt_cancel);
        Button bt_ok=v.findViewById(R.id.bt_ok);

        final AlertDialog.Builder builder2 = new AlertDialog.Builder(FlightSearch.this);
        builder2.setCancelable(false);
        builder2.setView(v);


      AlertDialog  alertDialog = builder2.create();
      bt_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });


        bt_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                travel=adult+" Adult";
                if (child>0)
                {
                    travel=travel+", "+child+" Child";
                }

                if (infant>0)
                {
                    travel=travel+", "+infant+" Infant";
                }

                tv_travellers.setText(travel);

                alertDialog.dismiss();
            }
        });

        alertDialog.show();
    }
    protected void mShowClass()
    {
        LayoutInflater inflater2 = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater2.inflate(R.layout.custome_dialog_for_class, null);

        RadioGroup rb_class=v.findViewById(R.id.rb_class);

        final AlertDialog.Builder builder2 = new AlertDialog.Builder(FlightSearch.this);
        builder2.setCancelable(false);
        builder2.setView(v);

       AlertDialog  alertDialog = builder2.create();

        final String[] selected_class = {"All"};

        rb_class.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (i==R.id.rb_all)
                {
                    travel_class="1";
                    selected_class[0] ="All";
                }
                else if (i==R.id.rb_economy)
                {
                    travel_class="2";
                    selected_class[0] ="Economy";
                }
                else if (i==R.id.rb_premium_economy)
                {
                    travel_class="3";
                    selected_class[0] ="Premium Economy";
                }
                else if (i==R.id.rb_business)
                {
                    travel_class="4";
                    selected_class[0] ="Business";
                }
                else if (i==R.id.rb_premium_business)
                {
                    travel_class="5";
                    selected_class[0] ="Premium Business";
                }
                else if (i==R.id.rb_first)
                {
                    travel_class="6";
                    selected_class[0] ="First Class";
                }

                tv_class.setText(selected_class[0]);

                alertDialog.dismiss();

                Log.e("class id "+travel_class,"class name "+selected_class[0]);

            }
        });


        alertDialog.show();
    }

    public void mGetDate(String date,String day,String month_name)
    {
        Log.e("date", "selected " + date);

        if (date_type.equalsIgnoreCase("return")) {
            tv_return_date.setText(date);
            tv_return_day.setText(day);
            return_date = date;

            month_name_return=month_name;
        }
        else {
            tv_depart.setText(date);
            tv_depart_day.setText(day);

            depart_date = date;

            month_name_depart=month_name;
        }
    }
}