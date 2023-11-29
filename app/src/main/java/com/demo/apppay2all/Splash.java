package com.demo.apppay2all;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.webkit.URLUtil;
import android.widget.TextView;
import android.widget.Toast;

import com.android.installreferrer.api.InstallReferrerClient;
import com.android.installreferrer.api.InstallReferrerStateListener;
import com.android.installreferrer.api.ReferrerDetails;
import com.demo.apppay2all.BaseURL.BaseURL;
import com.demo.apppay2all.FlightTicketBooking.FlightSearch;
import com.demo.apppay2all.LoginCoroutineDetails.LoginUsingCoroutine;
import com.demo.apppay2all.LoginMVPDetails.LoginMvp;
import com.demo.apppay2all.LoginMVVMDetails.LoginMVVM;
import com.demo.apppay2all.LoginNewDetails.LoginNew;
import com.demo.apppay2all.LoginRxJavaDetails.LoginRxJava;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.PendingDynamicLinkData;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;

import org.json.JSONException;
import org.json.JSONObject;

public class Splash extends AppCompatActivity {

    String name;

    TextView textview_name_version,textview_copyright;

    FirebaseRemoteConfig mFirebaseRemoteConfig=null;

    String app_details="";

    String app_name="",app_url="",app_id="";
    boolean url_changed=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        getSupportActionBar().hide();

        SharedPreferences sharedPreferences=getSharedPreferences("user",0);
        name=sharedPreferences.getString("first_name","");

        mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
        FirebaseRemoteConfigSettings configSettings = new FirebaseRemoteConfigSettings.Builder()
                .setMinimumFetchIntervalInSeconds(20)
                .build();

        mFirebaseRemoteConfig.setConfigSettingsAsync(configSettings);
        mFirebaseRemoteConfig.setDefaultsAsync(R.xml.remote_config_defaults);

        mFirebaseRemoteConfig.fetchAndActivate()
                .addOnCompleteListener(this, new OnCompleteListener<Boolean>() {
                    @Override
                    public void onComplete(@NonNull Task<Boolean> task) {
                        if (task.isSuccessful()) {
                            boolean updated = task.getResult();
                            Log.e("TAG", "Config params updated: " + updated);
                            Log.e("fetch", "success: ");

                            app_details=mFirebaseRemoteConfig.getString("app_details");

                            if (!app_details.equals(""))
                            {
                                try {
                                    JSONObject jsonObject=new JSONObject(app_details);

                                    if (jsonObject.has("app_name"))
                                    {
                                        app_name=jsonObject.getString("app_name");
                                    }

                                    if (jsonObject.has("app_url"))
                                    {
                                        app_url=jsonObject.getString("app_url");
                                    }

                                    if (jsonObject.has("app_id"))
                                    {
                                        app_id=jsonObject.getString("app_id");
                                    }

                                    if (jsonObject.has("url_changed"))
                                    {
                                        url_changed=jsonObject.getBoolean("url_changed");
                                    }

                                    if (url_changed)
                                    {
                                        if (app_id.equalsIgnoreCase(getPackageName())) {
                                            if (!app_url.equals("")) {
                                                BaseURL.BASEURL_B2C = app_url;
                                            }
                                        }
                                    }
                                }
                                catch (JSONException e)
                                {
                                    e.printStackTrace();
                                }
                            }
                        }
                        else {
                            Log.e("fetch", "failed: ");
//                            Toast.makeText(Splash.this, "Fetch failed",
//                                    Toast.LENGTH_SHORT).show();
                        }
//                        displayWelcomeMessage();
                    }
                });

        InstallReferrerClient mReferrerClient;

