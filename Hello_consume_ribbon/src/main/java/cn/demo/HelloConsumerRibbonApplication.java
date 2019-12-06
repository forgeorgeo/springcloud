package cn.demo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@EnableDiscoveryClient
@RestController
public class HelloConsumerRibbonApplication {

    public static void main(String[] args) {
        SpringApplication.run(HelloConsumerRibbonApplication.class, args);
    }

    @Bean
    @LoadBalanced
    public RestTemplate restTemplate () {
        return new RestTemplate();
    }

    @Autowired
    private RestTemplate restTemplate;

    //获取服务实例，作用为之后console显示效果; "http://USER-SERVICE/hello?name= 标识注册的服务。USER-SERVICE在eureka注册的服务名
    @RequestMapping("hello")
    public ResponseEntity<String> hello (String name) {
        return restTemplate.getForEntity("http://USER-SERVICE/hello?name=" + name, String.class);
    }

}
