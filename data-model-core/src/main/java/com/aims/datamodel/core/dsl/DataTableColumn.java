package com.aims.datamodel.core.dsl;

import lombok.Data;

@Data
public class DataTableColumn {
    private String column;
    /**
     * 数据展现类型，从业务使用角度来定义，如：text,number,datetime,boolean等
     */
    private String dataType;
    /**
     * 数据长度，如：50
     */
    private String dataLength;
    /**
     * 默认值，与store共享
     */
    private String defaultValue;

    /**
     * 完整的字段数据库存储类型，用于生成建表语句，如varchar(50)
     */
    private String storeType;
    /**
     * 存储字段类型，不包含长度，如varchar
     */
    private String storeDataType;
    /**
     * 存储字段长度，如50，与dataLength关联映射
     */
    private String storeLength;
    private String storeComment;
    private int storePrecision;
    private int storeScale;
    private boolean storeIsNullable;
    private boolean storeIsPrimaryKey;
}
