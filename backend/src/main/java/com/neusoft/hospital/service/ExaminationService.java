package com.neusoft.hospital.service;

import com.neusoft.hospital.common.PageResult;
import com.neusoft.hospital.entity.Examination;
import com.neusoft.hospital.entity.vo.ExaminationVO;

/**
 * 检查管理 Service 接口
 * 提供检查单的增删改查功能，支持录入检查结果以及按条件分页查询检查信息。
 */
public interface ExaminationService {

    /**
     * 新增检查单
     *
     * @param examination 检查对象
     */
    void add(Examination examination);

    /**
     * 更新检查单信息
     *
     * @param examination 检查对象
     */
    void update(Examination examination);

    /**
     * 录入检查结果
     *
     * @param id     检查ID
     * @param result 检查结果内容
     */
    void updateResult(Long id, String result);

    /**
     * 根据检查ID获取检查详情
     *
     * @param id 检查ID
     * @return 检查对象
     */
    Examination getById(Long id);

    /**
     * 分页查询检查列表（含患者、医生、挂号信息）
     *
     * @param registrationId 挂号ID（可选）
     * @param patientId      患者ID（可选）
     * @param doctorId       医生ID（可选）
     * @param status         检查状态（可选，0-未出结果 1-已出结果）
     * @param page           当前页码
     * @param size           每页条数
     * @return 分页结果
     */
    PageResult<ExaminationVO> list(Long registrationId, Long patientId, Long doctorId, Integer status, Integer page, Integer size);
}
