package com.app.localbig.rest.task.account;
import com.app.localbig.model.AccountRegistrationResponse;
import com.app.localbig.rest.ApiInterface;
import com.app.localbig.rest.ServiceGenerator;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegistrationTask {

    public ResponseRegisterCallBack callBack;

    public RegistrationTask(ResponseRegisterCallBack callBack) {
        this.callBack = callBack;
    }

    public void CallService(String username, String password, String phone, Boolean owner, String first, String last) {

        ApiInterface apiService =
                ServiceGenerator.createService(ApiInterface.class);

        Map<String, String> params = new HashMap<>();
        params.put("email", username);
        params.put("password", password);
        params.put("phoneNumber", phone);
        params.put("owner", String.valueOf(owner));
        params.put("firstName", first);
        params.put("lastName", last);

        Call<AccountRegistrationResponse> call = apiService.Register(params);
        call.enqueue(new Callback<AccountRegistrationResponse>() {
            @Override
            public void onResponse(Call<AccountRegistrationResponse> call, Response<AccountRegistrationResponse> response) {
                try {
                    switch (response.code()) {
                        case 401:
                            callBack.onError(response.message(), response.code());
                            break;

                        case 201:
                            AccountRegistrationResponse resp = response.body();
                            callBack.onResponseRegisterCallBack(resp);
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
            public void onFailure(Call<AccountRegistrationResponse> call, Throwable t) {
                callBack.onError(t.toString(), 500);
            }
        });
    }
}
