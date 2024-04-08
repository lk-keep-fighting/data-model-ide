package com.aims.lowcode.tools.datamodel.core;

import com.aims.lowcode.tools.datamodel.core.sqlbuilder.QueryBuilder;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class DataModelCoreApplicationTests {

    @Test
    void contextLoads() {
        String json = "{}";
        try {
            json = new String(Files.readAllBytes(Paths.get("src/main/resources/jsons/simple.json")));
        } catch (IOException e) {
            System.out.println("Failed to read JSON file: " + e.getMessage());
            e.printStackTrace();
        }
        var sql = QueryBuilder.buildByJson(json);
        assertEquals("SELECT m.id AS id, logic.name AS logicName FROM logic_instance as m LEFT JOIN logic logic ON m.logicId = logic.id WHERE 1=1 AND (m.logicId  IS NOT NULL) AND (m.bizId = '1' AND logic.name  LIKE '%测试%') ORDER BY m.serverTime DESC LIMIT 10 OFFSET 0", sql);
        System.out.println(sql);
    }

}
