package com.demo.apppay2all.ui.wallet;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.demo.apppay2all.FundRequesDetails.FundRequestList;
import com.demo.apppay2all.PayoutServices.Payout;
import com.demo.apppay2all.R;
import com.demo.apppay2all.SharePrefManager;

public class WalletFragment extends Fragment {


    TextView tv_main_balance,tv_aeps_balance;

    LinearLayout ll_add,ll_move;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
//        WalletViewModel slideshowViewModel =
//                new ViewModelProvider(this).get(WalletViewModel.class);

        View root = inflater.inflate(R.layout.fragment_wallet,container,false);
//
//        final TextView textView = binding.textSlideshow;
//        slideshowViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);

        tv_main_balance=root.findViewById(R.id.tv_main_balance);
        tv_main_balance.setText(getActivity().getResources().getString(R.string.rs)+ SharePrefManager.getInstance(getActivity()).mGetMainBalance());

        tv_aeps_balance=root.findViewById(R.id.tv_aeps_balance);
        tv_aeps_balance.setText(getActivity().getResources().getString(R.string.rs)+ SharePrefManager.getInstance(getActivity()).mGetAEPSBalance());


        ll_add=root.findViewById(R.id.ll_add);
        ll_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), FundRequestList.class));
            }
        });

        ll_move=root.findViewById(R.id.ll_move);
        ll_move.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), Payout.class));
            }
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

    }
}