package com.demo.apppay2all;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import org.jetbrains.annotations.NotNull;


public class ContactUs extends AppCompatActivity {

    TextView tv_company_name,tv_support_mobile,tv_whatsapp_mobile,tv_email,tv_web,tv_address1,tv_address2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);

        if (getSupportActionBar()!=null)
        {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        tv_company_name=findViewById(R.id.tv_company_name);
        tv_company_name.setText(SharePrefManager.getInstance(ContactUs.this).mGetCompanyName());

        tv_support_mobile=findViewById(R.id.tv_support_mobile);
        tv_support_mobile.setText("Support : "+SharePrefManager.getInstance(ContactUs.this).mGetSupportNumber());
        tv_support_mobile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkphonecall()){
                    Intent callIntent = new Intent(Intent.ACTION_DIAL);
                    callIntent.setData(Uri.parse("tel:"+SharePrefManager.getInstance(ContactUs.this).mGetSupportNumber()));
                    startActivity(callIntent);

                }
                else {
                    requestcheckphonecall();
                }
            }
        });

        tv_whatsapp_mobile=findViewById(R.id.tv_whatsapp_mobile);
        tv_whatsapp_mobile.setText("Whatsapp : "+SharePrefManager.getInstance(ContactUs.this).mGetChatNumber());
        tv_whatsapp_mobile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = "Hi";

                startActivity(
                        new Intent(Intent.ACTION_VIEW,
                                Uri.parse("https://api.whatsapp.com/send?phone=+91"+SharePrefManager.getInstance(ContactUs.this).mGetChatNumber()+"&text="+ message)));
            }
        });

        tv_email=findViewById(R.id.tv_email);
        tv_email.setText("Email : "+SharePrefManager.getInstance(ContactUs.this).mGetCompanyEmail());
        tv_email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(Intent.createChooser( new Intent("android.intent.action.SENDTO",Uri.parse("mailto:"+SharePrefManager.getInstance(ContactUs.this).mGetCompanyEmail())),"sendvia"));

            }
        });

        tv_web=findViewById(R.id.tv_web);
        tv_web.setText("Web : "+SharePrefManager.getInstance(ContactUs.this).mGetWebSite());
        tv_web.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(Intent.ACTION_VIEW, Uri.parse(SharePrefManager.getInstance(ContactUs.this).mGetWebSite()));
                startActivity(Intent.createChooser(i,"Open With"));
            }
        });

        tv_address1=findViewById(R.id.tv_address1);
        tv_address1.setText("Address 1 : "+SharePrefManager.getInstance(ContactUs.this).mGetCompanyAddress1());

        tv_address2=findViewById(R.id.tv_address2);
        tv_address2.setText("Address 2 : "+SharePrefManager.getInstance(ContactUs.this).mGetCompanyAddress2());
    }

    public boolean checkphonecall() {
        int result = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CALL_PHONE);
        if (result== PackageManager.PERMISSION_GRANTED)
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    private void requestcheckphonecall()
    {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CALL_PHONE))
        {
        }
        else {
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.CALL_PHONE},1);
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull @NotNull MenuItem item) {
        if (item.getItemId()==android.R.id.home)
        {
//            overridePendingTransition(R.anim.no_animation,R.anim.slide_down);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
//        overridePendingTransition(R.anim.no_animation,R.anim.slide_down);
    }
}