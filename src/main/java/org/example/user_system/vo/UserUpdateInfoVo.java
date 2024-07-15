package org.example.user_system.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserUpdateInfoVo {
    /**
     * 账号
     */
    private String account;
    /**
     * 昵称
     */
    private String nickname;
    /**
     * 性别（男 1 ，女 0，未知 -1）
     */
    private Integer gender;
}
