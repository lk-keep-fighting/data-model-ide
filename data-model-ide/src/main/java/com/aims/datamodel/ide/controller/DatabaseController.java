package com.aims.datamodel.ide.controller;

import com.aims.datamodel.ide.controller.dto.ApiResult;
import com.aims.datamodel.sdk.service.impl.DatabaseServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/api/ide/database")
@Slf4j
public class DatabaseController {
    @Autowired
    private DatabaseServiceImpl databaseServiceImpl;

    @GetMapping("/list")
    public ApiResult<List<Map<String, Object>>> getDatabaseList() {
        var result = new ApiResult<List<Map<String, Object>>>();
        result.setData(databaseServiceImpl.getDatabaseList());
        return result;
    }

    @GetMapping("/{dbId}/tables")
    public ApiResult<List<Map<String, Object>>> getDatabaseTableList(@PathVariable String dbId) {
        var result = new ApiResult<List<Map<String, Object>>>();
        result.setData(databaseServiceImpl.getTableList(dbId));
        return result;
    }
}
