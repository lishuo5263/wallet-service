package com.ecochain.ledger.config;

import static com.google.common.base.Predicates.or;
import static springfox.documentation.builders.PathSelectors.regex;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.async.DeferredResult;


import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * SwaggerConfig
 */
@Configuration
@EnableSwagger2
@ComponentScan("com.ecochain.ledger.web.rest")
public class SwaggerConfig {

  
    @Bean
    public Docket testApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("test")
                .genericModelSubstitutes(DeferredResult.class)
//                .genericModelSubstitutes(ResponseEntity.class)
                .useDefaultResponseMessages(false)
                .forCodeGeneration(true)
                .pathMapping("/")// base，最终调用接口后会和paths拼接在一起
                .select()
                .paths(or(regex("/api/.*")))//过滤的接口
                .build()
                .apiInfo(testApiInfo());
    }

  

    private ApiInfo testApiInfo() {
        return new ApiInfoBuilder()
            .title("Leger Service Platform API")//大标题
            .description(" Micro service application.")//详细描述
            .version("1.0")//版本
            .termsOfServiceUrl("http://jhipster.github.io/")
            .license("The Apache License, Version 2.0")
            .licenseUrl("http://www.apache.org/licenses/LICENSE-2.0.html")
            .build();
    }
 
}