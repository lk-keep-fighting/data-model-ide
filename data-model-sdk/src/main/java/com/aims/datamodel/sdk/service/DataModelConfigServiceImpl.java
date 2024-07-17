package com.aims.datamodel.sdk.service;

import com.aims.datamodel.core.dsl.DataModel;
import com.aims.datamodel.sdk.AppConfig;
import com.aims.datamodel.sdk.entity.DataModelEntity;
import com.aims.datamodel.sdk.utils.VersionUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class DataModelConfigServiceImpl {
    @Autowired
    private AppConfig appConfig;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public DataModel getConfigJson(String dataModelId) {
        try {
            var dm = getDataModelEntityOrCreate(dataModelId);
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

    public void updateOrCreateConfigJson(String dataModelId, DataModel dataModel) throws Exception {
        var res = updateConfigJson(dataModelId, dataModel);
        if (!res) {
            var entity = new DataModelEntity();
            entity.setId(dataModelId);
            entity.setConfigJson(JSON.toJSONString(dataModel));
            createEntity(entity);
        }
    }

    public boolean updateConfigJson(String dataModelId, DataModel dataModel) throws Exception {
        String version = VersionUtil.newVersionNoByNow();
        dataModel.setVersion(version);
        var rows = jdbcTemplate.update("update datamodel set version=?, configJson = ? where id = ?", version, JSON.toJSONString(dataModel), dataModelId);
        return rows > 0;
        //        FileUtil.writeFile(appConfig.getDATA_MODEL_DIR(), dataModelId + ".json", JSONObject.toJSONString(dataModel));
    }

    public void createEntity(DataModelEntity entity) throws Exception {
        String version = entity.getVersion() == null ? VersionUtil.newVersionNoByNow() : entity.getVersion();
        jdbcTemplate.update("insert into datamodel (id,name,module,version,`type`, configJson, memo) values (?,?,?,?,?,?,?)",
                entity.getId(), entity.getName(), entity.getModule(), version,
                entity.getType(), entity.getConfigJson(), entity.getMemo());
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
