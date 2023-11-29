package com.demo.apppay2all.LoginMVVMDetails;

import android.app.Application;

import androidx.lifecycle.MutableLiveData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyRepo {

//    private final MutableLiveData<ArrayList<ResponseBody>> allCountries;
//    private final ArrayList<ResponseBody> countryList;

    private final MutableLiveData<LoginModel> allCountries;
    private LoginModel countryList;

    public MyRepo(Application application) {
        //application is subclass of context

        //cant call abstract func but since instance is there we can do this
        countryList = null;
        allCountries = new MutableLiveData<>();

    }

    public MutableLiveData<LoginModel> callAPI(String username,String password,String device_id){

        Call<ResponseBody> call = RetrofitClient.getInstance().getapi().getLogin(username,password,device_id);
        call.enqueue(new Callback<ResponseBody>() {

            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                if(response.code() == 200) {

                    try {
                        assert response.body() != null;

//                        JSONArray dataArray = new JSONArray(response.body().string());

                        JSONObject jsonObject=new JSONObject(response.body().string());
                        LoginModel loginModel=new LoginModel();

                        if (jsonObject.has("status"))
                        {
                            loginModel.setStatus(jsonObject.getString("status"));

                            if (loginModel.getStatus().equalsIgnoreCase("success"))
                            {
                                Status status=Status.SUCCESS;
                                loginModel.setStatus_type(status);
                            }
                            
                        }

                        if (jsonObject.has("userdetails"))
                        {
                            loginModel.setUserdetails(jsonObject.getJSONObject("userdetails"));
                        }

                        if (jsonObject.has("userservices"))
                        {
                            loginModel.setUserservices(jsonObject.getJSONObject("userservices"));
                        }
                        if (jsonObject.has("companydetails"))
                        {
                            loginModel.setCompanydetails(jsonObject.getJSONObject("companydetails"));
                        }
                        if (jsonObject.has("banner"))
                        {
                            loginModel.setBanner(jsonObject.getJSONArray("banner"));
                        }

                        if (jsonObject.has("errors"))
                        {
                            loginModel.setErrors(jsonObject.getJSONObject("errors"));
                            Status status=Status.FAILURE;
                            loginModel.setStatus_type(status);
                        }

                        countryList=loginModel;

//                        for (int i = 0; i < dataArray.length(); i++) {
//
//                            LoginModel modelRecycler = new LoginModel();
//                            JSONObject dataobj = dataArray.getJSONObject(i);
//
//                            modelRecycler.setName(dataobj.getString("name"));
//                            modelRecycler.setRegion(dataobj.getString("region"));
//                            modelRecycler.setCapital(dataobj.getString("capital"));
//                            modelRecycler.setFlag(dataobj.getString("flag"));
//
//                            modelRecycler.setSubregion(dataobj.getString("subregion"));
//                            modelRecycler.setPopulation(dataobj.getLong("population"));
//                            modelRecycler.setBorders(dataobj.getJSONArray("borders"));
//
//                            modelRecycler.setLanguages(dataobj.getJSONArray("languages"));
//
//                            //   System.out.println("dataobj.getString(\"flag\") = " + dataobj.getJSONArray("languages"));
//
//                            //  productCode.add(dataobj.getString("productCode"));
//
//
//                            countryList.add(modelRecycler);
//
//                        }
                    }
                    catch (JSONException | IOException e) {
                        e.printStackTrace();
                        System.out.println("e.getMessage() = " + e.getMessage());
                        LoginModel loginModel=new LoginModel();

                        loginModel.setStatus_type(Status.ERROR);
                        loginModel.setMessage(e.getMessage());
                        countryList=loginModel;
                    }
                    allCountries.setValue(countryList);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                //failed
                allCountries.postValue(null);
                System.out.println("t.getMessage() = " + t.getMessage());
            }
        });
        return allCountries;
    }
}