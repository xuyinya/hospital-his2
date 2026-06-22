package com.neusoft.hospital.service.impl;

import com.neusoft.hospital.common.PageResult;
import com.neusoft.hospital.entity.Examination;
import com.neusoft.hospital.entity.vo.ExaminationVO;
import com.neusoft.hospital.mapper.ExaminationMapper;
import com.neusoft.hospital.service.ExaminationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ExaminationServiceImpl implements ExaminationService {

    private final ExaminationMapper examinationMapper;

    @Override
    public void add(Examination examination) {
        examinationMapper.insert(examination);
    }

    @Override
    public void update(Examination examination) {
        examinationMapper.update(examination);
    }

    @Override
    public void updateResult(Long id, String result) {
        examinationMapper.updateResult(id, result);
    }

    @Override
    public Examination getById(Long id) {
        return examinationMapper.selectById(id);
    }

    @Override
    public PageResult<ExaminationVO> list(Long registrationId, Long patientId, Long doctorId, Integer status, Integer page, Integer size) {
        int offset = (page - 1) * size;
        List<ExaminationVO> rows = examinationMapper.selectExaminationVO(registrationId, patientId, doctorId, status, offset, size);
        Long total = examinationMapper.selectExaminationVOCount(registrationId, patientId, doctorId, status);
        return new PageResult<>(total, rows);
    }
}
