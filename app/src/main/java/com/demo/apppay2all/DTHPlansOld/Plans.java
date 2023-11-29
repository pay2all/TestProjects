package com.demo.apppay2all.DTHPlansOld;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.demo.apppay2all.BaseURL.BaseURL;
import com.demo.apppay2all.DetectConnection;
import com.demo.apppay2all.R;
import com.demo.apppay2all.SharePrefManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class Plans extends AppCompatActivity {
    List<PlanItems> planItems;
    PlanCardAdapter planCardAdapter;

    RecyclerView recyclerview_dth_plan;
    ProgressDialog dialog;
    String provider="",number="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plans);
        recyclerview_dth_plan=findViewById(R.id.recyclerview_dth_plan);
        planItems= new ArrayList<>();
        recyclerview_dth_plan.setLayoutManager(new LinearLayoutManager(Plans.this));
        planCardAdapter=new PlanCardAdapter(Plans.this,planItems);
        recyclerview_dth_plan.setAdapter(planCardAdapter);

        if (getSupportActionBar()!=null)
        {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        provider=getIntent().getStringExtra("provider");
        number=getIntent().getStringExtra("number");
        if (DetectConnection.checkInternetConnection(Plans.this))
        {
            mPlans(provider);
        }
        else{
            Toast.makeText(this, "No Internet Connection", Toast.LENGTH_SHORT).show();
        }

    }

    @SuppressLint("StaticFieldLeak")
    private  void  mPlans(final String provider) {
        class getJSONData extends AsyncTask<String, String, String> {

            HttpURLConnection urlConnection;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                dialog=new ProgressDialog(Plans.this);
                dialog.setMessage("Please wait...");
                dialog.show();
                dialog.setCancelable(false);
            }
            @Override
            protected String doInBackground(String... args) {

                StringBuilder result = new StringBuilder();

                try {
                    URL url = new URL(BaseURL.BASEURL_B2C +"api/plans/dth-plans?api_token="+ SharePrefManager.getInstance(Plans.this).mGetApiToken()+"&provider_id="+provider+"&number="+number);
                    urlConnection = (HttpURLConnection) url.openConnection();
                    InputStream in = new BufferedInputStream(urlConnection.getInputStream());

                    BufferedReader reader = new BufferedReader(new InputStreamReader(in));

                    String line;
                    while ((line = reader.readLine()) != null) {
                        result.append(line);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    urlConnection.disconnect();
                }


                return result.toString();
            }

            @Override
            protected void onPostExecute(String result) {

                //Do something with the JSON string
                dialog.dismiss();
                Log.e("show response",result);


                try{
                    String status="";
                    JSONObject  jsonObject=new JSONObject(result);
                    if (jsonObject.has("status"))
                    {
                        status=jsonObject.getString("status");
                    }

                    if (status.equals("success"))
                    {
                        JSONArray jsonArray=jsonObject.getJSONArray("plans");
                        for (int i=0; i<jsonArray.length(); i++)
                        {
                            JSONObject data=jsonArray.getJSONObject(i);
                            PlanItems items=new PlanItems();
                            items.setPlan_name(data.getString("plan_name"));
                            items.setDesc(data.getString("desc"));
                            JSONObject rs=data.getJSONObject("rs");

                            if (rs.has("1 MONTHS"))
                            {
                                items.setOne_m(rs.getString("1 MONTHS"));
                            }
                            if (rs.has("3 MONTHS"))
                            {
                                items.setThree_m(rs.getString("3 MONTHS"));
                            }
                            if (rs.has("6 MONTHS"))
                            {
                                items.setSix_m(rs.getString("6 MONTHS"));
                            }
                            if (rs.has("1 YEAR"))
                            {
                                items.setOne_y(rs.getString("1 YEAR"));
                            }

                            planItems.add(items);
                            planCardAdapter.notifyDataSetChanged();
                        }
                    }
                    else{
                        Toast.makeText(Plans.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                    }
                }
                catch(JSONException e)
                {
                    e.printStackTrace();
                }

            }
        }

        getJSONData getJSONData=new getJSONData();
        getJSONData.execute();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_menu, menu);

        MenuItem search = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) search.getActionView();
        search(searchView);
        return true;

    }


    private void search(SearchView searchView) {

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                if (planItems.size()!=0) {
                    List<PlanItems> temp = new ArrayList();
                    for (PlanItems d : planItems) {
                        //or use .equal(text) with you want equal match
                        //use .toLowerCase() for better matches
                        if (d.getOne_m().toLowerCase().contains(newText.toLowerCase()) || d.getOne_y().toLowerCase().contains(newText.toLowerCase()) || d.getPlan_name().toLowerCase().contains(newText.toLowerCase()) ||
                                d.getSix_m().contains(newText) || d.getThree_m().toLowerCase().contains(newText.toLowerCase()) || d.getDesc().toLowerCase().contains(newText.toLowerCase())) {
                            temp.add(d);
                        }
                    }
                    //update recyclerview
                    planCardAdapter.UpdateList(temp);
                }
                return true;
            }

        });
    }

}