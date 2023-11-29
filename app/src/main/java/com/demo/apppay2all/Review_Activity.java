package com.demo.apppay2all;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.demo.apppay2all.TransactionRecept.Receipt;


public class Review_Activity extends AppCompatActivity {


    TextView textView_review,textView_failure_review;
    Button button_transaction_receipt,button_exit;
    String transaction_JSONData;
    String name,amount;

    RelativeLayout rl_contain_success_message,rl_contain_failure_message;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);
        Bundle bundle=getIntent().getExtras();
        final String activity=bundle.getString("activity");

        button_transaction_receipt= findViewById(R.id.button_transaction_receept);
        button_exit= findViewById(R.id.button_exit);
        textView_review= findViewById(R.id.textView_review);

        getSupportActionBar().hide();

        textView_failure_review= findViewById(R.id.textView_failure_review);
        rl_contain_success_message= findViewById(R.id.rl_contain_success_message);
        rl_contain_failure_message= findViewById(R.id.rl_contain_failure_message);

        if (activity.equalsIgnoreCase("transaction"))
        {
            if (bundle.getString("status").equals("success")) {
                textView_review.setText("Transaction Successfuly Completed");
                transaction_JSONData = bundle.getString("data");
                amount = bundle.getString("amount");
                name = bundle.getString("name");
                button_transaction_receipt.setVisibility(View.GONE);
            }

            else
            {
                String status=bundle.getString("status");
                rl_contain_success_message.setVisibility(View.GONE);
                button_transaction_receipt.setVisibility(View.GONE);
                rl_contain_failure_message.setVisibility(View.VISIBLE);
                textView_failure_review.setText(status.toUpperCase()+"\n"+bundle.getString("message"));
            }
        }

        else if (activity.equalsIgnoreCase("add_beneficiary"))
        {
            button_transaction_receipt.setVisibility(View.GONE);
        }

        else if (activity.equalsIgnoreCase("changepassword"))
        {
            textView_review.setText("Password Successfully Changed");
            button_transaction_receipt.setVisibility(View.GONE);
        }

        else if (activity.equalsIgnoreCase("fundrequest"))
        {
            String status=bundle.getString("status");
            if (status.equalsIgnoreCase("success")||status.equalsIgnoreCase("1"))
            {
                textView_review.setText(bundle.getString("message"));
                button_transaction_receipt.setVisibility(View.GONE);
            }

            else if (status.equalsIgnoreCase("failure")||status.equalsIgnoreCase("0"))
            {

                rl_contain_success_message.setVisibility(View.GONE);
                button_transaction_receipt.setVisibility(View.GONE);
                rl_contain_failure_message.setVisibility(View.VISIBLE);
                textView_failure_review.setText(status.toUpperCase()+"\n"+bundle.getString("message"));
            }
            else if (status.toLowerCase().equalsIgnoreCase("pending"))
            {

                rl_contain_success_message.setVisibility(View.GONE);
                button_transaction_receipt.setVisibility(View.GONE);
                rl_contain_failure_message.setVisibility(View.VISIBLE);
                textView_failure_review.setText(bundle.getString("message"));
                textView_failure_review.setTextColor(Color.parseColor("#1E3F86"));
            }
            else
            {

                rl_contain_success_message.setVisibility(View.GONE);
                button_transaction_receipt.setVisibility(View.GONE);
                rl_contain_failure_message.setVisibility(View.VISIBLE);
                textView_failure_review.setText(bundle.getString("operator_ref"));
                textView_failure_review.setTextColor(Color.parseColor("#1E3F86"));
            }
        }

        else if (activity.equalsIgnoreCase("mobile_recharge"))
        {
            String status=bundle.getString("status");
            if (status.equalsIgnoreCase("success"))
            {
                textView_review.setText("Transaction Successfuly Completed");
                button_transaction_receipt.setVisibility(View.GONE);
            }

            else if (status.equalsIgnoreCase("failure"))
            {

                rl_contain_success_message.setVisibility(View.GONE);
                button_transaction_receipt.setVisibility(View.GONE);
                rl_contain_failure_message.setVisibility(View.VISIBLE);
                textView_failure_review.setText(status.toUpperCase()+"\n"+bundle.getString("operator_ref"));
            }
            else if (status.toLowerCase().equalsIgnoreCase("pending"))
            {

                rl_contain_success_message.setVisibility(View.GONE);
                button_transaction_receipt.setVisibility(View.GONE);
                rl_contain_failure_message.setVisibility(View.VISIBLE);
                textView_failure_review.setText(status+"\n"+bundle.getString("operator_ref"));
                textView_failure_review.setTextColor(Color.parseColor("#1E3F86"));
            }
            else
            {

                rl_contain_success_message.setVisibility(View.GONE);
                button_transaction_receipt.setVisibility(View.GONE);
                rl_contain_failure_message.setVisibility(View.VISIBLE);
                textView_failure_review.setText(status+"\n"+bundle.getString("operator_ref"));
                textView_failure_review.setTextColor(Color.parseColor("#1E3F86"));
            }
        }
        else if (activity.equalsIgnoreCase("complaint"))
        {
            String status=bundle.getString("status");
            if (status.equalsIgnoreCase("success")||status.equalsIgnoreCase("1"))
            {
                textView_review.setText("Complaint Successfuly Submited");
                button_transaction_receipt.setVisibility(View.GONE);
            }

            else if (status.equalsIgnoreCase("failure")||status.equalsIgnoreCase("0"))
            {

                rl_contain_success_message.setVisibility(View.GONE);
                button_transaction_receipt.setVisibility(View.GONE);
                rl_contain_failure_message.setVisibility(View.VISIBLE);
                textView_failure_review.setText(status.toUpperCase()+"\n"+bundle.getString("operator_ref"));
            }
            else if (status.toLowerCase().equalsIgnoreCase("pending"))
            {

                rl_contain_success_message.setVisibility(View.GONE);
                button_transaction_receipt.setVisibility(View.GONE);
                rl_contain_failure_message.setVisibility(View.VISIBLE);
                textView_failure_review.setText(status+"\n"+bundle.getString("operator_ref"));
                textView_failure_review.setTextColor(Color.parseColor("#1E3F86"));
            }
            else
            {

                rl_contain_success_message.setVisibility(View.GONE);
                button_transaction_receipt.setVisibility(View.GONE);
                rl_contain_failure_message.setVisibility(View.VISIBLE);
                textView_failure_review.setText(status+"\n"+bundle.getString("operator_ref"));
                textView_failure_review.setTextColor(Color.parseColor("#1E3F86"));
            }
        }

        else if (activity.equalsIgnoreCase("dth"))
        {
            String status=bundle.getString("status");
            if (status.equalsIgnoreCase("success"))
            {
                textView_review.setText("Transaction Successfuly Completed");
                button_transaction_receipt.setVisibility(View.GONE);
            }

            else if (status.equalsIgnoreCase("failure"))
            {

                rl_contain_success_message.setVisibility(View.GONE);
                button_transaction_receipt.setVisibility(View.GONE);
                rl_contain_failure_message.setVisibility(View.VISIBLE);
                textView_failure_review.setText(status.toUpperCase()+"\n"+bundle.getString("operator_ref"));
            }
            else
            {
                rl_contain_success_message.setVisibility(View.GONE);
                button_transaction_receipt.setVisibility(View.GONE);
                rl_contain_failure_message.setVisibility(View.VISIBLE);
                textView_failure_review.setText(status+"\n"+bundle.getString("operator_ref"));
            }
        }

        else if (activity.equalsIgnoreCase("datacard"))
        {
            String status=bundle.getString("status");
            if (status.equalsIgnoreCase("success"))
            {
                textView_review.setText("Transaction Successfuly Completed");
                button_transaction_receipt.setVisibility(View.GONE);
            }

            else if (status.equalsIgnoreCase("failure"))
            {

                rl_contain_success_message.setVisibility(View.GONE);
                button_transaction_receipt.setVisibility(View.GONE);
                rl_contain_failure_message.setVisibility(View.VISIBLE);
                textView_failure_review.setText(status.toUpperCase()+"\n"+bundle.getString("operator_ref"));
            }
            else
            {
                rl_contain_success_message.setVisibility(View.GONE);
                button_transaction_receipt.setVisibility(View.GONE);
                rl_contain_failure_message.setVisibility(View.VISIBLE);
                textView_failure_review.setText(status+"\n"+bundle.getString("operator_ref"));
            }
        }

        else if (activity.equalsIgnoreCase("addmember"))
        {
            String status=bundle.getString("status");
            if (status.equalsIgnoreCase("success")||status.equalsIgnoreCase("1"))
            {
                textView_review.setText(bundle.getString("message"));
                button_transaction_receipt.setVisibility(View.GONE);
            }

            else if (status.equalsIgnoreCase("failure")||status.equalsIgnoreCase("0"))
            {

                rl_contain_success_message.setVisibility(View.GONE);
                button_transaction_receipt.setVisibility(View.GONE);
                rl_contain_failure_message.setVisibility(View.VISIBLE);
                textView_failure_review.setText(bundle.getString("message"));
            }
            else if (status.toLowerCase().equalsIgnoreCase("pending"))
            {

                rl_contain_success_message.setVisibility(View.GONE);
                button_transaction_receipt.setVisibility(View.GONE);
                rl_contain_failure_message.setVisibility(View.VISIBLE);
                textView_failure_review.setText(bundle.getString("message"));
                textView_failure_review.setTextColor(Color.parseColor("#1E3F86"));
            }
            else
            {

                rl_contain_success_message.setVisibility(View.GONE);
                button_transaction_receipt.setVisibility(View.GONE);
                rl_contain_failure_message.setVisibility(View.VISIBLE);
                textView_failure_review.setText(bundle.getString("message"));
                textView_failure_review.setTextColor(Color.parseColor("#1E3F86"));
            }
        }

        else if (activity.equalsIgnoreCase("postpaid"))
        {
            String status=bundle.getString("status");
            if (status.equalsIgnoreCase("success"))
            {
                textView_review.setText("Transaction Successfuly Completed");
                button_transaction_receipt.setVisibility(View.GONE);
            }

            else if (status.equalsIgnoreCase("failure"))
            {

                rl_contain_success_message.setVisibility(View.GONE);
                button_transaction_receipt.setVisibility(View.GONE);
                rl_contain_failure_message.setVisibility(View.VISIBLE);
                textView_failure_review.setText(status.toUpperCase()+"\n"+bundle.getString("operator_ref"));
            }
            else
            {

                rl_contain_success_message.setVisibility(View.GONE);
                button_transaction_receipt.setVisibility(View.GONE);
                rl_contain_failure_message.setVisibility(View.VISIBLE);
                textView_failure_review.setText(status.toUpperCase()+"\n"+bundle.getString("operator_ref"));
            }
        }

        else if (activity.equalsIgnoreCase("telephone"))
        {
            String status=bundle.getString("status");
            if (status.equalsIgnoreCase("success"))
            {
                textView_review.setText("Transaction Successfuly Completed");
                button_transaction_receipt.setVisibility(View.GONE);
            }

            else if (status.equalsIgnoreCase("failure"))
            {

                rl_contain_success_message.setVisibility(View.GONE);
                button_transaction_receipt.setVisibility(View.GONE);
                rl_contain_failure_message.setVisibility(View.VISIBLE);
                textView_failure_review.setText(status.toUpperCase()+"\n"+bundle.getString("operator_ref"));
            }

            else
            {
                rl_contain_success_message.setVisibility(View.GONE);
                button_transaction_receipt.setVisibility(View.GONE);
                rl_contain_failure_message.setVisibility(View.VISIBLE);
                textView_failure_review.setText(status.toUpperCase()+"\n"+bundle.getString("operator_ref"));
            }
        }


        else if (activity.equalsIgnoreCase("electricity"))
        {
            String status=bundle.getString("status");
            if (status.equalsIgnoreCase("success"))
            {
                textView_review.setText("Transaction Successfuly Completed");
                button_transaction_receipt.setVisibility(View.GONE);
            }

            else if (status.equalsIgnoreCase("failure"))
            {

                rl_contain_success_message.setVisibility(View.GONE);
                button_transaction_receipt.setVisibility(View.GONE);
                rl_contain_failure_message.setVisibility(View.VISIBLE);
                textView_failure_review.setText(status.toUpperCase()+"\n"+bundle.getString("operator_ref"));
            }

            else
            {
                rl_contain_success_message.setVisibility(View.GONE);
                button_transaction_receipt.setVisibility(View.GONE);
                rl_contain_failure_message.setVisibility(View.VISIBLE);
                textView_failure_review.setText(status.toUpperCase()+"\n"+bundle.getString("operator_ref"));
            }
        }

        else if (activity.equalsIgnoreCase("signup"))
        {
            String status=bundle.getString("status");
            if (status.equalsIgnoreCase("success")) {
                textView_review.setText("You are Successfuly registered \nuse your mobileno as a username and your password for login");
                button_transaction_receipt.setVisibility(View.GONE);
            }

            else
            {
                rl_contain_success_message.setVisibility(View.GONE);
                button_transaction_receipt.setVisibility(View.GONE);
                rl_contain_failure_message.setVisibility(View.VISIBLE);
                textView_failure_review.setText(bundle.getString("message"));
            }
        }
        else if (activity.equalsIgnoreCase("shoping"))
        {
            String status=bundle.getString("status");
            if (status.equalsIgnoreCase("success")) {
                textView_review.setText("Transaction successfully completed");
                button_transaction_receipt.setVisibility(View.GONE);
            }

            else
            {
                rl_contain_success_message.setVisibility(View.GONE);
                button_transaction_receipt.setVisibility(View.GONE);
                rl_contain_failure_message.setVisibility(View.VISIBLE);
                textView_failure_review.setText("Transaction failed");
            }
        }
        else if (activity.equalsIgnoreCase("order_cancel"))
        {
            String status=bundle.getString("status");
            String message=bundle.getString("message");
            if (status.equalsIgnoreCase("1")) {
                textView_review.setText(message);
                button_transaction_receipt.setVisibility(View.GONE);
            }

            else
            {
                rl_contain_success_message.setVisibility(View.GONE);
                button_transaction_receipt.setVisibility(View.GONE);
                rl_contain_failure_message.setVisibility(View.VISIBLE);
                textView_failure_review.setText(message);
            }
        }
        else if (activity.equalsIgnoreCase("complaints"))
        {
                textView_review.setText("Complaints Successfuly submited");
                button_transaction_receipt.setVisibility(View.GONE);
        }

        else if (activity.equalsIgnoreCase("wallet"))
        {
            String status=bundle.getString("status");
            String message=bundle.getString("message");
            if (status.equalsIgnoreCase("success"))
            {
                textView_review.setText(message);
                button_transaction_receipt.setVisibility(View.GONE);
            }

            else if (status.equalsIgnoreCase("failure"))
            {
                rl_contain_success_message.setVisibility(View.GONE);
                button_transaction_receipt.setVisibility(View.GONE);
                rl_contain_failure_message.setVisibility(View.VISIBLE);
                textView_failure_review.setText(message);
            }
            else
            {
                rl_contain_success_message.setVisibility(View.GONE);
                button_transaction_receipt.setVisibility(View.GONE);
                rl_contain_failure_message.setVisibility(View.VISIBLE);
                textView_failure_review.setText(status.toUpperCase()+"\n"+message);
            }
        }
        else if (activity.equalsIgnoreCase("water"))
        {
            String status=bundle.getString("status");
            String message=bundle.getString("operator_ref");
            if (status.equalsIgnoreCase("success"))
            {
                textView_review.setText("Transaction Successfuly Completed"+"\n"+message);
                button_transaction_receipt.setVisibility(View.GONE);
            }

            else if (status.equalsIgnoreCase("failure"))
            {
                rl_contain_success_message.setVisibility(View.GONE);
                button_transaction_receipt.setVisibility(View.GONE);
                rl_contain_failure_message.setVisibility(View.VISIBLE);
                textView_failure_review.setText(status+"\n"+message);
            }
            else
            {
                rl_contain_success_message.setVisibility(View.GONE);
                button_transaction_receipt.setVisibility(View.GONE);
                rl_contain_failure_message.setVisibility(View.VISIBLE);
                textView_failure_review.setText(status.toUpperCase()+"\n"+message);
            }
        }
        else if (activity.equalsIgnoreCase("gas"))
        {
            String status=bundle.getString("status");
            String message=bundle.getString("operator_ref");
            if (status.equalsIgnoreCase("success"))
            {
                textView_review.setText(message);
                button_transaction_receipt.setVisibility(View.GONE);
            }

            else if (status.equalsIgnoreCase("failure"))
            {
                rl_contain_success_message.setVisibility(View.GONE);
                button_transaction_receipt.setVisibility(View.GONE);
                rl_contain_failure_message.setVisibility(View.VISIBLE);
                textView_failure_review.setText(message);
            }
            else
            {
                rl_contain_success_message.setVisibility(View.GONE);
                button_transaction_receipt.setVisibility(View.GONE);
                rl_contain_failure_message.setVisibility(View.VISIBLE);
                textView_failure_review.setText(message);
            }
        }
        else if (activity.equalsIgnoreCase("add_member"))
        {
            String status=bundle.getString("status");
            String message=bundle.getString("message");
            if (status.equalsIgnoreCase("success")||status.equals("1"))
            {
                textView_review.setText(message);
                button_transaction_receipt.setVisibility(View.GONE);
            }

            else if (status.equalsIgnoreCase("failure"))
            {
                rl_contain_success_message.setVisibility(View.GONE);
                button_transaction_receipt.setVisibility(View.GONE);
                rl_contain_failure_message.setVisibility(View.VISIBLE);
                textView_failure_review.setText(message);
            }
            else
            {
                rl_contain_success_message.setVisibility(View.GONE);
                button_transaction_receipt.setVisibility(View.GONE);
                rl_contain_failure_message.setVisibility(View.VISIBLE);
                textView_failure_review.setText(message);
            }
        }
        else if (activity.equalsIgnoreCase("onboard"))
        {
            String status=bundle.getString("status");
            String message=bundle.getString("message");
            if (status.equalsIgnoreCase("success")||status.equals("1"))
            {
                textView_review.setText(message);
                button_transaction_receipt.setVisibility(View.GONE);
            }

            else if (status.equalsIgnoreCase("failure"))
            {
                rl_contain_success_message.setVisibility(View.GONE);
                button_transaction_receipt.setVisibility(View.GONE);
                rl_contain_failure_message.setVisibility(View.VISIBLE);
                textView_failure_review.setText(message);
            }
            else
            {
                rl_contain_success_message.setVisibility(View.GONE);
                button_transaction_receipt.setVisibility(View.GONE);
                rl_contain_failure_message.setVisibility(View.VISIBLE);
                textView_failure_review.setText(message);
            }
        }

        else if (activity.equalsIgnoreCase("tmw"))
        {
            String status=bundle.getString("status");
            String message=bundle.getString("message");
            if (status.equalsIgnoreCase("1"))
            {
                textView_review.setText(message);
                button_transaction_receipt.setVisibility(View.GONE);
            }

            else
            {
                rl_contain_success_message.setVisibility(View.GONE);
                button_transaction_receipt.setVisibility(View.GONE);
                rl_contain_failure_message.setVisibility(View.VISIBLE);
                textView_failure_review.setText(message);
            }
        }
        else if (activity.equalsIgnoreCase("payout_wallet"))
        {
            String status=bundle.getString("status");
            String message=bundle.getString("message");
            if (status.equalsIgnoreCase("1")||status.equalsIgnoreCase("success"))
            {
                textView_review.setText(message);
                button_transaction_receipt.setVisibility(View.GONE);
            }

            else
            {
                rl_contain_success_message.setVisibility(View.GONE);
                button_transaction_receipt.setVisibility(View.GONE);
                rl_contain_failure_message.setVisibility(View.VISIBLE);
                textView_failure_review.setText(message);
            }
        }

        else if (activity.equalsIgnoreCase("fund"))
        {
            String status=bundle.getString("status");
            String message=bundle.getString("message");
            if (status.equalsIgnoreCase("success"))
            {
                textView_review.setText(message);
                button_transaction_receipt.setVisibility(View.GONE);
            }

            else if (status.equalsIgnoreCase("failure"))
            {
                rl_contain_success_message.setVisibility(View.GONE);
                button_transaction_receipt.setVisibility(View.GONE);
                rl_contain_failure_message.setVisibility(View.VISIBLE);
                textView_failure_review.setText(status+"\n"+message);
            }
            else
            {
                rl_contain_success_message.setVisibility(View.GONE);
                button_transaction_receipt.setVisibility(View.GONE);
                rl_contain_failure_message.setVisibility(View.VISIBLE);
                textView_failure_review.setText(status.toUpperCase()+"\n"+message);
            }
        }
        else if (activity.equalsIgnoreCase("money_transfer"))
        {
            textView_review.setText("Wallet opened successfully.");
            button_transaction_receipt.setVisibility(View.GONE);
        }

        else
        {

        }
        button_transaction_receipt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Review_Activity.this, Receipt.class);
                Bundle bundle1=new Bundle();
                bundle1.putString("data",transaction_JSONData);
                bundle1.putString("name",name);
                bundle1.putString("amount",amount);
                bundle1.putString("activity","transaction");
                intent.putExtras(bundle1);
                startActivity(intent);
                finish();
            }
        });

        button_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (activity.equalsIgnoreCase("signup")) {
                    Intent intent = new Intent(Review_Activity.this, Login.class);
                    startActivity(intent);
                    finish();
                } else {
                    finish();
                }
            }
        });
    }
}
