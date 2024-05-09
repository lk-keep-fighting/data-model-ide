package com.aims.datamodel.ide.controller;

import com.aims.datamodel.ide.controller.dto.ApiResult;
import com.aims.datamodel.sdk.service.DataModelServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.websocket.server.PathParam;

@RestController
@RequestMapping(value = "/api/data-model/{dataModelId}")
@Slf4j
public class DataModelController {
    @Autowired
    DataModelServiceImpl dataModelService;

    @RequestMapping("/get")
    public ApiResult getDataModel(@PathVariable("dataModelId") String dataModelId) {
        var data = dataModelService.getDataModel(dataModelId);
        return ApiResult.ok(data);
    }
}
