package com.neusoft.hospital.mapper;

import com.neusoft.hospital.entity.Doctor;
import com.neusoft.hospital.entity.vo.DoctorVO;
import org.apache.ibatis.annotations.*;
import java.util.List;

@Mapper
public interface DoctorMapper {

    @Select("SELECT * FROM doctor WHERE status=1")
    List<Doctor> selectAll();

    List<DoctorVO> selectAllVO();

    List<DoctorVO> selectVOPage(@Param("doctorName") String doctorName,
                                 @Param("deptId") Long deptId,
                                 @Param("offset") int offset,
                                 @Param("pageSize") int pageSize);

    Long selectVOCount(@Param("doctorName") String doctorName,
                       @Param("deptId") Long deptId);

    @Select("SELECT * FROM doctor WHERE id=#{id}")
    Doctor selectById(Long id);

    @Select("SELECT * FROM doctor WHERE dept_id=#{deptId} AND status=1")
    List<Doctor> selectByDeptId(@Param("deptId") Long deptId);

    List<DoctorVO> selectByDeptIdVO(@Param("deptId") Long deptId);

    @Insert("INSERT INTO doctor(doctor_name, dept_id, title, specialty, status) VALUES(#{doctorName}, #{deptId}, #{title}, #{specialty}, #{status})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(Doctor doctor);

    @Update("UPDATE doctor SET doctor_name=#{doctorName}, dept_id=#{deptId}, title=#{title}, specialty=#{specialty}, status=#{status} WHERE id=#{id}")
    int update(Doctor doctor);

    @Delete("DELETE FROM doctor WHERE id=#{id}")
    int deleteById(Long id);

    @Select("SELECT COUNT(*) FROM registration WHERE doctor_id=#{doctorId}")
    long countRegistrationsByDoctorId(@Param("doctorId") Long doctorId);
}
