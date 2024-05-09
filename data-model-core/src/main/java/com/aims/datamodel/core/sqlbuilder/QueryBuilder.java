package com.aims.datamodel.core.sqlbuilder;

import com.aims.datamodel.core.sqlbuilder.input.QueryInput;
import com.alibaba.fastjson2.JSONObject;
import lombok.Data;

@Data
public class QueryBuilder {
    public QueryBuilder() {
    }

    public static String build(QueryInput input) {
        return input.buildPageSql();
    }

    public static String buildByJson(String inputJson) {
        QueryInput input = JSONObject.parseObject(inputJson, QueryInput.class);
        return input.buildPageSql();
    }
}
