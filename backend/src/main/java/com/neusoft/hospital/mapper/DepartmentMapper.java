package com.neusoft.hospital.mapper;

import com.neusoft.hospital.entity.Department;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import java.util.List;

@Mapper
public interface DepartmentMapper {

    @Select("SELECT * FROM department WHERE status=1")
    List<Department> selectAll();

    @Select("SELECT * FROM department WHERE id=#{id}")
    Department selectById(Long id);
}
