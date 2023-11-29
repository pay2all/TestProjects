package com.demo.apppay2all.FundRequesDetails;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.demo.apppay2all.R;

import java.util.List;


public class BankDetailCardAdapter extends RecyclerView.Adapter<BankDetailCardAdapter.ViewHolder> {

    Context context;
    List<BankDetailItem> bankDetailItems;
    public BankDetailCardAdapter(Context context, List<BankDetailItem> bankDetailItems)
    {
        this.context=context;
        this.bankDetailItems=bankDetailItems;
    }

    @Override
    public int getItemCount() {
        return bankDetailItems==null ?0:bankDetailItems.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.bankdetailitem,parent,false);
        ViewHolder viewHolder=new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        BankDetailItem item=bankDetailItems.get(position);
        holder.textview_accountname.setText(item.getBank_account_name());
        holder.textview_accountno.setText(item.getBank_account_number());

        holder.textview_accountno_in_small_letter.setText("ACCOUNT NO : "+item.getBank_account_number());

        holder.textView_ifscode.setText(item.getBank_ifsc());
        holder.textview_bank_name.setText(item.getBank_name());
        holder.textview_branch.setText(item.getBank_branch());

        holder.rl_bank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (holder.ll_detail.getVisibility()== View.VISIBLE)
                {
                    holder.ll_detail.setVisibility(View.GONE);
                    holder.imageview_expande.setImageResource(R.drawable.arrow_down);
                }
                else
                {
                    holder.ll_detail.setVisibility(View.VISIBLE);
                    holder.imageview_expande.setImageResource(R.drawable.arrow_up);
                }
            }
        });

    }

    class ViewHolder extends RecyclerView.ViewHolder
    {
        TextView textview_bank_name,textview_accountname,textview_accountno_in_small_letter,textview_accountno,textView_ifscode,textview_branch;
        LinearLayout ll_detail;
        RelativeLayout rl_bank;
        ImageView imageview_expande;
        ViewHolder (View view)
        {
            super(view);
            textview_bank_name=view.findViewById(R.id.textview_bank_name);
            textview_accountname=view.findViewById(R.id.textview_accountname);
            textview_accountno_in_small_letter=view.findViewById(R.id.textview_accountno_in_small_letter);
            textview_accountno=view.findViewById(R.id.textview_accountno);
            textView_ifscode=view.findViewById(R.id.textView_ifscode);
            textview_branch=view.findViewById(R.id.textview_branch);
            ll_detail=view.findViewById(R.id.ll_detail);
            rl_bank=view.findViewById(R.id.rl_bank);
            imageview_expande=view.findViewById(R.id.imageview_expande);

            imageview_expande.setImageResource(R.drawable.arrow_down);
        }
    }
}
