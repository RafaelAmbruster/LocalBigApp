package com.app.localbig.rest.task.account;

import com.app.localbig.model.AccountLoginResponse;

/**
 * Created by Ambruster on 16/08/2016.
 */
public interface ResponseLoginCallBack {
    void onResponseLoginCallBack(AccountLoginResponse response);

    void onError(String message, Integer code);
}
