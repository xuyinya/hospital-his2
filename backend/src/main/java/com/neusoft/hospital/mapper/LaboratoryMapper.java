package com.neusoft.hospital.mapper;

import com.neusoft.hospital.entity.Laboratory;
import org.apache.ibatis.annotations.*;
import java.util.List;

/**
 * 检验数据访问层 —— 对应 laboratory 表
 * <p>提供检验记录的增删改查操作，包括录入检验结果（含参考范围）、分页查询检验视图等。</p>
 */
@Mapper
public interface LaboratoryMapper {

    /**
     * 新增检验记录
     * <p>SQL：插入检验信息（关联挂号/患者/检验类型/名称/费用），初始状态为 0（未完成）</p>
     *
     * @param laboratory 检验实体
     * @return 影响的行数
     */
    @Insert("INSERT INTO laboratory(registration_id, patient_id, lab_type, lab_name, fee, status) " +
            "VALUES(#{registrationId}, #{patientId}, #{labType}, #{labName}, #{fee}, 0)")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(Laboratory laboratory);

    /**
     * 更新检验信息
     * <p>SQL：按主键更新检验的类型、名称和费用</p>
     *
     * @param laboratory 检验实体（需包含 id）
     * @return 影响的行数
     */
    @Update("UPDATE laboratory SET lab_type=#{labType}, lab_name=#{labName}, fee=#{fee} WHERE id=#{id}")
    int update(Laboratory laboratory);

    /**
     * 录入检验结果并更新状态
     * <p>SQL：填入检验结果和参考范围，将状态置为 1（已完成），并记录完成时间</p>
     *
     * @param id             检验ID
     * @param result         检验结果文本
     * @param referenceRange 参考范围
     * @return 影响的行数
     */
    @Update("UPDATE laboratory SET result=#{result}, reference_range=#{referenceRange}, status=1, lab_time=NOW() WHERE id=#{id}")
    int updateResult(@Param("id") Long id, @Param("result") String result, @Param("referenceRange") String referenceRange);

    /**
     * 根据ID查询检验
     * <p>SQL：按主键 id 查询检验记录</p>
     *
     * @param id 检验ID
     * @return 检验实体，未找到返回 null
     */
    @Select("SELECT * FROM laboratory WHERE id=#{id}")
    Laboratory selectById(Long id);

    /**
     * 分页查询检验列表（含患者/医生/科室信息）
     * <p>XML 动态 SQL：按挂号ID、患者ID、医生ID、状态等多条件分页查询检验视图</p>
     *
     * @param registrationId 挂号ID（可选）
     * @param patientId      患者ID（可选）
     * @param doctorId       医生ID（可选）
     * @param status         状态（可选，0=未完成，1=已完成）
     * @param offset         分页偏移量
     * @param pageSize       每页条数
     * @return 检验分页列表（含关联字段）
     */
    List<Laboratory> selectList(@Param("registrationId") Long registrationId,
                                           @Param("patientId") Long patientId,
                                           @Param("doctorId") Long doctorId,
                                           @Param("status") Integer status,
                                           @Param("offset") int offset,
                                           @Param("pageSize") int pageSize);

    /**
     * 查询检验总数（用于分页）
     * <p>XML 动态 SQL：按挂号ID、患者ID、医生ID、状态统计记录数</p>
     *
     * @param registrationId 挂号ID（可选）
     * @param patientId      患者ID（可选）
     * @param doctorId       医生ID（可选）
     * @param status         状态（可选）
     * @return 符合条件的检验总数
     */
    Long selectCount(@Param("registrationId") Long registrationId,
                                  @Param("patientId") Long patientId,
                                  @Param("doctorId") Long doctorId,
                                  @Param("status") Integer status);
}
