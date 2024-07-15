package org.example.user_system.utils;

import cn.hutool.jwt.JWTPayload;
import cn.hutool.jwt.JWTUtil;
import org.example.user_system.properties.JwtProperties;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;


public class JwtUtils {

    /**
     * 创建token
     * @param jwtProperties jwt参数
     * @param userId 用户id
     * @return String
     */
    public static String crateJwt(JwtProperties jwtProperties, Integer userId) {
        Map<String, Object> claims = new HashMap<>();

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime exp = now.plusSeconds(jwtProperties.getTtl());

        claims.put("userId", userId);
        claims.put(JWTPayload.ISSUED_AT, now); // 签发时间
        claims.put(JWTPayload.NOT_BEFORE, now); // 生效时间
        claims.put(JWTPayload.EXPIRES_AT, exp); // 过期时间

        // key默认是按HS256(HmacSHA256)
        return JWTUtil.createToken(claims, jwtProperties.getSecretKey().getBytes());
    }
}
