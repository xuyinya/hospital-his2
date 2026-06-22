package com.neusoft.hospital.service;

import com.neusoft.hospital.common.PageResult;
import com.neusoft.hospital.entity.Treatment;
import com.neusoft.hospital.entity.vo.TreatmentVO;

public interface TreatmentService {
    void add(Treatment treatment);
    void update(Treatment treatment);
    void updateStatus(Long id, Integer status);
    Treatment getById(Long id);
    PageResult<TreatmentVO> list(Long registrationId, Long patientId, Long doctorId, Integer status, Integer page, Integer size);
}
