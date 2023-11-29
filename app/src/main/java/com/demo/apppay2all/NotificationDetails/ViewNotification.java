package com.demo.apppay2all.NotificationDetails;

import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.widget.TextView;

import com.demo.apppay2all.BroadCast.SMSReceiver;
import com.demo.apppay2all.R;

public class ViewNotification extends AppCompatActivity {

    TextView tv_title,tv_message;
    NotificationItems notificationItems;

    SMSReceiver receiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_notification);

        notificationItems=(NotificationItems)getIntent().getSerializableExtra("DATA");

        tv_title=findViewById(R.id.tv_title);
        tv_message=findViewById(R.id.tv_message);

        if (!notificationItems.getTitle().equals(""))
        {
            if (getSupportActionBar()!=null)
            {
                getSupportActionBar().setTitle(notificationItems.getTitle());
            }
        }

        tv_title.setText(notificationItems.getTitle());
        tv_message.setText(notificationItems.getMessage());

        receiver =new SMSReceiver();
        LocalBroadcastManager lbm= LocalBroadcastManager.getInstance(this);
        lbm.registerReceiver(receiver, new IntentFilter("filter_string"));

        Intent intent=new Intent("filter_string");
        intent.putExtra("id",notificationItems.getId());
        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
    }

    @Override
    protected void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver);
    }

    @Override
    protected void onStop() {
        super.onStop();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver);
    }
}