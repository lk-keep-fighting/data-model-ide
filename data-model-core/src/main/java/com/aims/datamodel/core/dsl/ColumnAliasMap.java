package com.aims.datamodel.core.dsl;

import lombok.Data;

@Data
public class ColumnAliasMap extends DataTableColumn {
//    private String column;
    /**
     * 数据展现类型，从业务使用角度来定义，如：text,number,datetime,boolean等
     */
    private String dataType;
    /**
     * 数据长度，如：50
     */
    private String dataLength;
    private String table;
}
