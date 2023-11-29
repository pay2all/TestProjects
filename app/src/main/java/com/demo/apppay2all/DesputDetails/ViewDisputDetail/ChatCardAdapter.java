package com.demo.apppay2all.DesputDetails.ViewDisputDetail;

import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.demo.apppay2all.R;
import com.demo.apppay2all.SharePrefManager;

import java.util.List;

public class ChatCardAdapter extends RecyclerView.Adapter<ChatCardAdapter.ViewHolder>{

    Context context;
    List<ChatItem> chatItems;
    public ChatCardAdapter(Context context,List<ChatItem> chatItems)
    {
        this.context=context;
        this.chatItems=chatItems;
    }
    @Override
    public int getItemCount() {
        return chatItems==null?0:chatItems.size();
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ChatItem item=chatItems.get(position);
        holder.tv_chat.setText(item.getMessage());
        holder.tv_time.setText(item.getCreated_at());

        Log.e("chat id "+item.getUser_id(),"user id "+SharePrefManager.getInstance(context).mGetId());

        if (item.getUser_id().equals(SharePrefManager.getInstance(context).mGetId()))
        {
            holder.ll_background.setBackground(context.getResources().getDrawable(R.drawable.outgoing_chat_bubble));
//            holder.ll_background.setGravity(Gravity.RIGHT);

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT);
            params.weight = 1.0f;
            params.gravity = Gravity.RIGHT;

            holder.ll_background.setLayoutParams(params);

        }
        else
        {
            holder.ll_background.setBackground(context.getResources().getDrawable(R.drawable.incoming_chat_bubble));
        }

        holder.setIsRecyclable(false);

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(context).inflate(R.layout.chat_item,parent,false);
        return new ViewHolder(view);
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {

        LinearLayout ll_background,ll_time_check;
        TextView tv_chat,tv_time;
        ImageView iv_check2;
        ViewHolder(View view)
        {
            super(view);

            ll_background=view.findViewById(R.id.ll_background);
            ll_time_check=view.findViewById(R.id.ll_time_check);
            tv_chat=view.findViewById(R.id.tv_chat);
            tv_time=view.findViewById(R.id.tv_time);
            iv_check2=view.findViewById(R.id.iv_check2);
        }
    }
}
