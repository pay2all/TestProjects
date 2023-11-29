package com.demo.apppay2all.LoginMVVMDetails;

import com.demo.apppay2all.BaseURL.BaseURL;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class RetrofitClientCopy {

    private static final String base_url = BaseURL.BASEURL_B2C;//base url
    private static RetrofitClientCopy instance;
    private Retrofit retrofit;//retrofit object

    private RetrofitClientCopy() { //constructor
        retrofit = new Retrofit.Builder().baseUrl(base_url).addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create()).build();
    }

    public static synchronized RetrofitClientCopy getInstance() {
        if (instance == null) {
            instance = new RetrofitClientCopy();
        }
        return instance;

    }

    public Api getapi() {
        return retrofit.create(Api.class);
    }
}
