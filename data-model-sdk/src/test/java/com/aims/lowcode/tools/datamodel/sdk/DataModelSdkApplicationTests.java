package com.aims.lowcode.tools.datamodel.sdk;

import com.aims.lowcode.tools.datamodel.sdk.service.DataModelServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class DataModelSdkApplicationTests {
    @Autowired
    DataModelServiceImpl dataModelService;

    @Test
    void contextLoads() {
//        var dataModel = dataModelService.getDataModel("logic-ins-view");
//        System.out.println(dataModel);

        var result = dataModelService.query("logic", "{\"page\":1,\"pageSize\":10}");
        System.out.println(result.stream().count());
    }

}
