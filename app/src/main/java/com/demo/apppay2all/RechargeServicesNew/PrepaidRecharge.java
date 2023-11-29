package com.demo.apppay2all.RechargeServicesNew;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.demo.apppay2all.R;

public class PrepaidRecharge extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recharge_new);

        if (getSupportActionBar()!=null)
        {
            getSupportActionBar().hide();
        }
    }
}
