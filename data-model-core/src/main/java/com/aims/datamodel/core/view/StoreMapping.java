package com.aims.datamodel.core.view;

import com.aims.datamodel.core.store.StoreColumn;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 逻辑列到物理列的映射
 * 解耦业务视图与物理存储
 */
@Data
@Accessors(chain = true)
public class StoreMapping {
    /**
     * 逻辑列名
     */
    private String viewColumn;
    
    /**
     * 物理表名
     */
    private String storeTable;
    
    /**
     * 物理列名
     */
    private String storeColumn;
    
    /**
     * 物理列的元信息
     */
    private StoreColumn meta;
}
