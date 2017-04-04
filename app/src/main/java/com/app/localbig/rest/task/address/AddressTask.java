package com.app.localbig.rest.task.address;

import com.app.localbig.model.Address;
import com.app.localbig.rest.ApiInterface;
import com.app.localbig.rest.ResponseLoadCallBack;
import com.app.localbig.rest.ResponseObjectCallBack;
import com.app.localbig.rest.ServiceGenerator;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddressTask {

    public ResponseLoadCallBack<Address> callBackList;
    public ResponseObjectCallBack<Address> callBack;

    public AddressTask(ResponseLoadCallBack callBackList) {
        this.callBackList = callBackList;
    }

    public AddressTask(ResponseObjectCallBack callBack) {
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

        Call<List<Address>> call = apiService.getAddress();
        call.enqueue(new Callback<List<Address>>() {
            @Override
            public void onResponse(Call<List<Address>> call, Response<List<Address>> response) {
                try {
                    switch (response.code()) {
                        case 401:
                            callBackList.onError(response.message(), response.code());
                            break;

                        case 200:
                            List<Address> addresses = response.body();
                            callBackList.onResponseLoadCallBack((ArrayList<Address>) addresses);
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
            public void onFailure(Call<List<Address>> call, Throwable t) {
                callBackList.onError(t.toString(), 500);
            }
        });
    }

    public void LoadById(ApiInterface apiService, String AddressId) {

        Call<Address> call = apiService.getAddressDetails(AddressId);
        call.enqueue(new Callback<Address>() {
            @Override
            public void onResponse(Call<Address> call, Response<Address> response) {
                try {
                    switch (response.code()) {
                        case 401:
                            callBack.onError(response.message(), response.code());
                            break;

                        case 200:
                            Address addresses = response.body();
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
            public void onFailure(Call<Address> call, Throwable t) {
                callBack.onError(t.toString(), 500);
            }
        });
    }
}
