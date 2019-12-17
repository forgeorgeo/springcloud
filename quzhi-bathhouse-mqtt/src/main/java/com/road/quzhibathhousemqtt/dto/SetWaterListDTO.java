package com.road.quzhibathhousemqtt.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @author wenqi
 * @Title: SetWaterListDTO
 * @ProjectName quzhi-bathhouse-mqtt
 * @Description:
 * @date 2019/2/22下午1:56
 */
@Data
public class SetWaterListDTO implements Serializable {

    private static final long serialVersionUID = -7954679532691152103L;
    String Key;
    String Addr;
    String ProID;
    String TotalDevCount;
    String SetMode;
    String DevAddrList;
    String cmdId;


    public SetWaterListDTO() {


    }

    public SetWaterListDTO(String key, String addr, String proID, String totalDevCount, String setMode, String devAddrList, String cmdId) {
        Key = key;
        Addr = addr;
        ProID = proID;
        TotalDevCount = totalDevCount;
        SetMode = setMode;
        DevAddrList = devAddrList;
        cmdId = cmdId;
    }
}
