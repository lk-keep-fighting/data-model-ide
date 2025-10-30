package com.aims.datamodel.core.query;

import com.aims.datamodel.core.view.ViewModel;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;

/**
 * 查询场景模型
 */
@Data
@Accessors(chain = true)
public class QueryModel {
    /**
     * 关联的视图模型
     */
    private ViewModel view;
    
    /**
     * 查询条件列表（AND关系）
     */
    private List<QueryCondition> filters = new ArrayList<>();
    
    /**
     * 需要查询的列（空表示全部）
     */
    private List<String> selectColumns = new ArrayList<>();
    
    /**
     * 分页参数
     */
    private Pagination pagination;
    
    /**
     * 排序字段（格式："column ASC"或"column DESC"）
     */
    private List<String> orderBy = new ArrayList<>();
    
    /**
     * 添加查询条件
     */
    public QueryModel addFilter(QueryCondition condition) {
        this.filters.add(condition);
        return this;
    }
    
    /**
     * 添加查询列
     */
    public QueryModel addColumn(String column) {
        this.selectColumns.add(column);
        return this;
    }
}
