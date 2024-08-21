package com.aims.datamodel.ide.controller;

import com.aims.datamodel.core.dsl.DataModel;
import com.aims.datamodel.ide.controller.dto.ApiResult;
import com.aims.datamodel.sdk.service.DataModelConfigService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/data-model/{dataModelId}")
@Slf4j
public class DataModelController {
    @Autowired
    DataModelConfigService dataModelService;

    @GetMapping("/get")
    public ApiResult getDataModel(@PathVariable("dataModelId") String dataModelId) {
        var data = dataModelService.getById(dataModelId);
        return ApiResult.ok(data);
    }
//    @PostMapping("/query")
//    public ApiResult query(@RequestBody String input) {
//        var data = dataModelService.query(dataModelId);
//        return ApiResult.ok(data);
//    }

    @PostMapping("/createOrSave")
    public ApiResult saveDataModel(@PathVariable("dataModelId") String dataModelId, @RequestBody DataModel dataModel) throws Exception {
        dataModel.setId(dataModelId);
        dataModelService.create(dataModel);
        return ApiResult.ok(0);
    }
}
