package com.demo.apppay2all.ui.notifications;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.demo.apppay2all.R;
import com.demo.apppay2all.SharePrefManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class NotificationFragment extends Fragment {

    RecyclerView recyclerview_notification;
    TextView textview_message;
    NotificationCardAdapter cardAdapter;
    List<NotificationItems> notificationItems;
    String allnotification="";

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        NotificationViewModel slideshowViewModel =
                new ViewModelProvider(this).get(NotificationViewModel.class);


        View root = inflater.inflate(R.layout.fragment_notification,container,false);

        recyclerview_notification=root.findViewById(R.id.recyclerview_notification);
        notificationItems=new ArrayList<>();
        recyclerview_notification.setLayoutManager(new LinearLayoutManager(getActivity()));
        cardAdapter=new NotificationCardAdapter(getActivity(),notificationItems);
        recyclerview_notification.setAdapter(cardAdapter);

        textview_message=root.findViewById(R.id.textview_message);
        allnotification= SharePrefManager.getInstance(getActivity()).mGetAllNotification();
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
            
            if (notificationItems.size()==0)
            {
                textview_message.setVisibility(View.VISIBLE);
                recyclerview_notification.setVisibility(View.GONE);
                textview_message.setText("Notification Not Available");
            }
            else
            {
                textview_message.setVisibility(View.GONE);
                recyclerview_notification.setVisibility(View.VISIBLE);
            }
        }

//        final TextView textView = binding.textSlideshow;
//        slideshowViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}