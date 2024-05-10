package com.aims.datamodel.ide.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.oas.annotations.EnableOpenApi;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 *
 * @author : 村雨遥
 * @version : 1.0
 * @project : springboot-swagger3-demo
 * @package : com.cunyu.springbootswagger3demo.config
 * @className : SwaggerConfig
 * @createTime : 2022/1/6 14:19
 * @email : 747731461@qq.com
 * @微信 : cunyu1024
 * @公众号 : 村雨遥
 * @网站 : https://cunyu1943.github.io
 * @description :
 */

//@Configuration
//@EnableOpenApi
public class DataModelIdeSwagger3Config {

    /**
     * 用于读取配置文件 application.properties 中 swagger 属性是否开启
     */
//    @Value("${data-model.swagger.enabled}")
//    Boolean swaggerEnabled;

    @Bean
    public Docket docket() {
        return new Docket(DocumentationType.OAS_30)
                .apiInfo(apiInfo())
                // 是否开启swagger
                .enable(true)
                .select()
                // 过滤条件，扫描指定路径下的文件
                .apis(RequestHandlerSelectors.basePackage("com.aims.datamodel.ide.controller"))
                // 指定路径处理，PathSelectors.any()代表不过滤任何路径
                //.paths(PathSelectors.any())
                .build();
    }

    private ApiInfo apiInfo() {
        /*作者信息*/
        Contact contact = new Contact("LK", "", "442969153@qq.com");
        return new ApiInfo(
                "Data-Model-Ide",
                "Data-Model-Ide接口文档",
                "v1.0",
                "",
                contact,
                "",
                "",
                new ArrayList()
        );
    }
}
