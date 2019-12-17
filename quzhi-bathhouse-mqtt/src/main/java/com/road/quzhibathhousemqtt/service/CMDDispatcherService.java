package com.road.quzhibathhousemqtt.service;


import com.road.quzhibathhousemqtt.enums.WorkModeEnum;
import org.eclipse.paho.client.mqttv3.MqttMessage;

/**
 * @author wenqi
 * @Title: CMDDispatcherService
 * @ProjectName quzhi-bathhouse-mqtt
 * @Description: 命令解析服务
 * @date 2019/2/20上午11:36
 */
public interface CMDDispatcherService {

    /**
     * 解析命令
     * @param modeEnum 解析状态枚举
     * @param message  mqtt消息
     */
    void execute(WorkModeEnum modeEnum, MqttMessage message);
}
