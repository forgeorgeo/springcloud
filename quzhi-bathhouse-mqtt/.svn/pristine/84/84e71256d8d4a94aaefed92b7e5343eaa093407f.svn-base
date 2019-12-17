package com.road.quzhibathhousemqtt;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@MapperScan(basePackages = "com.road.quzhibathhousemqtt.dao")
@EnableScheduling
@EnableTransactionManagement
public class QuzhiBathhouseMqttApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(QuzhiBathhouseMqttApplication.class, args);
    }
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(QuzhiBathhouseMqttApplication.class);
    }


}
