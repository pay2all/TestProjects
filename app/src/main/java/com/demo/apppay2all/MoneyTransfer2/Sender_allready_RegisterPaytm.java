package com.demo.apppay2all.MoneyTransfer2;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.demo.apppay2all.R;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class Sender_allready_RegisterPaytm extends AppCompatActivity {

    private TabLayout tabLayout;
    public static ViewPager viewPager;
    String sender_number;
    String name,status,available_limit,limit_spend,senderid;

    CardView cardview_add_beneficiary,cardview_saved_beneficiary,cardview_validate_ifsc,cardview_validate_bank,cardview_money_charges;

//    TextView ,textView_sendermobilenumber;
    TextView textView_sendername,textView_senderstatus,textView_senderavailablelitmit,textView_sendertotalspend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sender_allready__register);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


//        textView_sendermobilenumber=(TextView)findViewById(R.id.textView_sendernumber);
        textView_sendername= findViewById(R.id.textView_sendername);
        textView_senderstatus= findViewById(R.id.textView_senderstatus);
        textView_senderavailablelitmit= findViewById(R.id.textView_senderavailablelimit);
        textView_sendertotalspend= findViewById(R.id.textView_sendertotalspend);

        final Bundle bundle=getIntent().getExtras();
//        senderid=bundle.getString("senderid");
        sender_number=bundle.getString("sender_number");
        name=bundle.getString("name");
        status=bundle.getString("status");
        available_limit=bundle.getString("available_limit");
        limit_spend=bundle.getString("total_spend");
        senderid=bundle.getString("sender_id");

        if (getSupportActionBar()!=null){
            if (!sender_number.equals("")){
                getSupportActionBar().setTitle("Sender "+sender_number);
            }

            else {
                getSupportActionBar().setTitle("Money Transfer");
            }
        }
        textView_senderavailablelitmit.setVisibility(View.GONE);

        textView_sendername.setText("Dear "+name +" ("+sender_number+")");
        textView_senderstatus.setVisibility(View.GONE);
        textView_senderavailablelitmit.setText("Total Limit : "+available_limit);
        textView_sendertotalspend.setText("Available Limit : "+available_limit);



        cardview_add_beneficiary=findViewById(R.id.cardview_add_beneficiary);
        cardview_add_beneficiary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (getSupportActionBar()!=null)
                {
                    getSupportActionBar().setTitle("Add Beneficiary");
                }
//                Bundle bundle1=new Bundle();
//                bundle1.putString("sender_number",sender_number);
//                bundle1.putString("name",name);
//                beneficiaryList.setArguments(bundle);
                FragmentManager manager=getSupportFragmentManager();
                manager.beginTransaction().replace(R.id.rl_contain,new Add_benificiary_FragmentPaymtm().newInstance(sender_number,senderid,"add")).addToBackStack(null).commit();
            }
        });

        cardview_saved_beneficiary=findViewById(R.id.cardview_saved_beneficiary);
        cardview_saved_beneficiary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getSupportActionBar()!=null)
                {
                    getSupportActionBar().setTitle("Saved List");
                }

                SavedBeneficiaryList beneficiaryList=new SavedBeneficiaryList();
                Bundle bundle1=new Bundle();
                bundle1.putString("sender_number",sender_number);
                bundle1.putString("name",name);
                beneficiaryList.setArguments(bundle1);
                FragmentManager manager=getSupportFragmentManager();
                manager.beginTransaction().replace(R.id.rl_contain,beneficiaryList).addToBackStack(null).commit();
            }
        });

        cardview_validate_ifsc=findViewById(R.id.cardview_validate_ifsc);
        cardview_validate_ifsc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getSupportActionBar()!=null)
                {
                    getSupportActionBar().setTitle("IFSC Validation");
                }

                FragmentManager manager=getSupportFragmentManager();
                manager.beginTransaction().replace(R.id.rl_contain,new IfscVerification()).addToBackStack(null).commit();
            }
        });


        cardview_validate_bank=findViewById(R.id.cardview_validate_bank);
        cardview_validate_bank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getSupportActionBar()!=null)
                {
                    getSupportActionBar().setTitle("Validate Bank");
                }

                FragmentManager manager=getSupportFragmentManager();
                manager.beginTransaction().replace(R.id.rl_contain,new Add_benificiary_FragmentPaymtm().newInstance(sender_number,senderid,"verify")).addToBackStack(null).commit();
            }
        });
        cardview_money_charges=findViewById(R.id.cardview_money_charges);
        cardview_money_charges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getSupportActionBar()!=null)
                {
                    getSupportActionBar().setTitle("Money Transfer Charge");
                }

                FragmentManager manager=getSupportFragmentManager();
                manager.beginTransaction().replace(R.id.rl_main,new MoneyTransferCharge()).addToBackStack(null).commit();
            }
        });



