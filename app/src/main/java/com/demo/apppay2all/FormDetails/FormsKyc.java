package com.demo.apppay2all.FormDetails;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.demo.apppay2all.R;

public class FormsKyc extends AppCompatActivity {

    ImageView iv_1,iv_2,iv_3,iv_4;
    TextView tv_1,tv_2,tv_3,tv_4;

    FragmentManager manager;
    String visible_fragment="1";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forms_kyc);

        iv_1=findViewById(R.id.iv_1);
        iv_2=findViewById(R.id.iv_2);
        iv_3=findViewById(R.id.iv_3);
        iv_4=findViewById(R.id.iv_4);

        tv_1=findViewById(R.id.tv_1);
        tv_2=findViewById(R.id.tv_2);
        tv_3=findViewById(R.id.tv_3);
        tv_4=findViewById(R.id.tv_4);

        manager=getSupportFragmentManager();
        manager.beginTransaction().add(R.id.rl_conteainer,new ChangePasswordFragment()).addToBackStack(null).commit();
        mSetSelected(visible_fragment);

        if (getSupportActionBar()!=null) {
            getSupportActionBar().setTitle("Change Password");
            getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.background));
        }

    }

    protected void mSetSelected(String number)
    {
        switch (number) {
            case "1":
                iv_1.setBackgroundResource((R.drawable.circle_shape_large));
                iv_2.setBackgroundResource(R.drawable.circle_shape);
                iv_3.setBackgroundResource(R.drawable.circle_shape);
                iv_4.setBackgroundResource(R.drawable.circle_shape);
                getSupportActionBar().setTitle("Change Password");
                break;
            case "2":
                iv_1.setBackgroundResource((R.drawable.circle_shape));
                iv_2.setBackgroundResource(R.drawable.circle_shape_large);
                iv_3.setBackgroundResource(R.drawable.circle_shape);
                iv_4.setBackgroundResource(R.drawable.circle_shape);
                getSupportActionBar().setTitle("KYC");
                break;
            case "3":
                iv_1.setBackgroundResource((R.drawable.circle_shape));
                iv_2.setBackgroundResource(R.drawable.circle_shape);
                iv_3.setBackgroundResource(R.drawable.circle_shape_large);
                iv_4.setBackgroundResource(R.drawable.circle_shape);
                getSupportActionBar().setTitle("Personal Information");
                break;
            case "4":
                iv_1.setBackgroundResource((R.drawable.circle_shape));
                iv_2.setBackgroundResource(R.drawable.circle_shape);
                iv_3.setBackgroundResource(R.drawable.circle_shape);
                iv_4.setBackgroundResource(R.drawable.circle_shape_large);
                getSupportActionBar().setTitle("Address Details");
                break;
        }
    }

    public void mChangeFragment(Fragment fragment,String steps)
    {
        manager.beginTransaction().replace(R.id.rl_conteainer, fragment).addToBackStack(null).commit();
        visible_fragment = steps;
        mSetSelected(visible_fragment);
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}