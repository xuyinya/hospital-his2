package com.neusoft.hospital.mapper;

import com.neusoft.hospital.entity.MedicalRecord;
import com.neusoft.hospital.entity.vo.MedicalRecordVO;
import org.apache.ibatis.annotations.*;
import java.util.List;

@Mapper
public interface MedicalRecordMapper {

    @Insert("INSERT INTO medical_record(registration_id, patient_id, doctor_id, chief_complaint, " +
            "present_illness, diagnosis, treatment_plan, record_time) " +
            "VALUES(#{registrationId}, #{patientId}, #{doctorId}, #{chiefComplaint}, " +
            "#{presentIllness}, #{diagnosis}, #{treatmentPlan}, NOW())")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(MedicalRecord medicalRecord);

    @Update("UPDATE medical_record SET chief_complaint=#{chiefComplaint}, present_illness=#{presentIllness}, " +
            "diagnosis=#{diagnosis}, treatment_plan=#{treatmentPlan} WHERE id=#{id}")
    int update(MedicalRecord medicalRecord);

    @Select("SELECT * FROM medical_record WHERE id=#{id}")
    MedicalRecord selectById(Long id);

    @Select("SELECT * FROM medical_record WHERE registration_id=#{registrationId}")
    MedicalRecord selectByRegistrationId(@Param("registrationId") Long registrationId);

    List<MedicalRecordVO> selectMedicalRecordVO(@Param("registrationId") Long registrationId,
                                                 @Param("patientId") Long patientId,
                                                 @Param("doctorId") Long doctorId,
                                                 @Param("patientName") String patientName,
                                                 @Param("offset") int offset,
                                                 @Param("pageSize") int pageSize);

    Long selectMedicalRecordVOCount(@Param("registrationId") Long registrationId,
                                     @Param("patientId") Long patientId,
                                     @Param("doctorId") Long doctorId,
                                     @Param("patientName") String patientName);
}
