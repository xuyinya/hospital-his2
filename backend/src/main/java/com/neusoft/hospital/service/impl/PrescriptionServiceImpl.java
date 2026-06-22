package com.neusoft.hospital.service.impl;

import com.neusoft.hospital.common.PageResult;
import com.neusoft.hospital.entity.Prescription;
import com.neusoft.hospital.entity.PrescriptionDetail;
import com.neusoft.hospital.entity.vo.PrescriptionDetailVO;
import com.neusoft.hospital.entity.vo.PrescriptionVO;
import com.neusoft.hospital.mapper.PrescriptionDetailMapper;
import com.neusoft.hospital.mapper.PrescriptionMapper;
import com.neusoft.hospital.service.PrescriptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PrescriptionServiceImpl implements PrescriptionService {

    private final PrescriptionMapper prescriptionMapper;
    private final PrescriptionDetailMapper prescriptionDetailMapper;

    @Override
    public void add(Prescription prescription) {
        prescriptionMapper.insert(prescription);
    }

    @Override
    @Transactional
    public void addDetail(PrescriptionDetail detail) {
        // 计算金额
        BigDecimal amount = detail.getUnitPrice().multiply(BigDecimal.valueOf(detail.getQuantity()));
        detail.setAmount(amount);
        prescriptionDetailMapper.insert(detail);
        // 更新处方总金额
        BigDecimal total = prescriptionDetailMapper.sumAmountByPrescriptionId(detail.getPrescriptionId());
        prescriptionMapper.updateTotalAmount(detail.getPrescriptionId(), total);
    }

    @Override
    public void updateStatus(Long id, Integer status) {
        prescriptionMapper.updateStatus(id, status);
    }

    @Override
    public Prescription getById(Long id) {
        return prescriptionMapper.selectById(id);
    }

    @Override
    public PageResult<PrescriptionVO> list(Long registrationId, Long patientId, Long doctorId, Integer status, Integer page, Integer size) {
        int offset = (page - 1) * size;
        List<PrescriptionVO> rows = prescriptionMapper.selectPrescriptionVO(registrationId, patientId, doctorId, status, offset, size);
        Long total = prescriptionMapper.selectPrescriptionVOCount(registrationId, patientId, doctorId, status);
        return new PageResult<>(total, rows);
    }

    @Override
    public List<PrescriptionDetailVO> getDetails(Long prescriptionId) {
        return prescriptionDetailMapper.selectDetailVO(prescriptionId);
    }
}
