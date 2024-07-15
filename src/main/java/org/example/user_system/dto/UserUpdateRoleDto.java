package org.example.user_system.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserUpdateRoleDto {
    /**
     * 用户id
     */
    private Integer userId;
    /**
     * 角色
     */
    private Integer roleId;
}
