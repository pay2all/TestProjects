package com.demo.apppay2all;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Menu;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.demo.apppay2all.LoginNewDetails.LoginNew;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.pay2all.aeps.AEPS_Service;
import com.demo.apppay2all.BaseURL.BaseURL;
import com.demo.apppay2all.DesputDetails.DisputeTypes;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Timer;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, View.OnTouchListener {

    BottomNavigationView bottomNavigation;
    DrawerLayout drawer;
    RecyclerView recyclerview_services;
    CardView cardview_recharge_service;

    NavigationView navigationView;

    LinearLayout ll_fund_transfer, ll_add_member, ll_distributer;

    String currentVersion = "";

    Menu mMenu = null;

    RelativeLayout rl_main;
    ImageView iv_insurance,iv_money_transfer,iv_bharatbill,iv_qrupi,iv_qrupi1,iv_pos,iv_mini_statement;


    private ViewGroup RootLayout;

    CardView cv_chat;
    WebView wv_chat;

    ViewPager viewpager_banners;
    private int currentPage_banners = 0;
    private int NUM_PAGES_banner = 0;
    Timer swipeTimer_banners;
    ArrayList<String> imageId;

    ViewPager viewPager;
    LinearLayout sliderDotspanel;
    private int dotscount;
    private ImageView[] dots;
    ImageView iv_electricity,iv_bal_enq;
    ImageView aadhaarpay,shoppingcard,iv_aadharwithdraw,iv_mob_rech,iv_dth,iv_datacard,iv_lic;

    AppBarConfiguration appBarConfiguration;
    NavController navController;

    ImageView iv_menu;

    ProgressDialog dialog;

    TextView tv_shop_name,tv_name;

    TextView tv_main_balance,tv_aeps_balance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar()!=null)
        {
            getSupportActionBar().hide();
        }

        tv_main_balance=findViewById(R.id.tv_main_balance);
        tv_aeps_balance=findViewById(R.id.tv_aeps_balance);

        tv_main_balance.setText("Main "+getResources().getString(R.string.rs)+SharePrefManager.getInstance(this).mGetMainBalance());
        tv_aeps_balance.setText("AEPS "+getResources().getString(R.string.rs)+SharePrefManager.getInstance(this).mGetAEPSBalance());



        iv_menu=findViewById(R.id.iv_menu);
        iv_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawer.openDrawer(Gravity.LEFT);
            }
        });

        RootLayout=findViewById(R.id.rl_main);



        viewPager = (ViewPager) findViewById(R.id.viewPager);

        sliderDotspanel = (LinearLayout) findViewById(R.id.SliderDots);

        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(this);

        viewPager.setAdapter(viewPagerAdapter);

        dotscount = viewPagerAdapter.getCount();
        dots = new ImageView[dotscount];

        for(int i = 0; i < dotscount; i++){

            dots[i] = new ImageView(this);
            dots[i].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.non_active_dot));

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

            params.setMargins(8, 0, 8, 0);

            sliderDotspanel.addView(dots[i], params);

        }

        dots[0].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.active_dot));

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                for(int i = 0; i< dotscount; i++){
                    dots[i].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.non_active_dot));
                }

                dots[position].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.active_dot));

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        navigationView.setItemIconTintList(null);
        navigationView.setNavigationItemSelectedListener(this);

        View navview = navigationView.getHeaderView(0);


        tv_shop_name=navview.findViewById(R.id.tv_shop_name);
        tv_shop_name.setText(SharePrefManager.getInstance(MainActivity.this).mGetSingleData("shop_name"));

        tv_name=navview.findViewById(R.id.tv_name);
        tv_name.setText(SharePrefManager.getInstance(MainActivity.this).mGetName());


        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();


