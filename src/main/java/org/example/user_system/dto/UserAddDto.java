package org.example.user_system.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserAddDto {
    /**
     * 账号
     */
    private String account;

    /**
     * 密码
     */
    private String password;

    /**
     * 昵称
     */
    private String nickname;

    /**
     * 性别（男 1 ，女 0，未知 -1）
     */
    private Integer gender;

    /**
     * 角色（超级管理员 0，管理员 1，普通用户 2）
     */
    private Integer role;
}
