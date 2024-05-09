package com.aims.datamodel.datamodelideservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"com.aims.datamodel"})
@SpringBootConfiguration
public class DataModelIdeServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(DataModelIdeServiceApplication.class, args);
    }

}
