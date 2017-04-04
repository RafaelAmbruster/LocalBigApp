package com.app.localbig.rest.task.account;

import com.app.localbig.model.ApplicationUser;
import com.app.localbig.rest.ApiInterface;
import com.app.localbig.rest.ResponseLoadCallBack;
import com.app.localbig.rest.ResponseObjectCallBack;
import com.app.localbig.rest.ServiceGenerator;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserApplicationTask {

    public ResponseLoadCallBack<ApplicationUser> callBackList;
    public ResponseObjectCallBack<ApplicationUser> callBack;

    public UserApplicationTask(ResponseLoadCallBack callBackList) {
        this.callBackList = callBackList;
    }

    public UserApplicationTask(ResponseObjectCallBack callBack) {
        this.callBack = callBack;
    }

    public void CallService(int operation, String id) {

        ApiInterface apiService =
                ServiceGenerator.createService(ApiInterface.class);

        switch (operation) {
            case 1:
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

    public void LoadById(ApiInterface apiService, String appUserId) {

        Call<ApplicationUser> call = apiService.getAppUserInformation(appUserId);
        call.enqueue(new Callback<ApplicationUser>() {
            @Override
            public void onResponse(Call<ApplicationUser> call, Response<ApplicationUser> response) {
                try {
                    switch (response.code()) {
                        case 401:
                            callBack.onError(response.message(), response.code());
                            break;

                        case 200:
                            ApplicationUser addresses = response.body();
                            callBack.onResponseObjectCallBack(addresses);
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
            public void onFailure(Call<ApplicationUser> call, Throwable t) {
                callBack.onError(t.toString(), 500);
            }
        });
    }
}
