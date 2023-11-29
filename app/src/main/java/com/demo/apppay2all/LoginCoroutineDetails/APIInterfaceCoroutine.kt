package com.demo.apppay2all.LoginCoroutineDetails

import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Headers
import retrofit2.http.POST

interface APIInterfaceCoroutine {

    @FormUrlEncoded
    @POST("api/application/v1/login")
    @Headers("Accept: application/json", "Content-Type: application/x-www-form-urlencoded")
    fun getData(@Field("username") username : String,@Field("password") password : String,@Field("device_id") device_id: String) : Call<LoginModelCoroutine>
}