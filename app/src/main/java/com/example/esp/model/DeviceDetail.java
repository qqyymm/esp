package com.example.esp.model;

import java.io.Serializable;
import java.util.Map;

/**
 * 设备详细信息
 *
 * Created by Ryan Hu on 2020/3/28.
 */
public class DeviceDetail extends Device implements Serializable {

    /**
     * 今日耗电量
     */
    public String socketOut_W;

    /**
     * 昨日耗电量
     */
    public String socketOutY_W;

    /**
     * 最近一条实时电量
     */
    public String socketOut_P;

    /**
     * 最近一次电量的更新时间
     */
    public String socketOut_upTime;

    /**
     * 多路继电器的状态，字典结构 ，{"1":"0","2":"0"}
     * key表示继电器路数
     * value 表示继电器状态: 0 为关闭，1 为接 通 。
     * 对于单路继电器的，返回值可能会是{}
     */
    public Map<String, String> subRlystatus;
}
