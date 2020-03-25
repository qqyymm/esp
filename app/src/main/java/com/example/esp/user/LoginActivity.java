package com.example.esp.user;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.esp.CurrentUser;
import com.example.esp.R;
import com.example.esp.api.Api;
import com.example.esp.api.UserService;
import com.example.esp.api.param.LoginParam;
import com.example.esp.api.result.LoginResult;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        findViewById(R.id.btn_login).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        String username = ((TextView)findViewById(R.id.username)).getText().toString();
        String password = ((TextView)findViewById(R.id.password)).getText().toString();
        login(username, password);
    }

    private void login(String username, String password) {
        //构造请求参数
        LoginParam param = new LoginParam();
        param.userName = username;
        param.passWord = password;
        param.appid = "ZYKJ";
        //发起请求
        Api.create(UserService.class).login(param).enqueue(new Callback<LoginResult>() {
            @Override
            public void onResponse(Call<LoginResult> call, Response<LoginResult> response) {
                LoginResult result = response.body();
                if (result != null && result.success()) {
                    //保存服务器返回的session
                    CurrentUser.sessionId = result.sessionID;
                    startActivity(new Intent(LoginActivity.this, WelcomeActivity.class));
                } else {
                    Toast.makeText(LoginActivity.this, "登录失败:" + (result != null ? result.errmsg : ""), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<LoginResult> call, Throwable t) {
                //请求失败
                Toast.makeText(LoginActivity.this, "登录失败:" + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
