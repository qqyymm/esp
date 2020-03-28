package com.example.esp.device;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.esp.R;
import com.example.esp.model.Device;
import com.example.esp.model.DeviceDetail;

/**
 * Created by Ryan Hu on 2020/3/28.
 */
public class DeviceDetailActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String EXTRA_DEVICE = "device";

    private Device device;
    private DeviceDetail deviceDetail;

    private TextView deviceName;
    private TextView deviceType;
    private TextView deviceStatus;
    private TextView deviceInst;

    public static void start(Activity activity, Device device) {
        Intent intent = new Intent(activity, DeviceDetailActivity.class);
        intent.putExtra(EXTRA_DEVICE, device);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        device = (Device)getIntent().getSerializableExtra(EXTRA_DEVICE);
        if (device == null) {
            finish();
        }

        setContentView(R.layout.activity_device_detail);
        initViews();
    }

    private void initViews() {
        findViewById(R.id.header_back).setOnClickListener(this);
        deviceName = findViewById(R.id.device_name);
        deviceType = findViewById(R.id.device_type);
        deviceStatus = findViewById(R.id.device_status);
        deviceInst = findViewById(R.id.last_inst);

        deviceName.setText(device.devName);
        deviceType.setText(device.devType);
        deviceStatus.setText(device.devStatus);
        deviceInst.setText(device.lastInst);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.header_back:
                finish();
                break;
        }
    }

    private void loadDeviceInfo() {
        //TODO
    }
}
