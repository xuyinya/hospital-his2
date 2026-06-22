package com.neusoft.hospital.mapper;

import com.neusoft.hospital.entity.Treatment;
import com.neusoft.hospital.entity.vo.TreatmentVO;
import org.apache.ibatis.annotations.*;
import java.util.List;

@Mapper
public interface TreatmentMapper {

    @Insert("INSERT INTO treatment(registration_id, patient_id, treatment_name, treatment_desc, fee, status) " +
            "VALUES(#{registrationId}, #{patientId}, #{treatmentName}, #{treatmentDesc}, #{fee}, 0)")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(Treatment treatment);

    @Update("UPDATE treatment SET treatment_name=#{treatmentName}, treatment_desc=#{treatmentDesc}, fee=#{fee} WHERE id=#{id}")
    int update(Treatment treatment);

    @Update("UPDATE treatment SET status=#{status} WHERE id=#{id}")
    int updateStatus(@Param("id") Long id, @Param("status") Integer status);

    @Select("SELECT * FROM treatment WHERE id=#{id}")
    Treatment selectById(Long id);

    List<TreatmentVO> selectTreatmentVO(@Param("registrationId") Long registrationId,
                                         @Param("patientId") Long patientId,
                                         @Param("doctorId") Long doctorId,
                                         @Param("status") Integer status,
                                         @Param("offset") int offset,
                                         @Param("pageSize") int pageSize);

    Long selectTreatmentVOCount(@Param("registrationId") Long registrationId,
                                 @Param("patientId") Long patientId,
                                 @Param("doctorId") Long doctorId,
                                 @Param("status") Integer status);
}
