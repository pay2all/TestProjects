package com.demo.apppay2all.AddMemberDetails.MemberTypeDetail;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.demo.apppay2all.DetectConnection;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.demo.apppay2all.BaseURL.BaseURL;
import com.demo.apppay2all.CallResAPIPOSTMethod;
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

public class RoleListBottomSheet3DialogFragment extends BottomSheetDialogFragment {

    RecyclerView recyclerview_operator;

    RecyclerView.LayoutManager layoutManager;
    private List<Role_Items> beneficiaryitems;

    String type;
    public static String activity_name;


    List<Role_Items> myJSON;

    private BottomSheetBehavior mBehavior;

    ImageView imageview_back_icon;

    public static RoleListBottomSheet3DialogFragment dialogFragment;

    String operator;
    RoleCardAdapter operatorsCardAdapter=null;

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

        operator= SharePrefManager.getInstance(getActivity()).mGetOperators();

//        Log.e("operator",operator);


        textview_title=view.findViewById(R.id.textview_title);
        textview_title.setText("Select Member Type");

        view.findViewById(R.id.fakeShadow).setVisibility(View.GONE);
        recyclerview_operator = (RecyclerView) view.findViewById(R.id.recyclerview_operator);
        recyclerview_operator.setHasFixedSize(true);
        recyclerview_operator.setLayoutManager(new LinearLayoutManager(getContext()));

        beneficiaryitems=new ArrayList<>();

        operatorsCardAdapter=new RoleCardAdapter(getContext(),beneficiaryitems);
        recyclerview_operator.setAdapter(operatorsCardAdapter);

        if (DetectConnection.checkInternetConnection(getActivity())) {
            mGetRoles();
        }
        else
        {
            Toast.makeText(getContext(), "No internet connection", Toast.LENGTH_SHORT).show();
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

    public void GetOperators() {

        String service_id="";
        String provider_id="";
        String provider_name="";
        String provider_image="";

        try
        {

            JSONArray jsonArray = new JSONArray(operator);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject data = jsonArray.getJSONObject(i);
                service_id = data.getString("service_id");
                provider_id = data.getString("id");
                provider_name = data.getString("provider_name");
                provider_image = data.getString("provider_image");

                if (service_id.equals(type)) {
                    Role_Items operators_items = new Role_Items();
                    operators_items.setOperator_id(provider_id);
                    operators_items.setOperator_name(provider_name);
                    operators_items.setOperator_image(provider_image);
                    beneficiaryitems.add(operators_items);
                }
                operatorsCardAdapter.notifyDataSetChanged();
            }

        }

        catch (JSONException e)
        {
            e.printStackTrace();
        }
    }

    @SuppressLint("StaticFieldLeak")
    protected void mGetRoles()
    {
        String sending_url= BaseURL.BASEURL_B2C+ "api/admin/get-roles";
        Uri.Builder builder=new Uri.Builder();
        builder.appendQueryParameter("api_token",SharePrefManager.getInstance(getContext()).mGetApiToken());

        new CallResAPIPOSTMethod(getActivity(),builder,sending_url,true,"POST")
        {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                textview_title.setText("Please wait...");
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                Log.e("response","data "+s);

                textview_title.setText("Select Member Type");

                try
                {

                    JSONObject jsonObject=new JSONObject(s);
                    JSONArray jsonArray =jsonObject.getJSONArray("roles");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject data = jsonArray.getJSONObject(i);

                        Role_Items operators_items = new Role_Items();
                        operators_items.setOperator_id(data.getString("role_id"));
                        operators_items.setOperator_name(data.getString("role_title"));
                        operators_items.setOperator_image("");
                        beneficiaryitems.add(operators_items);
                        operatorsCardAdapter.notifyDataSetChanged();
                    }

                    if (beneficiaryitems.size()==0)
                    {
                        Toast.makeText(getContext(), "Member Type list not found", Toast.LENGTH_SHORT).show();
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