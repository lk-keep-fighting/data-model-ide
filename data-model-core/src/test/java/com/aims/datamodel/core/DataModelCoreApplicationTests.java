package com.aims.datamodel.core;

import com.aims.datamodel.core.sqlbuilder.QueryBuilder;
import com.aims.datamodel.core.sqlbuilder.InsertBuilder;
import com.aims.datamodel.core.sqlbuilder.UpdateBuilder;
import com.aims.datamodel.core.sqlbuilder.input.InsertInput;
import com.aims.datamodel.core.sqlbuilder.input.UpdateInput;
import com.alibaba.fastjson2.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class DataModelCoreApplicationTests {

    @Test
    void testRelaQueryJson() {
        String json = "{}";
        try {
            json = new String(Files.readAllBytes(Paths.get("src/main/jsons/query/关联查询.json")));
        } catch (IOException e) {
            System.out.println("Failed to read JSON file: " + e.getMessage());
            e.printStackTrace();
        }
        var sql = QueryBuilder.buildByJson(json);
        System.out.println("buildsql:" + sql);
        String expected = "SELECT m.`id` AS `id`, logic.`name` AS `logicName`  FROM `logic_instance` as m LEFT JOIN `logic` logic ON m.`logicId` = logic.`id`  WHERE 1=1  AND (m.`logicId`  IS NOT NULL)  AND (m.`bizId` = '1' AND logic.`name`  LIKE '%测试%') ORDER BY m.`serverTime` DESC  LIMIT 10 OFFSET 0";
        System.out.println("expected:" + expected);
        assertEquals(expected, sql);
    }

    @Test
    void testInsert() {
        String json = "{}";
        try {
            json = new String(Files.readAllBytes(Paths.get("src/main/jsons/insert/logic.json")));
        } catch (IOException e) {
            System.out.println("Failed to read JSON file: " + e.getMessage());
            e.printStackTrace();
        }
        InsertInput input = JSONObject.parseObject(json, InsertInput.class);
        var sql = InsertBuilder.buildByInput(input);
//        assertEquals("SELECT m.id AS id, logic.name AS logicName FROM logic_instance as m LEFT JOIN logic logic ON m.logicId = logic.id WHERE 1=1 AND (m.logicId  IS NOT NULL) AND (m.bizId = '1' AND logic.name  LIKE '%测试%') ORDER BY m.serverTime DESC LIMIT 10 OFFSET 0", sql);
        System.out.println(sql);
    }

    @Test
    void testBatchInsert() {
        String json = "{}";
        try {
            json = new String(Files.readAllBytes(Paths.get("src/main/jsons/insert/logics.json")));
        } catch (IOException e) {
            System.out.println("Failed to read JSON file: " + e.getMessage());
            e.printStackTrace();
        }
        InsertInput input = JSONObject.parseObject(json, InsertInput.class);
        var sql = InsertBuilder.buildBatchInsertSqlByInput(input);
        System.out.println(sql);
    }
    @Test
    void testBatchInsert2() {
        String json = "{}";
        try {
            json = new String(Files.readAllBytes(Paths.get("src/main/jsons/insert/lapp_menu.json")));
        } catch (IOException e) {
            System.out.println("Failed to read JSON file: " + e.getMessage());
            e.printStackTrace();
        }
        InsertInput input = JSONObject.parseObject(json, InsertInput.class);
        var sql = InsertBuilder.buildBatchInsertSqlByInput(input);
        System.out.println(sql);
    }
    @Test
    void testBatchInsertLogic() {
        String json = "{}";
        try {
            json = new String(Files.readAllBytes(Paths.get("src/main/jsons/insert/batchInsertLogic.json")));
        } catch (IOException e) {
            System.out.println("Failed to read JSON file: " + e.getMessage());
            e.printStackTrace();
        }
        InsertInput input = JSONObject.parseObject(json, InsertInput.class);
        var sql = InsertBuilder.buildBatchInsertSqlByInput(input);
        System.out.println(sql);
    }

    @Test
    void testUpdate() {
        String json = "{}";
        try {
            json = new String(Files.readAllBytes(Paths.get("src/main/jsons/update/logic.json")));
        } catch (IOException e) {
            System.out.println("Failed to read JSON file: " + e.getMessage());
            e.printStackTrace();
        }
        UpdateInput input = JSONObject.parseObject(json, UpdateInput.class);
        var sql = UpdateBuilder.buildByInput(input);
//        assertEquals("SELECT m.id AS id, logic.name AS logicName FROM logic_instance as m LEFT JOIN logic logic ON m.logicId = logic.id WHERE 1=1 AND (m.logicId  IS NOT NULL) AND (m.bizId = '1' AND logic.name  LIKE '%测试%') ORDER BY m.serverTime DESC LIMIT 10 OFFSET 0", sql);
        System.out.println(sql);
    }

}
