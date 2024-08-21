package com.aims.datamodel.sdk.service;

import com.aims.datamodel.core.dsl.DataModel;
import com.aims.datamodel.core.dsl.DataViewCondition;
import com.aims.datamodel.core.sqlbuilder.input.QueryInput;
import com.aims.datamodel.sdk.dto.PageResult;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

public interface DataStoreService {
    DataModel getDataModel(String dataModelId);
    Map<String, Object> queryById(String dataModelId, String id);
    List<Map<String, Object>> queryByIds(String dataModelId, List<String>ids);
    List<Map<String, Object>> query(String dataModelId, String queryJson);
    List<Map<String, Object>> queryByInput(String dataModelId, QueryInput queryInput);
    List<Map<String, Object>> queryBySql(String sql);
    PageResult queryPageBySql(String sql, int page, long pageSize);
    PageResult queryPageByInput(String dataModelId, QueryInput queryInput);
    long insert(String dataModelId, String value);
    long insertBatch(String dataModelId, String values);
    long updateById(String dataModelId, String id, String value);
    long updateByCondition(String dataModelId, List<DataViewCondition> conditions, String value);
    void deleteById(String dataModelId, String id);
    void executeSql(String sql);

}
