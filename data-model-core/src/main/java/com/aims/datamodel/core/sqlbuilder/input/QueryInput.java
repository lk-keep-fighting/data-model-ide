package com.aims.datamodel.core.sqlbuilder.input;

import com.aims.datamodel.core.dsl.DataModel;
import com.aims.datamodel.core.dsl.DataViewAliasMap;
import com.aims.datamodel.core.dsl.DataViewCondition;
import com.aims.datamodel.core.dsl.DataViewJoin;
import lombok.Data;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

@Data
public class QueryInput {

    private boolean distinct;
    private List<Column> selects;
    private DataModel from;
    private List<DataViewCondition> conditions;
    private long page;
    private long pageSize;
    private GroupBy groupBy;
    private Having having;
    private OrderBy orderBy;

    public String buildSql() {
        StringBuilder sql = new StringBuilder("SELECT ");
        if (distinct) {
            sql.append(" DISTINCT ");
        }
        DataViewAliasMap aliasMap = from.getAliasMap();

        sql.append(buildColumnsSql(aliasMap)).append(" ");
        sql.append(" FROM ").append(from.getMainTable()).append(" as m");

        if (from.getJoins() != null) {
            for (DataViewJoin join : from.getJoins()) {
                sql.append(join.buildJoinSql(aliasMap)).append(" ");
            }
        }
        sql.append( " WHERE 1=1 ");
        if (from.getConditions() != null && !from.getConditions().isEmpty()) {
            sql.append(" AND (").append(buildMainConditionsSql(from.getConditions())).append(") ");
        }

        if (conditions != null && !conditions.isEmpty()) {
            sql.append(" AND (").append(buildMainConditionsSql(conditions)).append(") ");
        }


        if (groupBy != null && !groupBy.getColumns().isEmpty()) {
            sql.append(" GROUP BY ").append(groupBy.buildGroupBySql()).append(" ");
        }

        if (having != null && !having.getConditions().isEmpty()) {
            sql.append(" HAVING ").append(having.generateHavingSql(from.getAliasMap())).append(" ");
        }

        if (orderBy != null && !orderBy.getColumns().isEmpty()) {
            sql.append(orderBy.buildOrderBySql(aliasMap)).append(" ");
        }

        // 添加分页逻辑
        if (page != 0) {
            long offset = (page - 1) * pageSize;
            sql.append(" LIMIT ").append(pageSize).append(" OFFSET ").append(offset);
        }

        return sql.toString();
    }

    public String buildMainConditionsSql(List<DataViewCondition> conditions) {
        StringBuilder sql = new StringBuilder();
        for (var condition : conditions) {
            sql.append(condition.buildConditionSql(from.getAliasMap()) + " AND ");
        }
        return sql.substring(0, sql.length() - 5);
    }

    public String buildColumnsSql(DataViewAliasMap aliasMap) {
        if (selects == null || selects.isEmpty()) return " * ";
        var sql = selects.stream().map(column -> aliasMap.getColumnSql(column.getColumn()) + " AS " + column.getColumn()).collect(Collectors.joining(", "));
        if (StringUtils.hasText(sql)) {
            return sql;
        } else return " * ";
    }
}

