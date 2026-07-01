package com.neusoft.hospital.mapper;

import com.neusoft.hospital.entity.Doctor;
import org.apache.ibatis.annotations.*;
import java.util.List;

/**
 * 医生数据访问层 —— 对应 doctor 表
 * <p>提供医生信息的增删改查操作，包括按科室查询、分页查询医生（含科室名称）等。</p>
 */
@Mapper
public interface DoctorMapper {

    /**
     * 查询所有启用的医生列表（含科室名称）
     * <p>XML 动态 SQL：连表查询 doctor + department，返回带科室名称的医生信息</p>
     *
     * @return 启用的医生列表（含科室名称）
     */
    List<Doctor> selectAll();

    /**
     * 分页查询医生列表（含科室名称）
     * <p>XML 动态 SQL：按医生姓名和科室 ID 模糊分页查询，结果包含科室名称</p>
     *
     * @param doctorName 医生姓名（可选，模糊匹配）
     * @param deptId     科室ID（可选）
     * @param offset     分页偏移量
     * @param pageSize   每页条数
     * @return 医生分页列表（含科室名称）
     */
    List<Doctor> selectPage(@Param("doctorName") String doctorName,
                                 @Param("deptId") Long deptId,
                                 @Param("offset") int offset,
                                 @Param("pageSize") int pageSize);

    /**
     * 查询医生总数（用于分页）
     * <p>XML 动态 SQL：按姓名和科室 ID 统计医生总记录数</p>
     *
     * @param doctorName 医生姓名（可选）
     * @param deptId     科室ID（可选）
     * @return 符合条件的医生总数
     */
    Long selectCount(@Param("doctorName") String doctorName,
                       @Param("deptId") Long deptId);

    /**
     * 根据ID查询医生
     * <p>SQL：按主键 id 查询医生记录</p>
     *
     * @param id 医生ID
     * @return 医生实体，未找到返回 null
     */
    @Select("SELECT * FROM doctor WHERE id=#{id}")
    Doctor selectById(Long id);

    /**
     * 根据科室ID查询启用的医生列表（含科室名称）
     * <p>XML 动态 SQL：连表查询指定科室下的医生，含科室名称</p>
     *
     * @param deptId 科室ID
     * @return 该科室下的医生列表（含科室名称）
     */
    List<Doctor> selectByDeptId(@Param("deptId") Long deptId);

    /**
     * 新增医生记录
     * <p>SQL：插入医生基础信息，自动生成主键回填到实体 id 属性</p>
     *
     * @param doctor 医生实体
     * @return 影响的行数
     */
    @Insert("INSERT INTO doctor(doctor_name, dept_id, title, specialty, status) VALUES(#{doctorName}, #{deptId}, #{title}, #{specialty}, #{status})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(Doctor doctor);

    /**
     * 更新医生信息
     * <p>SQL：按主键更新医生的姓名、科室、职称、专长和状态</p>
     *
     * @param doctor 医生实体（需包含 id）
     * @return 影响的行数
     */
    @Update("UPDATE doctor SET doctor_name=#{doctorName}, dept_id=#{deptId}, title=#{title}, specialty=#{specialty}, status=#{status} WHERE id=#{id}")
    int update(Doctor doctor);

    /**
     * 根据ID删除医生
     * <p>SQL：按主键物理删除医生记录</p>
     *
     * @param id 医生ID
     * @return 影响的行数
     */
    @Delete("DELETE FROM doctor WHERE id=#{id}")
    int deleteById(Long id);

    /**
     * 统计某医生的挂号数量
     * <p>SQL：查询 registration 表中指定医生的挂号记录数</p>
     *
     * @param doctorId 医生ID
     * @return 挂号记录总数
     */
    @Select("SELECT COUNT(*) FROM registration WHERE doctor_id=#{doctorId} AND status=0")
    long countRegistrationsByDoctorId(@Param("doctorId") Long doctorId);
}
