package com.road.quzhibathhousemqtt;

import com.road.quzhibathhousemqtt.config.MqttConfig;
import com.road.quzhibathhousemqtt.mqtt.MQTTPushClient;
import com.road.quzhibathhousemqtt.mqtt.PushPayload;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author wenqi
 * @Title: QuzhiBathhouseMqttApplicationTest
 * @ProjectName quzhi-bathhouse-mqtt
 * @Description:
 * @date 2019/2/19下午3:50
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class QuzhiBathhouseMqttApplicationTest {
    @Test
    public void contextLoads() {
    }
    @Autowired
    private MqttConfig mqttConfig;

    @Test
    public void test(){
        PushPayload pushPayload = PushPayload.getPushPayloadBuider().setContent("test")
                .setMobile("119")
                .setType("2018")
                .bulid();


        MQTTPushClient client = MQTTPushClient.getInstance();
        client.subscribe("test2");
        client.publish("test",pushPayload.toString());

    }
}