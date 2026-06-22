package com.neusoft.hospital.service.impl;

import com.neusoft.hospital.common.PageResult;
import com.neusoft.hospital.entity.Payment;
import com.neusoft.hospital.entity.vo.PaymentVO;
import com.neusoft.hospital.mapper.PaymentMapper;
import com.neusoft.hospital.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentMapper paymentMapper;

    @Override
    public void add(Payment payment) {
        paymentMapper.insert(payment);
    }

    @Override
    public void updateStatus(Long id, Integer status) {
        paymentMapper.updateStatus(id, status);
    }

    @Override
    public Payment getById(Long id) {
        return paymentMapper.selectById(id);
    }

    @Override
    public PageResult<PaymentVO> list(String patientName, Integer status, Integer page, Integer size) {
        int offset = (page - 1) * size;
        List<PaymentVO> rows = paymentMapper.selectPaymentVO(patientName, status, offset, size);
        Long total = paymentMapper.selectPaymentVOCount(patientName, status);
        return new PageResult<>(total, rows);
    }
}
