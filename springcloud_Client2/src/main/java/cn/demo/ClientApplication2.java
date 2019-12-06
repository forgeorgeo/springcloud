package cn.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class ClientApplication2 {

	 public static void main(String[] args) {
         SpringApplication.run(ClientApplication2.class, args);
         System.out.println("启动成功!");
     }
}