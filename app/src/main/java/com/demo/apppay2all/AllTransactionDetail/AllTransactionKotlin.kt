package com.demo.apppay2all.AllTransactionDetail

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.ProgressDialog
import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.demo.apppay2all.*
import com.demo.apppay2all.BaseURL.BaseURL
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.launch
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.Exception
import java.net.HttpURLConnection
import java.net.URL
import java.text.SimpleDateFormat
import java.util.*

//data class StatusIds(val status_id : Int,val status : String )
data class StatusIds(val status: List<idStatus>)
data class idStatus(val status_id: Int,val status : String)

class AllTransactionKotlin : AppCompatActivity(){

    lateinit var recyclerview_reports: RecyclerView
    lateinit var report_items: MutableList<AllTRansactionItems>
    lateinit var report_cardAdaptor: AllTransactionCardaAdapter

    lateinit var dialog: ProgressDialog

    lateinit var alertDialog: AlertDialog

    lateinit var radiogroup_group: RadioGroup

    lateinit var textview_icdate: TextView

    lateinit var imageView_storage: ImageView

    lateinit var tv_title: TextView

    lateinit var floatactionbutton_filter: FloatingActionButton

    lateinit var rl_from: RelativeLayout
    lateinit var rl_to:RelativeLayout
    lateinit var rl_status:RelativeLayout
    lateinit var tv_from: TextView
    lateinit var tv_to:TextView
    lateinit var tv_status:TextView
    lateinit var ed_number: EditText
    lateinit var bt_search: Button

    private var mYear = 0
    private  var mMonth:Int = 0
    private  var mDay:Int = 0

    var fromdate = ""
    var todate:kotlin.String? = ""

    var searchkey = "All"

    var number = ""

    var mIsFirst = false

    lateinit var popupMenu: PopupMenu

    var status_id = ""
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reports)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)


        floatactionbutton_filter = findViewById<FloatingActionButton>(R.id.floatactionbutton_filter)
