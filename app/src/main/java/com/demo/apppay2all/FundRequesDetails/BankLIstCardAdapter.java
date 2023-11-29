package com.demo.apppay2all.FundRequesDetails;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.demo.apppay2all.R;

import java.util.List;

public class BankLIstCardAdapter extends RecyclerView.Adapter<BankLIstCardAdapter.ViewHolder>{


    Context context;
    List<BankListItems> bankListItems;
    public BankLIstCardAdapter(Context context,List<BankListItems> bankListItems)
    {
        this.context=context;
        this.bankListItems=bankListItems;
    }
    @Override
    public int getItemCount() {
        return bankListItems==null?0:bankListItems.size();
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        BankListItems items=bankListItems.get(position);
        holder.tv_account.setText("A/c "+items.getAccount_number());
        holder.tv_bank.setText(items.getBank_name());
        holder.tv_ifsc.setText("IFSC  "+items.getIfsc_code());
        holder.tv_branch.setText("Branch "+items.getBranch());
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(context).inflate(R.layout.bank_list_item,parent,false);
        return new ViewHolder(view);
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        TextView tv_bank,tv_account,tv_ifsc,tv_branch;
        ViewHolder (View view)
        {
            super(view);

            tv_bank=view.findViewById(R.id.tv_bank);
            tv_account=view.findViewById(R.id.tv_account);
            tv_ifsc=view.findViewById(R.id.tv_ifsc);
            tv_branch=view.findViewById(R.id.tv_branch);
        }
    }
}
