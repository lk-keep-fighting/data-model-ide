package com.aims.datamodel.core.aggregate;

import com.aims.datamodel.core.query.QueryCondition;
import com.aims.datamodel.core.view.ViewModel;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;

/**
 * 统计场景模型
 */
@Data
@Accessors(chain = true)
public class AggregateModel {
    /**
     * 关联的视图模型
     */
    private ViewModel view;
    
    /**
     * 统计条件
     */
    private List<QueryCondition> filters = new ArrayList<>();
    
    /**
     * 聚合字段列表
     */
    private List<AggregateField> aggregates = new ArrayList<>();
    
    /**
     * 分组字段
     */
    private List<String> groupBy = new ArrayList<>();
    
    /**
     * 添加聚合字段
     */
    public AggregateModel addAggregate(AggregateField field) {
        this.aggregates.add(field);
        return this;
    }
    
    /**
     * 添加分组字段
     */
    public AggregateModel addGroupBy(String column) {
        this.groupBy.add(column);
        return this;
    }
    
    /**
     * 添加过滤条件
     */
    public AggregateModel addFilter(QueryCondition condition) {
        this.filters.add(condition);
        return this;
    }
}
