package com.demo.apppay2all.AccountValidationReportDetails;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.demo.apppay2all.BaseURL.BaseURL;
import com.demo.apppay2all.CallResAPIPOSTMethod;
import com.demo.apppay2all.DetectConnection;
import com.demo.apppay2all.R;
import com.demo.apppay2all.Review_Activity;
import com.demo.apppay2all.SharePrefManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Random;

public class AccountValidationCardAdapter extends RecyclerView.Adapter<AccountValidationCardAdapter.ViewHolder>{

    Context context;

    List<AccountValidationItem> accountValidationItems;

    ProgressDialog dialog;

    String reasonid="";

    String reasonmessage="";

    AlertDialog alertDialog;

    TextView tv_report_id_error;

    Uri file=null;

    public AccountValidationCardAdapter(Context context,List<AccountValidationItem> accountValidationItems)
    {
        this.context=context;
        this.accountValidationItems=accountValidationItems;
    }
    @Override
    public int getItemCount() {
        return accountValidationItems==null?0:accountValidationItems.size();
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        AccountValidationItem item=accountValidationItems.get(position);
        holder.tv_txnid.setText("Txn id "+item.getTxnid());
        holder.tv_date.setText("Date "+item.getCreated_at());
        holder.tv_amount.setText("Rs "+item.getAmount());
        holder.tv_number.setText("A/c "+item.getNumber());
        holder.tv_status.setText(item.getStatus());

        if (item.getStatus().equalsIgnoreCase("success"))
        {
            holder.imageview_check.setBackground(context.getResources().getDrawable(R.drawable.checkicon));
        }
        else if (item.getStatus().equalsIgnoreCase("refund"))
        {
            holder.imageview_check.setBackground(context.getResources().getDrawable(R.drawable.refunded));
        }
        else
        {
            holder.imageview_check.setBackground(context.getResources().getDrawable(R.drawable.failure));
        }


        holder.tv_view.setVisibility(View.VISIBLE);
        holder.tv_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mShowrecipt(position);
            }
        });

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.account_validation_item,parent,false);
        return new ViewHolder(view);
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        TextView tv_txnid,tv_date,tv_number,tv_amount,tv_status,tv_view;
        ImageView imageview_check;

        ViewHolder(View view)
        {
            super(view);

            tv_txnid=view.findViewById(R.id.tv_txnid);
            tv_date=view.findViewById(R.id.tv_date);
            tv_number=view.findViewById(R.id.tv_number);
            tv_amount=view.findViewById(R.id.tv_amount);
            imageview_check=view.findViewById(R.id.imageview_check);
            tv_status=view.findViewById(R.id.tv_status);
            tv_view=view.findViewById(R.id.tv_view);
        }
    }

    private void mShowrecipt (final int position) {


        final AlertDialog alertDialog;
        LayoutInflater inflater2 =(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v2=inflater2.inflate(R.layout.custome_dialog_recharge_details,null);

        AccountValidationItem item=accountValidationItems.get(position);
        TextView textview_transctionid=v2.findViewById(R.id.textview_transctionid);
        TextView textview_rechargemount=v2.findViewById(R.id.textview_rechargemount);
        TextView textview_newbalance=v2.findViewById(R.id.textview_credit);
        TextView textview_comission=v2.findViewById(R.id.textview_comission);
        TextView textview_dateandtime=v2.findViewById(R.id.textview_dateandtime);
        TextView textview_name=v2.findViewById(R.id.textview_description);
        TextView textview_customernumber=v2.findViewById(R.id.textview_customernumber);
        ImageView imageview_provider_image=v2.findViewById(R.id.imageview_provider_image);
        TextView textview_success=v2.findViewById(R.id.textview_success);
        TextView textview_opening_balance=v2.findViewById(R.id.textview_debit);


        textview_opening_balance.setVisibility(View.GONE);
        textview_success.setText(item.getStatus());
        textview_name.setVisibility(View.GONE);
        textview_transctionid.setText(item.getTxnid());
        textview_rechargemount.setText(item.getAmount());
        textview_newbalance.setVisibility(View.GONE);
        textview_comission.setText(item.getProfit());
        textview_dateandtime.setText(item.getCreated_at());
        textview_customernumber.setText(item.getNumber());

        Button button_share=v2.findViewById(R.id.button_share);

        CardView cardview_receipt=v2.findViewById(R.id.cardview_receipt);
        button_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mCheckWriteStorage()) {
                    try {
                        mSaveImage(loadBitmapFromView(cardview_receipt));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Intent intent = new Intent("android.intent.action.SEND");
                    intent.setType("image/*");
                    intent.putExtra("android.intent.extra.STREAM", file);
                    context.startActivity(Intent.createChooser(intent, "Share Receipt"));


                    return;
                }
                mRequestWriteStorage();
            }
        });


        Button button_complaint=v2.findViewById(R.id.button_complaint);

