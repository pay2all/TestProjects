package com.demo.apppay2all.ProfileDetail

import com.demo.apppay2all.BaseURL.BaseURL
import com.demo.apppay2all.CallResAPIPOSTMethod
import com.demo.apppay2all.DetectConnection
import com.demo.apppay2all.R
import com.demo.apppay2all.SharePrefManager
import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.demo.apppay2all.FormDetails.KYCNewDesign
import de.hdodenhof.circleimageview.CircleImageView
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

class ProfileViewNew : AppCompatActivity() {

    lateinit var iv_profile : CircleImageView
    lateinit var tv_name : TextView
    lateinit var tv_email : TextView
    lateinit var tv_mobile : TextView
    lateinit var tv_join_date : TextView
    lateinit var tv_member_type : TextView
    lateinit var tv_first_name : TextView
    lateinit var tv_last_name : TextView
    lateinit var tv_name_email : TextView
    lateinit var tv_name_mobile : TextView
    lateinit var tv_address : TextView
    lateinit var tv_state : TextView
    lateinit var tv_district : TextView
    lateinit var tv_city : TextView
    lateinit var tv_pincode : TextView

    lateinit var tv_p_address : TextView
    lateinit var tv_p_state : TextView
    lateinit var tv_p_district : TextView
    lateinit var tv_p_city : TextView
    lateinit var tv_p_pincode : TextView

    lateinit var tv_shop_name : TextView
    lateinit var tv_shop_address : TextView


    lateinit var button_update : TextView

