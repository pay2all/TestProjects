package com.demo.apppay2all.LoginMVVMDetails;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;

import okhttp3.ResponseBody;

public class MyViewModel extends AndroidViewModel {

    private final MyRepo repository ;

    public MyViewModel(@NonNull Application application) {
        super(application);
        repository = new MyRepo(application);
    }

    public MutableLiveData<LoginModel> loadData(String username,String password,String device_id) {
        return repository.callAPI(username,password,device_id);
    }

}