package com.demo.apppay2all.LoginMVVMDetails;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;

import retrofit2.http.Headers;
import retrofit2.http.POST;
public interface Api {
    @FormUrlEncoded
    @POST("api/application/v1/login")
    @Headers({"Accept: application/json", "Content-Type: application/x-www-form-urlencoded"})
//    Call<Model> getLogin(@Field("username") String username, @Field("password") String password, @Field("device_id") String device_id);
    Call<ResponseBody> getLogin(@Field("username") String username, @Field("password") String password, @Field("device_id") String device_id);
//    Call<JsonElement> getLogin(@Body LoginCredentials data);

}