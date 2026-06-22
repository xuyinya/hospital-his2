package com.neusoft.hospital.controller;

import com.neusoft.hospital.common.PageResult;
import com.neusoft.hospital.common.Result;
import com.neusoft.hospital.entity.Payment;
import com.neusoft.hospital.entity.vo.PaymentVO;
import com.neusoft.hospital.service.PaymentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Tag(name = "收费管理")
@RestController
@RequestMapping("/api/payment")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @Operation(summary = "新增收费")
    @PostMapping
    public Result<Void> add(@RequestBody Payment payment) {
        paymentService.add(payment);
        return Result.success();
    }

    @Operation(summary = "查询收费详情")
    @GetMapping("/{id}")
    public Result<Payment> getById(@PathVariable Long id) {
        return Result.success(paymentService.getById(id));
    }

    @Operation(summary = "收费列表")
    @GetMapping("/list")
    public Result<PageResult<PaymentVO>> list(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) String patientName,
            @RequestParam(required = false) Integer status) {
        return Result.success(paymentService.list(patientName, status, page, size));
    }

    @Operation(summary = "更新支付状态")
    @PutMapping("/{id}/status")
    public Result<Void> updateStatus(@PathVariable Long id, @RequestParam Integer status) {
        paymentService.updateStatus(id, status);
        return Result.success();
    }
}
