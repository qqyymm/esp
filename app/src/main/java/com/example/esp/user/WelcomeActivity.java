package com.example.esp.user;

import android.os.Bundle;
import android.widget.TextView;

import com.example.esp.CurrentUser;
import com.example.esp.R;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

/**
 * 主页
 */
public class WelcomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        ((TextView)findViewById(R.id.welcome)).setText("欢迎，你的sessionId是" + CurrentUser.sessionId);
    }
}
