package com.demo.apppay2all;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;


public class SharePrefManager {
    private static final String SHARE_PREFRERENCE="user";
    private static SharePrefManager mInstance;
    private static Context mContext;
    String mResponse="";

    ProgressDialog dialog;

    private SharePrefManager(Context context)
    {
        mContext=context;
    }

    public static synchronized SharePrefManager getInstance(Context context)
    {
        if (mInstance==null)
        {
            mInstance=new SharePrefManager(context);
        }
        return mInstance;
    }


    public boolean mCheckLogin()
    {
        SharedPreferences sharedPreferences=mContext.getSharedPreferences(SHARE_PREFRERENCE,0);
        String name=sharedPreferences.getString("name","");

        if (!name.equals(""))
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    public void mSetBaseUrl(String baseurl)
    {
        SharedPreferences sharedPreferences=mContext.getSharedPreferences(SHARE_PREFRERENCE,0);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putString("baseurl",baseurl);
        editor.apply();
    }

    public String mGetMainBalance()
    {
        SharedPreferences sharedPreferences=mContext.getSharedPreferences(SHARE_PREFRERENCE,0);
        String main_balance=sharedPreferences.getString("user_balance","");
        return main_balance;
    }
    public String mGetAEPSBalance()
    {
        SharedPreferences sharedPreferences=mContext.getSharedPreferences(SHARE_PREFRERENCE,0);
        String aeps_balance=sharedPreferences.getString("aeps_balance","");
        return aeps_balance;
    }
    public void mSaveUserData(String username,String password,String first_name,String last_name,String email,String mobile,String role_id,String scheme_id,String
            joing_date,String permanent_address,String permanent_city,String permanent_district,String permanent_pin_code,String permanent_state,String present_address,String
                                      present_city,String present_district,String present_pin_code,String present_state,String lien_amount,String office_address,String call_back_url,String profile_photo,String
                                      shop_name,String shop_photo,String gst_regisration_photo,String pancard_photo,String cancel_cheque,String address_proof,String kyc_status,String kyc_remark,String mobile_verified,String
                                      lock_amount,String session_id,String active,String reason,String api_token,String user_balance,String aeps_balance,String recharge,String money,String aeps,String payout,String pancard,String ecommerce,String
                                      company_name,String company_email,String company_address,String company_address_two,String support_number,String whatsapp_number,String company_logo,String company_website,String
                                      news,String update_one,String update_two,String update_three,String update_for,String sender_id,String company_recharge,String company_money,String company_aeps,
                              String company_payout,String view_plan,String company_pancard,String company_ecommerce,String banners,String icici_agent_id,String outlet_id)
    {
        SharedPreferences sharedPreferences=mContext.getSharedPreferences(SHARE_PREFRERENCE,0);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putString("username",username);
        editor.putString("password",password);
        editor.putString("first_name",first_name);
        editor.putString("last_name",last_name);
        editor.putString("mobile",mobile);
        editor.putString("email",email);
        editor.putString("role_id",role_id);
        editor.putString("scheme_id",scheme_id);
        editor.putString("joing_date",joing_date);
        editor.putString("permanent_address",permanent_address);
        editor.putString("permanent_city",permanent_city);
        editor.putString("permanent_district",permanent_district);
        editor.putString("permanent_pin_code",permanent_pin_code);
        editor.putString("permanent_state",permanent_state);
        editor.putString("permanent_state",permanent_state);
        editor.putString("present_address",present_address);
        editor.putString("present_district",present_district);
        editor.putString("present_city",present_city);
        editor.putString("present_pin_code",present_pin_code);
        editor.putString("present_state",present_state);
        editor.putString("lien_amount",lien_amount);
        editor.putString("office_address",office_address);
        editor.putString("call_back_url",call_back_url);
        editor.putString("profile_photo",profile_photo);
        editor.putString("shop_name",shop_name);
        editor.putString("shop_photo",shop_photo);
        editor.putString("gst_regisration_photo",gst_regisration_photo);
        editor.putString("pancard_photo",pancard_photo);
        editor.putString("cancel_cheque",cancel_cheque);
        editor.putString("address_proof",address_proof);
        editor.putString("kyc_status",kyc_status);
        editor.putString("kyc_remark",kyc_remark);
        editor.putString("mobile_verified",mobile_verified);
        editor.putString("lock_amount",lock_amount);
        editor.putString("session_id",session_id);
        editor.putString("active",active);
        editor.putString("reason",reason);
        editor.putString("api_token",api_token);
        editor.putString("user_balance",user_balance);
        editor.putString("aeps_balance",aeps_balance);
        editor.putString("recharge",recharge);
        editor.putString("money",money);
        editor.putString("aeps",aeps);
        editor.putString("payout",payout);
        editor.putString("pancard",pancard);
        editor.putString("ecommerce",ecommerce);
        editor.putString("company_name",company_name);
        editor.putString("company_email",company_email);
        editor.putString("company_address",company_address);
        editor.putString("company_address_two",company_address_two);
        editor.putString("support_number",support_number);
        editor.putString("whatsapp_number",whatsapp_number);
        editor.putString("company_logo",company_logo);
        editor.putString("company_website",company_website);
        editor.putString("news",news);
        editor.putString("update_one",update_one);
        editor.putString("update_two",update_two);
        editor.putString("update_three",update_three);
        editor.putString("update_for",update_for);
        editor.putString("sender_id",sender_id);
        editor.putString("company_recharge",company_recharge);
        editor.putString("company_money",company_money);
        editor.putString("company_aeps",company_aeps);
        editor.putString("company_pancard",company_pancard);
        editor.putString("company_payout",company_payout);
        editor.putString("view_plan",view_plan);
        editor.putString("company_ecommerce",company_ecommerce);
        editor.putString("banners",banners);
        editor.putString("icici_agent_id",icici_agent_id);
        editor.putString("outlet_id",outlet_id);

        editor.apply();

    }

    public void mLogout()
    {
        SharedPreferences sharedPreferences=mContext.getSharedPreferences(SHARE_PREFRERENCE,0);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.clear();
        editor.commit();
    }

    public void mSaveUserid(String user_id)
    {
        SharedPreferences sharedPreferences=mContext.getSharedPreferences(SHARE_PREFRERENCE,0);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putString("user_id",user_id);
        editor.apply();
    }
    public void mSaveSingleData(String key,String value)
    {
        SharedPreferences sharedPreferences=mContext.getSharedPreferences(SHARE_PREFRERENCE,0);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putString(key,value);
        editor.apply();
    }

    public String mGetId() {
        SharedPreferences sharedPreferences=mContext.getSharedPreferences(SHARE_PREFRERENCE,0);
        return sharedPreferences.getString("user_id","");
    }
    public int mGetRoleId() {
        SharedPreferences sharedPreferences=mContext.getSharedPreferences(SHARE_PREFRERENCE,0);
        return Integer.parseInt(sharedPreferences.getString("role_id",""));
    }
    public String mGetTodaySale() {
        SharedPreferences sharedPreferences=mContext.getSharedPreferences(SHARE_PREFRERENCE,0);
        return sharedPreferences.getString("today_sale","");
    }
    public String mGetTodayProfit() {
        SharedPreferences sharedPreferences=mContext.getSharedPreferences(SHARE_PREFRERENCE,0);
        return sharedPreferences.getString("today_profit","");
    }
    public void mSaveTodaySale(String data) {
        SharedPreferences sharedPreferences=mContext.getSharedPreferences(SHARE_PREFRERENCE,0);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putString("today_sale",data);
        editor.apply();
    }

    public long mGetSharePrefSingleDataLong(String items)
    {
        SharedPreferences sharedPreferences=mContext.getSharedPreferences(SHARE_PREFRERENCE,0);
        return sharedPreferences.getLong(items,0);
    }


    public void mSaveSingleDataLong(String key,long value)
    {
        SharedPreferences sharedPreferences=mContext.getSharedPreferences(SHARE_PREFRERENCE,0);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putLong(key,value);
        editor.apply();
    }


    public void mSaveTodayProfit(String data) {
        SharedPreferences sharedPreferences=mContext.getSharedPreferences(SHARE_PREFRERENCE,0);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putString("today_profit",data);
        editor.apply();

    }
    public String mGetFirstName() {
        SharedPreferences sharedPreferences=mContext.getSharedPreferences(SHARE_PREFRERENCE,0);
        String first_name=sharedPreferences.getString("first_name","");
        return first_name;
    }

    public String mGetSharePrefSingleData(String items)
    {
        SharedPreferences sharedPreferences=mContext.getSharedPreferences(SHARE_PREFRERENCE,0);
        return sharedPreferences.getString(items,"");
    }
    public String mGetLastName() {
        SharedPreferences sharedPreferences=mContext.getSharedPreferences(SHARE_PREFRERENCE,0);
        String last_name=sharedPreferences.getString("last_name","");
        return last_name;
    }
    public String mGetEmail() {
        SharedPreferences sharedPreferences=mContext.getSharedPreferences(SHARE_PREFRERENCE,0);
        return sharedPreferences.getString("email","");
    }
    public String mGetApiToken() {
        SharedPreferences sharedPreferences=mContext.getSharedPreferences(SHARE_PREFRERENCE,0);
        return sharedPreferences.getString("api_token","");
    }
    public String mGetIciciId() {
        SharedPreferences sharedPreferences=mContext.getSharedPreferences(SHARE_PREFRERENCE,0);
        return sharedPreferences.getString("icici_agent_id","");
    }
    public String mGetOutletId() {
        SharedPreferences sharedPreferences=mContext.getSharedPreferences(SHARE_PREFRERENCE,0);
        return sharedPreferences.getString("outlet_id","");
    }


    public String mGetUserBalance()
    {
        SharedPreferences sharedPreferences=mContext.getSharedPreferences(SHARE_PREFRERENCE,0);
        return sharedPreferences.getString("user_balance","");

    }
    public String mGetAeps_balance()
    {
        SharedPreferences sharedPreferences=mContext.getSharedPreferences(SHARE_PREFRERENCE,0);
        return sharedPreferences.getString("aeps_balance","");

    }
    public String mGetLienAmount(String lien_amount)
    {
        SharedPreferences sharedPreferences=mContext.getSharedPreferences(SHARE_PREFRERENCE,0);
        return sharedPreferences.getString("lien_amount","");

    }
    public void mSaveNoOfNotification(String total_notification)
    {
        SharedPreferences sharedPreferences=mContext.getSharedPreferences(SHARE_PREFRERENCE,0);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putString("total_notification",total_notification);
        editor.apply();
    }
    public String mGetNoOfNotification() {
        SharedPreferences sharedPreferences=mContext.getSharedPreferences(SHARE_PREFRERENCE,0);
        String total_notification=sharedPreferences.getString("total_notification","");
        return total_notification;
    }

    public void mSaveAllNotification(String notification)
    {
        SharedPreferences sharedPreferences=mContext.getSharedPreferences(SHARE_PREFRERENCE,0);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putString("notification",notification);
        editor.apply();
    }

    public String mGetName() {
        SharedPreferences sharedPreferences=mContext.getSharedPreferences(SHARE_PREFRERENCE,0);
        String notification=sharedPreferences.getString("first_name","")+" "+sharedPreferences.getString("last_name","");
        return notification;
    }
    public String mGetNews() {
        SharedPreferences sharedPreferences=mContext.getSharedPreferences(SHARE_PREFRERENCE,0);
        String notification=sharedPreferences.getString("news","");
        return notification;
    }
    public String mGetAllNotification() {
        SharedPreferences sharedPreferences=mContext.getSharedPreferences(SHARE_PREFRERENCE,0);
        String notification=sharedPreferences.getString("notification","");
        return notification;
    }


    public boolean mKycStatus()
    {
        SharedPreferences sharedPreferences=mContext.getSharedPreferences(SHARE_PREFRERENCE,0);
        String active=sharedPreferences.getString("kyc_status","");

        if (active.equalsIgnoreCase("1"))
        {
            return  true;
        }
        else
        {
            return  false;
        }
    }

    public boolean mVerifyMobile()
    {
        SharedPreferences sharedPreferences=mContext.getSharedPreferences(SHARE_PREFRERENCE,0);
        String mobile_verified=sharedPreferences.getString("mobile_verified","");

        if (mobile_verified.equalsIgnoreCase("1"))
        {
            return true;
        }
        else
        {
            return  false;
        }
    }

    public String mGetSessionId() {
        SharedPreferences sharedPreferences=mContext.getSharedPreferences(SHARE_PREFRERENCE,0);
        String session_id=sharedPreferences.getString("session_id","");
        return session_id;
    }
    public String mGetUsername() {
        SharedPreferences sharedPreferences=mContext.getSharedPreferences(SHARE_PREFRERENCE,0);
        String password=sharedPreferences.getString("username","");
        return password;
    }
    public String getPassword() {
        SharedPreferences sharedPreferences=mContext.getSharedPreferences(SHARE_PREFRERENCE,0);
        String password=sharedPreferences.getString("password","");
        return password;
    }
    public String mGetUserId() {
        SharedPreferences sharedPreferences=mContext.getSharedPreferences(SHARE_PREFRERENCE,0);
        String userid=sharedPreferences.getString("id","");
        return userid;
    }
    public String mGetBanners() {
        SharedPreferences sharedPreferences=mContext.getSharedPreferences(SHARE_PREFRERENCE,0);
        String banners=sharedPreferences.getString("banners","");
        return banners;
    }
    public void mSaveServicesName(String service) {
        SharedPreferences sharedPreferences=mContext.getSharedPreferences(SHARE_PREFRERENCE,0);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putString("services",service);
        editor.apply();

    }
    public String mGetServices() {
        SharedPreferences sharedPreferences=mContext.getSharedPreferences(SHARE_PREFRERENCE,0);
        String services=sharedPreferences.getString("services","");
        return services;

    }

    public void mSaveOperators(String service) {
        SharedPreferences sharedPreferences=mContext.getSharedPreferences(SHARE_PREFRERENCE,0);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putString("operator",service);
        editor.apply();

    }
    public String mGetOperators() {
        SharedPreferences sharedPreferences=mContext.getSharedPreferences(SHARE_PREFRERENCE,0);
        String operator=sharedPreferences.getString("operator","");
        return operator;

    }


    public String mGetStatusList() {
        SharedPreferences sharedPreferences=mContext.getSharedPreferences(SHARE_PREFRERENCE,0);
        String operator=sharedPreferences.getString("status_list","");
        return operator;

    }

    public void mSaveStatusList(String all_status)
    {
        SharedPreferences sharedPreferences=mContext.getSharedPreferences(SHARE_PREFRERENCE,0);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putString("status_list",all_status);
        editor.apply();
    }

    public boolean mGetMoney() {
//        SharedPreferences sharedPreferences=mContext.getSharedPreferences(SHARE_PREFRERENCE,0);
//        String money=sharedPreferences.getString("money","");
//        String money_two=sharedPreferences.getString("money_two","");

        if (mGetMoney1()||mGetMoney2())
        {
            return true;
        }
        else {
            return false;
        }
    }

    public boolean mGetMoney1() {
        SharedPreferences sharedPreferences=mContext.getSharedPreferences(SHARE_PREFRERENCE,0);
        String money=sharedPreferences.getString("money","");
        String money_two=sharedPreferences.getString("company_money","");

        if (money.equalsIgnoreCase("1")&&money_two.equalsIgnoreCase("1"))
        {
            return true;
        }
        else {
            return false;
        }
    }

    public boolean mGetMoney2() {
        SharedPreferences sharedPreferences=mContext.getSharedPreferences(SHARE_PREFRERENCE,0);
        String money=sharedPreferences.getString("money_two","");
        String money_two=sharedPreferences.getString("company_money_two","");

        if (money.equalsIgnoreCase("1")&&money_two.equalsIgnoreCase("1"))
        {
            return true;
        }
        else {
            return false;
        }
    }

    public boolean mGetPayout() {
        SharedPreferences sharedPreferences=mContext.getSharedPreferences(SHARE_PREFRERENCE,0);
        String payout=sharedPreferences.getString("payout","");

        if (payout.equalsIgnoreCase("1"))
        {
            return true;
        }
        else {
            return false;
        }
    }

    public boolean mGetAEPS() {
        SharedPreferences sharedPreferences=mContext.getSharedPreferences(SHARE_PREFRERENCE,0);
        String aeps=sharedPreferences.getString("aeps","");
        String company_aeps=sharedPreferences.getString("company_aeps","");

        if (aeps.equalsIgnoreCase("1")&&company_aeps.equalsIgnoreCase("1"))
        {
            return true;
        }
        else {
            return false;
        }
    }

    public boolean mGetRecharge() {
        SharedPreferences sharedPreferences=mContext.getSharedPreferences(SHARE_PREFRERENCE,0);
        String recharge=sharedPreferences.getString("recharge","");
        String company_recharge=sharedPreferences.getString("company_recharge","");

        if (recharge.equalsIgnoreCase("1")&&company_recharge.equalsIgnoreCase("1"))
        {
            return true;
        }
        else {
            return false;
        }

    }
    public boolean mGetPanCard() {
        SharedPreferences sharedPreferences=mContext.getSharedPreferences(SHARE_PREFRERENCE,0);
        String pancard=sharedPreferences.getString("pancard","");

        if (pancard.equalsIgnoreCase("1"))
        {
            return true;
        }
        else {
            return false;
        }
    }


    public boolean mGetCompanyMoney1()
    {
        SharedPreferences sharedPreferences=mContext.getSharedPreferences(SHARE_PREFRERENCE,0);
        String money1=sharedPreferences.getString("company_money","");

        if (money1.equalsIgnoreCase("0"))
        {
            return false;
        }
        else
        {
            return true;
        }

    }
    public boolean mGetCompanyMoney2()
    {
        SharedPreferences sharedPreferences=mContext.getSharedPreferences(SHARE_PREFRERENCE,0);
        String money2=sharedPreferences.getString("company_money2","");

        if (money2.equalsIgnoreCase("0"))
        {
            return false;
        }
        else
        {
            return true;
        }
    }

    public String mGetSingleData(String key)
    {
        return mContext.getSharedPreferences(SHARE_PREFRERENCE, 0).getString(key, "");
    }


    public String mGetCompanyName() {
        SharedPreferences sharedPreferences=mContext.getSharedPreferences(SHARE_PREFRERENCE,0);
        return sharedPreferences.getString("company_name","");
    }
    public String mGetCompanyEmail() {
        SharedPreferences sharedPreferences=mContext.getSharedPreferences(SHARE_PREFRERENCE,0);
        return sharedPreferences.getString("company_email","");
    }
    public String mGetCompanyAddress1() {
        SharedPreferences sharedPreferences=mContext.getSharedPreferences(SHARE_PREFRERENCE,0);
        return sharedPreferences.getString("company_address","");
    }
    public String mGetCompanyAddress2() {
        SharedPreferences sharedPreferences=mContext.getSharedPreferences(SHARE_PREFRERENCE,0);
        return sharedPreferences.getString("company_address_two","");
    }
    public String mGetSupportNumber() {
        SharedPreferences sharedPreferences=mContext.getSharedPreferences(SHARE_PREFRERENCE,0);
        return sharedPreferences.getString("support_number","");
    }
    public String mGetChatNumber() {
        SharedPreferences sharedPreferences=mContext.getSharedPreferences(SHARE_PREFRERENCE,0);
        return sharedPreferences.getString("whatsapp_number","");
    }
    public String mGetWebSite() {
        SharedPreferences sharedPreferences=mContext.getSharedPreferences(SHARE_PREFRERENCE,0);
        return sharedPreferences.getString("company_website","");
    }


//    to get data with post method

}
