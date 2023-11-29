package com.demo.apppay2all.FlightTicketBooking.FlightListDetail;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.demo.apppay2all.FlightTicketBooking.APIInterface;
import com.google.gson.JsonElement;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class FlightViewModelCopy extends ViewModel {
    private MutableLiveData<JsonElement> mutableLiveData;

    public FlightViewModelCopy()
    {
        mutableLiveData=new MutableLiveData<>();
    }

    public MutableLiveData<JsonElement> getMutableLiveData()
    {
        return mutableLiveData;
    }

    public void mCallAPI(String token,String journey,String origin,String dest,String depart,String adult,String child,String infant,String cabin,String direct,String onestop)
    {
        OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
                .connectTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .build();

        Retrofit retrofit=new Retrofit.Builder()
                .baseUrl("https://travel.bceres.com/")
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        APIInterface apiInterface=retrofit.create(APIInterface.class);
//        Call<JsonElement> call=apiInterface.mFlightList(token,journey,origin,dest,depart,adult,child,infant,cabin,direct,onestop);



//        call.enqueue(new Callback<JsonElement>() {
//            @Override
//            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
//                mutableLiveData.postValue(response.body());
//
////                Log.e("status ","message "+response.body().toString());
////                Log.e("response ","flight "+new Gson().toJson(response.body().toString()));
//
//            }
//
//            @Override
//            public void onFailure(Call<JsonElement> call, Throwable t) {
//                mutableLiveData.postValue(null);
//                t.printStackTrace();
//            }
//        });
    }
}