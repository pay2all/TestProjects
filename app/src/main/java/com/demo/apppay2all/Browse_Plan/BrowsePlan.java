package com.demo.apppay2all.Browse_Plan;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.demo.apppay2all.BaseURL.BaseURL;
import com.demo.apppay2all.DetectConnection;
import com.demo.apppay2all.R;
import com.demo.apppay2all.SharePrefManager;
import com.google.android.material.tabs.TabLayout;

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


/**
 * Created by Basant on 3/29/2018.
 */

public class BrowsePlan extends AppCompatActivity {

    TextView textview_balance,textview_name;

    public static String plans_data="";

    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    String provider,circle;

    ProgressDialog dialog;

    LinearLayout ll_containstate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.browse_plan_layout);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Bundle bundle=getIntent().getExtras();
        provider=bundle.getString("operator");
        circle=bundle.getString("circle");



        if (DetectConnection.checkInternetConnection(BrowsePlan.this)) {

            mGetBrowsPlanData(provider,circle);
        }
        else
        {
            Toast.makeText(this, "No Internet Connection", Toast.LENGTH_SHORT).show();
        }


    }


    private void setupViewPager(ViewPager viewPager,String data) {

        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());


        try {
            JSONArray jsonArray=new JSONArray(data);

            for (int i=0; i<jsonArray.length(); i++) {

                JSONObject jsonObject=jsonArray.getJSONObject(i);
                adapter.addFrag(new TwoGFragment().newInstance(jsonObject.getString("plantype_id"),provider,circle), jsonObject.getString("plan_title"));
            }
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }

        viewPager.setAdapter(adapter);
    }

//    private void setupViewPager(ViewPager viewPager) {
//
//        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
//
//        adapter.addFrag(new TwoGFragment().newInstance("specialOffer"), "specialOffer");
//        adapter.addFrag(new TwoGFragment().newInstance("topUp"), "topUp");
//        adapter.addFrag(new TwoGFragment().newInstance("fullTalktime"), "fullTalktime");
//        adapter.addFrag(new TwoGFragment().newInstance("forG"), "4G");
//        adapter.addFrag(new TwoGFragment().newInstance("rateCutter"), "rateCutter");
//        adapter.addFrag(new TwoGFragment().newInstance("data"), "data");
//
//        viewPager.setAdapter(adapter);
//
//    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFrag(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private  void  mGetBrowsPlanData(final String provider, final String circle) {
        class getJSONData extends AsyncTask<String, String, String> {

            HttpURLConnection urlConnection;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                dialog=new ProgressDialog(BrowsePlan.this);
                dialog.setMessage("Please wait...");
                dialog.show();
                dialog.setCancelable(false);
            }
            @Override
            protected String doInBackground(String... args) {

                StringBuilder result = new StringBuilder();

                try {
//                    URL url = new URL(BaseURL.BASEURL_B2C+"api/plans/v1/prepaid-plans?api_token="+SharePrefManager.getInstance(BrowsePlan.this).mGetApiToken()+"&provider_id="+provider+"&state_id="+circle);
                    URL url = new URL(BaseURL.BASEURL_B2C+"api/plans/v1/type?api_token="+SharePrefManager.getInstance(BrowsePlan.this).mGetApiToken()+"&provider_id="+provider);
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
                Log.e("brows plan response",result);
//                plans_data=result;



                String status="",message="";
                try {
                    JSONObject jsonObject=new JSONObject(result);

                    if (jsonObject.has("status"))
                    {
                        status=jsonObject.getString("status");
                    }

                    if (jsonObject.has("message"))
                    {
                        message=jsonObject.getString("message");
                    }

                    if (!status.equals(""))
                    {
                        if (status.equalsIgnoreCase("success"))
                        {
                            if (jsonObject.has("data"))
                            {
                                JSONArray data=jsonObject.getJSONArray("data");


                                viewPager = findViewById(R.id.viewpager);
                                setupViewPager(viewPager,data+"");
                                tabLayout = findViewById(R.id.tabs);
                                tabLayout.setupWithViewPager(viewPager);
                            }
                        }
                        else if (!message.equals(""))
                        {
                            Toast.makeText(BrowsePlan.this, message, Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            Toast.makeText(BrowsePlan.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else if (!message.equals(""))
                    {
                        Toast.makeText(BrowsePlan.this, message, Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        Toast.makeText(BrowsePlan.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                    }



                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }


                textview_balance = findViewById(R.id.textview_balance);
                textview_name = findViewById(R.id.textview_name);
            }
        }

        getJSONData getJSONData=new getJSONData();
        getJSONData.execute();
    }



}