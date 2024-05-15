package com.aims.datamodel.core.dsl;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;
import java.util.stream.Collectors;

@Data
@Accessors(chain = true)
public class DataViewCondition {
    private String column;
    private String operator = "=";
    private Object value;
    private List<Object> values;
    private List<DataViewCondition> conditions;
    private String logic;

    public DataViewCondition(String alias, String operator, String value) {
        this.column = alias;
        this.operator = operator;
        this.value = value;
    }

    /**
     * 构造条件,默认操作符为=
     *
     * @param alias
     * @param value
     */
    public DataViewCondition(String alias, String value) {
        this.column = alias;
        this.value = value;
    }

    public DataViewCondition() {

    }

    public String buildConditionSql(DataModel dm) {
        String sql = dm.buildColumnSqlWithTable(column) + " ";

        operator = operator.toUpperCase();
        if ("NOT LIKE".equals(operator)) {
            sql += " NOT LIKE '%" + value.toString() + "%'";
        } else if ("LIKE".equals(operator)) {
            sql += " LIKE '%" + value.toString() + "%'";
        } else if ("IN".equals(operator) || "NOT IN".equals(operator)) {
            sql += " IN (" + values.stream().map(Object::toString).collect(Collectors.joining(", ")) + ")";
        } else if ("BETWEEN".equals(operator)) {
            sql += " BETWEEN '" + values.get(0) + "' AND '" + values.get(1) + "'";
        } else if ("IS NULL".equals(operator)) {
            sql += " IS NULL";
        } else if ("IS NOT NULL".equals(operator)) {
            sql += " IS NOT NULL";
        } else if ("OR".equals(operator)) {
            if (conditions != null && !conditions.isEmpty()) {
                sql = " (";
                sql += conditions.stream().map(condition -> condition.buildConditionSql(dm)).collect(Collectors.joining(" OR "));
                sql += ")";
            }
        } else {
            sql += operator + " '" + value + "'";
        }
        return sql;
    }


}
