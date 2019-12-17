package com.road.quzhibathhousemqtt.bo;

import lombok.Data;

import java.io.Serializable;

/**
 * @author wenqi
 * @Title: MQTTDeviceStatus
 * @ProjectName quzhi-bathhouse-mqtt
 * @Description:
 * @date 2019/2/27下午4:13
 */
@Data
public class MQTTDeviceStatus implements Serializable {

    private static final long serialVersionUID = 7970L;
    /**
     * 集中控制器key
     */
    private String key;

    /**
     * 项目编号
     */
    private String prjID;
    /**
     * 状态
     */
    private int status;

    /**
     * 最后更新时间
     */
    private String upTime;

    /**
     * 建立连接时间
     */
    private String createTime;
    /**
     * 集中控制器地址
     */
    private String addr;
}
