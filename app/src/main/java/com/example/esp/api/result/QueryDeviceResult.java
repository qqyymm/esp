package com.example.esp.api.result;

import com.example.esp.model.DeviceDetail;
import com.example.esp.model.UnitedDevice;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class QueryDeviceResult extends ApiResult {

    public String devID;

    public String devStatus;

    public String devName;

    public String devType;

    public String lastInst;

    public String socketOut_W;

    public String socketOutY_W;

    public String socketOut_P;

    public String socketOut_upTime;

    public String token;

    public String seed;

    public List<UnitedDevice> unitedevice;

    public Map subRlystatus;

    public DeviceDetail toDeviceDetail() {
        DeviceDetail result = new DeviceDetail();
        result.devID = devID;
        result.devType = devType;
        result.devName = devName;
        result.devStatus = devStatus;
        result.lastInst = lastInst;
        result.seed = seed;
        result.token = token;
        result.socketOut_W = socketOut_W;
        result.socketOutY_W = socketOutY_W;
        result.socketOut_P = socketOut_P;
        result.socketOut_upTime = socketOut_upTime;
        result.subRlystatus = subRlystatus;
        result.unitedevice = new ArrayList<>();
        result.unitedevice.addAll(unitedevice);
        return result;
    }
}
