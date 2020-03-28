package com.example.esp.api;

import com.example.esp.api.param.ChangePasswordParam;
import com.example.esp.api.param.LoginParam;
import com.example.esp.api.param.RegisterParam;
import com.example.esp.api.result.ApiResult;
import com.example.esp.api.result.LoginResult;
import com.example.esp.api.result.RegisterResult;

import retrofit2.Call;
import retrofit2.http.Body;
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
    Call<ApiResult> changepassword(@Body ChangePasswordParam param);

}
