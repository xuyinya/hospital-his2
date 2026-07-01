package com.neusoft.hospital.config;

import com.neusoft.hospital.entity.Patient;
import com.neusoft.hospital.mapper.PatientMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 数据初始化器
 * <p>
 * 实现CommandLineRunner接口，在Spring Boot应用启动完成后自动执行。
 * 负责为已有患者初始化默认登录密码（123456）。
 * 系统用户（sys_user）由 hospital_his.sql 中的初始化脚本创建。
 * </p>
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    /** BCrypt密码编码器，用于对密码进行加密存储 */
    private final BCryptPasswordEncoder passwordEncoder;
    /** 患者Mapper，用于操作patient表 */
    private final PatientMapper patientMapper;

    /**
     * 应用启动后的自动执行逻辑
     * <p>
     * 检查所有患者记录，为密码为空的患者设置默认密码（123456）。
     * 已有密码的患者不会被覆盖，确保多次启动时的幂等性。
     * </p>
     *
     * @param args 命令行参数（未使用）
     */
    @Override
    public void run(String... args) {
        // 为已有患者设置默认密码（如果没设置过）
        String defaultPwd = passwordEncoder.encode("123456");
        List<Patient> patients = patientMapper.selectList(null, null, 0, 1000);
        for (Patient p : patients) {
            if (p.getPassword() == null || p.getPassword().isEmpty()) {
                patientMapper.updatePassword(p.getId(), defaultPwd);
                log.info("患者 {} 密码已初始化 (123456)", p.getPatientName());
            }
        }
    }
}