//        textView_sendertotalspend.setVisibility(View.GONE);

//        Beneficiary_Items beneficiary_items=new Beneficiary_Items();
//        beneficiary_items.setSender_number(sender_number);

//        viewPager = findViewById(R.id.viewpager);
//        setupViewPager(viewPager);
//
//        tabLayout = findViewById(R.id.tabs);
//        tabLayout.setupWithViewPager(viewPager);
////        setupTabIcons();
//
//
//        int page=bundle.getInt("tab",0);
//        viewPager.setCurrentItem(page);
    }
//    private void setupTabIcons(){
//        tabLayout.getTabAt(0).setIcon(tabIcons[0]);
//        tabLayout.getTabAt(1).setIcon(tabIcons[1]);
//        tabLayout.getTabAt(2).setIcon(tabIcons[2]);
//    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new Add_benificiary_FragmentPaymtm().newInstance(sender_number,senderid,""), "Add Beneficiary");
        adapter.addFragment(new Saved_benificiary_FragmentPaytm().newInstance(sender_number,senderid,name), "Saved");

        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:

                if (getSupportFragmentManager().getBackStackEntryCount()==0)
                {
                    finish();
                }
                else {
                    if (getSupportActionBar()!=null)
                    {
                        getSupportActionBar().setTitle("Sender "+sender_number);
                    }

                    getSupportFragmentManager().popBackStackImmediate();
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }


    public void Update()
    {


        Bundle bundle=getIntent().getExtras();
        sender_number=bundle.getString("sender_number");
        name=bundle.getString("name");
        status=bundle.getString("status");
        available_limit=bundle.getString("available_limit");
        limit_spend=bundle.getString("total_spend");


        textView_sendername.setText("Dear "+name +" ("+sender_number+")");
        textView_senderstatus.setVisibility(View.GONE);
        textView_senderavailablelitmit.setText("Total Limit : "+available_limit);
        textView_sendertotalspend.setText("Available Limit : "+available_limit);
//        textView_sendertotalspend.setVisibility(View.GONE);

//        Beneficiary_Items beneficiary_items=new Beneficiary_Items();
//        beneficiary_items.setSender_number(sender_number);

        viewPager = findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
//        setupTabIcons();


        int page=bundle.getInt("tab",0);
        viewPager.setCurrentItem(page);
    }

    @Override
    public void onBackPressed() {

        if (getSupportFragmentManager().getBackStackEntryCount()==0)
        {
            super.onBackPressed();
        }
        else {
            if (getSupportActionBar()!=null)
            {
                getSupportActionBar().setTitle("Sender "+sender_number);
            }

            getSupportFragmentManager().popBackStackImmediate();
        }
    }

    public void mShowSavedBeneficiary()
    {
        getSupportFragmentManager().popBackStackImmediate();
        if (getSupportActionBar()!=null)
        {
            getSupportActionBar().setTitle("Saved List");
        }

        SavedBeneficiaryList beneficiaryList=new SavedBeneficiaryList();
        Bundle bundle1=new Bundle();
        bundle1.putString("sender_number",sender_number);
        bundle1.putString("name",name);
        beneficiaryList.setArguments(bundle1);
        FragmentManager manager=getSupportFragmentManager();
        manager.beginTransaction().replace(R.id.rl_contain,beneficiaryList).addToBackStack(null).commit();
    }

}