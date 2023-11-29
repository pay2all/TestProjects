package com.demo.apppay2all.MoneyTransfer1;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.demo.apppay2all.BaseURL.BaseURL;
import com.demo.apppay2all.CallResAPIPOSTMethod;
import com.demo.apppay2all.DetectConnection;
import com.demo.apppay2all.LocationDetails.MyLocation;
import com.demo.apppay2all.R;
import com.demo.apppay2all.RechargesServicesDetail.RechargeActivity;
import com.demo.apppay2all.Review_Activity;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Basant on 6/8/2017.
 */

public class Add_benificiary_FragmentPaymtm extends Fragment implements LocationListener {

    String sender_number,senderid,type="";
    Button button_ok;
    TextView textview_message;
    ImageView imageview_messase_image;



    TextView tv_location;

    LocationManager locationManager=null;
    private SettingsClient mSettingsClient;
    private LocationSettingsRequest mLocationSettingsRequest;
    private static final int REQUEST_CHECK_SETTINGS = 214;
    private static final int REQUEST_ENABLE_GPS = 516;
    MyLocation myLocation=new MyLocation();

    double lat=0.0,log=0.0;

    public static Add_benificiary_FragmentPaymtm newInstance(String s, String senderid,String type) {

        Add_benificiary_FragmentPaymtm result = new Add_benificiary_FragmentPaymtm();
        Bundle bundle = new Bundle();
        bundle.putString("sender_number", s);
        bundle.putString("senderid", senderid);
        bundle.putString("type", type);
        result.setArguments(bundle);
        return result;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = this.getArguments();
        senderid = bundle.getString("senderid");
        sender_number = bundle.getString("sender_number");
        type = bundle.getString("type");
    }

//    SearchableSpinner spinner_banknames;
    public static LinearLayout ll_bank_detail;
    AlertDialog alertDialog;

    String account_number,valid_name,beneficiary_number,ifsc;

    Button button_add_beneficiary;
    EditText editText_bankaccountnumber, editText_name, edittext_beneficiary_mobile;
    public static EditText edittext_ifscode;

    String string_bankname_selected_from_spinner;
    public static String bank_code;
    String myJSON,myJSON_verify;
    ProgressDialog dialog;
    Button button_verify_account;

    RelativeLayout rl_bank;
    public static TextView textview_bank_name;

    String username,balance,password;
    String Stringmessage="";
//    String Stingjson="{\"response_status_id\":-1,\"data\":{\"client_ref_id\":\"\",\"bank\":\"ICICI Bank\",\"amount\":\"0.00\",\"is_name_editable\":\"0\",\"fee\":\"0.00\",\"verification_failure_refund\":\"\",\"aadhar\":\"\",\"recipient_name\":\"CERES INFOTECH PVT\",\"is_Ifsc_required\":\"0\",\"account\":\"107005006912\",\"tid\":\"285188054\"},\"response_type_id\":61,\"message\":\"Success!  Account details found..\",\"status\":0}";
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.add_benificiary_fragment, container, false);


