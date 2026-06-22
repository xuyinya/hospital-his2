package com.neusoft.hospital.controller;

import com.neusoft.hospital.common.PageResult;
import com.neusoft.hospital.common.Result;
import com.neusoft.hospital.entity.Prescription;
import com.neusoft.hospital.entity.PrescriptionDetail;
import com.neusoft.hospital.entity.vo.PrescriptionDetailVO;
import com.neusoft.hospital.entity.vo.PrescriptionVO;
import com.neusoft.hospital.service.PrescriptionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Tag(name = "处方管理")
@RestController
@RequestMapping("/api/prescription")
@RequiredArgsConstructor
public class PrescriptionController {

    private final PrescriptionService prescriptionService;

    @Operation(summary = "新增处方")
    @PostMapping
    public Result<Void> add(@RequestBody Prescription prescription) {
        prescriptionService.add(prescription);
        return Result.success();
    }

    @Operation(summary = "新增处方明细")
    @PostMapping("/detail")
    public Result<Void> addDetail(@RequestBody PrescriptionDetail detail) {
        prescriptionService.addDetail(detail);
        return Result.success();
    }

    @Operation(summary = "查询处方详情")
    @GetMapping("/{id}")
    public Result<Prescription> getById(@PathVariable Long id) {
        return Result.success(prescriptionService.getById(id));
    }

    @Operation(summary = "查询处方明细")
    @GetMapping("/{id}/details")
    public Result<List<PrescriptionDetailVO>> getDetails(@PathVariable Long id) {
        return Result.success(prescriptionService.getDetails(id));
    }

    @Operation(summary = "处方列表")
    @GetMapping("/list")
    public Result<PageResult<PrescriptionVO>> list(
            HttpServletRequest request,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) Long registrationId,
            @RequestParam(required = false) Long patientId,
            @RequestParam(required = false) Integer status) {
        // 根据不同角色自动过滤数据
        Long doctorId = null;
        String role = (String) request.getAttribute("role");
        if ("patient".equals(role)) {
            patientId = (Long) request.getAttribute("userId");
        } else if ("doctor".equals(role)) {
            doctorId = (Long) request.getAttribute("doctorId");
        }
        return Result.success(prescriptionService.list(registrationId, patientId, doctorId, status, page, size));
    }

    @Operation(summary = "更新处方状态")
    @PutMapping("/{id}/status")
    public Result<Void> updateStatus(@PathVariable Long id, @RequestParam Integer status) {
        prescriptionService.updateStatus(id, status);
        return Result.success();
    }
}
