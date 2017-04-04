package com.app.localbig.rest.task.business;

import com.app.localbig.model.Business;
import com.app.localbig.model.BusinessFilter;
import com.app.localbig.model.BusinessResponse;
import com.app.localbig.model.CreateResponse;
import com.app.localbig.model.LocalEvent;
import com.app.localbig.rest.ApiInterface;
import com.app.localbig.rest.ResponseLoadCallBack;
import com.app.localbig.rest.ResponseObjectCallBack;
import com.app.localbig.rest.ServiceGenerator;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BusinessTask {

    public ResponseLoadCallBack<Business> callBackList;

    public ResponseObjectCallBack<Object> callBack;

    public BusinessTask(ResponseLoadCallBack callBackList) {
        this.callBackList = callBackList;
    }

    public BusinessTask(ResponseObjectCallBack callBack) {
        this.callBack = callBack;
    }

    public void CallService(int operation, String id, BusinessFilter filter, Business business) {

        ApiInterface apiService =
                ServiceGenerator.createService(ApiInterface.class);

        switch (operation) {
            case 1:
                Load(apiService, filter);
                break;
            case 2:
                LoadById(apiService, id);
                break;
            case 3:
                Create(business);
                break;
            case 4:
                break;
            case 5:
                break;
        }
    }

    private void Create(Business business) {
        ApiInterface apiService =
                ServiceGenerator.createService(ApiInterface.class);

        Map<String, String> params = new HashMap<>();

        params.put("title", business.getTitle());
        params.put("description", business.getDescription());
        params.put("startDate", business.getSinceDate());
        params.put("webSiteUrl", business.getWebSiteUrl());
        params.put("phoneNumber", business.getPhoneNumber());
        params.put("formattedAddress", business.getFormattedAddress());
        params.put("latitude", business.getLatitude());
        params.put("longitude", business.getLongitude());
        params.put("applicationUserId", business.getApplicationUserId());
        params.put("businessCategoryId", business.getBusinessCategory().getId());

        Call<BusinessResponse> call = apiService.createBusiness(params);
        call.enqueue(new Callback<BusinessResponse>() {
            @Override
            public void onResponse(Call<BusinessResponse> call, Response<BusinessResponse> response) {
                try {
                    switch (response.code()) {
                        case 401:
                            callBack.onError(response.message(), response.code());
                            break;

                        case 201:
                            BusinessResponse resp = response.body();
                            callBack.onResponseObjectCallBack(resp);
                            break;

                        case 500:
                            callBack.onError(response.message(), response.code());
                            break;

                        default:
                            callBack.onError(response.message(), response.code());
                            break;
                    }
                } catch (Exception ex) {
                    callBack.onError(ex.toString(), response.code());
                }
            }

            @Override
            public void onFailure(Call<BusinessResponse> call, Throwable t) {
                callBack.onError(t.toString(), 500);
            }
        });
    }

    public void Load(ApiInterface apiService, BusinessFilter filter) {

        Call<List<Business>> call = apiService.getBusiness(filter);
        call.enqueue(new Callback<List<Business>>() {
            @Override
            public void onResponse(Call<List<Business>> call, Response<List<Business>> response) {
                try {
                    switch (response.code()) {

                        case 401:
                            callBackList.onError(response.message(), response.code());
                            break;

                        case 200:
                            callBackList.onResponseLoadCallBack((ArrayList<Business>) response.body());
                            break;

                        case 500:
                            callBackList.onError(response.message(), response.code());
                            break;

                        default:
                            callBack.onError(response.message(), response.code());
                            break;
                    }
                } catch (Exception ex) {
                   callBackList.onError(ex.toString(), response.code());
                }
            }

            @Override
            public void onFailure(Call<List<Business>> call, Throwable t) {
                callBackList.onError(t.toString(), 500);
            }
        });
    }

    public void LoadById(ApiInterface apiService, String BusinessId) {

        Call<Business> call = apiService.getBusinessDetail(BusinessId);
        call.enqueue(new Callback<Business>() {
            @Override
            public void onResponse(Call<Business> call, Response<Business> response) {
                try {
                    switch (response.code()) {
                        case 401:
                            callBack.onError(response.message(), response.code());
                            break;

                        case 200:
                            Business business = response.body();
                            callBack.onResponseObjectCallBack(business);
                            break;

                        case 500:
                            callBack.onError(response.message(), response.code());
                            break;

                        default:
                            callBack.onError(response.message(), response.code());
                            break;
                    }
                } catch (Exception ex) {
                    callBack.onError(ex.toString(), response.code());
                }
            }

            @Override
            public void onFailure(Call<Business> call, Throwable t) {
                callBack.onError(t.toString(), 500);
            }
        });
    }
}
