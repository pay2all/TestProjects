package com.demo.apppay2all.FormDetails;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.demo.apppay2all.BaseURL.BaseURL;
import com.demo.apppay2all.CallResAPIPOSTMethod;
import com.demo.apppay2all.DetectConnection;
import com.demo.apppay2all.ListData.ListDataBottomSheet3DialogFragment;
import com.demo.apppay2all.ListData.ListDataItems;
import com.demo.apppay2all.R;
import com.demo.apppay2all.SharePrefManager;

import org.json.JSONException;
import org.json.JSONObject;

public class AddressFragment extends Fragment {


    EditText ed_address,ed_city,ed_pincode,ed_p_address,ed_p_city,ed_p_pincode,ed_shop_name,ed_office_address;
    ImageButton ib_prev,ib_next;
    RelativeLayout rl_state,rl_district, rl_present_state,rl_present_district;
    static TextView tv_state,tv_dictrict,tv_present_state,tv_present_dictrict;
    static String state_id="",district_id="",state_present_id="",district_present_id="";
    static String type="",address_type="";
    CheckBox cb_shame;

    ProgressDialog dialog;

    TextView tv_address_error,tv_city_error,tv_state_error,tv_district_error,tv_pincode_error,tv_present_address_error,
            tv_present_city_error,tv_present_state_error,tv_present_district_error,tv_present_pincode_error,tv_shopname_error,tv_office_address_error;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_addresss,container,false);

        tv_address_error=view.findViewById(R.id.tv_address_error);
        tv_city_error=view.findViewById(R.id.tv_city_error);
        tv_state_error=view.findViewById(R.id.tv_state_error);
        tv_district_error=view.findViewById(R.id.tv_district_error);
        tv_pincode_error=view.findViewById(R.id.tv_pincode_error);
        tv_present_address_error=view.findViewById(R.id.tv_present_address_error);
        tv_present_city_error=view.findViewById(R.id.tv_present_city_error);
        tv_present_state_error=view.findViewById(R.id.tv_present_state_error);
        tv_present_district_error=view.findViewById(R.id.tv_present_district_error);
        tv_present_pincode_error=view.findViewById(R.id.tv_present_pincode_error);
        tv_shopname_error=view.findViewById(R.id.tv_shopname_error);
        tv_office_address_error=view.findViewById(R.id.tv_office_address_error);

        ed_shop_name=view.findViewById(R.id.ed_shop_name);
        ed_office_address=view.findViewById(R.id.ed_office_address);

        ed_address=view.findViewById(R.id.ed_address);
        ed_address.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (cb_shame.isChecked())
                {
                    ed_p_address.setText(s.toString());
                }

            }
        });

        ed_city=view.findViewById(R.id.ed_city);
        ed_city.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if (cb_shame.isChecked()) {
                    ed_p_city.setText(s.toString());
                }
            }
        });

        ed_pincode=view.findViewById(R.id.ed_pincode);
        ed_pincode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (cb_shame.isChecked()) {
                    ed_p_pincode.setText(s.toString());
                }
            }
        });

        ed_p_address=view.findViewById(R.id.ed_p_address);
        ed_p_city=view.findViewById(R.id.ed_p_city);
        ed_p_pincode=view.findViewById(R.id.ed_p_pincode);

        rl_state=view.findViewById(R.id.rl_state);
        rl_state.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                type="State";
                address_type="add";
                ListDataBottomSheet3DialogFragment bottomSheetDialogFragment = new ListDataBottomSheet3DialogFragment();
                Bundle bundle = new Bundle();
                bundle.putString("type", type);
                bundle.putString("state_id", "");
                bundle.putString("url", BaseURL.BASEURL_B2C + "api/application/v1/state-list");
                bottomSheetDialogFragment.setArguments(bundle);
                bottomSheetDialogFragment.show(getActivity().getSupportFragmentManager(), bottomSheetDialogFragment.getTag());
            }
        });

        rl_present_state=view.findViewById(R.id.rl_present_state);
        rl_present_state.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                type="State";
                address_type="present";
                ListDataBottomSheet3DialogFragment bottomSheetDialogFragment = new ListDataBottomSheet3DialogFragment();
                Bundle bundle = new Bundle();
                bundle.putString("type", type);
                bundle.putString("state_id", "");
                bundle.putString("url", BaseURL.BASEURL_B2C + "api/application/v1/state-list");
                bottomSheetDialogFragment.setArguments(bundle);
                bottomSheetDialogFragment.show(getActivity().getSupportFragmentManager(), bottomSheetDialogFragment.getTag());
            }
        });

        rl_district=view.findViewById(R.id.rl_district);
        rl_district.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                type="District";
                address_type="add";
                ListDataBottomSheet3DialogFragment bottomSheetDialogFragment = new ListDataBottomSheet3DialogFragment();
                Bundle bundle = new Bundle();
                bundle.putString("type", type);
                bundle.putString("state_id", state_id);
                bundle.putString("url", BaseURL.BASEURL_B2C + "api/application/v1/state-list");
                bottomSheetDialogFragment.setArguments(bundle);
                bottomSheetDialogFragment.show(getActivity().getSupportFragmentManager(), bottomSheetDialogFragment.getTag());
            }
        });

        rl_present_district=view.findViewById(R.id.rl_present_district);
        rl_present_district.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                type="District";
                address_type="present";
                ListDataBottomSheet3DialogFragment bottomSheetDialogFragment = new ListDataBottomSheet3DialogFragment();
                Bundle bundle = new Bundle();
                bundle.putString("type", type);
                bundle.putString("state_id", state_present_id);
                bundle.putString("url", BaseURL.BASEURL_B2C + "api/application/v1/state-list");
                bottomSheetDialogFragment.setArguments(bundle);
                bottomSheetDialogFragment.show(getActivity().getSupportFragmentManager(), bottomSheetDialogFragment.getTag());
            }
        });

        tv_state=view.findViewById(R.id.tv_state);
        tv_dictrict=view.findViewById(R.id.tv_dictrict);


        tv_present_state=view.findViewById(R.id.tv_present_state);
        tv_present_dictrict=view.findViewById(R.id.tv_present_dictrict);

        cb_shame=view.findViewById(R.id.cb_shame);
        cb_shame.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mShmaeAsabove(isChecked);
            }
        });

        ib_prev=view.findViewById(R.id.ib_prev);
        ib_prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((FormsKyc)getActivity()).mChangeFragment(new PersonalInfoFragment(),"3");
            }
        });

        ib_next=view.findViewById(R.id.ib_next);
        ib_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (DetectConnection.checkInternetConnection(getActivity()))
                {
                    if (ed_address.getText().toString().equals(""))
                    {
                        mShowError(tv_address_error,"Please enter address",null,false);
                    }
                    else if (ed_city.getText().toString().equals(""))
                    {
                        mShowError(tv_city_error,"Please enter city",tv_address_error,true);
                    }
                    else if (state_id.equals(""))
                    {
                        mShowError(tv_state_error,"Please selelct state",tv_city_error,true);
                    }
                    else if (district_id.equals(""))
                    {
                        mShowError(tv_district_error,"Please select district",tv_state_error,true);
                    }
                    else if (ed_pincode.getText().toString().equals(""))
                    {
                        mShowError(tv_pincode_error,"Please enter PIN code",tv_district_error,true);
                    }

                    else if (ed_p_address.getText().toString().equals(""))
                    {
                        mShowError(tv_present_address_error,"Please enter present address",tv_pincode_error,true);
                    }
                    else if (ed_p_city.getText().toString().equals(""))
                    {
                        mShowError(tv_present_city_error,"Please enter present city",tv_present_address_error,true);
                    }
                    else if (state_present_id.equals(""))
                    {
                        mShowError(tv_present_state_error,"Please select present state name",tv_present_city_error,true);
                    }
                    else if (district_present_id.equals(""))
                    {
                        mShowError(tv_present_district_error,"Please select present district name",tv_present_state_error,true);
                    }
                    else if (ed_p_pincode.getText().toString().equals(""))
                    {
                        mShowError(tv_present_pincode_error,"Please enter present pincode",tv_present_district_error,true);
                    }
                    else if (ed_shop_name.getText().toString().equals(""))
                    {
                        mShowError(tv_shopname_error,"Please enter shop name",tv_present_pincode_error,true);
                    }
                    else if (ed_office_address.getText().toString().equals(""))
                    {
                        mShowError(tv_office_address_error,"Please enter shop address",tv_shopname_error,true);
                    }
                    else
                    {

                        String address=ed_address.getText().toString();
                        String city=ed_city.getText().toString();
                        String pincode=ed_pincode.getText().toString();
                        String p_address=ed_p_address.getText().toString();
                        String p_city=ed_p_city.getText().toString();
                        String p_pincode=ed_p_pincode.getText().toString();
                        String shopn_name=ed_shop_name.getText().toString();
                        String office_address=ed_office_address.getText().toString();

                        mSubmitAddress(address, city, state_id, district_id, pincode,p_address,state_present_id,district_present_id,p_city,p_pincode,shopn_name, office_address);

                    }
                }
                else
                {
                    Toast.makeText(getContext(), "No internet connection", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;
    }

    public static void mGetData(ListDataItems items)
    {
     if(type.equalsIgnoreCase("state"))
     {
         if (address_type.equalsIgnoreCase("present"))
         {
             state_present_id = items.getId();
             tv_present_state.setText(items.getName());
         }
         else {
             state_id = items.getId();
             tv_state.setText(items.getName());
         }
     }
     else {
         if (address_type.equalsIgnoreCase("present"))
         {
             district_present_id = items.getId();
             tv_present_dictrict.setText(items.getName());
         }
         else {
             district_id = items.getId();
             tv_dictrict.setText(items.getName());
         }
     }
    }

    protected void mShmaeAsabove(boolean check)
    {
        if (check) {
            ed_p_address.setText(ed_address.getText().toString());
            ed_p_city.setText(ed_city.getText().toString());
            ed_p_pincode.setText(ed_pincode.getText().toString());

            state_present_id = state_id;
            tv_present_state.setText(tv_state.getText().toString());

            district_present_id = district_id;
            tv_present_dictrict.setText(tv_dictrict.getText().toString());
        }
        else
        {
            ed_p_address.setText("");
            ed_p_city.setText("");
            ed_p_pincode.setText("");

            state_present_id="";
            district_present_id="";
        }
    }

    @SuppressLint("StaticFieldLeak")
    protected void mSubmitAddress(String permanent_address, String permanent_city, String permanent_state, String permanent_district, String permanent_pin_code, String present_address, String present_state, String present_district, String present_city, String present_pin_code, String shop_name, String office_address)
    {
        Uri.Builder builder=new Uri.Builder();
        builder.appendQueryParameter("api_token", SharePrefManager.getInstance(getContext()).mGetApiToken());
        builder.appendQueryParameter("permanent_address",permanent_address);
        builder.appendQueryParameter("permanent_state",permanent_state);
        builder.appendQueryParameter("permanent_district",permanent_district);
        builder.appendQueryParameter("permanent_city",permanent_city);
        builder.appendQueryParameter("permanent_pin_code",permanent_pin_code);
        builder.appendQueryParameter("present_address",present_address);
        builder.appendQueryParameter("present_state",present_state);
        builder.appendQueryParameter("present_district",present_district);
        builder.appendQueryParameter("present_city",present_city);
        builder.appendQueryParameter("present_pin_code",present_pin_code);
        builder.appendQueryParameter("shop_name",shop_name);
        builder.appendQueryParameter("office_address",office_address);

        String sending_url=BaseURL.BASEURL_B2C+"api/application/v1/update-profile";

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
                Log.e("response","data "+s);


                String status="",message="";
                try {
                    JSONObject jsonObject=new JSONObject(s);
                    if (jsonObject.has("success"))
                    {
                        status=jsonObject.getString("status");
                    }
                    if (jsonObject.has("message"))
                    {
                        message=jsonObject.getString("message");
                    }

                    if (status.equalsIgnoreCase("success"))
                    {
                        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
                    }
                    else if (!status.equalsIgnoreCase("success"))
                    {
                        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
//                        startActivity(new Intent(getActivity(), MainActivity.class));
                        getActivity().finish();
                    }
                    else
                    {
                        Toast.makeText(getContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
                    }
                }
                catch (JSONException e)
                {

                }
            }
        }.execute();

    }

    protected void mShowError(TextView tv_error,String message,TextView tv_pre,boolean pre_tv)
    {
        tv_error.setText(message);
        tv_error.setVisibility(View.VISIBLE);

        if (pre_tv) {
            tv_pre.setVisibility(View.GONE);
        }
    }
}
