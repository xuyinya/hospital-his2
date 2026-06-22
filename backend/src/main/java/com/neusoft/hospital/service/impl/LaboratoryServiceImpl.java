package com.neusoft.hospital.service.impl;

import com.neusoft.hospital.common.PageResult;
import com.neusoft.hospital.entity.Laboratory;
import com.neusoft.hospital.entity.vo.LaboratoryVO;
import com.neusoft.hospital.mapper.LaboratoryMapper;
import com.neusoft.hospital.service.LaboratoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LaboratoryServiceImpl implements LaboratoryService {

    private final LaboratoryMapper laboratoryMapper;

    @Override
    public void add(Laboratory laboratory) {
        laboratoryMapper.insert(laboratory);
    }

    @Override
    public void update(Laboratory laboratory) {
        laboratoryMapper.update(laboratory);
    }

    @Override
    public void updateResult(Long id, String result, String referenceRange) {
        laboratoryMapper.updateResult(id, result, referenceRange);
    }

    @Override
    public Laboratory getById(Long id) {
        return laboratoryMapper.selectById(id);
    }

    @Override
    public PageResult<LaboratoryVO> list(Long registrationId, Long patientId, Long doctorId, Integer status, Integer page, Integer size) {
        int offset = (page - 1) * size;
        List<LaboratoryVO> rows = laboratoryMapper.selectLaboratoryVO(registrationId, patientId, doctorId, status, offset, size);
        Long total = laboratoryMapper.selectLaboratoryVOCount(registrationId, patientId, doctorId, status);
        return new PageResult<>(total, rows);
    }
}
