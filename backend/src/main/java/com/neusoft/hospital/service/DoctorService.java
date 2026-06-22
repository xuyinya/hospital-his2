package com.neusoft.hospital.service;

import com.neusoft.hospital.common.PageResult;
import com.neusoft.hospital.entity.Doctor;
import com.neusoft.hospital.entity.vo.DoctorVO;
import java.util.List;

public interface DoctorService {
    List<Doctor> list();
    PageResult<DoctorVO> listVO(String doctorName, Long deptId, Integer page, Integer size);
    Doctor getById(Long id);
    List<Doctor> getByDeptId(Long deptId);
    List<DoctorVO> getByDeptIdVO(Long deptId);
    void add(Doctor doctor);
    void update(Doctor doctor);
    void delete(Long id);
}