//        floatactionbutton_filter.setVisibility(View.VISIBLE);
        //        floatactionbutton_filter.setVisibility(View.VISIBLE);
        floatactionbutton_filter.setOnClickListener(View.OnClickListener {
            val intent = Intent(this@AllTransactionKotlin, Search_Recharge::class.java)
            startActivity(intent)
        })

        tv_title = findViewById<TextView>(R.id.tv_title)
        tv_title.setText("All Transaction Report")

        rl_from = findViewById<RelativeLayout>(R.id.rl_from)
        rl_from.setOnClickListener(View.OnClickListener {
            val c = Calendar.getInstance()
            mYear = c[Calendar.YEAR]
            mMonth = c[Calendar.MONTH]
            mDay = c[Calendar.DAY_OF_MONTH]
            val datePickerDialog = DatePickerDialog(this@AllTransactionKotlin,
                { view, year, monthOfYear, dayOfMonth ->
                    fromdate = year.toString() + "-" + (monthOfYear + 1) + "-" + dayOfMonth
                    tv_from.setText(fromdate)
                }, mYear, mMonth, mDay
            )
            datePickerDialog.show()
            datePickerDialog.datePicker.maxDate = System.currentTimeMillis()
        })

        rl_to = findViewById<RelativeLayout>(R.id.rl_to)
        rl_to.setOnClickListener(View.OnClickListener {
            val c = Calendar.getInstance()
            mYear = c[Calendar.YEAR]
            mMonth = c[Calendar.MONTH]
            mDay = c[Calendar.DAY_OF_MONTH]
            val datePickerDialog = DatePickerDialog(this@AllTransactionKotlin,
                { view, year, monthOfYear, dayOfMonth ->
                    todate = year.toString() + "-" + (monthOfYear + 1) + "-" + dayOfMonth
                    tv_to.setText(todate)
                }, mYear, mMonth, mDay
            )
            datePickerDialog.show()
            datePickerDialog.datePicker.maxDate = System.currentTimeMillis()
        })

        rl_status = findViewById<RelativeLayout>(R.id.rl_status)
        popupMenu = PopupMenu(this@AllTransactionKotlin, rl_status)
        rl_status.setOnClickListener(View.OnClickListener { //                popupMenu.getMenu().add("All");
            //                popupMenu.getMenu().add("Success");
            //                popupMenu.getMenu().add("Failure");
            //                popupMenu.getMenu().add("Pending");
            //                popupMenu.getMenu().add("Dispute");
            //                popupMenu.getMenu().add("Profit");
            //                popupMenu.getMenu().add("Debit");
            if (popupMenu.getMenu().size() != 0) {
                popupMenu.show()
            }
            popupMenu.setOnMenuItemClickListener(PopupMenu.OnMenuItemClickListener { menuItem ->
                searchkey = menuItem.title.toString()
                status_id = menuItem.itemId.toString() + ""
                tv_status.setText(searchkey)
                true
            })
        })

        tv_from = findViewById<TextView>(R.id.tv_from)
        tv_to = findViewById<TextView>(R.id.tv_to)

        val formatter = SimpleDateFormat("yyyy-MM-dd")
        fromdate = formatter.format(Date(System.currentTimeMillis()))
        todate = formatter.format(Date(System.currentTimeMillis()))

        tv_from.setText(fromdate)
        tv_to.setText(todate)

        tv_status = findViewById<TextView>(R.id.tv_status)
        ed_number = findViewById<EditText>(R.id.ed_number)
        bt_search = findViewById<Button>(R.id.bt_search)
        bt_search.setOnClickListener(View.OnClickListener {
            if (fromdate == "") {
                Toast.makeText(this@AllTransactionKotlin, "Please Select From Date", Toast.LENGTH_SHORT)
                    .show()
            } else if (todate == "") {
                Toast.makeText(this@AllTransactionKotlin, "Please Select To Date", Toast.LENGTH_SHORT)
                    .show()
            } else {
                number = ed_number.getText().toString()
                mIsFirst = true
                mStatment("search")
            }
        })

        textview_icdate = findViewById<TextView>(R.id.textview_icdate)
        textview_icdate.setOnClickListener(View.OnClickListener { mShowAlertDialogSearchByStatus() })

        imageView_storage = findViewById<ImageView>(R.id.imageView_storage)
        imageView_storage.setOnClickListener(View.OnClickListener { mShowAlertDialogSearchByStatus() })

        recyclerview_reports = findViewById<RecyclerView>(R.id.recyclerview_report)

        recyclerview_reports.setLayoutManager(LinearLayoutManager(this@AllTransactionKotlin))
        report_items = ArrayList<AllTRansactionItems>()
        report_cardAdaptor = AllTransactionCardaAdapter(this@AllTransactionKotlin, report_items)
        recyclerview_reports.setAdapter(report_cardAdaptor)


        if (DetectConnection.checkInternetConnection(this@AllTransactionKotlin)) {
            mStatment("get")
            if (SharePrefManager.getInstance(this@AllTransactionKotlin).mGetStatusList() == "") {
                mGetStatusId()
            } else {
                mShowStatus(SharePrefManager.getInstance(this@AllTransactionKotlin).mGetStatusList())
            }
        } else {
            Toast.makeText(this, "No Internet Connection", Toast.LENGTH_SHORT).show()
        }
    }


    @SuppressLint("StaticFieldLeak")
    private fun mStatment(type: String) {
        var sending_url: String? = null
        sending_url =
            BaseURL.BASEURL_B2C + "api/reports/all-transaction-report?api_token=" + SharePrefManager.getInstance(
                this@AllTransactionKotlin
            ).mGetApiToken()
        if (type.equals("search", ignoreCase = true)) {
            sending_url =
                BaseURL.BASEURL_B2C + "api/reports/all-transaction-report?api_token=" + SharePrefManager.getInstance(
                    this@AllTransactionKotlin
                )
                    .mGetApiToken() + "&fromdate=" + fromdate + "&todate=" + todate + "&status_id=" + status_id + "&number=" + number + "&provider_id="
        }

        object : CallResAPIGetMethod(this@AllTransactionKotlin,sending_url)
        {
            override fun onPreExecute() {
                super.onPreExecute()
                dialog = ProgressDialog(this@AllTransactionKotlin)
                dialog.setMessage("Please wait...")
                dialog.show()
                dialog.setCancelable(false)
            }

            override fun onPostExecute(result: String) {

                dialog.dismiss()
                Log.e("response ", "data$result")
                if (result == "") {
                    Toast.makeText(this@AllTransactionKotlin, "Server Not Responding", Toast.LENGTH_SHORT)
                        .show()
                }
                else {
                    try {
                        val jsonObject_report = JSONObject(result)
                        val jsonArray = jsonObject_report.getJSONArray("reports")
                        if (mIsFirst) {
                            mIsFirst = false
                            report_items.clear()
                            report_cardAdaptor.notifyDataSetChanged()
                        }
                        for (i in 0 until jsonArray.length()) {
                            val item = AllTRansactionItems()
                            val jsonObject = jsonArray.getJSONObject(i)
                            item.id = jsonObject.getString("id")
                            item.date = jsonObject.getString("created_at")
                            item.provider = jsonObject.getString("provider")
                            //                            item.setName(jsonObject.getString("provider"));
                            item.number = jsonObject.getString("number")
                            item.txnid = jsonObject.getString("txnid")
                            item.amount = jsonObject.getString("amount")
                            item.commisson = jsonObject.getString("profit")
                            item.total_balance = jsonObject.getString("total_balance")
                            item.name = jsonObject.getString("user")
                            item.providerimage = jsonObject.getString("provider_icon")
                            item.status = jsonObject.getString("status")
                            item.number = jsonObject.getString("number")
                            item.ol = jsonObject.getString("opening_balance")
                            item.type = "all"
                            if (jsonObject.has("service_id")) {
                                item.service_id = jsonObject.getString("service_id")
                            }
                            report_items.add(item)
                            report_cardAdaptor.notifyDataSetChanged()
                        }
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                }

                Log.e("all","txn size "+report_items.size)

            }


        }.execute()


    }

    protected fun mShowAlertDialogSearchByStatus() {
        val layoutInflater = getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = layoutInflater.inflate(R.layout.custom_alert_dialouge_2, null)
        radiogroup_group = view.findViewById<RadioGroup>(R.id.radiogroup_group)
        radiogroup_group.setOnCheckedChangeListener(RadioGroup.OnCheckedChangeListener { radioGroup, i ->
            if (i == R.id.radiobutton_all) {
                mSearch("")
                alertDialog.dismiss()
            } else if (i == R.id.radiobutton_success) {
                mSearch("success")
                alertDialog.dismiss()
            } else if (i == R.id.radiobutton_pedning) {
                mSearch("pending")
                alertDialog.dismiss()
            } else if (i == R.id.radiobutton_failed) {
                mSearch("failure")
                alertDialog.dismiss()
            } else if (i == R.id.radiobutton_disputed) {
                mSearch("disput")
                alertDialog.dismiss()
            }
        })
        val builder = AlertDialog.Builder(this@AllTransactionKotlin)
        builder.setView(view)
        alertDialog = builder.create()
        alertDialog.show()
    }


    protected fun mSearch(newText: String) {
        if (report_items.size != 0) {
            val temp: MutableList<AllTRansactionItems?> = ArrayList()
            for (d in report_items) {
                //or use .equal(text) with you want equal match
                //use .toLowerCase() for better matches
                if (d.status.lowercase(Locale.getDefault())
                        .contains(newText.lowercase(Locale.getDefault()))
                ) {
                    temp.add(d)
                }
            }
            //update recyclerview
            report_cardAdaptor.UpdateList(temp)
            Log.e("key", "=$newText")
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.search_menu, menu)
        val search = menu.findItem(R.id.search)
        val searchView = search.actionView as SearchView
        search(searchView)
        return true
    }

    private fun search(searchView: SearchView) {
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                if (report_items.size != 0) {
                    val temp: MutableList<AllTRansactionItems?> = ArrayList()
                    for (d in report_items) {
                        //or use .equal(text) with you want equal match
                        //use .toLowerCase() for better matches
                        if (d.provider.lowercase(Locale.getDefault())
                                .contains(newText.lowercase(Locale.getDefault())) || d.number.lowercase(
                                Locale.getDefault()
                            )
                                .contains(newText.lowercase(Locale.getDefault())) || d.amount.lowercase(
                                Locale.getDefault()
                            ).contains(newText.lowercase(Locale.getDefault())) ||
                            d.commisson.contains(newText) || d.status.lowercase(Locale.getDefault())
                                .contains(
                                    newText.lowercase(
                                        Locale.getDefault()
                                    )
                                ) || d.total_balance.lowercase(Locale.getDefault()).contains(
                                newText.lowercase(
                                    Locale.getDefault()
                                )
                            )
                        ) {
                            temp.add(d)
                        }
                    }
                    //update recyclerview
                    report_cardAdaptor.UpdateList(temp)
                }
                return true
            }
        })
    }

    @SuppressLint("StaticFieldLeak")
    protected fun mGetStatusId() {
//        to use with coroutine of kotlin

        val retrofit = Retrofit.Builder().baseUrl(BaseURL.BASEURL_B2C)
        .addConverterFactory(GsonConverterFactory.create()).build()
        val apiservice = retrofit.create(APIService::class.java)
        lifecycleScope.launch {

            try {
                Log.e("thread","name "+Thread.currentThread().name)
                val list_status = apiservice.getStatusList(SharePrefManager.getInstance(this@AllTransactionKotlin).mGetApiToken())


                for (i in list_status.status)
                {
                    popupMenu.menu.add(i.status_id,i.status_id,i.status_id,i.status)
                }
            }
            catch (e : Exception)
            {
                e.printStackTrace()
            }
        }

//        val sending_url =
//            BaseURL.BASEURL_B2C + "api/reports/status-type?api_token=" + SharePrefManager.getInstance(
//                this@AllTransactionKotlin
//            ).mGetApiToken()
//        object : CallResAPIGetMethod(this@AllTransactionKotlin, sending_url) {
//            override fun onPreExecute() {
//                super.onPreExecute()
//            }
//
//            override fun onPostExecute(s: String) {
//                super.onPostExecute(s)
//                Log.e("response", "status id $s")
//                try {
//                    val jsonObject = JSONObject(s)
//                    if (jsonObject.has("status")) {
//                        val jsonArray = jsonObject.getJSONArray("status")
//                        for (i in 0 until jsonArray.length()) {
//                            val data = jsonArray.getJSONObject(i)
//                            popupMenu.getMenu().add(
//                                data.getInt("status_id"),
//                                data.getInt("status_id"),
//                                data.getInt("status_id"),
//                                data.getString("status")
//                            )
//                        }
//                        if (jsonArray.length() > 0) {
//                            SharePrefManager.getInstance(this@AllTransactionKotlin).mSaveStatusList(s)
//                        }
//                    }
//                } catch (e: JSONException) {
//                    e.printStackTrace()
//                }
//            }
//        }.execute()
    }

    protected fun mShowStatus(s: String?) {
        try {
            val jsonObject = JSONObject(s)
            if (jsonObject.has("status")) {
                val jsonArray = jsonObject.getJSONArray("status")
                for (i in 0 until jsonArray.length()) {
                    val data = jsonArray.getJSONObject(i)
                    popupMenu.getMenu().add(
                        data.getInt("status_id"),
                        data.getInt("status_id"),
                        data.getInt("status_id"),
                        data.getString("status")
                    )
                }

//                if (jsonArray.length()>0)
//                {
//                    SharePrefManager.getInstance(AllTransaction.this).mSaveStatusList(s);
//                }
            }
        }
        catch (e: JSONException) {
            e.printStackTrace()
        }
    }
}