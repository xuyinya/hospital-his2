package com.neusoft.hospital.service;

import com.neusoft.hospital.common.PageResult;
import com.neusoft.hospital.entity.Laboratory;
import com.neusoft.hospital.entity.vo.LaboratoryVO;

public interface LaboratoryService {
    void add(Laboratory laboratory);
    void update(Laboratory laboratory);
    void updateResult(Long id, String result, String referenceRange);
    Laboratory getById(Long id);
    PageResult<LaboratoryVO> list(Long registrationId, Long patientId, Long doctorId, Integer status, Integer page, Integer size);
}
