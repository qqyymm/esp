package com.example.esp.device;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.esp.CurrentUser;
import com.example.esp.R;
import com.example.esp.api.Api;
import com.example.esp.api.DeviceService;
import com.example.esp.api.param.DeviceListParam;
import com.example.esp.api.result.DeviceListResult;
import com.example.esp.model.Device;
import com.example.esp.view.StateView;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Ryan Hu on 2020/3/25.
 */
public class DeviceListActivity extends AppCompatActivity implements View.OnClickListener, DeviceListAdapter.Listener {

    private static final int REQUEST_CODE_ADD_DEVICE = 1;

    private SmartRefreshLayout refreshLayout;
    private RecyclerView recyclerView;
    private View headerBack;
    private View headerAddDevice;
    private StateView stateView;

    private DeviceListAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_list);
        refreshLayout = findViewById(R.id.refresh);
        recyclerView = findViewById(R.id.device_list);
        headerBack = findViewById(R.id.header_back);
        headerAddDevice = findViewById(R.id.header_add);
        stateView = findViewById(R.id.state);

        headerBack.setOnClickListener(this);
        headerAddDevice.setOnClickListener(this);

        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                loadDeviceList();
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        loadDeviceList();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.header_back:
                finish();
                break;
            case R.id.header_add:
                startActivityForResult(new Intent(this, AddDeviceActivity.class), REQUEST_CODE_ADD_DEVICE);
                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_CODE_ADD_DEVICE:
                loadDeviceList();
                break;
        }
    }

    /**
     * 加载设备列表
     */
    private void loadDeviceList() {
//        if (true) {
//            loadMock();
//            return;
//        }
        DeviceListParam param = new DeviceListParam();
        param.sessionID = CurrentUser.sessionId;
        Api.create(DeviceService.class).getDeviceList(param).enqueue(new Callback<DeviceListResult>() {
            @Override
            public void onResponse(Call<DeviceListResult> call, Response<DeviceListResult> response) {
                DeviceListResult result = response.body();
                if (result != null && result.success()) {
                    if (result.device != null && result.device.size() > 0) {
                        stateView.hide();
                        bind(result.device);
                    } else {
                        stateView.showEmpty("重试", () -> loadDeviceList());
                    }
                } else {
                    stateView.showError("重试", () -> loadDeviceList());
                }
                refreshLayout.finishRefresh(true);
            }

            @Override
            public void onFailure(Call<DeviceListResult> call, Throwable t) {
                stateView.showError("重试", () -> loadDeviceList());
                refreshLayout.finishRefresh(false);
            }
        });

        showLoading();
    }

    /**
     * 模拟设备列表请求
     */
    private void loadMock() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                List<Device> devices = DeviceManger.getInstance().getAllDevice();
                if (devices == null || devices.size() == 0) {
                    stateView.showEmpty("未找到设备", () -> loadDeviceList());
                } else {
                    stateView.hide();
                    bind(devices);
                }
                refreshLayout.finishRefresh(true);
            }
        }, 3000);

        showLoading();
    }

    private void showLoading() {
        if (adapter == null || adapter.getItemCount()== 0) {
            stateView.showLoading();
        } else {
            stateView.hide();
            refreshLayout.autoRefreshAnimationOnly();
        }
    }

    private void bind(List<Device> deviceList) {
        if (adapter == null) {
            adapter = new DeviceListAdapter();
            adapter.setListener(this);
            recyclerView.setAdapter(adapter);
        }
        adapter.update(deviceList);
    }

    @Override
    public void onClickDevice(Device device) {
        DeviceDetailActivity.start(this, device);
    }

    @Override
    public void onDeleteDevice(Device device) {
        //TODO 请求服务器，删除设备
        boolean deleted = DeviceManger.getInstance().removeDevice(device.devID);
        if (deleted) {
            if (adapter != null) {
                adapter.removeDevice(device.devID);
            }
        }
    }
}
