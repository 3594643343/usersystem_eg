package org.example.user_system.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserUpdateInfoDto {
    /**
     * id
     */
    private Integer id;
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