//        to get location
        boolean r = myLocation.getLocation(getActivity(), location);
        if (r) {
            Log.e("location", "found");
        } else {
            Log.e("location", "Not found");
        }


        tv_location=v.findViewById(R.id.tv_location);
        locationManager=(LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        tv_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
                }
                else{
                    mShowEnabledLocationDialog();
                }
            }
        });


        editText_bankaccountnumber = v.findViewById(R.id.edittext_bankaccountnumber);
        editText_name = v.findViewById(R.id.edittext_name);
        edittext_beneficiary_mobile = v.findViewById(R.id.edittext_beneficiary_mobile);
        edittext_beneficiary_mobile.setText(sender_number);
        edittext_ifscode = v.findViewById(R.id.edittext_ifscode);
        button_verify_account= v.findViewById(R.id.button_verify_account);

        SharedPreferences sharedPreferences=getActivity().getSharedPreferences("user", Context.MODE_PRIVATE);
        username=sharedPreferences.getString("username","");
        balance=sharedPreferences.getString("balance","");
        password=sharedPreferences.getString("password","");

        button_verify_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {


                    if (DetectConnection.checkInternetConnection(getContext())) {

                        if (textview_bank_name.getText().toString().toLowerCase().equals("bank name") || textview_bank_name.getText().toString().equalsIgnoreCase("Select Bank")) {
                            Toast.makeText(getContext(), "Select Bank name", Toast.LENGTH_SHORT).show();
                        } else if (editText_bankaccountnumber.getText().toString().equals("")) {
                            Toast.makeText(getContext(), "Enter bank account number..", Toast.LENGTH_SHORT).show();
                        } else {
                            String account = editText_bankaccountnumber.getText().toString();
                            String ifsc = edittext_ifscode.getText().toString();
                            beneficiary_number = edittext_beneficiary_mobile.getText().toString();

                            String sending_url = BaseURL.BASEURL_B2C + "api/dmt/v1/account-validate";
                            Uri.Builder builder = new Uri.Builder();
                            builder.appendQueryParameter("api_token", SharePrefManager.getInstance(getActivity()).mGetApiToken());
                            builder.appendQueryParameter("bank_id", bank_code);
                            builder.appendQueryParameter("ifsc_code", ifsc);
                            builder.appendQueryParameter("account_number", account);
                            builder.appendQueryParameter("mobile_number", beneficiary_number);


                            builder.appendQueryParameter("latitude",lat+"");
                            builder.appendQueryParameter("longitude",log+"");
                            builder.appendQueryParameter("dupplicate_transaction",System.currentTimeMillis()+"");

                            mGetData("verify", builder, sending_url);
//                        ShowVerify();
                        }
                    } else {
                        Toast.makeText(getContext(), "No Connection", Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    tv_location.setText("Please enable location");
                    tv_location.setVisibility(View.VISIBLE);
                    tv_location.setTextColor(getResources().getColor(R.color.red));
                }
            }
        });

        rl_bank=v.findViewById(R.id.rl_bank);
        rl_bank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BankListBottomSheet3DialogFragment bottomSheetDialogFragment = new BankListBottomSheet3DialogFragment();
                Bundle bundle=new Bundle();
                bundle.putString("type","1");
                bundle.putString("activity","money2");
                bottomSheetDialogFragment.setArguments(bundle);
                bottomSheetDialogFragment.show(getFragmentManager(), bottomSheetDialogFragment.getTag());
            }
        });
        textview_bank_name=v.findViewById(R.id.textview_bank_name);
        button_add_beneficiary = v.findViewById(R.id.button_add_benificiary);
        if (type.equals("verify"))
        {
            button_add_beneficiary.setText("Verify Now");
        }
        ll_bank_detail = v.findViewById(R.id.ll_bank_detail);
        button_add_beneficiary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    if (DetectConnection.checkInternetConnection(getActivity())) {
                        if (textview_bank_name.getText().toString().equals("Select Bank") || textview_bank_name.getText().toString().equalsIgnoreCase("bank name")) {
                            Toast.makeText(getContext(), "Select Bank name", Toast.LENGTH_SHORT).show();
                        } else if (editText_bankaccountnumber.getText().toString().equals("")) {
                            Toast.makeText(getContext(), "Enter bank account number..", Toast.LENGTH_SHORT).show();
                        } else if (editText_name.getText().toString().equals("")) {
                            Toast.makeText(getContext(), "Enter beneficiary name", Toast.LENGTH_SHORT).show();
                        } else if (edittext_beneficiary_mobile.getText().toString().equals("")) {
                            Toast.makeText(getContext(), "Enter beneficiary mobile number", Toast.LENGTH_SHORT).show();
                        } else if (edittext_beneficiary_mobile.getText().toString().length() < 10) {
                            Toast.makeText(getContext(), "Enter a valide beneficiary mobile number", Toast.LENGTH_SHORT).show();
                        } else {

                            if (type.equalsIgnoreCase("verify")) {
                                String account = editText_bankaccountnumber.getText().toString();
                                String ifsc = edittext_ifscode.getText().toString();
//                            AccountVerify(bank_code,sender_number,account,username,password,ifsc);
                                beneficiary_number = edittext_beneficiary_mobile.getText().toString();

                                String sending_url = BaseURL.BASEURL_B2C + "api/dmt/v1/account-validate";
                                Uri.Builder builder = new Uri.Builder();
                                builder.appendQueryParameter("api_token", SharePrefManager.getInstance(getActivity()).mGetApiToken());
                                builder.appendQueryParameter("bank_id", bank_code);
                                builder.appendQueryParameter("ifsc_code", ifsc);
                                builder.appendQueryParameter("account_number", account);
                                builder.appendQueryParameter("mobile_number", beneficiary_number);


                                builder.appendQueryParameter("latitude",lat+"");
                                builder.appendQueryParameter("longitude",log+"");
                                builder.appendQueryParameter("dupplicate_transaction",System.currentTimeMillis()+"");

                                mGetData("verify", builder, sending_url);
                            } else {
                                account_number = editText_bankaccountnumber.getText().toString();
                                String name = editText_name.getText().toString();

                                beneficiary_number = edittext_beneficiary_mobile.getText().toString();
                                ifsc = edittext_ifscode.getText().toString();
//                            Add_Beneficiary(beneficiary_number, valid_name, account_number, ifsc);

                                String sending_url = BaseURL.BASEURL_B2C + "api/dmt/v1/add-beneficiary";
                                Uri.Builder builder = new Uri.Builder();
                                builder.appendQueryParameter("api_token", SharePrefManager.getInstance(getActivity()).mGetApiToken());
                                builder.appendQueryParameter("bank_id", bank_code);
                                builder.appendQueryParameter("ifsc_code", ifsc);
                                builder.appendQueryParameter("account_number", account_number);
                                builder.appendQueryParameter("mobile_number", beneficiary_number);
                                builder.appendQueryParameter("beneficiary_name", name);


                                builder.appendQueryParameter("latitude",lat+"");
                                builder.appendQueryParameter("longitude",log+"");
                                builder.appendQueryParameter("dupplicate_transaction",System.currentTimeMillis()+"");

                                mGetData("add", builder, sending_url);
                            }
                        }
                    } else {
                        Toast.makeText(getContext(), "No Connection", Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    tv_location.setText("Please enable location");
                    tv_location.setVisibility(View.VISIBLE);
                    tv_location.setTextColor(getResources().getColor(R.color.red));
                }
            }
        });

        return v;
    }

    protected void Add_Beneficiary(final String sender_number, final String name, final String account_number, final String ifsc) {
        class Add_data extends AsyncTask<String, String, String> {
            HttpURLConnection httpURLConnection;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                dialog = new ProgressDialog(getActivity());
                dialog.setMessage("Please wait");
                dialog.setCancelable(false);
                dialog.show();
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                myJSON=s;
                Log.e("Add beneficiary respons",s);
                ShowData(s);
                dialog.dismiss();
            }

            @Override
            protected String doInBackground(String... params) {
                StringBuilder result = new StringBuilder();
                try {
                    URL url = new URL(BaseURL.BASEURL+"app/v2/add-beneficiary?mobile_number="+sender_number+"&bank_code="+bank_code+"&ifsc="+ifsc+"&beneficiary_name="+name+"&beneficiary_mobile="+beneficiary_number+"&beneficiary_account="+account_number);
                    httpURLConnection = (HttpURLConnection) url.openConnection();
                    InputStream in = new BufferedInputStream(httpURLConnection.getInputStream());

                    BufferedReader reader = new BufferedReader(new InputStreamReader(in));

                    String line;
                    while ((line = reader.readLine()) != null) {
                        result.append(line);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    httpURLConnection.disconnect();
                }


                return result.toString();
            }

        }
        Add_data add_data = new Add_data();
        add_data.execute();
    }

    protected void ShowData(final String json)
    {
        String status="";
        String message="";
        String beneficiaryid="";
        String otp="";
        try
        {
            JSONObject jsonObject=new JSONObject(json);
            status=jsonObject.getString("status");
            message=jsonObject.getString("message");

        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }

        if (status.equals("success"))
        {
//            String activity="add_beneficiary";
//            Intent intent=new Intent(getActivity(),Review_Activity.class);
//            Bundle bundle=new Bundle();
//            bundle.putString("activity",activity);
//            bundle.putString("status",status);
//            bundle.putString("message",message);
//            intent.putExtras(bundle);
//            startActivity(intent);
//            getActivity().finish();
            LayoutInflater inflater =(LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View v2=inflater.inflate(R.layout.custom_alertdalog_for_message,null);

            button_ok=(Button)v2.findViewById(R.id.button_ok);
            textview_message=(TextView)v2.findViewById(R.id.textview_message);
            textview_message.setText(message);

            final AlertDialog.Builder builder2=new AlertDialog.Builder(getActivity());
            builder2.setCancelable(false);

            builder2.setView(v2);
            final AlertDialog alert=builder2.create();

            button_ok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    alert.dismiss();
                    ((Sender_allready_RegisterPaytm)getActivity()).mShowSavedBeneficiary();
                }
            });

            alert.show();
        }

