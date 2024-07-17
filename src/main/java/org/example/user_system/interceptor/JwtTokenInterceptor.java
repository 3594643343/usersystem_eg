package org.example.user_system.interceptor;


import cn.hutool.core.util.ObjectUtil;
import cn.hutool.jwt.JWT;
import cn.hutool.jwt.JWTUtil;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.example.user_system.context.BaseContext;
import org.example.user_system.dto.CurrentUserDto;
import org.example.user_system.properties.JwtProperties;
import org.example.user_system.properties.RedisProp;
import org.springframework.data.redis.core.RedisTemplate;
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

    @Resource
    private RedisProp redisProp;

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

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

        if (ObjectUtil.isEmpty(token) ||
                !JWTUtil.verify(token, jwtProperties.getSecretKey().getBytes())) {
            // 不通过，响应401状态码
            response.setStatus(401);
            return false;
        }

        JWT jwt = JWTUtil.parseToken(token);

        Integer userId = Integer.valueOf(jwt.getPayload("userId").toString());
        Integer userRole = Integer.valueOf(jwt.getPayload("userRole").toString());

        if(redisProp.getEnabled() && redisTemplate.opsForValue().get(String.valueOf(userId)) == null) {
            response.setStatus(401);
            return false;
        }

        CurrentUserDto currentUserDto = CurrentUserDto.builder().userId(userId).userRole(userRole).build();

        log.info("当前用户 currentUserDto: {}", currentUserDto);

        // 创建线程局部变量
        BaseContext.setCurrentUser(currentUserDto);

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

        BaseContext.removeCurrentUser();
    }
}
