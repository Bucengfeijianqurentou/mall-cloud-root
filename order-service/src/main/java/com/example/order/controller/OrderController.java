package com.example.order.controller;

import com.example.common.entity.Product;
import com.example.order.DO.Order;
import com.example.order.DO.ProductDTO;
import com.example.order.feign.ProductClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.net.URI;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private RestTemplate restTemplate;

    //发现客户端
    @Autowired
    private DiscoveryClient discoveryClient;

    // 注入我们定义的 Feign 接口
    // Spring 会在这个接口运行时动态生成一个代理对象
    @Autowired
    private ProductClient productClient;

    // --- 1. 手写负载均衡版 ---
    @GetMapping("/create/manual/{productId}")
    public Order createOrderManual(@PathVariable("productId") Long productId) {

        //手写负载均衡算法
        List<ServiceInstance> instances = discoveryClient.getInstances("product-service");

        if (instances == null && instances.isEmpty()) {
            throw new RuntimeException("找不到商品服务！");
        }

        //从实例列表里随机选一个
        int index = ThreadLocalRandom.current().nextInt(instances.size());

        ServiceInstance serviceInstance = instances.get(index);

        System.out.println("本次服务选中了服务节点：" + serviceInstance.getHost() + ":" + serviceInstance.getPort());

        //手动拼接url
        URI uri = serviceInstance.getUri();

        String url = uri + "/product/" + productId;

        // 注意：这里不能用注入的 restTemplate，因为它带了 @LoadBalanced 拦截器，
        // 遇到具体的 IP 地址反而会懵圈。我们要 new 一个纯净的 RestTemplate。

        ProductDTO product = new RestTemplate().getForObject(url, ProductDTO.class);



        // 2. 模拟下单逻辑
        Order order = new Order();
        order.setOrderId(System.currentTimeMillis());
        order.setProductId(product.getId());
        order.setProductName(product.getName()); // 数据来自商品服务
        order.setTotalPrice(product.getPrice());

        return order;
    }


    // --- 2. 自动负载均衡版 (之前的代码) ---
    @GetMapping("/create/{productId}")
    public Order createOrder(@PathVariable("productId") Long productId) {
        // 直接用服务名，@LoadBalanced 会帮我们在底层做和上面一样的事情
        String url = "http://product-service/product/" + productId;

        // 使用注入的、带负载均衡功能的 restTemplate
        ProductDTO product = restTemplate.getForObject(url, ProductDTO.class);

        Order order = new Order();
        order.setOrderId(System.currentTimeMillis());
        order.setProductId(product.getId());
        order.setProductName(product.getName());
        order.setTotalPrice(product.getPrice());
        return order;
    }

    @GetMapping("/create/feign/{productId}")
    public Order createOrderFeign(@PathVariable("productId") Long productId) {

        System.out.println("收到下单请求，准备通过 Feign 调用商品服务...");

        // 以前：restTemplate.getForObject(...)
        // 现在：像调用本地方法一样简单
        //它会自动帮我们实现负载均衡
        Product product = productClient.getProductById(productId);

        Order order = new Order();
        order.setOrderId(System.currentTimeMillis());
        order.setProductId(product.getId());
        order.setProductName(product.getName());
        order.setTotalPrice(product.getPrice());

        return order;
    }

}