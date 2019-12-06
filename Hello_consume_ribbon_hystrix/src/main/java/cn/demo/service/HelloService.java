package cn.demo.service;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class HelloService {

	@Autowired
	private RestTemplate restTemplate;

	@HystrixCommand(fallbackMethod="fallback",commandProperties={@HystrixProperty(name= "execution.isolation.thread.timeoutInMilliseconds", value="2000")})
	public String hello (String name) {
		return restTemplate.getForEntity("http://USER-SERVICE/hello?name=" + name, String.class).getBody();
	}

	public String fallback (String name) {
		return "hello, hystrix熔断启动=== fail name:" + name;
	}

}
