package com.demo.apppay2all.LoginMVPDetails;

import android.content.Context;

public class Presenter implements LoginContract.Presenter,LoginContract.onGetDataLister{

    private LoginContract.View mGetDataView;

    private Interactor mInteractor;
    public Presenter(LoginContract.View mGetDataView)
    {
        this.mGetDataView=mGetDataView;
        this.mInteractor=new Interactor(this);
    }

    @Override
    public void getDataFromUrl(Context context, String url,String username,String password,String device_id) {
        mInteractor.makeRetrofitCall(context,url,username,password,device_id);
    }

    @Override
    public void onSuccess(String message, String list) {
        mGetDataView.onGetDataSuccess(message,list);
    }

    @Override
    public void onFailure(String message) {
        mGetDataView.onGetDataFailure(message);
    }
}