//        ll_mobile_prepaid=findViewById(R.id.ll_mobile_prepaid);
//        ll_mobile_prepaid.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                Intent intent=new Intent(MainActivity.this, Mobile_Recharge_Operator.class);
//                intent.putExtra("type","1");
//                startActivity(intent);
//            }
//        });




      /*  ll_postpaid=findViewById(R.id.ll_postpaid);
        ll_postpaid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(MainActivity.this, Mobile_Recharge_Operator.class);
                intent.putExtra("type","4");
                startActivity(intent);
            }
        });*/


//        ll_dish=findViewById(R.id.ll_dish);
//        ll_dish.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent=new Intent(MainActivity.this, Operator_Designe_List.class);
//                intent.putExtra("type","2");
//                startActivity(intent);
//            }
//        });


//       ll_payout=findViewById(R.id.ll_payout);
//       ll_payout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view)
//            {
//                Intent intent = new Intent(MainActivity.this, PayoutMoveToBank.class);
//                startActivity(intent);
//            }
//        });

 /*


       /* ll_microatm=findViewById(R.id.ll_microatm);
        ll_microatm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity.this, Micro_Atm.class);
                startActivity(intent);
            }
        });*/




//        ll_ledger=findViewById(R.id.ll_ledger);
//        ll_ledger.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent=new Intent(MainActivity.this, Ledger.class);
//                startActivity(intent);
//            }
//        });

//        ll_billpay=findViewById(R.id.ll_billpay);
//        ll_billpay.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent=new Intent(MainActivity.this,Bill_Pay.class);
//                startActivity(intent);
//            }
//        });


       /* ll_onlinecard=findViewById(R.id.ll_onlinecard);
        ll_onlinecard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity.this, Online_Card.class);
                startActivity(intent);
            }
        });*/


//        iv_qrupi=findViewById(R.id.iv_qrupi);
//        iv_qrupi.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent=new Intent(MainActivity.this, QRUPI.class);
//                startActivity(intent);
//            }
//        });


        bottomNavigation = findViewById(R.id.nav_view_bottom);
         appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_report,R.id.navigation_wallet, R.id.navigation_notification, R.id.navigation_help)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
//        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(bottomNavigation, navController);

//        NavigationUI.setupWithNavController(bottomNavigation, navController);

//        bottomNavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
//            @Override
//            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
//                int id = menuItem.getItemId();
//                if (id == R.id.navigation_home) {
//                    Intent intent = new Intent(MainActivity.this, MainActivity.class);
//                    startActivity(intent);
//                    finish();
//                } else if (id == R.id.navigation_serviceinformation) {
//
//
////                    Intent intent = new Intent(MainActivity.this, Complaints.class);
////                    startActivity(intent);
//                } else if (id == R.id.navigation_refer) {
//
//
//                    Intent intent = new Intent(MainActivity.this, ReferFragment.class);
//                    startActivity(intent);
//
//                } else if (id == R.id.navigation_addmoney) {
//
//
//                    Intent intent = new Intent(MainActivity.this, AddMoney.class);
//                    startActivity(intent);
//
//
//                } else if (id == R.id.navigation_report) {
//
////                    Intent intent=new Intent(MainActivity.this, History.class);
//                    Intent intent = new Intent(MainActivity.this, Search_Recharge.class);
//                    startActivity(intent);
//
//
//                }
//
//
//                return true;
//            }
//        });

        try {
            currentVersion = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);

        mMenu = menu;
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {

            mLogout();

        } else if (id == R.id.action_change_password) {

            Intent intent = new Intent(MainActivity.this, ChangePassword.class);
            startActivity(intent);


        }
