package com.demo.apppay2all

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.text.Html
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import com.bumptech.glide.Glide
import com.demo.apppay2all.AddMemberDetails.AddMember
import com.demo.apppay2all.AllReportsDetails.AllReports
import com.demo.apppay2all.AllTransactionDetail.AllTransaction
import com.demo.apppay2all.AppSettings.Settings
import com.demo.apppay2all.BaseURL.BaseURL
import com.demo.apppay2all.CommissionDetail.CommissionDetails
import com.demo.apppay2all.FundRequesDetails.FundRequestList
import com.demo.apppay2all.MemberDetail.MemberList
import com.demo.apppay2all.MoneyTransfer1.SenderActivity
import com.demo.apppay2all.MoneyTransfer2.SenderDetailActivity
import com.demo.apppay2all.MoneyTransferDetails.MoneyDetails
import com.demo.apppay2all.PayoutServices.Payout
import com.demo.apppay2all.ServicesDetails.ServicesCardAdapter
import com.demo.apppay2all.ServicesDetails.ServicesItems
import com.pay2all.aeps.AEPS_Service
import com.pay2all.microatm.MicroATMLaunch
import com.viewpagerindicator.CirclePageIndicator
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.util.*
import kotlin.collections.ArrayList

class MainActivitySingle : AppCompatActivity() {

    lateinit var textview_gud_evening : TextView
    lateinit var textview_name : TextView
    lateinit var textview_main_balance : TextView
    lateinit var textview_aeps_balance : TextView
    lateinit var textview_aeps : TextView
    lateinit var textView_news : TextView
    lateinit var tv_add_money : TextView

    lateinit var cardview_aeps: CardView
    lateinit var cardview_money: CardView
    lateinit var cardview_recharge: CardView
    lateinit var cardview_travel: CardView

    lateinit var ll_bus: LinearLayout
    lateinit var ll_train: LinearLayout
    lateinit var ll_flight: LinearLayout
    lateinit var ll_hotel: LinearLayout

    lateinit var viewpager_banners : ViewPager
    private var currentPage_banners = 0
    private var NUM_PAGES_banner = 0
    lateinit var swipeTimer_banners: Timer
    lateinit var imageId: ArrayList<String>
    var calendar = Calendar.getInstance()

    lateinit var  username: String
    lateinit var password: String

    lateinit var ll_money_transfer2: LinearLayout
    lateinit var tv_dmt_2_title: TextView

    lateinit var ll_enquiry: LinearLayout
    lateinit var ll_history: LinearLayout
    lateinit var ll_money_transfer: LinearLayout
    lateinit var ll_add_money: LinearLayout
    lateinit var ll_setting: LinearLayout
    lateinit var ll_dmt_report: LinearLayout
    lateinit var ll_payout: LinearLayout
    lateinit var ll_pan_card: LinearLayout
    lateinit var ll_withdrwal: LinearLayout
    lateinit var ll_mini_statement: LinearLayout
    lateinit var ll_aadhaar_pay: LinearLayout
    lateinit var ll_balance: LinearLayout
    lateinit var ll_account_statement: LinearLayout

    lateinit var name: String
    lateinit var balance: String
    lateinit var aeps_balance: String
    lateinit var image: String

    lateinit var ll_user: LinearLayout
    lateinit var ll_distributer: LinearLayout

    lateinit var ll_add_member: LinearLayout
    lateinit var ll_fund_transfer: LinearLayout
    lateinit var ll_commission: LinearLayout

    var doubleBackToExitPressedOnce = false

    lateinit var recyclerview_services: RecyclerView
    lateinit var servicesCardAdapter: ServicesCardAdapter

    lateinit var servicesItems: ArrayList<ServicesItems>

    lateinit var dialog : ProgressDialog

    lateinit var ll_matm_enquiry : LinearLayout
    lateinit var ll_matm_withdrwal : LinearLayout
    lateinit var cardview_atm : CardView

    lateinit var vw_line : View
    lateinit var ll_aeps_balance : LinearLayout

    lateinit var alertDialog : AlertDialog


    lateinit var rv_money : RecyclerView
    lateinit var servicesCardAdapter_money: ServicesCardAdapter
    lateinit var servicesItems_money: ArrayList<ServicesItems>

    lateinit var rv_matm : RecyclerView
    lateinit var servicesCardAdapter_atm: ServicesCardAdapter
    lateinit var servicesItems_atm: ArrayList<ServicesItems>

    lateinit var rv_aeps : RecyclerView
    lateinit var servicesCardAdapter_aeps: ServicesCardAdapter
    lateinit var servicesItems_aeps: ArrayList<ServicesItems>

    lateinit var rv_travel : RecyclerView
    lateinit var servicesCardAdapter_travel: ServicesCardAdapter
    lateinit var servicesItems_travel: ArrayList<ServicesItems>

    lateinit var rv_distributer : RecyclerView
    lateinit var servicesCardAdapter_distributer: ServicesCardAdapter
    lateinit var servicesItems_distributer: ArrayList<ServicesItems>

    lateinit var allserv : LinkedHashMap<Int,String>

    lateinit var rl_footer : RelativeLayout

    var mIsBroadcastShow = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_single)

        supportActionBar?.hide()

        textview_aeps=findViewById(R.id.textview_aeps)
        rl_footer=findViewById(R.id.rl_footer)
