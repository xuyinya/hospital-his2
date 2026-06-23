package com.neusoft.hospital.service.impl;

import com.neusoft.hospital.entity.Department;
import com.neusoft.hospital.mapper.DepartmentMapper;
import com.neusoft.hospital.service.DepartmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * 科室管理服务实现类
 * 提供科室信息的查询功能，包括获取科室列表和根据ID查询单个科室。
 * 科室数据为只读，不支持新增、修改和删除操作。
 */
@Service
@RequiredArgsConstructor
public class DepartmentServiceImpl implements DepartmentService {

    private final DepartmentMapper departmentMapper;

    /**
     * 获取所有科室列表
     * 调用 Mapper 查询全部科室记录，返回无分页的完整列表。
     *
     * @return 科室实体列表
     */
    @Override
    public List<Department> list() {
        return departmentMapper.selectAll();
    }

    /**
     * 根据科室ID获取科室详情
     * 调用 Mapper 按主键查询单条科室记录。
     *
     * @param id 科室ID
     * @return 科室实体，若不存在则返回null
     */
    @Override
    public Department getById(Long id) {
        return departmentMapper.selectById(id);
    }
}
