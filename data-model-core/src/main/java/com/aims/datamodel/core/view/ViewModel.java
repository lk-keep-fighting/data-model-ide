package com.aims.datamodel.core.view;

import com.aims.datamodel.core.store.StoreTable;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 逻辑视图模型
 * 业务层面的数据模型定义，与物理存储解耦
 */
@Data
@Accessors(chain = true)
public class ViewModel {
    /**
     * 视图唯一标识
     */
    private String id;
    
    /**
     * 视图名称
     */
    private String name;
    
    /**
     * 主表逻辑名
     */
    private String mainTable;
    
    /**
     * 逻辑列定义 key=逻辑列名
     */
    private Map<String, ViewColumn> columns = new LinkedHashMap<>();
    
    /**
     * 逻辑到物理的映射 key=逻辑列名
     */
    private Map<String, StoreMapping> mappings = new LinkedHashMap<>();
    
    /**
     * 物理表定义 key=表逻辑名
     */
    private Map<String, StoreTable> storeTables = new LinkedHashMap<>();
    
    /**
     * 查找主表配置
     */
    public StoreTable getMainStoreTable() {
        return storeTables.get(mainTable);
    }
    
    /**
     * 查找逻辑列对应的物理列名
     */
    public String getStoreColumnName(String viewColumn) {
        StoreMapping mapping = mappings.get(viewColumn);
        if (mapping != null) {
            return mapping.getStoreColumn();
        }
        return viewColumn; // 默认同名
    }
    
    /**
     * 查找逻辑列对应的物理表名
     */
    public String getStoreTableName(String viewColumn) {
        StoreMapping mapping = mappings.get(viewColumn);
        if (mapping != null) {
            return mapping.getStoreTable();
        }
        return mainTable; // 默认主表
    }
}
