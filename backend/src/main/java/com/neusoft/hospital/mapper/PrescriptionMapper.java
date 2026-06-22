package com.neusoft.hospital.mapper;

import com.neusoft.hospital.entity.Prescription;
import com.neusoft.hospital.entity.vo.PrescriptionVO;
import org.apache.ibatis.annotations.*;
import java.math.BigDecimal;
import java.util.List;

@Mapper
public interface PrescriptionMapper {

    @Insert("INSERT INTO prescription(registration_id, patient_id, doctor_id, total_amount, status, create_time) " +
            "VALUES(#{registrationId}, #{patientId}, #{doctorId}, 0, 0, NOW())")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(Prescription prescription);

    @Update("UPDATE prescription SET total_amount=#{totalAmount} WHERE id=#{id}")
    int updateTotalAmount(@Param("id") Long id, @Param("totalAmount") BigDecimal totalAmount);

    @Update("UPDATE prescription SET status=#{status} WHERE id=#{id}")
    int updateStatus(@Param("id") Long id, @Param("status") Integer status);

    @Select("SELECT * FROM prescription WHERE id=#{id}")
    Prescription selectById(Long id);

    List<PrescriptionVO> selectPrescriptionVO(@Param("registrationId") Long registrationId,
                                               @Param("patientId") Long patientId,
                                               @Param("doctorId") Long doctorId,
                                               @Param("status") Integer status,
                                               @Param("offset") int offset,
                                               @Param("pageSize") int pageSize);

    Long selectPrescriptionVOCount(@Param("registrationId") Long registrationId,
                                    @Param("patientId") Long patientId,
                                    @Param("doctorId") Long doctorId,
                                    @Param("status") Integer status);
}
