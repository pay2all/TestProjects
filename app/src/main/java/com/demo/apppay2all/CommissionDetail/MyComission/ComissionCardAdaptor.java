package com.demo.apppay2all.CommissionDetail.MyComission;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.demo.apppay2all.BaseURL.BaseURL;
import com.demo.apppay2all.CallResAPIGetMethod;
import com.demo.apppay2all.R;
import com.demo.apppay2all.SharePrefManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ComissionCardAdaptor extends RecyclerView.Adapter<ComissionCardAdaptor.ViewHolder> {

    Context context;
    List <ComissionItems>comissionItems;

    ProgressDialog dialog;

    final boolean[] visible = {false};

    public ComissionCardAdaptor(Context context, List<ComissionItems>comissionItems){
        this.context=context;
        this.comissionItems=comissionItems;
    }
    @Override
    public int getItemCount() {

        return comissionItems==null?0:comissionItems.size();
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        final ComissionItems items=comissionItems.get(position);
        holder.tv_id.setText(items.getProvider_id());
        holder.tv_provider.setText(items.getProvider_name());
        holder.tv_service.setText(items.getService_name());

        if (!items.getProvider_icon().equals("")){
            Glide.with(context).load(items.getProvider_icon()).into(holder.imageview_icon);
        }


        holder.iv_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (visible[0]) {
                    visible[0] = false;
                    holder.ll_commission.setVisibility(View.GONE);
                    holder.iv_add.setBackground(context.getResources().getDrawable(R.drawable.add_icon));
                }
                else
                {
                    visible[0]=true;
                    holder.iv_add.setBackground(context.getResources().getDrawable(R.drawable.minus_icon));

                    holder.ll_commission.setVisibility(View.VISIBLE);
                    mGetCommission(items.getProvider_id(),holder.recyclerview_commission,context,holder.ll_commission,holder.iv_add);
                }
            }
        });

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View  view= LayoutInflater.from(context).inflate(R.layout.mycommission_items,parent,false);
        ViewHolder viewHolder=new ViewHolder(view);
         return  viewHolder;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

       ImageView imageview_icon,iv_add;
       TextView tv_id,tv_provider,tv_service;
       RecyclerView recyclerview_commission;

       LinearLayout ll_commission;

        ViewHolder(View view){
            super(view);
            imageview_icon=view.findViewById(R.id.imageview_icon);
            iv_add=view.findViewById(R.id.iv_add);
            tv_id=view.findViewById(R.id.tv_id);
            tv_provider=view.findViewById(R.id.tv_provider);
            tv_service=view.findViewById(R.id.tv_service);
            recyclerview_commission=view.findViewById(R.id.recyclerview_commission);
            ll_commission=view.findViewById(R.id.ll_commission);
        }
    }

    @SuppressLint("StaticFieldLeak")
    protected void mGetCommission(String provider_id, RecyclerView recyclerView, final Context mContext, final LinearLayout linearLayout, final ImageView iv)
    {
        final List<ItemList>  itemLists;
        final CardAdapter  cardAdapter;
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        itemLists=new ArrayList<>();
        cardAdapter=new CardAdapter(mContext,itemLists);
        recyclerView.setAdapter(cardAdapter);

        String sending_url= BaseURL.BASEURL_B2C+ "api/commission/my-commission?api_token="+ SharePrefManager.getInstance(mContext).mGetApiToken() +"&provider_id="+provider_id;

        new CallResAPIGetMethod(mContext,sending_url)
        {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                dialog=new ProgressDialog(mContext);
                dialog.setMessage("Please wait...");
                dialog.setCancelable(false);
                dialog.show();
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                dialog.dismiss();
                Log.e("internal ","data "+s);

                if (!s.equals(""))
                {
                    String status="",message="";
                    try
                    {
                        JSONObject jsonObject=new JSONObject(s);
                        if (jsonObject.has("status"))
                        {
                            status=jsonObject.getString("status");
                        }
                        if (jsonObject.has("message"))
                        {
                            message=jsonObject.getString("message");
                        }

                        if (status.equalsIgnoreCase("success"))
                        {
                            JSONArray jsonArray=jsonObject.getJSONArray("commission");

                            for (int i=0;i<jsonArray.length(); i++ )
                            {
                                JSONObject data=jsonArray.getJSONObject(i);
                                ItemList item=new ItemList();
                                item.setMin_amount(data.getString("min_amount"));
                                item.setMax_amount(data.getString("max_amount"));
                                item.setCommission(data.getString("commission"));
                                item.setType(data.getString("type"));

                                itemLists.add(item);
                                cardAdapter.notifyDataSetChanged();
                            }

                        }
                        else if (status.equalsIgnoreCase("failure"))
                        {
                            visible[0] = false;

                            linearLayout.setVisibility(View.GONE);
                            iv.setBackground(context.getResources().getDrawable(R.drawable.add_icon));

                            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                        }
                    }
                    catch (JSONException e)
                    {
                        e.printStackTrace();
                    }
                }
                else
                {
                    Toast.makeText(mContext, "Something went wrong", Toast.LENGTH_SHORT).show();
                }
            }
        }.execute();

    }

    public class ItemList
    {
        public String getMin_amount() {
            return min_amount;
        }

        public void setMin_amount(String min_amount) {
            this.min_amount = min_amount;
        }

        public String getMax_amount() {
            return max_amount;
        }

        public void setMax_amount(String max_amount) {
            this.max_amount = max_amount;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getCommission() {
            return commission;
        }

        public void setCommission(String commission) {
            this.commission = commission;
        }

        String min_amount;
        String max_amount;
        String type;
        String commission;
    }

    public class CardAdapter extends RecyclerView.Adapter<CardAdapter.ViewHolder>
    {

        Context context;
        List<ItemList>  itemLists;
        public CardAdapter(Context context,List<ItemList> itemLists)
        {
            this.context=context;
            this.itemLists=itemLists;
        }
        @Override
        public int getItemCount() {
            return itemLists==null?0:itemLists.size();
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            ItemList item=itemLists.get(position);
            holder.tv_min.setText(item.getMin_amount());
            holder.tv_max.setText(item.getMax_amount());
            holder.tv_commission.setText(item.getCommission()+" "+item.getType());
        }


        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view=LayoutInflater.from(context).inflate(R.layout.intenal_commission_item_layout,parent,false);
            return new ViewHolder(view);
        }

        public class ViewHolder extends RecyclerView.ViewHolder{
            TextView tv_min,tv_max,tv_commission;
            ViewHolder(View view)
            {
                super(view);
                tv_min=view.findViewById(R.id.tv_min);
                tv_max=view.findViewById(R.id.tv_max);
                tv_commission=view.findViewById(R.id.tv_commission);
            }
        }
    }
}
