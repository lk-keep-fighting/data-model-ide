package com.aims.datamodel.sdk.service;

import com.aims.datamodel.core.dsl.DataModel;
import com.aims.datamodel.core.dsl.DataViewCondition;
import com.aims.datamodel.core.sqlbuilder.InsertBuilder;
import com.aims.datamodel.core.sqlbuilder.UpdateBuilder;
import com.aims.datamodel.core.sqlbuilder.input.InsertInput;
import com.aims.datamodel.core.sqlbuilder.input.QueryInput;
import com.aims.datamodel.core.sqlbuilder.input.UpdateInput;
import com.aims.datamodel.sdk.dto.PageResult;
import com.aims.datamodel.sdk.utils.FileUtil;
import com.aims.datamodel.sdk.AppConfig;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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

    public void saveDataModel(String dataModelId, JSONObject dataModel) throws Exception {
        FileUtil.writeFile(appConfig.getDATA_MODEL_DIR(), dataModelId + ".json", dataModel.toJSONString());
    }

    public Map<String, Object> queryById(String dataModelId, String id) {
        List<String> ids = new ArrayList<>();
        ids.add(id);
        var result = queryByIds(dataModelId, ids);
        return !result.isEmpty() ? result.get(0) : null;
    }

    public List<Map<String, Object>> queryByIds(String dataModelId, List<String> ids) {
        var dm = getDataModel(dataModelId);
        DataModel dv = dm.toJavaObject(DataModel.class);
        var query = new QueryInput();
        query.setIds(ids);
        query.setFrom(dv);
        return queryByInput(dataModelId, query);
    }

    public List<Map<String, Object>> query(String dataModelId, String queryJson) {
        var dm = getDataModel(dataModelId);
        DataModel dv = dm.toJavaObject(DataModel.class);
        var query = queryJson == null ? new QueryInput() : JSONObject.parseObject(queryJson, QueryInput.class);
        query.setFrom(dv);
        return queryByInput(dataModelId, query);
    }

    public List<Map<String, Object>> queryByInput(String dataModelId, QueryInput queryInput) {
        var dm = getDataModel(dataModelId);
        DataModel dv = dm.toJavaObject(DataModel.class);
        queryInput.setFrom(dv);
        queryInput.setPage(0);
        var sql = queryInput.buildPageSql();
        log.debug("query-sql {}", sql);
        return jdbcTemplate.queryForList(sql);
    }

    public List<Map<String, Object>> queryBySql(String sql) {
        log.info("接收到查询sql：{}", sql);
        sql = sql.replaceAll("\\s+", " ");//替换空格转义字符为空格，如\t,\n,\r等
        log.info("处理转义后sql：{}", sql);
        return jdbcTemplate.queryForList(sql);
    }

    public PageResult queryPageBySql(String sql, int page, long pageSize) {
        log.info("接收到查询sql：{}", sql);
        sql = sql.replaceAll("\\s+", " ");//替换空格转义字符为空格，如\t,\n,\r等
        log.info("处理转义后sql：{}", sql);
        if (page != 0) {
            long offset = (page - 1) * pageSize;
            sql += String.format(" LIMIT %s OFFSET %s", pageSize, offset);
        }
        int fromIdx = sql.indexOf("FROM");
        StringBuilder countSql = new StringBuilder("SELECT COUNT(*) ");
        countSql.append(sql.substring(fromIdx));
        int limitIdx = countSql.indexOf("LIMIT");
        if (limitIdx != -1) {
            countSql.delete(limitIdx, countSql.length());
        }
        log.debug("query-page-sql {}", sql);
        log.debug("query-page-countSql {}", countSql);
        var records = jdbcTemplate.queryForList(sql);
        var totalCount = jdbcTemplate.queryForObject(countSql.toString(), Long.class);
        return new PageResult(records, totalCount, page, pageSize);
    }

    public PageResult queryPageByInput(String dataModelId, QueryInput queryInput) {
        var dm = getDataModel(dataModelId);
        DataModel dv = dm.toJavaObject(DataModel.class);
        queryInput.setFrom(dv);
        var sql = queryInput.buildPageSql();
        var countSql = queryInput.convertPageSqlToCountSql(sql);
        log.debug("query-page-sql {}", sql);
        log.debug("query-page-countSql {}", countSql);
        var records = jdbcTemplate.queryForList(sql);
        var totalCount = jdbcTemplate.queryForObject(countSql, Long.class);
        return new PageResult(records, totalCount, queryInput.getPage(), queryInput.getPageSize());
    }

    public long insert(String dataModelId, String value) {
        var dm = getDataModel(dataModelId);
        InsertInput input = new InsertInput();
        input.setDataModel(dm.toJavaObject(DataModel.class));
        input.setValue(JSONObject.parse(value));
        var sql = InsertBuilder.buildByInput(input);
        log.debug("insert-sql: {}", sql);
        return jdbcTemplate.update(sql);
    }

    public long insertBatch(String dataModelId, String values) {
        var dm = getDataModel(dataModelId);
        InsertInput input = new InsertInput();
        input.setDataModel(dm.toJavaObject(DataModel.class));
        input.setValues(JSONArray.parse(values));
        var sql = InsertBuilder.buildBatchByInput(input);
        log.debug("batch-insert-sql: {}", sql);
        var res = jdbcTemplate.batchUpdate(sql.toArray(new String[0]));
        return res.length;
    }

    public long updateById(String dataModelId, String id, String value) {
        if (id == null) {
            throw new RuntimeException("id is null");
        }
        var dm = getDataModel(dataModelId);
        UpdateInput input = new UpdateInput();
        input.setDataModel(dm.toJavaObject(DataModel.class));
        input.setId(id);
        input.setValue(JSONObject.parse(value));
        var sql = UpdateBuilder.buildByInput(input);
        log.debug("update-sql: {}", sql);
        return jdbcTemplate.update(sql);
    }

    public long updateByCondition(String dataModelId, List<DataViewCondition> conditions, String value) {
        var dm = getDataModel(dataModelId);
        UpdateInput input = new UpdateInput();
        input.setDataModel(dm.toJavaObject(DataModel.class));
        input.setConditions(conditions);
        input.setValue(JSONObject.parse(value));
        var sql = UpdateBuilder.buildByInput(input);
        log.debug("update-sql: {}", sql);
        return jdbcTemplate.update(sql);
    }

    public void deleteById(String dataModelId, String id) {
        if (id == null) {
            throw new RuntimeException("id is null");
        }
        var dm = getDataModel(dataModelId);
        var sql = "delete from " + dataModelId + " where id='" + id + "'";
        log.debug("delete-sql: {}", sql);
        jdbcTemplate.execute(sql);
    }

    public void executeSql(String sql) {
        log.debug("execute-sql: {}", sql);
        jdbcTemplate.execute(sql);
    }
}
