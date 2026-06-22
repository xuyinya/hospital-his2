package com.neusoft.hospital.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Knife4jConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("东软云医院系统API")
                        .description("HIS门诊管理系统接口文档")
                        .version("1.0.0")
                        .contact(new Contact().name("东软云医院")));
    }
}
