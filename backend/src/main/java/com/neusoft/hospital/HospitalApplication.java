package com.neusoft.hospital;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
@MapperScan("com.neusoft.hospital.mapper")
public class HospitalApplication {

    public static void main(String[] args) {
        SpringApplication.run(HospitalApplication.class, args);
        System.out.println("============================================");
        System.out.println("   东软云医院系统启动成功！");
        System.out.println("   API文档: http://localhost:8080/doc.html");
        System.out.println("   科室接口: http://localhost:8080/api/department/list");
        System.out.println("============================================");
    }
}
