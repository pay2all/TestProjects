package com.demo.apppay2all.Browse_Plan;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.demo.apppay2all.BaseURL.BaseURL;
import com.demo.apppay2all.CallResAPIGetMethod;
import com.demo.apppay2all.DetectConnection;
import com.demo.apppay2all.R;
import com.demo.apppay2all.SharePrefManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Basant on 6/8/2017.
 */

public class TwoGFragment extends Fragment {

    String type,brows_data="",provider,circle,id;

    TextView tv_message;

    RecyclerView recyclerview_plan;
    RecyclerView.LayoutManager layoutManager;

    BrowseCardAdapter beneficiry_cardAdapter ;
    List<Plan_Items> planitems ;

    public static TwoGFragment newInstance(String type,String provider,String circle) {

        TwoGFragment result = new TwoGFragment();
        Bundle bundle = new Bundle();
        bundle.putString("type", type);

        bundle.putString("provider", provider);
        bundle.putString("circle", circle);
        result.setArguments(bundle);

        return result;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = this.getArguments();
        type = bundle.getString("type");
        provider = bundle.getString("provider");
        circle = bundle.getString("circle");
//        brows_data=BrowsePlan.plans_data;
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.top_up_fragment, container, false);

        tv_message = v.findViewById(R.id.tv_message);

        recyclerview_plan = v.findViewById(R.id.recyclerview_plan);
        recyclerview_plan.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this.getActivity());
        recyclerview_plan.setLayoutManager(layoutManager);
        planitems = new ArrayList<>();
        beneficiry_cardAdapter = new BrowseCardAdapter(getActivity(), planitems);
        recyclerview_plan.setAdapter(beneficiry_cardAdapter);

        if (DetectConnection.checkInternetConnection(getActivity())) {
//            new getBeneficiary().execute(BaseURL.BASEURL+"app-get-plan-info?provider="+operator+"&circle="+circle+"&type=4g");
            mGetData();
        }
        else {
            Toast.makeText(getContext(), "No Connection", Toast.LENGTH_SHORT).show();
        }
        return v;
    }


    protected void mGetBrowsPlanData(String brows_data)
    {
        if (!brows_data.equals(""))
        {
            String status="",message="";
            try
            {
                JSONObject jsonObject=new JSONObject(brows_data);

                status=jsonObject.getString("status");

                if (jsonObject.has("message"))
                {
                    message=jsonObject.getString("message");
                }

                if (status.equals("success")) {

                    tv_message.setVisibility(View.GONE);

                    JSONArray jsonArray =jsonObject.getJSONArray("plans");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject data = jsonArray.getJSONObject(i);

                        Plan_Items items = new Plan_Items();
                        items.setAmount(data.getString("rs"));

                        items.setDescription(data.getString("desc"));
                        items.setValidity(data.getString("validity"));

                        if (data.has("talktime")) {
                            items.setTalktime(data.getString("talktime"));
                        }

                        planitems.add(items);
                        beneficiry_cardAdapter.notifyDataSetChanged();

                    }
                }
                else if (!message.equals(""))
                {
                    tv_message.setText(message);
                }
            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }
        }
    }

    @SuppressLint("StaticFieldLeak")
    protected void mGetData()
    {
        String sending_url= BaseURL.BASEURL_B2C+"api/plans/v1/prepaid-plans?api_token="+ SharePrefManager.getInstance(getActivity()).mGetApiToken() +"&provider_id="+provider+"&state_id="+circle+"&plantype_id="+type;
        new CallResAPIGetMethod(getActivity(),sending_url)
        {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);

                brows_data=s;
                mGetBrowsPlanData(s);
            }
        }.execute();
    }
}