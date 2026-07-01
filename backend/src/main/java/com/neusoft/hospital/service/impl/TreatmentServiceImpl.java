package com.neusoft.hospital.service.impl;

import com.neusoft.hospital.common.PageResult;
import com.neusoft.hospital.entity.Treatment;
import com.neusoft.hospital.mapper.TreatmentMapper;
import com.neusoft.hospital.service.TreatmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

/**
 * 处置管理服务实现类
 * 负责处置单的新增、修改、状态更新和查询操作，支持关联挂号/患者/医生的多维度分页查询。
 * 处置状态标识是否已完成，用于跟踪门诊处置（如换药、注射等）的执行进度。
 */
@Service
@Transactional
@RequiredArgsConstructor
public class TreatmentServiceImpl implements TreatmentService {

    private final TreatmentMapper treatmentMapper;

    /**
     * 新增处置单
     * 调用 Mapper 将处置实体插入数据库。
     *
     * @param treatment 处置实体
     */
    @Override
    public void add(Treatment treatment) {
        treatmentMapper.insert(treatment);
    }

    /**
     * 修改处置单信息
     * 根据处置ID更新数据库中对应的记录。
     *
     * @param treatment 待更新的处置实体（需包含ID）
     */
    @Override
    public void update(Treatment treatment) {
        treatmentMapper.update(treatment);
    }

    /**
     * 更新处置完成状态
     * 根据处置ID更新状态（如：0-未完成，1-已完成）。
     *
     * @param id     处置ID
     * @param status 目标状态值
     */
    @Override
    public void updateStatus(Long id, Integer status) {
        treatmentMapper.updateStatus(id, status);
    }

    /**
     * 根据处置ID获取处置单详情
     * 调用 Mapper 按主键查询单条处置记录。
     *
     * @param id 处置ID
     * @return 处置实体，若不存在则返回null
     */
    @Override
    public Treatment getById(Long id) {
        return treatmentMapper.selectById(id);
    }

    /**
     * 分页查询处置单列表（含关联信息）
     * 根据挂号ID、患者ID、医生ID和状态进行多条件过滤，计算偏移量后执行分页查询，
     * 同时查询符合条件的总记录数以构造分页结果对象。
     *
     * @param registrationId 挂号ID（可选）
     * @param patientId      患者ID（可选）
     * @param doctorId       医生ID（可选）
     * @param status         状态（可选）
     * @param page           当前页码（从1开始）
     * @param size           每页条数
     * @return 分页结果，含总记录数和当前页数据列表
     */
    @Override
    public PageResult<Treatment> list(Long registrationId, Long patientId, Long doctorId, Integer status, Integer page, Integer size) {
        // 计算数据库分页的起始偏移量
        int offset = (page - 1) * size;
        // 执行多条件分页查询，返回带关联信息的VO列表
        List<Treatment> rows = treatmentMapper.selectList(registrationId, patientId, doctorId, status, offset, size);
        // 查询满足条件的总记录数
        Long total = treatmentMapper.selectCount(registrationId, patientId, doctorId, status);
        return new PageResult<>(total, rows);
    }
}
