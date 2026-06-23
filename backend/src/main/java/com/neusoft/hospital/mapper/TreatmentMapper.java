package com.neusoft.hospital.mapper;

import com.neusoft.hospital.entity.Treatment;
import com.neusoft.hospital.entity.vo.TreatmentVO;
import org.apache.ibatis.annotations.*;
import java.util.List;

/**
 * 处置数据访问层 —— 对应 treatment 表
 * <p>提供处置记录的增删改查操作，包括更新处置状态、分页查询处置视图等。</p>
 */
@Mapper
public interface TreatmentMapper {

    /**
     * 新增处置记录
     * <p>SQL：插入处置信息（关联挂号/患者/处置名称/描述/费用），初始状态为 0（未完成）</p>
     *
     * @param treatment 处置实体
     * @return 影响的行数
     */
    @Insert("INSERT INTO treatment(registration_id, patient_id, treatment_name, treatment_desc, fee, status) " +
            "VALUES(#{registrationId}, #{patientId}, #{treatmentName}, #{treatmentDesc}, #{fee}, 0)")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(Treatment treatment);

    /**
     * 更新处置信息
     * <p>SQL：按主键更新处置的名称、描述和费用</p>
     *
     * @param treatment 处置实体（需包含 id）
     * @return 影响的行数
     */
    @Update("UPDATE treatment SET treatment_name=#{treatmentName}, treatment_desc=#{treatmentDesc}, fee=#{fee} WHERE id=#{id}")
    int update(Treatment treatment);

    /**
     * 更新处置状态
     * <p>SQL：按主键更新处置完成状态（0=未完成，1=已完成）</p>
     *
     * @param id     处置ID
     * @param status 状态值
     * @return 影响的行数
     */
    @Update("UPDATE treatment SET status=#{status} WHERE id=#{id}")
    int updateStatus(@Param("id") Long id, @Param("status") Integer status);

    /**
     * 根据ID查询处置
     * <p>SQL：按主键 id 查询处置记录</p>
     *
     * @param id 处置ID
     * @return 处置实体，未找到返回 null
     */
    @Select("SELECT * FROM treatment WHERE id=#{id}")
    Treatment selectById(Long id);

    /**
     * 分页查询处置 VO 列表（含患者/医生/科室信息）
     * <p>XML 动态 SQL：按挂号ID、患者ID、医生ID、状态等多条件分页查询处置视图</p>
     *
     * @param registrationId 挂号ID（可选）
     * @param patientId      患者ID（可选）
     * @param doctorId       医生ID（可选）
     * @param status         状态（可选，0=未完成，1=已完成）
     * @param offset         分页偏移量
     * @param pageSize       每页条数
     * @return 处置 VO 分页列表
     */
    List<TreatmentVO> selectTreatmentVO(@Param("registrationId") Long registrationId,
                                         @Param("patientId") Long patientId,
                                         @Param("doctorId") Long doctorId,
                                         @Param("status") Integer status,
                                         @Param("offset") int offset,
                                         @Param("pageSize") int pageSize);

    /**
     * 查询处置 VO 总数（用于分页）
     * <p>XML 动态 SQL：按挂号ID、患者ID、医生ID、状态统计记录数</p>
     *
     * @param registrationId 挂号ID（可选）
     * @param patientId      患者ID（可选）
     * @param doctorId       医生ID（可选）
     * @param status         状态（可选）
     * @return 符合条件的处置总数
     */
    Long selectTreatmentVOCount(@Param("registrationId") Long registrationId,
                                 @Param("patientId") Long patientId,
                                 @Param("doctorId") Long doctorId,
                                 @Param("status") Integer status);
}
