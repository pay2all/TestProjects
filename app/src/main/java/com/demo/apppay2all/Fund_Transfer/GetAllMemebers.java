package com.demo.apppay2all.Fund_Transfer;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
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
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import com.demo.apppay2all.BaseURL.BaseURL;
import com.demo.apppay2all.DetectConnection;
import com.demo.apppay2all.R;

public class GetAllMemebers extends AppCompatActivity {

    String username,password;

    ProgressDialog dialog;
    RecyclerView recyclerview_members;
    RecyclerView.LayoutManager layoutManager;

    List<FundTransferItem> myJSON;

    FundTransferCardAdapter fundTransferCardAdapter;

    SwipeRefreshLayout swiperefresh_allmembers;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_member);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        SharedPreferences sharedPreferences=getSharedPreferences("user",0);
        username=sharedPreferences.getString("username","");
        password=sharedPreferences.getString("password","");

        recyclerview_members = findViewById(R.id.recyclerview_members);
        recyclerview_members.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerview_members.setLayoutManager(layoutManager);

        swiperefresh_allmembers=(SwipeRefreshLayout) findViewById(R.id.swiperefresh_allmembers);
        swiperefresh_allmembers.setColorSchemeResources(R.color.colorPrimary);
        swiperefresh_allmembers.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (DetectConnection.checkInternetConnection(GetAllMemebers.this))
                {
                    new mGetAllMembers().execute(BaseURL.BASEURL +"api/v1/get-user?username="+username+"&password="+password);
                }

                else
                {
                    Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
                    swiperefresh_allmembers.setRefreshing(false);
                }
            }
        });

        if (DetectConnection.checkInternetConnection(this))
        {
            new mGetAllMembers().execute(BaseURL.BASEURL +"api/v1/get-user?username="+username+"&password="+password);
        }

        else
        {
            Toast.makeText(this, "No Internet Connection", Toast.LENGTH_SHORT).show();
        }
    }

    public class mGetAllMembers extends AsyncTask<String, String, List<FundTransferItem>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            swiperefresh_allmembers.setRefreshing(true);
        }

        @Override
        protected void onPostExecute(List<FundTransferItem> s) {
            super.onPostExecute(s);

            myJSON=s;

            swiperefresh_allmembers.setRefreshing(false);
            ShowMemebers();

        }

        @Override
        protected List<FundTransferItem> doInBackground(String... params) {
            HttpURLConnection httpCon = null;
            BufferedReader reader = null;

            String childid = "";
            String name = "";
            String mobile = "";
            String balance="";

            try {
                URL url = new URL(params[0]);
                httpCon = (HttpURLConnection) url.openConnection();
                httpCon.connect();

                InputStream in = httpCon.getInputStream();
                reader = new BufferedReader(new InputStreamReader(in));
                StringBuffer buffer = new StringBuffer();
                String line = "";

                while ((line = reader.readLine()) != null) {
                    buffer.append(line);
                }

                String finalJSON = buffer.toString();
                Log.e("member data",finalJSON);

                List<FundTransferItem> fundTransferItems = new ArrayList<>();

                JSONArray jsonArray = new JSONArray(finalJSON);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject data = jsonArray.getJSONObject(i);
                    childid = data.getString("child_id");
                    name = data.getString("name");
                    mobile=data.getString("mobile");
                    balance = data.getString("balance");

                    FundTransferItem fundTransferItem = new FundTransferItem();
                    fundTransferItem.setId(childid);
                    fundTransferItem.setName(name);
                    fundTransferItem.setMobile(mobile);
                    fundTransferItem.setBalance(balance);

                    fundTransferItems.add(fundTransferItem);

                }

                return fundTransferItems;

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            } finally {
                if (httpCon != null)
                    httpCon.disconnect();

                try {
                    if (reader != null)
                        reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }
    }

    protected void ShowMemebers()
    {
        fundTransferCardAdapter=null;
        fundTransferCardAdapter=new FundTransferCardAdapter(GetAllMemebers.this,myJSON);
        recyclerview_members.setAdapter(fundTransferCardAdapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
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

                List<FundTransferItem> temp = new ArrayList();
                for (FundTransferItem d : myJSON) {
                    //or use .equal(text) with you want equal match
                    //use .toLowerCase() for better matches
                    if (d.getId().toLowerCase().contains(newText.toLowerCase())|| d.getMobile().contains(newText) || d.getName().toLowerCase().contains(newText.toLowerCase())) {
                        temp.add(d);
                    }
                }

                //update recyclerview
                fundTransferCardAdapter.UpdateList(temp);
                return true;
            }

        });
    }

    @Override
    protected void onResume() {
        if (DetectConnection.checkInternetConnection(this))
        {
            new mGetAllMembers().execute(BaseURL.BASEURL +"api/v1/get-user?username="+username+"&password="+password);
        }

        else
        {
            Toast.makeText(this, "No Internet Connection", Toast.LENGTH_SHORT).show();
        }

        super.onResume();
    }
}
