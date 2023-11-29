package com.demo.apppay2all

import android.annotation.SuppressLint
import android.content.Context

@SuppressLint("StaticFieldLeak")
class SharePrefManagerKotlin {

    private val SHARE_PREFERENCE = "user"

    private lateinit var mInstance: SharePrefManagerKotlin

    private var mContext: Context

    constructor(context: Context)
    {
        mContext=context;
    }

    @Synchronized
    fun getInstance(context: Context): SharePrefManagerKotlin {
        if (mInstance == null) {
            mInstance = SharePrefManagerKotlin(context)
        }
        return mInstance
    }

//    init {
//        Log.e("kotlin", "singleton initialized")
//    }


    fun mSaveUserData(
        username: String?,
        password: String?,
        first_name: String?,
        last_name: String?,
        email: String?,
        mobile: String?,
        role_id: String?,
        scheme_id: String?,
        joing_date: String?,
        permanent_address: String?,
        permanent_city: String?,
        permanent_district: String?,
        permanent_pin_code: String?,
        permanent_state: String?,
        present_address: String?,
        present_city: String?,
        present_district: String?,
        present_pin_code: String?,
        present_state: String?,
        lien_amount: String?,
        office_address: String?,
        call_back_url: String?,
        profile_photo: String?,
        shop_name: String?,
        shop_photo: String?,
        gst_regisration_photo: String?,
        pancard_photo: String?,
        cancel_cheque: String?,
        address_proof: String?,
        kyc_status: String?,
        kyc_remark: String?,
        mobile_verified: String?,
        lock_amount: String?,
        session_id: String?,
        active: String?,
        reason: String?,
        api_token: String?,
        user_balance: String?,
        aeps_balance: String?,
        recharge: String?,
        money: String?,
        aeps: String?,
        payout: String?,
        pancard: String?,
        ecommerce: String?,
        company_name: String?,
        company_email: String?,
        company_address: String?,
        company_address_two: String?,
        support_number: String?,
        whatsapp_number: String?,
        company_logo: String?,
        company_website: String?,
        news: String?,
        update_one: String?,
        update_two: String?,
        update_three: String?,
        update_for: String?,
        sender_id: String?,
        company_recharge: String?,
        company_money: String?,
        company_aeps: String?,
        company_payout: String?,
        view_plan: String?,
        company_pancard: String?,
        company_ecommerce: String?,
        banners: String?,
        icici_agent_id: String?,
        outlet_id: String?
    ) {
        val sharedPreferences = mContext.getSharedPreferences(SHARE_PREFERENCE, 0)
        val editor = sharedPreferences.edit()
        editor.putString("username", username)
        editor.putString("password", password)
        editor.putString("first_name", first_name)
        editor.putString("last_name", last_name)
        editor.putString("mobile", mobile)
        editor.putString("email", email)
        editor.putString("role_id", role_id)
        editor.putString("scheme_id", scheme_id)
        editor.putString("joing_date", joing_date)
        editor.putString("permanent_address", permanent_address)
        editor.putString("permanent_city", permanent_city)
        editor.putString("permanent_district", permanent_district)
        editor.putString("permanent_pin_code", permanent_pin_code)
        editor.putString("permanent_state", permanent_state)
        editor.putString("permanent_state", permanent_state)
        editor.putString("present_address", present_address)
        editor.putString("present_district", present_district)
        editor.putString("present_city", present_city)
        editor.putString("present_pin_code", present_pin_code)
        editor.putString("present_state", present_state)
        editor.putString("lien_amount", lien_amount)
        editor.putString("office_address", office_address)
        editor.putString("call_back_url", call_back_url)
        editor.putString("profile_photo", profile_photo)
        editor.putString("shop_name", shop_name)
        editor.putString("shop_photo", shop_photo)
        editor.putString("gst_regisration_photo", gst_regisration_photo)
        editor.putString("pancard_photo", pancard_photo)
        editor.putString("cancel_cheque", cancel_cheque)
        editor.putString("address_proof", address_proof)
        editor.putString("kyc_status", kyc_status)
        editor.putString("kyc_remark", kyc_remark)
        editor.putString("mobile_verified", mobile_verified)
        editor.putString("lock_amount", lock_amount)
        editor.putString("session_id", session_id)
        editor.putString("active", active)
        editor.putString("reason", reason)
        editor.putString("api_token", api_token)
        editor.putString("user_balance", user_balance)
        editor.putString("aeps_balance", aeps_balance)
        editor.putString("recharge", recharge)
        editor.putString("money", money)
        editor.putString("aeps", aeps)
        editor.putString("payout", payout)
        editor.putString("pancard", pancard)
        editor.putString("ecommerce", ecommerce)
        editor.putString("company_name", company_name)
        editor.putString("company_email", company_email)
        editor.putString("company_address", company_address)
        editor.putString("company_address_two", company_address_two)
        editor.putString("support_number", support_number)
        editor.putString("whatsapp_number", whatsapp_number)
        editor.putString("company_logo", company_logo)
        editor.putString("company_website", company_website)
        editor.putString("news", news)
        editor.putString("update_one", update_one)
        editor.putString("update_two", update_two)
        editor.putString("update_three", update_three)
        editor.putString("update_for", update_for)
        editor.putString("sender_id", sender_id)
        editor.putString("company_recharge", company_recharge)
        editor.putString("company_money", company_money)
        editor.putString("company_aeps", company_aeps)
        editor.putString("company_pancard", company_pancard)
        editor.putString("company_payout", company_payout)
        editor.putString("view_plan", view_plan)
        editor.putString("company_ecommerce", company_ecommerce)
        editor.putString("banners", banners)
        editor.putString("icici_agent_id", icici_agent_id)
        editor.putString("outlet_id", outlet_id)
        editor.apply()
    }

}