//        if (!SharePrefManager.getInstance(this@MainActivitySingle).mGetSingleData("color_end").equals(""))
//        {
//            rl_footer.setBackgroundColor(Color.parseColor(SharePrefManager.getInstance(this@MainActivitySingle).mGetSingleData("color_end")))
//        }

        rv_money=findViewById(R.id.rv_money)
        rv_money.layoutManager=GridLayoutManager(this,3)
        servicesItems_money = ArrayList<ServicesItems>()
        servicesCardAdapter_money = ServicesCardAdapter(this@MainActivitySingle, servicesItems_money)
        rv_money.setAdapter(servicesCardAdapter_money)
        mShowMoney()

        rv_matm=findViewById(R.id.rv_matm)
        rv_matm.layoutManager=GridLayoutManager(this,2)
        servicesItems_atm = ArrayList<ServicesItems>()
        servicesCardAdapter_atm = ServicesCardAdapter(this@MainActivitySingle, servicesItems_atm)
        rv_matm.setAdapter(servicesCardAdapter_atm)
        mShowATM()

        rv_aeps=findViewById(R.id.rv_aeps)
        rv_aeps.layoutManager=GridLayoutManager(this,3)
        servicesItems_aeps = ArrayList<ServicesItems>()
        servicesCardAdapter_aeps = ServicesCardAdapter(this@MainActivitySingle, servicesItems_aeps)
        rv_aeps.setAdapter(servicesCardAdapter_aeps)
        mShowAeps()

        rv_travel=findViewById(R.id.rv_travel)
        rv_travel.layoutManager=GridLayoutManager(this,3)
        servicesItems_travel = ArrayList<ServicesItems>()
        servicesCardAdapter_travel = ServicesCardAdapter(this@MainActivitySingle, servicesItems_travel)
        rv_travel.setAdapter(servicesCardAdapter_travel)
        mShowTravel()

        rv_distributer=findViewById(R.id.rv_distributer)
        rv_distributer.layoutManager=GridLayoutManager(this,3)
        servicesItems_distributer = ArrayList<ServicesItems>()
        servicesCardAdapter_distributer = ServicesCardAdapter(this@MainActivitySingle, servicesItems_distributer)
        rv_distributer.setAdapter(servicesCardAdapter_distributer)
        mShowDistributer()

        vw_line=findViewById(R.id.vw_line)
        ll_aeps_balance=findViewById(R.id.ll_aeps_balance)

        cardview_atm=findViewById(R.id.cardview_atm)
        if (!SharePrefManager.getInstance(this@MainActivitySingle).mGetAEPS())
        {
            cardview_atm.visibility=View.GONE
        }
        else
        {
            cardview_atm.visibility=View.VISIBLE
        }

        ll_matm_enquiry=findViewById(R.id.ll_matm_enquiry)
        ll_matm_enquiry.setOnClickListener {
            if (DetectConnection.checkInternetConnection(this@MainActivitySingle)) {
                mGetOutletIdForMatm("be", "Balance Enquiry")
            } else {
                Toast.makeText(this@MainActivitySingle, "No internet connection", Toast.LENGTH_SHORT).show()
            }
        }

        ll_matm_withdrwal=findViewById(R.id.ll_matm_withdrwal)
        ll_matm_withdrwal.setOnClickListener {
            if (DetectConnection.checkInternetConnection(this@MainActivitySingle)) {
                mGetOutletIdForMatm("cw", "Cash Withdrawal")
            } else {
                Toast.makeText(this@MainActivitySingle, "No Internet connection", Toast.LENGTH_SHORT).show()
            }
        }

        ll_user=findViewById(R.id.ll_user)
        ll_distributer=findViewById(R.id.ll_distributer)
        ll_add_member=findViewById(R.id.ll_add_member)
        ll_add_member.setOnClickListener {
            startActivity(Intent (this@MainActivitySingle,AddMember::class.java))
        }

        ll_fund_transfer=findViewById(R.id.ll_fund_transfer)
        ll_fund_transfer.setOnClickListener {
            startActivity(Intent (this@MainActivitySingle,MemberList::class.java))
        }

        ll_account_statement=findViewById(R.id.ll_account_statement)
        ll_account_statement.setOnClickListener {
            startActivity(Intent (this@MainActivitySingle,AllTransaction::class.java))
        }

        if (SharePrefManager.getInstance(this).mGetRoleId()<8)
        {
            ll_user.visibility=View.GONE
            ll_distributer.visibility=View.VISIBLE
        }
        else
        {
            ll_user.visibility=View.VISIBLE
            ll_distributer.visibility=View.GONE
        }

        ll_commission=findViewById(R.id.ll_commission)
        ll_commission.setOnClickListener {
            startActivity(Intent (this@MainActivitySingle,CommissionDetails::class.java))
        }

        password=SharePrefManager.getInstance(this@MainActivitySingle).password

        Log.e("pass",password)

        recyclerview_services = findViewById(R.id.recyclerview_services)
        recyclerview_services.layoutManager=GridLayoutManager(this,3)
        servicesItems = ArrayList<ServicesItems>()
        servicesCardAdapter = ServicesCardAdapter(this@MainActivitySingle, servicesItems)
        recyclerview_services.setAdapter(servicesCardAdapter)

        swipeTimer_banners = Timer()

//        news=SharePrefManager.getInstance(this@MainActivitySingle).mGetNews()
//        textView_news=findViewById(R.id.textView_news)
//        mShowNews(news)

        tv_add_money=findViewById(R.id.tv_add_money)
        tv_add_money.setOnClickListener {
            startActivity(Intent(this@MainActivitySingle,FundRequestList::class.java))
        }

        ll_add_money=findViewById(R.id.ll_add_money)
        ll_add_money.setOnClickListener {
            startActivity(Intent(this@MainActivitySingle,FundRequestList::class.java))
        }

        username = SharePrefManager.getInstance(this@MainActivitySingle).mGetUsername()

        name = SharePrefManager.getInstance(this@MainActivitySingle).mGetName()

        balance = SharePrefManager.getInstance(this@MainActivitySingle).mGetMainBalance()

        aeps_balance = SharePrefManager.getInstance(this@MainActivitySingle).mGetAEPSBalance()

        image = SharePrefManager.getInstance(this@MainActivitySingle).mGetSharePrefSingleData("profile_photo")

//        for check time
        var  houroftheday=calendar.get(Calendar.HOUR_OF_DAY)
        textview_gud_evening=findViewById(R.id.textview_gud_evening)


        if (((houroftheday>3)||houroftheday==3)&&(houroftheday<12))
        {
            textview_gud_evening.setText("Good Morning")
        }
        else if (((houroftheday>12)||houroftheday==12)&&(houroftheday<15))
        {
            textview_gud_evening.setText("Good Afternoon")
        }
        else if (((houroftheday>15)||houroftheday==15)&&(houroftheday<20))
        {
            textview_gud_evening.setText("Good Evening")
        }
        else
        {
            textview_gud_evening.setText("Good Night")
        }

        textview_name=findViewById(R.id.textview_name)
        textview_name.setText(SharePrefManager.getInstance(this@MainActivitySingle).mGetName())

        textview_main_balance=findViewById(R.id.textview_main_balance)
        textview_aeps_balance=findViewById(R.id.textview_aeps_balance)
        textview_main_balance.setText(resources.getString(R.string.rs) + " " + SharePrefManager.getInstance(this@MainActivitySingle).mGetMainBalance())

        textview_aeps_balance.setText(resources.getString(R.string.rs) + " " + SharePrefManager.getInstance(this@MainActivitySingle).mGetAEPSBalance())

        ll_balance=findViewById(R.id.ll_balance)
        ll_balance.setOnClickListener {
            if (SharePrefManager.getInstance(this@MainActivitySingle).mGetPayout())
            {
//                startActivity(Intent(this@MainActivitySingle, AvailableBalance::class.java))
            }
            else
            {
                Toast.makeText(applicationContext, "Service not activated, please contact to admin for same", Toast.LENGTH_SHORT).show()
            }
        }

        cardview_aeps=findViewById(R.id.cardview_aeps)
        if (!SharePrefManager.getInstance(this@MainActivitySingle).mGetAEPS())
        {
            cardview_aeps.visibility=View.GONE
            vw_line.visibility=View.GONE
            ll_aeps_balance.visibility=View.GONE
        }

        var aeps_title="";

        if (SharePrefManager.getInstance(this).mGetAEPS()&&SharePrefManager.getInstance(this).mGetPayout()&&SharePrefManager.getInstance(this).mGetPanCard())
        {
            aeps_title="AEPS, Payout and PAN Card"
        }

        else if (SharePrefManager.getInstance(this).mGetAEPS()&&!SharePrefManager.getInstance(this).mGetPayout()&&SharePrefManager.getInstance(this).mGetPanCard())
        {
            aeps_title="AEPS and PAN Card"
        }

        else if (SharePrefManager.getInstance(this).mGetAEPS()&&SharePrefManager.getInstance(this).mGetPayout()&&!SharePrefManager.getInstance(this).mGetPanCard())
        {
            aeps_title="AEPS and Payout"
        }
        else if (!SharePrefManager.getInstance(this).mGetAEPS()&&!SharePrefManager.getInstance(this).mGetPayout()&&SharePrefManager.getInstance(this).mGetPanCard())
        {
            aeps_title="PAN Card"
        }
        else if (!SharePrefManager.getInstance(this).mGetAEPS()&&SharePrefManager.getInstance(this).mGetPayout()&&SharePrefManager.getInstance(this).mGetPanCard())
        {
            aeps_title="AEPS"
        }

        textview_aeps.setText(aeps_title)

        if (SharePrefManager.getInstance(this@MainActivitySingle).mGetAEPS()||SharePrefManager.getInstance(this@MainActivitySingle).mGetPayout()||SharePrefManager.getInstance(this@MainActivitySingle).mGetPanCard())
        {
            cardview_aeps.visibility=View.VISIBLE
        }
        else
        {
            cardview_aeps.visibility=View.GONE
        }

        cardview_recharge=findViewById(R.id.cardview_recharge)
        if (SharePrefManager.getInstance(this@MainActivitySingle).mGetRecharge())
        {
            cardview_recharge.visibility=View.VISIBLE
        }
        else
        {
            cardview_recharge.visibility=View.GONE
        }

        cardview_money=findViewById(R.id.cardview_money)
        if (!SharePrefManager.getInstance(this@MainActivitySingle).mGetMoney())
        {
            cardview_money.visibility=View.GONE
        }


        cardview_travel=findViewById(R.id.cardview_travel)
        cardview_travel.visibility = View.GONE
