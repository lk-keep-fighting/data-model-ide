package com.aims.datamodel.ide.controller;

import com.aims.datamodel.ide.controller.dto.ApiResult;
import com.aims.datamodel.ide.controller.dto.SqlQueryInput;
import com.aims.datamodel.sdk.service.DataModelServiceImpl;
import com.alibaba.fastjson2.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/sql")
@Slf4j
public class SqlController {
    @Autowired
    DataModelServiceImpl dataModelService;

    @PostMapping("/query")
    public ApiResult queryData(@RequestBody SqlQueryInput input) {
        try {
            var sql = input.getSql();
            if (sql == null || sql.isEmpty()) {
                return ApiResult.error("sql is empty");
            }
            var res = dataModelService.queryBySql(sql);
            return ApiResult.ok(res);
        } catch (Exception e) {
            log.error("queryData error", e);
            return ApiResult.fromException(e);
        }
    }
}
