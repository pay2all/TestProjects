package com.demo.apppay2all.PayoutServices;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.demo.apppay2all.R;

public class Payout extends AppCompatActivity {

    LinearLayout ll_move_to_wallet,ll_move_bank;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payout);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        ll_move_to_wallet=findViewById(R.id.ll_move_to_wallet);
        ll_move_to_wallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent=new Intent(Payout.this, PayoutMoveToWallet.class);
                startActivity(intent);
            }
        });



        ll_move_bank=findViewById(R.id.ll_move_bank);
        ll_move_bank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent=new Intent(Payout.this, PayoutNew.class);
                startActivity(intent);
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
