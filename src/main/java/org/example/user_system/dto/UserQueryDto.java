package org.example.user_system.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserQueryDto {
    /**
     * 页码
     */
    private Integer page = 1;
    /**
     * 页大小
     */
    private Integer size = 10;
    /**
     * 模糊匹配账号
     */
    private String account;
    /**
     * 模糊匹配昵称
     */
    private String nickname;
    /**
     * 完全匹配性别
     */
    private Integer gender;
    /**
     * 完全匹配角色
     */
    private Integer role;
}
