package com.neusoft.hospital.service.impl;

import com.neusoft.hospital.common.PageResult;
import com.neusoft.hospital.entity.Examination;
import com.neusoft.hospital.mapper.ExaminationMapper;
import com.neusoft.hospital.service.ExaminationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * 检查管理服务实现类
 * 负责检查单的新增、修改、结果录入和查询操作，支持关联挂号/患者/医生的多维度分页查询。
 */
@Service
@RequiredArgsConstructor
public class ExaminationServiceImpl implements ExaminationService {

    private final ExaminationMapper examinationMapper;

    /**
     * 新增检查单
     * 调用 Mapper 将检查实体插入数据库。
     *
     * @param examination 检查实体
     */
    @Override
    public void add(Examination examination) {
        examinationMapper.insert(examination);
    }

    /**
     * 修改检查单信息
     * 根据检查ID更新数据库中对应的记录。
     *
     * @param examination 待更新的检查实体（需包含ID）
     */
    @Override
    public void update(Examination examination) {
        examinationMapper.update(examination);
    }

    /**
     * 录入检查结果
     * 根据检查ID更新检查结果字段，由医生或检验科室填写完成。
     *
     * @param id     检查ID
     * @param result 检查结果文本
     */
    @Override
    public void updateResult(Long id, String result) {
        examinationMapper.updateResult(id, result);
    }

    /**
     * 根据检查ID获取检查单详情
     * 调用 Mapper 按主键查询单条检查记录。
     *
     * @param id 检查ID
     * @return 检查实体，若不存在则返回null
     */
    @Override
    public Examination getById(Long id) {
        return examinationMapper.selectById(id);
    }

    /**
     * 分页查询检查单列表（含关联信息）
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
    public PageResult<Examination> list(Long registrationId, Long patientId, Long doctorId, Integer status, Integer page, Integer size) {
        // 计算数据库分页的起始偏移量
        int offset = (page - 1) * size;
        // 执行多条件分页查询，返回带关联信息的VO列表
        List<Examination> rows = examinationMapper.selectList(registrationId, patientId, doctorId, status, offset, size);
        // 查询满足条件的总记录数
        Long total = examinationMapper.selectCount(registrationId, patientId, doctorId, status);
        return new PageResult<>(total, rows);
    }
}
