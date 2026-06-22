package com.neusoft.hospital.service;

import com.neusoft.hospital.common.PageResult;
import com.neusoft.hospital.entity.Registration;
import com.neusoft.hospital.entity.vo.RegistrationVO;

public interface RegistrationService {
    void add(Registration registration);
    void update(Registration registration);
    void updateStatus(Long id, Integer status);
    Registration getById(Long id);
    PageResult<RegistrationVO> list(Long patientId, Long doctorId, Integer status, String patientName, Long deptId, Integer page, Integer size);
}
