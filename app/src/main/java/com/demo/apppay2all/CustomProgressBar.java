package com.demo.apppay2all;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;

public class CustomProgressBar {

    public AlertDialog alert;
    public void mShowProgressBar(Context context,String message)
    {
        LayoutInflater inflater =(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v2=inflater.inflate(R.layout.custom_progress_bar,null);


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