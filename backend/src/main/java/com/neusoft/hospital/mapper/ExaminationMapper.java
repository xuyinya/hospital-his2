package com.neusoft.hospital.mapper;

import com.neusoft.hospital.entity.Examination;
import com.neusoft.hospital.entity.vo.ExaminationVO;
import org.apache.ibatis.annotations.*;
import java.util.List;

@Mapper
public interface ExaminationMapper {

    @Insert("INSERT INTO examination(registration_id, patient_id, exam_type, exam_name, fee, status) " +
            "VALUES(#{registrationId}, #{patientId}, #{examType}, #{examName}, #{fee}, 0)")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(Examination examination);

    @Update("UPDATE examination SET exam_type=#{examType}, exam_name=#{examName}, fee=#{fee} WHERE id=#{id}")
    int update(Examination examination);

    @Update("UPDATE examination SET result=#{result}, status=1, exam_time=NOW() WHERE id=#{id}")
    int updateResult(@Param("id") Long id, @Param("result") String result);

    @Select("SELECT * FROM examination WHERE id=#{id}")
    Examination selectById(Long id);

    List<ExaminationVO> selectExaminationVO(@Param("registrationId") Long registrationId,
                                             @Param("patientId") Long patientId,
                                             @Param("doctorId") Long doctorId,
                                             @Param("status") Integer status,
                                             @Param("offset") int offset,
                                             @Param("pageSize") int pageSize);

    Long selectExaminationVOCount(@Param("registrationId") Long registrationId,
                                   @Param("patientId") Long patientId,
                                   @Param("doctorId") Long doctorId,
                                   @Param("status") Integer status);
}
