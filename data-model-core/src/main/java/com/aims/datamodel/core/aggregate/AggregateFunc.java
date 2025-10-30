package com.aims.datamodel.core.aggregate;

/**
 * 聚合函数
 */
public enum AggregateFunc {
    COUNT("COUNT"),
    SUM("SUM"),
    AVG("AVG"),
    MAX("MAX"),
    MIN("MIN");
    
    private final String sql;
    
    AggregateFunc(String sql) {
        this.sql = sql;
    }
    
    public String getSql() {
        return sql;
    }
}
