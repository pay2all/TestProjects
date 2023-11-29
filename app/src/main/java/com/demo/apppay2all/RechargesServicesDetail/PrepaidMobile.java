package com.demo.apppay2all.RechargesServicesDetail;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.ContactsContract;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.demo.apppay2all.BaseURL.BaseURL;
import com.demo.apppay2all.Browse_Plan.BrowsePlan;
import com.demo.apppay2all.CallResAPIGetMethod;
import com.demo.apppay2all.CallResAPIPOSTMethod;
import com.demo.apppay2all.DetectConnection;
import com.demo.apppay2all.ListData.ListDataBottomSheet3DialogFragment;
import com.demo.apppay2all.ListData.ListDataItems;
import com.demo.apppay2all.LocationDetails.MyLocation;
import com.demo.apppay2all.Login;
import com.demo.apppay2all.NumberToWord;
import com.demo.apppay2all.ProviderDetail.Operator;
import com.demo.apppay2all.ProviderDetail.OperatorsCardAdapter;
import com.demo.apppay2all.ProviderDetail.Operators_Items;
import com.demo.apppay2all.R;
import com.demo.apppay2all.ROffer.R_Offer;
import com.demo.apppay2all.ReceiptDetail.Receipt;
import com.demo.apppay2all.ServiceDetailSub.SubServiceItem;
import com.demo.apppay2all.ServicesDetails.ServicesItems;
import com.demo.apppay2all.SharePrefManager;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class PrepaidMobile extends AppCompatActivity {
    static LinearLayout ll_number,ll_optional1,ll_optional2,ll_optional3,ll_amount;
    static TextView tv_number1,textview_optional1,textview_optional2,textview_optional3;

    public static EditText ed_number,ed_other1,ed_other2,ed_other3,ed_amount;

    Button bt_proceed, bt_Verify;

    public  static RelativeLayout rl_operator,rl_circle,rl_payment_mode;
    public static TextView tv_provider;
    TextView tv_circle,tv_validate;

    public static String provider_id="",provider_name="",provider_image="";
    SubServiceItem rechargeItems;

    static ProgressDialog dialog;
    String optional_para4="";
    String optional_para4_value="";
    static String optional1="",optional2="",optional3="";
    TextView tv_number_error,tv_operator_error,tv_circle_error,tv_optional1_error,tv_optional2_error,tv_optional3_error,tv_amount_error;

    AlertDialog alertDialog=null;
    LinearLayout ll_bill_detail;

    String number="",amount="";

    static String service_id="";

    RecyclerView recyclerview_operator;

    RecyclerView.LayoutManager layoutManager;
    private List<Operators_Items> beneficiaryitems;

    PopupWindow popupWindow;
    OperatorsCardAdapter operatorsCardAdapter=null;

    static Activity activity;

    TextView tv_view_plan;
    LinearLayout ll_circle;
    String circle_id="";

    ImageView iv_ope_icon;
    TextView tv_operator;

    TextView tv_r_offer;

    public  static PopupMenu popupMenu_payment_mode;

    String selected_mode="";

    TextView tv_payment_moce,tv_payment_mode_error;

    ImageView imageview_contact;
    

    TextView tv_location;

    LocationManager locationManager=null;


    private SettingsClient mSettingsClient;
    private LocationSettingsRequest mLocationSettingsRequest;
    private static final int REQUEST_CHECK_SETTINGS = 214;
    private static final int REQUEST_ENABLE_GPS = 516;
    MyLocation myLocation=new MyLocation();

    double lat=0.0,log=0.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prepaid);

        tv_location=findViewById(R.id.tv_location);
        locationManager=(LocationManager) getSystemService(Context.LOCATION_SERVICE);
        tv_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (ActivityCompat.checkSelfPermission(PrepaidMobile.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(PrepaidMobile.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(PrepaidMobile.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
                }
                else{
                    mShowEnabledLocationDialog();
                }
            }
        });

        imageview_contact = findViewById(R.id.imageview_contact);
        imageview_contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCheckReadContact()) {
                    Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.CommonDataKinds.Phone.CONTENT_URI);
                    startActivityForResult(intent, 10);
                }
                else
                {
                    mRequestReadContact();
                }
            }
        });

        activity=this;
        Intent intent=getIntent();
        if (intent.hasExtra("provider_id"))
        {
            provider_id=intent.getStringExtra("provider_id");
        }

        if (intent.hasExtra("provider_name"))
        {
            provider_name=intent.getStringExtra("provider_name");
        }

        if (intent.hasExtra("provider_image"))
        {
            provider_image=intent.getStringExtra("provider_image");
        }

        rechargeItems=(SubServiceItem) getIntent().getSerializableExtra("DATA");

        service_id=rechargeItems.getService_id();
        bt_proceed=findViewById(R.id.bt_proceed);
        bt_Verify =findViewById(R.id.bt_Verify);

        ll_amount=findViewById(R.id.ll_amount);
        
        
        ed_amount=findViewById(R.id.ed_amount);
        TextView tv_amount_word=findViewById(R.id.tv_amount_word);
        ed_amount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                if (!editable.toString().equals(""))
                {
                    tv_amount_word.setText(new NumberToWord().convert(Integer.parseInt(editable.toString())));
                }
                else
                {
                    tv_amount_word.setText("");
                }
            }
        });


        tv_number1=findViewById(R.id.tv_number1);
        textview_optional1=findViewById(R.id.textview_optional1);
        textview_optional2=findViewById(R.id.textview_optional2);
        textview_optional3=findViewById(R.id.textview_optional3);

        tv_circle=findViewById(R.id.tv_circle);
        ll_circle=findViewById(R.id.ll_state);
        tv_view_plan=findViewById(R.id.tv_view_plan);
        tv_circle_error=findViewById(R.id.tv_circle_error);

        iv_ope_icon=findViewById(R.id.iv_ope_icon);
        if (!provider_image.equals(""))
        {
            Glide.with(PrepaidMobile.this).load(provider_image).error(Glide.with(iv_ope_icon).load(R.drawable.photo)).into(iv_ope_icon);
        }
        else
        {
            iv_ope_icon.setBackground(getResources().getDrawable(R.drawable.logo));
        }

        tv_operator=findViewById(R.id.tv_operator);
        tv_operator.setText(provider_name);

        ll_circle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ListDataBottomSheet3DialogFragment bottomSheetDialogFragment = new ListDataBottomSheet3DialogFragment();
                Bundle bundle = new Bundle();
                bundle.putString("type", "mobile");
                bundle.putString("state_id", "");
                bundle.putString("url", BaseURL.BASEURL_B2C + "api/application/v1/state-list");
                bottomSheetDialogFragment.setArguments(bundle);
                bottomSheetDialogFragment.show(getSupportFragmentManager(), bottomSheetDialogFragment.getTag());
            }
        });

        tv_payment_moce=findViewById(R.id.tv_payment_mode);
        tv_payment_mode_error=findViewById(R.id.tv_payment_mode_error);

        rl_payment_mode=findViewById(R.id.rl_payment_mode);
        rl_payment_mode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (popupMenu_payment_mode!=null)
                {
                    popupMenu_payment_mode.show();
                }
            }
        });

        popupMenu_payment_mode=new PopupMenu(PrepaidMobile.this,rl_payment_mode);
        popupMenu_payment_mode.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                selected_mode=item.getTitle().toString();
                tv_payment_moce.setText(selected_mode);
                return true;
            }
        });

        tv_r_offer=findViewById(R.id.tv_r_offer);

        if (rechargeItems.getService_id().equalsIgnoreCase("1"))
        {
            if (SharePrefManager.getInstance(PrepaidMobile.this).mGetSingleData("view_plan").equals("1")) {
                ll_circle.setVisibility(View.VISIBLE);
                tv_view_plan.setVisibility(View.VISIBLE);
                tv_r_offer.setVisibility(View.VISIBLE);
            }
            else {
                ll_circle.setVisibility(View.GONE);
                tv_view_plan.setVisibility(View.GONE);
                tv_r_offer.setVisibility(View.GONE);
            }
        }
        else if (rechargeItems.getService_id().equalsIgnoreCase("2"))
        {
            tv_view_plan.setVisibility(View.VISIBLE);
            tv_view_plan.setText("View Info");
        }

        tv_r_offer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (DetectConnection.checkInternetConnection(PrepaidMobile.this))
                {
                    if (ed_number.getText().toString().equals(""))
                    {
                        tv_number_error.setText("Please enter mobile number");
                        tv_number_error.setVisibility(View.VISIBLE);
                    }
                    else if (ed_number.getText().toString().length()<10)
                    {
                        tv_number_error.setText("Please enter a valid mobile number");
                        tv_number_error.setVisibility(View.VISIBLE);
                    }
                    else if (circle_id.equals(""))
                    {
                        tv_circle_error.setText("Please select circle");
                        tv_circle_error.setVisibility(View.VISIBLE);
                    }
                    else
                    {
                        tv_number_error.setVisibility(View.GONE);
                        tv_circle_error.setVisibility(View.GONE);
                        Intent intent=new Intent(PrepaidMobile.this, R_Offer.class);
                        intent.putExtra("number",ed_number.getText().toString());
                        intent.putExtra("provider",provider_id);
                        intent.putExtra("state",circle_id);
                        intent.putExtra("activity","prepaid");
                        startActivity(intent);
                    }
                }
                else
                {
                    Toast.makeText(PrepaidMobile.this, "No internet connection", Toast.LENGTH_SHORT).show();
                }
            }
        });

        tv_view_plan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (DetectConnection.checkInternetConnection(PrepaidMobile.this))
                {
                    if (rechargeItems.getService_id().equals("2"))
                    {
                        if (ed_number.getText().toString().equals(""))
                        {
                            tv_number_error.setText("Please enter cutomer id");
                            tv_number_error.setVisibility(View.VISIBLE);
                        }
                        else
                        {
                            tv_number_error.setVisibility(View.GONE);
                        }
                    }
                    else {
                        if (circle_id.equals("")) {
                            tv_circle_error.setVisibility(View.VISIBLE);
                            tv_circle_error.setText("Please select circle");
                        } else {
                            tv_optional1_error.setVisibility(View.GONE);
                            tv_circle_error.setVisibility(View.GONE);

                            Intent intent1 = new Intent(PrepaidMobile.this, BrowsePlan.class);
                            intent1.putExtra("operator", provider_id);
                            intent1.putExtra("circle", circle_id);
                            startActivity(intent1);

                        }
                    }
                }
                else{
                    Toast.makeText(PrepaidMobile.this, "No internet connection", Toast.LENGTH_SHORT).show();
                }
            }
        });

        if (getSupportActionBar()!=null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(rechargeItems.getService_name());
        }

        ll_number=findViewById(R.id.ll_number);
        ll_optional1=findViewById(R.id.ll_optional1);
        ll_optional2=findViewById(R.id.ll_optional2);
        ll_optional3=findViewById(R.id.ll_optional3);


        ed_number=findViewById(R.id.ed_number);
        ed_number.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (DetectConnection.checkInternetConnection(PrepaidMobile.this))
                {
                    if (editable.toString().length()==10)
                    {
                        mGetDetails(ed_number.getText().toString());
                    }
                }
                else
                {
                    Toast.makeText(PrepaidMobile.this, "No internet connection", Toast.LENGTH_SHORT).show();
                }
            }
        });

        ed_other1=findViewById(R.id.ed_other1);
        ed_other2=findViewById(R.id.ed_other2);
        ed_other3=findViewById(R.id.ed_other3);

        tv_number_error=findViewById(R.id.tv_number_error);
        tv_operator_error=findViewById(R.id.tv_operator_error);
        tv_circle_error=findViewById(R.id.tv_circle_error);
        tv_optional1_error=findViewById(R.id.tv_optional1_error);
        tv_optional2_error=findViewById(R.id.tv_optional2_error);
        tv_optional3_error=findViewById(R.id.tv_optional3_error);
        tv_amount_error=findViewById(R.id.tv_amount_error);

        ll_bill_detail=findViewById(R.id.ll_bill_detail);


        final int recharge_id;
        recharge_id = Integer.parseInt(rechargeItems.getService_id());

        bt_proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {

                    tv_location.setVisibility(View.GONE);

                    if (DetectConnection.checkInternetConnection(PrepaidMobile.this)) {
                        if ((rechargeItems.getService_id().equals("1") || rechargeItems.getService_id().equals("2") || rechargeItems.getService_id().equals("3")) && ed_number.getText().toString().equals("")) {
                            if (rechargeItems.getService_id().equalsIgnoreCase("2")) {
                                tv_number_error.setVisibility(View.VISIBLE);
                                tv_number_error.setText("Please enter Custome Id");
                            } else {
                                tv_number_error.setVisibility(View.VISIBLE);
                                tv_number_error.setText("Please enter number");
                            }
                        } else if (rechargeItems.getService_id().equals("1") && ed_number.getText().toString().length() < 10) {
                            tv_number_error.setVisibility(View.VISIBLE);
                            tv_number_error.setText("Please enter a valid mobile number");
                        } else if ((rl_payment_mode.getVisibility() == View.VISIBLE) && ((recharge_id > 3) && selected_mode.equals(""))) {
                            tv_payment_mode_error.setText("Please select payment mode");
                            tv_payment_mode_error.setVisibility(View.VISIBLE);
                        } else if (provider_id.equals("")) {
                            tv_operator_error.setText("Please select operator");
                            tv_operator_error.setVisibility(View.VISIBLE);
                        } else if (!optional1.equals("") && ed_other1.getText().toString().equals("")) {
                            tv_optional1_error.setText("Please enter " + optional1);
                            tv_optional1_error.setVisibility(View.VISIBLE);
                        } else if (!optional2.equals("") && ed_other2.getText().toString().equals("")) {
                            tv_optional2_error.setText("Please enter " + optional2);
                            tv_optional2_error.setVisibility(View.VISIBLE);
                        } else if (!optional3.equals("") && ed_other3.getText().toString().equals("")) {
                            tv_optional3_error.setText("Please enter " + optional3);
                            tv_optional3_error.setVisibility(View.VISIBLE);
                        } else if (ed_amount.getText().toString().equals("")) {
                            tv_amount_error.setVisibility(View.VISIBLE);
                            tv_amount_error.setText("Please enter amount");
                        } else {
                            tv_number_error.setVisibility(View.GONE);
                            tv_operator_error.setVisibility(View.GONE);
                            tv_circle_error.setVisibility(View.GONE);
                            tv_optional1_error.setVisibility(View.GONE);
                            tv_optional2_error.setVisibility(View.GONE);
                            tv_optional3_error.setVisibility(View.GONE);
                            tv_amount_error.setVisibility(View.GONE);

                            mShowConfirmDialog(ed_number.getText().toString());

//                        mProceedPayment();
                        }
                    } else {
                        Toast.makeText(PrepaidMobile.this, "No internet connection", Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    tv_location.setVisibility(View.VISIBLE);
                    tv_location.setText("Please Enable Location");
                    tv_location.setTextColor(getResources().getColor(R.color.red));
                    Toast.makeText(PrepaidMobile.this, "Please enable location", Toast.LENGTH_SHORT).show();
                }
            }
        });

        tv_validate=findViewById(R.id.tv_validate);
        if (rechargeItems.getService_id().equals("1")||rechargeItems.getService_id().equals("2")||rechargeItems.getService_id().equals("3"))
        {
            ll_number.setVisibility(View.VISIBLE);
        }
        else
        {
            ll_number.setVisibility(View.GONE);
        }

        rl_operator=findViewById(R.id.rl_operator);
        rl_operator.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (DetectConnection.checkInternetConnection(PrepaidMobile.this))
                {
//                    mGetOperators();

//                    getSupportActionBar().setTitle(rechargeItems.getName()+" Provider");

                    Intent intent1=new Intent(PrepaidMobile.this,Operator.class);
                    Bundle bundle=new Bundle();
                    bundle.putString("type",rechargeItems.getService_id());
                    bundle.putString("activity",rechargeItems.getService_name());
                    bundle.putString("from","activity");
                    intent1.putExtras(bundle);
                    startActivity(intent1);

                }
                else
                {
                    Toast.makeText(PrepaidMobile.this, "No internet connection", Toast.LENGTH_SHORT).show();
                }
            }
        });

        tv_provider=findViewById(R.id.tv_provider);

        rl_circle=findViewById(R.id.rl_circle);
        tv_circle=findViewById(R.id.tv_circle);



