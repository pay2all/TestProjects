package com.demo.apppay2all.LoginMVPDetails;

import android.content.Context;

public interface LoginContract {

    interface View
    {
        void onGetDataSuccess(String message, String data);
        void onGetDataFailure(String message);
    }

    interface Presenter
    {
        void getDataFromUrl(Context context,String url,String username,String password,String device_id);
    }

    interface Interactor
    {
        void makeRetrofitCall(Context context,String url,String username,String password,String device_id);
    }

    interface onGetDataLister
    {
        void onSuccess(String message, String data);
        void onFailure(String message);
    }

}