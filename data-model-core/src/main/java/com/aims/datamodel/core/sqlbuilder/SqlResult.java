package com.aims.datamodel.core.sqlbuilder;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;

/**
 * SQL构建结果
 * 包含参数化SQL和对应的参数列表
 */
@Data
@Accessors(chain = true)
public class SqlResult {
    /**
     * 参数化SQL（使用?占位符）
     */
    private String sql;
    
    /**
     * 参数列表（按顺序对应?占位符）
     */
    private List<Object> params = new ArrayList<>();
    
    /**
     * 添加参数
     */
    public SqlResult addParam(Object param) {
        this.params.add(param);
        return this;
    }
}
