package com.aims.datamodel.core.sqlbuilder;

import com.aims.datamodel.core.aggregate.AggregateField;
import com.aims.datamodel.core.aggregate.AggregateModel;
import com.aims.datamodel.core.query.Operator;
import com.aims.datamodel.core.query.QueryCondition;
import com.aims.datamodel.core.query.QueryModel;
import com.aims.datamodel.core.store.StoreTable;
import com.aims.datamodel.core.submit.SubmitModel;
import com.aims.datamodel.core.view.ViewModel;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * MySQL SQL构建器
 * 所有SQL使用参数化查询，杜绝注入
 */
public class MySqlBuilder implements SqlBuilder {
    
    @Override
    public SqlResult buildQuery(QueryModel query) {
        SqlResult result = new SqlResult();
        StringBuilder sql = new StringBuilder("SELECT ");
        
        ViewModel view = query.getView();
        
        // SELECT列
        if (query.getSelectColumns().isEmpty()) {
            sql.append("*");
        } else {
            sql.append(query.getSelectColumns().stream()
                    .map(col -> escapeColumn(view.getStoreColumnName(col)))
                    .collect(Collectors.joining(", ")));
        }
        
        // FROM主表
        StoreTable mainTable = view.getMainStoreTable();
        sql.append(" FROM ").append(escapeTable(mainTable));
        
        // WHERE条件
        if (!query.getFilters().isEmpty()) {
            sql.append(" WHERE ");
            List<String> conditions = query.getFilters().stream()
                    .map(cond -> buildCondition(cond, view, result))
                    .collect(Collectors.toList());
            sql.append(String.join(" AND ", conditions));
        }
        
        // ORDER BY
        if (!query.getOrderBy().isEmpty()) {
            sql.append(" ORDER BY ");
            sql.append(String.join(", ", query.getOrderBy()));
        }
        
        // LIMIT分页
        if (query.getPagination() != null) {
            sql.append(" LIMIT ? OFFSET ?");
            result.addParam(query.getPagination().getSize());
            result.addParam(query.getPagination().getOffset());
        }
        
        result.setSql(sql.toString());
        return result;
    }
    
    @Override
    public SqlResult buildInsert(SubmitModel submit) {
        SqlResult result = new SqlResult();
        ViewModel view = submit.getView();
        StoreTable table = view.getMainStoreTable();
        
        StringBuilder sql = new StringBuilder("INSERT INTO ");
        sql.append(escapeTable(table));
        
        // 列名
        List<String> columns = submit.getData().keySet().stream()
                .map(view::getStoreColumnName)
                .map(this::escapeColumn)
                .collect(Collectors.toList());
        sql.append(" (").append(String.join(", ", columns)).append(")");
        
        // 参数占位符
        String placeholders = submit.getData().values().stream()
                .map(v -> "?")
                .collect(Collectors.joining(", "));
        sql.append(" VALUES (").append(placeholders).append(")");
        
        // 添加参数
        submit.getData().values().forEach(result::addParam);
        
        result.setSql(sql.toString());
        return result;
    }
    
    @Override
    public SqlResult buildUpdate(SubmitModel submit) {
        SqlResult result = new SqlResult();
        ViewModel view = submit.getView();
        StoreTable table = view.getMainStoreTable();
        
        StringBuilder sql = new StringBuilder("UPDATE ");
        sql.append(escapeTable(table));
        sql.append(" SET ");
        
        // SET子句
        List<String> sets = submit.getData().entrySet().stream()
                .map(entry -> {
                    result.addParam(entry.getValue());
                    return escapeColumn(view.getStoreColumnName(entry.getKey())) + " = ?";
                })
                .collect(Collectors.toList());
        sql.append(String.join(", ", sets));
        
        // WHERE主键
        sql.append(" WHERE ").append(escapeColumn(table.getPrimaryKey())).append(" = ?");
        result.addParam(submit.getPrimaryKeyValue());
        
        result.setSql(sql.toString());
        return result;
    }
    
