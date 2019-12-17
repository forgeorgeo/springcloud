package com.road.quzhibathhousemqtt.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @author wenqi
 * @Title: RemoteCloseOrderDTO
 * @ProjectName quzhi-bathhouse-mqtt
 * @Description:
 * @date 2019/2/22下午2:06
 */
@Data
public class RemoteCloseOrderDTO implements Serializable {

    private static final long serialVersionUID = -4116825472369802912L;
    private String Key;
    private String Addr;
    private String ProID;
    private String timeIds;
    private String accountId;
    private String accountType;
    private String ReservationCode;
    private String cmdId;


    public RemoteCloseOrderDTO() {
    }

    public RemoteCloseOrderDTO(String key, String addr, String proID, String timeIds, String accountId, String accountType,  String cmdId) {
        this.Key = key;
        this.Addr = addr;
        this.ProID = proID;
        this.timeIds = timeIds;
        this.accountId = accountId;
        this.accountType = accountType;
        this.cmdId = cmdId;
    }
}
