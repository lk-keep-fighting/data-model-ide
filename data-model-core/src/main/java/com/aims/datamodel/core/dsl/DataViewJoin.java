package com.aims.datamodel.core.dsl;

import lombok.Data;

import java.util.List;

@Data
public class DataViewJoin {
    private String type;
    private String table;
    private List<DataViewJoinCondition> on;

    public String buildJoinSql(DataModel dm) {
        String tableSql = dm.buildTableSql(table);
        String col1Sql = dm.buildColumnSqlWithTable(on.get(0).getColumn1());
        String col2Sql =dm.buildColumnSqlWithTable(on.get(0).getColumn2());
        return " " + type + " JOIN " + tableSql + " " + table + " ON " + col1Sql + " = " + col2Sql;
    }
}
