package com.neusoft.hospital.config;

import com.neusoft.hospital.entity.Patient;
import com.neusoft.hospital.entity.SysUser;
import com.neusoft.hospital.mapper.PatientMapper;
import com.neusoft.hospital.mapper.SysUserMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final SysUserMapper sysUserMapper;
    private final PatientMapper patientMapper;
    private final BCryptPasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        // 创建系统用户（admin 管理员 / doctor 医生）
        createSysUser("admin", "admin123", "系统管理员", "admin", null);
        // 张明 = doctor表ID 1（内科主任医师）, 王强 = doctor表ID 3（外科主任医师）
        createSysUser("doctor", "123456", "张明", "doctor", 1L);
        createSysUser("doctor2", "123456", "王强", "doctor", 3L);

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

    private void createSysUser(String username, String rawPwd, String realName, String role, Long doctorId) {
        SysUser existing = sysUserMapper.selectByUsername(username);
        if (existing == null) {
            SysUser user = new SysUser();
            user.setUsername(username);
            user.setPassword(passwordEncoder.encode(rawPwd));
            user.setRealName(realName);
            user.setRole(role);
            user.setDoctorId(doctorId);
            user.setStatus(1);
            sysUserMapper.insert(user);
            log.info("系统账户已创建 ({} / {})", username, rawPwd);
        }
    }
}