//        else if (id == R.id.action_satalite) {
//
//            Intent intent = new Intent(MainActivity.this, NewsFragment.class);
//            startActivity(intent);
//
//        }


        return super.onOptionsItemSelected(item);

    }

    public void mLogout() {

        SharedPreferences sharedPreferences = getSharedPreferences("user", 0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear().apply();
        startActivity(new Intent(MainActivity.this, LoginNew.class));
        finish();
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return false;
    }



    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        if (item.getItemId()==R.id.nav_logout)
        {
            mLogout();
        }
        else if (item.getItemId()==R.id.nav_dispute)
        {
            startActivity(new Intent(MainActivity.this, DisputeTypes.class));
        }
       else if (item.getItemId()==R.id.nav_contact)
        {
            startActivity(new Intent(MainActivity.this, ContactUs.class));
        }
       else if (item.getItemId()==R.id.nav_about)
        {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(BaseURL.BASEURL_B2C+"about-us"));
            startActivity(browserIntent);
        }
       else if (item.getItemId()==R.id.nav_privacy)
        {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(BaseURL.BASEURL_B2C+"privacy-policy"));
            startActivity(browserIntent);
        }
       else if (item.getItemId()==R.id.nav_share)
        {
            Intent i = new Intent("android.intent.action.SEND");
            i.setType("text/plain");
            String stringBuilder = "https://play.google.com/store/apps/details?id=" +getPackageName();
            i.putExtra("android.intent.extra.TEXT", stringBuilder);
            startActivity(Intent.createChooser(i, "Share via"));
        }
       else if (item.getItemId()==R.id.nav_rateus)
        {
            try {
                    startActivity(new Intent("android.intent.action.VIEW", Uri.parse("https://play.google.com/store/apps/details?id="+getPackageName())));
                } catch (  Exception e) {
                    Toast.makeText(getApplicationContext(), "Unable to Connect Try Again...", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
        }
       else if (item.getItemId()==R.id.nav_contact)
        {
            startActivity(new Intent(MainActivity.this, ContactUs.class));
        }
       else if (item.getItemId()==R.id.nav_change_password)
        {
            startActivity(new Intent(MainActivity.this, ChangePassword.class));
        }
       else if (item.getItemId()==R.id.nav_logout)
        {
            mLogout();
        }
        return true;
    }


    //    private  void  mLogin(final  String username,final String password) {
//        class getJSONData extends AsyncTask<String, String, String> {
//
//
//            HttpURLConnection urlConnection;
//
//
//            @Override
//            protected void onPreExecute() {
//                super.onPreExecute();
//
//            }
//            @Override
//            protected String doInBackground(String... args) {
//
//                StringBuilder result = new StringBuilder();
//
//                try {
//                    URL url = new URL(BaseURL.BASEURL+"api/v1/login-new?username="+username+"&password="+password);
//                    urlConnection = (HttpURLConnection) url.openConnection();
//                    InputStream in = new BufferedInputStream(urlConnection.getInputStream());
//
//                    BufferedReader reader = new BufferedReader(new InputStreamReader(in));
//
//                    String line;
//                    while ((line = reader.readLine()) != null) {
//                        result.append(line);
//                    }
//
//                } catch (Exception e) {
//                    e.printStackTrace();
//                } finally {
//                    urlConnection.disconnect();
//                }
//
//                return result.toString();
//            }
//
//
//            @Override
//            protected void onPostExecute(String result) {
//
//                //Do something with the JSON string
//
//
//                Log.e("response","data "+result);
//
//                mShowLoginDetail(result);
//
//            }
//        }
//
//        getJSONData getJSONData=new getJSONData();
//        getJSONData.execute();
//    }
















//    private void mShowBanners() {
//
//        viewpager_banners = findViewById(R.id.viewpager_banners);
//        imageId=new ArrayList();
//
//        try {
//            JSONArray jsonArray =new JSONArray(SharePrefManager.getInstance(MainActivity.this).mGetBanners());
//
//            for (int i=0; i<jsonArray.length(); i++)
//            {
//                JSONObject data=jsonArray.getJSONObject(i);
//
//                imageId.add(data.getString("image"));
//            }
//
//        }
//        catch (  JSONException e)
//        {
//            e.printStackTrace();
//        }
//
//        viewpager_banners.setAdapter(new BannerSlidingImageAdapter(MainActivity.this, imageId));
//
//        NUM_PAGES_banner = imageId.size();
//
//        // Auto start of viewpager
//        final Handler handler =new Handler();
//
//        final Runnable Update=new Runnable() {
//            @Override
//            public void run() {
//                if (currentPage_banners == NUM_PAGES_banner) {
//                    currentPage_banners = 0;
//                }
//                viewpager_banners.setCurrentItem(currentPage_banners++, true);
//            }
//        };
//
//        swipeTimer_banners =new Timer();
//        swipeTimer_banners.schedule(new TimerTask() {
//            @Override
//            public void run() {
//                handler.post(Update);
//
//            }
//        }, 5000, 5000);
//    }

    @SuppressLint("StaticFieldLeak")
    public void mGetOutletId(String service)
    {
        Uri.Builder builder=new Uri.Builder();
        builder.appendQueryParameter("api_token",SharePrefManager.getInstance(MainActivity.this).mGetApiToken());
        String sending_url= BaseURL.BASEURL_B2C+"api/application/v1/aeps-outlet-id";
        new CallResAPIPOSTMethod(MainActivity.this,builder,sending_url,true,"POST"){
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                dialog=new ProgressDialog(MainActivity.this);
                dialog.setMessage("Please wait...");
                dialog.show();
                dialog.setCancelable(false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                dialog.dismiss();
                Log.e("response","id "+s);

                if (!s.equals(""))
                {
                    String status="",message="",icici_agent_id="",outlet_id="";
                    try{
                        JSONObject jsonObject=new JSONObject(s);

                        if (jsonObject.has("status"))
                        {
                            status=jsonObject.getString("status");
                        }

                        if (jsonObject.has("message"))
                        {
                            message=jsonObject.getString("message");
                        }


                        if (status.equalsIgnoreCase("success")) {
                            if (jsonObject.has("icici_agent_id")) {
                                icici_agent_id = jsonObject.getString("icici_agent_id");

                                SharePrefManager.getInstance(MainActivity.this).mSaveSingleData("icici_agent_id", icici_agent_id);

                            }

                            if (jsonObject.has("outlet_id")) {
                                outlet_id = jsonObject.getString("outlet_id");

                                SharePrefManager.getInstance(MainActivity.this).mSaveSingleData("outlet_id", outlet_id);

                            }

//                            if (type.equals("1")) {
                                mCallAEPSServices(service);
//                            }
//                            else if (type.equals("2"))
//                            {
//                                mCallMATMServices(service);
//                            }

                        }
                        else
                        {
                            if (!message.equals(""))
                            {
                                Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
                            }
                            else
                            {
                                Toast.makeText(MainActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                            }
                        }

                    }
                    catch (JSONException e)
                    {
                        e.printStackTrace();
                    }
                }
            }
        }.execute();
    }

    public void mCallAEPSServices(String service){

        if (SharePrefManager.getInstance(MainActivity.this).mGetOutletId().equals("")){
            Toast.makeText(this, "OutLet ID Not Found", Toast.LENGTH_SHORT).show();
        }
        else {
            Intent intent = new Intent(MainActivity.this, AEPS_Service.class);
            intent.putExtra("outlet_id", SharePrefManager.getInstance(MainActivity.this).mGetOutletId());
            intent.putExtra("mobile", SharePrefManager.getInstance(MainActivity.this).mGetUsername());
            intent.putExtra("name", SharePrefManager.getInstance(MainActivity.this).mGetName());
            intent.putExtra("service", service);
            startActivity(intent);
        }
    }

    public void mUpdateBalance()
    {
        tv_main_balance.setText("Main "+getResources().getString(R.string.rs)+SharePrefManager.getInstance(this).mGetMainBalance());
        tv_aeps_balance.setText("AEPS "+getResources().getString(R.string.rs)+SharePrefManager.getInstance(this).mGetAEPSBalance());
    }
}