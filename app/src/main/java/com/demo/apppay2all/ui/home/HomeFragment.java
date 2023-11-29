package com.demo.apppay2all.ui.home;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;

import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager.widget.ViewPager;

import com.demo.apppay2all.BaseURL.BaseURL;
import com.demo.apppay2all.CallResAPIPOSTMethod;
import com.demo.apppay2all.CommingSoonActivity;
import com.demo.apppay2all.CommissionDetail.CommissionDetails;
import com.demo.apppay2all.DetectConnection;
import com.demo.apppay2all.FlightTicketBooking.TravelItems;
import com.demo.apppay2all.FlightTicketBooking.TravelServiceAdapter;
import com.demo.apppay2all.MainActivity;
import com.demo.apppay2all.R;
import com.demo.apppay2all.ServiceDetailsNew.ServicesCardAdapter;
import com.demo.apppay2all.ServiceDetailsNew.ServicesItems;
import com.demo.apppay2all.SharePrefManager;
import com.demo.apppay2all.SlidingImageAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class HomeFragment extends Fragment {

    View root;

    ViewPager viewPager;

    ViewPager viewpager_banners;
    int currentPage_banners = 0;
    int NUM_PAGES_banner = 0;

    Timer swipeTimer_banners;
    ArrayList<String> imageId;

    LinearLayout ll_commission,ll_share,ll_university;
    CardView cv_share_1,cv_share_2;

    LinearLayout ll_retailer_details;

    RecyclerView all_services;
    List<ServicesItems> servicesItems;
    ServicesCardAdapter servicesCardAdapter;

    SwipeRefreshLayout swipe;


    List<TravelItems> travelItemsList;
    RecyclerView rv_travel;
    TravelServiceAdapter travelServiceAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
//        HomeViewModel homeViewModel =
//                new ViewModelProvider(this).get(HomeViewModel.class);


         root = inflater.inflate(R.layout.fragment_home,container,false);

        swipe=root.findViewById(R.id.swipe);
        swipe.setColorSchemeColors(getActivity().getResources().getColor(R.color.colorPrimary));
        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (DetectConnection.checkInternetConnection(getActivity()))
                {
                    mUpdateBalance();
                }
                else {
                    Toast.makeText(getContext(), "No internet connection", Toast.LENGTH_SHORT).show();
                }
            }
        });

        all_services=root.findViewById(R.id.all_services);
        all_services.setLayoutManager(new LinearLayoutManager(getActivity()));
        servicesItems=new ArrayList<>();
        servicesCardAdapter=new ServicesCardAdapter(getActivity(),servicesItems);
        all_services.setAdapter(servicesCardAdapter);
        mShowServices();

        ll_retailer_details=root.findViewById(R.id.ll_retailer_details);

        if (SharePrefManager.getInstance(getActivity()).mGetRoleId()<8)
        {
            ll_retailer_details.setVisibility(View.GONE);
        }
        else
        {
            ll_retailer_details.setVisibility(View.VISIBLE);

        }

        ll_commission=root.findViewById(R.id.ll_commission);
        ll_commission.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), CommissionDetails.class));
            }
        });

        ll_share =root.findViewById(R.id.ll_share);
        ll_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent("android.intent.action.SEND");
                i.setType("text/plain");
                String stringBuilder = "https://play.google.com/store/apps/details?id=" +getActivity().getPackageName();
                i.putExtra("android.intent.extra.TEXT", stringBuilder);
                startActivity(Intent.createChooser(i, "Share via"));
            }
        });

        ll_university=root.findViewById(R.id.ll_university);
        ll_university.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), CommingSoonActivity.class));
            }
        });

        cv_share_1=root.findViewById(R.id.cv_share_1);
        cv_share_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent("android.intent.action.SEND");
                i.setType("text/plain");
                String stringBuilder = "https://play.google.com/store/apps/details?id=" +getActivity().getPackageName();
                i.putExtra("android.intent.extra.TEXT", stringBuilder);
                startActivity(Intent.createChooser(i, "Share via"));
            }
        });


        cv_share_2=root.findViewById(R.id.cv_share_2);
        cv_share_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent("android.intent.action.SEND");
                i.setType("text/plain");
                String stringBuilder = "https://play.google.com/store/apps/details?id=" +getActivity().getPackageName();
                i.putExtra("android.intent.extra.TEXT", stringBuilder);
                startActivity(Intent.createChooser(i, "Share via"));
            }
        });


        rv_travel=root.findViewById(R.id.rv_travel);
        rv_travel.setLayoutManager(new GridLayoutManager(getActivity(),3));
        travelItemsList=new ArrayList<>();
        travelServiceAdapter=new TravelServiceAdapter(getActivity(),travelItemsList);
        rv_travel.setAdapter(travelServiceAdapter);
        mShowTravelServices();

        return root;
    }

    private void mShowTravelServices() {

        travelItemsList.clear();
        travelServiceAdapter.notifyDataSetChanged();

        String[] names={"Flight","Train","Bus"};
        int[] icons={R.drawable.flight_icon,R.drawable.train,R.drawable.bus};

        for (int i=0; i< names.length; i++)
        {
            TravelItems items=new TravelItems();
            items.setId(names[i]);
            items.setName(names[i]);
            items.setImage_url("");
            items.setImage(icons[i]);
            travelItemsList.add(items);
            travelServiceAdapter.notifyDataSetChanged();

        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    protected void mShowLoginDetail(final String response) {
        try {
            String message = "";
            String status = "";
            String first_name = "";
            String last_name = "";
            String email = "";
            String mobile = "";
            String role_id = "";
            String scheme_id = "";
            String joing_date = "";

            String permanent_address = "";
            String permanent_state = "";
            String permanent_city = "";
            String permanent_district = "";
            String permanent_pin_code = "";

            String present_address = "";
            String present_city = "";
            String present_state = "";
            String present_district = "";
            String present_pin_code = "";

            String shop_name = "";
            String office_address = "";
            String call_back_url = "";
            String profile_photo = "";
            String shop_photo = "";
            String gst_regisration_photo = "";
            String pancard_photo = "";
            String cancel_cheque = "";

            String address_proof = "";
            String kyc_status = "";
            String kyc_remark = "";
            String mobile_verified = "";
            String lock_amount = "";

            String session_id = "";
            String active = "";
            String reason = "";
            String api_token = "";
            String user_balance = "";
            String aeps_balance = "";
            String lien_amount = "";
            String icici_agent_id = "";
            String outlet_id = "";
            String recharge = "";
            String money = "";
            String aeps = "";
            String payout = "";
            String pancard = "";
            String ecommerce = "";

            String company_name = "";
            String company_email = "";
            String company_address = "";
            String company_address_two = "";
            String support_number = "";
            String whatsapp_number = "";
            String company_logo = "";
            String company_website = "";

            String news = "";
            String update_one = "";
            String update_two = "";
            String update_three = "";
            String update_for = "";
            String sender_id = "";
            String company_recharge = "";
            String company_money = "";
            String company_aeps = "";
            String company_payout = "";
            String view_plan = "";
            String company_pancard = "";
            String company_ecommerce = "";
            String today_sale = "";
            String today_profit = "";

            String upi_id = "";
            String collection = "";

            JSONObject jsonObject = new JSONObject(response);

            if (jsonObject.has("message")) {
                message = jsonObject.getString("message");
            }

            if (jsonObject.has("status")) {
                status = jsonObject.getString("status");
            }

            if (status.equalsIgnoreCase("success")) {
                JSONObject userdetail = jsonObject.getJSONObject("userdetails");

                first_name = userdetail.getString("first_name");
                last_name = userdetail.getString("last_name");
                email = userdetail.getString("email");
                mobile = userdetail.getString("mobile");
                role_id = userdetail.getString("role_id");
                scheme_id = userdetail.getString("scheme_id");

//                joing_date = userdetail.getString("joing_date");

                permanent_address = userdetail.getString("address");
                permanent_city = userdetail.getString("city");
                permanent_state = userdetail.getString("state_id");
                permanent_district = userdetail.getString("district_id");
                permanent_pin_code = userdetail.getString("pin_code");

//                permanent_address = userdetail.getString("permanent_address");
//                permanent_city = userdetail.getString("permanent_city");
//                permanent_state = userdetail.getString("permanent_state");
//                permanent_district = userdetail.getString("permanent_district");
//                permanent_pin_code = userdetail.getString("permanent_pin_code");
//
//                present_address = userdetail.getString("present_address");
//                present_city = userdetail.getString("present_city");
//                present_state = userdetail.getString("present_state");
//                present_district = userdetail.getString("present_district");
//                present_pin_code = userdetail.getString("present_pin_code");

                shop_name = userdetail.getString("shop_name");
                office_address = userdetail.getString("office_address");
                call_back_url = userdetail.getString("call_back_url");
                profile_photo = userdetail.getString("profile_photo");
                shop_photo = userdetail.getString("shop_photo");
                gst_regisration_photo = userdetail.getString("gst_regisration_photo");
                pancard_photo = userdetail.getString("pancard_photo");
                cancel_cheque = userdetail.getString("cancel_cheque");
                address_proof = userdetail.getString("address_proof");
                kyc_status = userdetail.getString("kyc_status");
                kyc_remark = userdetail.getString("kyc_remark");
                mobile_verified = userdetail.getString("mobile_verified");
                lock_amount = userdetail.getString("lock_amount");
                session_id = userdetail.getString("session_id");
                active = userdetail.getString("active");
                reason = userdetail.getString("reason");
                api_token = userdetail.getString("api_token");
                user_balance = userdetail.getString("user_balance");
                aeps_balance = userdetail.getString("aeps_balance");
                lien_amount = userdetail.getString("lien_amount");
//                icici_agent_id = userdetail.getString("icici_agent_id");
//                outlet_id = userdetail.getString("outlet_id");

                if (userdetail.has("gst_registration_number")) {
                    String value = userdetail.getString("gst_registration_number");
                    SharePrefManager.getInstance(getActivity()).mSaveSingleData("gst_registration_number", value);
                }

                if (userdetail.has("pancard_number")) {
                    String value = userdetail.getString("pancard_number");
                    SharePrefManager.getInstance(getActivity()).mSaveSingleData("pancard_number", value);
                }

                if (userdetail.has("cancel_cheque_number")) {
                    String value = userdetail.getString("cancel_cheque_number");
                    SharePrefManager.getInstance(getActivity()).mSaveSingleData("cancel_cheque_number", value);
                }
                if (userdetail.has("address_proof_back_side")) {
                    String value = userdetail.getString("address_proof_back_side");
                    SharePrefManager.getInstance(getActivity()).mSaveSingleData("address_proof_back_side", value);
                }

                if (userdetail.has("address_proof_number")) {
                    String value = userdetail.getString("address_proof_number");
                    SharePrefManager.getInstance(getActivity()).mSaveSingleData("address_proof_number", value);
                }

                if (userdetail.has("account_number")) {
                    String account_number = userdetail.getString("account_number");
                    SharePrefManager.getInstance(getActivity()).mSaveSingleData("account_number", account_number);
                }

                if (userdetail.has("ifsc_code")) {
                    String ifsc_code = userdetail.getString("ifsc_code");
                    SharePrefManager.getInstance(getActivity()).mSaveSingleData("ifsc_code", ifsc_code);
                }

                if (userdetail.has("pan_number")) {
                    String pan_number = userdetail.getString("pan_number");
                    SharePrefManager.getInstance(getActivity()).mSaveSingleData("pan_number", pan_number);
                }

                if (userdetail.has("agentonboarding")) {
                    String agentonboarding = userdetail.getString("agentonboarding");
                    SharePrefManager.getInstance(getActivity()).mSaveSingleData("agentonboarding", agentonboarding);
                }

//                JSONObject userservices = jsonObject.getJSONObject("userservices");
//                recharge = userservices.getString("recharge");
//                money = userservices.getString("money");
//                aeps = userservices.getString("aeps");
//                payout = userservices.getString("payout");
//                pancard = userservices.getString("pancard");
//                ecommerce = userservices.getString("ecommerce");
//
//
//                if (userservices.has("money_two")) {
//                    String money_two = userservices.getString("money_two");
//                    SharePrefManager.getInstance(getActivity()).mSaveSingleData("money_two", money_two);
//                }
//
//                if (userservices.has("money_for")) {
//                    String money_for = userservices.getString("money_for");
//                    SharePrefManager.getInstance(getActivity()).mSaveSingleData("money_for", money_for);
//                }
//
//                if (userservices.has("aeps_two")) {
//                    String aeps_two = userservices.getString("aeps_two");
//                    SharePrefManager.getInstance(getActivity()).mSaveSingleData("aeps_two", aeps_two);
//                }
//
//                if (userservices.has("humtum")) {
//                    String humtum = userservices.getString("humtum");
//                    SharePrefManager.getInstance(getActivity()).mSaveSingleData("humtum", humtum);
//                }

                if (userdetail.has("ekyc")) {
                    String ekyc = userdetail.getString("ekyc");
                    SharePrefManager.getInstance(getActivity()).mSaveSingleData("ekyc", ekyc);

//                    mUpdateNav();
                }

//                if (userservices.has("transfer_to_upi")) {
//                    String transfer_to_upi = userservices.getString("transfer_to_upi");
//                    SharePrefManager.getInstance(getActivity()).mSaveSingleData("transfer_to_upi", transfer_to_upi);
//                }
//
//                if (userservices.has("nsdl_pancard")) {
//                    String nsdl_pancard = userservices.getString("nsdl_pancard");
//                    SharePrefManager.getInstance(getActivity()).mSaveSingleData("nsdl_pancard", nsdl_pancard);
//                }
//
//                if (userservices.has("upi_collection")) {
//                    String upi_collection = userservices.getString("upi_collection");
//                    SharePrefManager.getInstance(getActivity()).mSaveSingleData("upi_collection", upi_collection);
//                }

                JSONObject companydetails = jsonObject.getJSONObject("companydetails");
                company_name = companydetails.getString("company_name");
                company_email = companydetails.getString("company_email");
                company_address = companydetails.getString("company_address");
                company_address_two = companydetails.getString("company_address_two");
                support_number = companydetails.getString("support_number");
                whatsapp_number = companydetails.getString("whatsapp_number");
                company_logo = companydetails.getString("company_logo");
                company_website = companydetails.getString("company_website");
                news = companydetails.getString("news");

                sender_id = companydetails.getString("sender_id");
                view_plan = companydetails.getString("view_plan");
//                update_one = companydetails.getString("update_one");
//                update_two = companydetails.getString("update_two");
//                update_three = companydetails.getString("update_three");
//                update_for = companydetails.getString("update_for");
//                company_recharge = companydetails.getString("recharge");
//                company_money = companydetails.getString("money");
//                company_aeps = companydetails.getString("aeps");
//                company_payout = companydetails.getString("payout");
//
//                company_pancard = companydetails.getString("pancard");
//                company_ecommerce = companydetails.getString("ecommerce");

                if (companydetails.has("transaction_pin")) {
                    String  transaction_pin = companydetails.getString("transaction_pin");
                    SharePrefManager.getInstance(getActivity()).mSaveSingleData("transaction_pin", transaction_pin);
                }


                if (companydetails.has("upi_id")) {
                    upi_id = companydetails.getString("upi_id");
                    SharePrefManager.getInstance(getActivity()).mSaveSingleData("upi_id", upi_id);
                }

//                if (companydetails.has("money_for")) {
//                    String money_for = companydetails.getString("money_for");
//                    SharePrefManager.getInstance(getActivity()).mSaveSingleData("company_money_for", money_for);
//                }
//
//                if (companydetails.has("aeps_two")) {
//                    String aeps_two = companydetails.getString("aeps_two");
//                    SharePrefManager.getInstance(getActivity()).mSaveSingleData("company_aeps_two", aeps_two);
//                }
//
//
//                if (companydetails.has("collection")) {
//                    collection = companydetails.getString("collection");
//                    SharePrefManager.getInstance(getActivity()).mSaveSingleData("collection", collection);
//
//                }

                JSONArray banners = jsonObject.getJSONArray("banner");

                if (jsonObject.has("total_notification")) {
                    String total_notification = jsonObject.getString("total_notification");
                    SharePrefManager.getInstance(getActivity()).mSaveNoOfNotification(total_notification);
                }

                if (jsonObject.has("notification")) {
                    String notification = jsonObject.getJSONArray("notification").toString();
                    SharePrefManager.getInstance(getActivity()).mSaveAllNotification(notification);
                }

                if (jsonObject.has("recharge_badge")) {
                    SharePrefManager.getInstance(getActivity()).mSaveServicesName(jsonObject.getString("recharge_badge"));
                    JSONArray jsonArray = jsonObject.getJSONArray("recharge_badge");

                    Log.e("data","services "+jsonArray);

                    if (jsonArray.length() == 0) {
                        jsonArray = new JSONArray(SharePrefManager.getInstance(getActivity()).mGetServices());
                    }

                    mShowServices();

                    mShowBanners();


//                    if (SharePrefManager.getInstance(getActivity()).mGetSharePrefSingleData("collection").equalsIgnoreCase("1")) {
//                        ServicesItems singleitem = new ServicesItems();
//                        singleitem.setId("upi");
//                        singleitem.setName("Accept payment");
//                        singleitem.setService_image("");
//                        singleitem.setBbps("0");
//                        singleitem.setImage(R.drawable.upi_icon);
//
//                        servicesItems.add(singleitem);
//                        servicesCardAdapter.notifyDataSetChanged();
//                    }
                }

                if (jsonObject.has("sales")) {
                    JSONObject sales = jsonObject.getJSONObject("sales");
                    if (sales.has("today_sale")) {
                        today_sale = sales.getString("today_sale");
                        SharePrefManager.getInstance(getActivity()).mSaveTodaySale(today_sale);
                    }
                    if (sales.has("today_profit")) {
                        today_profit = sales.getString("today_profit");
                        SharePrefManager.getInstance(getActivity()).mSaveTodayProfit(today_profit);
                    }
                }

                String username=SharePrefManager.getInstance(getActivity()).mGetUsername();
                String password=SharePrefManager.getInstance(getActivity()).getPassword();

//                if (SharePrefManager.getInstance(getActivity()).mGetSessionId().equalsIgnoreCase(session_id)) {
                SharePrefManager.getInstance(getActivity()).mSaveUserData(username, password, first_name, last_name, email, mobile, role_id, scheme_id,
                        joing_date, permanent_address, permanent_city, permanent_district, permanent_pin_code, permanent_state, present_address,
                        present_city, present_district, present_pin_code, present_state, lien_amount, office_address, call_back_url, profile_photo,
                        shop_name, shop_photo, gst_regisration_photo, pancard_photo, cancel_cheque, address_proof, kyc_status, kyc_remark, mobile_verified,
                        lock_amount, session_id, active, reason, api_token, user_balance, aeps_balance, recharge, money, aeps, payout, pancard, ecommerce,
                        company_name, company_email, company_address, company_address_two, support_number, whatsapp_number, company_logo, company_website,
                        news, update_one, update_two, update_three, update_for, sender_id, company_recharge, company_money, company_aeps, company_payout,
                        view_plan, company_pancard, company_ecommerce, banners.toString(), icici_agent_id, outlet_id);


                ((MainActivity)getActivity()).mUpdateBalance();



//                    if (!SharePrefManager.getInstance(getActivity()).mKycStatus()) {
//                        startActivity(new Intent(getActivity(), FormsKyc.class));
//                        finish();
//                    }
//                }
//                else {
//                    Toast.makeText(this, "Session expired please, login again", Toast.LENGTH_SHORT).show();
//                    SharePrefManager.getInstance(getActivity()).mLogout();
//                    startActivity(new Intent(getActivity(), Login.class));
//                    finish();
//                }
            }
//            else if (status.equalsIgnoreCase("failure"))
//            {
//                startActivity(new Intent(getActivity(),Login.class));
//                Toast.makeText(this, "Session expired please login again", Toast.LENGTH_SHORT).show();
//                finish();
//            }


        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @SuppressLint("StaticFieldLeak")
    protected void mUpdateBalance() {
        Uri.Builder builder = new Uri.Builder();
        builder.appendQueryParameter("api_token", SharePrefManager.getInstance(getActivity()).mGetApiToken());
        String sending_url = BaseURL.BASEURL_B2C + "api/application/v1/check-balance";

        new CallResAPIPOSTMethod(getActivity(), builder, sending_url, true, "POST") {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
//                pb_sync.setVisibility(View.VISIBLE);
//                iv_sync.setVisibility(View.GONE);
                swipe.setRefreshing(true);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                swipe.setRefreshing(false);
//                pb_sync.setVisibility(View.GONE);
//                iv_sync.setVisibility(View.VISIBLE);
                Log.e("response", "balance \n\n\ndata " + s);

                mShowLoginDetail(s);
            }
        }.execute();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (DetectConnection.checkInternetConnection(getActivity())) {
            mUpdateBalance();
        }
    }

    protected void mShowServices()
    {
        servicesItems.clear();

        try {
            JSONArray jsonArray=new JSONArray(SharePrefManager.getInstance(getActivity()).mGetServices());
            for (int i=0; i<jsonArray.length(); i++)
            {
                JSONObject data=jsonArray.getJSONObject(i);
                ServicesItems items=new ServicesItems();
                items.setId(data.getString("id"));
                items.setName(data.getString("title"));
                items.setData(data.getJSONArray("data")+"");
                servicesItems.add(items);
                servicesCardAdapter.notifyDataSetChanged();
            }
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
    }

    private void mShowBanners() {

        RelativeLayout rl_banner = root.findViewById(R.id.rl_banner);
        viewPager = root.findViewById(R.id.viewPager);
        imageId=new ArrayList();

        try {
            JSONArray jsonArray =new JSONArray(SharePrefManager.getInstance(getActivity()).mGetBanners());

            for (int i=0; i<jsonArray.length(); i++)
            {
                JSONObject data=jsonArray.getJSONObject(i);

                imageId.add(data.getString("image"));
            }

            if (imageId.size()>0)
            {
                rl_banner.setVisibility(View.GONE);
                viewPager.setVisibility(View.VISIBLE);
            }
            else
            {
                rl_banner.setVisibility(View.VISIBLE);
                viewPager.setVisibility(View.GONE);
            }

        }
        catch (  JSONException e)
        {
            e.printStackTrace();
        }

        viewPager.setAdapter(new SlidingImageAdapter(getActivity(), imageId));

        NUM_PAGES_banner = imageId.size();

        // Auto start of viewpager
        final Handler handler =new Handler();

        final Runnable Update=new Runnable() {
            @Override
            public void run() {
                if (currentPage_banners == NUM_PAGES_banner) {
                    currentPage_banners = 0;
                }
                viewPager.setCurrentItem(currentPage_banners++, true);
            }
        };

        swipeTimer_banners =new Timer();
        swipeTimer_banners.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(Update);

            }
        }, 5000, 5000);



//        CirclePageIndicate indicator = findViewById(R.id.indicator);

//        indicator.setViewPager(viewpager_banners);

//        float density = getResources().getDisplayMetrics().density;

        //Set circle indicator radius
//        indicator.setRadius(3 * density);

//        indicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
//            @Override
//            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//
//            }
//
//            @Override
//            public void onPageSelected(int position) {
//                currentPage_banners=position;
//            }
//
//            @Override
//            public void onPageScrollStateChanged(int state) {
//
//            }
//        });

    }

    @Override
    public void onDestroy() {
        if (swipeTimer_banners != null) {
            swipeTimer_banners.cancel();
        }

        super.onDestroy();
    }

    @Override
    public void onPause() {
        if (swipeTimer_banners != null) {
            swipeTimer_banners.cancel();
        }

        super.onPause();
    }


    public boolean mCheckCamera() {
        return ContextCompat.checkSelfPermission(getActivity(), "android.permission.CAMERA") == 0;
    }

    public void mRequestCamera() {
        if (!ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), "android.permission.CAMERA")) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{"android.permission.CAMERA"}, 1);
        }
    }
}