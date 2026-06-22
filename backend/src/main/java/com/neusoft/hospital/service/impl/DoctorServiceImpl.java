package com.neusoft.hospital.service.impl;

import com.neusoft.hospital.common.PageResult;
import com.neusoft.hospital.entity.Doctor;
import com.neusoft.hospital.entity.vo.DoctorVO;
import com.neusoft.hospital.mapper.DoctorMapper;
import com.neusoft.hospital.service.DoctorService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DoctorServiceImpl implements DoctorService {

    private final DoctorMapper doctorMapper;

    @Override
    public List<Doctor> list() {
        return doctorMapper.selectAll();
    }

    @Override
    public PageResult<DoctorVO> listVO(String doctorName, Long deptId, Integer page, Integer size) {
        int offset = (page - 1) * size;
        List<DoctorVO> rows = doctorMapper.selectVOPage(doctorName, deptId, offset, size);
        Long total = doctorMapper.selectVOCount(doctorName, deptId);
        return new PageResult<>(total, rows);
    }

    @Override
    public Doctor getById(Long id) {
        return doctorMapper.selectById(id);
    }

    @Override
    public List<Doctor> getByDeptId(Long deptId) {
        return doctorMapper.selectByDeptId(deptId);
    }

    @Override
    public List<DoctorVO> getByDeptIdVO(Long deptId) {
        return doctorMapper.selectByDeptIdVO(deptId);
    }

    @Override
    public void add(Doctor doctor) {
        if (doctor.getStatus() == null) doctor.setStatus(1);
        doctorMapper.insert(doctor);
    }

    @Override
    public void update(Doctor doctor) {
        doctorMapper.update(doctor);
    }

    @Override
    public void delete(Long id) {
        long count = doctorMapper.countRegistrationsByDoctorId(id);
        if (count > 0) {
            throw new RuntimeException("该医生有 " + count + " 条挂号记录，不能删除");
        }
        doctorMapper.deleteById(id);
    }
}
