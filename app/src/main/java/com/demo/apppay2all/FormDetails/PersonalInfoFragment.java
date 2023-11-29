package com.demo.apppay2all.FormDetails;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.demo.apppay2all.BaseURL.BaseURL;
import com.demo.apppay2all.CallResAPIPOSTMethod;
import com.demo.apppay2all.DetectConnection;
import com.demo.apppay2all.R;
import com.demo.apppay2all.SharePrefManager;

import org.json.JSONException;
import org.json.JSONObject;

public class PersonalInfoFragment extends Fragment {

    EditText ed_first_name,ed_last_name,ed_email,ed_mobile;
    TextView textview_verify;

    ImageButton ib_prev,ib_next;
    String otp="";

    AlertDialog alertDialog;
    ProgressDialog dialog;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_personal_info,container,false);

        ed_first_name=view.findViewById(R.id.ed_first_name);
        ed_last_name=view.findViewById(R.id.ed_last_name);
        ed_email=view.findViewById(R.id.ed_email);
        ed_mobile=view.findViewById(R.id.ed_mobile);

        ed_first_name.setText(SharePrefManager.getInstance(getActivity()).mGetFirstName());
        ed_last_name.setText(SharePrefManager.getInstance(getActivity()).mGetLastName());
        ed_email.setText(SharePrefManager.getInstance(getActivity()).mGetEmail());
        ed_mobile.setText(SharePrefManager.getInstance(getActivity()).mGetUsername());

        ib_prev=view.findViewById(R.id.ib_prev);
        ib_prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((FormsKyc)getActivity()).mChangeFragment(new KycFragment(),"2");
            }
        });

        ib_next=view.findViewById(R.id.ib_next);
        ib_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (textview_verify.getVisibility()==View.VISIBLE)
                {
                    Toast.makeText(getContext(), "Please verify mobile number to start service", Toast.LENGTH_SHORT).show();
                }
                else {
                    ((FormsKyc) getActivity()).mChangeFragment(new AddressFragment(), "4");
                }
            }
        });

        textview_verify=view.findViewById(R.id.textview_verify);
        textview_verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (DetectConnection.checkInternetConnection(getActivity()))
                {
                    String sending_url= BaseURL.BASEURL_B2C+"api/application/v1/verify-mobile";
                    mVerify(sending_url,"verify");
                }
                else
                {
                    Toast.makeText(getContext(), "No internet connection", Toast.LENGTH_SHORT).show();
                }
            }
        });

        if (!SharePrefManager.getInstance(getActivity()).mVerifyMobile())
        {
            textview_verify.setVisibility(View.VISIBLE);
        }
        return view;
    }

    @SuppressLint("StaticFieldLeak")
    protected void mVerify(String sending_url, final String type)
    {
        Uri.Builder builder=new Uri.Builder();
        builder.appendQueryParameter("api_token",SharePrefManager.getInstance(getActivity()).mGetApiToken());
        if (type.equalsIgnoreCase("submit"))
        {
            builder.appendQueryParameter("mobile_number",SharePrefManager.getInstance(getActivity()).mGetUsername());
            builder.appendQueryParameter("otp",otp);
        }
        new CallResAPIPOSTMethod(getActivity(),builder,sending_url,true,"POST")
        {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                dialog=new ProgressDialog(getActivity());
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
                try {
                    JSONObject jsonObject=new JSONObject(s);
                    if (jsonObject.has("status"))
                    {
                        status=jsonObject.getString("status");
                    }
                    if (jsonObject.has("message"))
                    {
                        message=jsonObject.getString("message");
                    }
                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }
                if (type.equalsIgnoreCase("verify"))
                {
                    if (status.equalsIgnoreCase("success"))
                    {
                        mShowOTPDialog();
                    }
                    else if (!message.equals(""))
                    {
                        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        Toast.makeText(getContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
                    }
                }
                else
                {
                    if (status.equalsIgnoreCase("success"))
                    {
                        if (alertDialog!=null)
                        {
                            if (alertDialog.isShowing()) {
                                alertDialog.dismiss();
                            }
                        }
                        textview_verify.setVisibility(View.GONE);
                        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
                    }
                    else if (!status.equals("success"))
                    {
                        if (!message.equals("")) {
                            Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Toast.makeText(getContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
        }.execute();
    }

    protected void mShowOTPDialog()
    {
        LayoutInflater inflater2 = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v2 = inflater2.inflate(R.layout.custome_alertdialog_for_otp, null);

        ImageView ib_close = v2.findViewById(R.id.ib_close);
        ImageButton ib_submit = v2.findViewById(R.id.ib_submit);

        final EditText ed_otp= v2.findViewById(R.id.ed_otp);
        final TextView tv_otp_error=v2.findViewById(R.id.tv_otp_error);
        TextView tv_resend=v2.findViewById(R.id.tv_resend);

        final AlertDialog.Builder builder2 = new AlertDialog.Builder(getActivity());
        builder2.setCancelable(false);

        builder2.setView(v2);

        ib_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (DetectConnection.checkInternetConnection(getActivity()))
                {
                    if (ed_otp.getText().toString().equals(""))
                    {
                        tv_otp_error.setText("Please enter OTP");
                        tv_otp_error.setVisibility(View.VISIBLE);
                    }
                    else
                    {
                        tv_otp_error.setVisibility(View.GONE);
                        otp=ed_otp.getText().toString();

                        String sending_url= BaseURL.BASEURL_B2C+"api/application/v1/confirm-verify-mobile";
                        mVerify(sending_url,"submit");
                    }
                }
            }
        });

        tv_resend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (DetectConnection.checkInternetConnection(getActivity()))
                {
                    String sending_url= BaseURL.BASEURL_B2C+"api/application/v1/verify-mobile";
                    mVerify(sending_url,"verify");
                    alertDialog.dismiss();
                }
                else
                {
                    Toast.makeText(getContext(), "No internet connection", Toast.LENGTH_SHORT).show();
                }
            }
        });
        alertDialog = builder2.create();
        ib_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });
        alertDialog.show();
    }
}
