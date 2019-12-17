package com.road.quzhibathhousemqtt.util;

import java.io.IOException;
import java.io.InputStream;

import java.util.Properties;

/**
 * @author wenqi
 * @Title: PropertiesUtil
 * @ProjectName quzhi-bathhouse-mqtt
 * @Description:
 * @date 2019/2/19下午4:57
 */
public class PropertiesUtil {

    public static String MQTT_HOST;
    public static String MQTT_CLIENTID;
    public static String MQTT_USER_NAME;
    public static String MQTT_PASSWORD;
    public static int MQTT_TIMEOUT;
    public static int MQTT_KEEP_ALIVE;
    public static String MQTT_TOPIC ;




    static {
        MQTT_HOST = loadMqttProperties().getProperty("host");
        MQTT_CLIENTID = loadMqttProperties().getProperty("clientId");
        MQTT_USER_NAME = loadMqttProperties().getProperty("username");
        MQTT_PASSWORD = loadMqttProperties().getProperty("password");
        MQTT_TIMEOUT = Integer.valueOf(loadMqttProperties().getProperty("timeout"));
        MQTT_KEEP_ALIVE = Integer.valueOf(loadMqttProperties().getProperty("keepAliveInterval"));
        MQTT_TOPIC = loadMqttProperties().getProperty("topics");
    }


    private static Properties loadMqttProperties() {
        InputStream inputstream = PropertiesUtil.class.getResourceAsStream("/mqtt.yml");
        Properties properties = new Properties();
        try {
            properties.load(inputstream);
            return properties;
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (inputstream != null) {
                    inputstream.close();
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

}
