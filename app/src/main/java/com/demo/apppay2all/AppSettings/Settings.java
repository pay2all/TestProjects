package com.demo.apppay2all.AppSettings;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.KeyguardManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.bumptech.glide.Glide;
import com.demo.apppay2all.AboutUs;
import com.demo.apppay2all.AllReportsDetails.AllReports;
import com.demo.apppay2all.AppSettings.ReferalDetails.NewMemberLIst;
import com.demo.apppay2all.BaseURL.BaseURL;
import com.demo.apppay2all.CallResAPIPOSTMethod;
import com.demo.apppay2all.ChangePassword;
import com.demo.apppay2all.CommissionDetail.CommissionDetails;
import com.demo.apppay2all.ContactUs;
import com.demo.apppay2all.DetectConnection;
import com.demo.apppay2all.FormDetails.KYCNewDesign;
import com.demo.apppay2all.Login;
import com.demo.apppay2all.LoginNewDetails.LoginNew;
import com.demo.apppay2all.MainActivitySingle;
import com.demo.apppay2all.R;
import com.demo.apppay2all.SharePrefManager;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.dynamiclinks.DynamicLink;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.ShortDynamicLink;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;

import de.hdodenhof.circleimageview.CircleImageView;

public class Settings extends AppCompatActivity {

    ImageView imageview_home;
    LinearLayout ll_history;

    TextView textview_name,textview_last,textview_mobile,textview_email,textview_join_date,textview_address;
    LinearLayout ll_change_password;
    Switch switch_security;
    CircleImageView circularimageview_profile;

    String language,username,password,email,joindate,address,name,profile_image;
    String secure;


    LinearLayout ll_logout;

    private static final int LOCK_REQUEST_CODE = 221;
    private static final int SECURITY_SETTING_REQUEST_CODE = 233;

    boolean screen_lock;
    boolean showlock=true;

    RelativeLayout rl_refer,rl_members,rl_about_us,rl_contact_us,rl_commission;

    TextView tv_main_balance,tv_aeps_balance;

    Uri mInvitationUrl;

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

    String type="photo";

    TextView tv_edit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        if (getSupportActionBar()!=null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        tv_edit=findViewById(R.id.tv_edit);
        tv_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!hasPermissions(Settings.this, PERMISSIONS)){
                    ActivityCompat.requestPermissions(Settings.this, PERMISSIONS, PERMISSION_ALL);
                }
                else
                {
                    type="photo";
                    mShowDialog();
                }
            }
        });


        SharedPreferences sharedPreferences=getSharedPreferences("user",0);
        name=sharedPreferences.getString("first_name","");
        profile_image=sharedPreferences.getString("image","");
        language=sharedPreferences.getString("language","");
        username=sharedPreferences.getString("username","");
        password=sharedPreferences.getString("password","");
        email=sharedPreferences.getString("email","");
        joindate=sharedPreferences.getString("joindate","");
        address=sharedPreferences.getString("address","");

        SharedPreferences sharedPreferences1=getSharedPreferences("secure_payment",0);
        secure=sharedPreferences1.getString("secure","");

        textview_name=findViewById(R.id.textview_name);
        textview_name.setText("First Name : "+SharePrefManager.getInstance(Settings.this).mGetFirstName());

        textview_last=findViewById(R.id.textview_last);
        textview_last.setText("Last Name : "+SharePrefManager.getInstance(Settings.this).mGetLastName());

        textview_mobile=findViewById(R.id.textview_mobile);
        textview_mobile.setText(" : "+username);

        textview_email=findViewById(R.id.textview_email);
        textview_email.setText(" : "+email);

        textview_join_date=findViewById(R.id.textview_join_date);
        textview_join_date.setText(" : "+SharePrefManager.getInstance(Settings.this).mGetSingleData("joing_date"));

        textview_address=findViewById(R.id.textview_address);
        textview_address.setText(" : "+SharePrefManager.getInstance(Settings.this).mGetSingleData("office_address"));

        tv_main_balance=findViewById(R.id.tv_main_balance);
        tv_main_balance.setText(SharePrefManager.getInstance(Settings.this).mGetMainBalance());

        tv_aeps_balance=findViewById(R.id.tv_aeps_balance);
        tv_aeps_balance.setText(SharePrefManager.getInstance(Settings.this).mGetAEPSBalance());

        ll_change_password=findViewById(R.id.ll_change_password);
        ll_change_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Settings.this, ChangePassword.class));
            }
        });

