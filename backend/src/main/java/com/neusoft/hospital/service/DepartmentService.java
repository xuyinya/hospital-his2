package com.neusoft.hospital.service;

import com.neusoft.hospital.entity.Department;
import java.util.List;

/**
 * 科室管理 Service 接口
 * 提供科室信息的查询功能，包括获取科室列表和根据ID查询科室详情。
 */
public interface DepartmentService {

    /**
     * 获取所有科室列表
     *
     * @return 科室列表
     */
    List<Department> list();

    /**
     * 根据科室ID获取科室详情
     *
     * @param id 科室ID
     * @return 科室对象
     */
    Department getById(Long id);
}
