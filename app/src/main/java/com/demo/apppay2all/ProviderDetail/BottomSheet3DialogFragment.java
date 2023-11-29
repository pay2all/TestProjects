package com.demo.apppay2all.ProviderDetail;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.net.Uri;
import android.os.AsyncTask;
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

import com.demo.apppay2all.CallResAPIPOSTMethod;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

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

import com.demo.apppay2all.BaseURL.BaseURL;
import com.demo.apppay2all.R;
import com.demo.apppay2all.SharePrefManager;


/**
 * Created by Basant on 3/29/2018.
 */

public class BottomSheet3DialogFragment extends BottomSheetDialogFragment {

    RecyclerView recyclerview_operator;

    RecyclerView.LayoutManager layoutManager;
    private List<Operators_Items> beneficiaryitems;

    String type;
    public static String activity_name;

    TextView textview_title;

    List<Operators_Items> myJSON;

    private BottomSheetBehavior mBehavior;

    ImageView imageview_back_icon;

    public static BottomSheet3DialogFragment dialogFragment;

    String operator;
    OperatorsCardAdapter operatorsCardAdapter=null;

    EditText edittext_search;

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

        textview_title=view.findViewById(R.id.textview_title);

        edittext_search=view.findViewById(R.id.edittext_search);
        edittext_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                if (myJSON!=null) {
                    List<Operators_Items> temp = new ArrayList();
                    for (Operators_Items d : myJSON) {
                        //or use .equal(text) with you want equal match
                        //use .toLowerCase() for better matches
                        if (d.getOperator_name().toLowerCase().contains(editable.toString().toLowerCase())) {
                            temp.add(d);
                        }
                    }

                    //update recyclerview
                    operatorsCardAdapter.UpdateList(temp);
                }
            }
        });

        dialogFragment=this;
        imageview_back_icon=(ImageView)view.findViewById(R.id.imageview_back_icon);
        imageview_back_icon.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        operator=SharePrefManager.getInstance(getActivity()).mGetOperators();

//        Log.e("operator",operator);

        type=getArguments().getString("type");
        activity_name=getArguments().getString("activity");

        view.findViewById(R.id.fakeShadow).setVisibility(View.GONE);
        recyclerview_operator = (RecyclerView) view.findViewById(R.id.recyclerview_operator);
        recyclerview_operator.setHasFixedSize(true);
        recyclerview_operator.setLayoutManager(new LinearLayoutManager(getContext()));

        beneficiaryitems=new ArrayList<>();

        operatorsCardAdapter=new OperatorsCardAdapter(getContext(),beneficiaryitems);
        recyclerview_operator.setAdapter(operatorsCardAdapter);

        if (activity_name.equalsIgnoreCase("circle")){

            mGetCicreleList();

        }

        else {

            mGetOperators();

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

    public void mShowOperators(String operator) {

        String service_id="";
        String provider_id="";
        String provider_name="";
        String provider_image="";
        String status="";

        try
        {

            JSONObject jsonObject=new JSONObject(operator);
            if (jsonObject.has("status"))
            {
                status=jsonObject.getString("status");
            }

            if (status.equalsIgnoreCase("success")) {
                JSONArray jsonArray = jsonObject.getJSONArray("providers");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject data = jsonArray.getJSONObject(i);
                    service_id = data.getString("service_id");
                    provider_id = data.getString("provider_id");
                    provider_name = data.getString("provider_name");

                    if (data.has("provider_image")) {
                        provider_image = data.getString("provider_image");
                    }
                    else
                    {
                        provider_image = "";
                    }

                    if (service_id.equals(type)) {
                        Operators_Items operators_items = new Operators_Items();
                        operators_items.setOperator_id(provider_id);
                        operators_items.setOperator_name(provider_name);
                        operators_items.setOperator_image(provider_image);
                        beneficiaryitems.add(operators_items);
                    }
                    operatorsCardAdapter.notifyDataSetChanged();
                }
            }
            else if (!SharePrefManager.getInstance(getActivity()).mGetOperators().equals(""))
            {
                JSONObject jsonObject1=new JSONObject(SharePrefManager.getInstance(getActivity()).mGetOperators());
                JSONArray jsonArray = jsonObject1.getJSONArray("providers");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject data = jsonArray.getJSONObject(i);
                    service_id = data.getString("service_id");
                    provider_id = data.getString("provider_id");
                    provider_name = data.getString("provider_name");

                    if (data.has("provider_image")) {
                        provider_image = data.getString("provider_image");
                    }
                    else
                    {
                        provider_image = "";
                    }

                    if (service_id.equals(type)) {
                        Operators_Items operators_items = new Operators_Items();
                        operators_items.setOperator_id(provider_id);
                        operators_items.setOperator_name(provider_name);
                        operators_items.setOperator_image(provider_image);
                        beneficiaryitems.add(operators_items);
                    }
                    operatorsCardAdapter.notifyDataSetChanged();
                }
            }
            else
            {
                Toast.makeText(getActivity(), "Unable to fetch operator list please check your internet connection and try again...", Toast.LENGTH_LONG).show();
            }

        }

        catch (JSONException e)
        {
            e.printStackTrace();
        }
    }

    @SuppressLint("StaticFieldLeak")
    protected void mGetOperators()
    {
        String sending=BaseURL.BASEURL_B2C+"api/application/v1/get-provider";
        Uri.Builder builder=new Uri.Builder();
        builder.appendQueryParameter("api_token",SharePrefManager.getInstance(getActivity()).mGetApiToken());

        new CallResAPIPOSTMethod(getActivity(),builder,sending,true,"POST")
        {
            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                Log.e("response","data "+s);
                textview_title.setText("Select "+activity_name+" Operator");

                SharePrefManager.getInstance(getActivity()).mSaveOperators(s);
                mShowOperators(s);

            }

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                textview_title.setText("Please wait...");
            }
        }.execute();
    }

    private  void  mGetCicreleList() {
        class getJSONData extends AsyncTask<String, String, String> {


            HttpURLConnection urlConnection;


            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                textview_title.setText("Please wait...");
            }
            @Override
            protected String doInBackground(String... args) {

                StringBuilder result = new StringBuilder();

                try {
                    URL url = new URL(BaseURL.BASEURL+"api/v1/get-circles");
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

                //Do something with the JSON string
                textview_title.setText("Select State");

                Log.e("circle response",result);

                if (!result.equals("")) {
                    mShwCircles(result);
                }
                else
                {
                    dialogFragment.dismiss();
                }
            }
        }

        getJSONData getJSONData=new getJSONData();
        getJSONData.execute();
    }


    public void mShwCircles(final String banklist) {

        String id="";
        String circle="";

        try
        {
//            JSONObject jsonObject=new JSONObject(banklist);
            JSONArray jsonArray =new JSONArray(banklist);

//            if (jsonArray.length()!=0) {
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject data = jsonArray.getJSONObject(i);
                id = data.getString("id");
                circle = data.getString("name");


                Operators_Items operators_items = new Operators_Items();
                operators_items.setOperator_id(id);
                operators_items.setOperator_image("");
                operators_items.setOperator_name(circle);
                beneficiaryitems.add(operators_items);

                operatorsCardAdapter.notifyDataSetChanged();
                myJSON=beneficiaryitems;
            }
//            }

        }

        catch (JSONException e)
        {
            e.printStackTrace();
        }
    }
}