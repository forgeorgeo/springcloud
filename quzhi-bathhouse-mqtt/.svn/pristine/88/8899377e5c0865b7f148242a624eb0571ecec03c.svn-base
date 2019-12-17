package com.road.quzhibathhousemqtt.mqtt;

import com.road.quzhibathhousemqtt.config.MqttConfig;
import com.road.quzhibathhousemqtt.util.PropertiesUtil;
import lombok.extern.slf4j.Slf4j;

import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.List;


/**
 * @author wenqi
 * @Title: MQTTPushClient
 * @ProjectName quzhi-bathhouse-mqtt
 * @Description:
 * @date 2019/2/19下午1:45
 */
@Slf4j
public class MQTTPushClient {

    private static MqttClient client;

    @Autowired
    private MqttConfig mqttConfig;




    private static MQTTPushClient mqttPushClient = null;

    public static MQTTPushClient getInstance(){
        log.info("MQTTPushClient getInstance ");
        if(null == mqttPushClient){
            synchronized (MQTTPushClient.class){
                if(null == mqttPushClient){
                    log.info("MQTTPushClient getInstance new MQTTPushClient");
                    mqttPushClient = new MQTTPushClient();
                }
            }

        }
        return mqttPushClient;

    }

    public static MqttClient getClient() {
        return client;
    }

    public static void setClient(MqttClient client) {
        MQTTPushClient.client = client;
    }

    public MQTTPushClient(){
        connect(PropertiesUtil.MQTT_HOST,PropertiesUtil.MQTT_CLIENTID,PropertiesUtil.MQTT_USER_NAME,
                PropertiesUtil.MQTT_PASSWORD,PropertiesUtil.MQTT_TIMEOUT,PropertiesUtil.MQTT_KEEP_ALIVE);
    }

    /**
     * 创建MQTT连接
     */
    public void connect(String host, String clientID, String username, String password, int timeout, int keepAliveInterval){

        try {
            log.info("MQTTPushClient connect 创建连接");
            String mqttClientId = clientID + MqttAsyncClient.generateClientId();
            client = new MqttClient(host, mqttClientId, new MemoryPersistence());
            MqttConnectOptions options = new MqttConnectOptions();
            // 不缓存Queues 队列
            options.setCleanSession(true);
            options.setUserName(username);
            options.setPassword(password.toCharArray());
            options.setConnectionTimeout(timeout);
            options.setKeepAliveInterval(keepAliveInterval);
            try {
                client.setCallback(new PushCallback());
                client.connect(options);
            } catch (Exception e) {
                log.error("MQTTPushClient connect 连接异常: " ,e);
                e.printStackTrace();
            }
        } catch (Exception e) {
            log.error("MQTTPushClient connect 创建异常: " + e);
            e.printStackTrace();
        }
    }

    /**
     * 发布，默认qos为0，非持久化
     * @param topic
     * @param pushMessage
     */
    public void publish(String topic,String pushMessage){
        publish(0, false, topic, pushMessage);
    }

    /**
     * 发布
     * @param qos
     * @param retained
     * @param topic
     * @param pushMessage
     */
    public void publish(int qos,boolean retained,String topic,String pushMessage){
        MqttMessage message = new MqttMessage();
        message.setQos(qos);
        message.setRetained(retained);
        message.setPayload(pushMessage.getBytes());
        MqttTopic mTopic = MQTTPushClient.getClient().getTopic(topic);
        if(null == mTopic){
            log.error("topic not exist");
        }
        MqttDeliveryToken token;
        try {

            log.info("向: "+topic+ ", 发布消息: "+pushMessage);
             token = mTopic.publish(message);
            token.waitForCompletion();
        } catch (MqttPersistenceException e) {
            log.error("发布消息异常: ",e);
            e.printStackTrace();
        } catch (MqttException e) {
            log.error("发布消息异常: ", e);
            MQTTPushClient client=   MQTTPushClient.getInstance();
            String topics = PropertiesUtil.MQTT_TOPIC;
            log.info("订阅topics: {}",topics);
            List<String> topicList = Arrays.asList(topics.split(","));

            // 订阅 Topic
            topicList.stream().forEach( item ->{
                List<String> topicBO = Arrays.asList(item.split("-"));
                client.subscribe(topicBO.get(0),Integer.valueOf(topicBO.get(1)));
            });

            e.printStackTrace();
        }
        log.info("发送完成");
    }

    /**
     * 订阅某个主题，qos默认为0
     * @param topic
     */
    public void subscribe(String topic){
        subscribe(topic,0);
    }

    /**
     * 订阅某个主题
     * @param topic
     * @param qos
     */
    public void subscribe(String topic,int qos){
        try {
            log.info("订阅主题 topic: {} , qos: {}",topic,qos);
            MQTTPushClient.getClient().subscribe(topic, qos);
        } catch (MqttException e) {
            log.error("订阅异常 topic: {} message: {}",topic,e.getMessage());
            e.printStackTrace();
        }
    }




}
