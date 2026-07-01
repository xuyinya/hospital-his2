package com.neusoft.hospital.controller;

import com.neusoft.hospital.common.Result;
import com.neusoft.hospital.entity.Registration;
import com.neusoft.hospital.mapper.RegistrationMapper;
import com.neusoft.hospital.service.PaymentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.*;

/**
 * 首页仪表盘控制器
 * <p>
 * 提供系统首页的统计数据和概览信息，包括挂号总数、就诊数量、待诊数量及总收入等。
 * 统计全部数据（不限定日期），适用于演示环境。
 * 接口路径：/api/dashboard
 * </p>
 */
@Tag(name = "首页仪表盘")
@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final RegistrationMapper registrationMapper;
    private final PaymentService paymentService;

    /**
     * 获取首页统计数据
     * <p>
     * 统计全部数据（不限定日期），包括：
     * 挂号总数、已就诊数量、待诊数量、已支付总金额。
     * </p>
     *
     * @return 包含挂号数、就诊数、待诊数和总收入的统计数据Map
     */
    @Operation(summary = "获取首页统计数据")
    @GetMapping("/stats")
    public Result<Map<String, Object>> stats() {
        // 查询全部挂号记录
        List<Registration> allRegs = registrationMapper.selectList(null, null, null, null, null, 0, 10000);

        // 统计全部数据（不限定日期，适用于演示环境）
        long regCount = allRegs.size();
        long doneCount = allRegs.stream()
                .filter(r -> r.getStatus() != null && r.getStatus() == 1)
                .count();
        long regWaitCount = allRegs.stream()
                .filter(r -> r.getStatus() != null && r.getStatus() == 0)
                .count();

        // 从收费表中计算已支付总金额
        var allPayments = paymentService.list(null, 1, 1, 10000);
        BigDecimal totalIncome = allPayments.getRows().stream()
                .map(p -> p.getTotalAmount() != null ? p.getTotalAmount() : BigDecimal.ZERO)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        Map<String, Object> stats = new HashMap<>();
        stats.put("registrationCount", regCount);
        stats.put("patientCount", doneCount);
        stats.put("registrationWaitCount", regWaitCount);
        stats.put("todayIncome", totalIncome.toString());

        return Result.success(stats);
    }
}
