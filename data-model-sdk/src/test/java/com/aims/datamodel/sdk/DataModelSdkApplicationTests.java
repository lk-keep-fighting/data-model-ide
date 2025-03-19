package com.aims.datamodel.sdk;

import com.aims.datamodel.core.dsl.DataViewCondition;
import com.aims.datamodel.core.sqlbuilder.QueryBuilder;
import com.aims.datamodel.core.sqlbuilder.input.QueryInput;
import com.aims.datamodel.sdk.service.DataModelConfigService;
import com.aims.datamodel.sdk.service.DataStoreService;
import com.aims.datamodel.sdk.service.DatabaseService;
import com.aims.datamodel.sdk.utils.PinyinUtil;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.Random;

@SpringBootTest
class DataModelSdkApplicationTests {
    @Autowired
    DataStoreService dataStoreService;

    //    @Test
    void contextLoads() {
        QueryInput queryInput = new QueryInput();
        try {
            String json = "{}";
            json = new String(Files.readAllBytes(Paths.get("src/main/resources/json/query/wcs_log.json")));
            queryInput = JSONObject.parseObject(json, QueryInput.class);
        } catch (IOException e) {
            System.out.println("Failed to read JSON file: " + e.getMessage());
            e.printStackTrace();
        }
        var result = dataStoreService.queryByInput("wcs_log", queryInput);
        System.out.println(result.stream().count());
    }

    @Test
    void testInsert() {
        String json = "{\"id\":\"" + new Random().nextInt() + "\",\"name\":\"测试\"}";
        var value = JSONObject.parseObject(json);
        var result = dataStoreService.insert("test", value);
        System.out.println(result);
    }

    @Test
    void testBatchInsert() {
        String batch = String.valueOf(new Random().nextInt());
        String values = "[{\"id\":\"" + new Random().nextInt() + "\",\"名称\":\"批量测试" + batch + "\"},{\"id\":\"" + new Random().nextInt() + "\",\"名称\":\"批量测试" + batch + "\"},{\"id\":\"" + new Random().nextInt() + "\",\"名称\":\"批量测试" + batch + "\"}]";
        var result = dataStoreService.insertBatch("test2view3", JSONArray.parse(values));
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
        var json = "{\"name\":\"测试更新\"}";
        var value = JSONObject.parseObject(json);
        var result = dataStoreService.updateByCondition("test", conditions, value);
        System.out.println(result);
    }

    @Test
    void testDelete() {
        dataStoreService.deleteById("test", "1");
        List<String> ids = List.of("1", "2");
        dataStoreService.deleteByIds("test", ids);
    }

    @Autowired
    DataModelConfigService dataModelConfigService;

    @Test
    void testQuery() {
        var result = dataStoreService.queryByInput("test", null);
        System.out.println(result.stream().count());
    }

    @Test
    void testSaveTableToDataModel() {
        try {
            dataModelConfigService.deleteById("datamodel.test");
            dataModelConfigService.createByDbTable("datamodel", "test");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //    @Test
    void testCreateByJsonData() throws Exception {
        List<JSONObject> json = List.of(
                JSONObject.of("id", 1, "名称", "测试"),
                JSONObject.of("id", 2, "名称", "测试2")
        );
        var dataModel = dataModelConfigService.createByJsonData("test2view3", "id", json);
        var sql = QueryBuilder.build(new QueryInput().setFrom(dataModel));
        System.out.println(sql);
    }

    @Autowired
    DatabaseService databaseService;

    //    @Test
    void testCreateTableByDataModel() {
        var sql = databaseService.createTableByDataModel(dataModelConfigService.getById("test2view3"));
        System.out.println(sql);
    }

    @Test
    void testPinyin() {
        var res = PinyinUtil.getFirstLetters("测试");
        System.out.println(res);
    }

}
