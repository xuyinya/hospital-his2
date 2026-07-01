package com.neusoft.hospital.service;

import com.neusoft.hospital.common.PageResult;
import com.neusoft.hospital.entity.Patient;

/**
 * 患者管理 Service 接口
 * 提供患者的增删改查功能，支持按身份证号查询以及按条件分页查询患者信息。
 */
public interface PatientService {

    /**
     * 新增患者
     *
     * @param patient 患者对象
     */
    void add(Patient patient);

    /**
     * 更新患者信息
     *
     * @param patient 患者对象
     */
    void update(Patient patient);

    /**
     * 删除患者
     *
     * @param id 患者ID
     */
    void delete(Long id);

    void updatePassword(Long id, String encodedPassword);

    /**
     * 根据患者ID获取患者详情
     *
     * @param id 患者ID
     * @return 患者对象
     */
    Patient getById(Long id);

    /**
     * 根据身份证号获取患者
     *
     * @param idCard 身份证号
     * @return 患者对象
     */
    Patient getByIdCard(String idCard);

    /**
     * 分页查询患者列表
     *
     * @param patientName 患者姓名（可选，模糊查询）
     * @param doctorId    医生ID（可选）
     * @param page        当前页码
     * @param size        每页条数
     * @return 分页结果
     */
    PageResult<Patient> list(String patientName, Long doctorId, Integer page, Integer size);
}
