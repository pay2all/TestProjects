package com.demo.apppay2all.FundRequesDetails;


import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.demo.apppay2all.R;


public class FundRequestList extends AppCompatActivity {

    RelativeLayout rl_balancerequest,rl_bankdetails,rl_accounts_team;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_fragment__balance_request);
        final TextView textView =findViewById(R.id.text_balancerequest);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        rl_balancerequest=findViewById(R.id.rl_balancerequest);
        rl_balancerequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent=new Intent(FundRequestList.this, FundRequest.class);
                startActivity(intent);
            }
        });

        rl_bankdetails=findViewById(R.id.rl_bankdetails);
        rl_bankdetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent=new Intent(FundRequestList.this, BankList.class);
                startActivity(intent);
            }
        });

        rl_accounts_team=findViewById(R.id.rl_fund_request_report);
        rl_accounts_team.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               startActivity(new Intent(FundRequestList.this,Payment_request_reports.class));
            }
        });

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