//        else if (status.equalsIgnoreCase("0"))
//        {
//            mShowOTP(sender_number,senderid,beneficiaryid);
//        }

        else if (!status.equalsIgnoreCase("success"))
        {
//            String activity="add_beneficiary";
//            Intent intent=new Intent(getActivity(),Review_Activity.class);
//            Bundle bundle=new Bundle();
//            bundle.putString("activity",activity);
//            bundle.putString("status",status);
//            bundle.putString("message",message);
//            intent.putExtras(bundle);
//            startActivity(intent);
//            getActivity().finish();


            LayoutInflater inflater =(LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View v2=inflater.inflate(R.layout.custom_alertdalog_for_message,null);

            button_ok=(Button)v2.findViewById(R.id.button_ok);
            textview_message=(TextView)v2.findViewById(R.id.textview_message);
            textview_message.setText(message);
            textview_message.setTextColor(Color.parseColor("#6c6c6c"));
            imageview_messase_image=(ImageView)v2.findViewById(R.id.imageview_messase_image);
            imageview_messase_image.setImageResource(R.drawable.error_icon);

            final AlertDialog.Builder builder2=new AlertDialog.Builder(getActivity());
            builder2.setCancelable(false);

            builder2.setView(v2);
            final AlertDialog alert=builder2.create();

            button_ok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    alert.dismiss();
                }
            });

            alert.show();

        }
        else
        {
            Toast.makeText(getContext(), "Something went wrong please try again", Toast.LENGTH_SHORT).show();
//            String activity="add_beneficiary";
//            Intent intent=new Intent(getActivity(),Review_Activity.class);
//            Bundle bundle=new Bundle();
//            bundle.putString("activity",activity);
//            intent.putExtras(bundle);
//            startActivity(intent);
//            getActivity().finish();
        }
    }


    protected void AccountVerify(final String bank_code, final String sender_number, final String account_number, final String username, final String password, final String ifsc)
    {
        class Verify extends AsyncTask<String, String, String>
        {
            HttpURLConnection httpURLConnection;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                dialog = new ProgressDialog(getActivity());
                dialog.setMessage("Please wait");
                dialog.setCancelable(false);
                dialog.show();
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                myJSON_verify=s;

                Log.e("MYJSON",myJSON_verify);
                dialog.dismiss();
                ShowVerify(s);
            }
            @Override
            protected String doInBackground(String... params) {
                StringBuilder result = new StringBuilder();
                try {
                    URL url = new URL(BaseURL.BASEURL+"app/v2/check-beneficiary-name?username="+username+"&password="+password+"&mobile_number="+sender_number+"&bank_id="+bank_code+"&ifsc="+ifsc+"&beneficiary_account="+account_number);
                    httpURLConnection = (HttpURLConnection) url.openConnection();
                    InputStream in = new BufferedInputStream(httpURLConnection.getInputStream());

                    BufferedReader reader = new BufferedReader(new InputStreamReader(in));

                    String line;
                    while ((line = reader.readLine()) != null) {
                        result.append(line);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    httpURLConnection.disconnect();
                }

                return result.toString();
            }
        }
        Verify verify=new Verify();
        verify.execute();

//        dialog = new ProgressDialog(getActivity());
//        dialog.setMessage("Please wait");
//        dialog.setCancelable(false);
//        dialog.show();
//
//        StringRequest stringRequest=new StringRequest(Request.Method.GET,
//                BaseURL.BASE_URL+"app/v1/check-beneficiary-name?username="+username+"&password="+password+"&mobile_number="+sender_number+"&bank_id="+bank_code+"&ifsc="+ifsc+"&beneficiary_account="+account_number,
//                new Response.Listener<String>() {
//            @Override
//            public void onResponse(String response) {
//                dialog.dismiss();
//                Log.e("res",response);
//                ShowVerify(response);
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                dialog.dismiss();
//                Log.e("err",error.getMessage());
//            }
//        });
//
//        RequestQueue requestQueue= Volley.newRequestQueue(getActivity());
//        requestQueue.add(stringRequest);
    }
    protected void ShowVerify(final String response)
    {

        String status="";
        String message="";
        String recipient_name="";

        try
        {
            JSONObject jsonObject=new JSONObject(response);
            status=jsonObject.getString("status");
            message=jsonObject.getString("message");
            if (status.equalsIgnoreCase("success")||status.equalsIgnoreCase("0")) {

//                JSONObject data=jsonObject.getJSONObject("data");
                recipient_name = jsonObject.getString("beneficiary_name");

                editText_name.setText(recipient_name);
            }
            else if (!status.equals("")&&!status.equalsIgnoreCase("success"))
            {
                Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
            }
            else{
                Toast.makeText(getContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
    }

    protected void mShowOTP(final String sender_number, final String senderid, final String beneficiaryid)
    {
        LayoutInflater inflater2 =(LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v2=inflater2.inflate(R.layout.custome_alert_for_otp,null);

        Button button_submit= v2.findViewById(R.id.button_submit);
        Button button_cancel= v2.findViewById(R.id.button_cancel);
        final EditText edittext_otp= v2.findViewById(R.id.edittext_otp);
        TextView textview_resend_otp= v2.findViewById(R.id.textview_resend_otp);

        final AlertDialog.Builder builder2=new AlertDialog.Builder(getActivity());
        builder2.setCancelable(false);

        builder2.setView(v2);
        alertDialog=builder2.create();

        textview_resend_otp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
                Add_Beneficiary(beneficiary_number, valid_name, account_number, ifsc);
            }
        });

        button_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (DetectConnection.checkInternetConnection(getActivity()))
                {
                    if (edittext_otp.getText().toString().equals(""))
                    {
                        Toast.makeText(getContext(), "Please enter OTP", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        String otp=edittext_otp.getText().toString();
                        mSubmitOTP(sender_number,beneficiaryid,senderid,otp);
                    }
                }
                else
                {
                    Toast.makeText(getContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
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

    protected void mSubmitOTP(final String sender_number, final String beneficiry_code, final String senderid, final String otp)
    {
        class Verify extends AsyncTask<String, String, String>
        {
            HttpURLConnection httpURLConnection;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                dialog = new ProgressDialog(getActivity());
                dialog.setMessage("Please wait");
                dialog.setCancelable(false);
                dialog.show();
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                dialog.dismiss();

                Log.e("submit otp respose",s);

                String status="";
                String message="";
                try
                {
                    JSONObject jsonObject=new JSONObject(s);
                    status=jsonObject.getString("status");
                    message=jsonObject.getString("message");

                    if (jsonObject.has("status")) {
                        if (status.equals("0")) {

                            if (alertDialog.isShowing())
                            {
                                alertDialog.dismiss();
                            }
                            String activity = "add_beneficiary";
                            Intent intent = new Intent(getActivity(), Review_Activity.class);
                            Bundle bundle = new Bundle();
                            bundle.putString("activity", activity);
                            intent.putExtras(bundle);
                            startActivity(intent);
                            getActivity().finish();
                        } else if (!status.equals("1"))
                        {
                            Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
                        }
                    }
                    else
                    {
                        Toast.makeText(getContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
                    }
                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }
            }
            @Override
            protected String doInBackground(String... params) {
                StringBuilder result = new StringBuilder();
                try {
                    URL url = new URL(BaseURL.BASEURL+"app/v2/bene-confirm?mobile_number="+sender_number+"&BeneficiaryCode="+beneficiry_code+"&senderid="+senderid+"&otp="+otp);
                    httpURLConnection = (HttpURLConnection) url.openConnection();
                    InputStream in = new BufferedInputStream(httpURLConnection.getInputStream());

                    BufferedReader reader = new BufferedReader(new InputStreamReader(in));

                    String line;
                    while ((line = reader.readLine()) != null) {
                        result.append(line);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    httpURLConnection.disconnect();
                }

                return result.toString();
            }
        }
        Verify verify=new Verify();
        verify.execute();
    }

    @SuppressLint("StaticFieldLeak")
    protected void mGetData(final String type, Uri.Builder builder, String sending_url)
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
                Log.e("add bene","response "+s);

                if (type.equalsIgnoreCase("add")) {
                    ShowData(s);
                }
                else if (type.equalsIgnoreCase("verify"))
                {
                   ShowVerify(s);
                }

            }
        }.execute();
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

        mSettingsClient = LocationServices.getSettingsClient(getActivity());

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
                                    rae.startResolutionForResult(getActivity(), REQUEST_CHECK_SETTINGS);
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

    @Override
    public void onLocationChanged(@NonNull Location location) {
        lat=location.getLatitude();
        log=location.getLongitude();
    }
}