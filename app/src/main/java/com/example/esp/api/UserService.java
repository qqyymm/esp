package com.example.esp.api;

import com.example.esp.api.param.ChangepasswordParam;
import com.example.esp.api.param.DevicebindParam;
import com.example.esp.api.param.DevicecontrolParam;
import com.example.esp.api.param.DeviceListParam;
import com.example.esp.api.param.DeviceqryParam;
import com.example.esp.api.param.DeviceremoveParam;
import com.example.esp.api.param.DevicerenewtokenParam;
import com.example.esp.api.param.LoginParam;
import com.example.esp.api.param.RegisterParam;
import com.example.esp.api.result.ApiResult;
import com.example.esp.api.result.DevicecontrolResult;
import com.example.esp.api.result.DeviceListResult;
import com.example.esp.api.result.DeviceqryResult;
import com.example.esp.api.result.DevicerenewtokenResult;
import com.example.esp.api.result.LoginResult;
import com.example.esp.api.result.RegisterResult;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

/**
 * 用户相关api
 */
public interface UserService {

    /**
     * 用户注册
     */
    @POST("regist")
    Call<RegisterResult> register(@Body RegisterParam param);

    /**
     * 用户登录
     */
    @POST("login")
    Call<LoginResult> login(@Body LoginParam param);

    /**
     * 修改密码
     */
    @POST("changepassword")
    Call<ApiResult> changepassword(@Body ChangepasswordParam param);

    /**
     * 设备绑定
     */
    @POST("devicebind")
    Call<ApiResult> devicebind(@Body DevicebindParam param);

    /**
     * 设备删除
     */
    @POST("deviceremove")
    Call<ApiResult> deviceremove(@Body DeviceremoveParam param);

    /**
     * 状态清单查询
     */
    @POST("devicelist")
    Call<DeviceListResult> devicelist(@Body DeviceListParam param);

    /**
     * 状态单个查询
     */
    @POST("deviceqry")
    Call<DeviceqryResult> deviceqry(@Body DeviceqryParam param);

    /**
     * 凭证更新
     */
    @POST("devicerenewtoken")
    Call<DevicerenewtokenResult> devicerenewtoken(@Body DevicerenewtokenParam param);

    /**
     * 设备控制
     */
    @GET("action?actionID=actionID&inst=inst&token=token")
    Call<DevicecontrolResult> devicecontrol(@Body DevicecontrolParam param);
}
