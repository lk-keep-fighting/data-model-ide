package com.aims.datamodel.core.aggregate;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 聚合字段定义
 */
@Data
@Accessors(chain = true)
public class AggregateField {
    /**
     * 列名
     */
    private String column;
    
    /**
     * 聚合函数
     */
    private AggregateFunc func;
    
    /**
     * 结果别名
     */
    private String alias;
    
    /**
     * 便捷构造
     */
    public static AggregateField of(AggregateFunc func, String column, String alias) {
        return new AggregateField()
                .setFunc(func)
                .setColumn(column)
                .setAlias(alias);
    }
}
