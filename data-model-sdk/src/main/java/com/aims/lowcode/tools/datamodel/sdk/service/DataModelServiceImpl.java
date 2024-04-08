package com.aims.lowcode.tools.datamodel.sdk.service;

import com.aims.lowcode.tools.datamodel.core.dsl.DataView;
import com.aims.lowcode.tools.datamodel.core.sqlbuilder.input.QueryInput;
import com.aims.lowcode.tools.datamodel.sdk.AppConfig;
import com.aims.lowcode.tools.datamodel.sdk.utils.FileUtil;
import com.alibaba.fastjson2.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class DataModelServiceImpl {
    @Autowired
    private AppConfig appConfig;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public JSONObject getDataModel(String dataModelId) {
        return FileUtil.readJsonFile(appConfig.getDATA_MODEL_DIR(), dataModelId + ".json");
    }

    public List<Map<String, Object>> query(String dataModelId, String queryJson) {
        var dm = getDataModel(dataModelId);
        DataView dv = dm.toJavaObject(DataView.class);
        var query = JSONObject.parseObject(queryJson, QueryInput.class);
        query.setFrom(dv);
        return queryByInput(dataModelId, query);
    }

    public List<Map<String, Object>> queryByInput(String dataModelId, QueryInput queryInput) {
        var dm = getDataModel(dataModelId);
        DataView dv = dm.toJavaObject(DataView.class);
        queryInput.setFrom(dv);
        var sql = queryInput.buildSql();
        return jdbcTemplate.queryForList(sql);
    }
}
