package com.demo.apppay2all.MemberDetail;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.demo.apppay2all.BaseURL.BaseURL;
import com.demo.apppay2all.CallResAPIPOSTMethod;
import com.demo.apppay2all.DetectConnection;
import com.demo.apppay2all.R;
import com.demo.apppay2all.SharePrefManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MemberList extends AppCompatActivity {

    List<MemberItems> memberLists;
    MemberCardAdapter memberCardAdapter;
    RecyclerView recyclerview_member;
    ProgressDialog dialog;

    String type="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_list);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        if (getIntent().hasExtra("type"))
        {
            type=getIntent().getStringExtra("type");
        }

        if (type.equalsIgnoreCase("team"))
        {
            getSupportActionBar().setTitle("My Team");
        }

        recyclerview_member=findViewById(R.id.recyclerview_member);
        recyclerview_member.setLayoutManager(new LinearLayoutManager(MemberList.this));
        memberLists=new ArrayList<>();
        memberCardAdapter=new MemberCardAdapter(MemberList.this,memberLists);
        recyclerview_member.setAdapter(memberCardAdapter);

        if (DetectConnection.checkInternetConnection(MemberList.this))
        {
            mGetList();
        }
        else
        {
            Toast.makeText(this, "No internet connection", Toast.LENGTH_SHORT).show();
        }
    }

    @SuppressLint("StaticFieldLeak")
    protected void mGetList()
    {
        String sending_url= BaseURL.BASEURL_B2C+"api/admin/get-users";
        Uri.Builder builder=new Uri.Builder();
        builder.appendQueryParameter("api_token", SharePrefManager.getInstance(MemberList.this).mGetApiToken());

        new CallResAPIPOSTMethod(MemberList.this,builder,sending_url,true,"POST")
        {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                dialog=new ProgressDialog(MemberList.this);
                dialog.setMessage("Please wait...");
                dialog.setCancelable(false);
                dialog.show();
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                dialog.dismiss();
                Log.e("response","meber "+s);

                if (!s.equals(""))
                {
                    try{
                        JSONObject jsonObject=new JSONObject(s);
                        JSONArray jsonArray=jsonObject.getJSONArray("users");
                        for (int i=0; i<jsonArray.length(); i++)
                        {
                            JSONObject data=jsonArray.getJSONObject(i);
                            MemberItems items=new MemberItems();
                            items.setId(data.getString("id"));
                            items.setName(data.getString("name"));
                            items.setMobile(data.getString("mobile"));
                            items.setEmail(data.getString("email"));
                            items.setRole(data.getString("member_type"));
                            items.setBalance(data.getString("normal_balance"));
                            items.setType(type);

                            memberLists.add(items);
                            memberCardAdapter.notifyDataSetChanged();
                        }
                    }
                    catch (JSONException e)
                    {
                        e.printStackTrace();
                    }
                }
                else
                {
                    Toast.makeText(MemberList.this, "Server not responding", Toast.LENGTH_SHORT).show();
                }
            }
        }
        .execute();
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

                if (memberLists!=null) {
                    List<MemberItems> temp = new ArrayList();
                    for (MemberItems d : memberLists) {
                        //or use .equal(text) with you want equal match
                        //use .toLowerCase() for better matches
                        if (d.getName().toLowerCase().contains(newText.toLowerCase()) || d.getMobile().toLowerCase().contains(newText.toLowerCase())) {
                            temp.add(d);
                        }
                    }
                    //update recyclerview
                    memberCardAdapter.UpdateList(temp);
                }
                return true;
            }
        });
    }
}