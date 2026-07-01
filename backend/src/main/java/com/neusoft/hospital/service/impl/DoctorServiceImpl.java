package com.neusoft.hospital.service.impl;

import com.neusoft.hospital.common.PageResult;
import com.neusoft.hospital.entity.Doctor;
import com.neusoft.hospital.entity.SysUser;
import com.neusoft.hospital.mapper.DoctorMapper;
import com.neusoft.hospital.mapper.SysUserMapper;
import com.neusoft.hospital.service.DoctorService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

/**
 * 医生管理服务实现类
 * 负责医生信息的查询、新增、修改和删除操作，提供分页查询和科室关联查询功能。
 * 删除医生时会校验是否存在关联的挂号记录，防止误删。
 */
@Service
@Transactional
@RequiredArgsConstructor
public class DoctorServiceImpl implements DoctorService {

    private final DoctorMapper doctorMapper;
    private final SysUserMapper sysUserMapper;
    private final BCryptPasswordEncoder passwordEncoder;

    /**
     * 获取所有医生列表（含科室名称，无分页）
     * 调用 Mapper 查询全部启用的医生记录，含科室名称。
     *
     * @return 医生列表（含科室名称）
     */
    @Override
    public List<Doctor> list() {
        return doctorMapper.selectAll();
    }

    /**
     * 分页查询医生信息（含科室名称）
     * 根据医生姓名、科室ID进行模糊过滤，计算偏移量后执行分页查询，
     * 同时查询符合条件的总记录数以构造分页结果对象。
     *
     * @param doctorName 医生姓名（可选，模糊查询）
     * @param deptId     科室ID（可选）
     * @param page       当前页码（从1开始）
     * @param size       每页条数
     * @return 分页结果，含总记录数和当前页数据列表
     */
    @Override
    public PageResult<Doctor> listPage(String doctorName, Long deptId, Integer page, Integer size) {
        // 计算数据库分页的起始偏移量
        int offset = (page - 1) * size;
        // 执行分页查询，返回带科室名称的医生列表
        List<Doctor> rows = doctorMapper.selectPage(doctorName, deptId, offset, size);
        // 查询满足条件的总记录数
        Long total = doctorMapper.selectCount(doctorName, deptId);
        return new PageResult<>(total, rows);
    }

    /**
     * 根据医生ID获取医生详情
     * 调用 Mapper 按主键查询单条医生记录。
     *
     * @param id 医生ID
     * @return 医生实体，若不存在则返回null
     */
    @Override
    public Doctor getById(Long id) {
        return doctorMapper.selectById(id);
    }

    /**
     * 根据科室ID获取该科室下的所有医生（含科室名称）
     * 调用 Mapper 按科室ID查询关联的医生列表，用于前端下拉选择。
     *
     * @param deptId 科室ID
     * @return 该科室下的医生列表（含科室名称）
     */
    @Override
    public List<Doctor> getByDeptId(Long deptId) {
        return doctorMapper.selectByDeptId(deptId);
    }

    /**
     * 新增医生
     * 如果医生状态未设置，则默认为1（正常/启用状态），然后插入数据库。
     *
     * @param doctor 医生实体
     */
    @Override
    public void add(Doctor doctor) {
        // 默认医生状态为启用（1），若未设置则赋予默认值
        if (doctor.getStatus() == null) doctor.setStatus(1);
        doctorMapper.insert(doctor);
        // 自动创建对应用户账户（前端传入username，密码123456）
        String username = doctor.getUsername();
        if (username != null && !username.isEmpty() && sysUserMapper.selectByUsername(username) == null) {
            SysUser user = new SysUser();
            user.setUsername(username);
            user.setPassword(passwordEncoder.encode("123456"));
            user.setRealName(doctor.getDoctorName());
            user.setRole("doctor");
            user.setDoctorId(doctor.getId());
            user.setStatus(1);
            sysUserMapper.insert(user);
        }
    }

    /**
     * 修改医生信息
     * 根据医生ID更新数据库中对应的记录。
     *
     * @param doctor 待更新的医生实体（需包含ID）
     */
    @Override
    public void update(Doctor doctor) {
        doctorMapper.update(doctor);
    }

    /**
     * 删除医生
     * 先校验该医生是否有关联的挂号记录，若有则抛出异常阻止删除，
     * 确保数据的引用完整性。
     *
     * @param id 待删除的医生ID
     * @throws RuntimeException 该医生有挂号记录时抛出
     */
    @Override
    public void delete(Long id) {
        // 只检查待诊挂号（status=0），历史记录不阻止删除
        long count = doctorMapper.countRegistrationsByDoctorId(id);
        if (count > 0) {
            throw new RuntimeException("该医生还有 " + count + " 条待诊记录，不能删除");
        }
        sysUserMapper.deleteByDoctorId(id);
        doctorMapper.deleteById(id);
    }
}
