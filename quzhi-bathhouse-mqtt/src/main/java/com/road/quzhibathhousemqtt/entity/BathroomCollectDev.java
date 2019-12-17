package com.road.quzhibathhousemqtt.entity;

import lombok.Data;

/**
 * @author wenqi
 * @title: BathroomCollectDev
 * @projectName quzhi-bathhouse-mqtt
 * @description: 澡堂集中器
 * @date 2019/4/10下午2:01
 */
@Data
public class BathroomCollectDev {

    /**
     * 集中器ID
     */
    private int id;

    /**
     * 集中器名称
     */
    private String collectName;

    /**
     * 澡堂ID
     */
    private int bathroomId;

    /**
     * 项目ID
     */
    private int projectId;

    /**
     * 设备SN码
     */
    private String sncode;

    /**
     * 区域ID
     */
    private int areaId;

    /**
     * 状态
     */
    private int status;

    /**
     * 固件版本
     */
    private String version;

    /**
     * 是否软删除 0否 1是
     */
    private int isSoftDelete;


    /**
     * 采集器名称
     */
    private String bathroomName;


}
