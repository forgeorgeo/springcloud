package com.road.quzhibathhousemqtt.mqtt;

import com.road.quzhibathhousemqtt.enums.WorkModeEnum;
import com.road.quzhibathhousemqtt.service.CMDDispatcherService;
import com.road.quzhibathhousemqtt.service.impl.CMDDispatcherServiceImpl;
import com.road.quzhibathhousemqtt.task.MQTTSubTask;
import com.road.quzhibathhousemqtt.util.PropertiesUtil;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.text.ParseException;
import java.util.Arrays;
import java.util.List;


/**
 * @author wenqi
 * @Title: PushCallback
 * @ProjectName quzhi-bathhouse-mqtt
 * @Description:
 * @date 2019/2/19下午1:49
 */
@Slf4j
@Component
public class PushCallback implements MqttCallback {


    @Autowired
    private CMDDispatcherService dispatcherService ;



    public static PushCallback pushCallback;

    @PostConstruct
    public void init() {
        pushCallback = this;
    }

    @Override
    public void connectionLost(Throwable cause) {
        // 连接丢失后，一般在这里面进行重连
       log.error("mqtt已断开连接");
       // 尝试重新连接和订阅

        subscribeTopic();
    }
    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {
        log.info("deliveryComplete massageId: {} isComplete: {}" ,token.getMessageId(), token.isComplete());
    }
    @Override
    public void messageArrived(String topic, MqttMessage message) throws Exception {
        // subscribe后得到的消息会执行到这里面
        log.info("接收消息 Topic: {} , Qos: {} ",topic , message.getQos());
        log.info("Topic: {}, 接收消息内容: {}" ,topic , new String(message.getPayload()));

        try {
            pushCallback.dispatcherService.execute(WorkModeEnum.WORK_MODE_RECEIVE, message);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("消息分发出错",e);
        }

    }
    public void subscribeTopic(){
        // 获取mqtt客户端
//        MQTTPushClient client = MQTTPushClient.getInstance();
        // 获取配置文件Topic
        String topics = PropertiesUtil.MQTT_TOPIC;
        log.info("订阅topics: {}",topics);
        List<String> topicList = Arrays.asList(topics.split(","));
        MQTTPushClient mqttPushClient = MQTTPushClient.getInstance();

        // 订阅 Topic
        topicList.stream().forEach( topic ->{
            List<String> topicBO = Arrays.asList(topic.split("-"));
            mqttPushClient.subscribe(topicBO.get(0),Integer.valueOf(topicBO.get(1)));
        });

    }
}


