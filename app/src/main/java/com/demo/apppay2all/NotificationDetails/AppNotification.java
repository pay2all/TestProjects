package com.demo.apppay2all.NotificationDetails;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.demo.apppay2all.R;
import com.demo.apppay2all.SharePrefManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class AppNotification extends AppCompatActivity {

    RecyclerView recyclerview_notification;
    TextView textview_message;
    NotificationCardAdapter cardAdapter;
    List<NotificationItems> notificationItems;
    String allnotification="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        if (getSupportActionBar()!=null)
        {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        recyclerview_notification=findViewById(R.id.recyclerview_notification);
        notificationItems=new ArrayList<>();
        recyclerview_notification.setLayoutManager(new LinearLayoutManager(AppNotification.this));
        cardAdapter=new NotificationCardAdapter(AppNotification.this,notificationItems);
        recyclerview_notification.setAdapter(cardAdapter);

        textview_message=findViewById(R.id.textview_message);
        allnotification=SharePrefManager.getInstance(AppNotification.this).mGetAllNotification();
        if (allnotification.equals(""))
        {
            recyclerview_notification.setVisibility(View.GONE);
            textview_message.setText("No notification yet");
        }
        else
        {
            textview_message.setVisibility(View.GONE);
            recyclerview_notification.setVisibility(View.VISIBLE);

            try {
                JSONArray jsonArray=new JSONArray(allnotification);
                for (int i=0; i<jsonArray.length(); i++)
                {
                    JSONObject data=jsonArray.getJSONObject(i);
                    NotificationItems items=new NotificationItems();

                    items.setId(data.getString("notification_id"));
                    items.setTitle(data.getString("notification_title"));
                    items.setMessage(data.getString("notification_data"));
                    notificationItems.add(items);
                    cardAdapter.notifyDataSetChanged();
                }
            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }
        }
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