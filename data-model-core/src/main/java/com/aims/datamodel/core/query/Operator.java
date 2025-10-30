package com.aims.datamodel.core.query;

/**
 * 查询操作符
 * 统一定义所有支持的查询条件操作符
 */
public enum Operator {
    EQ("="),           // 等于
    NE("!="),          // 不等于
    GT(">"),           // 大于
    GE(">="),          // 大于等于
    LT("<"),           // 小于
    LE("<="),          // 小于等于
    LIKE("LIKE"),      // 模糊匹配
    NOT_LIKE("NOT LIKE"),
    IN("IN"),          // 在集合中
    NOT_IN("NOT IN"),
    BETWEEN("BETWEEN"),
    IS_NULL("IS NULL"),
    IS_NOT_NULL("IS NOT NULL");
    
    private final String sql;
    
    Operator(String sql) {
        this.sql = sql;
    }
    
    public String getSql() {
        return sql;
    }
}
