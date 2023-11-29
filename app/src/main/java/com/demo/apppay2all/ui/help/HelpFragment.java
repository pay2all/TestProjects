package com.demo.apppay2all.ui.help;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.demo.apppay2all.R;
import com.demo.apppay2all.SharePrefManager;


public class HelpFragment extends Fragment {



    TextView tv_company_name,tv_support_mobile,tv_whatsapp_mobile,tv_email,tv_web,tv_address1,tv_address2,tv_version;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HelpViewModel slideshowViewModel =
                new ViewModelProvider(this).get(HelpViewModel.class);


        View root = inflater.inflate(R.layout.fragment_help,container,false);

        tv_company_name=root.findViewById(R.id.tv_company_name);
        tv_company_name.setText(SharePrefManager.getInstance(getActivity()).mGetCompanyName());

        tv_support_mobile=root.findViewById(R.id.tv_support_mobile);
        tv_support_mobile.setText(SharePrefManager.getInstance(getActivity()).mGetSupportNumber());
        tv_support_mobile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkphonecall()){
                    Intent callIntent = new Intent(Intent.ACTION_DIAL);
                    callIntent.setData(Uri.parse("tel:"+SharePrefManager.getInstance(getActivity()).mGetSupportNumber()));
                    startActivity(callIntent);
                }
                else {
                    requestcheckphonecall();
                }
            }
        });

        tv_whatsapp_mobile=root.findViewById(R.id.tv_whatsapp_mobile);
        tv_whatsapp_mobile.setText(SharePrefManager.getInstance(getActivity()).mGetChatNumber());
        tv_whatsapp_mobile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = "Hi";

                startActivity(
                        new Intent(Intent.ACTION_VIEW,
                                Uri.parse("https://api.whatsapp.com/send?phone=+91"+ SharePrefManager.getInstance(getActivity()).mGetChatNumber()+"&text="+ message)));
            }
        });

        tv_email=root.findViewById(R.id.tv_email);
        tv_email.setText(SharePrefManager.getInstance(getActivity()).mGetCompanyEmail());
        tv_email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(Intent.createChooser( new Intent("android.intent.action.SENDTO",Uri.parse("mailto:"+SharePrefManager.getInstance(getActivity()).mGetCompanyEmail())),"sendvia"));

            }
        });

        tv_web=root.findViewById(R.id.tv_web);
        tv_web.setText(SharePrefManager.getInstance(getActivity()).mGetWebSite());
        tv_web.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(Intent.ACTION_VIEW, Uri.parse(SharePrefManager.getInstance(getActivity()).mGetWebSite()));
                startActivity(Intent.createChooser(i,"Open With"));
            }
        });

        tv_address1=root.findViewById(R.id.tv_address1);
        tv_address1.setText("Address 1 : "+SharePrefManager.getInstance(getActivity()).mGetCompanyAddress1());

        tv_address2=root.findViewById(R.id.tv_address2);
        tv_address2.setText("Address 2 : "+SharePrefManager.getInstance(getActivity()).mGetCompanyAddress2());
//        final TextView textView = binding.textHelp;
//        slideshowViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

    }

    public boolean checkphonecall() {
        int result = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CALL_PHONE);
        if (result== PackageManager.PERMISSION_GRANTED)
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    private void requestcheckphonecall()
    {
        if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.CALL_PHONE))
        {
        }
        else {
            ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.CALL_PHONE},1);
        }
    }
}