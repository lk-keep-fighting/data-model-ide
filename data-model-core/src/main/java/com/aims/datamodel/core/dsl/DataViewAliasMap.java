package com.aims.datamodel.core.dsl;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public class DataViewAliasMap {
    @Setter
    @Getter
    private List<DataViewTableAliasMap> tableMaps;
    @Setter
    @Getter
    private List<DataViewColumnAliasMap> columnMaps;

//    public Optional<DataViewTableAliasMap> getTableMap(String alias) {
//        return tableMaps.stream().filter(m -> m.getAlias().equals(alias)).findFirst();
//    }
//
//    public Optional<DataViewColumnAliasMap> getColumnMap(String alias) {
//        return columnMaps.stream().filter(m -> m.getAlias().equals(alias)).findFirst();
//    }

    public String findTablePrimaryKey(String alias) {
        if (tableMaps == null) return "id";
        Optional<DataViewTableAliasMap> tableMap = tableMaps.stream().filter(m -> alias.equals(m.getAlias())).findFirst();
        if (tableMap.isPresent()) {
            return tableMap.get().findPrimaryKey();
        } else {
            return "id";
        }
    }

    public String buildTableSql(String alias) {
        if (tableMaps == null) return alias;
        Optional<DataViewTableAliasMap> tableMap = tableMaps.stream().filter(m -> alias.equals(m.getAlias())).findFirst();
        if (tableMap.isPresent()) {
            return tableMap.get().buildFullSqlName();
        } else {
            return alias;
        }
    }

    public String buildColumnSql(String alias) {
        if (columnMaps == null) return "`" + alias + "`";
        Optional<DataViewColumnAliasMap> columnMap = columnMaps.stream().filter(m -> m.getAlias().equals(alias)).findFirst();
        if (columnMap.isPresent()) {
            return columnMap.get().buildFullSqlName();
        } else {
            return "`" + alias + "`";
        }
    }
}
