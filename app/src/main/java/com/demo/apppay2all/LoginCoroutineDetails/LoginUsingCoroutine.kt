package com.demo.apppay2all.LoginCoroutineDetails

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.demo.apppay2all.DetectConnection
import com.demo.apppay2all.R
import com.google.gson.JsonElement
import retrofit2.Retrofit

class LoginUsingCoroutine : AppCompatActivity() {

    lateinit var retrofit: Retrofit
    lateinit var ed_mobile: EditText
    lateinit var ed_password:EditText
    lateinit var bt_login: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_using_coroutine)

        bt_login=findViewById(R.id.bt_login);
        ed_mobile=findViewById(R.id.ed_mobile);
        ed_password=findViewById(R.id.ed_password);
        bt_login=findViewById(R.id.bt_login);

        bt_login.setOnClickListener {
            if (DetectConnection.checkInternetConnection(this@LoginUsingCoroutine))
            {
                if (ed_mobile.text.equals(""))
                {
                    mShowToast("Please enter mobile number")
                }
                else if (ed_mobile.text.toString().length<10)
                {
                    mShowToast("Please enter a valid mobile number")
                }
            }
            else{
                mShowToast("No internet connection")
            }
        }
    }

    fun mShowToast(message : String)
    {
        Toast.makeText(applicationContext,message,Toast.LENGTH_SHORT).show()
    }
}