//        circularimageview_profile=findViewById(R.id.circularimageview_profile);
//        if (!profile_image.equals(""))
//        {
//            Picasso.with(this).load(profile_image).into(circularimageview_profile);
//        }
//        else
//        {
//            circularimageview_profile.setImageResource(R.drawable.person);
//        }


        String link = "https://mygame.example.com/?invitedby=" + SharePrefManager.getInstance(Settings.this).mGetUsername();
        FirebaseDynamicLinks.getInstance().createDynamicLink()
                .setLink(Uri.parse(link))
                .setDomainUriPrefix("https://bceres1422.page.link")
                .setAndroidParameters(
                        new DynamicLink.AndroidParameters.Builder(getPackageName())
                                .setMinimumVersion(1)
                                .build())
                .setIosParameters(
                        new DynamicLink.IosParameters.Builder("com.example.ios")
                                .setAppStoreId("123456789")
                                .setMinimumVersion("1.0.1")
                                .build())
                .buildShortDynamicLink()
                .addOnSuccessListener(new OnSuccessListener<ShortDynamicLink>() {
                    @Override
                    public void onSuccess(ShortDynamicLink shortDynamicLink) {
                        mInvitationUrl = shortDynamicLink.getShortLink();
                        // ...
                    }
                });

        rl_refer=findViewById(R.id.rl_refer);
        rl_refer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String referral=SharePrefManager.getInstance(Settings.this).mGetSingleData("referral_code");
                String msgHtml = "Hi There i am using " + getResources().getString(R.string.app_name) + " for best commission on Prepaid Recharge, Utilities Bill Payment, Money Transfer and AEPS, You can use following referral link \n https://play.google.com/store/apps/details?id=" + getPackageName() + "&referrer=" + referral+ " for best Commissions / Cashback";

                Intent i=new Intent(Intent.ACTION_SEND);
                i.setType("text/plain");
                i.putExtra(Intent.EXTRA_TEXT, msgHtml);
                startActivity(Intent.createChooser(i,"Share via"));

            }
        });

        rl_members=findViewById(R.id.rl_members);
        rl_members.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Settings.this, NewMemberLIst.class));
            }
        });

        rl_about_us=findViewById(R.id.rl_about_us);
        rl_about_us.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent i=new Intent(Intent.ACTION_VIEW, Uri.parse(BaseURL.BASEURL_B2C));
//                startActivity(Intent.createChooser(i,"Open With"));
                startActivity(new Intent(Settings.this, AboutUs.class));
            }
        });

        rl_contact_us=findViewById(R.id.rl_contact_us);
        rl_contact_us.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Settings.this, ContactUs.class));
            }
        });

        rl_commission=findViewById(R.id.rl_commission);
        rl_commission.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Settings.this, CommissionDetails.class));
            }
        });

        ll_logout=findViewById(R.id.ll_logout);
        ll_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SharedPreferences sharedPreferences=getSharedPreferences("user",0);
                sharedPreferences.edit().clear().apply();
                SharePrefManager.getInstance(Settings.this).mLogout();

//                startActivity(new Intent(Settings.this, Login.class));
                startActivity(new Intent(Settings.this, LoginNew.class));
                finish();
            }
        });

        imageview_home=findViewById(R.id.imageview_home);
        imageview_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Settings.this, MainActivitySingle.class));
                finish();
            }
        });

        ll_history=findViewById(R.id.ll_history);
        ll_history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Settings.this, AllReports.class));
                finish();
            }
        });

        circularimageview_profile=findViewById(R.id.circularimageview_profile);
        if (!SharePrefManager.getInstance(Settings.this).mGetSharePrefSingleData("profile_photo").equals(""))
        {
            Glide.with(Settings.this).load(SharePrefManager.getInstance(Settings.this).mGetSharePrefSingleData("profile_photo")).into(circularimageview_profile);
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            startActivity(new Intent(Settings.this, MainActivitySingle.class));
            finish();
            return true;
        }
        else if (item.getItemId()==R.id.action_kyc)
        {
            startActivity(new Intent(Settings.this, KYCNewDesign.class));
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(Settings.this, MainActivitySingle.class));
        finish();
    }

    private void authenticateApp() {
        //Get the instance of KeyGuardManager
        KeyguardManager keyguardManager = (KeyguardManager) getSystemService(KEYGUARD_SERVICE);

        //Check if the device version is greater than or equal to Lollipop(21)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //Create an intent to open device screen lock screen to authenticate
            //Pass the Screen Lock screen Title and Description
            Intent i = keyguardManager.createConfirmDeviceCredentialIntent(getResources().getString(R.string.unlock), getResources().getString(R.string.confirm_pattern));
            try {
                //Start activity for result
                startActivityForResult(i, LOCK_REQUEST_CODE);
            } catch (Exception e) {

                //If some exception occurs means Screen lock is not set up please set screen lock
                //Open Security screen directly to enable patter lock
                Intent intent = new Intent(android.provider.Settings.ACTION_SECURITY_SETTINGS);
                try {

                    //Start activity for result
                    startActivityForResult(intent, SECURITY_SETTING_REQUEST_CODE);
                } catch (Exception ex) {

                    //If app is unable to find any Security settings then user has to set screen lock manually
//                    textView.setText(getResources().getString(R.string.setting_label));
                    Toast.makeText(Settings.this, R.string.setting_label, Toast.LENGTH_SHORT).show();
                }
            }
        }
    }


