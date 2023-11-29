package com.demo.apppay2all.ProviderDetail;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.location.Location;
import android.location.LocationListener;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.demo.apppay2all.BaseURL.BaseURL;
import com.demo.apppay2all.CallResAPIPOSTMethod;
import com.demo.apppay2all.DetectConnection;
import com.demo.apppay2all.R;

import com.demo.apppay2all.ServiceDetailSub.SubServiceItem;
import com.demo.apppay2all.SharePrefManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Operator extends AppCompatActivity implements LocationListener {

    RecyclerView recyclerview_operator;

    private List<Operators_Items> operators_items;

    String type,from;
    SubServiceItem service_data;
    public String activity_name;

    OperatorsCardAdapter operatorsCardAdapter=null;
    EditText edittext_search;

    ProgressDialog dialog;

    public static Activity mOperatorActivity=null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_operator);

        mOperatorActivity=this;

        if (getSupportActionBar()!=null)
        {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        recyclerview_operator=findViewById(R.id.recyclerview_operator);
        recyclerview_operator.setLayoutManager(new LinearLayoutManager(Operator.this));
        operators_items=new ArrayList<>();
        operatorsCardAdapter=new OperatorsCardAdapter(Operator.this,operators_items);
        recyclerview_operator.setAdapter(operatorsCardAdapter);

        Bundle intent=getIntent().getExtras();

        if (intent.containsKey("type"))
        {
            type=intent.getString("type");
        }
        if (intent.containsKey("activity"))
        {
            activity_name=intent.getString("activity");

        }

        if (intent.containsKey("from"))
        {
            from=intent.getString("from");

            assert from != null;
            if (from.equalsIgnoreCase("home"))
            {
                service_data=((SubServiceItem)getIntent().getSerializableExtra("DATA"));
                assert service_data != null;
                type=service_data.getService_id();
                activity_name=service_data.getService_name();
            }
        }

        getSupportActionBar().setTitle(activity_name+" Provider");


        edittext_search=findViewById(R.id.edittext_search);
        edittext_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                if (operators_items!=null) {
                    List<Operators_Items> temp = new ArrayList();
                    for (Operators_Items d : operators_items) {
                        //or use .equal(text) with you want equal match
                        //use .toLowerCase() for better matches
                        if (d.getOperator_name().toLowerCase().contains(editable.toString().toLowerCase())) {
                            temp.add(d);
                        }
                    }

                    //update recyclerview
                    operatorsCardAdapter.UpdateList(temp);
                }
            }
        });

        long current_time=System.currentTimeMillis();
