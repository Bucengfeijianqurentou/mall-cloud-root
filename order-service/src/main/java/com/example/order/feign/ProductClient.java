package com.example.order.feign;


import com.example.common.entity.Product;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(value = "product-service")
public interface ProductClient {

    @GetMapping("/product/{id}")
    Product getProductById(@PathVariable("id") Long id);

}
