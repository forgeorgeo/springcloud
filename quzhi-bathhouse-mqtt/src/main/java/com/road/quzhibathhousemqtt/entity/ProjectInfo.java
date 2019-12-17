package com.road.quzhibathhousemqtt.entity;

import lombok.Data;

/**
 * @author wenqi
 * @title: ProjectInfo
 * @projectName quzhi-bathhouse-mqtt
 * @description: 项目信息
 * @date 2019/4/11下午12:00
 */
@Data
public class ProjectInfo {

    private int projectId;

    private String name;

    private int isBathBooking;
}
