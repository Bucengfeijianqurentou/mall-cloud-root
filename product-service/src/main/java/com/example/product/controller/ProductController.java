package com.example.product.controller;

import com.example.common.entity.Product;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping("/product")
@RefreshScope //开启配置自动刷新
public class ProductController {

    // 读取 Nacos 里的配置
    @Value("${nacos.feature.coupon:false}") // 冒号后面是默认值，防止Nacos没配报错
    private boolean couponEnabled;

    @Value("${nacos.feature.discount:1.0}")
    private double discount;

    @GetMapping("/config-test")
    public Map<String, Object> testConfig() {
        Map<String, Object> map = new HashMap<>();
        map.put("优惠券开关", couponEnabled);
        map.put("当前折扣", discount);
        return map;
    }

    // 模拟数据库数据
    private static final Map<Long, Product> PRODUCT_DB = new ConcurrentHashMap<>();

    static {
        PRODUCT_DB.put(1L, new Product(1L, "iPhone 15", new BigDecimal("999.00")));
        PRODUCT_DB.put(2L, new Product(2L, "MacBook Pro", new BigDecimal("1999.00")));
        PRODUCT_DB.put(3L, new Product(3L, "AirPods", new BigDecimal("199.00")));
    }

    @GetMapping("/{id}")
    public Product getProductById(@PathVariable("id") Long id) {
        // 实际开发中这里会调用 Service -> DAO
        System.out.println("商品服务被调用，查询商品ID: " + id);
        return PRODUCT_DB.getOrDefault(id, new Product(id, "未知商品", BigDecimal.ZERO));
    }

}
