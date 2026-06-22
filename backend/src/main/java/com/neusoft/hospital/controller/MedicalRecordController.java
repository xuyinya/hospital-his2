package com.neusoft.hospital.controller;

import com.neusoft.hospital.common.PageResult;
import com.neusoft.hospital.common.Result;
import com.neusoft.hospital.entity.MedicalRecord;
import com.neusoft.hospital.entity.vo.MedicalRecordVO;
import com.neusoft.hospital.service.MedicalRecordService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Tag(name = "病历管理")
@RestController
@RequestMapping("/api/medical-record")
@RequiredArgsConstructor
public class MedicalRecordController {

    private final MedicalRecordService medicalRecordService;

    @Operation(summary = "新增病历")
    @PostMapping
    public Result<Void> add(@RequestBody MedicalRecord medicalRecord) {
        medicalRecordService.add(medicalRecord);
        return Result.success();
    }

    @Operation(summary = "修改病历")
    @PutMapping("/{id}")
    public Result<Void> update(@PathVariable Long id, @RequestBody MedicalRecord medicalRecord) {
        medicalRecord.setId(id);
        medicalRecordService.update(medicalRecord);
        return Result.success();
    }

    @Operation(summary = "查询病历详情")
    @GetMapping("/{id}")
    public Result<MedicalRecord> getById(@PathVariable Long id) {
        return Result.success(medicalRecordService.getById(id));
    }

    @Operation(summary = "根据挂号查病历")
    @GetMapping("/registration/{regId}")
    public Result<MedicalRecord> getByRegistrationId(@PathVariable Long regId) {
        return Result.success(medicalRecordService.getByRegistrationId(regId));
    }

    @Operation(summary = "病历列表")
    @GetMapping("/list")
    public Result<PageResult<MedicalRecordVO>> list(
            HttpServletRequest request,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) Long registrationId,
            @RequestParam(required = false) Long patientId,
            @RequestParam(required = false) String patientName) {
        // 根据不同角色自动过滤数据
        Long doctorId = null;
        String role = (String) request.getAttribute("role");
        if ("patient".equals(role)) {
            patientId = (Long) request.getAttribute("userId");
        } else if ("doctor".equals(role)) {
            doctorId = (Long) request.getAttribute("doctorId");
        }
        return Result.success(medicalRecordService.list(registrationId, patientId, doctorId, patientName, page, size));
    }
}
