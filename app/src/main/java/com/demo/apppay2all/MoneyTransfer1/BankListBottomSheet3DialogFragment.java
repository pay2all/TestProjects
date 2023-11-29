package com.demo.apppay2all.MoneyTransfer1;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.demo.apppay2all.ProviderDetail.Operator;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
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

/**
 * Created by Basant on 3/29/2018.
 */

public class BankListBottomSheet3DialogFragment extends BottomSheetDialogFragment {

    RecyclerView recyclerview_operator;

    RecyclerView.LayoutManager layoutManager;
    private List<BankListItems> beneficiaryitems;
    EditText edittext_search;

    String type;
    public static String activity_name;

    List<BankListItems> myJSON;

    private BottomSheetBehavior mBehavior;

    ImageView imageview_back_icon;

    public static BankListBottomSheet3DialogFragment dialogFragment;

    String operator;
    BankListCardAdapter operatorsCardAdapter=null;

    TextView textview_title;

    private BottomSheetBehavior.BottomSheetCallback mBottomSheetBehaviorCallback = new BottomSheetBehavior.BottomSheetCallback() {

        @Override
        public void onStateChanged(@NonNull View bottomSheet, int newState) {
            if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                dismiss();
            }
        }
        @Override
        public void onSlide(@NonNull View bottomSheet, float slideOffset) {
        }
    };

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        BottomSheetDialog dialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);

        View view = View.inflate(getContext(), R.layout.bottom_sheet_layout, null);
        textview_title=view.findViewById(R.id.textview_title);
        textview_title.setText("Select Bank");

        LinearLayout linearLayout = view.findViewById(R.id.root);
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) linearLayout.getLayoutParams();
        params.height = Resources.getSystem().getDisplayMetrics().heightPixels;
        linearLayout.setLayoutParams(params);

        dialogFragment=this;
        imageview_back_icon=(ImageView)view.findViewById(R.id.imageview_back_icon);
        imageview_back_icon.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        edittext_search=view.findViewById(R.id.edittext_search);
        edittext_search.setVisibility(View.VISIBLE);
        edittext_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                List<BankListItems> temp = new ArrayList();
                for (BankListItems d : myJSON) {
                    //or use .equal(text) with you want equal match
                    //use .toLowerCase() for better matches
                    if (d.getBank().toLowerCase().contains(editable.toString().toLowerCase())|| d.getBank_code().toLowerCase().contains(editable.toString().toLowerCase())) {
                        temp.add(d);
                    }
                }
                //update recyclerview
                operatorsCardAdapter.UpdateList(temp);
            }
        });

        type=getArguments().getString("type");
        activity_name=getArguments().getString("activity");

        view.findViewById(R.id.fakeShadow).setVisibility(View.GONE);
        recyclerview_operator = (RecyclerView) view.findViewById(R.id.recyclerview_operator);
        recyclerview_operator.setHasFixedSize(true);
        recyclerview_operator.setLayoutManager(new LinearLayoutManager(getContext()));

        beneficiaryitems=new ArrayList<>();

        operatorsCardAdapter=new BankListCardAdapter(getContext(),beneficiaryitems);
        recyclerview_operator.setAdapter(operatorsCardAdapter);

        long current_time=System.currentTimeMillis();
//        String login_time=SharePrefManager.getInstance(getActivity()).mGetSharePrefSingleData("bank_list1_time");
        long save_time=SharePrefManager.getInstance(getActivity()).mGetSharePrefSingleDataLong("operator_time");
        long load_time=(((current_time-save_time)/1000)/60);
        String operator_list=SharePrefManager.getInstance(getActivity()).mGetOperators();
        if (load_time<30&&(!operator_list.equals("")))
        {
            if (DetectConnection.checkInternetConnection(getActivity())) {
                GetOperators(SharePrefManager.getInstance(getActivity()).mGetOperators());
            }

        }
        else {

            if (DetectConnection.checkInternetConnection(getActivity()))
            {
                String sending_url=BaseURL.BASEURL_B2C+"api/application/v1/common-list";
                Uri.Builder builder=new Uri.Builder();
                builder.appendQueryParameter("api_token", SharePrefManager.getInstance(getActivity()).mGetApiToken());

                mGetBankList(builder,sending_url);
            }
            else
            {
                Toast.makeText(getActivity(), "No Internet Connection", Toast.LENGTH_SHORT).show();
                dialogFragment.dismiss();
            }
        }




        dialog.setContentView(view);
        mBehavior = BottomSheetBehavior.from((View) view.getParent());


        dialog.getActionBar();

        return dialog;

    }

    @SuppressLint("RestrictedApi")
    @Override
    public void setupDialog(Dialog dialog, int style) {
        super.setupDialog(dialog, style);

    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);

    }

//    protected void mGetBankList()
//    {
//        StringRequest stringRequest=new StringRequest(Request.Method.GET, BaseURL.BASEURL+"api/v1/get-bank",
//                new Response.Listener<String>() {
//            @Override
//            public void onResponse(String response) {
//                GetOperators(response);
//                Log.e("bank response",response);
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//
//            }
//        });
//
//        RequestQueue queue= Volley.newRequestQueue(getActivity());
//        queue.add(stringRequest);
//    }

    public void GetOperators(final String banklist) {

        String id="";
        String bank="";
        String bank_code="";
        String ifsc="";

        try
        {
            JSONObject jsonObject=new JSONObject(banklist);
//            JSONArray jsonArray =jsonObject.getJSONArray("bank_list");
            JSONArray jsonArray =jsonObject.getJSONArray("bankList");

            if (jsonArray.length()!=0)
            {
                SharePrefManager.getInstance(getActivity()).mSaveSingleDataLong("operator_time",System.currentTimeMillis());
                SharePrefManager.getInstance(getActivity()).mSaveOperators(banklist);

            }

//            if (jsonArray.length()!=0) {
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject data = jsonArray.getJSONObject(i);
                    id = data.getString("bank_id");
                    bank = data.getString("bank_name");

                    if (activity_name.equalsIgnoreCase("money"))
                    {
                        bank_code = data.getString("bank_code");
                    }
                    else
                    {
                        bank_code = data.getString("bank_id");
                    }

                    ifsc = data.getString("ifsc_code");

                    BankListItems operators_items = new BankListItems();
                    operators_items.setId(id);
                    operators_items.setBank(bank);
                    operators_items.setBank_code(bank_code);
                    operators_items.setIfsc(ifsc);
                    beneficiaryitems.add(operators_items);

                    operatorsCardAdapter.notifyDataSetChanged();
                    myJSON=beneficiaryitems;
                }
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
    }

    @SuppressLint("StaticFieldLeak")
    protected void mGetBankList(Uri.Builder builder, String sending_url)
    {
        new CallResAPIPOSTMethod(getActivity(),builder,sending_url,true,"POST"){
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                Log.e("bank","response "+s);

                GetOperators(s);
            }
        }.execute();
    }
}