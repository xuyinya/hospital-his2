package com.neusoft.hospital.mapper;

import com.neusoft.hospital.entity.Examination;
import org.apache.ibatis.annotations.*;
import java.util.List;

/**
 * 检查数据访问层 —— 对应 examination 表
 * <p>提供检查记录的增删改查操作，包括录入检查结果、分页查询检查视图等。</p>
 */
@Mapper
public interface ExaminationMapper {

    /**
     * 新增检查记录
     * <p>SQL：插入检查信息（关联挂号/患者/检查类型/名称/费用），初始状态为 0（未完成）</p>
     *
     * @param examination 检查实体
     * @return 影响的行数
     */
    @Insert("INSERT INTO examination(registration_id, patient_id, exam_type, exam_name, fee, status) " +
            "VALUES(#{registrationId}, #{patientId}, #{examType}, #{examName}, #{fee}, 0)")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(Examination examination);

    /**
     * 更新检查信息
     * <p>SQL：按主键更新检查的类型、名称和费用</p>
     *
     * @param examination 检查实体（需包含 id）
     * @return 影响的行数
     */
    @Update("UPDATE examination SET exam_type=#{examType}, exam_name=#{examName}, fee=#{fee} WHERE id=#{id}")
    int update(Examination examination);

    /**
     * 录入检查结果并更新状态
     * <p>SQL：填入检查结果，将状态置为 1（已完成），并记录完成时间</p>
     *
     * @param id     检查ID
     * @param result 检查结果文本
     * @return 影响的行数
     */
    @Update("UPDATE examination SET result=#{result}, status=1, exam_time=NOW() WHERE id=#{id}")
    int updateResult(@Param("id") Long id, @Param("result") String result);

    /**
     * 根据ID查询检查
     * <p>SQL：按主键 id 查询检查记录</p>
     *
     * @param id 检查ID
     * @return 检查实体，未找到返回 null
     */
    @Select("SELECT * FROM examination WHERE id=#{id}")
    Examination selectById(Long id);

    /**
     * 分页查询检查列表（含患者/医生/科室信息）
     * <p>XML 动态 SQL：按挂号ID、患者ID、医生ID、状态等多条件分页查询检查视图</p>
     *
     * @param registrationId 挂号ID（可选）
     * @param patientId      患者ID（可选）
     * @param doctorId       医生ID（可选）
     * @param status         状态（可选，0=未完成，1=已完成）
     * @param offset         分页偏移量
     * @param pageSize       每页条数
     * @return 检查分页列表（含关联字段）
     */
    List<Examination> selectList(@Param("registrationId") Long registrationId,
                                             @Param("patientId") Long patientId,
                                             @Param("doctorId") Long doctorId,
                                             @Param("status") Integer status,
                                             @Param("offset") int offset,
                                             @Param("pageSize") int pageSize);

    /**
     * 查询检查总数（用于分页）
     * <p>XML 动态 SQL：按挂号ID、患者ID、医生ID、状态统计记录数</p>
     *
     * @param registrationId 挂号ID（可选）
     * @param patientId      患者ID（可选）
     * @param doctorId       医生ID（可选）
     * @param status         状态（可选）
     * @return 符合条件的检查总数
     */
    Long selectCount(@Param("registrationId") Long registrationId,
                                   @Param("patientId") Long patientId,
                                   @Param("doctorId") Long doctorId,
                                   @Param("status") Integer status);
}
