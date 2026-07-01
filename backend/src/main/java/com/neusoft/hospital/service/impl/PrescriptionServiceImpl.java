package com.neusoft.hospital.service.impl;

import com.neusoft.hospital.common.PageResult;
import com.neusoft.hospital.entity.Prescription;
import com.neusoft.hospital.entity.PrescriptionDetail;
import com.neusoft.hospital.mapper.PrescriptionDetailMapper;
import com.neusoft.hospital.mapper.PrescriptionMapper;
import com.neusoft.hospital.service.PrescriptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.util.List;

/**
 * 处方管理服务实现类
 * 负责处方单的新增、处方明细录入、状态更新和查询操作。
 * 新增处方明细时自动计算金额并更新处方总金额，确保数据一致性。
 * 支持按挂号/患者/医生/状态的多维度分页查询。
 */
@Service
@Transactional
@RequiredArgsConstructor
public class PrescriptionServiceImpl implements PrescriptionService {

    private final PrescriptionMapper prescriptionMapper;
    private final PrescriptionDetailMapper prescriptionDetailMapper;

    /**
     * 新增处方
     * 调用 Mapper 将处方实体插入数据库。
     *
     * @param prescription 处方实体
     */
    @Override
    public void add(Prescription prescription) {
        prescriptionMapper.insert(prescription);
    }

    /**
     * 新增处方明细（事务处理）
     * 计算单项金额（单价 x 数量）后插入明细记录，然后重新汇总该处方的所有明细金额，
     * 更新处方主表的总金额字段，保证明细与总金额数据一致。
     *
     * @param detail 处方明细实体（需包含处方ID、单价、数量）
     */
    @Override
    @Transactional
    public void addDetail(PrescriptionDetail detail) {
        // 计算单项金额 = 单价 x 数量
        BigDecimal amount = detail.getUnitPrice().multiply(BigDecimal.valueOf(detail.getQuantity()));
        detail.setAmount(amount);
        // 插入处方明细记录
        prescriptionDetailMapper.insert(detail);
        // 重新汇总该处方的所有明细总金额，并更新到处方主表
        BigDecimal total = prescriptionDetailMapper.sumAmountByPrescriptionId(detail.getPrescriptionId());
        prescriptionMapper.updateTotalAmount(detail.getPrescriptionId(), total);
    }

    /**
     * 更新处方状态
     * 根据处方ID更新状态（如：0-未取药，1-已取药）。
     *
     * @param id     处方ID
     * @param status 目标状态值
     */
    @Override
    public void updateStatus(Long id, Integer status) {
        prescriptionMapper.updateStatus(id, status);
    }

    /**
     * 根据处方ID获取处方详情
     * 调用 Mapper 按主键查询单条处方记录。
     *
     * @param id 处方ID
     * @return 处方实体，若不存在则返回null
     */
    @Override
    public Prescription getById(Long id) {
        return prescriptionMapper.selectById(id);
    }

    /**
     * 分页查询处方列表（含关联信息）
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
    public PageResult<Prescription> list(Long registrationId, Long patientId, Long doctorId, Integer status, Integer page, Integer size) {
        // 计算数据库分页的起始偏移量
        int offset = (page - 1) * size;
        // 执行多条件分页查询，返回带关联信息的VO列表
        List<Prescription> rows = prescriptionMapper.selectList(registrationId, patientId, doctorId, status, offset, size);
        // 查询满足条件的总记录数
        Long total = prescriptionMapper.selectCount(registrationId, patientId, doctorId, status);
        return new PageResult<>(total, rows);
    }

    /**
     * 获取指定处方的明细列表
     * 根据处方ID查询其下所有药品明细，包含药品名称、单价、数量、金额等信息。
     *
     * @param prescriptionId 处方ID
     * @return 处方明细VO列表
     */
    @Override
    public List<PrescriptionDetail> getDetails(Long prescriptionId) {
        return prescriptionDetailMapper.selectByPrescriptionId(prescriptionId);
    }
}
