package com.example.esp.view;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.esp.R;

public class StateView extends FrameLayout {

    private View loadingView;
    private View messageContainer;
    private ImageView messageImage;
    private TextView messageText;
    private TextView messageButton;

    public StateView(Context context) {
        super(context);
        init(context);
    }

    public StateView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.view_state, this);
        loadingView = findViewById(R.id.loading);
        messageContainer = findViewById(R.id.message_container);
        messageImage = findViewById(R.id.message_image);
        messageText = findViewById(R.id.message_text);
        messageButton= findViewById(R.id.message_button);
    }

    public void showLoading() {
        setVisibility(View.VISIBLE);
        loadingView.setVisibility(View.VISIBLE);
        messageContainer.setVisibility(View.GONE);
    }

    public void showMessage(int image, String text, String buttonText, Runnable buttonAction) {
        setVisibility(View.VISIBLE);
        loadingView.setVisibility(View.GONE);
        messageContainer.setVisibility(View.VISIBLE);
        if (image <= 0) {
            messageImage.setVisibility(View.GONE);
        } else {
            messageImage.setVisibility(View.VISIBLE);
            messageImage.setImageResource(image);
        }
        if (TextUtils.isEmpty(text)) {
            messageText.setVisibility(View.GONE);
        } else {
            messageText.setVisibility(View.VISIBLE);
            messageText.setText(text);
        }
        if (TextUtils.isEmpty(buttonText)) {
            messageButton.setVisibility(View.GONE);
        } else {
            messageButton.setVisibility(View.VISIBLE);
            messageButton.setText(buttonText);
            if (buttonAction != null) {
                messageButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        buttonAction.run();
                    }
                });
            }
        }
    }

    public void showError(String buttonText, Runnable buttonAction) {
        showMessage(R.drawable.error, "加载出错", buttonText, buttonAction);
    }

    public void showEmpty(String buttonText, Runnable buttonAction) {
        showMessage(R.drawable.empty, "暂无数据", buttonText, buttonAction);
    }

    public void hide() {
        setVisibility(View.GONE);
    }
}
