package com.demo.apppay2all.AllReportsDetails;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.demo.apppay2all.AccountValidationReportDetails.AccountValidationReport;
import com.demo.apppay2all.AepsReportDetails.AepsReports;
import com.demo.apppay2all.AllTransactionDetail.AllTransaction;
import com.demo.apppay2all.AllTransactionDetail.AllTransactionKotlin;
import com.demo.apppay2all.AppSettings.Settings;
import com.demo.apppay2all.IncomeDetails.IncomeReport;
import com.demo.apppay2all.LedgerDetail.Ledger;
import com.demo.apppay2all.MainActivitySingle;
import com.demo.apppay2all.MoneyTransferDetails.MoneyDetails;
import com.demo.apppay2all.OperatorReportDetail.OperatorReport;
import com.demo.apppay2all.R;
import com.demo.apppay2all.RechargeReportMVVM.RechargeReport;
import com.demo.apppay2all.RechargeReports.Reports;
import com.demo.apppay2all.SharePrefManager;

public class AllReports extends AppCompatActivity {

    RelativeLayout rl_all_txn,rl_ledger_report;
    LinearLayout rl_operator_report,rl_account_verification_report,rl_income_report,rl_recharge_report,rl_money_transfer_report,rl_aeps_report,rl_aeps_ledeger;
    ImageView imageview_home;
    LinearLayout ll_setting;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_reports);

        imageview_home=findViewById(R.id.imageview_home);
        imageview_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AllReports.this, MainActivitySingle.class));
                finish();
            }
        });

        ll_setting=findViewById(R.id.ll_setting);
        ll_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AllReports.this, Settings.class));
                finish();
            }
        });

        if (getSupportActionBar()!=null)
        {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        rl_all_txn=findViewById(R.id.rl_all_txn);
        rl_all_txn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                startActivity(new Intent(AllReports.this, AllTransaction.class));
                startActivity(new Intent(AllReports.this, AllTransactionKotlin.class));
            }
        });

        rl_ledger_report=findViewById(R.id.rl_ledger_report);
        rl_ledger_report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AllReports.this, Ledger.class));
            }
        });

        rl_recharge_report=findViewById(R.id.rl_recharge_report);
        if (!SharePrefManager.getInstance(AllReports.this).mGetRecharge())
        {
            rl_recharge_report.setVisibility(View.GONE);
        }
        rl_recharge_report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AllReports.this, Reports.class));
//                startActivity(new Intent(AllReports.this, RechargeReport.class));
            }
        });

        rl_account_verification_report=findViewById(R.id.rl_account_verification_report);
        rl_account_verification_report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AllReports.this, AccountValidationReport.class));
            }
        });

        rl_money_transfer_report=findViewById(R.id.rl_money_transfer_report);
        if (!SharePrefManager.getInstance(AllReports.this).mGetMoney())
        {
            rl_money_transfer_report.setVisibility(View.GONE);
        }

        rl_money_transfer_report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AllReports.this, MoneyDetails.class));

            }
        });

        rl_operator_report=findViewById(R.id.rl_operator_report);
        rl_operator_report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AllReports.this, OperatorReport.class));
            }
        });

        rl_income_report=findViewById(R.id.rl_income_report);
        rl_income_report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AllReports.this, IncomeReport.class));
            }
        });

        rl_aeps_report=findViewById(R.id.rl_aeps_report);
        if (!SharePrefManager.getInstance(AllReports.this).mGetAEPS())
        {
            rl_aeps_report.setVisibility(View.GONE);
        }

        rl_aeps_report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(AllReports.this, AepsReports.class);
                intent.putExtra("type","aeps");
                startActivity(intent);
            }
        });

        rl_aeps_ledeger=findViewById(R.id.rl_aeps_ledeger);
        if (!SharePrefManager.getInstance(AllReports.this).mGetAEPS())
        {
            rl_aeps_ledeger.setVisibility(View.GONE);
        }

        rl_aeps_ledeger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(AllReports.this, AepsReports.class);
                intent.putExtra("type","aeps_ledger");
                startActivity(intent);
            }
        });

        if (SharePrefManager.getInstance(AllReports.this).mGetRoleId()<8)
        {
            rl_account_verification_report.setVisibility(View.GONE);
            rl_aeps_ledeger.setVisibility(View.GONE);
            rl_aeps_report.setVisibility(View.GONE);
            rl_income_report.setVisibility(View.GONE);
            rl_operator_report.setVisibility(View.GONE);
            rl_money_transfer_report.setVisibility(View.GONE);
            rl_recharge_report.setVisibility(View.GONE);
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId()==android.R.id.home)
        {
            startActivity(new Intent(AllReports.this, MainActivitySingle.class));
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(AllReports.this, MainActivitySingle.class));
        finish();
    }
}