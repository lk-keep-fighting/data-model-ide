package com.aims.datamodel.core.sqlbuilder.input;

import com.aims.datamodel.core.dsl.DataModel;
import com.aims.datamodel.core.dsl.DataViewAliasMap;
import com.aims.datamodel.core.dsl.DataViewCondition;
import com.aims.datamodel.core.dsl.DataViewJoin;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Accessors(chain = true)
public class QueryInput {

    private boolean distinct;
    private List<String> ids;
    private List<Column> selects;
    private DataModel from;
    private List<DataViewCondition> conditions;
    private long page;
    private long pageSize;
    private GroupBy groupBy;
    private Having having;
    private OrderBy orderBy;

    public String buildPageSql() {
        StringBuilder sql = new StringBuilder("SELECT ");
        if (distinct) {
            sql.append(" DISTINCT ");
        }
        sql.append(buildColumnsSql(from)).append(" ");
        sql.append(" FROM ").append(from.buildTableSql(from.getMainTable())).append(" as m");

        if (from.getJoins() != null) {
            for (DataViewJoin join : from.getJoins()) {
                sql.append(join.buildJoinSql(from)).append(" ");
            }
        }
        sql.append(" WHERE 1=1 ");
        if (from.getConditions() != null && !from.getConditions().isEmpty()) {
            sql.append(" AND (").append(buildMainConditionsSql(from.getConditions())).append(") ");
        }
        if (ids != null && !ids.isEmpty()) {
            sql.append(" AND " + from.buildPrimaryKeySql() + " IN (").append(ids.stream().map(id -> "'" + id + "'").collect(Collectors.joining(","))).append(") ");
        }
        if (conditions != null && !conditions.isEmpty()) {
            sql.append(" AND (").append(buildMainConditionsSql(conditions)).append(") ");
        }


        if (groupBy != null && !groupBy.getColumns().isEmpty()) {
            sql.append(groupBy.buildGroupBySql()).append(" ");
        }

        if (having != null && !having.getConditions().isEmpty()) {
            sql.append(having.generateHavingSql(from)).append(" ");
        }

        if (orderBy != null && !orderBy.getColumns().isEmpty()) {
            sql.append(orderBy.buildOrderBySql(from)).append(" ");
        }

        // 添加分页逻辑
        if (page != 0) {
            long offset = (page - 1) * pageSize;
            sql.append(" LIMIT ").append(pageSize).append(" OFFSET ").append(offset);
        }

        return sql.toString();
    }

    public String convertPageSqlToCountSql(String pageSql) {
        StringBuilder countSql = new StringBuilder("SELECT COUNT(*) ");
        int limitIdx = pageSql.indexOf("LIMIT");
        if (limitIdx != -1) {//去除limit
            pageSql = pageSql.substring(0, limitIdx - 1);
        }
        int orderByIdx = pageSql.indexOf("ORDER BY");
        if (orderByIdx != -1) {//去除order by
            pageSql = pageSql.substring(0, orderByIdx - 1);
        }
        if (pageSql.toUpperCase().contains("GROUP BY")) {
            countSql.append("FROM (").append(pageSql).append(") AS t");
        } else {
            int fromIdx = pageSql.indexOf("FROM");
            countSql.append(pageSql.substring(fromIdx));
        }
        return countSql.toString();
    }


    public String buildMainConditionsSql(List<DataViewCondition> conditions) {
        StringBuilder sql = new StringBuilder();
        for (var condition : conditions) {
            sql.append(condition.buildConditionSql(from) + " AND ");
        }
        return sql.substring(0, sql.length() - 5);
    }

    public String buildColumnsSql(DataModel dm) {
        var dmClms = from.getColumns();
        if (selects == null || selects.isEmpty()) {
            if (dmClms == null)
                return " * ";
            else {
                if (selects == null)
                    selects = new ArrayList<>();
                dmClms.forEach(dmClm -> {
                    selects.add(new Column(dmClm.getColumn()));
                });
            }
        }
        var sql = selects.stream().map(column -> dm.buildColumnSqlWithTable(column.getColumn()) + " AS `" + column.getColumn() + "`").collect(Collectors.joining(", "));
        if (StringUtils.hasText(sql)) {
            return sql;
        } else return " * ";
    }
}

