package com.neusoft.hospital.service;

import com.neusoft.hospital.common.PageResult;
import com.neusoft.hospital.entity.Patient;

public interface PatientService {
    void add(Patient patient);
    void update(Patient patient);
    void delete(Long id);
    Patient getById(Long id);
    Patient getByIdCard(String idCard);
    PageResult<Patient> list(String patientName, Long doctorId, Integer page, Integer size);
}
