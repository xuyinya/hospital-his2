package com.neusoft.hospital.service;

import com.neusoft.hospital.common.PageResult;
import com.neusoft.hospital.entity.Examination;
import com.neusoft.hospital.entity.vo.ExaminationVO;

public interface ExaminationService {
    void add(Examination examination);
    void update(Examination examination);
    void updateResult(Long id, String result);
    Examination getById(Long id);
    PageResult<ExaminationVO> list(Long registrationId, Long patientId, Long doctorId, Integer status, Integer page, Integer size);
}