        mReferrerClient = InstallReferrerClient.newBuilder(this).build();
        mReferrerClient.startConnection(new InstallReferrerStateListener() {
            @Override
            public void onInstallReferrerSetupFinished(int responseCode) {
                switch (responseCode) {
                    case InstallReferrerClient.InstallReferrerResponse.OK:
                        // Connection established
                        try {
                            ReferrerDetails response =
                                    mReferrerClient.getInstallReferrer();
                            if (!response.getInstallReferrer().contains("utm_source"))

                                SharePrefManager.getInstance(Splash.this).mSaveSingleData("data_referal1", response.getInstallReferrer());
//                                edtPRferelCode.setText("" + response.getInstallReferrer());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        mReferrerClient.endConnection();
                        break;
                    case InstallReferrerClient.InstallReferrerResponse.FEATURE_NOT_SUPPORTED:
                        // API not available on the current Play Store app
                        break;
                    case InstallReferrerClient.InstallReferrerResponse.SERVICE_UNAVAILABLE:
                        // Connection could not be established
                        break;
                    case InstallReferrerClient.InstallReferrerResponse.DEVELOPER_ERROR:
                        break;
                    case InstallReferrerClient.InstallReferrerResponse.SERVICE_DISCONNECTED:
                        break;
                }
            }

            @Override
            public void onInstallReferrerServiceDisconnected() {
                // Try to restart the connection on the next request to
                // Google Play by calling the startConnection() method.
            }
        });

        textview_name_version=findViewById(R.id.textview_name_version);

        try {
            PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            String version = pInfo.versionName;
            textview_name_version.setText(getResources().getString(R.string.app_name)+" "+version);
        }
        catch ( PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        textview_copyright=findViewById(R.id.textview_copyright);
        textview_copyright.setText("2023 © "+getResources().getString(R.string.app_name)+". All rights reserved ");

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                if (name.equals(""))
                {
                    Intent intent=new Intent(Splash.this, FlightSearch.class);
//                    Intent intent=new Intent(Splash.this, LoginNew.class);
//                    Intent intent=new Intent(Splash.this, Login.class);
//                    Intent intent=new Intent(Splash.this, LoginMvp.class);
//                    Intent intent=new Intent(Splash.this, LoginMVVM.class);
//                    Intent intent=new Intent(Splash.this, LoginRxJava.class);
//                    Intent intent=new Intent(Splash.this, TestDesignNew.class);
//                    Intent intent=new Intent(Splash.this, LoginUsingCoroutine.class);
                    startActivity(intent);
                    finish();
                }
                else
                {
//                    Intent intent = new Intent(Splash.this, MainActivitySingle.class);
                    Intent intent = new Intent(Splash.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        },2000);

        FirebaseDynamicLinks.getInstance()
                .getDynamicLink(getIntent())
                .addOnSuccessListener(this, new OnSuccessListener<PendingDynamicLinkData>() {
                    @Override
                    public void onSuccess(PendingDynamicLinkData pendingDynamicLinkData) {
                        // Get deep link from result (may be null if no link is found)
                        Uri deepLink = null;
                        if (pendingDynamicLinkData != null) {
                            deepLink = pendingDynamicLinkData.getLink();
                        }
                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                        if (user == null
                                && deepLink != null
                                && deepLink.getBooleanQueryParameter("invitedby", false)) {
                            String referrerUid = deepLink.getQueryParameter("invitedby");

                            SharePrefManager.getInstance(Splash.this).mSaveSingleData("referral_id",referrerUid);
                        }
                    }
                });
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (mFirebaseRemoteConfig!=null) {
            Log.e("change", "value " + mFirebaseRemoteConfig.getString("app_details"));

            app_details=mFirebaseRemoteConfig.getString("app_details");

            if (!app_details.equals(""))
            {
                try {
                    JSONObject jsonObject=new JSONObject(app_details);

                    if (jsonObject.has("app_name"))
                    {
                        app_name=jsonObject.getString("app_name");
                    }

                    if (jsonObject.has("app_url"))
                    {
                        app_url=jsonObject.getString("app_url");
                    }

                    if (jsonObject.has("app_id"))
                    {
                        app_id=jsonObject.getString("app_id");
                    }

                    if (jsonObject.has("url_changed"))
                    {
                        url_changed=jsonObject.getBoolean("url_changed");
                    }

                    if (url_changed)
                    {
                        if (app_id.equalsIgnoreCase(getPackageName())) {
                            if (!app_url.equals("")) {
                                BaseURL.BASEURL_B2C = app_url;
                            }
                        }
                    }
                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }
            }
        }
    }
}