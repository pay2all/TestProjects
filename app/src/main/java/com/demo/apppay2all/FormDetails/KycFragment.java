package com.demo.apppay2all.FormDetails;

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
import android.os.StrictMode;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.demo.apppay2all.BaseURL.BaseURL;
import com.demo.apppay2all.CallResAPIPOSTMethod;
import com.demo.apppay2all.DetectConnection;
import com.demo.apppay2all.R;
import com.demo.apppay2all.SharePrefManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;

import static android.app.Activity.RESULT_OK;

public class KycFragment extends Fragment {

    ImageView iv_photo,iv_shop_photo,iv_gst_registration,iv_pan_photo,iv_cancel_check,iv_address_proof;
    ImageButton ib_prev,ib_next;

    String imagepath="";

    int PERMISSION_ALL = 1;
    String[] PERMISSIONS = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA
    };

    Uri.Builder builder=new Uri.Builder();
    String sending_url="";
    ProgressDialog dialog;

    AlertDialog alertDialog;

    int CAMERA_INTENT=1,GALLERY_INTENT=2;
    String image="",image_type="";

    Button bt_photo,bt_shop,bt_gst,bt_pan,bt_cancel,bt_address_proof;

    boolean done1=false,done2=false,done3=false,done4=false,done5=false,done6=false;
    boolean all_done=false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_kyc,container,false);

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        bt_photo=view.findViewById(R.id.bt_photo);
        bt_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (DetectConnection.checkInternetConnection(getActivity()))
                {
                    if (image_type.equalsIgnoreCase("photo")) {
                        if (iv_photo.getDrawable() == null) {
                            Toast.makeText(getContext(), "Please select Profile photo", Toast.LENGTH_SHORT).show();
                        } else {
                            mSubmitProfilePhoto();
                        }
                    }
                }
                else
                {
                    Toast.makeText(getContext(), "No internet connection", Toast.LENGTH_SHORT).show();
                }
            }
        });


        bt_shop=view.findViewById(R.id.bt_shop);
        bt_shop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (DetectConnection.checkInternetConnection(getActivity()))
                {
                    if (image_type.equalsIgnoreCase("shop")) {
                        if (iv_shop_photo.getDrawable() == null) {
                            Toast.makeText(getContext(), "Please select Shop photo", Toast.LENGTH_SHORT).show();
                        } else {
                            mSubmitProfilePhoto();
                        }
                    }
                }
                else
                {
                    Toast.makeText(getContext(), "No internet connection", Toast.LENGTH_SHORT).show();
                }
            }
        });


        bt_gst=view.findViewById(R.id.bt_gst);
        bt_gst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (DetectConnection.checkInternetConnection(getActivity()))
                {
                    if (image_type.equalsIgnoreCase("gst")) {
                        if (iv_gst_registration.getDrawable() == null) {
                            Toast.makeText(getContext(), "Please select GST registration photo", Toast.LENGTH_SHORT).show();
                        } else {
                            mSubmitProfilePhoto();
                        }
                    }
                }
                else
                {
                    Toast.makeText(getContext(), "No internet connection", Toast.LENGTH_SHORT).show();
                }
            }
        });


        bt_pan=view.findViewById(R.id.bt_pan);
        bt_pan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (DetectConnection.checkInternetConnection(getActivity()))
                {
                    if (image_type.equalsIgnoreCase("pan")) {
                        if (iv_pan_photo.getDrawable() == null) {
                            Toast.makeText(getContext(), "Please select PAN card photo", Toast.LENGTH_SHORT).show();
                        } else {
                            mSubmitProfilePhoto();
                        }
                    }
                }
                else
                {
                    Toast.makeText(getContext(), "No internet connection", Toast.LENGTH_SHORT).show();
                }
            }
        });


        bt_cancel=view.findViewById(R.id.bt_cancel);
        bt_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (DetectConnection.checkInternetConnection(getActivity()))
                {
                    if (image_type.equalsIgnoreCase("cancel")) {
                        if (iv_cancel_check.getDrawable() == null) {
                            Toast.makeText(getContext(), "Please select cancel check photo", Toast.LENGTH_SHORT).show();
                        } else {
                            mSubmitProfilePhoto();
                        }
                    }
                }
                else
                {
                    Toast.makeText(getContext(), "No internet connection", Toast.LENGTH_SHORT).show();
                }
            }
        });


        bt_address_proof=view.findViewById(R.id.bt_address_proof);
        bt_address_proof.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (DetectConnection.checkInternetConnection(getActivity()))
                {
                    if (image_type.equalsIgnoreCase("proof")) {
                        if (iv_address_proof.getDrawable() == null) {
                            Toast.makeText(getContext(), "Please select address proof photo", Toast.LENGTH_SHORT).show();
                        } else {
                            mSubmitProfilePhoto();
                        }
                    }
                }
                else
                {
                    Toast.makeText(getContext(), "No internet connection", Toast.LENGTH_SHORT).show();
                }
            }
        });

        ib_prev=view.findViewById(R.id.ib_prev);
        ib_prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((FormsKyc)getActivity()).mChangeFragment(new ChangePasswordFragment(),"1");
            }
        });

        ib_next=view.findViewById(R.id.ib_next);
        ib_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!all_done)
                {
                    Toast.makeText(getContext(), "Please upload all required documents", Toast.LENGTH_SHORT).show();
                }
                else {
                    ((FormsKyc) getActivity()).mChangeFragment(new PersonalInfoFragment(), "3");
                }

            }
        });


        iv_shop_photo=view.findViewById(R.id.iv_shop_photo);
        iv_shop_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!hasPermissions(getContext(), PERMISSIONS)){
                    ActivityCompat.requestPermissions(getActivity(), PERMISSIONS, PERMISSION_ALL);
                }
                else
                {
                    image_type="shop";
                    mShowDialog();
                }
            }
        });


        iv_gst_registration=view.findViewById(R.id.iv_gst_registration);
        iv_gst_registration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!hasPermissions(getContext(), PERMISSIONS)){
                    ActivityCompat.requestPermissions(getActivity(), PERMISSIONS, PERMISSION_ALL);
                }
                else
                {
                    image_type="gst";
                    mShowDialog();
                }
            }
        });


        iv_pan_photo=view.findViewById(R.id.iv_pan_photo);
        iv_pan_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!hasPermissions(getContext(), PERMISSIONS)){
                    ActivityCompat.requestPermissions(getActivity(), PERMISSIONS, PERMISSION_ALL);
                }
                else
                {
                    image_type="pan";
                    mShowDialog();
                }
            }
        });


        iv_cancel_check=view.findViewById(R.id.iv_cancel_check);
        iv_cancel_check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!hasPermissions(getContext(), PERMISSIONS)){
                    ActivityCompat.requestPermissions(getActivity(), PERMISSIONS, PERMISSION_ALL);
                }
                else
                {
                    image_type="cancel";
                    mShowDialog();
                }
            }
        });


        iv_address_proof=view.findViewById(R.id.iv_address_proof);
        iv_address_proof.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!hasPermissions(getContext(), PERMISSIONS)){
                    ActivityCompat.requestPermissions(getActivity(), PERMISSIONS, PERMISSION_ALL);
                }
                else
                {
                    image_type="proof";
                    mShowDialog();
                }
            }
        });


        iv_photo=view.findViewById(R.id.iv_photo);
        iv_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!hasPermissions(getContext(), PERMISSIONS)){
                    ActivityCompat.requestPermissions(getActivity(), PERMISSIONS, PERMISSION_ALL);
                }
                else
                {
                    image_type="photo";
                    mShowDialog();
                }
            }
        });

        return view;
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode==RESULT_OK) {

                    if (requestCode==CAMERA_INTENT) {
                        File imgFile = new File(imagepath);
                        if (imgFile.exists()) {
                            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                            mSetimageInImageView(image_type,myBitmap);
                        }
                    }
                    else
                    {
                        Uri selectedImage = data.getData();
                        InputStream imageStream = null;
                        try {
                            imageStream = getActivity().getApplicationContext().getContentResolver().openInputStream(selectedImage);
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                        Bitmap yourSelectedImage = BitmapFactory.decodeStream(imageStream);
                        mSetimageInImageView(image_type,yourSelectedImage);
                    }

        }
    }

    public static String encodeToBase64(Bitmap image)
    {
        ByteArrayOutputStream byteArrayOS = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 30, byteArrayOS);
        return Base64.encodeToString(byteArrayOS.toByteArray(), Base64.DEFAULT);
    }

    @SuppressLint("StaticFieldLeak")
    protected void mSubmitProfilePhoto()
    {
        new CallResAPIPOSTMethod(getActivity(),builder,sending_url,true,"POST"){
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                dialog=new ProgressDialog(getActivity());
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
                            Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
                        }

                        if (image_type.equalsIgnoreCase("photo"))
                        {
                            done1=true;
                            bt_photo.setVisibility(View.GONE);
                        }
                        else if (image_type.equalsIgnoreCase("shop"))
                        {
                            done2=true;
                            bt_shop.setVisibility(View.GONE);
                        }
                        else if (image_type.equalsIgnoreCase("gst"))
                        {
                            done3=true;
                            bt_gst.setVisibility(View.GONE);
                        }
                        else if (image_type.equalsIgnoreCase("pan"))
                        {
                            done4=true;
                            bt_pan.setVisibility(View.GONE);
                        }
                        else if (image_type.equalsIgnoreCase("cancel"))
                        {
                            done5=true;
                            bt_cancel.setVisibility(View.GONE);
                        }
                        else if (image_type.equalsIgnoreCase("proof"))
                        {
                            done6=true;
                            bt_address_proof.setVisibility(View.GONE);
                        }


                        if (!done1||!done2||!done3||!done4||!done5||!done6)
                        {
                            all_done=false;
                        }
                        else
                        {
                            all_done=true;
                        }

                    }
                    else if (!status.equalsIgnoreCase("success"))
                    {
                        if (!message.equals(""))
                        {
                            Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
                        }
                    }
                    else
                    {
                        Toast.makeText(getContext(), "Something went wrong, please try again...", Toast.LENGTH_SHORT).show();
                    }
                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }
            }
        }.execute();
    }

    protected void mShowDialog()
    {
        LayoutInflater inflater2 = (LayoutInflater)getActivity(). getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v2 = inflater2.inflate(R.layout.custome_alertdialog_choose_image, null);

        final AlertDialog.Builder builder2 = new AlertDialog.Builder(getActivity());
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

    protected void mSetimageInImageView(String img_type,Bitmap img)
    {
        if (img_type.equalsIgnoreCase("photo"))
        {
            sending_url=BaseURL.BASEURL_B2C+ "api/application/v1/update-profile-photo";
            iv_photo.setImageBitmap(img);
            image=encodeToBase64(img);
            builder.appendQueryParameter("api_token", SharePrefManager.getInstance(getActivity()).mGetApiToken());
            builder.appendQueryParameter("profile_photo",image);
            bt_photo.setVisibility(View.VISIBLE);
        }
        else if (img_type.equalsIgnoreCase("shop"))
        {
            iv_shop_photo.setImageBitmap(img);
            image=encodeToBase64(img);
            sending_url=BaseURL.BASEURL_B2C+ "api/application/v1/update-shop-photo";
            builder.appendQueryParameter("api_token", SharePrefManager.getInstance(getActivity()).mGetApiToken());
            builder.appendQueryParameter("shop_photo",image);

            bt_shop.setVisibility(View.VISIBLE);
        }
        else if (img_type.equalsIgnoreCase("gst"))
        {
            iv_gst_registration.setImageBitmap(img);
            image=encodeToBase64(img);
            sending_url=BaseURL.BASEURL_B2C+ "api/application/v1/update-gst-regisration-photo";
            builder.appendQueryParameter("api_token", SharePrefManager.getInstance(getActivity()).mGetApiToken());
            builder.appendQueryParameter("gst_regisration_photo",image);
            bt_gst.setVisibility(View.VISIBLE);
        }
        else if (img_type.equalsIgnoreCase("pan"))
        {
            iv_pan_photo.setImageBitmap(img);
            image=encodeToBase64(img);
            sending_url=BaseURL.BASEURL_B2C+ "api/application/v1/update-pancard-photo";
            builder.appendQueryParameter("api_token", SharePrefManager.getInstance(getActivity()).mGetApiToken());
            builder.appendQueryParameter("pancard_photo",image);
            bt_pan.setVisibility(View.VISIBLE);
        }
        else if (img_type.equalsIgnoreCase("cancel"))
        {
            iv_cancel_check.setImageBitmap(img);
            image=encodeToBase64(img);
            sending_url=BaseURL.BASEURL_B2C+ "api/application/v1/update-cancel-cheque-photo";
            builder.appendQueryParameter("api_token", SharePrefManager.getInstance(getActivity()).mGetApiToken());
            builder.appendQueryParameter("cancel_cheque",image);
            bt_cancel.setVisibility(View.VISIBLE);
        }
        else if (img_type.equalsIgnoreCase("proof"))
        {
            iv_address_proof.setImageBitmap(img);
            image=encodeToBase64(img);
            sending_url=BaseURL.BASEURL_B2C+ "api/application/v1/update-address-proof-photo";
            builder.appendQueryParameter("api_token", SharePrefManager.getInstance(getActivity()).mGetApiToken());
            builder.appendQueryParameter("address_proof",image);
            bt_address_proof.setVisibility(View.VISIBLE);
        }
    }
}
