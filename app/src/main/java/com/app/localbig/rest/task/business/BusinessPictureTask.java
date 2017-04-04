package com.app.localbig.rest.task.business;

import com.app.localbig.helper.log.LogManager;
import com.app.localbig.model.Business;
import com.app.localbig.model.BusinessPicture;
import com.app.localbig.model.CreateResponse;
import com.app.localbig.rest.ApiInterface;
import com.app.localbig.rest.ResponseLoadCallBack;
import com.app.localbig.rest.ResponseObjectCallBack;
import com.app.localbig.rest.ServiceGenerator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class BusinessPictureTask {

    public ResponseLoadCallBack<Business> callBackList;

    public ResponseObjectCallBack<Object> callBack;

    public BusinessPictureTask(ResponseLoadCallBack callBackList) {
        this.callBackList = callBackList;
    }

    public BusinessPictureTask() {
    }

    public void CallService(int operation, ArrayList<BusinessPicture> pictures) {

        switch (operation) {
            case 1:
                Create(pictures);
                break;
        }
    }

    private void Create(ArrayList<BusinessPicture> pictures) {
        ApiInterface apiService =
                ServiceGenerator.createService(ApiInterface.class);
        Map<String, String> params;
        for (BusinessPicture item : pictures) {

            params = new HashMap<>();
            params.put("businessId", item.getBusinessId());
            params.put("imageBase64", item.getImageBase64());
            params.put("imageFileName", item.getImageFileName());

            apiService.createBusinessPicture(params)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<CreateResponse>() {
                        @Override
                        public final void onCompleted() {
                            LogManager.getInstance().info("Completed", "true");
                        }

                        @Override
                        public final void onError(Throwable e) {
                            LogManager.getInstance().error("Error", "true");
                        }

                        @Override
                        public final void onNext(CreateResponse response) {
                        }
                    });
        }
        LogManager.getInstance().info("All work Completed", "true");
    }
}
