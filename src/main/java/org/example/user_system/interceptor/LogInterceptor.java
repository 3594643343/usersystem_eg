package org.example.user_system.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * 普通拦截器
 */
@Component
@Slf4j
public class LogInterceptor implements HandlerInterceptor {

    public boolean preHandle(final HttpServletRequest request, final HttpServletResponse response, final Object handler) {
        log.info("{} {}", request.getMethod(), request.getRequestURI());
        return true;
    }
}
