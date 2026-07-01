package com.neusoft.hospital.service;

import com.neusoft.hospital.common.PageResult;
import com.neusoft.hospital.entity.MedicalRecord;

/**
 * 病历管理 Service 接口
 * 提供病历的增改查功能，支持按挂号ID查询病历以及分页查询病历信息（含患者、医生、挂号信息）。
 */
public interface MedicalRecordService {

    /**
     * 新增病历
     *
     * @param medicalRecord 病历对象
     */
    void add(MedicalRecord medicalRecord);

    /**
     * 更新病历信息
     *
     * @param medicalRecord 病历对象
     */
    void update(MedicalRecord medicalRecord);

    void delete(Long id);

    /**
     * 根据病历ID获取病历详情
     *
     * @param id 病历ID
     * @return 病历对象
     */
    MedicalRecord getById(Long id);

    /**
     * 根据挂号ID获取病历
     *
     * @param registrationId 挂号ID
     * @return 病历对象
     */
    MedicalRecord getByRegistrationId(Long registrationId);

    /**
     * 分页查询病历列表（含患者、医生、挂号信息）
     *
     * @param registrationId 挂号ID（可选）
     * @param patientId      患者ID（可选）
     * @param doctorId       医生ID（可选）
     * @param patientName    患者姓名（可选，模糊查询）
     * @param page           当前页码
     * @param size           每页条数
     * @return 分页结果
     */
    PageResult<MedicalRecord> list(Long registrationId, Long patientId, Long doctorId, String patientName, Integer page, Integer size);
}
