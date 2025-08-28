package com.aims.datamodel.sdk.service;

import com.aims.datamodel.core.dsl.*;
import com.aims.datamodel.core.sqlbuilder.input.Column;
import com.aims.datamodel.sdk.entity.DataModelEntity;
import com.aims.datamodel.sdk.utils.PinyinUtil;
import com.aims.datamodel.sdk.utils.RandomUtil;
import com.aims.datamodel.sdk.utils.VersionUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class DataModelConfigServiceImpl implements DataModelConfigService {
    private final JdbcTemplate jdbcTemplate;
    private final DatabaseService databaseService;

    @Autowired
    public DataModelConfigServiceImpl(
            JdbcTemplate jdbcTemplate,
            DatabaseService databaseService
    ) {
        this.jdbcTemplate = jdbcTemplate;
        this.databaseService = databaseService;
    }


    @Override
    public List<String> list() {
        String sql = "select id from datamodel";
        return jdbcTemplate.query(sql, (rs, rowNum) -> rs.getString("id"));
    }

    @Override
    public DataModel create(DataModel dataModel) {
        String version = dataModel.getVersion() == null ? VersionUtil.newVersionNoByNow() : dataModel.getVersion();
        jdbcTemplate.update("insert into datamodel (id,name,module,version,`type`, configJson, memo) values (?,?,?,?,?,?,?)",
                dataModel.getId(), dataModel.getName(), dataModel.getGroup(), version,
                dataModel.getType(), JSON.toJSONString(dataModel), dataModel.getMemo());
        return dataModel;
    }

    @Override
    public DataModel createByDbTable(String dbName, String tableName) throws Exception {
        return createByDbTable(dbName, tableName, tableName);
    }

    @Override
    public DataModel createByJsonData(String dataModelId, String primaryColumn, List<JSONObject> jsonData, String dbName, String tableName) throws Exception {
        try {
            var storeDbName = databaseService.getDefaultDbNameIfNull(dbName);
            if (!StringUtils.hasText(tableName))
                tableName = dataModelId;
            DataModel dataModel = new DataModel();
            dataModel.setId(dataModelId);
            dataModel.setMainTable(tableName);
            TableAliasMap tableMapValue = new TableAliasMap();
            tableMapValue.setStoreTable(tableName);
            tableMapValue.setStoreDatabase(storeDbName);
            dataModel.getTableMap().put(tableName, tableMapValue);
            List<DataModelColumn> columns = new ArrayList<>();
            var exampleData = jsonData.get(0);
            exampleData.keySet().forEach(key -> {
                DataModelColumn column = new DataModelColumn();
                String clmStoreName = PinyinUtil.toPurePinyin(key);
                if (clmStoreName.length() > 10) {
                    clmStoreName = PinyinUtil.getFirstLetters(key);
                    if (clmStoreName.length() > 10) {
                        clmStoreName = clmStoreName.substring(0, 10) + RandomUtil.getRandomNumber(3);
                    }
                }
                column.setColumn(key);
                ColumnAliasMap clmMap = new ColumnAliasMap();
                if (key.equals(primaryColumn)) {
                    clmMap.setStoreIsPrimaryKey(true);
                }
                clmMap.setStoreColumn(clmStoreName);
                columns.add(column);
                dataModel.getColumnMap().put(key, clmMap);
            });
            dataModel.setColumns(columns);
            create(dataModel);
            return dataModel;
        } catch (Exception e) {
            log.error("createByJsonData error", e);
            throw e;
        }
    }

    @Override
    public DataModel createByJsonData(String dataModelId, String primaryColumn, List<JSONObject> jsonData) throws Exception {
        return createByJsonData(dataModelId, primaryColumn, jsonData, null, null);
    }

    @Override
    public DataModel createByDbTable(String dbName, String tableName, String dataModelId) throws Exception {
        var storeDbName = databaseService.getDefaultDbNameIfNull(dbName);
        DataModel dataModel = new DataModel();
        dataModel.setMainTable(tableName);
        var columns = databaseService.getDbColumnList(storeDbName, tableName);
        var tableMapValue = new TableAliasMap();
//        tableMapValue.setTable(tableName);
        tableMapValue.setStoreTable(tableName);
//        tableMap.setColumns(columns);
        tableMapValue.setPrimaryKey(columns.stream().filter(DataTableColumn::isStoreIsPrimaryKey).findFirst().map(DataTableColumn::getStoreColumn).orElse(""));
        tableMapValue.setStoreDatabase(dbName);
        LinkedHashMap<String, TableAliasMap> tableMap = new LinkedHashMap<>();
        tableMap.put(tableName, tableMapValue);
        dataModel.setTableMap(tableMap);
        LinkedHashMap<String, ColumnAliasMap> columnMap = new LinkedHashMap<>();
        columns.forEach(column -> {
            ColumnAliasMap clmMapValue = JSONObject.parseObject(JSONObject.toJSONString(column), ColumnAliasMap.class);
            clmMapValue.setTable(tableName);
//            clmMapValue.setColumn(column.getStoreColumn());
            clmMapValue.setStoreDataType(column.getStoreDataType());
            clmMapValue.setStoreDataLength(column.getStoreDataLength());
            columnMap.put(clmMapValue.getStoreColumn(), clmMapValue);
        });
        dataModel.setColumnMap(columnMap);
        if (dataModelId == null) dataModelId = tableName;
        dataModel.setId(dataModelId);
        create(dataModel);
        return dataModel;
    }

    @Override
    public DataModel update(DataModel dataModel) {
        String version = VersionUtil.newVersionNoByNow();
        dataModel.setVersion(version);
        var rows = jdbcTemplate.update("update datamodel set version=?, configJson = ? where id = ?", version, JSON.toJSONString(dataModel), dataModel.getId());
        if (rows > 0)
            return dataModel;
        else return null;
    }

    @Override
    public DataModel updateById(String dataModelId, DataModel dataModel) {
        dataModel.setId(dataModelId);
        return update(dataModel);
    }

    @Override
    public DataModel getById(String dataModelId) {
        try {
            var dm = jdbcTemplate.queryForMap(
                    "select id,name,module,version,type,configJson,memo from datamodel where id = ?",
                    dataModelId
            );
            if (dm.isEmpty()) {
                return null;
            }
            var json = JSONObject.from(dm);
            var entity = json.to(DataModelEntity.class);
            var configJson = entity.getConfigJson();
            return JSONObject.parse(configJson).to(DataModel.class);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public void deleteById(String dataModelId) {
        jdbcTemplate.execute("delete from datamodel where id = '" + dataModelId + "'");
    }

    @Override
    public void deleteByIds(List<String> ids) {
        String sql = "delete from datamodel where id in (" + ids.stream().map(id -> "'" + id + "'").collect(Collectors.joining(",")) + ")";
        jdbcTemplate.execute(sql);
    }

    public DataModel getConfigJson(String dataModelId) {
        try {
            var dm = getDataModelEntity(dataModelId);
            if (dm != null) {
                JSONObject defDataModel = JSONObject.parse(dm.getConfigJson());
                return defDataModel.to(DataModel.class);
            }
        } catch (Exception e) {
            log.error("get config json error: " + e.getMessage());
        }
        return null;
    }

    public DataModelEntity getDataModelEntity(String dataModelId) {
        try {
            var dm = jdbcTemplate.queryForMap(
                    "select id,name,module,version,type,configJson,memo from datamodel where id = ?",
                    dataModelId
            );
            if (dm.isEmpty()) {
                return null;
            }
            var json = JSONObject.from(dm);
            return json.to(DataModelEntity.class);
        } catch (Exception e) {
            return null;
        }
    }

    public DataModelEntity getDataModelEntityOrCreate(String dataModelId) throws Exception {
        var dm = getDataModelEntity(dataModelId);
        if (dm == null) {
            dm = new DataModelEntity();
            dm.setId(dataModelId);
            dm.setName(dataModelId);
            dm.setVersion(VersionUtil.newVersionNoByNow());
            JSONObject defDataModel = new JSONObject();
            defDataModel.put("mainTable", dataModelId);
            dm.setConfigJson(defDataModel.toJSONString());
            createEntity(dm);
        }
        return dm;
    }

    public DataModel updateOrCreateConfigJson(String dataModelId, DataModel dataModel) throws Exception {
        var res = updateConfigJson(dataModelId, dataModel);
        if (!res) {
            var entity = new DataModelEntity();
            entity.setId(dataModelId);
            entity.setConfigJson(JSON.toJSONString(dataModel));
            createEntity(entity);
        }
        return dataModel;
    }

    public boolean updateConfigJson(String dataModelId, DataModel dataModel) throws Exception {
        String version = VersionUtil.newVersionNoByNow();
        dataModel.setVersion(version);
        var rows = jdbcTemplate.update("update datamodel set version=?, configJson = ? where id = ?", version, JSON.toJSONString(dataModel), dataModelId);
        return rows > 0;
        //        FileUtil.writeFile(appConfig.getDATA_MODEL_DIR(), dataModelId + ".json", JSONObject.toJSONString(dataModel));
    }

    public DataModelEntity createEntity(DataModelEntity entity) throws Exception {
        String version = entity.getVersion() == null ? VersionUtil.newVersionNoByNow() : entity.getVersion();
        jdbcTemplate.update("insert into datamodel (id,name,module,version,`type`, configJson, memo) values (?,?,?,?,?,?,?)",
                entity.getId(), entity.getName(), entity.getModule(), version,
                entity.getType(), entity.getConfigJson(), entity.getMemo());
        return entity;
    }

    /**
     * 更新数据实体信息，不更新version、configJson
     *
     * @param entity
     * @return
     * @throws Exception
     */
    public boolean updateEntity(DataModelEntity entity) throws Exception {
        var rows = jdbcTemplate.update("update datamodel set name=?,module=?,`type`=?, memo=? where id=?",
                entity.getName(), entity.getModule(), entity.getType(), entity.getMemo(), entity.getId());
        return rows > 0;
    }


}