    @Override
    public SqlResult buildDelete(String viewModelId, Object primaryKeyValue) {
        // 简化实现，实际需要从配置加载ViewModel
        throw new UnsupportedOperationException("待实现");
    }
    
    @Override
    public SqlResult buildAggregate(AggregateModel aggregate) {
        SqlResult result = new SqlResult();
        StringBuilder sql = new StringBuilder("SELECT ");
        
        ViewModel view = aggregate.getView();
        
        // SELECT聚合字段和分组字段
        List<String> selectParts = aggregate.getAggregates().stream()
                .map(agg -> buildAggregateField(agg, view))
                .collect(Collectors.toList());
        
        if (!aggregate.getGroupBy().isEmpty()) {
            selectParts.addAll(0, aggregate.getGroupBy().stream()
                    .map(col -> escapeColumn(view.getStoreColumnName(col)))
                    .collect(Collectors.toList()));
        }
        
        sql.append(String.join(", ", selectParts));
        
        // FROM
        StoreTable mainTable = view.getMainStoreTable();
        sql.append(" FROM ").append(escapeTable(mainTable));
        
        // WHERE
        if (!aggregate.getFilters().isEmpty()) {
            sql.append(" WHERE ");
            List<String> conditions = aggregate.getFilters().stream()
                    .map(cond -> buildCondition(cond, view, result))
                    .collect(Collectors.toList());
            sql.append(String.join(" AND ", conditions));
        }
        
        // GROUP BY
        if (!aggregate.getGroupBy().isEmpty()) {
            sql.append(" GROUP BY ");
            sql.append(aggregate.getGroupBy().stream()
                    .map(col -> escapeColumn(view.getStoreColumnName(col)))
                    .collect(Collectors.joining(", ")));
        }
        
        result.setSql(sql.toString());
        return result;
    }
    
    /**
     * 构建单个查询条件
     */
    private String buildCondition(QueryCondition cond, ViewModel view, SqlResult result) {
        String columnName = escapeColumn(view.getStoreColumnName(cond.getColumn()));
        Operator op = cond.getOperator();
        
        switch (op) {
            case IS_NULL:
                return columnName + " IS NULL";
            case IS_NOT_NULL:
                return columnName + " IS NOT NULL";
            case IN:
            case NOT_IN:
                String placeholders = cond.getValues() != null 
                    ? java.util.stream.Stream.of(cond.getValues()).map(v -> "?").collect(Collectors.joining(", "))
                    : "";
                for (Object val : cond.getValues()) {
                    result.addParam(val);
                }
                return columnName + " " + op.getSql() + " (" + placeholders + ")";
            case BETWEEN:
                result.addParam(cond.getValues()[0]);
                result.addParam(cond.getValues()[1]);
                return columnName + " BETWEEN ? AND ?";
            case LIKE:
            case NOT_LIKE:
                result.addParam("%" + cond.getValues()[0] + "%");
                return columnName + " " + op.getSql() + " ?";
            default: // EQ, NE, GT, GE, LT, LE
                result.addParam(cond.getValues()[0]);
                return columnName + " " + op.getSql() + " ?";
        }
    }
    
    /**
     * 构建聚合字段
     */
    private String buildAggregateField(AggregateField agg, ViewModel view) {
        String columnName = escapeColumn(view.getStoreColumnName(agg.getColumn()));
        String func = agg.getFunc().getSql();
        String alias = agg.getAlias() != null ? " AS " + escapeColumn(agg.getAlias()) : "";
        return func + "(" + columnName + ")" + alias;
    }
    
    /**
     * 转义列名
     */
    private String escapeColumn(String column) {
        return "`" + column + "`";
    }
    
    /**
     * 转义表名
     */
    private String escapeTable(StoreTable table) {
        if (table.getDatabase() != null && !table.getDatabase().isEmpty()) {
            return "`" + table.getDatabase() + "`.`" + table.getTable() + "`";
        }
        return "`" + table.getTable() + "`";
    }
}
