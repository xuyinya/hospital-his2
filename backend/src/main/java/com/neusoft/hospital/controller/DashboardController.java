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
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

@Tag(name = "首页仪表盘")
@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
/**
 * 首页仪表盘控制器
 * <p>
 * 提供系统首页的统计数据和概览信息，包括当日挂号数量、就诊数量、待诊数量及当日收入等。
 * 接口路径：/api/dashboard
 * </p>
 */
public class DashboardController {

    private final RegistrationMapper registrationMapper;
    private final PaymentService paymentService;

    /**
     * 获取首页统计数据
     * <p>
     * 按当日（亚洲/上海时区）统计系统运营数据，包括：
     * 当日挂号总数、当日已就诊人数、当日待诊人数、当日已支付总金额。
     * </p>
     *
     * @return 包含注册数、就诊数、待诊数和当日收入的统计数据Map
     */
    @Operation(summary = "获取首页统计数据（按当日过滤）")
    @GetMapping("/stats")
    public Result<Map<String, Object>> stats() {
        // 获取当天日期（按亚洲/上海时区）
        LocalDate today = LocalDate.now(ZoneId.of("Asia/Shanghai"));

        // 查询全部挂号记录
        List<RegistrationVO> allRegs = registrationMapper.selectRegistrationVO(null, null, null, null, null, 0, 10000);

        // 在Java中按日期过滤
        long regCount = allRegs.stream()
                .filter(r -> r.getRegTime() != null && r.getRegTime().toLocalDate().equals(today))
                .count();
        long doneCount = allRegs.stream()
                .filter(r -> r.getRegTime() != null && r.getStatus() != null && r.getStatus() == 1
                        && r.getRegTime().toLocalDate().equals(today))
                .count();
        long regWaitCount = allRegs.stream()
                .filter(r -> r.getRegTime() != null && r.getStatus() != null && r.getStatus() == 0
                        && r.getRegTime().toLocalDate().equals(today))
                .count();

        // 从收费表中计算当日已支付总金额（同样按日期过滤）
        var allPayments = paymentService.list(null, 1, 1, 10000);
        BigDecimal todayIncome = allPayments.getRows().stream()
                .filter(p -> p.getPaymentTime() != null && p.getPaymentTime().toLocalDate().equals(today))
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
