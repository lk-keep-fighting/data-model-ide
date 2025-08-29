package com.aims.datamodel.ide.controller;

import com.aims.datamodel.core.dsl.DataModel;
import com.aims.datamodel.core.sqlbuilder.input.QueryInput;
import com.aims.datamodel.ide.controller.dto.ApiResult;
import com.aims.datamodel.sdk.service.DataStoreService;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/data-model/crud/{dataModelId}")
@Slf4j
public class CrudController {
    @Autowired
    DataStoreService dataStoreService;

    @PostMapping("/add")
    public ApiResult addData(@PathVariable("dataModelId") String dataModelId, @RequestBody JSONObject data) {
        try {
            var res = dataStoreService.insert(dataModelId, data);
            return new ApiResult().setData(res);
        } catch (Exception e) {
            log.error("addData error", e);
            return ApiResult.fromException(e);
        }
    }

    @PostMapping("/batch-add")
    public ApiResult batchAddData(@PathVariable("dataModelId") String dataModelId, @RequestBody JSONArray data) {
        try {
            var res = dataStoreService.insertBatch(dataModelId, data);
            return new ApiResult().setData(res);
        } catch (Exception e) {
            log.error("batchAddData error", e);
            return ApiResult.fromException(e);
        }
    }

    @PutMapping("/edit/{dataId}")
    public ApiResult editData(@PathVariable("dataModelId") String dataModelId, @PathVariable("dataId") String dataId, @RequestBody JSONObject data) {
        try {
            var res = dataStoreService.updateById(dataModelId, dataId, data);
            return new ApiResult().setData(res);
        } catch (Exception e) {
            log.error("editData error", e);
            return ApiResult.fromException(e);
        }

    }

    @GetMapping("/get/{dataId}")
    public ApiResult getData(@PathVariable("dataModelId") String dataModelId, @PathVariable("dataId") String dataId) {
        try {
            var res = dataStoreService.queryById(dataModelId, dataId);
            return new ApiResult().setData(res);
        } catch (Exception e) {
            log.error("getData error", e);
            return ApiResult.fromException(e);
        }
    }

    @PostMapping("/query")
    public ApiResult queryData(@PathVariable("dataModelId") String dataModelId, @RequestBody QueryInput queryInput) {
        try {
            var res = dataStoreService.queryByInput(dataModelId, queryInput);
            return new ApiResult().setData(res);
        } catch (Exception e) {
            log.error("queryData error", e);
            return ApiResult.fromException(e);
        }
    }

    @PostMapping("/query-page")
    public ApiResult queryPageData(@PathVariable("dataModelId") String dataModelId, @RequestBody QueryInput queryInput) {
        try {
            var res = dataStoreService.queryPageByInput(dataModelId, queryInput);
            return new ApiResult().setData(res);
        } catch (Exception e) {
            log.error("queryPageData error", e);
            return ApiResult.fromException(e);
        }
    }

    @DeleteMapping("/delete/{dataId}")
    public ApiResult deleteData(@PathVariable("dataModelId") String dataModelId, @PathVariable("dataId") String dataId) {
        try {
            dataStoreService.deleteById(dataModelId, dataId);
            return ApiResult.ok(dataId);
        } catch (Exception e) {
            log.error("deleteData error", e);
            return ApiResult.fromException(e);
        }
    }
}