//        to get location
        boolean r = myLocation.getLocation(getApplicationContext(),
                location);
        if (r) {
            Log.e("location", "found");
        } else {
            Log.e("location", "Not found");
        }

    }

    public static void mGetOperatorDetail(String provider_name,String id)
    {
//        getSupportActionBar().setTitle(rechargeItems.getName());
        tv_provider.setText(provider_name);
        if (DetectConnection.checkInternetConnection(activity))
        {
            int recharge_id;
            provider_id=id;
            if (!id.equals("")) {
                recharge_id = Integer.parseInt(service_id);
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId()==android.R.id.home)
        {
            if (getSupportFragmentManager().getBackStackEntryCount()!=0)
            {
                getSupportFragmentManager().popBackStackImmediate();
            }
            else {
                finish();
            }
        }
        return super.onOptionsItemSelected(item);
    }


    @SuppressLint("StaticFieldLeak")
    protected void mProceedPayment(String transaction_pin)
    {
        Uri.Builder builder=new Uri.Builder();
        builder.appendQueryParameter("api_token",SharePrefManager.getInstance(PrepaidMobile.this).mGetApiToken());

        if (rechargeItems.getService_id().equals("1")||rechargeItems.getService_id().equals("2")||rechargeItems.getService_id().equals("3")) {
            builder.appendQueryParameter("mobile_number", ed_number.getText().toString());
            number=ed_number.getText().toString();
        }
        else
        {
            builder.appendQueryParameter("mobile_number",ed_other1.getText().toString());
            number=ed_other1.getText().toString();
        }

        amount=ed_amount.getText().toString();
        builder.appendQueryParameter("amount",amount);
        builder.appendQueryParameter("provider_id",provider_id);
        builder.appendQueryParameter("optional1",ed_other1.getText().toString());
        builder.appendQueryParameter("optional2",ed_other2.getText().toString());
        builder.appendQueryParameter("optional3",ed_other3.getText().toString());
        builder.appendQueryParameter("payment_mode",selected_mode);
        builder.appendQueryParameter("transaction_pin",transaction_pin);
        builder.appendQueryParameter("latitude",lat+"");
        builder.appendQueryParameter("longitude",log+"");
        builder.appendQueryParameter("dupplicate_transaction",System.currentTimeMillis()+"");

        if (!optional_para4.equals(""))
        {
            builder.appendQueryParameter(optional_para4,optional_para4_value);
        }

        String sending_url= BaseURL.BASEURL_B2C+"api/application/v1/recharge-now";

        new CallResAPIPOSTMethod(PrepaidMobile.this,builder,sending_url,true,"POST")
        {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                dialog=new ProgressDialog(PrepaidMobile.this);
                dialog.setMessage("Please wait....");
                dialog.setCancelable(false);
                dialog.show();
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                dialog.dismiss();
                Log.e("response","data recharge "+s);

                try {
                    String status="",message="",payid="";
                    JSONObject jsonObject=new JSONObject(s);
                    if (jsonObject.has("status"))
                    {
                        status=jsonObject.getString("status");
                    }
                    if (jsonObject.has("message"))
                    {
                        message=jsonObject.getString("message");
                    }

                    if (jsonObject.has("payid"))
                    {
                        payid=jsonObject.getString("payid");
                    }

                    if (jsonObject.has("transaction_details"))
                    {
                        JSONObject transaction_details=jsonObject.getJSONObject("transaction_details");

                        if (transaction_details.has("status"))
                        {
                            status=transaction_details.getString("status");
                        }

                        if (transaction_details.has("operator_ref"))
                        {
                            message=transaction_details.getString("operator_ref");
                        }

                        if (transaction_details.has("operator_ref"))
                        {
                            payid=transaction_details.getString("operator_ref");
                        }
                    }


                    if (jsonObject.has("errors"))
                    {
                        JSONObject errors=jsonObject.getJSONObject("errors");
                        if (errors.has("mobile_number"))
                        {
                            String err=errors.getString("mobile_number").replaceAll("\\[","").replaceAll("]","").replaceAll("\"","");
                            if (rechargeItems.getService_id().equals("1")||rechargeItems.getService_id().equals("2")||rechargeItems.getService_id().equals("3"))
                            {

                                tv_number_error.setText(err);
                                tv_number_error.setVisibility(View.VISIBLE);
                            }
                            else
                            {
                                tv_optional1_error.setText(err);
                                tv_optional1_error.setVisibility(View.VISIBLE);

                            }
                        }

                        if (errors.has("amount"))
                        {
                            String err=errors.getString("amount").replaceAll("\\[","").replaceAll("]","").replaceAll("\"","");
                            tv_amount_error.setVisibility(View.VISIBLE);
                            tv_amount_error.setText(err);
                        }

                        if (errors.has("optional2"))
                        {
                            String err=errors.getString("optional2").replaceAll("\\[","").replaceAll("]","").replaceAll("\"","");
                            tv_optional2_error.setVisibility(View.VISIBLE);
                            tv_optional2_error.setText(err);
                        }

                        if (errors.has("optional3"))
                        {
                            String err=errors.getString("optional3").replaceAll("\\[","").replaceAll("]","").replaceAll("\"","");
                            tv_optional3_error.setVisibility(View.VISIBLE);
                            tv_optional3_error.setText(err);
                        }

                        if (errors.has("provider_id"))
                        {
                            String err=errors.getString("provider_id").replaceAll("\\[","").replaceAll("]","").replaceAll("\"","");
                            Toast.makeText(PrepaidMobile.this, err, Toast.LENGTH_SHORT).show();
                        }

                        if (errors.has("optional1"))
                        {
                            String err=errors.getString("optional1").replaceAll("\\[","").replaceAll("]","").replaceAll("\"","");
                            tv_optional1_error.setVisibility(View.VISIBLE);
                            tv_optional1_error.setText(err);
                        }

                        if (errors.has("transaction_pin"))
                        {
                            String err=errors.getString("transaction_pin").replaceAll("\\[","").replaceAll("]","").replaceAll("\"","");
                            Toast.makeText(getApplicationContext(), err, Toast.LENGTH_LONG).show();

                        }

                    }
                    else {

                        if (alertDialog!=null)
                        {
                            if (alertDialog.isShowing())
                            {
                                alertDialog.dismiss();
                            }
                        }

                        Intent intent = new Intent(PrepaidMobile.this, Receipt.class);
                        intent.putExtra("status", status);
                        intent.putExtra("message", message);
                        intent.putExtra("number", number);
                        intent.putExtra("amount", amount);
                        intent.putExtra("payid", payid);
                        intent.putExtra("type", "recharge");
                        intent.putExtra("provider", provider_name);
                        startActivity(intent);

                        if (Operator.mOperatorActivity!=null) {
                            Operator.mOperatorActivity.finish();
                        }

                        finish();
                    }
                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }
            }
        }.execute();
    }

    public void mSHowOperaters(View v,String operator,String type) {

        LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View popupView = layoutInflater.inflate(R.layout.bottom_sheet_layout, null);

          popupWindow = new PopupWindow(
                popupView,
                ViewGroup.LayoutParams.MATCH_PARENT,
                1000);

        popupWindow.setOutsideTouchable(true);

        EditText edittext_search=popupView.findViewById(R.id.edittext_search);
        edittext_search.setVisibility(View.VISIBLE);

        RelativeLayout rl_title_bar=popupView.findViewById(R.id.rl_title_bar);
        rl_title_bar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });

        edittext_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                if (beneficiaryitems!=null) {
                    List<Operators_Items> temp = new ArrayList();
                    for (Operators_Items d : beneficiaryitems) {
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

        recyclerview_operator = (RecyclerView) popupView.findViewById(R.id.recyclerview_operator);
        recyclerview_operator.setHasFixedSize(true);
        recyclerview_operator.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        beneficiaryitems=new ArrayList<>();

        operatorsCardAdapter=new OperatorsCardAdapter(PrepaidMobile.this,beneficiaryitems);
        recyclerview_operator.setAdapter(operatorsCardAdapter);

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
            else if (!SharePrefManager.getInstance(PrepaidMobile.this).mGetOperators().equals(""))
            {
                JSONObject jsonObject1=new JSONObject(SharePrefManager.getInstance(PrepaidMobile.this).mGetOperators());
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
                Toast.makeText(getApplicationContext(), "Unable to fetch operator list please check your internet connection and try again...", Toast.LENGTH_LONG).show();
            }

        }

        catch (JSONException e)
        {
            e.printStackTrace();
        }

        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {

            }
        });

        popupWindow.showAsDropDown(v);
    }

    @SuppressLint("StaticFieldLeak")
    protected void mGetOperators()
    {
        String sending= BaseURL.BASEURL_B2C+"api/application/v1/get-provider";
        Uri.Builder builder=new Uri.Builder();
        builder.appendQueryParameter("api_token",SharePrefManager.getInstance(PrepaidMobile.this).mGetApiToken());

        new CallResAPIPOSTMethod(PrepaidMobile.this,builder,sending,true,"POST")
        {
            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                dialog.dismiss();
                Log.e("response","data "+s);

                SharePrefManager.getInstance(PrepaidMobile.this).mSaveOperators(s);
                mSHowOperaters(rl_operator,SharePrefManager.getInstance(PrepaidMobile.this).mGetOperators(),rechargeItems.getService_id());

            }

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                dialog=new ProgressDialog(PrepaidMobile.this);
                dialog.setMessage("Please wait...");
                dialog.show();
                dialog.setCancelable(false);
            }
        }.execute();
    }

    public void mGetCircleDetail(ListDataItems items)
    {
        tv_circle.setText(items.getName());
        circle_id=items.getId();
    }


    public boolean mCheckReadContact() {
        return ContextCompat.checkSelfPermission(getApplicationContext(), "android.permission.READ_CONTACTS") == 0;
    }

    public void mRequestReadContact() {
        if (!ActivityCompat.shouldShowRequestPermissionRationale(this, "android.permission.READ_CONTACTS")) {
            ActivityCompat.requestPermissions(this, new String[]{"android.permission.READ_CONTACTS"}, 1);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case (10) :
                if (resultCode == Activity.RESULT_OK) {

                    Cursor cursor = null;
                    try {
                        String phoneNo = null ;
                        String name = null;
                        Uri uri = data.getData();
                        cursor = getContentResolver().query(uri, null, null, null, null);
                        cursor.moveToFirst();
                        int  phoneIndex =cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
                        phoneNo = cursor.getString(phoneIndex);
                        phoneNo=phoneNo.replaceAll(" ","");

                        if (phoneNo.startsWith("+91"))
                        {
                            ed_number.setText(phoneNo.substring(3,13));
                        }

                        else {
                            ed_number.setText(phoneNo);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                break;
            case (12):
                if (resultCode == Activity.RESULT_OK) {
                    Bundle res = data.getExtras();
                    String result = res.getString("qr_code");
                    ed_number.setText(result);

                }else if(resultCode== Activity.RESULT_CANCELED){
                    Toast.makeText(getApplicationContext(),"Unable to read QR Code.Please Try Again.", Toast.LENGTH_LONG).show();
                }
                break;
        }

    }

    protected void mShowConfirmDialog(String number)
    {

        LayoutInflater inflater2 =(LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v2=inflater2.inflate(R.layout.custome_dialog_confirm_recharge,null);
        TextView textview_servicename=v2.findViewById(R.id.textview_servicename);
        TextView textview_oper=v2.findViewById(R.id.textview_operator);
        TextView textview_mobnumber=v2.findViewById(R.id.textview_mobnumber);
        TextView textview_amount=v2.findViewById(R.id.textview_amount);
        Button button_submit=(Button) v2.findViewById(R.id.button_confirm);
        Button button_cancel=(Button) v2.findViewById(R.id.button_cancel);

        EditText tv_transaction_pin= v2.findViewById(R.id.tv_transaction_pin);
        if (SharePrefManager.getInstance(PrepaidMobile.this).mGetSingleData("transaction_pin").equals("1"))
        {
            tv_transaction_pin.setVisibility(View.VISIBLE);
            tv_transaction_pin.setHint("Enter Transaction PIN");
        }

        textview_servicename.setText(rechargeItems.getService_name());
        textview_oper.setText(provider_name);
        textview_mobnumber.setText(number);
        textview_amount.setText("Rs "+ed_amount.getText().toString());
        final AlertDialog.Builder builder2=new AlertDialog.Builder(PrepaidMobile.this);
        builder2.setCancelable(false);
        builder2.setView(v2);
        alertDialog=builder2.create();

        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        button_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (DetectConnection.checkInternetConnection(PrepaidMobile.this)){

                    if (tv_transaction_pin.getVisibility()==View.VISIBLE&&tv_transaction_pin.getText().toString().equals(""))
                    {
                        Toast.makeText(PrepaidMobile.this, "Please enter transaction PIN", Toast.LENGTH_SHORT).show();
                    }
                    else {

                        alertDialog.dismiss();
                        mProceedPayment(tv_transaction_pin.getText().toString());

                    }

                }
                else
                {
                    Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
                }
            }
        });

        button_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });


        alertDialog.show();
    }


    @SuppressLint("StaticFieldLeak")
    protected void mGetDetails(String number) {
        String sending_url = BaseURL.BASEURL_B2C+"api/plans/v1/prepaid-auto-find?api_token=" + SharePrefManager.getInstance(this).mGetApiToken() + "&mobile_number=" + number;
        new CallResAPIGetMethod(this, sending_url) {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
               dialog = new ProgressDialog(PrepaidMobile.this);
               dialog.setMessage("Please wait... we are getting provider detail");
               dialog.show();
               dialog.setCancelable(false);
            }

            public void onPostExecute(String s) {
                JSONException e;
                super.onPostExecute( s);
                dialog.dismiss();
                Log.e("response", "detail " + s);
                String id = "";
                String c_id = "";
                String circle_name = "";
                String p_name = "";
                try {
                    JSONObject jsonObjects = new JSONObject(s);
                    JSONObject jsonObject = jsonObjects.getJSONObject("details");
                    if (jsonObject.has("provider_id")) {
                        id = jsonObject.getString("provider_id");
                    }
                    if (jsonObject.has("provider_name")) {
                        p_name = jsonObject.getString("provider_name");
                    }
                    if (jsonObject.has("state_name")) {
                        circle_name = jsonObject.getString("state_name");
                    }
                    if (jsonObject.has("state_id")) {
                        c_id = jsonObject.getString("state_id");
                    }

                    tv_circle.setText(circle_name);
                    circle_id=c_id;

                    if (!PrepaidMobile.provider_id.equals(id)) {
                        PrepaidMobile.this.mShowOperatorChangeDialog(id, p_name, "", c_id, circle_name);
                    }
                } catch (JSONException e3) {
                    e = e3;
                }
            }
        }.execute();
    }

    protected void mShowOperatorChangeDialog(final String id, final String name, String image, final String c_id, final String c_name) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() { // from class: sp.api.RechargesServicesDetail.PrepaidMobile.24
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialog2, int which) {
                dialog2.dismiss();
            }
        });
        builder.setTitle("Change Provider Details");
        builder.setMessage("The Provider name of your entered number " + ed_number.getText().toString() + " is " + name + ", \nDo you want to change ?");
        StringBuilder sb = new StringBuilder();
        sb.append("id ");
        sb.append(id);
        String sb2 = sb.toString();
        Log.e(sb2, "image " + image);
        builder.setPositiveButton("Change", new DialogInterface.OnClickListener() { // from class: sp.api.RechargesServicesDetail.PrepaidMobile.25
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialog2, int which) {
                provider_id = id;
                tv_operator.setText(name);

                circle_id = c_id;
                tv_circle.setText(c_name);

                mSetIcon(id);

            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        alertDialog.show();
    }

    protected void mSetIcon(String id)
    {

        try {

            String operator=SharePrefManager.getInstance(PrepaidMobile.this).mGetOperators();
            JSONObject jsonObject=new JSONObject(operator);

            JSONArray jsonArray = jsonObject.getJSONArray("providerList");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject data = jsonArray.getJSONObject(i);
                service_id = data.getString("service_id");
                String pro_id = data.getString("provider_id");


                if (pro_id.equals(id)) {
                    if (data.has("provider_icon")) {
                       String provider_image = data.getString("provider_icon");

                        Glide.with(PrepaidMobile.this).load(provider_image).error(Glide.with(iv_ope_icon).load(R.drawable.photo)).into(iv_ope_icon);

                    }

                }

            }
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }

    }

   MyLocation.LocationResult location= new MyLocation.LocationResult() {

        @Override
        public void gotLocation(Location location) {
            double Longitude = location.getLongitude();
            double Latitude = location.getLatitude();

//            Toast.makeText(getApplicationContext(), "Got Location",
//                    Toast.LENGTH_LONG).show();

            tv_location.setText("Got Location");
            tv_location.setTextColor(getResources().getColor(R.color.green));

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    tv_location.setTextColor(getResources().getColor(R.color.red));
                    tv_location.setVisibility(View.GONE);
                }
            },2000);

            lat=Latitude;
            log=Longitude;


