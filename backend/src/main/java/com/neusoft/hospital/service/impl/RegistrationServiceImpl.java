package com.neusoft.hospital.service.impl;

import com.neusoft.hospital.common.PageResult;
import com.neusoft.hospital.entity.Registration;
import com.neusoft.hospital.entity.vo.RegistrationVO;
import com.neusoft.hospital.mapper.RegistrationMapper;
import com.neusoft.hospital.service.RegistrationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * 挂号管理服务实现类
 * 负责挂号记录的新增、修改、状态更新和查询操作，支持多维度分页查询。
 * 挂号状态包括：待诊、已诊、取消等，贯穿门诊业务流程的起点。
 */
@Service
@RequiredArgsConstructor
public class RegistrationServiceImpl implements RegistrationService {

    private final RegistrationMapper registrationMapper;

    /**
     * 新增挂号记录
     * 调用 Mapper 将挂号实体插入数据库。
     *
     * @param registration 挂号实体
     */
    @Override
    public void add(Registration registration) {
        registrationMapper.insert(registration);
    }

    /**
     * 修改挂号信息
     * 根据挂号ID更新数据库中对应的记录。
     *
     * @param registration 待更新的挂号实体（需包含ID）
     */
    @Override
    public void update(Registration registration) {
        registrationMapper.update(registration);
    }

    /**
     * 更新挂号状态
     * 根据挂号ID更新状态（如：0-待诊，1-已诊，2-取消）。
     *
     * @param id     挂号ID
     * @param status 目标状态值
     */
    @Override
    public void updateStatus(Long id, Integer status) {
        registrationMapper.updateStatus(id, status);
    }

    /**
     * 根据挂号ID获取挂号记录详情
     * 调用 Mapper 按主键查询单条挂号记录。
     *
     * @param id 挂号ID
     * @return 挂号实体，若不存在则返回null
     */
    @Override
    public Registration getById(Long id) {
        return registrationMapper.selectById(id);
    }

    /**
     * 分页查询挂号记录列表（含关联信息）
     * 根据患者ID、医生ID、状态、患者姓名和科室ID进行多条件过滤，
     * 计算偏移量后执行分页查询，同时查询符合条件的总记录数以构造分页结果对象。
     *
     * @param patientId  患者ID（可选）
     * @param doctorId   医生ID（可选）
     * @param status     状态（可选）
     * @param patientName 患者姓名（可选，模糊查询）
     * @param deptId     科室ID（可选）
     * @param page       当前页码（从1开始）
     * @param size       每页条数
     * @return 分页结果，含总记录数和当前页数据列表
     */
    @Override
    public PageResult<RegistrationVO> list(Long patientId, Long doctorId, Integer status, String patientName, Long deptId, Integer page, Integer size) {
        // 计算数据库分页的起始偏移量
        int offset = (page - 1) * size;
        // 执行多条件分页查询，返回带患者、医生、科室等关联信息的VO列表
        List<RegistrationVO> rows = registrationMapper.selectRegistrationVO(patientId, doctorId, status, patientName, deptId, offset, size);
        // 查询满足条件的总记录数
        Long total = registrationMapper.selectRegistrationVOCount(patientId, doctorId, status, patientName, deptId);
        return new PageResult<>(total, rows);
    }
}
