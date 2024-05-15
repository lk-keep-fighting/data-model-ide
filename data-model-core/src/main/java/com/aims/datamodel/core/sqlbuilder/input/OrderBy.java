package com.aims.datamodel.core.sqlbuilder.input;

import com.aims.datamodel.core.dsl.DataModel;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

@Data
public class OrderBy {
    private List<OrderByColumn> columns;

    public String buildOrderBySql(DataModel dm) {
        return "ORDER BY " + columns.stream().map(column -> dm.buildColumnSqlWithTable(column.getColumn()) + " " + (column.getDirection() != null && column.getDirection().equalsIgnoreCase("asc") ? "ASC" : "DESC")).collect(Collectors.joining(", "));
    }
}
