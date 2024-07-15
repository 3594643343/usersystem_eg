package org.example.user_system.utils;

import cn.hutool.crypto.SecureUtil;

public class SecureUtils {
    /**
     * 加密密码
     * @param password 密码
     * @return String
     */
    public static String EncryptedPassword(String password) {
        // 使用md54加密密码
        return SecureUtil.md5(password);
    }
}
