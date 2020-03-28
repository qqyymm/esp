package com.example.esp.device;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.esp.R;

/**
 * Created by Ryan Hu on 2020/3/28.
 */
public class AddDeviceActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int REQUEST_PERMISSION = 1;

    private boolean receiverRegistered;

    private View headerBack;
    private TextView wifiSsidView;
    private EditText wifiPasswordView;
    private View confirmButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_device);
        initViews();
        checkPermissionAndRegister();
    }

    private void initViews() {
        headerBack = findViewById(R.id.header_back);
        headerBack.setOnClickListener(this);
        wifiSsidView = findViewById(R.id.wifi_ssid);
        wifiPasswordView = findViewById(R.id.wifi_password);
        confirmButton = findViewById(R.id.btn_confirm);
        confirmButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.header_back:
                finish();
                break;
            case R.id.btn_confirm:
                //添加一个设备
                //TODO 这里需要通过smart config来绑定设备,等联调
                DeviceManger.getInstance().addRandomDevice();
                Toast.makeText(this, "添加设备成功", Toast.LENGTH_SHORT).show();
                finish();
                break;
            default:
                break;
        }
    }

    private void checkPermissionAndRegister() {
        if (Build.VERSION.SDK_INT >= 28) {
            if (checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION};
                requestPermissions(permissions, REQUEST_PERMISSION);
            } else {
                registerBroadcastReceiver();
            }
        } else {
            registerBroadcastReceiver();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_PERMISSION) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (!isDestroyed()) {
                    registerBroadcastReceiver();
                }
            }
            return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void registerBroadcastReceiver() {
        IntentFilter filter = new IntentFilter(WifiManager.NETWORK_STATE_CHANGED_ACTION);
        if (Build.VERSION.SDK_INT >= 28) {
            filter.addAction(LocationManager.PROVIDERS_CHANGED_ACTION);
        }
        registerReceiver(wifiReceiver, filter);
        receiverRegistered = true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (receiverRegistered) {
            unregisterReceiver(wifiReceiver);
        }
    }

    /**
     * Wifi状态变化后更新显示信息
     *
     * @param wifiInfo
     */
    private void onWifiChanged(WifiInfo wifiInfo) {

        //wifi是否断开
        boolean disconnected = wifiInfo == null
                || wifiInfo.getNetworkId() == -1
                || "<unknown ssid>".equals(wifiInfo.getSSID());


        if (disconnected) { //显示无wifi
            wifiSsidView.setText("请先连接WiFi");
            wifiPasswordView.setEnabled(false);
            confirmButton.setEnabled(false);
        } else {    //显示wifi信息
            String ssid = wifiInfo.getSSID();
            if (ssid.startsWith("\"") && ssid.endsWith("\"")) {
                ssid = ssid.substring(1, ssid.length() - 1);
            }
            wifiSsidView.setText(ssid);
            wifiPasswordView.setEnabled(true);
            confirmButton.setEnabled(true);
        }
    }

    /**
     *  监听系统网络状态变化广播的Receiver
     */
    private BroadcastReceiver wifiReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action == null) {
                return;
            }

            WifiManager wifiManager = (WifiManager) context.getApplicationContext()
                    .getSystemService(WIFI_SERVICE);
            assert wifiManager != null;

            switch (action) {
                case WifiManager.NETWORK_STATE_CHANGED_ACTION:
                case LocationManager.PROVIDERS_CHANGED_ACTION:
                    onWifiChanged(wifiManager.getConnectionInfo());
                    break;
            }
        }
    };
}
