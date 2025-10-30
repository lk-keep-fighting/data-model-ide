package com.aims.datamodel.core.sqlbuilder;

import com.aims.datamodel.core.aggregate.AggregateModel;
import com.aims.datamodel.core.query.QueryModel;
import com.aims.datamodel.core.submit.SubmitModel;

/**
 * SQL构建器接口
 * 职责单一：只负责根据模型生成SQL
 * 支持不同数据库实现
 */
public interface SqlBuilder {
    /**
     * 构建查询SQL
     * 返回PreparedStatement格式的SQL和参数
     */
    SqlResult buildQuery(QueryModel query);
    
    /**
     * 构建插入SQL
     */
    SqlResult buildInsert(SubmitModel submit);
    
    /**
     * 构建更新SQL
     */
    SqlResult buildUpdate(SubmitModel submit);
    
    /**
     * 构建删除SQL
     */
    SqlResult buildDelete(String viewModelId, Object primaryKeyValue);
    
    /**
     * 构建统计SQL
     */
    SqlResult buildAggregate(AggregateModel aggregate);
}
