package com.neusoft.hospital.service;

import com.neusoft.hospital.entity.Department;
import java.util.List;

public interface DepartmentService {
    List<Department> list();
    Department getById(Long id);
}
