package com.aims.datamodel.core.dsl;

import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.util.StringUtils;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

@Accessors(chain = true)
@Data
public class DataModel {
    private String id;
    private String name;
    private String type;
    private String group;
    private String memo;
    private String mainTable;
    private String version;
    private List<DataModelColumn> columns;
    private List<DataViewCondition> conditions;
    private List<DataViewJoin> joins;
    private LinkedHashMap<String, TableAliasMap> tableMap = new LinkedHashMap<>();
    private LinkedHashMap<String, ColumnAliasMap> columnMap = new LinkedHashMap<>();

    public String findPrimaryKey() {
        var mapValue = tableMap.get(this.mainTable);
        if (mapValue != null && mapValue.getPrimaryKey() != null)
            return mapValue.getPrimaryKey();
        AtomicReference<String> key = new AtomicReference<>();
        if (!columnMap.isEmpty()) {
            columnMap.forEach((k, v) -> {
                if (v.isStoreIsPrimaryKey()) {
                    key.set(v.getStoreColumn());
                }
            });
        }
        if (key.get() != null) {
            return key.get();
        }
        return "id";
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

    public ColumnAliasMap findColumnAliasMap(String column) {
        var mapValue = columnMap.get(column);
        if (mapValue != null) {
            return mapValue;
        }
        return null;
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
        if (tableName.startsWith("("))//子查询
            return tableName;
        return "`" + tableName + "`";
    }

    public String findTableAlias(String table) {
        if (table.equals(this.mainTable))
            return "m";
        return table;
    }
}
