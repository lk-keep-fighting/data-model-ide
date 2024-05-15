package com.aims.datamodel.core.dsl;

import lombok.Data;

@Data
public class DataModelColumn extends DataTableColumn {
    private String column;
//    /**
//     * 数据精度，包含小数位的数据总长度，与store共享
//     */
//    private int dataPrecision;
//    /**
//     * 数据小数位长度，与store共享
//     */
//    private int dataScale;

}
