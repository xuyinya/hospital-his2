package com.neusoft.hospital.mapper;

import com.neusoft.hospital.entity.MedicalRecord;
import org.apache.ibatis.annotations.*;
import java.util.List;

/**
 * 病历数据访问层 —— 对应 medical_record 表
 * <p>提供病历记录的增删改查操作，包括按挂号ID查询病历、分页查询病历视图等。</p>
 */
@Mapper
public interface MedicalRecordMapper {

    /**
     * 新增病历记录
     * <p>SQL：插入病历信息（主诉、现病史、诊断、治疗方案等），记录时间为当前时间</p>
     *
     * @param medicalRecord 病历实体
     * @return 影响的行数
     */
    @Insert("INSERT INTO medical_record(registration_id, patient_id, doctor_id, chief_complaint, " +
            "present_illness, diagnosis, treatment_plan, record_time) " +
            "VALUES(#{registrationId}, #{patientId}, #{doctorId}, #{chiefComplaint}, " +
            "#{presentIllness}, #{diagnosis}, #{treatmentPlan}, NOW())")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(MedicalRecord medicalRecord);

    /**
     * 更新病历信息
     * <p>SQL：按主键更新病历的主诉、现病史、诊断和治疗方案</p>
     *
     * @param medicalRecord 病历实体（需包含 id）
     * @return 影响的行数
     */
    @Update("UPDATE medical_record SET chief_complaint=#{chiefComplaint}, present_illness=#{presentIllness}, " +
            "diagnosis=#{diagnosis}, treatment_plan=#{treatmentPlan} WHERE id=#{id}")
    int update(MedicalRecord medicalRecord);

    /**
     * 根据ID查询病历
     * <p>SQL：按主键 id 查询病历记录</p>
     *
     * @param id 病历ID
     * @return 病历实体，未找到返回 null
     */
    @Select("SELECT * FROM medical_record WHERE id=#{id}")
    MedicalRecord selectById(Long id);

    /**
     * 根据挂号ID查询病历
     * <p>SQL：按 registration_id 查询该挂号对应的病历（一对一提对应）</p>
     *
     * @param registrationId 挂号ID
     * @return 病历实体，未找到返回 null
     */
    @Select("SELECT * FROM medical_record WHERE registration_id=#{registrationId}")
    MedicalRecord selectByRegistrationId(@Param("registrationId") Long registrationId);

    /**
     * 分页查询病历列表（含患者/医生信息）
     * <p>XML 动态 SQL：按挂号ID、患者ID、医生ID、患者姓名等多条件分页查询病历视图</p>
     *
     * @param registrationId 挂号ID（可选）
     * @param patientId      患者ID（可选）
     * @param doctorId       医生ID（可选）
     * @param patientName    患者姓名（可选，模糊匹配）
     * @param offset         分页偏移量
     * @param pageSize       每页条数
     * @return 病历分页列表（含关联字段）
     */
    List<MedicalRecord> selectList(@Param("registrationId") Long registrationId,
                                                 @Param("patientId") Long patientId,
                                                 @Param("doctorId") Long doctorId,
                                                 @Param("patientName") String patientName,
                                                 @Param("offset") int offset,
                                                 @Param("pageSize") int pageSize);

    /**
     * 查询病历总数（用于分页）
     * <p>XML 动态 SQL：按挂号ID、患者ID、医生ID、患者姓名统计记录数</p>
     *
     * @param registrationId 挂号ID（可选）
     * @param patientId      患者ID（可选）
     * @param doctorId       医生ID（可选）
     * @param patientName    患者姓名（可选）
     * @return 符合条件的病历总数
     */
    Long selectCount(@Param("registrationId") Long registrationId,
                                     @Param("patientId") Long patientId,
                                     @Param("doctorId") Long doctorId,
                                     @Param("patientName") String patientName);
}
