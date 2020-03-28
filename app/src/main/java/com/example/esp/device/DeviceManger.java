package com.example.esp.device;

import com.example.esp.model.Device;
import com.example.esp.model.DeviceDetail;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * 本地模拟的服务端设备列表数据
 *
 * Created by Ryan Hu on 2020/3/28.
 */
public class DeviceManger {

    private static DeviceManger instance;

    private ArrayList<DeviceDetail> deviceList = new ArrayList<>();

    public static DeviceManger getInstance() {
        if (instance == null) {
            instance = new DeviceManger();
        }
        return instance;
    }

    private DeviceManger(){
    }

    /**
     * 添加一个随机设备
     */
    public void addRandomDevice() {
        DeviceDetail deviceDetail = new DeviceDetail();
        deviceDetail.devID = Integer.toString((int)(Math.random() * 10000));
        deviceDetail.devName = "海尔";
        deviceDetail.devType = "空调";
        deviceDetail.devStatus = "A002";
        deviceDetail.token = "";
        deviceDetail.seed = "";
        deviceDetail.lastInst = "自动24度低风自动摆风";
        addDevice(deviceDetail);
    }

    public void addDevice(DeviceDetail deviceDetail) {
        deviceList.add(deviceDetail);
    }

    public boolean removeDevice(String deviceId) {
        Iterator<DeviceDetail> it = deviceList.iterator();
        while (it.hasNext()) {
            if (deviceId.equals(it.next().devID)) {
                it.remove();
                return true;
            }
        }
        return false;
    }

    public List<Device> getAllDevice() {
        List<Device> result = new ArrayList<Device>();
        result.addAll(deviceList);
        return result;
    }
}
