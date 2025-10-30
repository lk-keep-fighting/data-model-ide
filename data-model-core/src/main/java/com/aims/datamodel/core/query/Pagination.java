package com.aims.datamodel.core.query;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 分页参数
 */
@Data
@Accessors(chain = true)
public class Pagination {
    /**
     * 页码，从1开始
     */
    private int page = 1;
    
    /**
     * 每页大小
     */
    private int size = 20;
    
    /**
     * 计算offset
     */
    public int getOffset() {
        return (page - 1) * size;
    }
}
