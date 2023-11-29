package com.demo.apppay2all.ListData;

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

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
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

public class ListDataBottomSheet3DialogFragment extends BottomSheetDialogFragment {

    RecyclerView recyclerview_operator;

    RecyclerView.LayoutManager layoutManager;
    private List<ListDataItems> beneficiaryitems;
    EditText edittext_search;

    public static String type,sending_url="",act="k";

//    List<ListDataItems> myJSON;

    private BottomSheetBehavior mBehavior;

    ImageView imageview_back_icon;

    public static ListDataBottomSheet3DialogFragment dialogFragment;

    String operator;
    ListDataCardAdapter operatorsCardAdapter=null;

    JSONArray jsonArray,jsonArray1;

    TextView textview_title;

    String state_list="",state_id="",activity="";

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

        view.findViewById(R.id.fakeShadow).setVisibility(View.GONE);
        recyclerview_operator =  view.findViewById(R.id.recyclerview_operator);
        recyclerview_operator.setHasFixedSize(true);
        recyclerview_operator.setLayoutManager(new LinearLayoutManager(getContext()));
        beneficiaryitems=new ArrayList<>();
        operatorsCardAdapter=new ListDataCardAdapter(getContext(),beneficiaryitems);
        recyclerview_operator.setAdapter(operatorsCardAdapter);

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

        textview_title=view.findViewById(R.id.textview_title);

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

                List<ListDataItems> temp = new ArrayList();
                if (beneficiaryitems.size()!=0) {
                    for (ListDataItems d : beneficiaryitems) {
                        //or use .equal(text) with you want equal match
                        //use .toLowerCase() for better matches
                        if (d.getId().toLowerCase().contains(editable.toString().toLowerCase()) || d.getName().toLowerCase().contains(editable.toString().toLowerCase())) {
                            temp.add(d);
                        }
                    }
                    //update recyclerview
                    operatorsCardAdapter.UpdateList(temp);
                }
            }
        });

        type=getArguments().getString("type");
        sending_url=getArguments().getString("url");
        if(getArguments().containsKey("act")){
            act=getArguments().getString("act");
        }
        if (type.equalsIgnoreCase("district"))
        {
            state_id=getArguments().getString("state_id");

            if (state_list.equals(""))
            {
                if (DetectConnection.checkInternetConnection(getActivity()))
                {
                    mGetData();
                }
                else
                {
                    Toast.makeText(getActivity(), "No Internet Connection", Toast.LENGTH_SHORT).show();
                    dialogFragment.dismiss();
                }
            }
            else
            {
                GetOperators(state_list);
            }
        }
        else
        {
            if (DetectConnection.checkInternetConnection(getActivity()))
            {
                mGetData();
            }
            else
            {
                Toast.makeText(getActivity(), "No Internet Connection", Toast.LENGTH_SHORT).show();
                dialogFragment.dismiss();
            }
        }

//        textview_title.setText("Select "+type);





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

    @SuppressLint("StaticFieldLeak")
    private  void  mGetData() {


        long current_time=System.currentTimeMillis();
//        String login_time=SharePrefManager.getInstance(getActivity()).mGetSharePrefSingleData("bank_list1_time");
        long save_time=SharePrefManager.getInstance(getActivity()).mGetSharePrefSingleDataLong("operator_time");
        long load_time=(((current_time-save_time)/1000)/60);
        String operator_list=SharePrefManager.getInstance(getActivity()).mGetOperators();
        if (load_time<30&&(!operator_list.equals("")))
        {
            if (DetectConnection.checkInternetConnection(getActivity())) {
                String state_list=SharePrefManager.getInstance(getActivity()).mGetOperators();
                if (!state_list.equals("")) {
                    GetOperators(state_list);
                }
                else
                {
                    mGetStateList();
                }
            }


        }
        else {
            mGetStateList();
        }
    }


    public void GetOperators(final String banklist) {

        String id="";
        String name="";
        String status="";

        try
        {

            JSONObject jsonObject=new JSONObject(banklist);


            if (jsonObject.has("status"))
            {
                status=jsonObject.getString("status");
            }


            if (status.equalsIgnoreCase("success")) {
                if (type.equalsIgnoreCase("state")||type.equalsIgnoreCase("mobile")) {
                    jsonArray = jsonObject.getJSONArray("stateList");
                }
                else if (type.equalsIgnoreCase("role")) {
                    jsonArray = jsonObject.getJSONArray("roles");
                }

                else if (type.equalsIgnoreCase("district")) {

                    jsonArray = jsonObject.getJSONArray("stateList");
                    for (int i=0; i<jsonArray.length(); i++) {

                        JSONObject data=jsonArray.getJSONObject(i);
                        Log.e("check","data "+data.toString());
                        if (state_id.equalsIgnoreCase(data.getString("state_id"))) {
                             jsonArray1 = data.getJSONArray("district_list");
                        }
                    }

                    jsonArray=jsonArray1;
                }

                for (int i = 0; i < jsonArray.length(); i++) {

                    JSONObject data = jsonArray.getJSONObject(i);

                    if (type.equalsIgnoreCase("district")) {
                        id=data.getString("district_id");
                        name=data.getString("district_name");
                    }
                    else if (type.equalsIgnoreCase("state")||type.equalsIgnoreCase("mobile")) {
                            id = data.getString("state_id");
                            name = data.getString("state_name");
                        }
                    else if (type.equalsIgnoreCase("role")) {
                            id = data.getString("role_id");
                            name = data.getString("role_title");
                        }

                    ListDataItems operators_items = new ListDataItems();
                    operators_items.setId(id);
                    operators_items.setName(name);
                    operators_items.setType(type);
                    beneficiaryitems.add(operators_items);
                    operatorsCardAdapter.notifyDataSetChanged();
                }

            }
            else
            {
                dialogFragment.dismiss();
            }
        }

        catch (JSONException e)
        {
            e.printStackTrace();
        }
    }

    @SuppressLint("StaticFieldLeak")
    protected void mGetStateList()
    {
        Uri.Builder builder = new Uri.Builder();

        new CallResAPIPOSTMethod(getActivity(), builder, sending_url, false, "POST") {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                textview_title.setText("Please wait...");
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                Log.e("response", "data " + s);
                if (type.equalsIgnoreCase("mobile")) {
                    textview_title.setText("Select Circle");
                } else {
                    textview_title.setText("Select " + type);
                }

                try {
                    JSONObject jsonObject = new JSONObject(s);
                    if (jsonObject.has("stateList")) {
                        JSONArray jsonArray = jsonObject.getJSONArray("stateList");
                        if (jsonArray.length() != 0) {
                            SharePrefManager.getInstance(getActivity()).mSaveOperators( s);
                            SharePrefManager.getInstance(getActivity()).mSaveSingleDataLong("operator_time", System.currentTimeMillis());
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                GetOperators(s);

            }
        }.execute();
    }
}