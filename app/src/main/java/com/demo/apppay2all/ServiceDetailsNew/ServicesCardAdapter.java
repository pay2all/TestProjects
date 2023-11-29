package com.demo.apppay2all.ServiceDetailsNew;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.demo.apppay2all.R;

import com.demo.apppay2all.ServiceDetailSub.SubServiceAdapter;
import com.demo.apppay2all.ServiceDetailSub.SubServiceItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ServicesCardAdapter extends RecyclerView.Adapter<ServicesCardAdapter.ViewHolder> {

    Context context;
    List<ServicesItems> rechargeItems;
    public ServicesCardAdapter(Context context, List<ServicesItems> rechargeItems)
    {
        this.context=context;
        this.rechargeItems=rechargeItems;
    }
    @Override
    public int getItemCount() {
        return rechargeItems==null?0:rechargeItems.size();
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        ServicesItems items=rechargeItems.get(position);
        if (items.getData().length()>0)
        {
            holder.tv_title.setText(items.getName());
            mShowSubData(holder.rv_sub_services,items.getData(),items);
            holder.ll_root.setVisibility(View.VISIBLE);

            try {
                if (position > 0 && position < rechargeItems.size() - 1) {
                    holder.vw_line.setVisibility(View.VISIBLE);
                } else {
                    holder.vw_line.setVisibility(View.GONE);
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }

        }
        else {
            holder.ll_root.setVisibility(View.GONE);
        }

        holder.setIsRecyclable(false);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.service_detail_item_layout,parent,false);

        return new ViewHolder(view);
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        TextView tv_title;
        RecyclerView rv_sub_services;
        LinearLayout ll_root;

        View vw_line;

        ViewHolder (View view)
        {
            super(view);
            vw_line=view.findViewById(R.id.vw_line);
            tv_title=view.findViewById(R.id.tv_title);
            ll_root=view.findViewById(R.id.ll_root);
            rv_sub_services=view.findViewById(R.id.rv_sub_services);
            rv_sub_services.setLayoutManager(new GridLayoutManager(context,3));

        }
    }

    protected void mShowSubData(RecyclerView recyclerView,String json,ServicesItems servicesItems)
    {

        SubServiceAdapter subServiceAdapter;
        List<SubServiceItem> subServiceItems;

        subServiceItems=new ArrayList<>();
        subServiceAdapter=new SubServiceAdapter(context,subServiceItems);
        recyclerView.setAdapter(subServiceAdapter);

        try{

            JSONArray jsonArray=new JSONArray(json);
            for (int i=0; i<jsonArray.length(); i++)
            {
                JSONObject data=jsonArray.getJSONObject(i);
                SubServiceItem item=new SubServiceItem();
                item.setService_id(data.getString("service_id"));
                item.setService_name(data.getString("service_name"));
                item.setService_image(data.getString("service_image"));
                item.setBbps(data.getString("bbps"));
                item.setReport_title(data.getString("report_title"));
                item.setReport_url(data.getString("report_url"));
                item.setReport_is_static(data.getString("report_is_static"));
                item.setServicesItems(servicesItems);
                subServiceItems.add(item);
                subServiceAdapter.notifyDataSetChanged();
            }
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }

    }

}