package com.demo.apppay2all.LoginMVPDetails;

import android.content.Context;

import com.demo.apppay2all.BaseURL.BaseURL;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Interactor implements LoginContract.Interactor {

    private LoginContract.onGetDataLister mOnGetDataLister;
//    Model login_data=new Model();

    public Interactor(LoginContract.onGetDataLister mOnGetDataLister)
    {
        this.mOnGetDataLister=mOnGetDataLister;
    }

    @Override
    public void makeRetrofitCall(Context context, String url, String username, String password,String device_id) {
        Gson gson= new GsonBuilder()
                .setLenient()
                .create();
        Retrofit retrofit=new Retrofit.Builder()
                .baseUrl(BaseURL.BASEURL_B2C)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        LoginResponse loginResponse=retrofit.create(LoginResponse.class);

//        LoginCredentials loginCredentials=new LoginCredentials(username,password,device_id);
//        retrofit2.Call<JsonElement> call=loginResponse.getLogin(loginCredentials);

        retrofit2.Call<JsonElement> call=loginResponse.getLogin(username,password,device_id);
        call.enqueue(new retrofit2.Callback<JsonElement>()
                     {
                         @Override
                         public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                             System.out.println("Login response =====>>> "+response);
                             String data=response.body()+"";
//                             login_data=response.body();
                             mOnGetDataLister.onSuccess("success",data);
                         }

                         @Override
                         public void onFailure(Call<JsonElement> call, Throwable t) {
                             mOnGetDataLister.onFailure(t.getMessage());
                             t.printStackTrace();
                         }
                     }
        );

    }
}
