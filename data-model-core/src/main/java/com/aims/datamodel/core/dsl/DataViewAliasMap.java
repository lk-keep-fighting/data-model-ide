package com.aims.datamodel.core.dsl;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

public class DataViewAliasMap {
    @Setter
    @Getter
    private List<TableAliasMap> tableMaps;
    @Setter
    @Getter
    private List<ColumnAliasMap> columnMaps;

//    public Optional<DataViewTableAliasMap> getTableMap(String alias) {
//        return tableMaps.stream().filter(m -> m.getAlias().equals(alias)).findFirst();
//    }
//
//    public Optional<DataViewColumnAliasMap> getColumnMap(String alias) {
//        return columnMaps.stream().filter(m -> m.getAlias().equals(alias)).findFirst();
//    }


//    public String buildTableSql(String alias) {
//        if (tableMaps == null) return alias;
//        Optional<DataViewTableAliasMap> tableMap = tableMaps.stream().filter(m -> alias.equals(m.getTable())).findFirst();
//        if (tableMap.isPresent()) {
//            return tableMap.get().buildSqlWithDb();
//        } else {
//            return alias;
//        }
//    }

//    public String buildColumnSql(String alias) {
//        if (columnMaps == null) return "`" + alias + "`";
//        Optional<DataViewColumnAliasMap> columnMap = columnMaps.stream().filter(m -> m.getAlias().equals(alias)).findFirst();
//        if (columnMap.isPresent()) {
//            return columnMap.get().buildColumnFullSqlName();
//        } else {
//            return "`" + alias + "`";
//        }
//    }
}
