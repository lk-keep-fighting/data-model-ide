package com.aims.lowcode.tools.datamodel.core.dsl;

import lombok.Data;

import java.util.List;

@Data
public class DataViewJoin {
    private String type;
    private String table;
    private List<DataViewJoinCondition> on;

    public String buildJoinSql(DataViewAliasMap aliasMap) {
        String tableSql = aliasMap.getTableSql(table);
        String col1Sql =aliasMap.getColumnSql(on.get(0).getColumn1());
        String col2Sql =aliasMap.getColumnSql(on.get(0).getColumn2());
        return " " + type + " JOIN " + tableSql + " " + table + " ON " + col1Sql + " = " + col2Sql;
    }


}
