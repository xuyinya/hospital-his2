package com.neusoft.hospital.controller;

import com.neusoft.hospital.common.Result;
import com.neusoft.hospital.entity.vo.RegistrationVO;
import com.neusoft.hospital.mapper.RegistrationMapper;
import com.neusoft.hospital.service.PaymentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.*;

@Tag(name = "首页仪表盘")
@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final RegistrationMapper registrationMapper;
    private final PaymentService paymentService;

    @Operation(summary = "获取首页统计数据")
    @GetMapping("/stats")
    public Result<Map<String, Object>> stats() {
        List<RegistrationVO> todayRegs = registrationMapper.selectRegistrationVO(null, null, null, null, null, 0, 1000);
        long regCount = todayRegs.stream().filter(r -> r.getRegTime() != null).count();
        long doneCount = todayRegs.stream().filter(r -> r.getStatus() != null && r.getStatus() == 1).count();
        long regWaitCount = todayRegs.stream().filter(r -> r.getStatus() != null && r.getStatus() == 0).count();

        // 从收费表中计算已支付总金额
        var paidPayments = paymentService.list(null, 1, 1, 10000);
        BigDecimal todayIncome = paidPayments.getRows().stream()
                .map(p -> p.getTotalAmount() != null ? p.getTotalAmount() : BigDecimal.ZERO)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        Map<String, Object> stats = new HashMap<>();
        stats.put("registrationCount", regCount);
        stats.put("patientCount", doneCount);
        stats.put("registrationWaitCount", regWaitCount);
        stats.put("todayIncome", todayIncome.toString());

        return Result.success(stats);
    }
}
