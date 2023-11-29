package com.demo.apppay2all.FlightTicketBooking.FlightListDetail

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.demo.apppay2all.FlightTicketBooking.APIInterface
import com.google.gson.JsonElement
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class FlightViewModel : ViewModel() {
    @JvmField
    val mutableLiveData: MutableLiveData<JsonElement?>

    init {
        mutableLiveData = MutableLiveData()
    }

    fun mCallAPI(
        token: String?,
        journey: String?,
        origin: String?,
        dest: String?,
        depart: String?,
        adult: String?,
        child: String?,
        infant: String?,
        cabin: String?,
        direct: String?,
        onestop: String?
    ) {
        val okHttpClient = OkHttpClient().newBuilder()
            .connectTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .build()
        val retrofit = Retrofit.Builder()
            .baseUrl("https://travel.bceres.com/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val apiInterface = retrofit.create(APIInterface::class.java)
//        val call = apiInterface.mFlightList(
//            token,
//            journey,
//            origin,
//            dest,
//            depart,
//            adult,
//            child,
//            infant,
//            cabin,
//            direct,
//            onestop
//        )

        viewModelScope.launch {

            try {
                val data = apiInterface.mFlightList(
                    token,
                    journey,
                    origin,
                    dest,
                    depart,
                    adult,
                    child,
                    infant,
                    cabin,
                    direct,
                    onestop
                )

                mutableLiveData.postValue(data)
            }
            catch (e: Exception)
            {
                e.printStackTrace()
                mutableLiveData.postValue(null)
            }

        }



//        call.enqueue(object : Callback<JsonElement?> {
//            override fun onResponse(call: Call<JsonElement?>, response: Response<JsonElement?>) {
//                mutableLiveData.postValue(response.body())
//
////                Log.e("status ","message "+response.body().toString());
////                Log.e("response ","flight "+new Gson().toJson(response.body().toString()));
//            }
//
//            override fun onFailure(call: Call<JsonElement?>, t: Throwable) {
//                mutableLiveData.postValue(null)
//                t.printStackTrace()
//            }
//        })
    }
}