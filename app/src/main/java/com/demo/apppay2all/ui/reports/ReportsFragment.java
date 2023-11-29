package com.demo.apppay2all.ui.reports;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.demo.apppay2all.AccountValidationReportDetails.AccountValidationReport;
import com.demo.apppay2all.AepsReportDetails.AepsReports;
import com.demo.apppay2all.AllTransactionDetail.AllTransaction;
import com.demo.apppay2all.IncomeDetails.IncomeReport;
import com.demo.apppay2all.LedgerDetail.Ledger;
import com.demo.apppay2all.MoneyTransferDetails.MoneyDetails;
import com.demo.apppay2all.OperatorReportDetail.OperatorReport;
import com.demo.apppay2all.R;
import com.demo.apppay2all.RechargeReports.Reports;
import com.demo.apppay2all.SharePrefManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ReportsFragment extends Fragment {

    RelativeLayout rl_all_txn,rl_ledger_report,rl_recharge_report,rl_account_verification_report
            ,rl_money_transfer_report,rl_operator_report,rl_income_report,rl_aeps_report,
            rl_aeps_ledeger;

    RecyclerView rv_reports;
    List<AllReportItem> allReportItems;
    AllReportAdapter allReportAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        ReportsViewModel galleryViewModel =
                new ViewModelProvider(this).get(ReportsViewModel.class);

        View root = inflater.inflate(R.layout.fragment_reports,container,false);

        rv_reports=root.findViewById(R.id.rv_reports);
        allReportItems=new ArrayList<>();
        allReportAdapter=new AllReportAdapter(getActivity(),allReportItems);
        rv_reports.setLayoutManager(new LinearLayoutManager(getActivity()));
        rv_reports.setAdapter(allReportAdapter);

        rl_all_txn=root.findViewById(R.id.rl_all_txn);
        rl_all_txn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), AllTransaction.class));
            }
        });
        rl_ledger_report=root.findViewById(R.id.rl_ledger_report);
        rl_ledger_report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), Ledger.class));
            }
        });
        rl_recharge_report=root.findViewById(R.id.rl_recharge_report);
        rl_recharge_report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), Reports.class));
            }
        });
        rl_account_verification_report=root.findViewById(R.id.rl_account_verification_report);
        rl_account_verification_report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), AccountValidationReport.class));
            }
        });
        rl_money_transfer_report=root.findViewById(R.id.rl_money_transfer_report);
        rl_money_transfer_report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), MoneyDetails.class));
            }
        });
        rl_operator_report=root.findViewById(R.id.rl_operator_report);
        rl_operator_report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), OperatorReport.class));
            }
        });
        rl_income_report=root.findViewById(R.id.rl_income_report);
        rl_income_report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), IncomeReport.class));
            }
        });
        rl_aeps_report=root.findViewById(R.id.rl_aeps_report);
        rl_aeps_report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getActivity(), AepsReports.class);
                intent.putExtra("type","aeps");
                startActivity(intent);
            }
        });
        rl_aeps_ledeger=root.findViewById(R.id.rl_aeps_ledeger);
        rl_aeps_ledeger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getActivity(), AepsReports.class);
                intent.putExtra("type","ledger");
                startActivity(intent);
            }
        });

//        final TextView textView = binding.textGallery;
//        galleryViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);

        mShowReport();
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    protected void mShowReport()
    {
        String[] all_names={"All Transaction Report","Ledger Report","Account Validation Report",
                "Operator Report","Income Report"};
        int[] all_icons={R.drawable.mini_statemen_icon,R.drawable.mini_statemen_icon,R.drawable.mini_statemen_icon,
                R.drawable.bank_icon,R.drawable.mini_statemen_icon};

        for (int i=0; i<all_names.length; i++)
        {
            AllReportItem item=new AllReportItem();
            item.setId(all_names[i]);
            item.setImage(all_icons[i]);
            item.setName(all_names[i]);
            item.setService_image("");

            allReportItems.add(item);
            allReportAdapter.notifyDataSetChanged();
        }

        mShowOtherServiceReport();
    }

    protected void mShowOtherServiceReport()
    {

        try {
            String services= SharePrefManager.getInstance(getActivity()).mGetServices();
            JSONArray jsonArray=new JSONArray(services);
            for (int i=0; i<jsonArray.length(); i++)
            {
                JSONObject data=jsonArray.getJSONObject(i);
                AllReportItem item=new AllReportItem();
                item.setId(data.getString("id"));
                item.setName(data.getString("title"));
                item.setSub_report(data.getJSONArray("data")+"");
                item.setImage(0);
                item.setService_image("");


                allReportItems.add(item);
                allReportAdapter.notifyDataSetChanged();
            }
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
    }
}