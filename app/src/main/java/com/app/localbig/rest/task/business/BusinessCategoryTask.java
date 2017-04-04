package com.app.localbig.rest.task.business;

import com.app.localbig.model.BusinessCategory;
import com.app.localbig.rest.ApiInterface;
import com.app.localbig.rest.ResponseLoadCallBack;
import com.app.localbig.rest.ResponseObjectCallBack;
import com.app.localbig.rest.ServiceGenerator;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BusinessCategoryTask {

    public ResponseLoadCallBack<BusinessCategory> callBackList;

    public ResponseObjectCallBack<BusinessCategory> callBack;

    public BusinessCategoryTask(ResponseLoadCallBack callBackList) {
        this.callBackList = callBackList;
    }

    public BusinessCategoryTask(ResponseObjectCallBack callBack) {
        this.callBack = callBack;
    }

    public void CallService(int operation, String id) {

        ApiInterface apiService =
                ServiceGenerator.createService(ApiInterface.class);

        switch (operation) {
            case 1:
                Load(apiService);
                break;
            case 2:
                LoadById(apiService, id);
                break;
            case 3:
                break;
            case 4:
                break;
            case 5:
                break;
        }
    }

    public void Load(ApiInterface apiService) {

        Call<List<BusinessCategory>> call = apiService.getBusinessCategories();
        call.enqueue(new Callback<List<BusinessCategory>>() {
            @Override
            public void onResponse(Call<List<BusinessCategory>> call, Response<List<BusinessCategory>> response) {
                try {
                    switch (response.code()) {

                        case 401:
                            callBackList.onError(response.message(), response.code());
                            break;

                        case 200:
                            callBackList.onResponseLoadCallBack((ArrayList<BusinessCategory>) response.body());
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
            public void onFailure(Call<List<BusinessCategory>> call, Throwable t) {
                callBackList.onError(t.toString(), 500);
            }
        });
    }

    public void LoadById(ApiInterface apiService, String BusinessId) {

        Call<BusinessCategory> call = apiService.getBusinessCategoryDetail(BusinessId);
        call.enqueue(new Callback<BusinessCategory>() {
            @Override
            public void onResponse(Call<BusinessCategory> call, Response<BusinessCategory> response) {
                try {
                    switch (response.code()) {
                        case 401:
                            callBack.onError(response.message(), response.code());
                            break;

                        case 200:
                            BusinessCategory business = response.body();
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
            public void onFailure(Call<BusinessCategory> call, Throwable t) {
                callBack.onError(t.toString(), 500);
            }
        });
    }
}
