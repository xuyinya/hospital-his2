package com.neusoft.hospital.mapper;

import com.neusoft.hospital.entity.Department;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import java.util.List;

/**
 * 科室数据访问层 —— 对应 department 表
 * <p>提供科室信息的查询操作，包括查询所有启用科室、根据ID查询科室等。</p>
 */
@Mapper
public interface DepartmentMapper {

    /**
     * 查询所有启用的科室列表
     * <p>SQL：查询 department 表中 status=1 的所有记录</p>
     *
     * @return 启用的科室列表
     */
    @Select("SELECT * FROM department WHERE status=1")
    List<Department> selectAll();

    /**
     * 根据ID查询科室
     * <p>SQL：按主键 id 查询科室记录</p>
     *
     * @param id 科室ID
     * @return 科室实体，未找到返回 null
     */
    @Select("SELECT * FROM department WHERE id=#{id}")
    Department selectById(Long id);
}
