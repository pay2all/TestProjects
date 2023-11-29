package com.demo.apppay2all.ui.reports.SubReportsDetail;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.demo.apppay2all.AepsReportDetails.AepsReports;
import com.demo.apppay2all.MoneyTransferDetails.MoneyDetails;
import com.demo.apppay2all.R;
import com.demo.apppay2all.RechargeReportMVVM.RechargeReport;
import com.demo.apppay2all.RechargeReports.Reports;

import java.util.List;

public class SubReportAdapter extends RecyclerView.Adapter<SubReportAdapter.ViewHolder>{

    Context context;
    List<SubReportItems> subReportItems;

    public SubReportAdapter(Context context,List<SubReportItems> subReportItems)
    {
        this.context=context;
        this.subReportItems=subReportItems;
    }

    @Override
    public int getItemCount() {
        return subReportItems==null?0:subReportItems.size();
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        SubReportItems item=subReportItems.get(position);
        holder.tv_title.setText(item.getName());

        try{
            if (position<subReportItems.size()-1)
            {
                holder.vw_line.setVisibility(View.VISIBLE);
            }
            else {
                holder.vw_line.setVisibility(View.GONE);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @NonNull
    @Override
    public SubReportAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.all_sub_report_item_layout,parent,false);
        return new ViewHolder(view);
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        TextView tv_title;

        View vw_line;
        ViewHolder(View view)
        {
            super(view);
            tv_title=view.findViewById(R.id.tv_title);
            vw_line=view.findViewById(R.id.vw_line);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    SubReportItems items=subReportItems.get(getAdapterPosition());
                    if (items.getIs_static().equals("0"))
                    {
                        Intent intent=new Intent(context, Reports.class);
                        intent.putExtra("url",items.getUrl());
                        intent.putExtra("title",items.getName());
                        context.startActivity(intent);
                    }
                    else {
                        if (items.getId().equals("17")||items.getId().equals("18"))
                        {
                            Intent intent=new Intent(context, MoneyDetails.class);
                            intent.putExtra("url",items.getUrl());
                            intent.putExtra("title",items.getName());
                            context.startActivity(intent);
                        }
                        else if (items.getId().equals("19"))
                        {
                            Intent intent=new Intent(context, AepsReports.class);
                            intent.putExtra("url",items.getUrl());
                            intent.putExtra("title",items.getName());
                            intent.putExtra("type","aeps");
                            context.startActivity(intent);
                        }
                    }
                }
            });
        }
    }
}