package org.example.user_system.interceptor;


import cn.hutool.core.util.ObjectUtil;
import cn.hutool.jwt.JWT;
import cn.hutool.jwt.JWTUtil;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.example.user_system.context.BaseContext;
import org.example.user_system.properties.JwtProperties;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;


/**
 * jwt令牌校验的拦截器
 */
@Component
@Slf4j
public class JwtTokenInterceptor implements HandlerInterceptor {

    @Resource
    private JwtProperties jwtProperties;

    /**
     * 校验jwt
     *
     * @param request 请求
     * @param response 返回
     * @param handler 支持函数
     * @return boolean
     * @throws Exception 异常
     */
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 判断当前拦截到的是Controller的方法还是其他资源
        if (!(handler instanceof HandlerMethod)) {
            //当前拦截到的不是动态方法，直接放行
            return true;
        }

        String token = request.getHeader(jwtProperties.getUserTokenName());

        log.info("JWT Token: {}", token);

        if (ObjectUtil.isNotEmpty(token) ||
                !JWTUtil.verify(token, jwtProperties.getSecretKey().getBytes())) {
            // 不通过，响应401状态码
            response.setStatus(401);
            return false;
        }

        JWT jwt = JWTUtil.parseToken(token);

        Integer userId = Integer.valueOf(jwt.getHeader("userId").toString());
        log.info("当前用户 userId: {}", userId);

        // 创建线程局部变量
        BaseContext.setCurrentId(userId);

        return true;
    }

    /**
     * 删除线程局部变量
     *
     * @param request 请求
     * @param response 返回
     * @param handler 支持函数
     * @throws Exception 异常
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        HandlerInterceptor.super.afterCompletion(request, response, handler, ex);

        BaseContext.removeCurrentId();
    }
}
