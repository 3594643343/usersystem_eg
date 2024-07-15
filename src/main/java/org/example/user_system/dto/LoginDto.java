package org.example.user_system.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoginDto {
    /**
     * 账号
     */
    String account;
    /**
     * 密码
     */
    String password;
}
