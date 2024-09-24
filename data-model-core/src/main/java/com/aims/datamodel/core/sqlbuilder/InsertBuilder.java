package com.aims.datamodel.core.sqlbuilder;

import com.aims.datamodel.core.dsl.DataModel;
import com.aims.datamodel.core.dsl.DataModelColumn;
import com.aims.datamodel.core.sqlbuilder.input.InsertInput;
import com.alibaba.fastjson2.JSONObject;

import java.util.Collections;
import java.util.List;
import java.util.StringJoiner;
import java.util.stream.Collectors;

public class InsertBuilder {
    public static String buildByInput(InsertInput input) {
        StringBuilder sb = new StringBuilder();
        if (input.getDataModel() == null)
            throw new RuntimeException("dataModel is null");
        if (input.getValue() == null)
            throw new RuntimeException("value is null");
        DataModel dataModel = input.getDataModel();
        sb.append(buildOneInsertSql(dataModel, input.getValue()));
        return sb.toString();
    }
//
//    public static String buildParameterSqlByInput(InsertInput input) {
//        StringBuilder sb = new StringBuilder();
//        if (input.getDataModel() == null)
//            throw new RuntimeException("dataModel is null");
//        if (input.getValue() == null)
//            throw new RuntimeException("value is null");
//        DataModel dataModel = input.getDataModel();
//        sb.append(buildOneParameterInsertSql(dataModel, input.getValue()));
//        return sb.toString();
//    }

//    public static List<String> buildBatchByInput(InsertInput input) {
//        if (input.getDataModel() == null)
//            throw new RuntimeException("dataModel is null");
//        if (input.getValues() == null)
//            throw new RuntimeException("values is null");
//        DataModel dataModel = input.getDataModel();
//        return input.getValues().stream().map(value -> buildOneInsertSql(dataModel, JSONObject.from(value))).collect(Collectors.toList());
//    }

//    public static String buildParamBatchInsertSqlByInput(InsertInput input) {
//        StringBuilder sb = new StringBuilder();
//        var dataModel = input.getDataModel();
//        sb.append("insert into ").append(dataModel.getMainTable()).append("(");
//        if (input.getValues() == null) return "";
//        var values = input.getValues();
//        var insertColumns = JSONObject.from(input.getValues().get(0)).keySet().stream().map(dataModel::findStoreColumnName).collect(Collectors.joining(","));
//        sb.append(insertColumns).append(") values");
//        String placeHolders = String.join(",", Collections.nCopies(values.size(), "(" + String.join(",", Collections.nCopies(insertColumns.split(",").length, "?")) + ")"));
//        sb.append(placeHolders);
//
//        // 将构建好的SQL语句返回
//        return sb.toString();
//    }

    public static String buildBatchInsertSqlByInput(InsertInput input) {
        StringBuilder sb = new StringBuilder();
        var dataModel = input.getDataModel();
        sb.append("insert into ").append(dataModel.buildTableSql(dataModel.getMainTable())).append("(");
        if (input.getValues() == null) return "";
        var values = input.getValues();
        List<String> columnNames;
        if (dataModel.getColumns() != null && dataModel.getColumns().size() > 0) {
            columnNames = dataModel.getColumns().stream()
                    .map(DataModelColumn::findStoreColumnName)
                    .collect(Collectors.toList());
        } else {
            columnNames = JSONObject.from(values.get(0)).keySet().stream()
                    .map(dataModel::findStoreColumnName)
                    .collect(Collectors.toList());
        }
        columnNames.forEach(column -> sb.append(dataModel.findStoreColumnSqlName(column)).append(","));
        sb.delete(sb.length() - 1, sb.length());
        sb.append(") VALUES ");
        StringJoiner valuesJoiner = new StringJoiner(", ");
        for (var value : values) {
            var jsonValue = JSONObject.from(value);
            List<String> valueStrs = columnNames.stream()
                    .map(column -> {
                        String v = jsonValue.getString(column);
                        if (v == null)
                            return "NULL";
                        else
//                            return "'" + v + "'";
                            // 确保字符串值被引号包围，并且转义单引号
                            return "'" + v.replace("'", "''").replace("\\", "\\\\") + "'";
                    })
                    .collect(Collectors.toList());
            valuesJoiner.add("(" + String.join(", ", valueStrs) + ")");
        }
        sb.append(valuesJoiner);
        return sb.toString();
    }

    private static String buildOneInsertSql(DataModel dataModel, JSONObject inputJson) {
        StringBuilder sb = new StringBuilder();
        sb.append("insert into ").append(dataModel.buildTableSql(dataModel.getMainTable())).append("(");
        if (inputJson == null) return "";
        var insertColumns = inputJson.keySet().stream().map(dataModel::findStoreColumnSqlName).collect(Collectors.joining(","));
        sb.append(insertColumns).append(") values(");
        inputJson.forEach((key, value) -> {
            sb.append("'").append(value == null ? null : value.toString().replace("'", "''").replace("\\", "\\\\")).append("',");
        });
        sb.deleteCharAt(sb.length() - 1);
        sb.append(")");
        return sb.toString();
    }

//    private static String buildOneParameterInsertSql(DataModel dataModel, JSONObject inputJson) {
//        StringBuilder sb = new StringBuilder();
//        sb.append("insert into ").append(dataModel.getMainTable()).append("(`");
//        if (inputJson == null) return "";
//        var insertColumns = String.join("`,`", inputJson.keySet());
//        sb.append(insertColumns).append("`) values(");
//        inputJson.forEach((key, value) -> {
//            sb.append("?,");
//        });
//        sb.deleteCharAt(sb.length() - 1);
//        sb.append(")");
//        return sb.toString();
//    }
}
