package com.demo.apppay2all.RechargeReportMVVM;


import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface APIServices {

    @GET("api/reports/recharge-report")
    Call<List<ReportModel>>   getReports(@Query("api_token") String api_token, @Query("fromdate") String fromdate, @Query("todate")  String todate, @Query("status_id")  String status_id, @Query("number") String number);
}