//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        switch (requestCode) {
//            case LOCK_REQUEST_CODE:
//                if (resultCode == RESULT_OK) {
//                    //If screen lock authentication is success update text
//                    showlock=true;
//                    if (screen_lock)
//                    {
//                        switch_security.setText(R.string.enable);
//
//                        SharedPreferences sharedPreferences = getSharedPreferences("secure_payment", 0);
//                        SharedPreferences.Editor editor = sharedPreferences.edit();
//                        editor.putString("secure", "enable");
//                        editor.putString("showdialog","no");
//                        editor.apply();
//                    }
//                    else
//                    {
//                        switch_security.setText(R.string.disable);
//                        SharedPreferences sharedPreferences = getSharedPreferences("secure_payment", 0);
//                        SharedPreferences.Editor editor = sharedPreferences.edit();
//                        editor.putString("secure", "disable");
//                        editor.putString("showdialog","yes");
//                        editor.apply();
//                    }
//
//                } else {
//                    //If screen lock authentication is failed update text
//                    showlock=false;
//                    if (screen_lock)
//                    {
//                        switch_security.setChecked(false);
//                        showlock=true;
//                    }
//                    else
//                    {
//                        switch_security.setChecked(true);
//                        showlock=true;
//                    }
//                    Toast.makeText(this,getResources().getString(R.string.unlock_failed) , Toast.LENGTH_SHORT).show();
//                }
//                break;
//            case SECURITY_SETTING_REQUEST_CODE:
//                //When user is enabled Security settings then we don't get any kind of RESULT_OK
//                //So we need to check whether device has enabled screen lock or not
//                if (isDeviceSecure()) {
//                    //If screen lock enabled show toast and start intent to authenticate user
//                    Toast.makeText(this, getResources().getString(R.string.device_is_secure), Toast.LENGTH_SHORT).show();
//                    authenticateApp();
//                } else {
//                    //If screen lock is not enabled just update text
//
//                    Toast.makeText(Settings.this,getResources().getString(R.string.security_device_cancelled) , Toast.LENGTH_SHORT).show();
//                }
//
//                break;
//        }
//    }

    /**
     * method to return whether device has screen lock enabled or not
     **/
    private boolean isDeviceSecure() {
        KeyguardManager keyguardManager = (KeyguardManager) getSystemService(KEYGUARD_SERVICE);

        //this method only work whose api level is greater than or equal to Jelly_Bean (16)
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN && keyguardManager.isKeyguardSecure();

        //You can also use keyguardManager.isDeviceSecure(); but it requires API Level 23
    }
    //On Click of button do authentication again
    public void authenticateAgain(View view) {
        authenticateApp();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.kyc_menu,menu);

        if (!SharePrefManager.getInstance(Settings.this).mKycStatus())
        {
            menu.findItem(R.id.action_kyc).setVisible(true);
        }
        else
        {
            menu.findItem(R.id.action_kyc).setVisible(false);
        }

        return super.onCreateOptionsMenu(menu);
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

        final AlertDialog.Builder builder2 = new AlertDialog.Builder(Settings.this);
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



    protected void mSetimageInImageView(String img_type,Bitmap img)
    {
        if (img_type.equalsIgnoreCase("photo"))
        {
            sending_url= BaseURL.BASEURL_B2C+ "api/application/v1/update-profile-photo";
            image=encodeToBase64(img);
            builder.appendQueryParameter("api_token", SharePrefManager.getInstance(Settings.this).mGetApiToken());
            builder.appendQueryParameter("profile_photo",image);
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
                if (DetectConnection.checkInternetConnection(Settings.this))
                {
                    if (iv_photo.getDrawable()==null)
                    {
                        Toast.makeText(Settings.this, "Please attach photo", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        mSubmitProfilePhoto(bitmap);
                    }
                }
                else
                {
                    Toast.makeText(Settings.this, "No internet connection", Toast.LENGTH_SHORT).show();
                }
            }
        });

        final AlertDialog.Builder builder2 = new AlertDialog.Builder(Settings.this);
        builder2.setCancelable(false);

        builder2.setView(v2);


        alertDialog = builder2.create();
        alertDialog.setCancelable(true);

        alertDialog.show();
    }


    @SuppressLint("StaticFieldLeak")
    protected void mSubmitProfilePhoto(Bitmap bitmap)
    {
        new CallResAPIPOSTMethod(Settings.this,builder,sending_url,true,"POST"){
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                dialog=new ProgressDialog(Settings.this);
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
                            Toast.makeText(Settings.this, message, Toast.LENGTH_SHORT).show();
                        }

                        if (alertDialog!=null)
                        {
                            alertDialog.dismiss();
                        }

                        circularimageview_profile.setImageBitmap(bitmap);

                    }
                    else if (!status.equalsIgnoreCase("success"))
                    {
                        if (!message.equals(""))
                        {
                            Toast.makeText(Settings.this, message, Toast.LENGTH_SHORT).show();

                        }
                        else
                        {
                            Toast.makeText(Settings.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else
                    {
                        Toast.makeText(Settings.this, "Server not responding", Toast.LENGTH_SHORT).show();
                    }

                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }
            }
        }.execute();
    }
}