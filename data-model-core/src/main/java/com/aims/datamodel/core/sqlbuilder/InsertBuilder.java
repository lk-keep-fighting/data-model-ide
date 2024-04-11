package com.aims.datamodel.core.sqlbuilder;

import com.aims.datamodel.core.dsl.DataModel;
import com.aims.datamodel.core.sqlbuilder.input.InsertInput;
import com.alibaba.fastjson2.JSONObject;

import java.util.List;
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

    public static List<String> buildBatchByInput(InsertInput input) {
        if (input.getDataModel() == null)
            throw new RuntimeException("dataModel is null");
        if (input.getValues() == null)
            throw new RuntimeException("values is null");
        DataModel dataModel = input.getDataModel();
        return input.getValues().stream().map(value -> buildOneInsertSql(dataModel, JSONObject.from(value))).collect(Collectors.toList());
    }

    private static String buildOneInsertSql(DataModel dataModel, JSONObject inputJson) {
        StringBuilder sb = new StringBuilder();
        sb.append("insert into ").append(dataModel.getMainTable()).append("(`");
        if (inputJson == null) return "";
        var insertColumns = String.join("`,`", inputJson.keySet());
        sb.append(insertColumns).append("`) values(");
        inputJson.forEach((key, value) -> {
            sb.append("'").append(value).append("',");
        });
        sb.deleteCharAt(sb.length() - 1);
        sb.append(")");
        return sb.toString();
    }
}
