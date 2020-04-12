package com.example.esp.device;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import com.example.esp.CurrentUser;
import com.example.esp.R;
import com.example.esp.api.Api;
import com.example.esp.api.DeviceService;
import com.example.esp.api.param.QueryDeviceParam;
import com.example.esp.api.result.QueryDeviceResult;
import com.example.esp.model.Device;
import com.example.esp.model.DeviceDetail;
import com.example.esp.view.StateView;

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
    private StateView stateView;
    private ViewGroup controlContainer;

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

        loadDeviceInfo();
    }

    private void initViews() {
        findViewById(R.id.header_back).setOnClickListener(this);
        deviceName = findViewById(R.id.device_name);
        deviceType = findViewById(R.id.device_type);
        deviceStatus = findViewById(R.id.device_status);
        deviceInst = findViewById(R.id.last_inst);
        stateView = findViewById(R.id.state);
        controlContainer = findViewById(R.id.control_container);
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
        stateView.showLoading();
        QueryDeviceParam param = new QueryDeviceParam();
        param.sessionID = CurrentUser.sessionId;
        param.devID = device.devID;
        Api.create(DeviceService.class).queryDevice(param).enqueue(new Callback<QueryDeviceResult>() {
            @Override
            public void onResponse(Call<QueryDeviceResult> call, Response<QueryDeviceResult> response) {
                if (response.body() != null && response.body().success()) {
                    stateView.hide();
                    updateDeviceDetail(response.body());
                } else {
                    stateView.showError("重试", () -> loadDeviceInfo());
                }
            }

            @Override
            public void onFailure(Call<QueryDeviceResult> call, Throwable t) {
                stateView.showError("重试", () -> loadDeviceInfo());
            }
        });
    }

    private void updateDeviceDetail(QueryDeviceResult deviceDetail) {
        deviceName.setText(deviceDetail.devName);
        deviceType.setText(deviceDetail.devType);
        deviceStatus.setText(deviceDetail.devStatus);
        deviceInst.setText(deviceDetail.lastInst);
        //如果设备信息里至少有一个组合设备，显示其控制面板
        if (deviceDetail.unitedevice != null && deviceDetail.unitedevice.size() > 0) {
            DeviceControlFragment fragment = DeviceControlFragment.create(deviceDetail.unitedevice.get(0), deviceDetail.token);
            getSupportFragmentManager().beginTransaction().replace(R.id.control_container, fragment).commitAllowingStateLoss();
        }
    }
}
