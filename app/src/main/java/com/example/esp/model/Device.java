package com.example.esp.model;

import java.io.Serializable;
import java.util.List;

/**
 * 设备信息
 *
 * Created by Ryan Hu on 2020/3/25.
 */
public class Device implements Serializable {

    /**
     * 设备名
     */
    public String devName;

    /**
     * 设备ID
     */
    public String devID;

    /**
     * 联网状态，有下列可选项：
     * "A002":"
     * 离线失联
     * "A003":"
     * 在线待机
     * "A004":"
     * 在线负载平稳
     * "A005":"
     * 在线负载波动
     */
    public String devStatus;

    /**
     * 设备类型，目前支持的设备类型有‘空调、插座、台灯、风扇、热水器等
     */
    public String devType;

    /**
     * 设备控制凭证 。凭证是包含了用户名、设备序列值
     * 得加密串，用于区别每个设备并进行合法控制的关
     * 键。非法的凭证无法通过网关校验。
     */
    public String token;

    /**
     * 捆绑时使用的索引值
     */
    public String seed;

    /**
     * 目前设备状态，类似 "自动24度低风自动摆风" 等描述性语句
     */
    public String lastInst;

    /**
     * 组合设备列表
     */
    public List<UnitedDevice> unitedevice;
}
