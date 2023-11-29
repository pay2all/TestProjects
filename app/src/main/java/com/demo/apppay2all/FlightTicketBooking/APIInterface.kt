package com.demo.apppay2all.FlightTicketBooking

import com.demo.apppay2all.FlightTicketBooking.FlightDetails.FlightDetailsModel
import com.google.gson.JsonElement
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Headers
import retrofit2.http.POST

interface APIInterface {
    @FormUrlEncoded
    @Headers("Accept: application/json", "Content-Type: application/x-www-form-urlencoded")
    @POST("api/travel/flight/getOriginDestinationList")
    fun getData(@Field("api_token") api_token: String?): Call<JsonElement?>?

    @FormUrlEncoded
    @Headers("Accept: application/json", "Content-Type: application/x-www-form-urlencoded")
    @POST("api/travel/flight/searchFlight")
    suspend fun mFlightList(
        @Field("api_token") api_token: String?,
        @Field("journeyType") journeyType: String?,
        @Field("origin") origin: String?,
        @Field("destination") destination: String?,
        @Field("departureDate") departureDate: String?,
        @Field("adult") adult: String?,
        @Field("child") child: String?,
        @Field("infant") infant: String?,
        @Field("flightCabinClass") flightCabinClass: String?,
        @Field("DirectFlight") DirectFlight: String?,
        @Field("OneStopFlight") OneStopFlight: String?
    ): JsonElement?

    @FormUrlEncoded
    @Headers("Accept: application/json", "Content-Type: application/x-www-form-urlencoded")
    @POST("api/travel/flight/flightDetails")
    fun mGetFlightDetail(
        @Field("api_token") api_token: String?, @Field("TraceId") traceid: String?,
        @Field("ResultIndex") resultindex: String?,
        @Field("Adult") adult: String?,
        @Field("Child") child: String?,
        @Field("Infant") infant: String?
    ): Call<FlightDetailsModel?>?
}