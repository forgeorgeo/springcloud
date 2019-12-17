package com.road.quzhibathhousemqtt.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @author wenqi
 * @Title: DeviceRegisterDTO
 * @ProjectName quzhi-bathhouse-mqtt
 * @Description:
 * @date 2019/2/22上午10:47
 */
@Data
public class DeviceRegisterDTO  implements Serializable {

    private static final long serialVersionUID = -7325568264682987449L;
    /**
     * 设备通讯 key
     */
    private String Key;
    /**
     * 设备地址
     */
    private String Addr;
    /**
     * 设备ID
     */
    private String DevID;
    /**
     * 项目ID
     */
    private String ProID;
    /**
     * 水表数
     */
    private String WaterCnt;
    /**
     * 起始编号
     */
    private String StartCnt;
    /**
     * 终止编号
     */
    private String EndCnt;
    /**
     * 使用模式 计时0x00  计量0x11
     */
    private String UsrMode;
    /**
     * 费率
     */
    private String Rate;
    /**
     * 计费单位
     */
    private String Unit;
    /**
     * 无流量超时
     */
    private String TimeOut;
    /**
     * 最大消费金额
     */
    private String MaxConsume;
    /**
     * 低多少条命令
     */
    private String cmdId;

    public DeviceRegisterDTO() {

    }

    public DeviceRegisterDTO(String key, String addr, String devID, String proID, String waterCnt, String startCnt, String endCnt, String usrMode, String rate, String unit, String timeOut, String maxConsume, String cmdId) {
        Key = key;
        Addr = addr;
        DevID = devID;
        ProID = proID;
        WaterCnt = waterCnt;
        StartCnt = startCnt;
        EndCnt = endCnt;
        UsrMode = usrMode;
        Rate = rate;
        Unit = unit;
        TimeOut = timeOut;
        MaxConsume = maxConsume;
        cmdId = cmdId;
    }
}
