package com.neusoft.hospital.service.impl;

import com.neusoft.hospital.common.PageResult;
import com.neusoft.hospital.entity.Laboratory;
import com.neusoft.hospital.mapper.LaboratoryMapper;
import com.neusoft.hospital.service.LaboratoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

/**
 * 检验管理服务实现类
 * 负责检验单的新增、修改、结果录入和查询操作，支持关联挂号/患者/医生的多维度分页查询。
 * 与检查管理类似，但面向的是化验/检验项目，录入结果时需同时填写参考范围。
 */
@Service
@Transactional
@RequiredArgsConstructor
public class LaboratoryServiceImpl implements LaboratoryService {

    private final LaboratoryMapper laboratoryMapper;

    /**
     * 新增检验单
     * 调用 Mapper 将检验实体插入数据库。
     *
     * @param laboratory 检验实体
     */
    @Override
    public void add(Laboratory laboratory) {
        laboratoryMapper.insert(laboratory);
    }

    /**
     * 修改检验单信息
     * 根据检验ID更新数据库中对应的记录。
     *
     * @param laboratory 待更新的检验实体（需包含ID）
     */
    @Override
    public void update(Laboratory laboratory) {
        laboratoryMapper.update(laboratory);
    }

    /**
     * 录入检验结果及参考范围
     * 根据检验ID更新检验结果和参考范围字段，由检验科室填写完成。
     *
     * @param id             检验ID
     * @param result         检验结果文本
     * @param referenceRange 参考范围（正常值区间）
     */
    @Override
    public void updateResult(Long id, String result, String referenceRange) {
        laboratoryMapper.updateResult(id, result, referenceRange);
    }

    /**
     * 根据检验ID获取检验单详情
     * 调用 Mapper 按主键查询单条检验记录。
     *
     * @param id 检验ID
     * @return 检验实体，若不存在则返回null
     */
    @Override
    public Laboratory getById(Long id) {
        return laboratoryMapper.selectById(id);
    }

    /**
     * 分页查询检验单列表（含关联信息）
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
    public PageResult<Laboratory> list(Long registrationId, Long patientId, Long doctorId, Integer status, Integer page, Integer size) {
        // 计算数据库分页的起始偏移量
        int offset = (page - 1) * size;
        // 执行多条件分页查询，返回带关联信息的VO列表
        List<Laboratory> rows = laboratoryMapper.selectList(registrationId, patientId, doctorId, status, offset, size);
        // 查询满足条件的总记录数
        Long total = laboratoryMapper.selectCount(registrationId, patientId, doctorId, status);
        return new PageResult<>(total, rows);
    }
}
