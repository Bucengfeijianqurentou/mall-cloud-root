package com.example.product;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;


// 加上这个注解，指定扫描的基础包，把 com.example 都扫进去
// 这样既能扫到 com.example.order，也能扫到 com.example.common
@SpringBootApplication(scanBasePackages = "com.example")
@EnableDiscoveryClient //开启服务发现
public class ProductServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(ProductServiceApplication.class,args);
    }
}
