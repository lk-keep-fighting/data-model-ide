package com.aims.datamodel.core.query;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 查询条件
 * 使用统一的values数组，消除value/values分支
 */
@Data
@Accessors(chain = true)
public class QueryCondition {
    /**
     * 逻辑列名
     */
    private String column;
    
    /**
     * 操作符
     */
    private Operator operator = Operator.EQ;
    
    /**
     * 值数组（统一使用数组，适配所有操作符）
     * EQ/NE/GT等：values[0]
     * IN/NOT_IN：values[0..n]
     * BETWEEN：values[0], values[1]
     * IS_NULL/IS_NOT_NULL：不使用
     */
    private Object[] values;
    
    /**
     * 便捷构造：单值条件
     */
    public static QueryCondition eq(String column, Object value) {
        return new QueryCondition()
                .setColumn(column)
                .setOperator(Operator.EQ)
                .setValues(new Object[]{value});
    }
    
    /**
     * 便捷构造：IN条件
     */
    public static QueryCondition in(String column, Object... values) {
        return new QueryCondition()
                .setColumn(column)
                .setOperator(Operator.IN)
                .setValues(values);
    }
    
    /**
     * 便捷构造：BETWEEN条件
     */
    public static QueryCondition between(String column, Object start, Object end) {
        return new QueryCondition()
                .setColumn(column)
                .setOperator(Operator.BETWEEN)
                .setValues(new Object[]{start, end});
    }
    
    /**
     * 便捷构造：LIKE条件
     */
    public static QueryCondition like(String column, Object value) {
        return new QueryCondition()
                .setColumn(column)
                .setOperator(Operator.LIKE)
                .setValues(new Object[]{value});
    }
}
