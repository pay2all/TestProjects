package com.demo.apppay2all.FormDetails;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.demo.apppay2all.BaseURL.BaseURL;
import com.demo.apppay2all.CallResAPIPOSTMethod;
import com.demo.apppay2all.DetectConnection;
import com.demo.apppay2all.R;
import com.demo.apppay2all.SharePrefManager;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class KYCNewDesign extends AppCompatActivity {

    EditText ed_aadhaar_number,ed_pan_number;

    ImageView iv_profile,iv_shop_photo,iv_gst_registration,iv_pan_photo,iv_cancel_cheque,iv_address_proof_photo;

    LinearLayout ll_profile_photo,ll_shop_photo,ll_gst_registration,ll_pan_photo,ll_cancel_cheque,ll_address_proof_photo;
    Button bt_upload;

    String type="photo";

    int PERMISSION_ALL = 1;
    String[] PERMISSIONS = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA
    };

    int CAMERA_INTENT=1,GALLERY_INTENT=2;

    AlertDialog alertDialog;

    String imagepath="";

    Uri.Builder builder=new Uri.Builder();
    String sending_url="",image="";
    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kycnew_design);

        if (getSupportActionBar()!=null)
        {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        ed_aadhaar_number=findViewById(R.id.ed_aadhaar_number);
        ed_pan_number=findViewById(R.id.ed_pan_number);
        ed_pan_number.setText(SharePrefManager.getInstance(KYCNewDesign.this).mGetSingleData("pan_number"));

        iv_profile=findViewById(R.id.iv_profile);
        iv_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!hasPermissions(KYCNewDesign.this, PERMISSIONS)){
                    ActivityCompat.requestPermissions(KYCNewDesign.this, PERMISSIONS, PERMISSION_ALL);
                }
                else
                {
                    type="photo";
                    mShowDialog();
                }
            }
        });

        iv_shop_photo=findViewById(R.id.iv_shop_photo);
        iv_shop_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!hasPermissions(KYCNewDesign.this, PERMISSIONS)){
                    ActivityCompat.requestPermissions(KYCNewDesign.this, PERMISSIONS, PERMISSION_ALL);
                }
                else
                {
                    type="shop";
                    mShowDialog();
                }
            }
        });


        iv_gst_registration=findViewById(R.id.iv_gst_registration);
        iv_gst_registration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!hasPermissions(KYCNewDesign.this, PERMISSIONS)){
                    ActivityCompat.requestPermissions(KYCNewDesign.this, PERMISSIONS, PERMISSION_ALL);
                }
                else
                {
                    type="gst";
                    mShowDialog();
                }
            }
        });


        iv_pan_photo=findViewById(R.id.iv_pan_photo);
        iv_pan_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!hasPermissions(KYCNewDesign.this, PERMISSIONS)){
                    ActivityCompat.requestPermissions(KYCNewDesign.this, PERMISSIONS, PERMISSION_ALL);
                }
                else
                {
                    type="pan";
                    mShowDialog();
                }
            }
        });


        iv_cancel_cheque=findViewById(R.id.iv_cancel_cheque);
        iv_cancel_cheque.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!hasPermissions(KYCNewDesign.this, PERMISSIONS)){
                    ActivityCompat.requestPermissions(KYCNewDesign.this, PERMISSIONS, PERMISSION_ALL);
                }
                else
                {
                    type="cancel";
                    mShowDialog();
                }
            }
        });


        iv_address_proof_photo=findViewById(R.id.iv_address_proof_photo);
        iv_address_proof_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!hasPermissions(KYCNewDesign.this, PERMISSIONS)){
                    ActivityCompat.requestPermissions(KYCNewDesign.this, PERMISSIONS, PERMISSION_ALL);
                }
                else
                {
                    type="address";
                    mShowDialog();
                }
            }
        });


        ll_profile_photo=findViewById(R.id.ll_profile_photo);
        ll_shop_photo=findViewById(R.id.ll_shop_photo);
        ll_gst_registration=findViewById(R.id.ll_gst_registration);
        ll_pan_photo=findViewById(R.id.ll_pan_photo);
        ll_cancel_cheque=findViewById(R.id.ll_cancel_cheque);
        ll_address_proof_photo=findViewById(R.id.ll_address_proof_photo);

        bt_upload=findViewById(R.id.bt_upload);
        bt_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (DetectConnection.checkInternetConnection(KYCNewDesign.this))
                {
                    if (ed_aadhaar_number.getText().toString().equals(""))
                    {
                        Toast.makeText(KYCNewDesign.this, "Please enter aadhaar number", Toast.LENGTH_SHORT).show();
                    }
                    else if (ed_aadhaar_number.getText().toString().length()<12)
                    {
                        Toast.makeText(KYCNewDesign.this, "Please enter a valid aadhaar number", Toast.LENGTH_SHORT).show();
                    }
                    else if (ed_pan_number.getText().toString().equals(""))
                    {
                        Toast.makeText(KYCNewDesign.this, "Please enter PAN number", Toast.LENGTH_SHORT).show();
                    }
                    else if (ed_pan_number.getText().toString().length()<10)
                    {
                        Toast.makeText(KYCNewDesign.this, "Please enter a valid PAN number", Toast.LENGTH_SHORT).show();
                    }
                    else if (iv_profile.getDrawable()==null)
                    {
                        Toast.makeText(KYCNewDesign.this, "Please attach profile photo", Toast.LENGTH_SHORT).show();
                    }
                    else if (iv_shop_photo.getDrawable()==null)
                    {
                        Toast.makeText(KYCNewDesign.this, "Please attach Shop/Office photo", Toast.LENGTH_SHORT).show();
                    }
                    else if (iv_gst_registration.getDrawable()==null)
                    {
                        Toast.makeText(KYCNewDesign.this, "Please attach GST Registration photo", Toast.LENGTH_SHORT).show();
                    }
                    else if (iv_pan_photo.getDrawable()==null)
                    {
                        Toast.makeText(KYCNewDesign.this, "Please attach PAN card photo", Toast.LENGTH_SHORT).show();
                    }
                    else if (iv_cancel_cheque.getDrawable()==null)
                    {
                        Toast.makeText(KYCNewDesign.this, "Please attach Cancelled Cheque photo", Toast.LENGTH_SHORT).show();
                    }
                    else if (iv_address_proof_photo.getDrawable()==null)
                    {
                        Toast.makeText(KYCNewDesign.this, "Please attach address Proof photo", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {

                    }
                }
                else
                {
                    Toast.makeText(KYCNewDesign.this, "No internet connection", Toast.LENGTH_SHORT).show();
                }
            }
        });

        mShowKycImages();
    }

    protected void mShowImage(String type, Bitmap bitmap)
    {
        if (type.equalsIgnoreCase("photo"))
        {
            ll_profile_photo.setVisibility(View.GONE);
            iv_profile.setImageBitmap(bitmap);
        }

       else if (type.equalsIgnoreCase("shop"))
        {
            ll_shop_photo.setVisibility(View.GONE);
            iv_shop_photo.setImageBitmap(bitmap);
        }

       else if (type.equalsIgnoreCase("gst"))
        {
            ll_gst_registration.setVisibility(View.GONE);
            iv_gst_registration.setImageBitmap(bitmap);
        }

       else if (type.equalsIgnoreCase("pan"))
        {
            ll_pan_photo.setVisibility(View.GONE);
            iv_pan_photo.setImageBitmap(bitmap);
        }

       else if (type.equalsIgnoreCase("cancel"))
        {
            ll_cancel_cheque.setVisibility(View.GONE);
            iv_cancel_cheque.setImageBitmap(bitmap);
        }

       else if (type.equalsIgnoreCase("address"))
        {
            ll_address_proof_photo.setVisibility(View.GONE);
            iv_address_proof_photo.setImageBitmap(bitmap);
        }
    }

    public static boolean hasPermissions(Context context, String... permissions) {
        if (context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    protected void mShowDialog()
    {
        LayoutInflater inflater2 = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v2 = inflater2.inflate(R.layout.custome_alertdialog_choose_image, null);

        final AlertDialog.Builder builder2 = new AlertDialog.Builder(KYCNewDesign.this);
        builder2.setCancelable(false);

        builder2.setView(v2);

        RelativeLayout rl_capture=v2.findViewById(R.id.rl_capture);
        rl_capture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                String imageFileName = getResources().getString(R.string.app_name) + ".jpg";
                File storageDir = Environment.getExternalStoragePublicDirectory(
                        Environment.DIRECTORY_PICTURES);
                imagepath = storageDir.getAbsolutePath() + "/" + imageFileName;
                File file = new File(imagepath);
                Uri outputFileUri = Uri.fromFile(file);
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);

                startActivityForResult(cameraIntent, CAMERA_INTENT);
            }
        });
        RelativeLayout rl_from_gallery=v2.findViewById(R.id.rl_from_gallery);
        rl_from_gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"),GALLERY_INTENT);
            }
        });

        alertDialog = builder2.create();
        alertDialog.setCancelable(true);

        alertDialog.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode==RESULT_OK) {

            if (requestCode==CAMERA_INTENT) {
                File imgFile = new File(imagepath);
                if (imgFile.exists()) {
                    Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
//                    mShowImage(type,myBitmap);

                    mSetimageInImageView(type,myBitmap);
                    mShowUploadDialog(myBitmap);
                }
            }
            else
            {
                Uri selectedImage = data.getData();
                InputStream imageStream = null;
                try {
                    imageStream = getApplicationContext().getContentResolver().openInputStream(selectedImage);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                Bitmap yourSelectedImage = BitmapFactory.decodeStream(imageStream);
//                mShowImage(type,yourSelectedImage);

                mSetimageInImageView(type,yourSelectedImage);
                mShowUploadDialog(yourSelectedImage);
            }

        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull @NotNull MenuItem item) {
        if (item.getItemId()==android.R.id.home)
        {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    protected void mSetimageInImageView(String img_type,Bitmap img)
    {
        if (img_type.equalsIgnoreCase("photo"))
        {
            sending_url= BaseURL.BASEURL_B2C+ "api/application/v1/update-profile-photo";
            image=encodeToBase64(img);
            builder.appendQueryParameter("api_token", SharePrefManager.getInstance(KYCNewDesign.this).mGetApiToken());
            builder.appendQueryParameter("profile_photo",image);
        }
        else if (img_type.equalsIgnoreCase("shop"))
        {
            image=encodeToBase64(img);
            sending_url=BaseURL.BASEURL_B2C+ "api/application/v1/update-shop-photo";
            builder.appendQueryParameter("api_token", SharePrefManager.getInstance(KYCNewDesign.this).mGetApiToken());
            builder.appendQueryParameter("shop_photo",image);

        }
        else if (img_type.equalsIgnoreCase("gst"))
        {
            
            image=encodeToBase64(img);
            sending_url=BaseURL.BASEURL_B2C+ "api/application/v1/update-gst-regisration-photo";
            builder.appendQueryParameter("api_token", SharePrefManager.getInstance(KYCNewDesign.this).mGetApiToken());
            builder.appendQueryParameter("gst_regisration_photo",image);
        }
        else if (img_type.equalsIgnoreCase("pan"))
        {
            image=encodeToBase64(img);
            sending_url=BaseURL.BASEURL_B2C+ "api/application/v1/update-pancard-photo";
            builder.appendQueryParameter("api_token", SharePrefManager.getInstance(KYCNewDesign.this).mGetApiToken());
            builder.appendQueryParameter("pancard_photo",image);
        }
        else if (img_type.equalsIgnoreCase("cancel"))
        {
            image=encodeToBase64(img);
            sending_url=BaseURL.BASEURL_B2C+ "api/application/v1/update-cancel-cheque-photo";
            builder.appendQueryParameter("api_token", SharePrefManager.getInstance(KYCNewDesign.this).mGetApiToken());
            builder.appendQueryParameter("cancel_cheque",image);
        }
        else if (img_type.equalsIgnoreCase("address"))
        {
            image=encodeToBase64(img);
            sending_url=BaseURL.BASEURL_B2C+ "api/application/v1/update-address-proof-photo";
            builder.appendQueryParameter("api_token", SharePrefManager.getInstance(KYCNewDesign.this).mGetApiToken());
            builder.appendQueryParameter("address_proof",image);
        }
    }

    public static String encodeToBase64(Bitmap image)
    {
        ByteArrayOutputStream byteArrayOS = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 30, byteArrayOS);
        return Base64.encodeToString(byteArrayOS.toByteArray(), Base64.DEFAULT);
    }

    protected void mShowUploadDialog(Bitmap bitmap)
    {
        LayoutInflater inflater2 = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v2 = inflater2.inflate(R.layout.custome_alert_dialog_sumit_photo, null);

        TextView bt_exit=v2.findViewById(R.id.bt_exit);
        bt_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });

        TextView tv_title=v2.findViewById(R.id.tv_title);
        if (type.equalsIgnoreCase("photo")) {
            tv_title.setText("Profile Photo");
        }
        else if (type.equalsIgnoreCase("shop"))
        {
            tv_title.setText("Shop Photo");
        }
        else if (type.equalsIgnoreCase("cancel"))
        {
            tv_title.setText("Cancelled Cheque Photo");
        }
        else if (type.equalsIgnoreCase("gst"))
        {
            tv_title.setText("GST Registration Photo");
        }
        else if (type.equalsIgnoreCase("pan"))
        {
            tv_title.setText("PAN Card Photo");
        }
        else if (type.equalsIgnoreCase("address"))
        {
            tv_title.setText("Address Proof Photo");
        }

        ImageView iv_photo=v2.findViewById(R.id.iv_photo);
        iv_photo.setImageBitmap(bitmap);

        TextView bt_upload=v2.findViewById(R.id.bt_upload);
        bt_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (DetectConnection.checkInternetConnection(KYCNewDesign.this))
                {
                    if (iv_photo.getDrawable()==null)
                    {
                        Toast.makeText(KYCNewDesign.this, "Please attach photo", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        mSubmitProfilePhoto(bitmap);
                    }
                }
                else
                {
                    Toast.makeText(KYCNewDesign.this, "No internet connection", Toast.LENGTH_SHORT).show();
                }
            }
        });

        final AlertDialog.Builder builder2 = new AlertDialog.Builder(KYCNewDesign.this);
        builder2.setCancelable(false);

        builder2.setView(v2);


        alertDialog = builder2.create();
        alertDialog.setCancelable(true);

        alertDialog.show();
    }


    @SuppressLint("StaticFieldLeak")
    protected void mSubmitProfilePhoto(Bitmap bitmap)
    {
        new CallResAPIPOSTMethod(KYCNewDesign.this,builder,sending_url,true,"POST"){
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                dialog=new ProgressDialog(KYCNewDesign.this);
                dialog.setMessage("Please wait...");
                dialog.setCancelable(false);
                dialog.show();
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                dialog.dismiss();

                Log.e("response","data "+s);

                String status="",message="";
                try {
                    JSONObject jsonObject=new JSONObject(s);
                    if (jsonObject.has("status"))
                    {
                        status=jsonObject.getString("status");
                    }

                    if (jsonObject.has("message"))
                    {
                        message=jsonObject.getString("message");
                    }

                    if (status.equalsIgnoreCase("success"))
                    {
                        if (!message.equals(""))
                        {
                            Toast.makeText(KYCNewDesign.this, message, Toast.LENGTH_SHORT).show();
                        }

                        if (alertDialog!=null)
                        {
                            alertDialog.dismiss();
                        }

                        mShowImage(type,bitmap);

                    }
                    else if (!status.equalsIgnoreCase("success"))
                    {
                        if (!message.equals(""))
                        {
                            Toast.makeText(KYCNewDesign.this, message, Toast.LENGTH_SHORT).show();

                        }
                        else
                        {
                            Toast.makeText(KYCNewDesign.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else
                    {
                        Toast.makeText(KYCNewDesign.this, "Server not responding", Toast.LENGTH_SHORT).show();
                    }

                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }
            }
        }.execute();
    }

    protected void mShowKycImages()
    {
        if (!SharePrefManager.getInstance(KYCNewDesign.this).mGetSingleData("profile_photo").equals("")||!SharePrefManager.getInstance(KYCNewDesign.this).mGetSingleData("profile_photo").equals("null"))
        {
            Glide.with(KYCNewDesign.this).load(SharePrefManager.getInstance(KYCNewDesign.this).mGetSingleData("profile_photo").replaceAll(" ","%20")).error(Glide.with(iv_profile).load(R.drawable.image_icon)).into(iv_profile);
            ll_profile_photo.setVisibility(View.GONE);
        }

        if (!SharePrefManager.getInstance(KYCNewDesign.this).mGetSingleData("pancard_photo").equals("")||!SharePrefManager.getInstance(KYCNewDesign.this).mGetSingleData("pancard_photo").equals("null"))
        {
            Glide.with(KYCNewDesign.this).load(SharePrefManager.getInstance(KYCNewDesign.this).mGetSingleData("pancard_photo").replaceAll(" ","%20")).error(Glide.with(iv_pan_photo).load(R.drawable.image_icon)).into(iv_pan_photo);
            ll_pan_photo.setVisibility(View.GONE);
        }

        if (!SharePrefManager.getInstance(KYCNewDesign.this).mGetSingleData("address_proof").equals("")||!SharePrefManager.getInstance(KYCNewDesign.this).mGetSingleData("address_proof").equals("null"))
        {
            Glide.with(KYCNewDesign.this).load(SharePrefManager.getInstance(KYCNewDesign.this).mGetSingleData("address_proof").replaceAll(" ","%20")).error(Glide.with(iv_address_proof_photo).load(R.drawable.image_icon)).into(iv_address_proof_photo);
            ll_address_proof_photo.setVisibility(View.GONE);
        }

        if (!SharePrefManager.getInstance(KYCNewDesign.this).mGetSingleData("gst_regisration_photo").equals("")||!SharePrefManager.getInstance(KYCNewDesign.this).mGetSingleData("gst_regisration_photo").equals("null"))
        {
            Glide.with(KYCNewDesign.this).load(SharePrefManager.getInstance(KYCNewDesign.this).mGetSingleData("gst_regisration_photo").replaceAll(" ","%20")).error(Glide.with(iv_gst_registration).load(R.drawable.image_icon)).into(iv_gst_registration);
            ll_gst_registration.setVisibility(View.GONE);
        }

        if (!SharePrefManager.getInstance(KYCNewDesign.this).mGetSingleData("cancel_cheque").equals("")||!SharePrefManager.getInstance(KYCNewDesign.this).mGetSingleData("cancel_cheque").equals("null"))
        {
            Glide.with(KYCNewDesign.this).load(SharePrefManager.getInstance(KYCNewDesign.this).mGetSingleData("cancel_cheque").replaceAll(" ","%20")).error(Glide.with(iv_cancel_cheque).load(R.drawable.image_icon)).into(iv_cancel_cheque);
            ll_cancel_cheque.setVisibility(View.GONE);
        }

        if (!SharePrefManager.getInstance(KYCNewDesign.this).mGetSingleData("shop_photo").equals("")||!SharePrefManager.getInstance(KYCNewDesign.this).mGetSingleData("shop_photo").equals("null"))
        {
            Glide.with(KYCNewDesign.this).load(SharePrefManager.getInstance(KYCNewDesign.this).mGetSingleData("shop_photo").replaceAll(" ","%20")).error(Glide.with(iv_shop_photo).load(R.drawable.image_icon)).into(iv_shop_photo);
            ll_shop_photo.setVisibility(View.GONE);
        }
    }


}