package com.neusoft.hospital.mapper;

import com.neusoft.hospital.entity.Registration;
import com.neusoft.hospital.entity.vo.RegistrationVO;
import org.apache.ibatis.annotations.*;
import java.util.List;

@Mapper
public interface RegistrationMapper {

    @Insert("INSERT INTO registration(patient_id, doctor_id, dept_id, reg_type, reg_fee, reg_time, status) " +
            "VALUES(#{patientId}, #{doctorId}, #{deptId}, #{regType}, #{regFee}, NOW(), 0)")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(Registration registration);

    @Update("UPDATE registration SET patient_id=#{patientId}, doctor_id=#{doctorId}, dept_id=#{deptId}, " +
            "reg_type=#{regType}, reg_fee=#{regFee} WHERE id=#{id}")
    int update(Registration registration);

    @Update("UPDATE registration SET status=#{status} WHERE id=#{id}")
    int updateStatus(@Param("id") Long id, @Param("status") Integer status);

    @Select("SELECT * FROM registration WHERE id=#{id}")
    Registration selectById(Long id);

    List<RegistrationVO> selectRegistrationVO(@Param("patientId") Long patientId,
                                               @Param("doctorId") Long doctorId,
                                               @Param("status") Integer status,
                                               @Param("patientName") String patientName,
                                               @Param("deptId") Long deptId,
                                               @Param("offset") int offset,
                                               @Param("pageSize") int pageSize);

    Long selectRegistrationVOCount(@Param("patientId") Long patientId,
                                    @Param("doctorId") Long doctorId,
                                    @Param("status") Integer status,
                                    @Param("patientName") String patientName,
                                    @Param("deptId") Long deptId);
}
