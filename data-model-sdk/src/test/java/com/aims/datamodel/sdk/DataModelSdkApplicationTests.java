package com.aims.datamodel.sdk;

import com.aims.datamodel.core.dsl.DataViewCondition;
import com.aims.datamodel.sdk.service.DataModelServiceImpl;
import com.aims.datamodel.sdk.service.DatabaseServiceImpl;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.JSONPath;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@SpringBootTest
class DataModelSdkApplicationTests {
    @Autowired
    DataModelServiceImpl dataModelService;

    //    @Test
    void contextLoads() {
        String json = "{}";
        try {
            json = new String(Files.readAllBytes(Paths.get("src/main/resources/json/query/wcs_log.json")));
        } catch (IOException e) {
            System.out.println("Failed to read JSON file: " + e.getMessage());
            e.printStackTrace();
        }
        var result = dataModelService.query("wcs_log", json);
        System.out.println(result.stream().count());
    }

    //    @Test
    void testInsert() {
        var result = dataModelService.insert("test", "{\"id\":\"5\",\"name\":\"测试\"}");
        System.out.println(result);
    }

//            @Test
    void testBatchInsert() {
        var result = dataModelService.insertBatch("test", "[{\"id\":\"66999\",\"name\":\"测试66999\"},{\"id\":\"661001\",\"name\":\"66测试1001\"},{\"id\":\"661002\",\"name\":\"66测试1002\"}]");
        System.out.println(result);
    }

    @Test
    void getAModel() {
        var res = dataModelService.getDataModel("logic");
        var json= JSONObject.from(res);
        System.out.println(json);
    }

    @Test
    void testUpdate() {
        var conditionsJson = "[{\"column\":\"id\",\"operator\":\"=\",\"value\":\"5\"}]";
        var conditions = JSONArray.parseArray(conditionsJson, DataViewCondition.class);
        var result = dataModelService.updateByCondition("test", conditions, "{\"name\":\"测试更新\"}");
        System.out.println(result);
    }

    @Autowired
    DatabaseServiceImpl databaseService;

    @Test
    void testQuery() {
        var result = dataModelService.query("logic", null);
        System.out.println(result.stream().count());
    }

    @Test
    void testSaveTableToFile() {
        try {
            databaseService.saveTableToFile("lapp", "logic");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