//            tv_lat.setText("Latitude : "+lat);
//            tv_long.setText("Longitude : "+log);
        }
    };


    protected void mShowEnabledLocationDialog()
    {
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(new LocationRequest().setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY));
        builder.setAlwaysShow(true);
        mLocationSettingsRequest = builder.build();

        mSettingsClient = LocationServices.getSettingsClient(PrepaidMobile.this);

        mSettingsClient
                .checkLocationSettings(mLocationSettingsRequest)
                .addOnSuccessListener(new OnSuccessListener<LocationSettingsResponse>() {
                    @Override
                    public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                        //Success Perform Task Here
                        tv_location.setVisibility(View.GONE);
                    }
                })

                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        int statusCode = ((ApiException) e).getStatusCode();
                        switch (statusCode) {
                            case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                                try {
                                    ResolvableApiException rae = (ResolvableApiException) e;
                                    rae.startResolutionForResult(PrepaidMobile.this, REQUEST_CHECK_SETTINGS);
                                } catch (IntentSender.SendIntentException sie) {
                                    Log.e("GPS","Unable to execute request.");
                                }
                                break;
                            case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                                Log.e("GPS","Location settings are inadequate, and cannot be fixed here. Fix in Settings.");
                        }
                    }
                })
                .addOnCanceledListener(new OnCanceledListener() {
                    @Override
                    public void onCanceled() {
                        Log.e("GPS","checkLocationSettings -> onCanceled");
                    }
                });
    }

}