//        String login_time=SharePrefManager.getInstance(getActivity()).mGetSharePrefSingleData("bank_list1_time");
        long save_time=SharePrefManager.getInstance(Operator.this).mGetSharePrefSingleDataLong("operator_time");
        long load_time=(((current_time-save_time)/1000)/60);
        String operator_list=SharePrefManager.getInstance(Operator.this).mGetOperators();
        if (load_time<30&&(!operator_list.equals("")))
        {
            if (DetectConnection.checkInternetConnection(Operator.this)) {
                mShowOperators(SharePrefManager.getInstance(Operator.this).mGetOperators());
            }

        }
        else {

            if (DetectConnection.checkInternetConnection(Operator.this)) {
                mGetOperators();
            }
            else
            {
                Toast.makeText(mOperatorActivity, "No internet connection", Toast.LENGTH_SHORT).show();
            }
        }
    }



    @SuppressLint("StaticFieldLeak")
    protected void mGetOperators()
    {
        String sending= BaseURL.BASEURL_B2C+"api/application/v1/common-list";
        Uri.Builder builder=new Uri.Builder();
        builder.appendQueryParameter("api_token", SharePrefManager.getInstance(Operator.this).mGetApiToken());

        new CallResAPIPOSTMethod(Operator.this,builder,sending,true,"POST")
        {
            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                dialog.dismiss();
                Log.e("response","data "+s);

                SharePrefManager.getInstance(Operator.this).mSaveOperators(s);
                mShowOperators(s);

            }

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                dialog=new ProgressDialog(Operator.this);
                dialog.setMessage("Please wait...");
                dialog.setCancelable(false);
                dialog.show();
            }
        }.execute();
    }

    public void mShowOperators(String operator) {

        String service_id="";
        String provider_id="";
        String provider_name="";
        String provider_image="";
        String status="";

        try
        {

            JSONObject jsonObject=new JSONObject(operator);
            if (jsonObject.has("status"))
            {
                status=jsonObject.getString("status");
            }

            if (status.equalsIgnoreCase("success")) {
//                JSONArray jsonArray = jsonObject.getJSONArray("providers");
                JSONArray jsonArray = jsonObject.getJSONArray("providerList");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject data = jsonArray.getJSONObject(i);
                    service_id = data.getString("service_id");
                    provider_id = data.getString("provider_id");
                    provider_name = data.getString("provider_name");

                    if (data.has("provider_icon")) {
                        provider_image = data.getString("provider_icon");
                    }
                    else
                    {
                        provider_image = "";
                    }

                    if (data.getString("provider_id").equals("384"))
                    {
                        Log.e("service id "+data.getString("service_id"),"provider name "+data.getString("provider_name"));
                    }

//                    Log.e("service id "+data.getString("service_id"),"provider name "+data.getString("provider_name"));


                    if (service_id.equals(type)) {
                        Operators_Items items = new Operators_Items();
                        items.setOperator_id(provider_id);
                        items.setOperator_name(provider_name);
                        items.setOperator_image(provider_image);

                        items.setService_id(type);

                        items.setFrom(from);
                        items.setData(service_data);
                        operators_items.add(items);
                    }
                    operatorsCardAdapter.notifyDataSetChanged();
                }

                if (operators_items.size()!=0)
                {
                    SharePrefManager.getInstance(Operator.this).mSaveSingleDataLong("operator_time",System.currentTimeMillis());
                    SharePrefManager.getInstance(Operator.this).mSaveOperators(operator);
                }
            }
            else if (!SharePrefManager.getInstance(Operator.this).mGetOperators().equals(""))
            {
                JSONObject jsonObject1=new JSONObject(SharePrefManager.getInstance(Operator.this).mGetOperators());
                JSONArray jsonArray = jsonObject1.getJSONArray("providers");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject data = jsonArray.getJSONObject(i);
                    service_id = data.getString("service_id");
                    provider_id = data.getString("provider_id");
                    provider_name = data.getString("provider_name");

                    if (data.has("provider_image")) {
                        provider_image = data.getString("provider_image");
                    }
                    else
                    {
                        provider_image = "";
                    }

//                    Log.e("service id "+data.getString("service_id"),"provider name "+data.getString("provider_name"));

                    if (service_id.equals(type)) {
                        Operators_Items items = new Operators_Items();
                        items.setOperator_id(provider_id);
                        items.setOperator_name(provider_name);
                        items.setOperator_image(provider_image);
                        items.setService_id(type);
                        operators_items.add(items);
                    }
                    operatorsCardAdapter.notifyDataSetChanged();
                }
            }
            else
            {
                Toast.makeText(getApplicationContext(), "Unable to fetch operator list please check your internet connection and try again...", Toast.LENGTH_LONG).show();
            }

        }

        catch (JSONException e)
        {
            e.printStackTrace();
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

    @Override
    public void onLocationChanged(@NonNull Location location) {

    }

    @Override
    public void onLocationChanged(@NonNull List<Location> locations) {
        LocationListener.super.onLocationChanged(locations);
    }

    @Override
    public void onFlushComplete(int requestCode) {
        LocationListener.super.onFlushComplete(requestCode);
    }

    @Override
    public void onProviderEnabled(@NonNull String provider) {
        LocationListener.super.onProviderEnabled(provider);
    }

    @Override
    public void onProviderDisabled(@NonNull String provider) {
        LocationListener.super.onProviderDisabled(provider);
    }
}