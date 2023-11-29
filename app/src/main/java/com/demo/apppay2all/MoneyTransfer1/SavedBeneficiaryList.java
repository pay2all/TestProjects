package com.demo.apppay2all.MoneyTransfer1;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
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

public class SavedBeneficiaryList extends Fragment {

    ProgressDialog dialog;
    LinearLayout ll_contain_listview;
    LinearLayout ll_contain_norecepient_found;

    RecyclerView recyclerview_benificiary;
    private List<Beneficiary_ItemsPaytm> beneficiaryitems;
    Beneficiry_CardAdapterPaytm beneficiry_cardAdapter ;

    String status;
    String message;
    List<Beneficiary_ItemsPaytm> myJSON;

    String sender_number = "";
    String senderid;
    String name = "";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle=this.getArguments();
        sender_number=bundle.getString("sender_number");
        name=bundle.getString("name");
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.saved_benificiary_fragment,container,false);

        beneficiaryitems = new ArrayList<>();


        recyclerview_benificiary = v.findViewById(R.id.recyclerview_plan);
        beneficiry_cardAdapter = new Beneficiry_CardAdapterPaytm(getActivity(), beneficiaryitems);
        recyclerview_benificiary.setHasFixedSize(true);

        int orientation = getResources().getConfiguration().orientation;
        if (orientation== Configuration.ORIENTATION_LANDSCAPE)
        {
            recyclerview_benificiary.setLayoutManager(new GridLayoutManager(this.getActivity(),2));
        }
        else {
            recyclerview_benificiary.setLayoutManager(new LinearLayoutManager(this.getActivity()));
        }
        recyclerview_benificiary.setAdapter(beneficiry_cardAdapter);


        ll_contain_listview = v.findViewById(R.id.ll_contain_listview);
        ll_contain_norecepient_found = v.findViewById(R.id.ll_contain_norecepient_found);

        if (DetectConnection.checkInternetConnection(getActivity())) {
            String sending_url=BaseURL.BASEURL_B2C+"api/dmt/v1/get-all-beneficiary";
            Uri.Builder builder=new Uri.Builder();
            builder.appendQueryParameter("api_token", SharePrefManager.getInstance(getActivity()).mGetApiToken());
            builder.appendQueryParameter("mobile_number",sender_number);
            builder.appendQueryParameter("sender_name",name);

            mCheckSender(builder,sending_url);
        } else {
            Toast.makeText(getContext(), "No Connection", Toast.LENGTH_SHORT).show();
        }

        return v;
    }



    @SuppressLint("StaticFieldLeak")
    protected void mCheckSender( Uri.Builder builder, String sending_url)
    {

        new CallResAPIPOSTMethod(getActivity(),builder,sending_url,true,"POST"){
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                dialog=new ProgressDialog(getActivity());
                dialog.setMessage("Please wait...");
                dialog.setCancelable(false);
                dialog.show();
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                dialog.dismiss();
                Log.e("check sender","response "+s);

                try
                {
                    JSONObject jsonObject=new JSONObject(s);
                    status=jsonObject.getString("status");
                    message=jsonObject.getString("message");
                    if (status.equalsIgnoreCase("success")){
                        JSONArray jsonArray = jsonObject.getJSONArray("recipient_list");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject data = jsonArray.getJSONObject(i);

                            Beneficiary_ItemsPaytm beneficiary_items = new Beneficiary_ItemsPaytm();
                            beneficiary_items.setBeneficiry_name(data.getString("recipient_name"));
                            beneficiary_items.setAccountno(data.getString("recipient_account"));
                            beneficiary_items.setBank(data.getString("recipient_bank"));
                            beneficiary_items.setIfsc(data.getString("recipient_ifsc"));
                            beneficiary_items.setRecepient_id(data.getString("recipient_id"));
                            beneficiary_items.setSender_number(sender_number);

                            beneficiaryitems.add(beneficiary_items);
                            beneficiry_cardAdapter.notifyDataSetChanged();
                        }
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
                catch (JSONException e)
                {
                    e.printStackTrace();
                }
            }
        }.execute();
    }

}
