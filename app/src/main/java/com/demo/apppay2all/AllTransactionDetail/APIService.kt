package com.demo.apppay2all.AllTransactionDetail

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface APIService {

    @GET("api/reports/status-type")
    suspend fun getStatusList(@Query("api_token") api_token: String): StatusIds

//    @GET("/users/{id}")
//    suspend fun getUser(@Path("id") userId: Int): User

//    @GET("/users/{id}/posts")
//    suspend fun getPostsByUser(@Path("id") userId: Int): List<Post>

}