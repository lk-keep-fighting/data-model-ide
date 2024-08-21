package com.aims.datamodel.sdk;

import com.aims.datamodel.core.dsl.DataViewCondition;
import com.aims.datamodel.sdk.service.DataModelConfigService;
import com.aims.datamodel.sdk.service.DataStoreService;
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
    DataStoreService dataStoreService;

    //    @Test
    void contextLoads() {
        String json = "{}";
        try {
            json = new String(Files.readAllBytes(Paths.get("src/main/resources/json/query/wcs_log.json")));
        } catch (IOException e) {
            System.out.println("Failed to read JSON file: " + e.getMessage());
            e.printStackTrace();
        }
        var result = dataStoreService.query("wcs_log", json);
        System.out.println(result.stream().count());
    }

    @Test
    void testInsert() {
        var result = dataStoreService.insert("test", "{\"id\":\"" + new Random().nextInt() + "\",\"name\":\"测试\"}");
        System.out.println(result);
    }

    @Test
    void testBatchInsert() {
        String batch = String.valueOf(new Random().nextInt());
        var result = dataStoreService.insertBatch("test", "[{\"id\":\"" + new Random().nextInt() + "\",\"name\":\"批量测试" + batch + "\"},{\"id\":\"" + new Random().nextInt() + "\",\"name\":\"批量测试" + batch + "\"},{\"id\":\"" + new Random().nextInt() + "\",\"name\":\"批量测试" + batch + "\"}]");
        System.out.println(result);
    }

    @Test
    void getAModel() throws Exception {
        var res = dataStoreService.getDataModel("test");
        var json = JSONObject.from(res);
        System.out.println(json);
    }

    @Test
    void testUpdate() {
        var conditionsJson = "[{\"column\":\"id\",\"operator\":\"=\",\"value\":\"5\"}]";
        var conditions = JSONArray.parseArray(conditionsJson, DataViewCondition.class);
        var result = dataStoreService.updateByCondition("test", conditions, "{\"name\":\"测试更新\"}");
        System.out.println(result);
    }

    @Autowired
    DataModelConfigService dataModelConfigService;

    @Test
    void testQuery() {
        var result = dataStoreService.query("test", null);
        System.out.println(result.stream().count());
    }

    @Test
    void testSaveTableToDataModel() {
        try {
            dataModelConfigService.createByDbTable("datamodel", "test");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
