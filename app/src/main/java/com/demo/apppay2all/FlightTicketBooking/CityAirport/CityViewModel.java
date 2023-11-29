package com.demo.apppay2all.FlightTicketBooking.CityAirport;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.demo.apppay2all.FlightTicketBooking.APIInterface;
import com.google.gson.JsonElement;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CityViewModel extends ViewModel {

    private MutableLiveData<JsonElement> mutableLiveData;

    public CityViewModel()
    {
        mutableLiveData=new MutableLiveData<>();
    }

    public MutableLiveData<JsonElement> getMutableLiveData()
    {
        return mutableLiveData;
    }

    public void mGetFromDestList(String api_token)
    {
        Retrofit retrofit=new Retrofit.Builder()
                .baseUrl("https://travel.bceres.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        APIInterface apiInterface=retrofit.create(APIInterface.class);
        Call<JsonElement> call=apiInterface.getData(api_token);
        call.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                mutableLiveData.postValue(response.body());
            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
                mutableLiveData.postValue(null);
            }
        });
    }
}
