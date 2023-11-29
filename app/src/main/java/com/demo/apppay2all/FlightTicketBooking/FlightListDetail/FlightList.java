package com.demo.apppay2all.FlightTicketBooking.FlightListDetail;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.demo.apppay2all.DetectConnection;
import com.demo.apppay2all.R;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.util.ArrayList;
import java.util.List;

public class FlightList extends AppCompatActivity {

    LinearLayout ll_flight_list;
    TextView tv_from_to,tv_date,tv_traveler,tv_class;
    RelativeLayout rl_edit;

    RecyclerView rv_flight_list;

    ImageView iv_loading;

    String from_city="",to_city="",from="",to="";
    int adult=1,child=0,infant=0;
    String date="";
    String journey_type="1",travel_class="1";

    FlightViewModel flightViewModel;

    ImageView iv_edit;

    String depart_month="",return_month="";

    String tracid="";

    String DirectFlight="false";
    String OneStopFlight="false";

    FlightListAdapter flightListAdapter;
    List<Result> segmentList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flight_list);

        if (getSupportActionBar()!=null)
        {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        iv_edit=findViewById(R.id.iv_edit);
        iv_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        if (getIntent().hasExtra("depart_month"))
        {
            depart_month=getIntent().getStringExtra("depart_month");
        }

        if (getIntent().hasExtra("return_month"))
        {
            return_month=getIntent().getStringExtra("return_month");
        }

        if (getIntent().hasExtra("from_city"))
        {
            from_city=getIntent().getStringExtra("from_city");
        }

        if (getIntent().hasExtra("to_city"))
        {
            to_city=getIntent().getStringExtra("to_city");
        }

        if (getIntent().hasExtra("from"))
        {
            from=getIntent().getStringExtra("from");
        }

        if (getIntent().hasExtra("to"))
        {
            to=getIntent().getStringExtra("to");
        }

        if (getIntent().hasExtra("date"))
        {
            date=getIntent().getStringExtra("date");
        }

        if (getIntent().hasExtra("adult"))
        {
            adult=getIntent().getIntExtra("adult",1);
        }

        if (getIntent().hasExtra("child"))
        {
            child=getIntent().getIntExtra("child",0);
        }

        if (getIntent().hasExtra("infant"))
        {
            infant=getIntent().getIntExtra("infant",0);
        }

        if (getIntent().hasExtra("class"))
        {
            travel_class=getIntent().getStringExtra("class");
        }

        if (getIntent().hasExtra("journey_type"))
        {
            journey_type=getIntent().getStringExtra("journey_type");
        }

        Log.e("date","on list page "+date);

        ll_flight_list=findViewById(R.id.ll_flight_list);

        tv_from_to=findViewById(R.id.tv_from_to);
        tv_from_to.setText(from_city+" to "+to_city);

        tv_date=findViewById(R.id.tv_date);
        tv_date.setText(depart_month);


        tv_traveler=findViewById(R.id.tv_traveler);
        tv_traveler.setText((adult+child+infant)+" Travelers");

        tv_class=findViewById(R.id.tv_class);

        String class_name="All";

        if (travel_class.equals("2"))
        {
            class_name="Economy";
        }
        else if (travel_class.equals("3"))
        {
            class_name="Premium Economy";
        }
        else if (travel_class.equals("4"))
        {
            class_name="Business";
        }
        else if (travel_class.equals("5"))
        {
            class_name="Premium Business";
        }
        else if (travel_class.equals("6"))
        {
            class_name="First Class";
        }


        tv_class.setText(class_name);

        rl_edit=findViewById(R.id.rl_edit);


        rv_flight_list=findViewById(R.id.rv_flight_list);
        rv_flight_list.setHasFixedSize(true);
        rv_flight_list.setHasTransientState(true);
        rv_flight_list.setLayoutManager(new LinearLayoutManager(FlightList.this));
        segmentList=new ArrayList<>();
        flightListAdapter=new FlightListAdapter(FlightList.this,segmentList);
        rv_flight_list.setAdapter(flightListAdapter);

        iv_loading=findViewById(R.id.iv_loading);

        flightViewModel=new ViewModelProvider(FlightList.this).get(FlightViewModel.class);
        flightViewModel.mutableLiveData.observe(FlightList.this, new Observer<JsonElement>() {
            @Override
            public void onChanged(JsonElement flightResponse) {

                Log.e("data","response "+flightResponse);


                try {
                    String responsedata = flightResponse.toString().replaceAll("\\[\\[", "\\[").replaceAll("\\]\\]", "\\]");

                    JsonElement mJson =  JsonParser.parseString(responsedata);
                    Gson gson = new Gson();
                    FlightListModel flightListItemKotlin = gson.fromJson(mJson, FlightListModel.class);

                    if (flightListItemKotlin!=null) {
                        if (!flightListItemKotlin.getStatus().equals("")) {
                            if (flightListItemKotlin.getStatus().equalsIgnoreCase("success")) {

                                //                                    flightListAdapter.notifyDataSetChanged();
                                segmentList.addAll(flightListItemKotlin.getResponse().getResults());
                            tracid = flightListItemKotlin.getResponse().getTraceId();
                            }
                            else if (!flightListItemKotlin.getMessage().equals("")) {
                                mShowDialogMessage(flightListItemKotlin.getMessage());
                            }
                            else {
                                mShowDialogMessage("Something went wrong, please try again later");
                            }

                        }
                        else {
                            mShowDialogMessage("Something went wrong, please try again later");
                        }
                    }
                    else {
                        mShowDialogMessage("Something went wrong, please try again later");
                    }
                }
                catch (Exception e)
                {
                    e.getMessage();
                }


                ll_flight_list.setVisibility(View.VISIBLE);
                iv_loading.setVisibility(View.GONE);
                iv_loading.setImageBitmap(null);
            }
        });

        if (DetectConnection.checkInternetConnection(FlightList.this))
        {
            mGetFLights();
        }
        else {
            Toast.makeText(this, "No internet connection", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId()==android.R.id.home)
        {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    protected void mGetFLights()
    {
        ll_flight_list.setVisibility(View.GONE);
        iv_loading.setVisibility(View.VISIBLE);
        Glide.with(FlightList.this).load(R.drawable.flight_flying).into(iv_loading);

        String token="BGfDenjFWiOgiVW67m8LI7Y24xPmhuY0X6IFVCg49U9iExFwMPLVYjbQLhRh";
        flightViewModel.mCallAPI(token,journey_type,from,to,date,adult+"",child+"",infant+"",travel_class+"",DirectFlight,OneStopFlight);
    }

    public String mGetTracid()
    {
        return tracid;
    }

    public String mGetAdult()
    {
        return adult+"";
    }

    public String mGetChild()
    {
        return child+"";
    }

    public String mGetInfant()
    {
        return infant+"";
    }

    protected void mShowDialogMessage(String message)
    {
        AlertDialog.Builder builder=new AlertDialog.Builder(FlightList.this);
        builder.setMessage(message);
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        AlertDialog alertDialog=builder.create();
        alertDialog.setCancelable(false);
        alertDialog.show();
    }

}