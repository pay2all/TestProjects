package com.demo.apppay2all.RechargeReportMVVM;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReportViewModel extends ViewModel {

    private MutableLiveData<List<ReportModel>> reportList;

    public ReportViewModel()
    {
        reportList=new MutableLiveData<>();
    }

    public MutableLiveData<List<ReportModel>> getReportList()
    {
        return reportList;
    }

    public void mCallApi(String api_token,String fromdate,String todate,String status_id,String number)
    {
        APIServices apiServices=RetrofitIntance.getRetrofitClient().create(APIServices.class);
        Call<List<ReportModel>> call=apiServices.getReports(api_token,fromdate,todate,status_id,number);
        call.enqueue(new Callback<List<ReportModel>>() {
            @Override
            public void onResponse(Call<List<ReportModel>> call, Response<List<ReportModel>> response) {
                reportList.postValue(response.body());
            }

            @Override
            public void onFailure(Call<List<ReportModel>> call, Throwable t) {
                reportList.setValue(null);
            }
        });
    }
}