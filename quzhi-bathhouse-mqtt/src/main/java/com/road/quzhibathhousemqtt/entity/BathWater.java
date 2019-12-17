package com.road.quzhibathhousemqtt.entity;

import lombok.Data;

/**
 * @author wenqi
 * @Title: BathWater
 * @ProjectName quzhi-bathhouse-mqtt
 * @Description:
 * @date 2019/3/4上午9:21
 */
@Data
public class BathWater {

    private long waterId;

    private long projectId;

    private long deviceId;

    private String snCode;

    private long waterNo;

    private String waterName;

    private long areaId;

    private int status;

    private String updt;

    private int isBooking;

    private String lastorderstardt;

    private String lastorderstopdt;
    private String createtime;
    private String remark;

    private int isDelete;
}
