package org.example.user_system.dto;

import jakarta.validation.constraints.Pattern;
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
//    @NotEmpty(message = "账号不能为空")
//    @Length(min = 4, max = 12, message = "账号长度范围为4-12")
//    @Pattern(regexp = "^[a-zA-Z0-9]{4,12}$", message = "账号不符合要求，账号仅由大小写字母和数字构成，且长度为4-12")
    String account;
    /**
     * 密码
     */
//    @NotEmpty(message = "密码不能为空")
//    @Length(min = 6, max = 16, message = "账号长度范围为6-16")
//    @Pattern(regexp = "^[a-zA-Z0-9]{6,16}$", message = "密码不符合要求，密码仅由大小写字母和数字构成，且长度为6-16")
    String password;
}
