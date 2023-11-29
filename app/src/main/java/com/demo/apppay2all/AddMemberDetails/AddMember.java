package com.demo.apppay2all.AddMemberDetails;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.demo.apppay2all.AddMemberDetails.MemberTypeDetail.RoleListBottomSheet3DialogFragment;
import com.demo.apppay2all.AddMemberDetails.MemberTypeDetail.Role_Items;
import com.demo.apppay2all.BaseURL.BaseURL;
import com.demo.apppay2all.CallResAPIPOSTMethod;
import com.demo.apppay2all.DetectConnection;
import com.demo.apppay2all.ListData.ListDataBottomSheet3DialogFragment;
import com.demo.apppay2all.ListData.ListDataItems;

import com.demo.apppay2all.R;
import com.demo.apppay2all.Review_Activity;
import com.demo.apppay2all.SharePrefManager;

import org.json.JSONException;
import org.json.JSONObject;

public class AddMember extends AppCompatActivity {

    EditText ed_f_name,ed_l_name,ed_email,ed_mobile,ed_address,ed_city,ed_pincode,ed_shop_name,ed_shop_address,ed_pan_number;
    RelativeLayout rl_member_type,rl_state,rl_district;

    public static TextView tv_member_type,tv_state,tv_district;
    TextView tv_f_name_error,tv_l_name_error,tv_email_error,tv_mobile_error,tv_member_type_error,tv_address_error,
            tv_city_error,tv_pincode_error,tv_state_error,tv_district_error,tv_shop_name_error,tv_shop_address_error,tv_pan_number_error;

    public static String type="",state_id="",district_id="",role_id="";

    static String address_type="";

