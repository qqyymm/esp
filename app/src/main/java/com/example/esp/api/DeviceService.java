package com.example.esp.api;

import com.example.esp.api.param.DeviceListParam;
import com.example.esp.api.result.DeviceListResult;

import retrofit2.Call;
import retrofit2.http.POST;

/**
 * Created by Ryan Hu on 2020/3/25.
 */
public interface DeviceService {

    @POST("devicelist")
    Call<DeviceListResult> getDeviceList(DeviceListParam param);
}
