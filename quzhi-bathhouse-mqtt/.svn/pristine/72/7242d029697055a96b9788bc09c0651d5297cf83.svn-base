package com.road.quzhibathhousemqtt.controller;

import com.road.quzhibathhousemqtt.config.MqttConfig;
import com.road.quzhibathhousemqtt.mqtt.MQTTPushClient;
import com.road.quzhibathhousemqtt.mqtt.PushPayload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author wenqi
 * @Title: HelloWorldController
 * @ProjectName quzhi-bathhouse-mqtt
 * @Description:
 * @date 2019/2/19下午3:28
 */
@RestController
public class HelloWorldController {
    @Autowired
    private MqttConfig MqttConfig;

    @GetMapping
    public String hello() {
        return "hello";
    }
    @GetMapping("/push")
    public String push(@RequestParam String topic){
        PushPayload pushPayload = PushPayload.getPushPayloadBuider().setContent("test")
                .setMobile("119")
                .setType("2018")
                .bulid();


        MQTTPushClient mqttPushClient = MQTTPushClient.getInstance();

        mqttPushClient.publish(topic,pushPayload.toString());

        return topic;
    }
}
