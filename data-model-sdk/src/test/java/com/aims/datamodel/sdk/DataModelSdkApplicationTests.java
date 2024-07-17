package com.aims.datamodel.sdk;

import com.aims.datamodel.core.dsl.DataViewCondition;
import com.aims.datamodel.sdk.service.DataModelServiceImpl;
import com.aims.datamodel.sdk.service.DatabaseServiceImpl;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Random;

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

    @Test
    void testInsert() {
        var result = dataModelService.insert("test", "{\"id\":\"" + new Random().nextInt() + "\",\"name\":\"测试\"}");
        System.out.println(result);
    }

    @Test
    void testBatchInsert() {
        String batch = String.valueOf(new Random().nextInt());
        var result = dataModelService.insertBatch("test", "[{\"id\":\"" + new Random().nextInt() + "\",\"name\":\"批量测试" + batch + "\"},{\"id\":\"" + new Random().nextInt() + "\",\"name\":\"批量测试" + batch + "\"},{\"id\":\"" + new Random().nextInt() + "\",\"name\":\"批量测试" + batch + "\"}]");
        System.out.println(result);
    }

    @Test
    void getAModel() throws Exception {
        var res = dataModelService.getDataModel("test");
        var json = JSONObject.from(res);
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
        var result = dataModelService.query("test", null);
        System.out.println(result.stream().count());
    }

    @Test
    void testSaveTableToDataModel() {
        try {
            databaseService.saveTableToDataModel("datamodel", "test");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
