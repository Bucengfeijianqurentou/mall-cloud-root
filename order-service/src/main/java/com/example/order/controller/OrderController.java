package com.example.order.controller;

import com.example.order.DO.Order;
import com.example.order.DO.ProductDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;

@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private RestTemplate restTemplate;

    @GetMapping("/create/{productId}")
    public Order createOrder(@PathVariable("productId") Long productId) {
        // 1. 远程调用商品服务 (硬编码 URL，这是微服务最原始的样子)
        // 注意：这里直接写死了 localhost:8081，这就是下一章我们要解决的问题
        String url = "http://localhost:8081/product/" + productId;

        // 发送请求，并把结果自动封装成 ProductDTO 对象
        ProductDTO product = restTemplate.getForObject(url, ProductDTO.class);

        // 2. 模拟下单逻辑
        Order order = new Order();
        order.setOrderId(System.currentTimeMillis());
        order.setProductId(product.getId());
        order.setProductName(product.getName()); // 数据来自商品服务
        order.setTotalPrice(product.getPrice());

        return order;
    }

}