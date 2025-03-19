package com.aims.datamodel.core.dsl;

import lombok.Data;

@Data
public class DataTableColumn {
    /**
     * 存储字段
     */
    private String storeColumn;
    /**
     * 默认值，与store共享
     */
    private String storeDefaultValue;

    /**
     * 完整的字段数据库存储类型，用于生成建表语句，如varchar(50)
     */
    private String storeColumnType;
    /**
     * 存储字段类型，不包含长度，如varchar
     */
    private String storeDataType;
    /**
     * 存储字段长度，如50，与dataLength关联映射
     */
    private String storeDataLength;
    private String storeComment;
    private int storePrecision;
    private int storeScale;
    private boolean storeIsNullable = true;
    private boolean storeIsPrimaryKey;

    public String storeColumnType() {
        if (storeDataType == null) {
            return "varchar(100)";
        }
        return storeDataType + "(" + storeDataLength + ")";
    }

    public boolean isStoreIsNullable() {
        if (isStoreIsPrimaryKey()) return false;
        return storeIsNullable;
    }
}
