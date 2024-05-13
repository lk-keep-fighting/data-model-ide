package com.aims.datamodel.core.dsl;

import lombok.Data;

import java.util.List;

@Data
public class DataViewJoin {
    private String type;
    private String table;
    private List<DataViewJoinCondition> on;

    public String buildJoinSql(DataViewAliasMap aliasMap) {
        String tableSql = aliasMap.buildTableSql(table);
        String col1Sql =aliasMap.buildColumnSql(on.get(0).getColumn1());
        String col2Sql =aliasMap.buildColumnSql(on.get(0).getColumn2());
        return " " + type + " JOIN " + tableSql + " " + table + " ON " + col1Sql + " = " + col2Sql;
    }


}