//        if (item.get().equals("")){
//
//        }
//        else {
//            Picasso.with(context).load(item.getProviderimage()).into(imageview_provider_image);
//        }

        final AlertDialog.Builder builder2=new AlertDialog.Builder(context);
        builder2.setCancelable(false);
        builder2.setView(v2);
        alertDialog=builder2.create();

        ImageView imageview_close=v2.findViewById(R.id.imageview_close);
        alertDialog.setCancelable(true);

        if (alertDialog.getWindow()!=null) {
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
        button_complaint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                alertDialog.dismiss();
//                mGetReason(position);
                mGetDisputeReason(position);
            }
        });

        alertDialog.show();

    }
    private void mShowBookComplaint (final int position,String jsoandata) {



        LayoutInflater inflater2 =(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v2=inflater2.inflate(R.layout.custome_dialog_book_complaint,null);

        final AccountValidationItem item=accountValidationItems.get(position);
        RelativeLayout rl_choose_reason=v2.findViewById(R.id.rl_choose_reason);
        final TextView textview_choose_reason=v2.findViewById(R.id.textview_choose_reason);

        EditText ed_report_id=v2.findViewById(R.id.ed_report_id);
        ed_report_id.setText(item.getId());
         tv_report_id_error=v2.findViewById(R.id.tv_report_id_error);

        EditText ed_provider=v2.findViewById(R.id.ed_provider);
        ed_provider.setText(item.getProvider());

        EditText ed_number=v2.findViewById(R.id.ed_number);
        ed_number.setText(item.getNumber());

        final EditText ed_message=v2.findViewById(R.id.ed_message);

        Button button_submit=v2.findViewById(R.id.button_submit);
        Button button_cancel=v2.findViewById(R.id.button_cancel);



        final AlertDialog.Builder builder2=new AlertDialog.Builder(context);
        builder2.setCancelable(false);
        builder2.setView(v2);
        alertDialog=builder2.create();

        alertDialog.setCancelable(true);

        if (alertDialog.getWindow()!=null) {
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
        button_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });

        String status="";

        final PopupMenu popupMenu=new PopupMenu(context,rl_choose_reason);
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                reasonid=menuItem.getGroupId()+"";
                reasonmessage=menuItem.getTitle().toString();
                textview_choose_reason.setText(menuItem.getTitle().toString());
                return true;
            }
        });
        rl_choose_reason.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (popupMenu!=null){
                    popupMenu.show();
                }
            }
        });
        try{
            JSONObject jsonObject=new JSONObject(jsoandata);
            if (jsonObject.has("status")){

                status=jsonObject.getString("status");
            }

            if (status.equalsIgnoreCase("success")){

                if (jsonObject.has("reason")){

                    JSONArray jsonArray=jsonObject.getJSONArray("reason");
                    for (int i=0;i<jsonArray.length();i++){
                        JSONObject data=jsonArray.getJSONObject(i);
                        popupMenu.getMenu().add(data.getInt("reason_id"),0,0,data.getString("reason"));
                    }
                }
            }
        }

        catch (JSONException e){
            e.printStackTrace();
        }
        button_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (DetectConnection.checkInternetConnection(context)){

                    if (reasonid.equals("")){
                        Toast.makeText(context, "Please Select Reason", Toast.LENGTH_SHORT).show();
                    }

                    else {

                        String message=ed_message.getText().toString();
                        mSaveDisput(message,item.getId(),reasonid);
                    }
                }

                else {

                    Toast.makeText(context, "No Internet Connection", Toast.LENGTH_SHORT).show();
                }
            }
        });
        alertDialog.show();
    }

    @SuppressLint("StaticFieldLeak")
    protected void mGetDisputeReason(final int position)
    {
        String sendingUrl= BaseURL.BASEURL_B2C+"api/dispute/reason";
        Uri.Builder builder=new Uri.Builder();
        builder.appendQueryParameter("api_token", SharePrefManager.getInstance(context).mGetApiToken());

        new CallResAPIPOSTMethod((Activity) context,builder,sendingUrl,true,"POST")
        {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                dialog=new ProgressDialog(context);
                dialog.setMessage("Please wait...");
                dialog.setCancelable(false);
                dialog.show();
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                dialog.dismiss();
                Log.e("reason","disput "+s);

                if (!s.equals("")){
                    mShowBookComplaint(position,s);
                }
                else {
                    Toast.makeText(context, "Unable To Get Data", Toast.LENGTH_SHORT).show();
                }
            }
        }.execute();
    }

    @SuppressLint("StaticFieldLeak")
    protected void mSaveDisput(String message, String resport_id, String reasonid)
    {
        String sending_url=BaseURL.BASEURL_B2C+"api/dispute/save-dispute";
        Uri.Builder builder=new Uri.Builder();
        builder.appendQueryParameter("api_token",SharePrefManager.getInstance(context).mGetApiToken());
        builder.appendQueryParameter("report_id",resport_id);
        builder.appendQueryParameter("reason",reasonid);
        builder.appendQueryParameter("message",message);

        new CallResAPIPOSTMethod((Activity) context,builder,sending_url,true,"POST")
        {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                dialog=new ProgressDialog(context);
                dialog.setMessage("Please wait...");
                dialog.show();
                dialog.setCancelable(false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                dialog.dismiss();
                Log.e("save","disput "+s);

                if (!s.equals("")){
                    String status="",message="";
                    try {

                        JSONObject jsonObject=new JSONObject(s);
                        if (jsonObject.has("status")){
                            status=jsonObject.getString("status");
                        }
                        if (jsonObject.has("message")){
                            message=jsonObject.getString("message");
                        }

                        if (!status.equals("")){

                            if (status.equalsIgnoreCase("success"))
                            {

                                alertDialog.dismiss();
                                Intent intent=new Intent(context, Review_Activity.class);
                                intent.putExtra("status",status);
                                intent.putExtra("message",message);
                                intent.putExtra("activity","complaint");
                                context.startActivity(intent);
                            }
                            else if (status.equalsIgnoreCase("validation_error"))
                            {
                                JSONObject errors=jsonObject.getJSONObject("errors");
                                if (errors.has("report_id"))
                                {
                                    tv_report_id_error.setVisibility(View.VISIBLE);
                                    tv_report_id_error.setText(errors.getString("report_id").replaceAll("\\[","").replaceAll("]","").replaceAll("\"",""));
                                }
                            }
                            else if (!message.equals(""))
                            {
                                Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                            }
                            else
                            {
                                Toast.makeText(context, "Something went wrong, please try again later", Toast.LENGTH_SHORT).show();
                            }


                        }

                        else {
                            Toast.makeText(context, "Something Went Wrong", Toast.LENGTH_SHORT).show();
                        }

                    }

                    catch (JSONException e){
                        e.printStackTrace();
                    }
                }
                else {
                    Toast.makeText(context, "Server Not Responding", Toast.LENGTH_SHORT).show();
                }
            }
        }.execute();
    }

    public void UpdateList(List<AccountValidationItem> item)
    {
        accountValidationItems =item;
        notifyDataSetChanged();
    }


    public static Bitmap loadBitmapFromView(View view) {
        view.setDrawingCacheEnabled(true);
        Bitmap createBitmap = Bitmap.createBitmap(view.getDrawingCache());
        view.setDrawingCacheEnabled(false);
        return createBitmap;
    }


    private void mSaveImage(Bitmap bitmap) throws IOException {
        boolean saved;
        OutputStream fos;

        int nextInt = new Random().nextInt(10000);
        StringBuilder sb2 = new StringBuilder();
        sb2.append("Image-");
        sb2.append(nextInt);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            ContentResolver resolver = context.getContentResolver();
            ContentValues contentValues = new ContentValues();
            contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, sb2.toString());
            contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "image/png");
            contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH, "DCIM/" +  context.getResources().getString(R.string.app_name).replaceAll(" ","_"));
            Uri imageUri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
            fos = resolver.openOutputStream(imageUri);

            Log.e("uri","name "+imageUri.toString()+".png");

            file=imageUri;
//            file=new File(imageUri.toString()+".png");

        }

        else {
            String imagesDir = Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_DCIM).toString() + File.separator + context.getResources().getString(R.string.app_name).replaceAll(" ","_");

            file=Uri.fromFile(new File(imagesDir));
            File file = new File(imagesDir);

            if (!file.exists()) {
                file.mkdir();
            }

            file = new File(imagesDir, sb2.toString() + ".png");
            fos = new FileOutputStream(file);
        }

        saved = bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
        fos.flush();
        fos.close();

//        if (saved) {
//            Toast.makeText(context, "Successfully Downloaded", Toast.LENGTH_SHORT).show();
//        }
    }

    public boolean mCheckWriteStorage() {
        return ContextCompat.checkSelfPermission(context.getApplicationContext(), "android.permission.WRITE_EXTERNAL_STORAGE") == 0;
    }

    public void mRequestWriteStorage() {
        if (!ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, "android.permission.WRITE_EXTERNAL_STORAGE")) {
            ActivityCompat.requestPermissions((Activity) context, new String[]{"android.permission.WRITE_EXTERNAL_STORAGE"}, 1);
        }
    }

}
