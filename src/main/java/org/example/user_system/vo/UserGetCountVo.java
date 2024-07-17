package org.example.user_system.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserGetCountVo {
    /**
     * 总数
     */
    private Long num;
}
