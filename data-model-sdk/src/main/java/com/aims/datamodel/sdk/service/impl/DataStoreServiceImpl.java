package com.aims.datamodel.sdk.service.impl;

import com.aims.datamodel.core.dsl.DataModel;
import com.aims.datamodel.core.dsl.DataViewCondition;
import com.aims.datamodel.core.sqlbuilder.InsertBuilder;
import com.aims.datamodel.core.sqlbuilder.UpdateBuilder;
import com.aims.datamodel.core.sqlbuilder.input.InsertInput;
import com.aims.datamodel.core.sqlbuilder.input.QueryInput;
import com.aims.datamodel.core.sqlbuilder.input.UpdateInput;
import com.aims.datamodel.sdk.dto.PageResult;
import com.aims.datamodel.sdk.service.DataModelConfigService;
import com.aims.datamodel.sdk.service.DataModelConfigServiceImpl;
import com.aims.datamodel.sdk.service.DataStoreService;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class DataStoreServiceImpl implements DataStoreService {
    private final JdbcTemplate jdbcTemplate;
    private final DataModelConfigService dataModelConfigService;

    @Autowired
    public DataStoreServiceImpl(
            JdbcTemplate jdbcTemplate,
            DataModelConfigServiceImpl databaseService) {
        this.jdbcTemplate = jdbcTemplate;
        this.dataModelConfigService = databaseService;
    }

    public DataModel getDataModel(String dataModelId) {
        return dataModelConfigService.getConfigJson(dataModelId);
    }


    public Map<String, Object> queryById(String dataModelId, String id) {
        List<String> ids = new ArrayList<>();
        ids.add(id);
        var result = queryByIds(dataModelId, ids);
        return !result.isEmpty() ? result.get(0) : null;
    }

    public List<Map<String, Object>> queryByIds(String dataModelId, List<String> ids) {
        var dm = getDataModel(dataModelId);
        var query = new QueryInput();
        query.setIds(ids);
        query.setFrom(dm);
        return queryByInput(dataModelId, query);
    }

//    public List<Map<String, Object>> query(String dataModelId, String queryJson) {
//        var dm = getDataModel(dataModelId);
//        var query = queryJson == null ? new QueryInput() : JSONObject.parseObject(queryJson, QueryInput.class);
//        query.setFrom(dm);
//        return queryByInput(dataModelId, query);
//    }

    public List<Map<String, Object>> queryByInput(String dataModelId, QueryInput queryInput) {
        if (queryInput == null) queryInput = new QueryInput();
        var dm = getDataModel(dataModelId);
        queryInput.setFrom(dm);
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

        StringBuilder countSql = new StringBuilder("SELECT COUNT(*) ");
        if (sql.toUpperCase().contains("GROUP BY")) {
            countSql.append("FROM (").append(sql).append(") AS t");
        } else {
            int fromIdx = sql.indexOf("FROM");
            countSql.append(sql.substring(fromIdx));
        }
        if (page != 0) {
            long offset = (page - 1) * pageSize;
            sql += String.format(" LIMIT %s OFFSET %s", pageSize, offset);
        }
        log.debug("query-page-sql {}", sql);
        log.debug("query-page-countSql {}", countSql);
        var records = jdbcTemplate.queryForList(sql);
        var totalCount = jdbcTemplate.queryForObject(countSql.toString(), Long.class);
        return new PageResult(records, totalCount, page, pageSize);
    }

    public PageResult queryPageByInput(String dataModelId, QueryInput queryInput) {
        var dm = getDataModel(dataModelId);
        queryInput.setFrom(dm);
        var sql = queryInput.buildPageSql();
        var countSql = queryInput.convertPageSqlToCountSql(sql);
        log.debug("query-page-sql {}", sql);
        log.debug("query-page-countSql {}", countSql);
        var items = jdbcTemplate.queryForList(sql);
        var totalCount = jdbcTemplate.queryForObject(countSql, Long.class);
        return new PageResult(items, totalCount, queryInput.getPage(), queryInput.getPageSize());
    }

    public long insert(String dataModelId, JSONObject value) {
        var dm = getDataModel(dataModelId);
        InsertInput input = new InsertInput();
        input.setDataModel(dm);
        input.setValue(value);
        var sql = InsertBuilder.buildByInput(input);
        log.debug("insert-sql: {}", sql);
        return jdbcTemplate.update(sql);
    }

    public long insertBatch(String dataModelId, JSONArray values) {
        if (values == null || values.isEmpty()) return 0;
        var dm = getDataModel(dataModelId);
        InsertInput input = new InsertInput();
        input.setDataModel(dm);
        input.setValues(values);
        var sql = InsertBuilder.buildBatchInsertSqlByInput(input);
        log.debug("batch-insert-sql: {}", sql);
        var res = jdbcTemplate.batchUpdate(sql);
        return res.length;
    }

    public long updateById(String dataModelId, String id, JSONObject value) {
        if (id == null) {
            throw new RuntimeException("id is null");
        }
        var dm = getDataModel(dataModelId);
        UpdateInput input = new UpdateInput();
        input.setDataModel(dm);
        input.setId(id);
        input.setValue(value);
        var sql = UpdateBuilder.buildByInput(input);
        log.debug("update-sql: {}", sql);
        return jdbcTemplate.update(sql);
    }

    public long updateByCondition(String dataModelId, List<DataViewCondition> conditions, JSONObject value) {
        var dm = getDataModel(dataModelId);
        UpdateInput input = new UpdateInput();
        input.setDataModel(dm);
        input.setConditions(conditions);
        input.setValue(value);
        var sql = UpdateBuilder.buildByInput(input);
        log.debug("update-sql: {}", sql);
        return jdbcTemplate.update(sql);
    }

    public void deleteById(String dataModelId, String id) {
        if (id == null) {
            throw new RuntimeException("id is null");
        }
        var dm = getDataModel(dataModelId);
        var sql = "delete from " + dm.buildTableSql(dm.getMainTable()) + " where " + dm.findPrimaryKey() + "='" + id + "'";
        log.debug("delete-sql: {}", sql);
        jdbcTemplate.execute(sql);
    }

    public void deleteByIds(String dataModelId, List<String> ids) {
        if (ids == null) {
            throw new RuntimeException("id is null");
        }
        var dm = getDataModel(dataModelId);
        var sql = "delete from " + dm.buildTableSql(dm.getMainTable()) + " where " + dm.findPrimaryKey() + " in ('" + String.join("','", ids) + "')";
        log.debug("delete-sql: {}", sql);
        jdbcTemplate.execute(sql);
    }

    /**
     * 执行任意sql，无返回值
     *
     * @param sql
     */
    public void executeSql(String sql) {
        log.debug("execute-sql: {}", sql);
        jdbcTemplate.execute(sql);
    }
}
