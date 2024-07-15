package org.example.user_system.config;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.example.user_system.interceptor.JwtTokenInterceptor;
import org.example.user_system.interceptor.LogInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@Slf4j
public class WebMvcConfiguration implements WebMvcConfigurer {

    @Resource
    private LogInterceptor logInterceptor;

    @Resource
    JwtTokenInterceptor jwtTokenInterceptor;


    /**
     * @param registry 注册器，用于注册拦截器
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        WebMvcConfigurer.super.addInterceptors(registry);

        log.info("开始注册自定义拦截器...");

        registry.addInterceptor(logInterceptor);

        registry.addInterceptor(jwtTokenInterceptor)
                .addPathPatterns("/admin/**")
                .excludePathPatterns("/admin/login");
    }
}
