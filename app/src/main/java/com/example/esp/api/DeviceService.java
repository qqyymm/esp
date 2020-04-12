package com.example.esp.api;

import com.example.esp.api.param.DeviceBindParam;
import com.example.esp.api.param.DeviceListParam;
import com.example.esp.api.param.DeviceRemoveParam;
import com.example.esp.api.param.QueryDeviceParam;
import com.example.esp.api.param.DevicerenewtokenParam;
import com.example.esp.api.param.QueryInfraTypeAbilityParam;
import com.example.esp.api.result.ApiResult;
import com.example.esp.api.result.DeviceListResult;
import com.example.esp.api.result.DevicecontrolResult;
import com.example.esp.api.result.QueryDeviceResult;
import com.example.esp.api.result.DevicerenewtokenResult;
import com.example.esp.api.result.QueryInfraTypeAbilityResult;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by Ryan Hu on 2020/3/25.
 */
public interface DeviceService {

    /**
     * 设备绑定
     */
    @POST("devicebind")
    Call<ApiResult> bindDevice(@Body DeviceBindParam param);

    /**
     * 设备解绑
     */
    @POST("devicebind")
    Call<ApiResult> removeDevice(@Body DeviceRemoveParam param);

    /**
     * 设备列表
     */
    @POST("devicelist")
    Call<DeviceListResult> getDeviceList(@Body DeviceListParam param);

    /**
     * 状态单个查询
     */
    @POST("deviceqry")
    Call<QueryDeviceResult> queryDevice(@Body QueryDeviceParam param);

    /**
     * 凭证更新
     */
    @POST("devicerenewtoken")
    Call<DevicerenewtokenResult> devicerenewtoken(@Body DevicerenewtokenParam param);

    /**
     * 设备控制
     *
     * @param actionId 操作类别，0：继电器关闭，2：继电器打开，6：红外控制
     * @param inst 指令
     * @param token 授权设备码
     * @param infraTypeId 设备红外ID
     */
    @GET("action")
    Call<DevicecontrolResult> deviceControl(
            @Query("actionID") String actionId,
            @Query("inst") String inst,
            @Query("token") String token,
            @Query("infraTypeID") String infraTypeId);

    /**
     * 红外型号支持能力查询
     */
    @POST("infratypeability")
    Call<QueryInfraTypeAbilityResult> queryInfraTypeAbility(@Body QueryInfraTypeAbilityParam param);
}
