package com.demo.apppay2all.LoginMVVMDetails;

import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.demo.apppay2all.DetectConnection;
import com.demo.apppay2all.LoginMVPDetails.LoginMvp;
import com.demo.apppay2all.R;

public class LoginMVVM extends AppCompatActivity {

    EditText ed_username, ed_password;

    ImageView iv_login;

    MyViewModel myViewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_mvvm);

        ed_username =findViewById(R.id.ed_username);
        ed_password =findViewById(R.id.ed_password);

        iv_login=findViewById(R.id.iv_login);
        iv_login.setEnabled(true);
        myViewModel=new ViewModelProvider(this).get(MyViewModel.class);

        iv_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (DetectConnection.checkInternetConnection(LoginMVVM.this))
                {
                    if (ed_username.getText().toString().equals(""))
                    {
                        Toast.makeText(LoginMVVM.this, "Please enter username", Toast.LENGTH_SHORT).show();
                    }
                    else if (ed_username.getText().toString().equals(""))
                    {
                        Toast.makeText(LoginMVVM.this, "Please enter a valid username", Toast.LENGTH_SHORT).show();
                    }
                    else if (ed_password.getText().toString().equals(""))
                    {
                        Toast.makeText(LoginMVVM.this, "Please enter password", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {

                        String device_id= Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);

//                        myViewModel.loadData( ed_username.getText().toString(), ed_password.getText().toString(),device_id);
                        myViewModel.loadData( ed_username.getText().toString(), ed_password.getText().toString(),device_id).observe(LoginMVVM.this, new Observer<LoginModel>() {
                            @Override
                            public void onChanged(LoginModel loginModel) {
                                if (loginModel.getStatus_type()==Status.SUCCESS)
                                {
                                    Toast.makeText(LoginMVVM.this, "Login Success \n"+loginModel.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                                else if (loginModel.getStatus_type()==Status.ERROR)
                                {
                                    Toast.makeText(LoginMVVM.this, "Login Error\n"+loginModel.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                                else if (loginModel.getStatus_type()==Status.FAILURE)
                                {
                                    Toast.makeText(LoginMVVM.this, "Login Failure\n"+loginModel.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                }
                else
                {
                    Toast.makeText(LoginMVVM.this, "No internet connection", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}