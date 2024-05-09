package com.aims.datamodel.core.dsl;

import lombok.Data;

import java.util.List;
import java.util.Optional;

@Data
public class DataViewAliasMap {
    private List<DataViewTableAliasMap> tableMaps;
    private List<DataViewColumnAliasMap> columnMaps;

    public Optional<DataViewTableAliasMap> getTableMap(String alias) {
        return tableMaps.stream().filter(m -> m.getAlias().equals(alias)).findFirst();
    }

    public Optional<DataViewColumnAliasMap> getColumnMap(String alias) {
        return columnMaps.stream().filter(m -> m.getAlias().equals(alias)).findFirst();
    }

    public String getTablePrimaryKey(String alias) {
        if (tableMaps == null) return "id";
        Optional<DataViewTableAliasMap> tableMap = tableMaps.stream().filter(m -> m.getAlias().equals(alias)).findFirst();
        if (tableMap.isPresent()) {
            return tableMap.get().getPrimaryKey();
        } else {
            return alias + "id";
        }
    }

    public String getTableSql(String alias) {
        if (tableMaps == null) return alias;
        Optional<DataViewTableAliasMap> tableMap = tableMaps.stream().filter(m -> m.getAlias().equals(alias)).findFirst();
        if (tableMap.isPresent()) {
            return tableMap.get().getFullSqlName();
        } else {
            return alias;
        }
    }

    public String getColumnSql(String alias) {
        if (columnMaps == null)  return "`" + alias + "`";
        Optional<DataViewColumnAliasMap> columnMap = columnMaps.stream().filter(m -> m.getAlias().equals(alias)).findFirst();
        if (columnMap.isPresent()) {
            return columnMap.get().getFullSqlName();
        } else {
            return "`" + alias + "`";
        }
    }
}
