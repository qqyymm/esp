package com.example.esp.model;

import java.io.Serializable;

/**
 * 组合设备信息
 *
 * Created by Ryan Hu on 2020/3/25.
 */
public class UnitedDevice implements Serializable {

    /**
     * 设备类型
     */
    public String devType;

    /**
     * 为控制面板类型
     */
    public String bigType;

    /**
     * 红外控制码
     */
    public String infraTypeID;

    /**
     * 红外类型名称
     */
    public String infraName;

    /**
     * 红外设备状态
     */
    public String lastInst;

}
