package com.demo.apppay2all.MoneyTransfer1;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
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

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class IfscVerification extends Fragment {

    ProgressDialog dialog;

    LinearLayout ll_bank_detail;
    EditText edittext_ifsc;
    TextView textview_bank,textview_branch,textView_ifsc,textview_address;

    Button button_submit;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View  view=inflater.inflate(R.layout.fragment_ifsc_verification,container,false);

        edittext_ifsc=view.findViewById(R.id.edittext_ifsc);
        textview_bank=view.findViewById(R.id.textview_bank);
        textview_branch=view.findViewById(R.id.textview_branch);
        textView_ifsc=view.findViewById(R.id.textView_ifsc);
        textview_address=view.findViewById(R.id.textview_address);
        button_submit=view.findViewById(R.id.button_submit);
        ll_bank_detail=view.findViewById(R.id.ll_bank_detail);

        button_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (DetectConnection.checkInternetConnection(getActivity()))
                {
                    if (edittext_ifsc.getText().toString().equals(""))
                    {
                        Toast.makeText(getContext(), "Please enter ifsc", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        String ifsc=edittext_ifsc.getText().toString();
                        String sending_url=BaseURL.BASEURL_B2C+"api/dmt/v1/ifsc-code-finder";
                        Uri.Builder builder=new Uri.Builder();
                        builder.appendQueryParameter("api_token", SharePrefManager.getInstance(getContext()).mGetApiToken());
                        builder.appendQueryParameter("ifsc_code",ifsc);

                        mCheckSender(builder,sending_url);
                    }
                }
                else
                {
                    Toast.makeText(getActivity(), "No internet conecction", Toast.LENGTH_SHORT).show();
                }
            }
        });
        return view;
    }

    protected void mGetBankDetail(final String ifsc) {
        class BankDetail extends AsyncTask<String, String, String> {
            HttpURLConnection urlConnection;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                dialog = new ProgressDialog(getActivity());
                dialog.setMessage("Please wait...");
                dialog.show();
                dialog.setCancelable(true);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                Log.e("ifsc response",s);
                dialog.dismiss();

                if (!s.equals(""))
                {
                    String ADDRESS="",BRANCH="",BANK="",IFSC="";
                    try{
                        JSONObject jsonObject=new JSONObject(s);


                        if (jsonObject.has("ADDRESS"))
                        {
                            ADDRESS=jsonObject.getString("ADDRESS");
                        }
                        if (jsonObject.has("IFSC"))
                        {
                            IFSC=jsonObject.getString("IFSC");
                        }

                        if (jsonObject.has("BRANCH"))
                        {
                            BRANCH=jsonObject.getString("BRANCH");
                        }

                        if (jsonObject.has("BANK"))
                        {
                            BANK=jsonObject.getString("BANK");
                        }


                        if (!IFSC.equals(""))
                        {
                            ll_bank_detail.setVisibility(View.VISIBLE);
                        }
                        else
                        {
                            ll_bank_detail.setVisibility(View.GONE);
                        }
                        textview_bank.setText(BANK);
                        textview_branch.setText(BRANCH);
                        textView_ifsc.setText(IFSC);
                        textview_address.setText(ADDRESS);

                    }
                    catch (JSONException e)
                    {
                        e.printStackTrace();
                    }
                }
                else
                {
                    ll_bank_detail.setVisibility(View.GONE);
                    Toast.makeText(getContext(), "Server not responding...", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            protected String doInBackground(String... params) {
                StringBuilder result = new StringBuilder();

                try {
                    URL url = new URL(BaseURL.BASEURL+"api/v1/ifsc-code-finder?ifsc="+ifsc);
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
        }
        BankDetail bd = new BankDetail();
        bd.execute();
    }

    @SuppressLint("StaticFieldLeak")
    protected void mCheckSender(Uri.Builder builder,String sending_url)
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
                Log.e("check sender","response "+s);

                if (!s.equals(""))
                {
                    String ADDRESS="",BRANCH="",BANK="",IFSC="";
                    try{
                        JSONObject jsonObject=new JSONObject(s);


                        if (jsonObject.has("ADDRESS"))
                        {
                            ADDRESS=jsonObject.getString("ADDRESS");
                        }
                        if (jsonObject.has("IFSC"))
                        {
                            IFSC=jsonObject.getString("IFSC");
                        }

                        if (jsonObject.has("BRANCH"))
                        {
                            BRANCH=jsonObject.getString("BRANCH");
                        }

                        if (jsonObject.has("BANK"))
                        {
                            BANK=jsonObject.getString("BANK");
                        }


                        if (!IFSC.equals(""))
                        {
                            ll_bank_detail.setVisibility(View.VISIBLE);
                        }
                        else
                        {
                            ll_bank_detail.setVisibility(View.GONE);
                        }
                        textview_bank.setText(BANK);
                        textview_branch.setText(BRANCH);
                        textView_ifsc.setText(IFSC);
                        textview_address.setText(ADDRESS);

                    }
                    catch (JSONException e)
                    {
                        e.printStackTrace();
                    }
                }
                else
                {
                    ll_bank_detail.setVisibility(View.GONE);
                    Toast.makeText(getContext(), "Server not responding...", Toast.LENGTH_SHORT).show();
                }
            }
        }.execute();
    }


}
