package com.demo.apppay2all.ListData;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.demo.apppay2all.AddMemberDetails.AddMember;
import com.demo.apppay2all.FormDetails.AddressFragment;
import com.demo.apppay2all.R;
import com.demo.apppay2all.RechargesServicesDetail.PrepaidMobile;

import java.util.List;

public class ListDataCardAdapter extends RecyclerView.Adapter<ListDataCardAdapter.ViewHolder> {

    Context context;
    List<ListDataItems> listDataItems;
    public ListDataCardAdapter(Context context,List<ListDataItems> listDataItems)
    {
        this.context=context;
        this.listDataItems=listDataItems;
    }

    @Override
    public int getItemCount() {
        return listDataItems==null?0:listDataItems.size();
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        ListDataItems items=listDataItems.get(position);
        holder.textview_name.setText(items.getName());
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.list_data_item_layout,parent,false);
        return new ViewHolder(view);
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        TextView textview_name;
        ViewHolder(View view)
        {
            super(view);
            textview_name=view.findViewById(R.id.textview_name);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listDataItems.get(getAdapterPosition()).getType().equalsIgnoreCase("mobile"))
                    {
                        ((PrepaidMobile)context).mGetCircleDetail(listDataItems.get(getAdapterPosition()));
                    }

                    else if (ListDataBottomSheet3DialogFragment.act.equalsIgnoreCase("add_member"))
                    {
                        ((AddMember)context).mGetData(listDataItems.get(getAdapterPosition()));
                    }
                    else {
                        AddressFragment.mGetData(listDataItems.get(getAdapterPosition()));
                    }
                    ListDataBottomSheet3DialogFragment.dialogFragment.dismiss();
                }
            });
        }
    }

    public void UpdateList(List<ListDataItems> item)
    {
        listDataItems=item;
        notifyDataSetChanged();
    }
}
