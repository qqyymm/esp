package com.example.esp.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Ryan Hu on 2020/3/28.
 */
public class InfraAbility implements Serializable {

    public int instCount;

    /**
     * 图标
     */
    public String img;

    public String text;

    public String timer;

    public List<String> tag;

    /**
     * 控制能力代码
     */
    public List<String> inst;

    /**
     * 说明
     */
    public String type;
}
