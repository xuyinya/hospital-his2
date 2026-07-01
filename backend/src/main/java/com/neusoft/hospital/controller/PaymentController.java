package com.neusoft.hospital.controller;

import com.neusoft.hospital.common.PageResult;
import com.neusoft.hospital.common.Result;
import com.neusoft.hospital.entity.Payment;
import com.neusoft.hospital.service.PaymentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Tag(name = "收费管理")
@RestController
@RequestMapping("/api/payment")
@RequiredArgsConstructor
/**
 * 收费管理控制器
 * <p>
 * 提供收费记录的创建和查询功能，支持更新支付状态（未支付/已支付）。
 * 接口路径：/api/payment
 * </p>
 */
public class PaymentController {

    private final PaymentService paymentService;

    /**
     * 新增收费记录
     * <p>
     * 为患者的挂号记录创建一笔收费，包括费用项目、总金额等信息。
     * 收费记录创建后状态默认为未支付。
     * </p>
     *
     * @param payment 收费信息
     * @return 操作结果
     */
    @Operation(summary = "新增收费")
    @PostMapping
    public Result<Void> add(@RequestBody Payment payment) {
        paymentService.add(payment);
        return Result.success();
    }

    @Operation(summary = "删除收费")
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        paymentService.delete(id);
        return Result.success();
    }

    /**
     * 查询收费详情
     * <p>
     * 根据收费ID查询单笔收费记录的详细信息，包括费用项目、金额、支付状态等。
     * </p>
     *
     * @param id 收费ID
     * @return 指定收费记录的详细信息
     */
    @Operation(summary = "查询收费详情")
    @GetMapping("/{id}")
    public Result<Payment> getById(@PathVariable Long id) {
        return Result.success(paymentService.getById(id));
    }

    /**
     * 分页查询收费列表
     * <p>
     * 根据患者姓名和支付状态进行分页查询，返回收费视图对象列表。
     * 支持按患者姓名模糊搜索和按支付状态（0-未支付，1-已支付）筛选。
     * </p>
     *
     * @param page        页码，默认值为1
     * @param size        每页记录数，默认值为10
     * @param patientName 患者姓名（可选），用于模糊搜索
     * @param status      支付状态（可选），0-未支付，1-已支付
     * @return 分页结果，包含收费视图对象列表和总记录数
     */
    @Operation(summary = "收费列表")
    @GetMapping("/list")
    public Result<PageResult<Payment>> list(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) String patientName,
            @RequestParam(required = false) Integer status) {
        return Result.success(paymentService.list(patientName, status, page, size));
    }

    /**
     * 更新支付状态
     * <p>
     * 更新指定收费记录的支付状态，例如从未支付（0）更新为已支付（1）。
     * 确认支付时系统会记录支付时间。
     * </p>
     *
     * @param id     收费记录ID
     * @param status 新的支付状态，0-未支付，1-已支付
     * @return 操作结果
     */
    @Operation(summary = "更新支付状态")
    @PutMapping("/{id}/status")
    public Result<Void> updateStatus(@PathVariable Long id, @RequestParam Integer status) {
        paymentService.updateStatus(id, status);
        return Result.success();
    }
}
