package com.neusoft.hospital.controller;

import com.neusoft.hospital.common.PageResult;
import com.neusoft.hospital.common.Result;
import com.neusoft.hospital.entity.Treatment;
import com.neusoft.hospital.entity.vo.TreatmentVO;
import com.neusoft.hospital.service.TreatmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Tag(name = "处置管理")
@RestController
@RequestMapping("/api/treatment")
@RequiredArgsConstructor
public class TreatmentController {

    private final TreatmentService treatmentService;

    @Operation(summary = "新增处置")
    @PostMapping
    public Result<Void> add(@RequestBody Treatment treatment) {
        treatmentService.add(treatment);
        return Result.success();
    }

    @Operation(summary = "修改处置")
    @PutMapping("/{id}")
    public Result<Void> update(@PathVariable Long id, @RequestBody Treatment treatment) {
        treatment.setId(id);
        treatmentService.update(treatment);
        return Result.success();
    }

    @Operation(summary = "查询处置详情")
    @GetMapping("/{id}")
    public Result<Treatment> getById(@PathVariable Long id) {
        return Result.success(treatmentService.getById(id));
    }

    @Operation(summary = "处置列表")
    @GetMapping("/list")
    public Result<PageResult<TreatmentVO>> list(
            HttpServletRequest request,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) Long registrationId,
            @RequestParam(required = false) Long patientId,
            @RequestParam(required = false) Integer status) {
        // 医生角色只能看自己患者的处置
        Long doctorId = null;
        if ("doctor".equals(request.getAttribute("role"))) {
            doctorId = (Long) request.getAttribute("doctorId");
        }
        return Result.success(treatmentService.list(registrationId, patientId, doctorId, status, page, size));
    }

    @Operation(summary = "更新处置状态")
    @PutMapping("/{id}/status")
    public Result<Void> updateStatus(@PathVariable Long id, @RequestParam Integer status) {
        treatmentService.updateStatus(id, status);
        return Result.success();
    }
}
