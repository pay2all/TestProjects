package com.demo.apppay2all.RechargeReportMVVM;

import com.demo.apppay2all.BaseURL.BaseURL;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitIntance {

    private static Retrofit retrofit;

    public static Retrofit getRetrofitClient()
    {
        if (retrofit==null)
        {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BaseURL.BASEURL_B2C)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }

        return retrofit;
    }
}