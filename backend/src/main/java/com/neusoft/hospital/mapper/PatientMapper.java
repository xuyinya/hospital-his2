package com.neusoft.hospital.mapper;

import com.neusoft.hospital.entity.Patient;
import org.apache.ibatis.annotations.*;
import java.util.List;

@Mapper
public interface PatientMapper {

    @Insert("INSERT INTO patient(patient_name, gender, age, id_card, phone, address, password, create_time, update_time) " +
            "VALUES(#{patientName}, #{gender}, #{age}, #{idCard}, #{phone}, #{address}, #{password}, NOW(), NOW())")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(Patient patient);

    @Update("UPDATE patient SET patient_name=#{patientName}, gender=#{gender}, age=#{age}, " +
            "id_card=#{idCard}, phone=#{phone}, address=#{address}, update_time=NOW() WHERE id=#{id}")
    int update(Patient patient);

    @Delete("DELETE FROM patient WHERE id=#{id}")
    int deleteById(Long id);

    @Select("SELECT * FROM patient WHERE id=#{id}")
    Patient selectById(Long id);

    @Select("SELECT * FROM patient WHERE id_card = #{idCard}")
    Patient selectByIdCard(@Param("idCard") String idCard);

    @Update("UPDATE patient SET password=#{password} WHERE id=#{id}")
    int updatePassword(@Param("id") Long id, @Param("password") String password);

    List<Patient> selectList(@Param("patientName") String patientName,
                             @Param("doctorId") Long doctorId,
                             @Param("offset") int offset,
                             @Param("pageSize") int pageSize);

    Long selectCount(@Param("patientName") String patientName,
                     @Param("doctorId") Long doctorId);
}
