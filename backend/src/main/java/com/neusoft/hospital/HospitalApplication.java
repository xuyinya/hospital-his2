package com.neusoft.hospital;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

/**
 * 东软云医院系统 - 应用启动类
 * <p>
 * 作为Spring Boot应用的入口，负责启动整个HIS门诊管理系统。
 * 排除SecurityAutoConfiguration以使用自定义的JWT认证方案，而非Spring Security默认的登录表单。
 * </p>
 */
@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
@MapperScan("com.neusoft.hospital.mapper") // 扫描MyBatis Mapper接口所在包
public class HospitalApplication {

    /**
     * 应用主入口方法
     *
     * @param args 命令行参数
     */
    public static void main(String[] args) {
        // 启动Spring Boot应用
        SpringApplication.run(HospitalApplication.class, args);
        // 控制台输出启动成功的提示信息
        System.out.println("============================================");
        System.out.println("   东软云医院系统启动成功！");
        System.out.println("   API文档: http://localhost:8080/doc.html");
        System.out.println("   科室接口: http://localhost:8080/api/department/list");
        System.out.println("============================================");
    }
}
