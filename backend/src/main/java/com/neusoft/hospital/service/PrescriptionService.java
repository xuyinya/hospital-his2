package com.neusoft.hospital.service;

import com.neusoft.hospital.common.PageResult;
import com.neusoft.hospital.entity.Prescription;
import com.neusoft.hospital.entity.PrescriptionDetail;
import java.util.List;

/**
 * 处方管理 Service 接口
 * 提供处方及处方明细的增改查功能，支持确认取药状态更新以及按条件分页查询处方信息。
 */
public interface PrescriptionService {

    /**
     * 新增处方
     *
     * @param prescription 处方对象
     */
    void add(Prescription prescription);

    /**
     * 新增处方明细
     *
     * @param detail 处方明细对象
     */
    void addDetail(PrescriptionDetail detail);

    /**
     * 更新处方状态（确认取药）
     *
     * @param id     处方ID
     * @param status 处方状态（0-未取药 1-已取药）
     */
    void updateStatus(Long id, Integer status);

    /**
     * 根据处方ID获取处方详情
     *
     * @param id 处方ID
     * @return 处方对象
     */
    Prescription getById(Long id);

    /**
     * 分页查询处方列表（含患者、医生、挂号信息）
     *
     * @param registrationId 挂号ID（可选）
     * @param patientId      患者ID（可选）
     * @param doctorId       医生ID（可选）
     * @param status         处方状态（可选，0-未取药 1-已取药）
     * @param page           当前页码
     * @param size           每页条数
     * @return 分页结果
     */
    PageResult<Prescription> list(Long registrationId, Long patientId, Long doctorId, Integer status, Integer page, Integer size);

    /**
     * 根据处方ID获取处方明细列表（含药品信息）
     *
     * @param prescriptionId 处方ID
     * @return 处方明细列表（含药品信息）
     */
    List<PrescriptionDetail> getDetails(Long prescriptionId);
}
