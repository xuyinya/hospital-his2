package com.neusoft.hospital.service.impl;

import com.neusoft.hospital.common.PageResult;
import com.neusoft.hospital.entity.Registration;
import com.neusoft.hospital.entity.vo.RegistrationVO;
import com.neusoft.hospital.mapper.RegistrationMapper;
import com.neusoft.hospital.service.RegistrationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RegistrationServiceImpl implements RegistrationService {

    private final RegistrationMapper registrationMapper;

    @Override
    public void add(Registration registration) {
        registrationMapper.insert(registration);
    }

    @Override
    public void update(Registration registration) {
        registrationMapper.update(registration);
    }

    @Override
    public void updateStatus(Long id, Integer status) {
        registrationMapper.updateStatus(id, status);
    }

    @Override
    public Registration getById(Long id) {
        return registrationMapper.selectById(id);
    }

    @Override
    public PageResult<RegistrationVO> list(Long patientId, Long doctorId, Integer status, String patientName, Long deptId, Integer page, Integer size) {
        int offset = (page - 1) * size;
        List<RegistrationVO> rows = registrationMapper.selectRegistrationVO(patientId, doctorId, status, patientName, deptId, offset, size);
        Long total = registrationMapper.selectRegistrationVOCount(patientId, doctorId, status, patientName, deptId);
        return new PageResult<>(total, rows);
    }
}
