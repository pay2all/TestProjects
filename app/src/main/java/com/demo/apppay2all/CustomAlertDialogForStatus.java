package com.demo.apppay2all;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class CustomAlertDialogForStatus {

    public AlertDialog alert;
    public void mShowDialog(Context context,String message, String status, String number, String amount, String provider)
    {
        LayoutInflater inflater =(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v2=inflater.inflate(R.layout.custome_dialog_success_recharge,null);
        ImageView imageview_checkicon=v2.findViewById(R.id.imageview_checkicon);
        TextView textview_requestsendsuccessfull=v2.findViewById(R.id.textview_requestsendsuccessfull);
        TextView textview_oper=v2.findViewById(R.id.textview_operator);
        TextView textview_mobnumber=v2.findViewById(R.id.textview_mobnumber);
        TextView textview_mobile_title=v2.findViewById(R.id.textview_mobile_title);
        textview_mobile_title.setText("Number");
        TextView textview_amount=v2.findViewById(R.id.textview_amount);
        Button button_done=v2.findViewById(R.id.button_done);

        textview_requestsendsuccessfull.setText(message);
        textview_oper.setText(provider);
        textview_mobnumber.setText(number);
        textview_amount.setText("RS "+amount);

        if (status.equalsIgnoreCase("success")){
            imageview_checkicon.setBackground(context.getResources().getDrawable(R.drawable.checkicon));

        }

        else if (status.equalsIgnoreCase("failure")){
            imageview_checkicon.setBackground(context.getResources().getDrawable(R.drawable.failure));

        }

        else if (status.equalsIgnoreCase("pending")){
            imageview_checkicon.setBackground(context.getResources().getDrawable(R.drawable.pending));

        }

        else {
            imageview_checkicon.setBackground(context.getResources().getDrawable(R.drawable.pending));

        }

        button_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alert.dismiss();
            }
        });

        final AlertDialog.Builder builder2=new AlertDialog.Builder(context);
        builder2.setCancelable(false);

        builder2.setView(v2);
         alert=builder2.create();
         if (alert.getWindow()!=null) {
             alert.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
         }

        alert.show();
    }
    public boolean AlertDialogShowing()
    {
        if (alert.isShowing())
        {
            return true;
        }
        else
        {
            return  false;
        }
    }


}
