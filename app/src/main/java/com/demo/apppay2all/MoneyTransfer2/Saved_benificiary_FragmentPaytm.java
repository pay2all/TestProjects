package com.demo.apppay2all.MoneyTransfer2;

import android.app.ProgressDialog;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.demo.apppay2all.BaseURL.BaseURL;
import com.demo.apppay2all.DetectConnection;
import com.demo.apppay2all.R;

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


/**
 * Created by Basant on 6/8/2017.
 */

public class Saved_benificiary_FragmentPaytm extends Fragment {

    public static Saved_benificiary_FragmentPaytm newInstance(String s, String senderid, String name) {

        Saved_benificiary_FragmentPaytm result = new Saved_benificiary_FragmentPaytm();
        Bundle bundle = new Bundle();
        bundle.putString("sender_number", s);
        bundle.putString("senderid", senderid);
        bundle.putString("name", name);
        result.setArguments(bundle);
        return result;
    }

    public static String sender_number;
    public static String senderid;
     String name;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle=this.getArguments();
        sender_number=bundle.getString("sender_number");
        name=bundle.getString("name");
    }

    ProgressDialog dialog;



    LinearLayout ll_contain_listview;
    LinearLayout ll_contain_norecepient_found;

    RecyclerView recyclerview_benificiary;
    private List<Beneficiary_ItemsPaytm> beneficiaryitems;

    String status;
    String message;

    List<Beneficiary_ItemsPaytm> myJSON;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.saved_benificiary_fragment, container, false);

        beneficiaryitems = new ArrayList<>();

        recyclerview_benificiary = v.findViewById(R.id.recyclerview_plan);
        recyclerview_benificiary.setHasFixedSize(true);

        int orientation = getResources().getConfiguration().orientation;
        if (orientation== Configuration.ORIENTATION_LANDSCAPE)
        {
            recyclerview_benificiary.setLayoutManager(new GridLayoutManager(this.getActivity(),2));
        }
        else {
            recyclerview_benificiary.setLayoutManager(new LinearLayoutManager(this.getActivity()));
        }

        ll_contain_listview = v.findViewById(R.id.ll_contain_listview);
        ll_contain_norecepient_found = v.findViewById(R.id.ll_contain_norecepient_found);

        if (DetectConnection.checkInternetConnection(Saved_benificiary_FragmentPaytm.this.getActivity())) {
            new getBeneficiary().execute(BaseURL.BASEURL+"app/v2/get-all-beneficiary?mobile_number="+sender_number+"&name="+name);
        } else {
            Toast.makeText(getContext(), "No Connection", Toast.LENGTH_SHORT).show();
        }
        return v;
    }

    public class getBeneficiary extends AsyncTask<String, String, List<Beneficiary_ItemsPaytm>> {



            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                dialog = new ProgressDialog(Saved_benificiary_FragmentPaytm.this.getActivity());
                dialog.setMessage("Please wait...");
                dialog.show();
                dialog.setCancelable(true);
            }

            @Override
            protected void onPostExecute(List<Beneficiary_ItemsPaytm> s) {
                super.onPostExecute(s);
                dialog.dismiss();
                myJSON=s;
                ShowBeneficiary();

            }

            @Override
            protected List<Beneficiary_ItemsPaytm> doInBackground(String... params) {
                HttpURLConnection httpCon = null;
                BufferedReader reader = null;


                 message = "";
                String account = "";
                String recipient_name = "";
                String bank = "";
                String ifsc="";
                String recepient_id="";

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
                    Log.e("result",finalJSON);

                    List<Beneficiary_ItemsPaytm> beneficiaryitems = new ArrayList<>();
                    JSONObject jsonObject = new JSONObject(finalJSON);
                    status = jsonObject.getString("status");
                    message = jsonObject.getString("message");



                    if (!status.equalsIgnoreCase("failure")){
                        JSONArray jsonArray = jsonObject.getJSONArray("recipient_list");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject data = jsonArray.getJSONObject(i);
                            account = data.getString("recipient_account");
                            recipient_name = data.getString("recipient_name");
                            ifsc = data.getString("recipient_ifsc");
                            bank = data.getString("recipient_bank");
                            recepient_id = data.getString("recipient_id");


                            Beneficiary_ItemsPaytm beneficiary_items = new Beneficiary_ItemsPaytm();
                            beneficiary_items.setBeneficiry_name(recipient_name);
                            beneficiary_items.setAccountno(account);
                            beneficiary_items.setBank(bank);
                            beneficiary_items.setIfsc(ifsc);
                            beneficiary_items.setRecepient_id(recepient_id);
                            beneficiary_items.setSender_number(sender_number);

                            beneficiaryitems.add(beneficiary_items);
                        }

                    }
                    return beneficiaryitems;

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

    protected void ShowBeneficiary()
    {
        if (beneficiaryitems!=null||!status.equalsIgnoreCase("failure")) {
            Beneficiry_CardAdapterPaytm beneficiry_cardAdapter = null;
            beneficiry_cardAdapter = new Beneficiry_CardAdapterPaytm(getActivity(), myJSON);
            recyclerview_benificiary.setAdapter(beneficiry_cardAdapter);
        }
        else if (beneficiaryitems==null||status.equalsIgnoreCase("failure"))
        {
            ll_contain_listview.setVisibility(View.GONE);
            ll_contain_norecepient_found.setVisibility(View.VISIBLE);
        }

        else
        {
            Toast.makeText(getContext(), "Something went wrong please check your connection and try again after sometime", Toast.LENGTH_SHORT).show();
        }
    }

}