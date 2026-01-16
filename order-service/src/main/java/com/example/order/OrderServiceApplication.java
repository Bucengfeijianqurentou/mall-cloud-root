package com.example.order;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

// 加上这个注解，指定扫描的基础包，把 com.example 都扫进去
// 这样既能扫到 com.example.order，也能扫到 com.example.common
@SpringBootApplication(scanBasePackages = "com.example")
@EnableFeignClients //开启Feign扫描
@EnableDiscoveryClient //开启服务发现
public class OrderServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(OrderServiceApplication.class,args);
    }
}
