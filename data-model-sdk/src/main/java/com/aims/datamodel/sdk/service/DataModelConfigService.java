package com.aims.datamodel.sdk.service;

import com.aims.datamodel.core.dsl.DataModel;
import com.aims.datamodel.sdk.entity.DataModelEntity;
import com.alibaba.fastjson2.JSONObject;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

public interface DataModelConfigService {
    DataModel create(DataModel dataModel);

    DataModel createByDbTable(String dbName, String tableName) throws Exception;

    DataModel createByDbTable(String dbName, String tableName, String dataModelId) throws Exception;

    DataModel createByJsonData(String dataModelId, String primaryColumn, List<JSONObject> jsonData, String dbName, String tableName) throws Exception;

    DataModel createByJsonData(String dataModelId, String primaryColumn, List<JSONObject> jsonData) throws Exception;

    DataModel update(DataModel dataModel);

    DataModel updateById(String dataModelId, DataModel dataModel);

    DataModel getById(String dataModelId);

    DataModel getConfigJson(String dataModelId);

    void deleteById(String dataModelId);

    void deleteByIds(List<String> dataModelId);

}
