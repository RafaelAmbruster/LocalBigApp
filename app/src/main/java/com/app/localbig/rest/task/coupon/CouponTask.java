package com.app.localbig.rest.task.coupon;


import com.app.localbig.model.Coupon;
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

public class CouponTask {

    public ResponseLoadCallBack<Coupon> callBackList;
    public ResponseObjectCallBack<Object> callBack;

    public CouponTask(ResponseLoadCallBack callBackList) {
        this.callBackList = callBackList;
    }

    public CouponTask(ResponseObjectCallBack callBack) {
        this.callBack = callBack;
    }

    public void CallService(int operation, String id, Coupon coupon) {

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
                Create(coupon);
                break;
            case 4:
                break;
            case 5:
                break;
        }
    }

    private void Create(Coupon coupon) {
        ApiInterface apiService =
                ServiceGenerator.createService(ApiInterface.class);

        Map<String, String> params = new HashMap<>();

        params.put("description", coupon.getDescription());
        params.put("startDate", coupon.getStartDate());
        params.put("endDate", coupon.getEndDate());
        params.put("active", String.valueOf(coupon.getActive()));
        params.put("discount", String.valueOf(coupon.getDiscount()));
        params.put("inPercent", String.valueOf(coupon.getInPercent()));
        params.put("placeOrBusiness", coupon.getPlaceOrBusiness());
        params.put("businessId", coupon.getBusinessId());

        if (coupon.getImageBase64() != null) {
            params.put("imageFileName", coupon.getImageFileName());
            params.put("imageBase64", coupon.getImageBase64());
        }

        Call<CreateResponse> call = apiService.createBusinessCoupon(params);
        call.enqueue(new Callback<CreateResponse>() {
            @Override
            public void onResponse(Call<CreateResponse> call, Response<CreateResponse> response) {
                try {
                    switch (response.code()) {
                        case 401:
                            callBack.onError(response.message(), response.code());
                            break;

                        case 201:
                            CreateResponse resp = response.body();
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
            public void onFailure(Call<CreateResponse> call, Throwable t) {
                callBack.onError(t.toString(), 500);
            }
        });
    }

    public void Load(ApiInterface apiService) {

        Call<List<Coupon>> call = apiService.getCoupons();
        call.enqueue(new Callback<List<Coupon>>() {
            @Override
            public void onResponse(Call<List<Coupon>> call, Response<List<Coupon>> response) {
                try {
                    switch (response.code()) {
                        case 401:
                            callBackList.onError(response.message(), response.code());
                            break;

                        case 200:
                            List<Coupon> events = response.body();
                            callBackList.onResponseLoadCallBack((ArrayList<Coupon>) events);
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
            public void onFailure(Call<List<Coupon>> call, Throwable t) {
                callBackList.onError(t.toString(), 500);
            }
        });
    }

    public void LoadById(ApiInterface apiService, String id) {

        Call<Coupon> call = apiService.getCouponDetail(id);
        call.enqueue(new Callback<Coupon>() {
            @Override
            public void onResponse(Call<Coupon> call, Response<Coupon> response) {
                try {
                    switch (response.code()) {
                        case 401:
                            callBack.onError(response.message(), response.code());
                            break;

                        case 200:
                            Coupon event = response.body();
                            callBack.onResponseObjectCallBack(event);
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
            public void onFailure(Call<Coupon> call, Throwable t) {
                callBack.onError(t.toString(), 500);
            }
        });
    }
}
