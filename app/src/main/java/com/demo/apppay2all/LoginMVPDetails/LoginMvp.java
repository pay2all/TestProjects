package com.demo.apppay2all.LoginMVPDetails;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.demo.apppay2all.DetectConnection;
import com.demo.apppay2all.MainActivitySingle;
import com.demo.apppay2all.R;
import com.demo.apppay2all.SharePrefManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class LoginMvp extends AppCompatActivity implements LoginContract.View{

    EditText ed_username, ed_password;
    ImageView iv_login;
    ProgressBar loading;

    private Presenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_mvp);

        if (getSupportActionBar()!=null)
        {
            getSupportActionBar().hide();
        }

        ed_username =findViewById(R.id.ed_username);
        ed_password =findViewById(R.id.ed_password);
        loading=findViewById(R.id.loading);
        iv_login=findViewById(R.id.iv_login);
        iv_login.setEnabled(true);

        mPresenter=new Presenter(this);

        iv_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (DetectConnection.checkInternetConnection(LoginMvp.this))
                {
                    if (ed_username.getText().toString().equals(""))
                    {
                        Toast.makeText(LoginMvp.this, "Please enter username", Toast.LENGTH_SHORT).show();
                    }
                    else if (ed_username.getText().toString().equals(""))
                    {
                        Toast.makeText(LoginMvp.this, "Please enter a valid username", Toast.LENGTH_SHORT).show();
                    }
                    else if (ed_password.getText().toString().equals(""))
                    {
                        Toast.makeText(LoginMvp.this, "Please enter password", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        loading.setVisibility(View.VISIBLE);
                        String device_id= Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);

                        mPresenter.getDataFromUrl(LoginMvp.this,"", ed_username.getText().toString(), ed_password.getText().toString(),device_id);
                    }
                }
                else
                {
                    Toast.makeText(LoginMvp.this, "No internet connection", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onGetDataSuccess(String message, String data) {
        loading.setVisibility(View.GONE);

        mShowLoginDetail(data);

//        Log.e("status","response "+list.getStatus());
//        if (list.getStatus().equals("success"))
//        {
//            Toast.makeText(this, list.getUserDetails().getFirstname(), Toast.LENGTH_SHORT).show();
//        }
//        else
//        {
//            Toast.makeText(this, list.getStatus(), Toast.LENGTH_SHORT).show();
//        }

        Log.e("data","response "+message);
    }

    @Override
    public void onGetDataFailure(String message) {
        loading.setVisibility(View.GONE);
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    protected void mShowLoginDetail(final String response)
    {
        try
        {
            String message="";
            String status="";
            String user_id="";
            String first_name="";
            String last_name="";
            String email="";
            String mobile="";
            String role_id="";
            String scheme_id="";
            String joing_date="";

            String permanent_address="";
            String permanent_state="";
            String permanent_city="";
            String permanent_district="";
            String permanent_pin_code="";


            String present_address="";
            String present_city="";
            String present_state="";
            String present_district="";
            String present_pin_code="";


            String shop_name="";
            String office_address="";
            String call_back_url="";
            String profile_photo="";
            String shop_photo="";
            String gst_regisration_photo="";
            String pancard_photo="";
            String cancel_cheque="";

            String address_proof="";
            String kyc_status="";
            String kyc_remark="";
            String mobile_verified="";
            String lock_amount="";

            String session_id="";
            String active="";
            String reason="";
            String api_token="";
            String user_balance="";
            String aeps_balance="";
            String lien_amount="";
            String recharge="";
            String money="";
            String aeps="";
            String payout="";
            String pancard="";
            String ecommerce="";

            String company_name="";
            String company_email="";
            String company_address="";
            String company_address_two="";
            String support_number="";
            String whatsapp_number="";
            String company_logo="";
            String company_website="";

            String news="";
            String update_one="";
            String update_two="";
            String update_three="";
            String update_for="";
            String sender_id="";
            String company_recharge="";
            String company_money="";
            String company_aeps="";
            String company_payout="";
            String view_plan="";
            String company_pancard="";
            String company_ecommerce="";

            JSONObject jsonObject=new JSONObject(response);

            if (jsonObject.has("message"))
            {
                message=jsonObject.getString("message");
            }

            if (jsonObject.has("status"))
            {
                status=jsonObject.getString("status");
            }

            if (jsonObject.has("broadcast"))
            {
                JSONObject broadcast=jsonObject.getJSONObject("broadcast");
                Log.e("broadcast","details "+broadcast);

                if (broadcast.has("status_id"))
                {
                    String status_id=broadcast.getString("status_id");

                    SharePrefManager.getInstance(LoginMvp.this).mSaveSingleData("broadcast_status_id",status_id);
                }

                SharePrefManager.getInstance(LoginMvp.this).mSaveSingleData("broadcast",broadcast.toString());
            }


            if (status.equalsIgnoreCase("success"))
            {
                JSONObject userdetail=jsonObject.getJSONObject("userdetails");

                if (userdetail.has("user_id")) {
                    user_id = userdetail.getString("user_id");
                }
                first_name=userdetail.getString("first_name");
                last_name=userdetail.getString("last_name");
                email=userdetail.getString("email");
                mobile=userdetail.getString("mobile");
                role_id=userdetail.getString("role_id");
                scheme_id=userdetail.getString("scheme_id");

                joing_date=userdetail.getString("joing_date");
                permanent_address=userdetail.getString("address");
                permanent_city=userdetail.getString("city");
                permanent_state=userdetail.getString("state_id");
                permanent_district=userdetail.getString("district_id");
                permanent_pin_code=userdetail.getString("pin_code");

//                present_address=userdetail.getString("present_address");
//                present_city=userdetail.getString("present_city");
//                present_state=userdetail.getString("present_state");
//                present_district=userdetail.getString("present_district");
//                present_pin_code=userdetail.getString("present_pin_code");

                shop_name=userdetail.getString("shop_name");
                office_address=userdetail.getString("office_address");
                call_back_url=userdetail.getString("call_back_url");
                profile_photo=userdetail.getString("profile_photo");
                shop_photo=userdetail.getString("shop_photo");
                gst_regisration_photo=userdetail.getString("gst_regisration_photo");
                pancard_photo=userdetail.getString("pancard_photo");
                cancel_cheque=userdetail.getString("cancel_cheque");
                address_proof=userdetail.getString("address_proof");
                kyc_status=userdetail.getString("kyc_status");
                kyc_remark=userdetail.getString("kyc_remark");
                mobile_verified=userdetail.getString("mobile_verified");
                lock_amount=userdetail.getString("lock_amount");
                session_id=userdetail.getString("session_id");
                active=userdetail.getString("active");
                reason=userdetail.getString("reason");
                api_token=userdetail.getString("api_token");
                user_balance=userdetail.getString("user_balance");
                aeps_balance=userdetail.getString("aeps_balance");
                lien_amount=userdetail.getString("lien_amount");

                if (userdetail.has("account_number"))
                {
                    SharePrefManager.getInstance(LoginMvp.this).mSaveSingleData("account_number",userdetail.getString("account_number"));
                }

                if (userdetail.has("ifsc_code"))
                {
                    SharePrefManager.getInstance(LoginMvp.this).mSaveSingleData("ifsc_code",userdetail.getString("ifsc_code"));
                }

                if (userdetail.has("pan_username"))
                {
                    SharePrefManager.getInstance(LoginMvp.this).mSaveSingleData("pan_username",userdetail.getString("pan_username"));
                }

                if (userdetail.has("ekyc"))
                {
                    SharePrefManager.getInstance(LoginMvp.this).mSaveSingleData("ekyc",userdetail.getString("ekyc"));
                }

                if (userdetail.has("pan_number"))
                {
                    SharePrefManager.getInstance(LoginMvp.this).mSaveSingleData("pan_number",userdetail.getString("pan_number"));
                }
                if (userdetail.has("agentonboarding"))
                {
                    SharePrefManager.getInstance(LoginMvp.this).mSaveSingleData("agentonboarding",userdetail.getString("agentonboarding"));
                }

                JSONObject userservices=jsonObject.getJSONObject("userservices");
                recharge=userservices.getString("recharge");
                money=userservices.getString("money");

                if (userservices.has("money_two")) {
                    String money_two = userservices.getString("money_two");
                    SharePrefManager.getInstance(LoginMvp.this).mSaveSingleData("money_two",money_two);
                }

                aeps=userservices.getString("aeps");
                payout=userservices.getString("payout");
                pancard=userservices.getString("pancard");
                ecommerce=userservices.getString("ecommerce");

                JSONObject companydetails=jsonObject.getJSONObject("companydetails");
                company_name=companydetails.getString("company_name");
                company_email=companydetails.getString("company_email");
                company_address=companydetails.getString("company_address");
                company_address_two=companydetails.getString("company_address_two");
                support_number=companydetails.getString("support_number");
                whatsapp_number=companydetails.getString("whatsapp_number");
                company_logo=companydetails.getString("company_logo");
                company_website=companydetails.getString("company_website");
                news=companydetails.getString("news");
//                update_one=companydetails.getString("update_one");
//                update_two=companydetails.getString("update_two");
//                update_three=companydetails.getString("update_three");
//                update_for=companydetails.getString("update_for");
                sender_id=companydetails.getString("sender_id");
                company_recharge=companydetails.getString("recharge");
                company_money=companydetails.getString("money");

                if (companydetails.has("money_two")) {
                    String money_two = companydetails.getString("money_two");
                    SharePrefManager.getInstance(LoginMvp.this).mSaveSingleData("company_money_two",money_two);
                }

                company_aeps=companydetails.getString("aeps");
                company_payout=companydetails.getString("payout");
                view_plan=companydetails.getString("view_plan");
                company_pancard=companydetails.getString("pancard");
                company_ecommerce=companydetails.getString("ecommerce");

                if (companydetails.has("upi_id")) {
                    String upi_id = companydetails.getString("upi_id");
                    SharePrefManager.getInstance(LoginMvp.this).mSaveSingleData("upi_id",upi_id);
                }

                if (companydetails.has("collection")) {
                    String collection = companydetails.getString("collection");
                    SharePrefManager.getInstance(LoginMvp.this).mSaveSingleData("collection",collection);
                }

                JSONArray banners=jsonObject.getJSONArray("banner");

                SharePrefManager.getInstance(LoginMvp.this).mSaveUserid(user_id);
                SharePrefManager.getInstance(LoginMvp.this).mSaveUserData(ed_username.getText().toString(), ed_password.getText().toString(),first_name,last_name,email,mobile,role_id,scheme_id,
                        joing_date,permanent_address,permanent_city,permanent_district,permanent_pin_code,permanent_state,present_address,
                        present_city,present_district,present_pin_code,present_state,lien_amount,office_address,call_back_url,profile_photo,
                        shop_name,shop_photo,gst_regisration_photo,pancard_photo,cancel_cheque,address_proof,kyc_status,kyc_remark,mobile_verified,
                        lock_amount,session_id,active,reason,api_token,user_balance,aeps_balance,recharge,money,aeps,payout,pancard,ecommerce,
                        company_name,company_email,company_address,company_address_two,support_number,whatsapp_number,company_logo,company_website,
                        news,update_one,update_two,update_three,update_for,sender_id,company_recharge,company_money,company_aeps,company_payout,view_plan,company_pancard,company_ecommerce,banners.toString(),"","");

                Intent intent = new Intent(LoginMvp.this, MainActivitySingle.class);
                startActivity(intent);
                finish();
//                mGetOperators();

            }
            else if (!message.equals(""))
            {
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
            }
            else
            {
                Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
    }
}