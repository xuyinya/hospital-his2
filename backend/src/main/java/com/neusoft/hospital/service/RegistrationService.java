package com.neusoft.hospital.service;

import com.neusoft.hospital.common.PageResult;
import com.neusoft.hospital.entity.Registration;

/**
 * 挂号管理 Service 接口
 * 提供挂号记录的增改查功能，支持更新就诊状态以及按多条件分页查询挂号信息。
 */
public interface RegistrationService {

    /**
     * 新增挂号记录
     *
     * @param registration 挂号对象
     */
    void add(Registration registration);

    /**
     * 更新挂号信息
     *
     * @param registration 挂号对象
     */
    void update(Registration registration);

    /**
     * 更新挂号状态（待诊/已诊/取消）
     *
     * @param id     挂号ID
     * @param status 挂号状态（0-待诊 1-已诊 2-已取消）
     */
    void updateStatus(Long id, Integer status);

    /**
     * 根据挂号ID获取挂号详情
     *
     * @param id 挂号ID
     * @return 挂号对象
     */
    Registration getById(Long id);

    /**
     * 分页查询挂号列表（含患者、医生、科室信息）
     *
     * @param patientId   患者ID（可选）
     * @param doctorId    医生ID（可选）
     * @param status      挂号状态（可选，0-待诊 1-已诊 2-已取消）
     * @param patientName 患者姓名（可选，模糊查询）
     * @param deptId      科室ID（可选）
     * @param page        当前页码
     * @param size        每页条数
     * @return 分页结果
     */
    PageResult<Registration> list(Long patientId, Long doctorId, Integer status, String patientName, Long deptId, Integer page, Integer size);
}
