package com.demo.apppay2all.ui.reports;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.demo.apppay2all.AccountValidationReportDetails.AccountValidationReport;
import com.demo.apppay2all.AllTransactionDetail.AllTransaction;
import com.demo.apppay2all.IncomeDetails.IncomeReport;
import com.demo.apppay2all.LedgerDetail.Ledger;
import com.demo.apppay2all.OperatorReportDetail.OperatorReport;
import com.demo.apppay2all.R;
import com.demo.apppay2all.ServiceDetailSub.SubServiceAdapter;
import com.demo.apppay2all.ui.reports.SubReportsDetail.SubReportAdapter;
import com.demo.apppay2all.ui.reports.SubReportsDetail.SubReportItems;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class AllReportAdapter extends RecyclerView.Adapter<AllReportAdapter.ViewHolder>{

    Context context;
    List<AllReportItem> allReportItems;

    public AllReportAdapter(Context context,List<AllReportItem> allReportItems)
    {
        this.context=context;
        this.allReportItems=allReportItems;
    }

    @Override
    public int getItemCount() {
        return allReportItems==null?0:allReportItems.size();
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        AllReportItem item=allReportItems.get(position);
        if (item.getService_image().equals(""))
        {
            if (item.getImage()!=0)
            {
                holder.iv_icon.setImageResource(item.getImage());
            }
        }
        else {
            Glide.with(context).load(item.getService_image()).into(holder.iv_icon);
        }

        holder.tv_title.setText(item.getName());
        holder.tv_description.setText("See Your "+item.getName());

        if (!item.getSub_report().equals("")) {
            mShowSubReport(item, holder.rv_sub_report);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.all_report_item_layout,parent,false);
        return new ViewHolder(view);
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        ImageView iv_icon,iv_arrow;
        TextView tv_title;
        TextView tv_description;

        RecyclerView rv_sub_report;
        ViewHolder(View view)
        {
            super(view);
            iv_icon=view.findViewById(R.id.iv_icon);
            iv_arrow=view.findViewById(R.id.iv_arrow);
            tv_title=view.findViewById(R.id.tv_title);
            tv_description=view.findViewById(R.id.tv_description);
            rv_sub_report=view.findViewById(R.id.rv_sub_report);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AllReportItem item=allReportItems.get(getAdapterPosition());
                    if (!item.getSub_report().equals(""))
                    {
                        if (rv_sub_report.getVisibility()==View.VISIBLE)
                        {
                            rv_sub_report.setVisibility(View.GONE);
                            iv_arrow.setImageResource(R.drawable.arrow_right);

                        }
                        else {
                            rv_sub_report.setVisibility(View.VISIBLE);
                            iv_arrow.setImageResource(R.drawable.arrow_down);
                        }
                    }
                    else {
                        if (!item.getId().equals(""))
                        {
                            if (item.getId().equalsIgnoreCase("All Transaction Report"))
                            {
                                context.startActivity(new Intent(context, AllTransaction.class));
                            }
                            else if (item.getId().equalsIgnoreCase("Ledger Report"))
                            {
                                context.startActivity(new Intent(context, Ledger.class));
                            }
                            else if (item.getId().equalsIgnoreCase("Account Validation Report"))
                            {
                                context.startActivity(new Intent(context, AccountValidationReport.class));
                            }
                            else if (item.getId().equalsIgnoreCase("Operator Report"))
                            {
                                context.startActivity(new Intent(context, OperatorReport.class));
                            }
                            else if (item.getId().equalsIgnoreCase("Income Report"))
                            {
                                context.startActivity(new Intent(context, IncomeReport.class));
                            }
                        }
                    }
                }
            });
        }
    }

    protected void mShowSubReport(AllReportItem item,RecyclerView recyclerView)
    {
        recyclerView.setLayoutManager(new LinearLayoutManager(context));

        List<SubReportItems> list=new ArrayList<>();
        SubReportAdapter subReportAdapter=new SubReportAdapter(context,list);
        recyclerView.setAdapter(subReportAdapter);

        try
        {
            JSONArray jsonArray=new JSONArray(item.getSub_report());
            for (int i=0; i<jsonArray.length(); i++)
            {
                JSONObject data=jsonArray.getJSONObject(i);
                SubReportItems items=new SubReportItems();
                items.setId(data.getString("service_id"));
                items.setIs_static(data.getString("report_is_static"));
                items.setName(data.getString("report_title"));
                items.setUrl(data.getString("report_url"));
                list.add(items);
                subReportAdapter.notifyDataSetChanged();
            }
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
    }
}