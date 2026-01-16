package com.example.common.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;

public class GatewayInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 1. 获取请求头中的暗号
        String secretKey = request.getHeader("Gateway-Key");

        // 2. 校验暗号
        if ("token-secret".equals(secretKey)) {
            // 暗号正确，放行
            return true;
        }

        // 3. 暗号错误，拒绝访问
        response.setStatus(403); // Forbidden
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().write("{\"code\": 403, \"msg\": \"请从网关访问！禁止直接偷家！\"}");
        return false;
    }
}