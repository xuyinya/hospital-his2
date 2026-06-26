package com.neusoft.hospital.service;

import com.neusoft.hospital.common.PageResult;
import com.neusoft.hospital.entity.Doctor;
import java.util.List;

/**
 * 医生管理 Service 接口
 * 提供医生的增删改查功能，支持按科室查询医生以及分页查询医生信息（含科室名称）。
 */
public interface DoctorService {

    /**
     * 获取所有医生列表（含科室名称）
     *
     * @return 医生列表（含科室名称）
     */
    List<Doctor> list();

    /**
     * 分页查询医生信息（含科室名称）
     *
     * @param doctorName 医生姓名（可选，模糊查询）
     * @param deptId     科室ID（可选）
     * @param page       当前页码
     * @param size       每页条数
     * @return 分页结果
     */
    PageResult<Doctor> listPage(String doctorName, Long deptId, Integer page, Integer size);

    /**
     * 根据医生ID获取医生详情
     *
     * @param id 医生ID
     * @return 医生对象
     */
    Doctor getById(Long id);

    /**
     * 根据科室ID获取医生列表（含科室名称）
     *
     * @param deptId 科室ID
     * @return 医生列表（含科室名称）
     */
    List<Doctor> getByDeptId(Long deptId);

    /**
     * 新增医生
     *
     * @param doctor 医生对象
     */
    void add(Doctor doctor);

    /**
     * 更新医生信息
     *
     * @param doctor 医生对象
     */
    void update(Doctor doctor);

    /**
     * 删除医生
     *
     * @param id 医生ID
     */
    void delete(Long id);
}