//        if (!SharePrefManager.getInstance(this@MainActivitySingle).mGetTravel())
//        {
//            cardview_travel.visibility=View.GONE
//        }

        ll_bus=findViewById(R.id.ll_bus)
        ll_bus.setOnClickListener {

            startActivity(Intent(this@MainActivitySingle, CommingSoonActivity::class.java))
        }

        ll_train=findViewById(R.id.ll_train)
        ll_train.setOnClickListener {

            startActivity(Intent(this@MainActivitySingle, CommingSoonActivity::class.java))
        }

        ll_flight=findViewById(R.id.ll_flight)
        ll_flight.setOnClickListener {

            startActivity(Intent(this@MainActivitySingle, CommingSoonActivity::class.java))
        }

        ll_hotel=findViewById(R.id.ll_hotel)
        ll_hotel.setOnClickListener {

            startActivity(Intent(this@MainActivitySingle, CommingSoonActivity::class.java))
        }

        ll_money_transfer2=findViewById(R.id.ll_money_transfer2)
        tv_dmt_2_title=findViewById(R.id.tv_dmt_2_title)

        ll_money_transfer2.setOnClickListener {
            startActivity(Intent(this@MainActivitySingle, SenderDetailActivity::class.java))
        }

        ll_enquiry=findViewById(R.id.ll_enquiry)
        ll_enquiry.setOnClickListener {

            if (SharePrefManager.getInstance(this@MainActivitySingle).mGetAEPS())
            {
                mGetOutletId("be")
            }
            else
            {
                Toast.makeText(applicationContext, "Service not activated, please contact to admin for same", Toast.LENGTH_SHORT).show()
            }
        }

        ll_withdrwal=findViewById(R.id.ll_withdrwal)
        ll_withdrwal.setOnClickListener {

            if (SharePrefManager.getInstance(this@MainActivitySingle).mGetAEPS())
            {
                mGetOutletId("cw")
            }
            else
            {
                Toast.makeText(applicationContext, "Service not activated, please contact to admin for same", Toast.LENGTH_SHORT).show()
            }
        }

        ll_mini_statement=findViewById(R.id.ll_mini_statement)
        ll_mini_statement.setOnClickListener {

            if (SharePrefManager.getInstance(this@MainActivitySingle).mGetAEPS())
            {
                mGetOutletId("mst")
            }
            else
            {
                Toast.makeText(applicationContext, "Service not activated, please contact to admin for same", Toast.LENGTH_SHORT).show()
            }
        }

        ll_aadhaar_pay=findViewById(R.id.ll_aadhaar_pay)
        ll_aadhaar_pay.setOnClickListener {
            if (SharePrefManager.getInstance(this@MainActivitySingle).mGetAEPS())
            {
                mGetOutletId("ap")
            }
            else
            {
                Toast.makeText(applicationContext, "Service not activated, please contact to admin for same", Toast.LENGTH_SHORT).show()
            }
        }

        ll_money_transfer=findViewById(R.id.ll_money_transfer)
        ll_money_transfer.setOnClickListener {
            if (SharePrefManager.getInstance(this@MainActivitySingle).mGetCompanyMoney1())
            {
            startActivity(Intent(this@MainActivitySingle, SenderActivity::class.java))
            }
            else
            {
                Toast.makeText(applicationContext, "Service not activated, please contact to admin for same", Toast.LENGTH_SHORT).show()
            }
        }


        ll_dmt_report=findViewById(R.id.ll_dmt_report)

        if (SharePrefManager.getInstance(this@MainActivitySingle).mGetMoney1()&&SharePrefManager.getInstance(this@MainActivitySingle).mGetMoney2())
        {
            ll_money_transfer.visibility = View.VISIBLE
            ll_money_transfer2.visibility = View.VISIBLE
            ll_dmt_report.visibility=View.GONE
        }
        else if (!SharePrefManager.getInstance(this@MainActivitySingle).mGetMoney1()&&SharePrefManager.getInstance(this@MainActivitySingle).mGetMoney2())
        {
            ll_money_transfer.visibility = View.GONE
            ll_money_transfer2.visibility = View.VISIBLE
            tv_dmt_2_title.setText("Bank Transfer")
            ll_dmt_report.visibility=View.VISIBLE
        }
        else if (SharePrefManager.getInstance(this@MainActivitySingle).mGetMoney1()&&(!SharePrefManager.getInstance(this@MainActivitySingle).mGetMoney2()))
        {
            ll_money_transfer.visibility = View.VISIBLE
            ll_money_transfer2.visibility = View.GONE
            ll_dmt_report.visibility=View.VISIBLE
        }

        ll_dmt_report.setOnClickListener {
            startActivity(Intent(this@MainActivitySingle, MoneyDetails::class.java))
//            finish()
        }

        ll_payout=findViewById(R.id.ll_payout)
        ll_payout.setOnClickListener {
            if (SharePrefManager.getInstance(this@MainActivitySingle).mGetPayout())
            {
            startActivity(Intent(this@MainActivitySingle, Payout::class.java))
            }
            else
            {
                Toast.makeText(applicationContext, "Service not activated, please contact to admin for same", Toast.LENGTH_SHORT).show()
            }
        }

        ll_pan_card=findViewById(R.id.ll_pan_card)
        ll_pan_card.setOnClickListener {
            if (SharePrefManager.getInstance(this@MainActivitySingle).mGetPanCard())
            {
            startActivity(Intent(this@MainActivitySingle, PanCard::class.java))
            }
            else
            {
                Toast.makeText(applicationContext, "Service not activated, please contact to admin for same", Toast.LENGTH_SHORT).show()
            }
        }

        ll_history=findViewById(R.id.ll_history)
        ll_history.setOnClickListener {
            startActivity(Intent(this@MainActivitySingle, AllReports::class.java))
            finish()
        }

        ll_setting=findViewById(R.id.ll_setting)
        ll_setting.setOnClickListener {
            startActivity(Intent(this@MainActivitySingle, Settings::class.java))
            finish()
        }

        mShowService()


    }
    private fun mShowBanners() {

        imageId= ArrayList()

        try {
            var jsonArray = JSONArray(SharePrefManager.getInstance(this@MainActivitySingle).mGetBanners())

            for (i in 0 until  jsonArray.length())
            {
                var data=jsonArray.getJSONObject(i)

                imageId.add(data.getString("image"))
            }
        }
        catch (e: JSONException)
        {
            e.printStackTrace()
        }

        viewpager_banners = findViewById(R.id.viewpager_banners)
        viewpager_banners.setAdapter(SlidingImageAdapter(this@MainActivitySingle, imageId))

//        val indicator = findViewById(R.id.indicator) as CirclePageIndicator

//        indicator.setViewPager(viewpager_banners)

        val density = resources.displayMetrics.density

        //Set circle indicator radius
//        indicator.radius = 5 * density

        NUM_PAGES_banner = imageId.size

        // Auto start of viewpager
        val handler = Handler()
        val Update = Runnable {
            if (currentPage_banners == NUM_PAGES_banner) {
                currentPage_banners = 0
            }
            viewpager_banners.setCurrentItem(currentPage_banners++, true)
        }
        swipeTimer_banners = Timer()
        swipeTimer_banners.schedule(object : TimerTask() {
            override fun run() {
                handler.post(Update)
            }
        }, 3000, 3000)

        // Pager listener over indicator
//        indicator.setOnPageChangeListener(object : ViewPager.OnPageChangeListener {
//
//            override fun onPageSelected(position: Int) {
//                currentPage_banners = position
//
//            }
//
//            override fun onPageScrolled(pos: Int, arg1: Float, arg2: Int) {
//
//            }
//
//            override fun onPageScrollStateChanged(pos: Int) {
//
//            }
//        })
    }

    override fun onResume() {
        if (DetectConnection.checkInternetConnection(this@MainActivitySingle)) {
            mUpdateBalance()
        }

        if (SharePrefManager.getInstance(this@MainActivitySingle).mGetSingleData("broadcast_status_id").equals("1")) {
           if (!mIsBroadcastShow) {
               mShowBroadCast(
                   SharePrefManager.getInstance(this@MainActivitySingle).mGetSingleData("broadcast")
               )
           }
        }

        super.onResume()
    }

    override fun onDestroy() {
        if (swipeTimer_banners != null) {
            swipeTimer_banners.cancel()
        }
        super.onDestroy()
    }

    override fun onPause() {
        if (swipeTimer_banners != null) {
            swipeTimer_banners.cancel()
        }
        super.onPause()
    }

    fun  mShowNews(news: String)
    {
        if (!news.equals(""))
        {
            textView_news.setText(news)
            textView_news.visibility= View.VISIBLE
            textView_news.isSelected=true
        }
        else
        {
            textView_news.visibility=View.GONE
        }
    }

    fun mCallAEPS(service: String)
    {
        if (SharePrefManager.getInstance(this@MainActivitySingle).mGetOutletId().equals(""))
        {
            Toast.makeText(applicationContext, "Merchant id not found please contact to admin for same", Toast.LENGTH_SHORT).show()
        }
        else {
            val intent = Intent(this@MainActivitySingle, AEPS_Service::class.java);
            intent.putExtra("outlet_id", SharePrefManager.getInstance(this@MainActivitySingle).mGetOutletId())
            intent.putExtra("mobile", SharePrefManager.getInstance(this@MainActivitySingle).mGetUsername())
            intent.putExtra("name", SharePrefManager.getInstance(this@MainActivitySingle).mGetName())
            intent.putExtra("service", service)
            startActivityForResult(intent, 1421)
        }
    }

    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount == 0) {
            if (doubleBackToExitPressedOnce) {
                super.onBackPressed()
                finish()
                overridePendingTransition(0, 0)
                return
            }
            this.doubleBackToExitPressedOnce = true
            Toast.makeText(this, R.string.please_click_back_again_to_exit, Toast.LENGTH_SHORT).show()
            Handler().postDelayed({ doubleBackToExitPressedOnce = false }, 2000)
        } else {
            fragmentManager.popBackStackImmediate()
        }
    }

    protected fun mShowLoginDetail(response: String?) {
        try {
            var message = ""
            var status = ""
            var first_name = ""
            var last_name = ""
            var email = ""
            var mobile = ""
            var role_id = ""
            var scheme_id = ""
            var joing_date = ""
            var permanent_address = ""
            var permanent_state = ""
            var permanent_city = ""
            var permanent_district = ""
            var permanent_pin_code = ""
            var present_address = ""
            var present_city = ""
            var present_state = ""
            var present_district = ""
            var present_pin_code = ""
            var shop_name = ""
            var office_address = ""
            var call_back_url = ""
            var profile_photo = ""
            var shop_photo = ""
            var gst_regisration_photo = ""
            var pancard_photo = ""
            var cancel_cheque = ""
            var address_proof = ""
            var kyc_status = ""
            var kyc_remark = ""
            var mobile_verified = ""
            var lock_amount = ""
            var session_id = ""
            var active = ""
            var reason = ""
            var api_token = ""
            var user_balance = ""
            var aeps_balance = ""
            var lien_amount = ""
            var icici_agent_id = ""
            var outlet_id = ""
            var recharge = ""
            var money = ""
            var aeps = ""
            var payout = ""
            var pancard = ""
            var ecommerce = ""
            var company_name = ""
            var company_email = ""
            var company_address = ""
            var company_address_two = ""
            var support_number = ""
            var whatsapp_number = ""
            var company_logo = ""
            var company_website = ""
            var news = ""
            var update_one = ""
            var update_two = ""
            var update_three = ""
            var update_for = ""
            var sender_id = ""
            var company_recharge = ""
            var company_money = ""
            var company_aeps = ""
            var company_payout = ""
            var view_plan = ""
            var company_pancard = ""
            var company_ecommerce = ""
            var today_sale = ""
            var today_profit = ""
            var upi_id = ""
            var collection = ""
            val jsonObject = JSONObject(response)

            if (jsonObject.has("message")) {
                message = jsonObject.getString("message")
            }

            if (jsonObject.has("status")) {
                status = jsonObject.getString("status")
            }

            if (jsonObject.has("broadcast"))
            {
                var broadcast=jsonObject.getJSONObject("broadcast")
                Log.e("broadcast","details "+broadcast)

                if (broadcast.has("status_id"))
                {
                   var status_id=broadcast.getString("status_id")

                    SharePrefManager.getInstance(this@MainActivitySingle).mSaveSingleData("broadcast_status_id",status_id)
                }

                SharePrefManager.getInstance(this@MainActivitySingle).mSaveSingleData("broadcast",broadcast.toString());
            }

            if (status.equals("success", ignoreCase = true)) {
                val userdetail = jsonObject.getJSONObject("userdetails")

                if (userdetail.has("user_id")) {
                  val  user_id = userdetail.getString("user_id")
                }

                first_name = userdetail.getString("first_name")
                last_name = userdetail.getString("last_name")
                email = userdetail.getString("email")
                mobile = userdetail.getString("mobile")
                role_id = userdetail.getString("role_id")
                scheme_id = userdetail.getString("scheme_id")

                if (userdetail.has("joing_date")) {
                    joing_date = userdetail.getString("joing_date")
                }
                permanent_address = userdetail.getString("address")
                permanent_city = userdetail.getString("city")
                permanent_state = userdetail.getString("state_id")
                permanent_district = userdetail.getString("district_id")
                permanent_pin_code = userdetail.getString("pin_code")

//                present_address=userdetail.getString("present_address");
//                present_city=userdetail.getString("present_city");
//                present_state=userdetail.getString("present_state");
//                present_district=userdetail.getString("present_district");
//                present_pin_code=userdetail.getString("present_pin_code");


//                present_address=userdetail.getString("present_address");
//                present_city=userdetail.getString("present_city");
//                present_state=userdetail.getString("present_state");
//                present_district=userdetail.getString("present_district");
//                present_pin_code=userdetail.getString("present_pin_code");
                shop_name = userdetail.getString("shop_name")
                office_address = userdetail.getString("office_address")
                call_back_url = userdetail.getString("call_back_url")
                profile_photo = userdetail.getString("profile_photo")
                shop_photo = userdetail.getString("shop_photo")
                gst_regisration_photo = userdetail.getString("gst_regisration_photo")
                pancard_photo = userdetail.getString("pancard_photo")
                cancel_cheque = userdetail.getString("cancel_cheque")
                address_proof = userdetail.getString("address_proof")
                kyc_status = userdetail.getString("kyc_status")
                kyc_remark = userdetail.getString("kyc_remark")
                mobile_verified = userdetail.getString("mobile_verified")
                lock_amount = userdetail.getString("lock_amount")
                session_id = userdetail.getString("session_id")
                active = userdetail.getString("active")
                reason = userdetail.getString("reason")
                api_token = userdetail.getString("api_token")
                user_balance = userdetail.getString("user_balance")
                aeps_balance = userdetail.getString("aeps_balance")
                lien_amount = userdetail.getString("lien_amount")

                if (userdetail.has("account_number")) {
                    SharePrefManager.getInstance(this@MainActivitySingle)
                        .mSaveSingleData("account_number", userdetail.getString("account_number"))
                }

                if (userdetail.has("referral_code")) {
                    SharePrefManager.getInstance(this@MainActivitySingle)
                        .mSaveSingleData("referral_code", userdetail.getString("referral_code"))
                }

                if (userdetail.has("ifsc_code")) {
                    SharePrefManager.getInstance(this@MainActivitySingle)
                        .mSaveSingleData("ifsc_code", userdetail.getString("ifsc_code"))
                }

                if (userdetail.has("pan_username")) {
                    SharePrefManager.getInstance(this@MainActivitySingle)
                        .mSaveSingleData("pan_username", userdetail.getString("pan_username"))
                }

                if (userdetail.has("ekyc")) {
                    SharePrefManager.getInstance(this@MainActivitySingle)
                        .mSaveSingleData("ekyc", userdetail.getString("ekyc"))
                }

                if (userdetail.has("pan_number")) {
                    SharePrefManager.getInstance(this@MainActivitySingle)
                        .mSaveSingleData("pan_number", userdetail.getString("pan_number"))
                }
                if (userdetail.has("agentonboarding")) {
                    SharePrefManager.getInstance(this@MainActivitySingle)
                        .mSaveSingleData("agentonboarding", userdetail.getString("agentonboarding"))
                }

                val userservices = jsonObject.getJSONObject("userservices")
                recharge = userservices.getString("recharge")
                money = userservices.getString("money")

                if (userservices.has("money_two")) {
                    val money_two = userservices.getString("money_two")
                    SharePrefManager.getInstance(this@MainActivitySingle).mSaveSingleData("money_two", money_two)
                }

                aeps = userservices.getString("aeps")
                payout = userservices.getString("payout")
                pancard = userservices.getString("pancard")
                ecommerce = userservices.getString("ecommerce")

                val companydetails = jsonObject.getJSONObject("companydetails")
                company_name = companydetails.getString("company_name")
                company_email = companydetails.getString("company_email")
                company_address = companydetails.getString("company_address")
                company_address_two = companydetails.getString("company_address_two")
                support_number = companydetails.getString("support_number")
                whatsapp_number = companydetails.getString("whatsapp_number")
                company_logo = companydetails.getString("company_logo")
                company_website = companydetails.getString("company_website")
                news = companydetails.getString("news")
//                update_one=companydetails.getString("update_one");
//                update_two=companydetails.getString("update_two");
//                update_three=companydetails.getString("update_three");
//                update_for=companydetails.getString("update_for");
                //                update_one=companydetails.getString("update_one");
//                update_two=companydetails.getString("update_two");
//                update_three=companydetails.getString("update_three");
//                update_for=companydetails.getString("update_for");
                sender_id = companydetails.getString("sender_id")
                company_recharge = companydetails.getString("recharge")
                company_money = companydetails.getString("money")

                if (companydetails.has("money_two")) {
                    val money_two = companydetails.getString("money_two")
                    SharePrefManager.getInstance(this@MainActivitySingle)
                        .mSaveSingleData("company_money_two", money_two)
                }

                if (companydetails.has("transaction_pin")) {
                    val transaction_pin = companydetails.getString("transaction_pin")
                    SharePrefManager.getInstance(this@MainActivitySingle)
                        .mSaveSingleData("transaction_pin", transaction_pin)
                }


                if (companydetails.has("color_start")) {
                    val color_start = companydetails.getString("color_start")
                    SharePrefManager.getInstance(this@MainActivitySingle)
                        .mSaveSingleData("color_start", color_start)
                }
                if (companydetails.has("color_end")) {
                    val color_end = companydetails.getString("color_end")
                    SharePrefManager.getInstance(this@MainActivitySingle)
                        .mSaveSingleData("color_end", color_end)
                }

                company_aeps = companydetails.getString("aeps")
                company_payout = companydetails.getString("payout")
                view_plan = companydetails.getString("view_plan")
                company_pancard = companydetails.getString("pancard")
                company_ecommerce = companydetails.getString("ecommerce")

                if (companydetails.has("upi_id")) {
                    val upi_id = companydetails.getString("upi_id")
                    SharePrefManager.getInstance(this@MainActivitySingle).mSaveSingleData("upi_id", upi_id)
                }

                if (companydetails.has("collection")) {
                    val collection = companydetails.getString("collection")
                    SharePrefManager.getInstance(this@MainActivitySingle)
                        .mSaveSingleData("collection", collection)
                }


                val banners = jsonObject.getJSONArray("banner")
                if (jsonObject.has("total_notification")) {
                    val total_notification = jsonObject.getString("total_notification")
                    SharePrefManager.getInstance(this@MainActivitySingle)
                            .mSaveNoOfNotification(total_notification)
                }
                if (jsonObject.has("notification")) {
                    val notification = jsonObject.getJSONArray("notification").toString()
                    SharePrefManager.getInstance(this@MainActivitySingle)
                            .mSaveAllNotification(notification)
                }
                if (jsonObject.has("recharge_badge")) {
                    SharePrefManager.getInstance(this@MainActivitySingle)
                            .mSaveServicesName(jsonObject.getString("recharge_badge"))
                    var jsonArray = jsonObject.getJSONArray("recharge_badge")
                    if (jsonArray.length() == 0) {
                        jsonArray = JSONArray(
                                SharePrefManager.getInstance(this@MainActivitySingle).mGetServices()
                        )
                    }
                    if (jsonArray.length() != 0 && servicesItems.size != 0) {
                        servicesItems.clear()
                    }

                    for (i in 0 until jsonArray.length()) {
                        val data = jsonArray.getJSONObject(i)
                        //                        if (data.getString("bbps").equals("0"))
//                        {
                        val items = ServicesItems()
                        items.setId(data.getString("service_id"))
                        items.setName(data.getString("service_name"))
                        items.setService_image(data.getString("service_image"))
                        items.setBbps(data.getString("bbps"))
                        servicesItems.plus(items)
                        servicesCardAdapter.notifyDataSetChanged()
                    }

//                    val items = ServicesItems()
//                    items.setId("googleplay")
//                    items.setName("Google Play Vouchers")
//                    items.setService_image("")
//                    items.setBbps("")
//                    items.image=R.drawable.google_play_icon
//                    servicesItems.add(items)
//                    servicesCardAdapter.notifyDataSetChanged()

                }
                if (jsonObject.has("sales")) {
                    val sales = jsonObject.getJSONObject("sales")
                    if (sales.has("today_sale")) {
                        today_sale = sales.getString("today_sale")
                        SharePrefManager.getInstance(this@MainActivitySingle).mSaveTodaySale(
                                today_sale
                        )
                    }
                    if (sales.has("today_profit")) {
                        today_profit = sales.getString("today_profit")
                        SharePrefManager.getInstance(this@MainActivitySingle)
                                .mSaveTodayProfit(today_profit)
                    }
                }
//                if (SharePrefManager.getInstance(this@MainActivitySingle).mGetSessionId()
//                                .equals(session_id, ignoreCase = true)
//                )
//                {
                    SharePrefManager.getInstance(this@MainActivitySingle).mSaveUserData(
                            username,
                            password,
                            first_name,
                            last_name,
                            email,
                            mobile,
                            role_id,
                            scheme_id,
                            joing_date,
                            permanent_address,
                            permanent_city,
                            permanent_district,
                            permanent_pin_code,
                            permanent_state,
                            present_address,
                            present_city,
                            present_district,
                            present_pin_code,
                            present_state,
                            lien_amount,
                            office_address,
                            call_back_url,
                            profile_photo,
                            shop_name,
                            shop_photo,
                            gst_regisration_photo,
                            pancard_photo,
                            cancel_cheque,
                            address_proof,
                            kyc_status,
                            kyc_remark,
                            mobile_verified,
                            lock_amount,
                            session_id,
                            active,
                            reason,
                            api_token,
                            user_balance,
                            aeps_balance,
                            recharge,
                            money,
                            aeps,
                            payout,
                            pancard,
                            ecommerce,
                            company_name,
                            company_email,
                            company_address,
                            company_address_two,
                            support_number,
                            whatsapp_number,
                            company_logo,
                            company_website,
                            news,
                            update_one,
                            update_two,
                            update_three,
                            update_for,
                            sender_id,
                            company_recharge,
                            company_money,
                            company_aeps,
                            company_payout,
                            view_plan,
                            company_pancard,
                            company_ecommerce,
                            banners.toString(),
                            icici_agent_id,
                            outlet_id
                    )

                    mShowBanners()

                    textview_name.setText(SharePrefManager.getInstance(this@MainActivitySingle).mGetName())
                    textview_main_balance.setText("Rs " + SharePrefManager.getInstance(this@MainActivitySingle).mGetMainBalance())

                    textview_aeps_balance.setText(resources.getString(R.string.rs) + " " + SharePrefManager.getInstance(this@MainActivitySingle).mGetAEPSBalance())

                    if (SharePrefManager.getInstance(this).mGetRoleId()<8)
                    {
                        ll_user.visibility=View.GONE
                        ll_distributer.visibility=View.VISIBLE

                    }
                    else
                    {
                        ll_user.visibility=View.VISIBLE
                        ll_distributer.visibility=View.GONE
                    }

                    if (SharePrefManager.getInstance(this@MainActivitySingle).mGetAEPS())
                    {
                        cardview_aeps.visibility = View.VISIBLE
                        cardview_atm.visibility = View.VISIBLE

                        vw_line.visibility=View.VISIBLE
                        ll_aeps_balance.visibility=View.VISIBLE
                    }
                    else{
                        cardview_aeps.visibility = View.GONE
                        cardview_atm.visibility = View.GONE

                        vw_line.visibility=View.GONE
                        ll_aeps_balance.visibility=View.GONE
                    }

                    if (SharePrefManager.getInstance(this@MainActivitySingle).mGetMoney())
                    {
                        cardview_money.visibility = View.VISIBLE
                    }
                    else{
                        cardview_money.visibility = View.GONE
                    }

                    if (SharePrefManager.getInstance(this@MainActivitySingle).mGetRecharge())
                    {
                        cardview_recharge.visibility = View.VISIBLE
                    }
                    else{
                        cardview_recharge.visibility = View.GONE
                    }

                    mShowService()

//                } else {
//                    Toast.makeText(this, "Session expired please, login again", Toast.LENGTH_SHORT)
//                            .show()
//                    SharePrefManager.getInstance(this@MainActivitySingle).mLogout()
//                    startActivity(Intent(this@MainActivitySingle, Login::class.java))
//                    finish()
//                }
            }
        } catch (e: JSONException) {
            e.printStackTrace()
        }
    }

    @SuppressLint("StaticFieldLeak")
    protected fun mUpdateBalance() {
        val builder = Uri.Builder()
        builder.appendQueryParameter(
                "api_token",
                SharePrefManager.getInstance(this@MainActivitySingle).mGetApiToken()
        )
        val sending_url = BaseURL.BASEURL_B2C + "api/application/v1/check-balance"
        object : CallResAPIPOSTMethod(this@MainActivitySingle, builder, sending_url, true, "POST") {
            override fun onPreExecute() {
                super.onPreExecute()
            }

            override fun onPostExecute(s: String) {
                super.onPostExecute(s)
                Log.e("response", "balance \n\n\ndata $s")
                mShowLoginDetail(s)
            }
        }.execute()
    }


    private fun checkIfAlreadyhavePermission(): Boolean {
        val result = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE)
        return result == PackageManager.PERMISSION_GRANTED
    }

    private fun requestForSpecificPermission() {
        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_PHONE_STATE), 101)
    }

