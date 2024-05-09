package com.aims.datamodel.ide;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"com.aims.datamodel"})
public class DataModelIdeApplication {
    public static void main(String[] args) {
        SpringApplication.run(DataModelIdeApplication.class, args);
    }

}
