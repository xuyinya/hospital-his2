package com.neusoft.hospital.mapper;

import com.neusoft.hospital.entity.Laboratory;
import com.neusoft.hospital.entity.vo.LaboratoryVO;
import org.apache.ibatis.annotations.*;
import java.util.List;

@Mapper
public interface LaboratoryMapper {

    @Insert("INSERT INTO laboratory(registration_id, patient_id, lab_type, lab_name, fee, status) " +
            "VALUES(#{registrationId}, #{patientId}, #{labType}, #{labName}, #{fee}, 0)")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(Laboratory laboratory);

    @Update("UPDATE laboratory SET lab_type=#{labType}, lab_name=#{labName}, fee=#{fee} WHERE id=#{id}")
    int update(Laboratory laboratory);

    @Update("UPDATE laboratory SET result=#{result}, reference_range=#{referenceRange}, status=1, lab_time=NOW() WHERE id=#{id}")
    int updateResult(@Param("id") Long id, @Param("result") String result, @Param("referenceRange") String referenceRange);

    @Select("SELECT * FROM laboratory WHERE id=#{id}")
    Laboratory selectById(Long id);

    List<LaboratoryVO> selectLaboratoryVO(@Param("registrationId") Long registrationId,
                                           @Param("patientId") Long patientId,
                                           @Param("doctorId") Long doctorId,
                                           @Param("status") Integer status,
                                           @Param("offset") int offset,
                                           @Param("pageSize") int pageSize);

    Long selectLaboratoryVOCount(@Param("registrationId") Long registrationId,
                                  @Param("patientId") Long patientId,
                                  @Param("doctorId") Long doctorId,
                                  @Param("status") Integer status);
}
