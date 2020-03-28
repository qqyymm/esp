package com.example.esp.api.result;

import com.example.esp.model.InfraAbility;

import java.util.List;
import java.util.Map;

/**
 * Created by Ryan Hu on 2020/3/28.
 */
public class QueryInfraTypeAbilityResult extends ApiResult {

    public String _id;

    /**
     * 面板类型，
     * ACST 空调；
     * GENE 非空调
     */
    public String panelType;

    //********************* 以下为非空调能力 *********************

    /**
     * 按钮总数
     */
    public int keyCount;

    /**
     * 按钮列数
     */
    public int keyCol;

    /**
     * 按钮行数
     */
    public int keyRow;

    /**
     * 每行的按钮个数
     * 比如：[2,4,4,4]表示4行，第1行2个按钮，第2-4行各4个按钮
     */
    public List<Integer> keyRowNum;

    /**
     * 可用能力清单
     */
    public Map<String, InfraAbility> keysSet;

    /**
     * 图标的路径
     */
    public String dirURL;

    //********************* 以下为空调能力 *********************

    /**
     * 空调支持的温度调节范围，[16,30]表示16-30度
     */
    public String temperature;

    /**
     * 风向选项，共有 6 个选项，每个选项代表的含义和具体空调相关。
     * "YYNNNN": 表示只支持前两个 对应的值是 0,1,2,3,4,5)
     */
    public String fanDirection;

    /**
     * 风速选项，共有4个选项，每个选项代表的含义和具体空调相关。
     * "YYYY": 表示支持 4 个选项
     * 对应的值是0,1,2,3, 含义是自动 低风 中风 高风
     */
    public String fanSpeed;

    /**
     * YN=支持单独的开和关，
     * YY== 开关是同一个指令
     */
//    public String switch;

    /**
     * YY=支持自动模式
     */
    public String AUTO;

    /**
     * YY=支持制冷模式
     */
    public String COLD;

    /**
     * YY=支持制热模式
     */
    public String HEAT;

    /**
     * YY=支持吹风模式
     */
    public String FAN;

    /**
     * YY=支持抽湿模式
     */
    public String WATER;
}
