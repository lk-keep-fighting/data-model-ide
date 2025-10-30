package com.aims.datamodel.core.view;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 逻辑视图列定义
 * 从业务视角定义字段特性
 */
@Data
@Accessors(chain = true)
public class ViewColumn {
    /**
     * 逻辑列名
     */
    private String name;
    
    /**
     * 显示类型（text/number/date/datetime/boolean/json）
     * 用于前端渲染
     */
    private String displayType;
    
    /**
     * 是否可查询
     */
    private boolean queryable = true;
    
    /**
     * 是否可编辑
     */
    private boolean editable = true;
    
    /**
     * 是否可统计
     */
    private boolean aggregable = false;
    
    /**
     * 显示标签
     */
    private String label;
}
