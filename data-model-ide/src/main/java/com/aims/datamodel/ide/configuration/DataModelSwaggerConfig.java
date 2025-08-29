package com.aims.datamodel.ide.configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataModelSwaggerConfig {

    @Bean
    public OpenAPI createRestApi() {
        return new OpenAPI().info(new Info()
                .title("DataModel API")
                .description("DataModel API"));
    }
}
