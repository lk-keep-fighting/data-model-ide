package com.aims.datamodel.ide.controller;

import com.aims.datamodel.core.dsl.DataModel;
import com.aims.datamodel.ide.controller.dto.ApiResult;
import com.aims.datamodel.sdk.service.DataModelServiceImpl;
import com.alibaba.fastjson2.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;

@RestController
@RequestMapping(value = "/api/data-model/{dataModelId}")
@Slf4j
public class DataModelController {
    @Autowired
    DataModelServiceImpl dataModelService;

    @GetMapping("/get")
    public ApiResult getDataModel(@PathVariable("dataModelId") String dataModelId) {
        var data = dataModelService.getDataModel(dataModelId);
        return ApiResult.ok(data);
    }
//    @PostMapping("/query")
//    public ApiResult query(@RequestBody String input) {
//        var data = dataModelService.query(dataModelId);
//        return ApiResult.ok(data);
//    }

    @PostMapping("/createOrSave")
    public ApiResult saveDataModel(@PathVariable("dataModelId") String dataModelId, @RequestBody DataModel dataModel) throws Exception {
        dataModelService.saveDataModel(dataModelId, dataModel);
        return ApiResult.ok(0);
    }
}
