package com.neusoft.hospital.service;

import com.neusoft.hospital.common.PageResult;
import com.neusoft.hospital.entity.Payment;
import com.neusoft.hospital.entity.vo.PaymentVO;

/**
 * 收费管理 Service 接口
 * 提供收费记录的创建、支付状态更新以及按条件分页查询功能。
 */
public interface PaymentService {

    /**
     * 新增收费记录
     *
     * @param payment 收费对象
     */
    void add(Payment payment);

    /**
     * 更新支付状态
     *
     * @param id     收费ID
     * @param status 支付状态（0-未支付 1-已支付）
     */
    void updateStatus(Long id, Integer status);

    /**
     * 根据收费ID获取收费详情
     *
     * @param id 收费ID
     * @return 收费对象
     */
    Payment getById(Long id);

    /**
     * 分页查询收费列表（含患者、挂号信息）
     *
     * @param patientName 患者姓名（可选，模糊查询）
     * @param status      支付状态（可选，0-未支付 1-已支付）
     * @param page        当前页码
     * @param size        每页条数
     * @return 分页结果
     */
    PageResult<PaymentVO> list(String patientName, Integer status, Integer page, Integer size);
}
