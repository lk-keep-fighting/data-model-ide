package com.aims.lowcode.tools.datamodel.core.sqlbuilder.input;

import com.aims.lowcode.tools.datamodel.core.dsl.DataViewAliasMap;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

@Data
public class OrderBy {
    private List<OrderByColumn> columns;

    public String buildOrderBySql(DataViewAliasMap aliasMap) {
        return "ORDER BY " + columns.stream().map(column -> getColumnSqlByAlias(aliasMap, column.getColumn()) + " " + (column.getDirection() != null && column.getDirection().equalsIgnoreCase("asc") ? "ASC" : "DESC")).collect(Collectors.joining(", "));
    }

    public String getColumnSqlByAlias(DataViewAliasMap aliasMap, String alias) {
        var column = aliasMap.getColumnMap(alias);
        String sql = alias;
        if (column.isPresent()) {
            sql = column.get().getFullSqlName();
        }
        return sql;
    }

}
