package com.example.product.controller;

import com.example.common.entity.Product;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping("/product")
public class ProductController {

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
