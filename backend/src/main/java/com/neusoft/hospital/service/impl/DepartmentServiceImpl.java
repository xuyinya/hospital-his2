package com.neusoft.hospital.service.impl;

import com.neusoft.hospital.entity.Department;
import com.neusoft.hospital.mapper.DepartmentMapper;
import com.neusoft.hospital.service.DepartmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DepartmentServiceImpl implements DepartmentService {

    private final DepartmentMapper departmentMapper;

    @Override
    public List<Department> list() {
        return departmentMapper.selectAll();
    }

    @Override
    public Department getById(Long id) {
        return departmentMapper.selectById(id);
    }
}
