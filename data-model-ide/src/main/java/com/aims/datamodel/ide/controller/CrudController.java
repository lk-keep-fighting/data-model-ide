package com.aims.datamodel.ide.controller;

import com.aims.datamodel.core.sqlbuilder.input.QueryInput;
import com.aims.datamodel.ide.controller.dto.ApiResult;
import com.aims.datamodel.sdk.dto.PageResult;
import com.aims.datamodel.sdk.service.DataModelServiceImpl;
import com.alibaba.fastjson2.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/api/crud/{dataModelId}")
@Slf4j
public class CrudController {
    @Autowired
    DataModelServiceImpl dataModelService;

    @PostMapping("/add")
    public ApiResult addData(@PathVariable("dataModelId") String dataModelId, @RequestBody String data) {
        try {
            var res = dataModelService.insert(dataModelId, data);
            return new ApiResult().setData(res);
        } catch (Exception e) {
            log.error("addData error", e);
            return ApiResult.fromException(e);
        }
    }

    @PostMapping("/batchAdd")
    public ApiResult batchAddData(@PathVariable("dataModelId") String dataModelId, @RequestBody String data) {
        try {
            var res = dataModelService.insertBatch(dataModelId, data);
            return new ApiResult().setData(res);
        } catch (Exception e) {
            log.error("batchAddData error", e);
            return ApiResult.fromException(e);
        }
    }

    @PutMapping("/edit/{dataId}")
    public ApiResult editData(@PathVariable("dataModelId") String dataModelId, @PathVariable("dataId") String dataId, @RequestBody String data) {
        try {
            var res = dataModelService.updateById(dataModelId, dataId, data);
            return new ApiResult().setData(res);
        } catch (Exception e) {
            log.error("editData error", e);
            return ApiResult.fromException(e);
        }

    }

    @GetMapping("/get/{dataId}")
    public ApiResult getData(@PathVariable("dataModelId") String dataModelId, @PathVariable("dataId") String dataId) {
        try {
            var res = dataModelService.queryById(dataModelId, dataId);
            return new ApiResult().setData(res);
        } catch (Exception e) {
            log.error("getData error", e);
            return ApiResult.fromException(e);
        }
    }

    @PostMapping("/query")
    public ApiResult queryData(@PathVariable("dataModelId") String dataModelId, @RequestBody QueryInput queryInput) {
        try {
            var res = dataModelService.queryByInput(dataModelId, queryInput);
            return new ApiResult().setData(res);
        } catch (Exception e) {
            log.error("queryData error", e);
            return ApiResult.fromException(e);
        }
    }

    @PostMapping("/queryPage")
    public ApiResult queryPageData(@PathVariable("dataModelId") String dataModelId, @RequestBody QueryInput queryInput) {
        try {
            var res = dataModelService.queryPageByInput(dataModelId, queryInput);
            return new ApiResult().setData(res);
        } catch (Exception e) {
            log.error("queryPageData error", e);
            return ApiResult.fromException(e);
        }
    }

    @DeleteMapping("/delete/{dataId}")
    public ApiResult deleteData(@PathVariable("dataModelId") String dataModelId, @PathVariable("dataId") String dataId) {
        try {
            dataModelService.deleteById(dataModelId, dataId);
            return ApiResult.ok(dataId);
        } catch (Exception e) {
            log.error("deleteData error", e);
            return ApiResult.fromException(e);
        }
    }
}
