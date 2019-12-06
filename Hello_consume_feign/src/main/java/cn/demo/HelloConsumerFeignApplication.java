package cn.demo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cn.demo.service.HelloService;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
@RestController
public class HelloConsumerFeignApplication {

	public static void main(String[] args) {
		SpringApplication.run(HelloConsumerFeignApplication.class, args);
	}


	@Autowired
	private HelloService helloService;

	@RequestMapping("hello")
	public String hello(String name){
		return helloService.hello(name);
	}
}
