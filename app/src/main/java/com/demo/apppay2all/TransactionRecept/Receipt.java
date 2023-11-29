package com.demo.apppay2all.TransactionRecept;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.demo.apppay2all.MainActivitySingle;
import com.demo.apppay2all.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Receipt extends AppCompatActivity {

    String myJSON;

    Button button_receipt_exit;
    String name,amount;

    RecyclerView recyclerview_receipt;
    RecyclerView.LayoutManager layoutManager;
    List<ReceiptItem> receiptItems;
    ReceiptCardAdapter receiptCardAdapter=null;

    String activity;
    ImageView imageview_status;

    TextView textview_shop_name,textview_mobile,textview_beneficiary_name,textview_bank_name,
            textview_accountno,textView_ifsc,textview_mode,textview_total_amount,textview_date;
    TextView tv_print_transaction;

    CardView cardview_receipt;

    File file;

    Uri fileuri=null;

    Button bt_home;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receipt);
        StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().build());
        cardview_receipt=findViewById(R.id.cardview_receipt);

        bt_home=findViewById(R.id.bt_home);
        bt_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Receipt.this, MainActivitySingle.class);

                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
//                finish();
            }
        });

        button_receipt_exit=findViewById(R.id.button_receipt_exit);
        button_receipt_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        tv_print_transaction=findViewById(R.id.tv_print_transaction);
        tv_print_transaction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCheckWriteStorage()) {

                    try {
                        mSaveImage(loadBitmapFromView(cardview_receipt));
                    }
                    catch (IOException e) {
                        e.printStackTrace();
                    }

                    Intent intent = new Intent("android.intent.action.SEND");
                    intent.setType("image/*");
                    intent.putExtra("android.intent.extra.STREAM", fileuri);
                    startActivity(Intent.createChooser(intent, "Share Receipt"));

                }
                else {
                    mRequestWriteStorage();
                }
            }
        });

        textview_shop_name=findViewById(R.id.textview_shop_name);
        textview_mobile=findViewById(R.id.textview_mobile);
        textview_beneficiary_name=findViewById(R.id.textview_beneficiary_name);
        textview_bank_name=findViewById(R.id.textview_bank_name);
        textview_accountno=findViewById(R.id.textview_accountno);
        textView_ifsc=findViewById(R.id.textView_ifsc);
        textview_mode=findViewById(R.id.textview_mode);
        textview_total_amount=findViewById(R.id.textview_total_amount);
        textview_date=findViewById(R.id.textview_date);
        imageview_status=findViewById(R.id.imageview_status);

        recyclerview_receipt=findViewById(R.id.recyclerview_receipt);
        recyclerview_receipt.setHasFixedSize(true);
        recyclerview_receipt.setItemViewCacheSize(20);
        recyclerview_receipt.setDrawingCacheEnabled(true);
        recyclerview_receipt.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        layoutManager =new LinearLayoutManager(getApplicationContext());
        recyclerview_receipt.setLayoutManager(layoutManager);
        receiptItems =new ArrayList<>();
        receiptCardAdapter=new ReceiptCardAdapter(Receipt.this,receiptItems);
        recyclerview_receipt.setAdapter(receiptCardAdapter);

        Bundle bundle = getIntent().getExtras();
        activity=bundle.getString("activity");

        String status="";
        if (activity.equalsIgnoreCase("transaction")) {
            myJSON = bundle.getString("data");
        }

        try
        {
            JSONObject jsonObject=new JSONObject(myJSON);

            if (jsonObject.has("status"))
            {
                status=jsonObject.getString("status");
            }

            if (status.equalsIgnoreCase("success"))
            {
                imageview_status.setBackground(getResources().getDrawable(R.drawable.success));
            }

            JSONObject benedetails=jsonObject.getJSONObject("benedetails");

            textview_shop_name.setText(benedetails.getString("remiter_name"));

            textview_mobile.setText(benedetails.getString("remiter_number"));

            textview_total_amount.setText(benedetails.getString("full_amount"));

            if (benedetails.has("created_at")) {
                textview_date.setText(benedetails.getString("created_at"));
            }

            if (benedetails.has("beneficiary_name")) {
                textview_beneficiary_name.setText(benedetails.getString("beneficiary_name"));
            }

            textview_bank_name.setText(benedetails.getString("bank_name"));
            textview_accountno.setText(benedetails.getString("account_number"));

            textView_ifsc.setText(benedetails.getString("ifsc"));
            textview_mode.setText(benedetails.getString("payment_mode"));

            JSONArray jsonArray=jsonObject.getJSONArray("reports");
            for (int i=0; i<jsonArray.length(); i++)
            {
                ReceiptItem Item=new ReceiptItem();
                JSONObject data=jsonArray.getJSONObject(i);
                Item.setReceipt_id(data.getString("report_id"));
                Item.setReceipt_bank_ref(data.getString("utr_number"));
                Item.setReceipt_amount(data.getString("amount"));
                Item.setReceipt_status(data.getString("status"));

                receiptItems.add(Item);
            }
            receiptCardAdapter.notifyDataSetChanged();
        }

        catch (JSONException e)
        {
            e.printStackTrace();
        }
    }

    public static Bitmap loadBitmapFromView(View view) {
        view.setDrawingCacheEnabled(true);
        Bitmap createBitmap = Bitmap.createBitmap(view.getDrawingCache());
        view.setDrawingCacheEnabled(false);
        return createBitmap;
    }

    public void mSaveFile(Bitmap bitmap) {
        String file2 = Environment.getExternalStorageDirectory().toString();
        StringBuilder sb = new StringBuilder();
        sb.append(file2);
        sb.append("/");
        sb.append(getResources().getString(R.string.app_name).replaceAll(" ",""));
        sb.append("_receipt");
        File file3 = new File(sb.toString());
        file3.mkdirs();
        int nextInt = new Random().nextInt(10000);
        StringBuilder sb2 = new StringBuilder();
        sb2.append("Image-");
        sb2.append(nextInt);
        sb2.append(".jpg");
        file = new File(file3, sb2.toString());
        StringBuilder sb3 = new StringBuilder();
        sb3.append("");
        sb3.append(file);
        Log.i("Freelinfing", sb3.toString());
        StringBuilder sb4 = new StringBuilder();
        sb4.append("Receipt Downloaded ");
        sb4.append(file);
        Toast.makeText(this, sb4.toString(), Toast.LENGTH_SHORT).show();
        if (file.exists()) {
            file.delete();
        }
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, fileOutputStream);
            fileOutputStream.flush();
            fileOutputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean mCheckWriteStorage() {
        return ContextCompat.checkSelfPermission(getApplicationContext(), "android.permission.WRITE_EXTERNAL_STORAGE") == 0;
    }

    public void mRequestWriteStorage() {
        if (!ActivityCompat.shouldShowRequestPermissionRationale(this, "android.permission.WRITE_EXTERNAL_STORAGE")) {
            ActivityCompat.requestPermissions(this, new String[]{"android.permission.WRITE_EXTERNAL_STORAGE"}, 1);
        }
    }


    private void mSaveImage(Bitmap bitmap) throws IOException {
        boolean saved;
        OutputStream fos;

        int nextInt = new Random().nextInt(10000);
        StringBuilder sb2 = new StringBuilder();
        sb2.append("Image-");
        sb2.append(nextInt);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            ContentResolver resolver = getContentResolver();
            ContentValues contentValues = new ContentValues();
            contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, sb2.toString());
            contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "image/png");
            contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH, "DCIM/" +  getResources().getString(R.string.app_name).replaceAll(" ","_"));
            Uri imageUri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
            fos = resolver.openOutputStream(imageUri);

            Log.e("uri","name "+imageUri.toString()+".png");

            fileuri=imageUri;
//            file=new File(imageUri.toString()+".png");

        }

        else {
            String imagesDir = Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_DCIM).toString() + File.separator + getResources().getString(R.string.app_name).replaceAll(" ","_");

            fileuri=Uri.fromFile(new File(imagesDir));
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

}