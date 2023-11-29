package com.demo.apppay2all.FlightTicketBooking.FlightDetails;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.demo.apppay2all.FlightTicketBooking.APIInterface;
import com.google.gson.Gson;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class FlightDetailViewModel extends ViewModel {

    public MutableLiveData<FlightDetailsModel> mutableLiveData;

    public FlightDetailViewModel()
    {
        mutableLiveData=new MutableLiveData<>();
    }

    public MutableLiveData<FlightDetailsModel> getMutableLiveData()
    {
        return mutableLiveData;
    }

    public void mCallAPI(String token,String tracid,String indexid,String adult,String child,String infant)
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
        Call<FlightDetailsModel> call=apiInterface.mGetFlightDetail(token, tracid, indexid, adult, child, infant);
        call.enqueue(new Callback<FlightDetailsModel>() {
            @Override
            public void onResponse(Call<FlightDetailsModel> call, Response<FlightDetailsModel> response) {
                Log.e("flight","detail "+new Gson().toJson(response.body()));
                mutableLiveData.postValue(response.body());

            }

            @Override
            public void onFailure(Call<FlightDetailsModel> call, Throwable t) {
                mutableLiveData.postValue(null);
                t.printStackTrace();
            }
        });
    }
}