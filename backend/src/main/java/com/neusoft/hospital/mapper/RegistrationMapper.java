package com.neusoft.hospital.mapper;

import com.neusoft.hospital.entity.Registration;
import org.apache.ibatis.annotations.*;
import java.util.List;

/**
 * 挂号数据访问层 —— 对应 registration 表
 * <p>提供挂号记录的增删改查操作，包括更新挂号状态、分页查询挂号视图（含患者/医生/科室信息）等。</p>
 */
@Mapper
public interface RegistrationMapper {

    /**
     * 新增挂号记录
     * <p>SQL：插入挂号信息（关联患者/医生/科室），初始状态为 0（待诊），记录当前时间为挂号时间</p>
     *
     * @param registration 挂号实体
     * @return 影响的行数
     */
    @Insert("INSERT INTO registration(patient_id, doctor_id, dept_id, reg_type, reg_fee, reg_time, status, chief_complaint, present_illness) " +
            "VALUES(#{patientId}, #{doctorId}, #{deptId}, #{regType}, #{regFee}, NOW(), 0, #{chiefComplaint}, #{presentIllness})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(Registration registration);

    /**
     * 更新挂号信息
     * <p>SQL：按主键更新挂号的患者、医生、科室、类型和费用</p>
     *
     * @param registration 挂号实体（需包含 id）
     * @return 影响的行数
     */
    @Update("UPDATE registration SET patient_id=#{patientId}, doctor_id=#{doctorId}, dept_id=#{deptId}, " +
            "reg_type=#{regType}, reg_fee=#{regFee} WHERE id=#{id}")
    int update(Registration registration);

    /**
     * 更新挂号状态
     * <p>SQL：按主键更新挂号状态（0=待诊，1=已诊，2=已取消）</p>
     *
     * @param id     挂号ID
     * @param status 状态值
     * @return 影响的行数
     */
    @Update("UPDATE registration SET status=#{status} WHERE id=#{id}")
    int updateStatus(@Param("id") Long id, @Param("status") Integer status);

    @Delete("DELETE FROM registration WHERE id=#{id}")
    int deleteById(Long id);

    /**
     * 根据ID查询挂号
     * <p>SQL：按主键 id 查询挂号记录</p>
     *
     * @param id 挂号ID
     * @return 挂号实体，未找到返回 null
     */
    @Select("SELECT * FROM registration WHERE id=#{id}")
    Registration selectById(Long id);

    /**
     * 分页查询挂号列表（含患者/医生/科室名称）
     * <p>XML 动态 SQL：按患者ID、医生ID、状态、患者姓名、科室ID等多条件分页查询挂号视图</p>
     *
     * @param patientId   患者ID（可选）
     * @param doctorId    医生ID（可选）
     * @param status      状态（可选，0=待诊，1=已诊，2=已取消）
     * @param patientName 患者姓名（可选，模糊匹配）
     * @param deptId      科室ID（可选）
     * @param offset      分页偏移量
     * @param pageSize    每页条数
     * @return 挂号分页列表（含关联字段）
     */
    List<Registration> selectList(@Param("patientId") Long patientId,
                                               @Param("doctorId") Long doctorId,
                                               @Param("status") Integer status,
                                               @Param("patientName") String patientName,
                                               @Param("deptId") Long deptId,
                                               @Param("offset") int offset,
                                               @Param("pageSize") int pageSize);

    /**
     * 查询挂号总数（用于分页）
     * <p>XML 动态 SQL：按患者ID、医生ID、状态、患者姓名、科室ID统计记录数</p>
     *
     * @param patientId   患者ID（可选）
     * @param doctorId    医生ID（可选）
     * @param status      状态（可选）
     * @param patientName 患者姓名（可选）
     * @param deptId      科室ID（可选）
     * @return 符合条件的挂号总数
     */
    Long selectCount(@Param("patientId") Long patientId,
                                    @Param("doctorId") Long doctorId,
                                    @Param("status") Integer status,
                                    @Param("patientName") String patientName,
                                    @Param("deptId") Long deptId);
}