    Button button_submit;

    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_member);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ed_f_name=findViewById(R.id.ed_f_name);
        ed_l_name=findViewById(R.id.ed_l_name);
        ed_email=findViewById(R.id.ed_email);
        ed_mobile=findViewById(R.id.ed_mobile);
        ed_address=findViewById(R.id.ed_address);
        ed_city=findViewById(R.id.ed_city);
        ed_pincode=findViewById(R.id.ed_pincode);
        ed_shop_name=findViewById(R.id.ed_shop_name);
        ed_shop_address=findViewById(R.id.ed_shop_address);
        ed_pan_number=findViewById(R.id.ed_pan_number);


        rl_member_type=findViewById(R.id.rl_member_type);
        rl_member_type.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RoleListBottomSheet3DialogFragment bottomSheetDialogFragment = new RoleListBottomSheet3DialogFragment();
                Bundle bundle=new Bundle();
                bundle.putString("type","2");
                bundle.putString("activity","dth");
                bottomSheetDialogFragment.setArguments(bundle);
                bottomSheetDialogFragment.show(getSupportFragmentManager(), bottomSheetDialogFragment.getTag());
            }
        });

        rl_state=findViewById(R.id.rl_state);
        rl_state.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                type="State";
                address_type="add";
                ListDataBottomSheet3DialogFragment bottomSheetDialogFragment = new ListDataBottomSheet3DialogFragment();
                Bundle bundle = new Bundle();
                bundle.putString("type", type);
                bundle.putString("state_id", "");
                bundle.putString("act", "add_member");
                bundle.putString("url", BaseURL.BASEURL_B2C + "api/application/v1/state-list");
                bottomSheetDialogFragment.setArguments(bundle);
                bottomSheetDialogFragment.show(getSupportFragmentManager(), bottomSheetDialogFragment.getTag());
            }
        });

        rl_district=findViewById(R.id.rl_district);
        rl_district.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (state_id.equals(""))
                {
                    tv_state_error.setText("Please select state first");
                    tv_state_error.setVisibility(View.VISIBLE);
                }
                else {
                    type = "District";
                    address_type="add";
                    ListDataBottomSheet3DialogFragment bottomSheetDialogFragment = new ListDataBottomSheet3DialogFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString("type", type);
                    bundle.putString("state_id", state_id);
                    bundle.putString("act", "add_member");
                    bundle.putString("url", BaseURL.BASEURL_B2C + "api/application/v1/state-list");
                    bottomSheetDialogFragment.setArguments(bundle);
                    bottomSheetDialogFragment.show(getSupportFragmentManager(), bottomSheetDialogFragment.getTag());
                }
            }
        });

        tv_member_type=findViewById(R.id.tv_member_type);
        tv_state=findViewById(R.id.tv_state);
        tv_district=findViewById(R.id.tv_district);

        tv_f_name_error=findViewById(R.id.tv_f_name_error);
        tv_l_name_error=findViewById(R.id.tv_l_name_error);
        tv_email_error=findViewById(R.id.tv_email_error);
        tv_mobile_error=findViewById(R.id.tv_mobile_error);
        tv_member_type_error=findViewById(R.id.tv_member_type_error);
        tv_address_error=findViewById(R.id.tv_address_error);
        tv_city_error=findViewById(R.id.tv_city_error);
        tv_pincode_error=findViewById(R.id.tv_pincode_error);
        tv_state_error=findViewById(R.id.tv_state_error);
        tv_district_error=findViewById(R.id.tv_district_error);
        tv_shop_name_error=findViewById(R.id.tv_shop_name_error);
        tv_shop_address_error=findViewById(R.id.tv_shop_address_error);
        tv_pan_number_error=findViewById(R.id.tv_pan_number_error);


        button_submit=findViewById(R.id.button_submit);
        button_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (DetectConnection.checkInternetConnection(AddMember.this))
                {
                    if (ed_f_name.getText().toString().equals(""))
                    {
                        mShowError(tv_f_name_error,"Please enter first name");
                    }
                    else if (ed_l_name.getText().toString().equals(""))
                    {
                        mShowError(tv_l_name_error,"Please enter Last name");
                    }
                    else if (ed_email.getText().toString().equals(""))
                    {
                        mShowError(tv_email_error,"Please enter Email");
                    }
                    else if (ed_mobile.getText().toString().equals(""))
                    {
                        mShowError(tv_mobile_error,"Please enter Mobile ");
                    }
                    else if (ed_mobile.getText().toString().length()<10)
                    {
                        mShowError(tv_mobile_error,"Please enter a valid mobile");
                    }
                    else if (role_id.equals(""))
                    {
                        mShowError(tv_member_type_error,"Please enter select member type");
                    }
                    else if (ed_shop_name.getText().toString().equals(""))
                    {
                        mShowError(tv_shop_name_error,"Please enter Shop name");
                    }
                    else if (ed_shop_address.getText().toString().equals(""))
                    {
                        mShowError(tv_shop_address_error,"Please enter Shop address");
                    }
                    else if (ed_address.getText().toString().equals(""))
                    {
                        mShowError(tv_address_error,"Please enter address ");
                    }
                    else if (ed_city.getText().toString().equals(""))
                    {
                        mShowError(tv_city_error,"Please enter city ");
                    }
                    else if (ed_pincode.getText().toString().equals(""))
                    {
                        mShowError(tv_pincode_error,"Please enter PIN Code ");
                    }
                    else if (ed_pincode.getText().toString().length()<6)
                    {
                        mShowError(tv_pincode_error,"Please enter a valid PIN Code ");
                    }
                    else if (state_id.equals(""))
                    {
                        mShowError(tv_state_error,"Please select state");
                    }
                    else if (district_id.equals(""))
                    {
                        mShowError(tv_district_error,"Please select district");
                    }
                    else if (ed_pan_number.getText().toString().equals(""))
                    {
                        mShowError(tv_pan_number_error,"Please enter PAN number");
                    }
                    else
                    {
                        tv_f_name_error.setVisibility(View.GONE);
                        tv_l_name_error.setVisibility(View.GONE);
                        tv_email_error.setVisibility(View.GONE);
                        tv_mobile_error.setVisibility(View.GONE);
                        tv_member_type_error.setVisibility(View.GONE);
                        tv_address_error.setVisibility(View.GONE);
                        tv_city_error.setVisibility(View.GONE);
                        tv_pincode_error.setVisibility(View.GONE);
                        tv_state_error.setVisibility(View.GONE);
                        tv_district_error.setVisibility(View.GONE);
                        tv_shop_name_error.setVisibility(View.GONE);
                        tv_shop_address_error.setVisibility(View.GONE);
                        tv_pan_number_error.setVisibility(View.GONE);

                        mSubmitData();
                    }

                }
                else
                {
                    Toast.makeText(AddMember.this, "No internet connection", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public static void mGetData(ListDataItems items)
    {
        if(type.equalsIgnoreCase("state"))
        {
            state_id = items.getId();
            tv_state.setText(items.getName());
        }
        else {
            district_id = items.getId();
            tv_district.setText(items.getName());
        }
    }

    public void mGetRole(Role_Items role_items)
    {
        tv_member_type.setText(role_items.getOperator_name());
        role_id=role_items.getOperator_id();
    }

    protected void mShowError(TextView tv,String message)
    {
        tv.setVisibility(View.VISIBLE);
        tv.setText(message);
    }

    @SuppressLint("StaticFieldLeak")
    protected void mSubmitData()
    {
        String sending_url=BaseURL.BASEURL_B2C+"api/admin/add-members";
        Uri.Builder builder=new Uri.Builder();
        builder.appendQueryParameter("api_token", SharePrefManager.getInstance(AddMember.this).mGetApiToken());
        builder.appendQueryParameter("name",ed_f_name.getText().toString());
        builder.appendQueryParameter("last_name",ed_l_name.getText().toString());
        builder.appendQueryParameter("email",ed_email.getText().toString());
        builder.appendQueryParameter("mobile",ed_mobile.getText().toString());
        builder.appendQueryParameter("role_id",role_id);
        builder.appendQueryParameter("shop_name",ed_shop_name.getText().toString());
        builder.appendQueryParameter("office_address",ed_shop_address.getText().toString());
        builder.appendQueryParameter("address",ed_address.getText().toString());
        builder.appendQueryParameter("city",ed_city.getText().toString());
        builder.appendQueryParameter("state_id",state_id);
        builder.appendQueryParameter("district_id",district_id);
        builder.appendQueryParameter("pin_code",ed_pincode.getText().toString());
        builder.appendQueryParameter("pan_number",ed_pan_number.getText().toString());

        builder.appendQueryParameter("lock_amount","0");

        new CallResAPIPOSTMethod(AddMember.this,builder,sending_url,true,"POST")
        {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                dialog=new ProgressDialog(AddMember.this);
                dialog.setMessage("Please wait...");
                dialog.show();
                dialog.setCancelable(false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                dialog.dismiss();
                Log.e("response","data "+s);

                String status="",message="";
                try{
                    JSONObject jsonObject=new JSONObject(s);
                    if (jsonObject.has("status"))
                    {
                        status=jsonObject.getString("status");
                    }
                    if (jsonObject.has("message"))
                    {
                        message=jsonObject.getString("message");
                    }

                    if (status.equalsIgnoreCase("validation_error"))
                    {
                        if (jsonObject.has("errors"))
                        {
                            JSONObject errors=jsonObject.getJSONObject("errors");

                            if (errors.has("name"))
                            {
                                String name_err=errors.getString("name").replaceAll("\\[","").replaceAll("]","").replaceAll("\"","");
                                tv_f_name_error.setText(name_err);
                                tv_f_name_error.setVisibility(View.VISIBLE);
                            }

                            if (errors.has("last_name"))
                            {
                                String last_name=errors.getString("last_name").replaceAll("\\[","").replaceAll("]","").replaceAll("\"","");
                                tv_l_name_error.setText(last_name);
                                tv_l_name_error.setVisibility(View.VISIBLE);
                            }

                            if (errors.has("email"))
                            {
                                String err=errors.getString("email").replaceAll("\\[","").replaceAll("]","").replaceAll("\"","");
                                tv_email_error.setText(err);
                                tv_email_error.setVisibility(View.VISIBLE);
                            }

                            if (errors.has("mobile"))
                            {
                                String err=errors.getString("mobile").replaceAll("\\[","").replaceAll("]","").replaceAll("\"","");
                                tv_mobile_error.setText(err);
                                tv_mobile_error.setVisibility(View.VISIBLE);
                            }

                            if (errors.has("shop_name"))
                            {
                                String err=errors.getString("shop_name").replaceAll("\\[","").replaceAll("]","").replaceAll("\"","");
                                tv_shop_name_error.setText(err);
                                tv_shop_name_error.setVisibility(View.VISIBLE);
                            }

                            if (errors.has("address"))
                            {
                                String err=errors.getString("address").replaceAll("\\[","").replaceAll("]","").replaceAll("\"","");
                                tv_address_error.setText(err);
                                tv_address_error.setVisibility(View.VISIBLE);
                            }

                            if (errors.has("office_address"))
                            {
                                String err=errors.getString("office_address").replaceAll("\\[","").replaceAll("]","").replaceAll("\"","");
                                tv_shop_address_error.setText(err);
                                tv_shop_address_error.setVisibility(View.VISIBLE);
                            }

                            if (errors.has("city"))
                            {
                                String err=errors.getString("city").replaceAll("\\[","").replaceAll("]","").replaceAll("\"","");
                                tv_city_error.setText(err);
                                tv_city_error.setVisibility(View.VISIBLE);
                            }

                            if (errors.has("state_id"))
                            {
                                String err=errors.getString("state_id").replaceAll("\\[","").replaceAll("]","").replaceAll("\"","");
                                tv_state_error.setText(err);
                                tv_state_error.setVisibility(View.VISIBLE);
                            }

                            if (errors.has("district_id"))
                            {
                                String err=errors.getString("district_id").replaceAll("\\[","").replaceAll("]","").replaceAll("\"","");
                                tv_district_error.setText(err);
                                tv_district_error.setVisibility(View.VISIBLE);
                            }

                            if (errors.has("permanent_pin_code"))
                            {
                                String err=errors.getString("permanent_pin_code").replaceAll("\\[","").replaceAll("]","").replaceAll("\"","");
                                tv_pincode_error.setText(err);
                                tv_pincode_error.setVisibility(View.VISIBLE);
                            }

                            if (errors.has("pan_number"))
                            {
                                String err=errors.getString("pan_number").replaceAll("\\[","").replaceAll("]","").replaceAll("\"","");
                                tv_pan_number_error.setText(err);
                                tv_pan_number_error.setVisibility(View.VISIBLE);
                            }
                        }

                        Toast.makeText(AddMember.this, "Please check all input", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Intent intent = new Intent(AddMember.this, Review_Activity.class);
                        intent.putExtra("status", status);
                        intent.putExtra("message", message);
                        intent.putExtra("activity", "addmember");
                        startActivity(intent);
                        finish();
                    }
                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }
            }
        }
        .execute();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId()==android.R.id.home)
        {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }


}