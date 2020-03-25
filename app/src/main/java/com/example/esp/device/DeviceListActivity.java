package com.example.esp.device;

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
public class DeviceListActivity extends AppCompatActivity implements View.OnClickListener {

    private View loading;
    private SmartRefreshLayout refreshLayout;
    private RecyclerView recyclerView;
    private View headerBack;
    private View listEmpty;
    private View listError;
    private View retry;

    private DeviceListAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_list);
        loading = findViewById(R.id.loading);
        refreshLayout = findViewById(R.id.refresh);
        recyclerView = findViewById(R.id.device_list);
        headerBack = findViewById(R.id.header_back);
        listEmpty = findViewById(R.id.list_empty);
        listError = findViewById(R.id.list_error);
        retry = findViewById(R.id.retry);

        headerBack.setOnClickListener(this);
        retry.setOnClickListener(this);

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
            case R.id.retry:
                loadDeviceList();
                break;
            default:
                break;
        }
    }

    /**
     * 加载设备列表
     */
    private void loadDeviceList() {
        if (true) {
            loadMock();
            return;
        }
        DeviceListParam param = new DeviceListParam();
        param.sessionID = CurrentUser.sessionId;
        Api.create(DeviceService.class).getDeviceList(param).enqueue(new Callback<DeviceListResult>() {
            @Override
            public void onResponse(Call<DeviceListResult> call, Response<DeviceListResult> response) {
                DeviceListResult result = response.body();
                if (result != null && result.success()) {
                    if (result.device != null && result.device.size() > 0) {
                        showList(result.device);
                    } else {
                        showEmpty();
                    }
                } else {
                    showError();
                }
                refreshLayout.finishRefresh(true);
            }

            @Override
            public void onFailure(Call<DeviceListResult> call, Throwable t) {
                showError();
                refreshLayout.finishRefresh(false);
            }
        });
        showLoading();
    }

    private void loadMock() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                showError();
            }
        }, 3000);
        showLoading();
    }

    private void showLoading() {
        loading.setVisibility(View.VISIBLE);
        refreshLayout.setVisibility(View.GONE);
        listEmpty.setVisibility(View.GONE);
        listError.setVisibility(View.GONE);
    }

    private void showEmpty() {
        loading.setVisibility(View.GONE);
        refreshLayout.setVisibility(View.GONE);
        listEmpty.setVisibility(View.VISIBLE);
        listError.setVisibility(View.GONE);
    }

    private void showError() {
        loading.setVisibility(View.GONE);
        refreshLayout.setVisibility(View.GONE);
        listEmpty.setVisibility(View.GONE);
        listError.setVisibility(View.VISIBLE);
    }

    private void showList(List<Device> deviceList) {
        loading.setVisibility(View.GONE);
        refreshLayout.setVisibility(View.VISIBLE);
        listEmpty.setVisibility(View.GONE);
        listError.setVisibility(View.GONE);
        bind(deviceList);
    }

    private void bind(List<Device> deviceList) {
        if (adapter == null) {
            adapter = new DeviceListAdapter();
            recyclerView.setAdapter(adapter);
        }
        adapter.update(deviceList);
    }
}
