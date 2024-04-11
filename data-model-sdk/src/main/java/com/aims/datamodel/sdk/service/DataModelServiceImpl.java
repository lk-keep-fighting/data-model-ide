package com.aims.datamodel.sdk.service;

import com.aims.datamodel.core.dsl.DataModel;
import com.aims.datamodel.core.sqlbuilder.InsertBuilder;
import com.aims.datamodel.core.sqlbuilder.input.InsertInput;
import com.aims.datamodel.core.sqlbuilder.input.QueryInput;
import com.aims.datamodel.sdk.utils.FileUtil;
import com.aims.datamodel.sdk.AppConfig;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class DataModelServiceImpl {
    @Autowired
    private AppConfig appConfig;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public JSONObject getDataModel(String dataModelId) {
        JSONObject defDataModel = new JSONObject();
        defDataModel.put("mainTable", dataModelId);
        return FileUtil.readOrCreateFile(appConfig.getDATA_MODEL_DIR(), dataModelId + ".json", defDataModel.toJSONString());
    }

    public List<Map<String, Object>> query(String dataModelId, String queryJson) {
        var dm = getDataModel(dataModelId);
        DataModel dv = dm.toJavaObject(DataModel.class);
        var query = JSONObject.parseObject(queryJson, QueryInput.class);
        query.setFrom(dv);
        return queryByInput(dataModelId, query);
    }

    public List<Map<String, Object>> queryByInput(String dataModelId, QueryInput queryInput) {
        var dm = getDataModel(dataModelId);
        DataModel dv = dm.toJavaObject(DataModel.class);
        queryInput.setFrom(dv);
        var sql = queryInput.buildSql();
        log.debug("query-sql {}", sql);
        return jdbcTemplate.queryForList(sql);
    }

    public long insertByJson(String dataModelId, String value) {
        var dm = getDataModel(dataModelId);
        InsertInput input = new InsertInput();
        input.setDataModel(dm.toJavaObject(DataModel.class));
        input.setValue(JSONObject.parse(value));
        var sql = InsertBuilder.buildByInput(input);
        log.debug("insert-sql: {}", sql);
        return jdbcTemplate.update(sql);
    }

    public long insertBatchByJson(String dataModelId, String values) {
        var dm = getDataModel(dataModelId);
        InsertInput input = new InsertInput();
        input.setDataModel(dm.toJavaObject(DataModel.class));
        input.setValues(JSONArray.parse(values));
        var sql = InsertBuilder.buildBatchByInput(input);
        log.debug("batch-insert-sql: {}", sql);
        var res = jdbcTemplate.batchUpdate(sql.toArray(new String[0]));
        return res.length;
    }

    public void executeSql(String sql) {
        log.debug("execute-sql: {}", sql);
        jdbcTemplate.execute(sql);
    }
}
