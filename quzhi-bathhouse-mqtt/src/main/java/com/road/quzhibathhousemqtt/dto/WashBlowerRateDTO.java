package com.road.quzhibathhousemqtt.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author wenqi
 * @title: WashBlowerRateDTO
 * @projectName quzhi-bathhouse-mqtt
 * @description:
 * @date 2019/4/9下午4:49
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class WashBlowerRateDTO {

    private String key;
    private String addr;
    private String projectId;
    private String rate1;
    private String time1;
    private String rate2;
    private String time2;
    private String rate3;
    private String time3;
    private String rate4;
    private String time4;
    private String deviceType;
    private String rateVersion;
    private String cmdId;


}
