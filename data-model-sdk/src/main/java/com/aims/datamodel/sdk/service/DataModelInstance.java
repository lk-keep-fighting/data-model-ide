package com.aims.datamodel.sdk.service;

import com.aims.datamodel.core.sqlbuilder.input.QueryInput;
import com.alibaba.fastjson2.JSONObject;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class DataModelInstance {
    @Setter
    @Getter
    private String dataModelId;
    private DataModelServiceImpl dataModelService;

    @Autowired
    public DataModelInstance(DataModelServiceImpl _dataModelService) {
        this.dataModelService = _dataModelService;
    }

    public JSONObject getDataModel() {
        return dataModelService.getDataModel(dataModelId);
    }

    public List<Map<String, Object>> query(String queryJson) {
        return dataModelService.query(dataModelId, queryJson);
    }

    public List<Map<String, Object>> queryByInput(QueryInput queryInput) {
        return dataModelService.queryByInput(dataModelId, queryInput);
    }
}
