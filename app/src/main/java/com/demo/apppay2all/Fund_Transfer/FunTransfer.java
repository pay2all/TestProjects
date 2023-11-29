package com.demo.apppay2all.Fund_Transfer;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.demo.apppay2all.DetectConnection;
import com.demo.apppay2all.R;

public class FunTransfer extends AppCompatActivity {


    TextView textview_name,textview_mobileno;
    EditText edittext_amount;
    Button button_fund_transfer;

    ProgressDialog dialog;

    String username,password,childid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fun_transfer);


        SharedPreferences sharedPreferences=getSharedPreferences("user",0);
        username=sharedPreferences.getString("username","");
        password=sharedPreferences.getString("password","");


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Bundle bundle=getIntent().getExtras();
        childid=bundle.getString("childid");
        textview_name=(TextView)findViewById(R.id.textview_name);
        textview_mobileno=(TextView)findViewById(R.id.textview_mobileno);
        edittext_amount=(EditText) findViewById(R.id.edittext_amount);
        textview_name.setText("Fund Transfer to : "+bundle.getString("name"));
        textview_mobileno.setText("Mobile no : "+bundle.getString("mobile"));


        button_fund_transfer=(Button) findViewById(R.id.button_fund_transfer);
        button_fund_transfer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (DetectConnection.checkInternetConnection(FunTransfer.this))
                {
                    if (edittext_amount.getText().toString().equals(""))
                    {
                        Toast.makeText(FunTransfer.this, "Please enter amount", Toast.LENGTH_SHORT).show();
                    }
                    else{

                        String amount=edittext_amount.getText().toString();
//                    mFundTransfer(username,password,childid,amount);
                }
                }

                else
                {
                    Toast.makeText(FunTransfer.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
                }
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
