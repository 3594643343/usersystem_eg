package org.example.user_system.result;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PageResult<T> implements Serializable {
    /**
     * 总数数据大小
     */
    private Integer total;
    /**
     * 获取到的数据
     */
    private List<T> rows;
}
