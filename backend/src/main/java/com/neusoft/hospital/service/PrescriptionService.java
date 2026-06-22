package com.neusoft.hospital.service;

import com.neusoft.hospital.common.PageResult;
import com.neusoft.hospital.entity.Prescription;
import com.neusoft.hospital.entity.PrescriptionDetail;
import com.neusoft.hospital.entity.vo.PrescriptionDetailVO;
import com.neusoft.hospital.entity.vo.PrescriptionVO;
import java.util.List;

public interface PrescriptionService {
    void add(Prescription prescription);
    void addDetail(PrescriptionDetail detail);
    void updateStatus(Long id, Integer status);
    Prescription getById(Long id);
    PageResult<PrescriptionVO> list(Long registrationId, Long patientId, Long doctorId, Integer status, Integer page, Integer size);
    List<PrescriptionDetailVO> getDetails(Long prescriptionId);
}
