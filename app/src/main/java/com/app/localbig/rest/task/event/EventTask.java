package com.app.localbig.rest.task.event;

import com.app.localbig.helper.log.LogManager;
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

public class EventTask {

    public ResponseLoadCallBack<LocalEvent> callBackList;
    public ResponseObjectCallBack<Object> callBack;

    public EventTask(ResponseLoadCallBack callBackList) {
        this.callBackList = callBackList;
    }

    public EventTask(ResponseObjectCallBack callBack) {
        this.callBack = callBack;
    }

    public void CallService(int operation, String id, LocalEvent event) {

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
                Create(event);
                break;
            case 4:
                break;
            case 5:
                break;
        }
    }

    private void Create(LocalEvent event) {
        ApiInterface apiService =
                ServiceGenerator.createService(ApiInterface.class);

        Map<String, String> params = new HashMap<>();

        params.put("title", event.getTitle());
        params.put("description", event.getDescription());
        params.put("startDate", event.getStartDate());
        params.put("endDate", event.getEndDate());
        params.put("freeEntrance", String.valueOf(event.getFreeEntrance()));
        params.put("formattedAddress", event.getFormattedAddress());
        params.put("latitude", event.getLatitude());
        params.put("longitude", event.getLongitude());
        params.put("applicationUserId", event.getApplicationUserId());

        if (event.getImageBase64() != null) {
            params.put("imageFileName", event.getImageFileName());
            params.put("imageBase64", event.getImageBase64());
        }

        Call<CreateResponse> call = apiService.createEvent(params);
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

        Call<List<LocalEvent>> call = apiService.getLocalEvents();
        call.enqueue(new Callback<List<LocalEvent>>() {
            @Override
            public void onResponse(Call<List<LocalEvent>> call, Response<List<LocalEvent>> response) {
                try {
                    switch (response.code()) {
                        case 401:
                            callBackList.onError(response.message(), response.code());
                            break;

                        case 200:
                            List<LocalEvent> events = response.body();
                            callBackList.onResponseLoadCallBack((ArrayList<LocalEvent>) events);
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
            public void onFailure(Call<List<LocalEvent>> call, Throwable t) {
                callBackList.onError(t.toString(), 500);
            }
        });
    }

    public void LoadById(ApiInterface apiService, String id) {

        Call<LocalEvent> call = apiService.getLocalEventDetail(id);
        call.enqueue(new Callback<LocalEvent>() {
            @Override
            public void onResponse(Call<LocalEvent> call, Response<LocalEvent> response) {
                try {
                    switch (response.code()) {
                        case 401:
                            callBack.onError(response.message(), response.code());
                            break;

                        case 200:
                            LocalEvent event = response.body();
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
            public void onFailure(Call<LocalEvent> call, Throwable t) {
                callBack.onError(t.toString(), 500);
            }
        });
    }
}