//    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
//        menuInflater.inflate(R.menu.add_money_menu, menu)
//        return super.onCreateOptionsMenu(menu)
//    }

//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        if (item.itemId==R.id.action_add_money)
//        {
//            startActivity(Intent(this@MainActivitySingle, AddMoney::class.java))
//        }
//        return super.onOptionsItemSelected(item)
//    }

    protected fun mShowService() {
        try {
            val jsonArray =
                    JSONArray(SharePrefManager.getInstance(this@MainActivitySingle).mGetServices())

            if (jsonArray.length()!=0)
            {
                servicesItems.clear()
            }
            for (i in 0 until jsonArray.length()) {
                val data = jsonArray.getJSONObject(i)
                //                        if (data.getString("bbps").equals("0"))
//                        {
                val items = ServicesItems()
                items.id = data.getString("service_id")
                items.name = data.getString("service_name")
                items.service_image = data.getString("service_image")
                items.bbps = data.getString("bbps")
                servicesItems.add(items)
                servicesCardAdapter.notifyDataSetChanged()
                //                        }
            }

//            val items = ServicesItems()
//            items.setId("googleplay")
//            items.setName("Google Play Vouchers")
//            items.setService_image("")
//            items.setBbps("0")
//            items.image=R.drawable.google_play_icon
//            servicesItems.add(items)
//            servicesCardAdapter.notifyDataSetChanged()

        } catch (e: JSONException) {
            e.printStackTrace()
        }
    }


    @SuppressLint("StaticFieldLeak")
    fun mGetOutletId(service: String?) {
        val builder = Uri.Builder()
        builder.appendQueryParameter(
            "api_token",
            SharePrefManager.getInstance(this@MainActivitySingle).mGetApiToken()
        )
        val sending_url = BaseURL.BASEURL_B2C + "api/application/v1/aeps-outlet-id"
        object : CallResAPIPOSTMethod(this@MainActivitySingle, builder, sending_url, true, "POST") {
            override fun onPreExecute() {
                super.onPreExecute()
                dialog = ProgressDialog(this@MainActivitySingle)
                dialog.setMessage("Please wait...")
                dialog.show()
                dialog.setCancelable(false)
            }

            override fun onPostExecute(s: String) {
                super.onPostExecute(s)
                dialog.dismiss()
                Log.e("response", "id $s")
                if (s != "") {
                    var status = ""
                    var message = ""
                    var icici_agent_id = ""
                    var outlet_id = ""
                    try {
                        val jsonObject = JSONObject(s)
                        if (jsonObject.has("status")) {
                            status = jsonObject.getString("status")
                        }
                        if (jsonObject.has("message")) {
                            message = jsonObject.getString("message")
                        }
                        if (status.equals("success", ignoreCase = true)) {
                            if (jsonObject.has("icici_agent_id")) {
                                icici_agent_id = jsonObject.getString("icici_agent_id")
                                SharePrefManager.getInstance(this@MainActivitySingle)
                                    .mSaveSingleData("icici_agent_id", icici_agent_id)
                            }
                            if (jsonObject.has("outlet_id")) {
                                outlet_id = jsonObject.getString("outlet_id")
                                SharePrefManager.getInstance(this@MainActivitySingle)
                                    .mSaveSingleData("outlet_id", outlet_id)
                            }

                            if (service.equals("ekyc",ignoreCase = true))
                            {
//                                mCallKYC()
                            }
                            else {
                                if (service != null) {
                                    mCallAEPS(service)
                                }
                            }

                        } else {
                            if (message != "") {
                                Toast.makeText(this@MainActivitySingle, message, Toast.LENGTH_SHORT).show()
                            } else {
                                Toast.makeText(
                                    this@MainActivitySingle,
                                    "Something went wrong",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                }
            }
        }.execute()
    }

    @SuppressLint("StaticFieldLeak")
    fun mGetOutletIdForMatm(service: String?, remark: String?) {
        val builder = Uri.Builder()
        builder.appendQueryParameter(
            "api_token",
            SharePrefManager.getInstance(this@MainActivitySingle).mGetApiToken()
        )

        val sending_url = BaseURL.BASEURL_B2C + "api/application/v1/aeps-outlet-id"
        object : CallResAPIPOSTMethod(this@MainActivitySingle, builder, sending_url, true, "POST") {
            override fun onPreExecute() {
                super.onPreExecute()
                dialog = ProgressDialog(this@MainActivitySingle)
                dialog.setMessage("Please wait...")
                dialog.show()
                dialog.setCancelable(false)
            }

            override fun onPostExecute(s: String) {
                super.onPostExecute(s)
                dialog.dismiss()
                Log.e("response", "id $s")
                if (s != "") {
                    var status = ""
                    var message = ""
                    var icici_agent_id = ""
                    var outlet_id = ""
                    try {
                        val jsonObject = JSONObject(s)
                        if (jsonObject.has("status")) {
                            status = jsonObject.getString("status")
                        }
                        if (jsonObject.has("message")) {
                            message = jsonObject.getString("message")
                        }
                        if (status.equals("success", ignoreCase = true)) {
                            if (jsonObject.has("icici_agent_id")) {
                                icici_agent_id = jsonObject.getString("icici_agent_id")
                                SharePrefManager.getInstance(this@MainActivitySingle)
                                    .mSaveSingleData("icici_agent_id", icici_agent_id)
                            }
                            if (jsonObject.has("outlet_id")) {
                                outlet_id = jsonObject.getString("outlet_id")
                                SharePrefManager.getInstance(this@MainActivitySingle)
                                    .mSaveSingleData("outlet_id", outlet_id)
                            }
                            mCallServices(service, remark)
                        } else {
                            if (message != "") {
                                Toast.makeText(this@MainActivitySingle, message, Toast.LENGTH_SHORT).show()
                            } else {
                                Toast.makeText(
                                    this@MainActivitySingle,
                                    "Something went wrong",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                }
            }
        }.execute()
    }

    protected fun mCallServices(service_code: String?, remark: String?) {

        val outlet_id=SharePrefManager.getInstance(this@MainActivitySingle).mGetOutletId();
        if (outlet_id != "") {
            val INTENT_CODE = 1421
            val intent = Intent(this@MainActivitySingle, MicroATMLaunch::class.java)
            intent.putExtra("outlet_id", outlet_id)
            intent.putExtra("service", service_code)
            intent.putExtra("mobile", SharePrefManager.getInstance(this@MainActivitySingle).mGetUsername())
            intent.putExtra("remark", remark)
            startActivityForResult(intent, INTENT_CODE)
        }
        else {
            Toast.makeText(
                this,
                "Merchant id not found, please contact to admin for same",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    protected fun mShowBroadCast( broadcast : String) {

        val inflater2 = getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val v2 = inflater2.inflate(R.layout.custom_alertdialog_for_broadcast, null)

        val tv_header = v2.findViewById<TextView>(R.id.tv_header)

        val iv_close = v2.findViewById<ImageView>(R.id.iv_close)

        val iv_image = v2.findViewById<ImageView>(R.id.iv_image)

        val tv_text = v2.findViewById<TextView>(R.id.tv_text)

        val builder2 = AlertDialog.Builder(this@MainActivitySingle)
        builder2.setCancelable(false)

        var img_status=""
        var image_url=""
        var status_id=""

        try {
            val jsonObject = JSONObject(broadcast)
            if (jsonObject.has("heading"))
            {
                tv_header.setText(jsonObject.getString("heading"))
            }

            if (jsonObject.has("img_status"))
            {
                 img_status=jsonObject.getString("img_status")
            }

            if (jsonObject.has("status_id"))
            {
                status_id=jsonObject.getString("status_id")

                SharePrefManager.getInstance(this@MainActivitySingle).mSaveSingleData("broadcast_status_id",status_id)
            }

            if (jsonObject.has("image_url"))
            {
                image_url=jsonObject.getString("image_url")
            }

            if (img_status.equals("1"))
            {
                if (!image_url.equals(""))
                {
                    Glide.with(this@MainActivitySingle).load(image_url).into(iv_image)
                }
            }
            else
            {
                iv_image.visibility=View.GONE
            }

            if (jsonObject.has("message"))
            {
                if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.N)
                {
                    tv_text.setText(Html.fromHtml(jsonObject.getString("message"),Html.FROM_HTML_MODE_LEGACY))
                }
                else {
                    tv_text.setText(Html.fromHtml(jsonObject.getString("message")))
                }
            }

        }
        catch ( e: JSONException)
        {
            e.printStackTrace()
        }

        builder2.setView(v2)
        alertDialog = builder2.create()
        iv_close.setOnClickListener(View.OnClickListener {
            alertDialog.dismiss()
        mIsBroadcastShow=false
        })
        alertDialog.show()

        mIsBroadcastShow=true
    }

    fun mShowMoney()
    {

//        val icons: IntArray = intArrayOf(R.drawable.bank_icon_foreground,R.drawable.bank_icon_foreground,R.drawable.bank_icon_foreground)
//        val  names : Array<String> = arrayOf("Money Transfer 1","Money Transfer 2","Report");

        allserv = LinkedHashMap()

        allserv.put(R.drawable.bank_icon_foreground,"Money Transfer 1")
        allserv.put(R.drawable.bank_icon_foreground1,"Money Transfer 2")
        allserv.put(R.drawable.bank_icon_foreground2,"Report")

        for (i in allserv)
        {
            val items = ServicesItems()
            items.setBbps("0")
            items.setName(allserv.getValue(i.key))
            items.setId(allserv.getValue(i.key));
            items.setImage(i.key);
            items.setService_image("");

            if (allserv.getValue(i.key).equals("Money Transfer 1",ignoreCase = true)&&SharePrefManager.getInstance(this).mGetMoney1())
            {
                servicesItems_money.add(items)
                servicesCardAdapter_money.notifyDataSetChanged()
            }
            else if (allserv.getValue(i.key).equals("Money Transfer 2",ignoreCase = true)&&SharePrefManager.getInstance(this).mGetMoney2())
            {
                items.setName("Money Transfer");
                servicesItems_money.add(items)
                servicesCardAdapter_money.notifyDataSetChanged()
            }
            else
            {
                servicesItems_money.add(items)
                servicesCardAdapter_money.notifyDataSetChanged()
            }
        }

    }


    fun mShowATM()
    {
//        val icons: IntArray = intArrayOf(R.drawable.micro_atm,R.drawable.micro_atm)
//        val  names : Array<String> =  arrayOf("Enquiry","Withdrawal")


        lateinit var allserv : LinkedHashMap<Int,String>

        allserv = LinkedHashMap()

        allserv.put(R.drawable.micro_atm,"Enquiry")
        allserv.put(R.drawable.micro_atm1,"Withdrawal")


        for (i in allserv)
        {
            val items = ServicesItems()
            items.setBbps("0")
            items.setName(allserv.getValue(i.key))

            if (allserv.getValue(i.key).equals("Enquiry")) {
                items.setId("matm_enquiry");
            }
            else
            {
                items.setId("matm_withdrawal");
            }

            items.setImage(i.key);
            items.setService_image("");

            servicesItems_atm.add(items)
            servicesCardAdapter_atm.notifyDataSetChanged()
        }

    }

    fun mShowAeps()
    {

        lateinit var allserv : LinkedHashMap<Int,String>

        allserv = LinkedHashMap()

        if (SharePrefManager.getInstance(this@MainActivitySingle).mGetAEPS()) {
            allserv.put(R.drawable.aeps, "Enquiry")
            allserv.put(R.drawable.aeps1, "Withdrawal")
            allserv.put(R.drawable.aeps2, "Mini Statement")
            allserv.put(R.drawable.aeps3, "Aadhaar Pay")
        }

        if (SharePrefManager.getInstance(this).mGetPayout()) {
            allserv.put(R.drawable.payout_icon_foreground, "Payout")
        }

        if (SharePrefManager.getInstance(this).mGetPanCard()) {
            allserv.put(R.drawable.pancard,"PAN Card")
        }




//        val icons: IntArray = intArrayOf(R.drawable.aeps,R.drawable.aeps,R.drawable.aeps,R.drawable.aeps,R.drawable.payout_icon_foreground,R.drawable.pancard)
//        val  names : Array<String> =  arrayOf("Enquiry","Withdrawal","Mini Statement","Aadhaar Pay","Payout","PAN Card")

        for (i in allserv)
        {
            val items = ServicesItems()
            items.setBbps("0")
            items.setName(allserv.getValue(i.key))
            items.setId(allserv.getValue(i.key));
            items.setImage(i.key);
            items.setService_image("");

            servicesItems_aeps.add(items)
            servicesCardAdapter_aeps.notifyDataSetChanged()
        }
    }

    fun mShowTravel()
    {

        lateinit var allserv : LinkedHashMap<Int,String>

        allserv = LinkedHashMap()

        allserv.put(R.drawable.bus,"Bus")
        allserv.put(R.drawable.train,"Train")
        allserv.put(R.drawable.flight,"Flight")
        allserv.put(R.drawable.hotel,"Hotel")

//        val icons: IntArray = intArrayOf(R.drawable.bus,R.drawable.train,R.drawable.flight,R.drawable.hotel)
//        val  names : Array<String> =  arrayOf("Bus","Train","Flight","Hotel")

        for (i in allserv)
        {
            val items = ServicesItems()
            items.setBbps("0")
            items.setName(allserv.getValue(i.key))
            items.setId(allserv.getValue(i.key));
            items.setImage(i.key);
            items.setService_image("");

            servicesItems_travel.add(items)
            servicesCardAdapter_travel.notifyDataSetChanged()
        }
    }


    fun mShowDistributer()
    {

        lateinit var allserv : LinkedHashMap<Int,String>

        allserv = LinkedHashMap()

        allserv.put(R.drawable.add_member_icon,"Add Member")
        allserv.put(R.drawable.fund_transfer,"Fund Transfer")
        allserv.put(R.drawable.report_icon,"All Transactions")


//        val icons: IntArray = intArrayOf(R.drawable.add_member_icon,R.drawable.fund_transfer,R.drawable.report_icon)
//        val  names : Array<String> =  arrayOf("Add Member","Fund Transfer","All Transactions")

        for (i in allserv)
        {
            val items = ServicesItems()
            items.setBbps("0")
            items.setName(allserv.getValue(i.key))
            items.setId(allserv.getValue(i.key));
            items.setImage(i.key);
            items.setService_image("");

            servicesItems_distributer.add(items)
            servicesCardAdapter_distributer.notifyDataSetChanged()
        }
    }
}