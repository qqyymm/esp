package com.example.esp.user;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.esp.R;
import com.example.esp.api.Api;
import com.example.esp.api.UserService;
import com.example.esp.api.param.RegisterParam;
import com.example.esp.api.result.RegisterResult;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 用户注册页面
 */
public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        findViewById(R.id.btn_register).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        String username = ((TextView)findViewById(R.id.username)).getText().toString();
        String password = ((TextView)findViewById(R.id.password)).getText().toString();
        register(username, password);
    }

    /**
     * 注册用户
     */
    private void register(String username, String password) {
        //构造请求参数
        RegisterParam param = new RegisterParam();
        param.userName = username;
        param.passWord = password;
        param.appid = "ZYKJ";
        //发起请求
        Api.create(UserService.class).register(param).enqueue(new Callback<RegisterResult>() {
            @Override
            public void onResponse(Call<RegisterResult> call, Response<RegisterResult> response) {
                //请求成功
                Toast.makeText(RegisterActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
                //跳到登录界面
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                finish();
            }

            @Override
            public void onFailure(Call<RegisterResult> call, Throwable t) {
                //请求失败
                Toast.makeText(RegisterActivity.this, "注册失败", Toast.LENGTH_SHORT).show();
                Log.e("test",t.getMessage());
            }
        });
    }
}
