package com.neusoft.hospital.service.impl;

import com.neusoft.hospital.common.PageResult;
import com.neusoft.hospital.entity.Payment;
import com.neusoft.hospital.mapper.PaymentMapper;
import com.neusoft.hospital.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

/**
 * 收费管理服务实现类
 * 负责收费记录的新增、状态更新和查询操作，支持按患者姓名和支付状态的分页查询。
 * 收费记录关联挂号和患者，支付状态标识是否已缴费。
 */
@Service
@Transactional
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentMapper paymentMapper;

    /**
     * 新增收费记录
     * 调用 Mapper 将收费实体插入数据库。
     *
     * @param payment 收费实体
     */
    @Override
    public void add(Payment payment) {
        paymentMapper.insert(payment);
    }

    /**
     * 更新收费状态
     * 根据收费ID更新支付状态（如：0-未支付，1-已支付）。
     *
     * @param id     收费ID
     * @param status 目标状态值
     */
    @Override
    public void updateStatus(Long id, Integer status) {
        paymentMapper.updateStatus(id, status);
    }

    @Override
    public void delete(Long id) {
        paymentMapper.deleteById(id);
    }

    /**
     * 根据收费ID获取收费记录详情
     * 调用 Mapper 按主键查询单条收费记录。
     *
     * @param id 收费ID
     * @return 收费实体，若不存在则返回null
     */
    @Override
    public Payment getById(Long id) {
        return paymentMapper.selectById(id);
    }

    /**
     * 分页查询收费记录列表（含关联信息）
     * 根据患者姓名和支付状态进行过滤，计算偏移量后执行分页查询，
     * 同时查询符合条件的总记录数以构造分页结果对象。
     *
     * @param patientName 患者姓名（可选，模糊查询）
     * @param status      支付状态（可选）
     * @param page        当前页码（从1开始）
     * @param size        每页条数
     * @return 分页结果，含总记录数和当前页数据列表
     */
    @Override
    public PageResult<Payment> list(String patientName, Integer status, Integer page, Integer size) {
        // 计算数据库分页的起始偏移量
        int offset = (page - 1) * size;
        // 执行多条件分页查询，返回带患者等关联信息的VO列表
        List<Payment> rows = paymentMapper.selectList(patientName, status, offset, size);
        // 查询满足条件的总记录数
        Long total = paymentMapper.selectCount(patientName, status);
        return new PageResult<>(total, rows);
    }
}
