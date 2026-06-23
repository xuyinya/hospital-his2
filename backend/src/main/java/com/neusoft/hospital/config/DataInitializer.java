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

/**
 * 数据初始化器
 * <p>
 * 实现CommandLineRunner接口，在Spring Boot应用启动完成后自动执行。
 * 负责创建默认的系统用户账户（管理员/医生）以及初始化患者默认密码。
 * </p>
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    /** 系统用户Mapper，用于操作sys_user表 */
    private final SysUserMapper sysUserMapper;
    /** 患者Mapper，用于操作patient表 */
    private final PatientMapper patientMapper;
    /** BCrypt密码编码器，用于对密码进行加密存储 */
    private final BCryptPasswordEncoder passwordEncoder;

    /**
     * 应用启动后的自动执行逻辑
     * <p>
     * 依次创建预定义的系统账号，然后检查并初始化所有患者的默认登录密码。
     * 如果账号已存在则跳过创建，确保重复启动时不会报错。
     * </p>
     *
     * @param args 命令行参数（未使用）
     */
    @Override
    public void run(String... args) {
        // 创建系统用户（admin 管理员 / doctor 医生）
        createSysUser("admin", "admin123", "系统管理员", "admin", null);
        // 张明 = doctor表ID 1（内科主任医师）, 王强 = doctor表ID 3（外科主任医师）
        createSysUser("doctor", "123456", "张明", "doctor", 1L);
        createSysUser("doctor2", "123456", "王强", "doctor", 3L);

        // 为已有患者设置默认密码（如果没设置过）
        String defaultPwd = passwordEncoder.encode("123456"); // 加密默认密码
        List<Patient> patients = patientMapper.selectList(null, null, 0, 1000);
        for (Patient p : patients) {
            // 只初始化密码为空的患者，已有密码的不覆盖
            if (p.getPassword() == null || p.getPassword().isEmpty()) {
                patientMapper.updatePassword(p.getId(), defaultPwd);
                log.info("患者 {} 密码已初始化 (123456)", p.getPatientName());
            }
        }
    }

    /**
     * 创建系统用户（幂等操作）
     * <p>
     * 先按用户名查询，如果不存在则创建新用户。
     * 密码使用BCrypt加密后存储，确保安全性。
     * </p>
     *
     * @param username 登录用户名
     * @param rawPwd   原始密码（明文）
     * @param realName 用户真实姓名
     * @param role     用户角色（admin/doctor等）
     * @param doctorId 关联的医生ID（医生角色时有效，管理员为null）
     */
    private void createSysUser(String username, String rawPwd, String realName, String role, Long doctorId) {
        SysUser existing = sysUserMapper.selectByUsername(username);
        if (existing == null) {
            SysUser user = new SysUser();
            user.setUsername(username);
            user.setPassword(passwordEncoder.encode(rawPwd)); // 密码加密存储
            user.setRealName(realName);
            user.setRole(role);
            user.setDoctorId(doctorId);
            user.setStatus(1); // 1=启用状态
            sysUserMapper.insert(user);
            log.info("系统账户已创建 ({} / {})", username, rawPwd);
        }
        // 如果用户已存在，不重复创建，保持幂等性
    }
}
