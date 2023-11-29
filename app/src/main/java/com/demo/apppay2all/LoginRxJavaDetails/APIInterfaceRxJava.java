package com.demo.apppay2all.LoginRxJavaDetails;


import com.google.gson.JsonElement;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIInterfaceRxJava {

    @FormUrlEncoded
    @POST("api/application/v1/login")
    @Headers({"Accept: application/json", "Content-Type: application/x-www-form-urlencoded"})
    Observable<LoginModelRxJava> getData(@Field("username") String username, @Field("password") String password, @Field("device_id") String device_id);
}
