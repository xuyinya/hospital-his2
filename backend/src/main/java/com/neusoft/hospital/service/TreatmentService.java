package com.neusoft.hospital.service;

import com.neusoft.hospital.common.PageResult;
import com.neusoft.hospital.entity.Treatment;
import com.neusoft.hospital.entity.vo.TreatmentVO;

/**
 * 处置管理 Service 接口
 * 提供处置单的增改查功能，支持更新处置完成状态以及按条件分页查询处置信息。
 */
public interface TreatmentService {

    /**
     * 新增处置单
     *
     * @param treatment 处置对象
     */
    void add(Treatment treatment);

    /**
     * 更新处置单信息
     *
     * @param treatment 处置对象
     */
    void update(Treatment treatment);

    /**
     * 更新处置完成状态
     *
     * @param id     处置ID
     * @param status 处置状态（0-未完成 1-已完成）
     */
    void updateStatus(Long id, Integer status);

    /**
     * 根据处置ID获取处置详情
     *
     * @param id 处置ID
     * @return 处置对象
     */
    Treatment getById(Long id);

    /**
     * 分页查询处置列表（含患者、医生、挂号信息）
     *
     * @param registrationId 挂号ID（可选）
     * @param patientId      患者ID（可选）
     * @param doctorId       医生ID（可选）
     * @param status         处置状态（可选，0-未完成 1-已完成）
     * @param page           当前页码
     * @param size           每页条数
     * @return 分页结果
     */
    PageResult<TreatmentVO> list(Long registrationId, Long patientId, Long doctorId, Integer status, Integer page, Integer size);
}
