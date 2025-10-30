package com.aims.datamodel.core.store;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 物理存储表定义
 * 只关心数据库层面的表信息
 */
@Data
@Accessors(chain = true)
public class StoreTable {
    /**
     * 数据库名（可选，支持跨库）
     */
    private String database;
    
    /**
     * 物理表名
     */
    private String table;
    
    /**
     * 主键列名，默认"id"
     */
    private String primaryKey = "id";
    
    /**
     * 返回完整表名（含数据库）
     */
    public String fullTableName() {
        if (database == null || database.isEmpty()) {
            return table;
        }
        return database + "." + table;
    }
}
