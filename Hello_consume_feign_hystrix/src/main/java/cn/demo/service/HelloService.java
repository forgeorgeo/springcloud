package cn.demo.service;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "user-service", fallback = HelloServiceFallBack.class)
public interface HelloService {

    @RequestMapping("hello")
    String hello (@RequestParam(value = "name") String name) ;
}
