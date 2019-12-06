package cn.demo.service;
import org.springframework.stereotype.Component;

@Component
public class HelloServiceFallBack implements HelloService{

    @Override
    public String hello(String name) {
        return "hello, hystrix 触发熔断     == fail name : " + name;
    }
}
