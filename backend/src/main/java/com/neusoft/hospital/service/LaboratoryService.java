package com.neusoft.hospital.service;

import com.neusoft.hospital.common.PageResult;
import com.neusoft.hospital.entity.Laboratory;

/**
 * 检验管理 Service 接口
 * 提供检验单的增删改查功能，支持录入检验结果及参考范围，以及按条件分页查询检验信息。
 */
public interface LaboratoryService {

    /**
     * 新增检验单
     *
     * @param laboratory 检验对象
     */
    void add(Laboratory laboratory);

    /**
     * 更新检验单信息
     *
     * @param laboratory 检验对象
     */
    void update(Laboratory laboratory);

    /**
     * 录入检验结果及参考范围
     *
     * @param id             检验ID
     * @param result         检验结果内容
     * @param referenceRange 结果参考范围
     */
    void updateResult(Long id, String result, String referenceRange);

    /**
     * 根据检验ID获取检验详情
     *
     * @param id 检验ID
     * @return 检验对象
     */
    Laboratory getById(Long id);

    /**
     * 分页查询检验列表（含患者、医生、挂号信息）
     *
     * @param registrationId 挂号ID（可选）
     * @param patientId      患者ID（可选）
     * @param doctorId       医生ID（可选）
     * @param status         检验状态（可选，0-未出结果 1-已出结果）
     * @param page           当前页码
     * @param size           每页条数
     * @return 分页结果
     */
    PageResult<Laboratory> list(Long registrationId, Long patientId, Long doctorId, Integer status, Integer page, Integer size);
}
