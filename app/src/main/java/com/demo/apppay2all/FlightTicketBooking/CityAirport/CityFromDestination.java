package com.demo.apppay2all.FlightTicketBooking.CityAirport;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.demo.apppay2all.DetectConnection;
import com.demo.apppay2all.R;
import com.google.gson.JsonElement;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class CityFromDestination extends AppCompatActivity {

    RecyclerView rv_airport;

    CityViewModel cityViewModel;

    SwipeRefreshLayout swipe_city;
    List<CityItems> cityItems;
    CityAdapter cityAdapter;

    EditText ed_search;

    String type="from";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_from_destination);

        if (getSupportActionBar()!=null)
        {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        ed_search=findViewById(R.id.ed_search);
        ed_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                if (!editable.toString().equals("")) {
                    if (cityItems.size() != 0) {
                        List<CityItems> temp = new ArrayList();
                        for (CityItems d : cityItems) {
                            if (d.getCityName().toLowerCase().contains(editable.toString().toLowerCase())||d.getDd().toLowerCase().contains(editable.toString().toLowerCase())) {
                                temp.add(d);
                            }
                        }
                        //update recyclerview
                        cityAdapter.UpdateList(temp);
                        Log.e("key", "=" + editable.toString());
                    }
                }
            }
        });

        swipe_city=findViewById(R.id.swipe_city);
        swipe_city.setColorSchemeColors(getResources().getColor(R.color.colorPrimaryDark));
        swipe_city.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                cityViewModel.mGetFromDestList("BGfDenjFWiOgiVW67m8LI7Y24xPmhuY0X6IFVCg49U9iExFwMPLVYjbQLhRh");
            }
        });


        rv_airport=findViewById(R.id.rv_airport);
        rv_airport.setLayoutManager(new LinearLayoutManager(CityFromDestination.this));
        cityItems=new ArrayList<>();
        cityAdapter=new CityAdapter(CityFromDestination.this,cityItems);
        rv_airport.setAdapter(cityAdapter);

        cityViewModel=new ViewModelProvider(CityFromDestination.this).get(CityViewModel.class);
        cityViewModel.getMutableLiveData().observe(CityFromDestination.this, new Observer<JsonElement>() {
            @Override
            public void onChanged(JsonElement jsonElement) {
                swipe_city.setRefreshing(false);


                String status="",message="";
                if (jsonElement!=null)
                {
                    try {
                        JSONObject jsonObject=new JSONObject(jsonElement+"");

                        if (jsonObject.has("status"))
                        {
                            status=jsonObject.getString("status");
                        }

                        if (jsonObject.has("message"))
                        {
                            message=jsonObject.getString("message");
                        }

                        if (jsonObject.has("list"))
                        {
                            JSONArray jsonArray=jsonObject.getJSONArray("list");
                            for (int i=0; i<jsonArray.length(); i++)
                            {
                                JSONObject data=jsonArray.getJSONObject(i);
                                CityItems items=new CityItems();
                                items.setDd(data.getString("dd"));
                                items.setCityName(data.getString("CityName"));
                                items.setAirportcode(data.getString("Airportcode"));
                                items.setCountryName(data.getString("CountryName"));
                                items.setCitycode(data.getString("Citycode"));
                                cityItems.add(items);
                                cityAdapter.notifyDataSetChanged();

                            }
                        }

                    }
                    catch (JSONException e)
                    {
                        e.printStackTrace();
                    }
                }
                else {
                    Toast.makeText(CityFromDestination.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                }
            }
        });


        if (DetectConnection.checkInternetConnection(CityFromDestination.this))
        {
            swipe_city.setRefreshing(true);
            cityViewModel.mGetFromDestList("BGfDenjFWiOgiVW67m8LI7Y24xPmhuY0X6IFVCg49U9iExFwMPLVYjbQLhRh");
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

    public void mSetOnActivityResult(CityItems cityItems)
    {
        Intent intent=new Intent();
        Bundle bundle=new Bundle();
        bundle.putSerializable("DATA",cityItems);
        intent.putExtras(bundle);
        intent.putExtra("data",cityItems.cityName);
        setResult(1421,intent);
        finish();
    }
}