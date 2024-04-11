package com.aims.datamodel.sdk;

import com.aims.datamodel.sdk.service.DataModelServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class DataModelSdkApplicationTests {
    @Autowired
    DataModelServiceImpl dataModelService;

//    @Test
    void contextLoads() {
        var result = dataModelService.query("logic", "{\"page\":1,\"pageSize\":10}");
        System.out.println(result.stream().count());
    }

//    @Test
    void testInsert() {
        var result = dataModelService.insertByJson("test", "{\"id\":\"5\",\"name\":\"测试\"}");
        System.out.println(result);
    }

//    @Test
    void testBatchInsert() {
        var result = dataModelService.insertBatchByJson("test", "[{\"id\":\"66999\",\"name\":\"测试66999\"},{\"id\":\"661001\",\"name\":\"66测试1001\"},{\"id\":\"661002\",\"name\":\"66测试1002\"}]");
        System.out.println(result);
    }
}
