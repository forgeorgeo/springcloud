package com.road.quzhibathhousemqtt.config;

import com.road.quzhibathhousemqtt.mqtt.MQTTPushClient;
import com.road.quzhibathhousemqtt.util.PropertiesUtil;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 * @author wenqi
 * @Title: MqttConfig
 * @ProjectName quzhi-bathhouse-mqtt
 * @Description: mqtt服务配置
 * @date 2019/2/19下午1:36
 */
@Component
@ConfigurationProperties(prefix = "com.mqtt")
@Data
public class MqttConfig{

    private String host;

    private String clientId;

    private String topic;

    private String username;

    private String password;

    private int timeout;

    private int keepAliveInterval;
    @Bean
    public MQTTPushClient getMqttPushClient(){
        MQTTPushClient mqttPushClient = new MQTTPushClient();
        mqttPushClient.connect(PropertiesUtil.MQTT_HOST, PropertiesUtil.MQTT_CLIENTID, PropertiesUtil.MQTT_USER_NAME,
                PropertiesUtil.MQTT_PASSWORD, PropertiesUtil.MQTT_TIMEOUT,PropertiesUtil.MQTT_KEEP_ALIVE);
        return mqttPushClient;
    }



}
