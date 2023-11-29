package com.demo.apppay2all.MoneyTransfer2;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.demo.apppay2all.BaseURL.BaseURL;
import com.demo.apppay2all.DetectConnection;
import com.demo.apppay2all.R;

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

public class MoneyTransferCharge extends Fragment {

    ProgressDialog dialog;
    RecyclerView recyclerview_dmt_charge;
    List<MoneyChargeItems> moneyChargeItems;
    MoneyTransferChargeCardAdapter moneyTransferChargeCardAdapter;

    String username,password;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_money_transfer_charge,container,false);

        SharedPreferences sharedPreferences=getActivity().getSharedPreferences("user",0);
        username=sharedPreferences.getString("username","");
        password=sharedPreferences.getString("password","");

        recyclerview_dmt_charge=view.findViewById(R.id.recyclerview_dmt_charge);
        recyclerview_dmt_charge.setLayoutManager(new LinearLayoutManager(getActivity()));
        moneyChargeItems=new ArrayList<>();
        moneyTransferChargeCardAdapter=new MoneyTransferChargeCardAdapter(getActivity(),moneyChargeItems);
        recyclerview_dmt_charge.setAdapter(moneyTransferChargeCardAdapter);

        if (DetectConnection.checkInternetConnection(getActivity()))
        {
            mGetBankDetail(username,password);
        }
        else
        {
            Toast.makeText(getContext(), "No internet connection", Toast.LENGTH_SHORT).show();
        }

        return view;
    }

    protected void mGetBankDetail(final String username, final String password) {
        class BankDetail extends AsyncTask<String, String, String> {
            HttpURLConnection urlConnection;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                dialog = new ProgressDialog(getActivity());
                dialog.setMessage("Please wait...");
                dialog.show();
                dialog.setCancelable(true);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                Log.e("ifsc response",s);
                dialog.dismiss();

                if (!s.equals(""))
                {
                    try{
                        JSONObject jsonObject=new JSONObject(s);
                        if (jsonObject.has("slabs"))
                        {
                            JSONArray jsonArray=jsonObject.getJSONArray("slabs");
                            for (int i=0; i<jsonArray.length(); i++)
                            {
                                JSONObject data=jsonArray.getJSONObject(i);
                                MoneyChargeItems items=new MoneyChargeItems();
                                items.setSlab(data.getString("slab"));
                                items.setCharge(data.getString("charge"));
                                items.setType(data.getString("type"));

                                moneyChargeItems.add(items);
                                moneyTransferChargeCardAdapter.notifyDataSetChanged();
                            }
                        }

                    }
                    catch (JSONException e)
                    {
                        e.printStackTrace();
                    }
                }
                else
                {
                    Toast.makeText(getContext(), "Server not reponding", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            protected String doInBackground(String... params) {
                StringBuilder result = new StringBuilder();

                try {
                    URL url = new URL(BaseURL.BASEURL+"api/v2/money-transfer-charges?username="+username+"&password="+password);
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
        }
        BankDetail bd = new BankDetail();
        bd.execute();
    }



    //    card adapter
    public class MoneyTransferChargeCardAdapter extends RecyclerView.Adapter<MoneyTransferChargeCardAdapter.ViewHolder>
    {

        Context context;
        List<MoneyChargeItems> moneyChargeItems;
        public MoneyTransferChargeCardAdapter(Context context,List<MoneyChargeItems> moneyChargeItems)
        {
            this.context=context;
            this.moneyChargeItems=moneyChargeItems;
        }

        @Override
        public int getItemCount() {
            return moneyChargeItems==null?0:moneyChargeItems.size();
        }


        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

            MoneyChargeItems items=moneyChargeItems.get(position);
            holder.textview_slab.setText(items.getSlab());
            holder.textview_charge.setText(items.getCharge());
            holder.textview_rs.setText(items.getType());

            Log.e("data","position "+items.getSlab());
        }


        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view=LayoutInflater.from(context).inflate(R.layout.money_charge_item,parent,false);
            return new ViewHolder(view);
        }

        public class ViewHolder extends RecyclerView.ViewHolder
        {
            TextView textview_slab,textview_charge,textview_rs;
            ViewHolder(View view)
            {
                super(view);
                textview_slab=view.findViewById(R.id.textview_slab);
                textview_charge=view.findViewById(R.id.textview_charge);
                textview_rs=view.findViewById(R.id.textview_rs);
            }
        }
    }



//    items class
    public class MoneyChargeItems{
        public String getSlab() {
            return slab;
        }

        public void setSlab(String slab) {
            this.slab = slab;
        }

        public String getCharge() {
            return charge;
        }

        public void setCharge(String charge) {
            this.charge = charge;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        String slab;
        String charge;
        String type;
    }
}
