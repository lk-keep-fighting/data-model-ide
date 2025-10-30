package com.aims.datamodel.core.store;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 物理存储列定义
 * 只关心数据库层面的列信息
 */
@Data
@Accessors(chain = true)
public class StoreColumn {
    /**
     * 物理列名
     */
    private String name;
    
    /**
     * 完整类型（如varchar(50), int, datetime）
     */
    private String type;
    
    /**
     * 默认值
     */
    private String defaultValue;
    
    /**
     * 是否可为空，默认true
     */
    private boolean nullable = true;
    
    /**
     * 是否主键
     */
    private boolean primaryKey = false;
    
    /**
     * 列注释
     */
    private String comment;
}