    var state_id = ""
    var district_id:kotlin.String? = ""
    var state_present_id:kotlin.String? = ""
    var district_present_id:kotlin.String? = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_view_new)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        iv_profile=findViewById(R.id.iv_profile)
        if (!SharePrefManager.getInstance(this).mGetSharePrefSingleData("profile_photo").equals(""))
        {
            Glide.with(this).load(SharePrefManager.getInstance(this).mGetSharePrefSingleData("profile_photo")).into(iv_profile)
        }

        tv_name=findViewById(R.id.tv_name)
        tv_name.setText(SharePrefManager.getInstance(this).mGetSharePrefSingleData("first_name")+" "+SharePrefManager.getInstance(this).mGetSharePrefSingleData("last_name"))

        tv_email=findViewById(R.id.tv_email)
        tv_email.setText(SharePrefManager.getInstance(this).mGetSharePrefSingleData("email"))


        tv_mobile=findViewById(R.id.tv_mobile)
        tv_mobile.setText(SharePrefManager.getInstance(this).mGetSharePrefSingleData("mobile"))


        tv_join_date=findViewById(R.id.tv_join_date)
        tv_join_date.setText(SharePrefManager.getInstance(this).mGetSharePrefSingleData("joing_date"))


        tv_member_type=findViewById(R.id.tv_member_type)
        tv_member_type.setText(SharePrefManager.getInstance(this).mGetSharePrefSingleData("role_id"))


        tv_first_name=findViewById(R.id.tv_first_name)
        tv_first_name.setText(SharePrefManager.getInstance(this).mGetSharePrefSingleData("first_name"))


        tv_last_name=findViewById(R.id.tv_last_name)
        tv_last_name.setText(SharePrefManager.getInstance(this).mGetSharePrefSingleData("last_name"))


        tv_name_email=findViewById(R.id.tv_name_email)
        tv_name_email.setText(SharePrefManager.getInstance(this).mGetSharePrefSingleData("email"))


        tv_name_mobile=findViewById(R.id.tv_name_mobile)
        tv_name_mobile.setText(SharePrefManager.getInstance(this).mGetSharePrefSingleData("mobile"))



        tv_address=findViewById(R.id.tv_address)
        tv_address.setText(SharePrefManager.getInstance(this).mGetSharePrefSingleData("permanent_address"))


        tv_state=findViewById(R.id.tv_state)
        state_id=SharePrefManager.getInstance(this).mGetSharePrefSingleData("permanent_state")


        tv_district=findViewById(R.id.tv_district)
        district_id=SharePrefManager.getInstance(this).mGetSharePrefSingleData("permanent_district")



        tv_city=findViewById(R.id.tv_city)
        tv_city.setText(SharePrefManager.getInstance(this).mGetSharePrefSingleData("permanent_city"))


        tv_pincode=findViewById(R.id.tv_pincode)
        tv_pincode.setText(SharePrefManager.getInstance(this).mGetSharePrefSingleData("permanent_pin_code"))


        tv_p_address=findViewById(R.id.tv_p_address)
        tv_p_address.setText(SharePrefManager.getInstance(this).mGetSharePrefSingleData("present_address"))


        tv_p_state=findViewById(R.id.tv_p_state)
        state_present_id=SharePrefManager.getInstance(this).mGetSharePrefSingleData("present_state")



        tv_p_district=findViewById(R.id.tv_p_district)
        district_present_id=SharePrefManager.getInstance(this).mGetSharePrefSingleData("present_district")



        tv_p_city=findViewById(R.id.tv_p_city)
        tv_p_city.setText(SharePrefManager.getInstance(this).mGetSharePrefSingleData("present_city"))


        tv_p_pincode=findViewById(R.id.tv_p_pincode)
        tv_p_pincode.setText(SharePrefManager.getInstance(this).mGetSharePrefSingleData("present_pin_code"))


        tv_shop_name=findViewById(R.id.tv_shop_name)
        tv_shop_name.setText(SharePrefManager.getInstance(this).mGetSharePrefSingleData("shop_name"))


        tv_shop_address=findViewById(R.id.tv_shop_address)
        tv_shop_address.setText(SharePrefManager.getInstance(this).mGetSharePrefSingleData("office_address"))



        button_update=findViewById(R.id.button_update)
        button_update.setOnClickListener {
            startActivity(Intent(this,KYCNewDesign::class.java))
            finish()
        }

        if (state_id != "") {
            if (DetectConnection.checkInternetConnection(this@ProfileViewNew)) {
                mGetStateDetail()
            }
        }


    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId==android.R.id.home)
        {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }


    @SuppressLint("StaticFieldLeak")
    private fun mGetStateDetail() {
        val builder = Uri.Builder()
        object : CallResAPIPOSTMethod(
            this@ProfileViewNew,
            builder,
            BaseURL.BASEURL_B2C + "api/application/v1/state-list",
            false,
            "POST"
        ) {
             override fun onPreExecute() {
                super.onPreExecute()
            }

             override fun onPostExecute(s: String) {
                super.onPostExecute(s)
                Log.e("response", "data $s")
                mShowStateDetails(s)
            }
        }.execute()
    }


    fun mShowStateDetails(banklist: String?) {
        var status = ""
        val jsonArray: JSONArray
        try {
            val jsonObject = JSONObject(banklist)
            if (jsonObject.has("status")) {
                status = jsonObject.getString("status")
            }
            if (status.equals("success", ignoreCase = true)) {
                jsonArray = jsonObject.getJSONArray("state_list")
                for (i in 0 until jsonArray.length()) {
                    val data = jsonArray.getJSONObject(i)
                    Log.e("check", "data $data")
                    if (state_id.equals(
                            data.getString("state_id"),
                            ignoreCase = true
                        )
                    ) {
                        tv_state.text = data.getString("state_name")

                        val jsonArray1 = data.getJSONArray("district_list")
                        for (j in 0 until jsonArray1.length()) {
                            val data1 = jsonArray1.getJSONObject(j)
                            if (district_id.equals(data1.getString("district_id"))) {
                                tv_district.text = data1.getString("district_name")
                            }
                        }
                    }
                    if (state_present_id.equals(
                            data.getString("state_id"),
                            ignoreCase = true
                        )
                    ) {
                        val jsonArray1 = data.getJSONArray("district_list")
                        tv_p_state.text = data.getString("state_name")
                        for (j in 0 until jsonArray1.length()) {
                            val data1 = jsonArray1.getJSONObject(j)

                            if (district_present_id.equals(data1.getString("district_id"))) {
                                tv_p_district.text = data1.getString("district_name")
                            }

                        }
                    }
                }
            }
        } catch (e: JSONException) {
            e.printStackTrace()
        }
    }
}