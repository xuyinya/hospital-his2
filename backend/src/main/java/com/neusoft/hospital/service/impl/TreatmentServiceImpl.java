package com.neusoft.hospital.service.impl;

import com.neusoft.hospital.common.PageResult;
import com.neusoft.hospital.entity.Treatment;
import com.neusoft.hospital.entity.vo.TreatmentVO;
import com.neusoft.hospital.mapper.TreatmentMapper;
import com.neusoft.hospital.service.TreatmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TreatmentServiceImpl implements TreatmentService {

    private final TreatmentMapper treatmentMapper;

    @Override
    public void add(Treatment treatment) {
        treatmentMapper.insert(treatment);
    }

    @Override
    public void update(Treatment treatment) {
        treatmentMapper.update(treatment);
    }

    @Override
    public void updateStatus(Long id, Integer status) {
        treatmentMapper.updateStatus(id, status);
    }

    @Override
    public Treatment getById(Long id) {
        return treatmentMapper.selectById(id);
    }

    @Override
    public PageResult<TreatmentVO> list(Long registrationId, Long patientId, Long doctorId, Integer status, Integer page, Integer size) {
        int offset = (page - 1) * size;
        List<TreatmentVO> rows = treatmentMapper.selectTreatmentVO(registrationId, patientId, doctorId, status, offset, size);
        Long total = treatmentMapper.selectTreatmentVOCount(registrationId, patientId, doctorId, status);
        return new PageResult<>(total, rows);
    }
}
