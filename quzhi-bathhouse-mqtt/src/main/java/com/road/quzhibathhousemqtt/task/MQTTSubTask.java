package com.road.quzhibathhousemqtt.task;

import com.road.quzhibathhousemqtt.mqtt.MQTTPushClient;
import com.road.quzhibathhousemqtt.util.PropertiesUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;


import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.List;

/**
 * @author wenqi
 * @Title: MqttTask
 * @ProjectName quzhi-bathhouse-mqtt
 * @Description: 启动项目
 * @date 2019/2/20上午9:36
 */
@Slf4j
@Component
public class MQTTSubTask implements ApplicationRunner {

    @Autowired
    private MQTTPushClient mqttPushClient;



    public static MQTTSubTask mqttSubTask;

    @PostConstruct
    public void init() {
        mqttSubTask = this;
    }

    /**
     *  项目启动后执行
     * @param args
     * @throws Exception
     */
    @Override
    public void run(ApplicationArguments args) throws Exception {
        subscribeTopic();
    }

    /**
     * 如果MQTT未连接 连接MQTT，订阅Topic
     */
    public void subscribeTopic(){
        // 获取mqtt客户端
//        MQTTPushClient client = MQTTPushClient.getInstance();
        // 获取配置文件Topic
        String topics = PropertiesUtil.MQTT_TOPIC;
        log.info("订阅topics: {}",topics);
        List<String>  topicList = Arrays.asList(topics.split(","));

        // 订阅 Topic
        topicList.stream().forEach( topic ->{
            List<String> topicBO = Arrays.asList(topic.split("-"));
                mqttSubTask.mqttPushClient.subscribe(topicBO.get(0),Integer.valueOf(topicBO.get(1)));
        });

    }


}
