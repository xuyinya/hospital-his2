package com.neusoft.hospital.service;

import com.neusoft.hospital.common.PageResult;
import com.neusoft.hospital.entity.Payment;
import com.neusoft.hospital.entity.vo.PaymentVO;

public interface PaymentService {
    void add(Payment payment);
    void updateStatus(Long id, Integer status);
    Payment getById(Long id);
    PageResult<PaymentVO> list(String patientName, Integer status, Integer page, Integer size);
}
