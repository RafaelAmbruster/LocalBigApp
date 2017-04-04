package com.app.localbig.rest.task.account;

import com.app.localbig.model.AccountRegistrationResponse;

/**
 * Created by Ambruster on 16/08/2016.
 */
public interface ResponseRegisterCallBack {
    void onResponseRegisterCallBack(AccountRegistrationResponse response);

    void onError(String message, Integer code);
}
