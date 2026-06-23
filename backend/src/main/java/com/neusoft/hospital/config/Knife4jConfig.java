package com.neusoft.hospital.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Knife4j接口文档配置类
 * <p>
 * 集成Knife4j（基于Swagger/OpenAPI 3.0），自动生成RESTful API文档。
 * 启动后可通过 http://localhost:8080/doc.html 访问文档页面。
 * </p>
 */
@Configuration
public class Knife4jConfig {

    /**
     * 自定义OpenAPI配置
     * <p>
     * 配置API文档的标题、描述、版本号和联系信息。
     * Spring Doc会自动扫描所有Controller的@Operation注解生成接口文档。
     * </p>
     *
     * @return OpenAPI实例
     */
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
