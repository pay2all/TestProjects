package com.demo.apppay2all.LedgerDetail;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
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
import java.util.Calendar;
import java.util.List;

public class Ledger extends AppCompatActivity {

    ProgressDialog dialog;
    List<Ledger_Item> account_statement_items;
    com.demo.apppay2all.LedgerDetail.LedgerCardAdaptor account_statemnt_cardAdaptor;
    String username,password;
    RecyclerView recyclerview_ledger;
    String[] country = { "ALL","Success","Pending","Failed","Disputed"};
    RadioGroup radiogroup_group;



    AlertDialog alertDialog;

    String number="";

    String fromdate="",todate="";


    TextView textview_icdate;
    ImageView imageView_storage;

    Button button_search;

    String searchkey="All";

    EditText edittext_operator,edittext_customernumber;
    ArrayList <String> operatorlist=new ArrayList<>();
    ArrayList <String> shortaddlist=new ArrayList<>();

    private int mYear, mMonth, mDay;

    String stype="";

    TextView tv_message;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ledger);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        tv_message=findViewById(R.id.tv_message);
        textview_icdate=findViewById(R.id.textview_icdate);
        textview_icdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mShowAlertDialogSearchByDate();
            }
        });


        imageView_storage=findViewById(R.id.imageView_storage);
        imageView_storage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                mShowAlertDialogSearchByStatus();


            }
        });


        username= SharePrefManager.getInstance(Ledger.this).mGetUsername();
        password= SharePrefManager.getInstance(Ledger.this).getPassword();
        recyclerview_ledger=findViewById(R.id.recyclerview_ledger);
        recyclerview_ledger.setLayoutManager(new LinearLayoutManager(Ledger.this));
        account_statement_items=new ArrayList<>();
        account_statemnt_cardAdaptor=new com.demo.apppay2all.LedgerDetail.LedgerCardAdaptor(Ledger.this,account_statement_items);
        recyclerview_ledger.setAdapter(account_statemnt_cardAdaptor);

        if (DetectConnection.checkInternetConnection(this)){
//            getData(BaseURL.BASEURL_B2C+"api/reports/ledger-report?api_token=EOmp8MyuCGyHC5avFkYqBXUqp9nIg1R0EkKtHxM6olO8C6VBBdT4lD2ijB5G");
            getData(BaseURL.BASEURL_B2C+"api/reports/v1/ledger-report?api_token="+SharePrefManager.getInstance(Ledger.this).mGetApiToken());
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

    private  void  getData(final String sendingurl) {
        class getJSONData extends AsyncTask<String, String, String> {


            HttpURLConnection urlConnection;


            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                dialog=new ProgressDialog(Ledger.this);
                dialog.setMessage("Please wait...");
                dialog.show();
                dialog.setCancelable(false);
            }
            @Override
            protected String doInBackground(String... args) {

                StringBuilder result = new StringBuilder();

                try {
                    URL url=new URL(sendingurl);
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

                dialog.dismiss();

                Log.e("response","data "+result);

                if (!result.equals("")) {
                    mShowData(result);
                }

                else {

                    Toast.makeText(Ledger.this, "Server Not Responding", Toast.LENGTH_SHORT).show();
                }
            }

        }

        getJSONData getJSONData=new getJSONData();
        getJSONData.execute();

    }

    private void mShowData(String jsoandata){

        try{

            JSONObject jsonObject=new JSONObject(jsoandata);
            JSONArray jsonArray= jsonObject.getJSONArray("reports");
            for (int i=0;i<jsonArray.length();i++){

                JSONObject data=jsonArray.getJSONObject(i);
                Ledger_Item item=new Ledger_Item();
                item.setId(data.getString("id"));
                item.setDate(data.getString("created_at"));
                item.setTxnid(data.getString("txnid"));
                item.setDebit(data.getString("debit"));
                item.setDescription(data.getString("description"));
                item.setProvider_image(data.getString("provider_icon"));
                item.setCredit(data.getString("credit"));
                item.setOpening_balance(data.getString("opening_balance"));
                item.setClosing_balance(data.getString("total_balance"));
                item.setCommission(data.getString("profit"));
                //                      Log.e("data","ol"+jsonObject.getString("opening_balance"));
                account_statement_items.add(item);
                account_statemnt_cardAdaptor.notifyDataSetChanged();
            }

            if (account_statement_items.size()!=0)
            {

                recyclerview_ledger.setVisibility(View.VISIBLE);
                tv_message.setVisibility(View.GONE);
            }
            else
            {
                recyclerview_ledger.setVisibility(View.GONE);
                tv_message.setVisibility(View.VISIBLE);
                tv_message.setText("No record found");

            }
        }

        catch (JSONException e) {
            e.printStackTrace();
        }
    }

    protected void mShowAlertDialogSearchByDate(){
        LayoutInflater layoutInflater=(LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view=layoutInflater.inflate(R.layout.custom_alert_dialouge_1,null);
        radiogroup_group=view.findViewById(R.id.radiogroup_group);
        radiogroup_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {


                if(i==R.id.radiobutton_today){
//                     mSearch("credit");

                    Calendar calendar=Calendar.getInstance();
                    int day=calendar.get(Calendar.DAY_OF_MONTH);
                    int month=calendar.get(Calendar.MONTH);
                    int year=calendar.get(Calendar.YEAR);
                    fromdate= year+"-"+month+"-"+day;
                    todate=fromdate;

//                    mGetStatment(username,password,"search");
                    alertDialog.dismiss();
                }
                else if(i==R.id.radiobutton_yesterday){

                    Calendar calendar=Calendar.getInstance();

                    int to_day=calendar.get(Calendar.DAY_OF_MONTH);
                    int to_month=calendar.get(Calendar.MONTH);
                    int to_year=calendar.get(Calendar.YEAR);

                    todate=to_year+"-"+to_month+"-"+to_day;

                    calendar.add(Calendar.DAY_OF_MONTH, -1);
                    int day=calendar.get(Calendar.DAY_OF_MONTH);
                    int month=calendar.get(Calendar.MONTH);
                    int year=calendar.get(Calendar.YEAR);
                    fromdate= year+"-"+month+"-"+day;

//                    mGetStatment(username,password,"search");

                    alertDialog.dismiss();
                }

                else if(i==R.id.radiobutton_1weak){

                    Calendar calendar=Calendar.getInstance();

                    int to_day=calendar.get(Calendar.DAY_OF_MONTH);
                    int to_month=calendar.get(Calendar.MONTH);
                    int to_year=calendar.get(Calendar.YEAR);

                    todate=to_year+"-"+to_month+"-"+to_day;

                    calendar.add(Calendar.DAY_OF_MONTH, -7);
                    int day=calendar.get(Calendar.DAY_OF_MONTH);
                    int month=calendar.get(Calendar.MONTH);
                    int year=calendar.get(Calendar.YEAR);
                    fromdate= year+"-"+month+"-"+day;

//                    mGetStatment(username,password,"search");


                    alertDialog.dismiss();
                }

                else if(i==R.id.radiobutton_1month){

                    Calendar calendar=Calendar.getInstance();

                    int to_day=calendar.get(Calendar.DAY_OF_MONTH);
                    int to_month=calendar.get(Calendar.MONTH);
                    int to_year=calendar.get(Calendar.YEAR);

                    todate=to_year+"-"+to_month+"-"+to_day;

                    calendar.add(Calendar.MONTH, -1);
                    int day=calendar.get(Calendar.DAY_OF_MONTH);
                    int month=calendar.get(Calendar.MONTH);
                    int year=calendar.get(Calendar.YEAR);
                    fromdate= year+"-"+month+"-"+day;

//                    mGetStatment(username,password,"search");

                    alertDialog.dismiss();
                }
                else if(i==R.id.radiobutton_all_date){

//                    mGetStatment(username,password,"get");

                    alertDialog.dismiss();
                }

            }
        });
        final AlertDialog.Builder builder=new AlertDialog.Builder(Ledger.this);
        builder.setView(view);
        alertDialog=builder.create();
        alertDialog.show();


    }
    protected void mShowAlertDialogSearchByStatus(){
        LayoutInflater layoutInflater=(LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view=layoutInflater.inflate(R.layout.custom_alert_dialouge_2,null);
        RadioButton radiobutton_all=view.findViewById(R.id.radiobutton_all);
        RadioButton radiobutton_success=view.findViewById(R.id.radiobutton_success);
        RadioButton radiobutton_pedning=view.findViewById(R.id.radiobutton_pedning);
        RadioButton radiobutton_failed=view.findViewById(R.id.radiobutton_failed);
        RadioButton radiobutton_disputed=view.findViewById(R.id.radiobutton_disputed);

        if (!stype.equals("")){

            radiobutton_all.setVisibility(View.GONE);
            radiobutton_success.setVisibility(View.GONE);
            radiobutton_pedning.setVisibility(View.GONE);
            radiobutton_failed.setVisibility(View.GONE);
            radiobutton_disputed.setVisibility(View.GONE);

        }


        radiogroup_group=view.findViewById(R.id.radiogroup_group);
        radiogroup_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {

                if(i==R.id.radiobutton_all){
                    mSearch("");

                    alertDialog.dismiss();
                }

                else if(i==R.id.radiobutton_success){
                    mSearch("success");

                    alertDialog.dismiss();
                }

                else if(i==R.id.radiobutton_pedning){

                    mSearch("pending");

                    alertDialog.dismiss();
                }

                else if(i==R.id.radiobutton_failed){

                    mSearch("failure");
                    alertDialog.dismiss();
                }

                else if(i==R.id.radiobutton_disputed){

                    mSearch("disput");
                    alertDialog.dismiss();
                }
                else if(i==R.id.radiobutton_credit){

                    mSearch("credit");
                    alertDialog.dismiss();
                }
                else if(i==R.id.radiobutton_debit){

                    mSearch("debit");
                    alertDialog.dismiss();
                }

            }
        });
        final AlertDialog.Builder builder=new AlertDialog.Builder(Ledger.this);
        builder.setView(view);
        alertDialog=builder.create();
        alertDialog.show();

    }
    protected void mSearch(String newText){

        if (account_statement_items!=null) {
            List<Ledger_Item> temp = new ArrayList();
            for (Ledger_Item d : account_statement_items) {
                //or use .equal(text) with you want equal match
                //use .toLowerCase() for better matches
                if (d.getCredit().toLowerCase().contains(newText.toLowerCase())||d.getDebit().toLowerCase().contains(newText.toLowerCase())) {
                    temp.add(d);
                }
            }
            //update recyclerview
            account_statemnt_cardAdaptor.UpdateList(temp);
            Log.e("key","="+newText);
        }
    }
}
