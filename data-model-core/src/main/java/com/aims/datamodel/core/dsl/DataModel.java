package com.aims.datamodel.core.dsl;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Accessors(chain = true)
@Data
public class DataModel {
    private String mainTable;
    private List<DataModelColumn> columns;
    private List<DataViewCondition> conditions;
    private List<DataViewJoin> joins;
    private LinkedHashMap<String, TableAliasMap> tableMap = new LinkedHashMap<>();
    private LinkedHashMap<String, ColumnAliasMap> columnMap = new LinkedHashMap<>();

    public String findPrimaryKey() {
        var mapValue = tableMap.get(this.mainTable);
        if (mapValue == null) return "id";
        return mapValue.findPrimaryKey();
    }

    public String buildPrimaryKeySql() {
        return "m." + this.findPrimaryKey();
    }

    public String buildColumnSqlWithTable(String column) {
        String sql = buildColumnSql(column);
        var mapValue = columnMap.get(column);
        if (mapValue != null && StringUtils.hasText(mapValue.getTable())) {
            sql = findTableAlias(mapValue.getTable()) + "." + sql;
        }
        return sql;
    }

    public String buildColumnSql(String column) {
        var columnName = column;
        String sql = String.format("`%s`", columnName);
        var mapValue = columnMap.get(column);
        if (mapValue != null) {
            columnName = mapValue.getStoreColumn() == null ? column : mapValue.getStoreColumn();
            sql = String.format("`%s`", columnName);
        }
        return sql;
    }

    public String findStoreColumnName(String column) {
        var columnName = column;
        var mapValue = columnMap.get(column);
        if (columnMap != null && mapValue != null) {
            columnName = mapValue.getStoreColumn() == null ? column : mapValue.getStoreColumn();
        }
        return columnName;
    }

    /**
     * 返回包含反引号的列名，如`type`
     *
     * @param column
     * @return
     */
    public String findStoreColumnSqlName(String column) {
        return String.format("`%s`", findStoreColumnName(column));
    }

    public String buildTableSql(String alias) {
        if (tableMap == null) return alias;
        var tableName = alias;
        var mapValue = tableMap.get(alias);
        if (mapValue != null) {
            tableName = mapValue.getStoreTable() == null ? alias : mapValue.getStoreTable();
            if (StringUtils.hasText(mapValue.getStoreDatabase()))
                return "`" + mapValue.getStoreDatabase() + "`.`" + mapValue.getStoreTable() + "`";
        }
        return "`" + tableName + "`";
    }

    public String findTableAlias(String table) {
        if (table.equals(this.mainTable))
            return "m";
        return table;
    }
}
