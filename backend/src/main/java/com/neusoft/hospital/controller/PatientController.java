package com.neusoft.hospital.controller;

import com.neusoft.hospital.common.PageResult;
import com.neusoft.hospital.common.Result;
import com.neusoft.hospital.entity.Patient;
import com.neusoft.hospital.service.PatientService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Tag(name = "患者管理")
@RestController
@RequestMapping("/api/patient")
@RequiredArgsConstructor
public class PatientController {

    private final PatientService patientService;

    @Operation(summary = "新增患者")
    @PostMapping
    public Result<Void> add(@RequestBody Patient patient) {
        patientService.add(patient);
        return Result.success();
    }

    @Operation(summary = "修改患者")
    @PutMapping("/{id}")
    public Result<Void> update(@PathVariable Long id, @RequestBody Patient patient) {
        patient.setId(id);
        patientService.update(patient);
        return Result.success();
    }

    @Operation(summary = "删除患者")
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        patientService.delete(id);
        return Result.success();
    }

    @Operation(summary = "查询患者详情")
    @GetMapping("/{id}")
    public Result<Patient> getById(@PathVariable Long id) {
        return Result.success(patientService.getById(id));
    }

    @Operation(summary = "患者列表")
    @GetMapping("/list")
    public Result<PageResult<Patient>> list(
            HttpServletRequest request,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) String patientName) {
        // 医生角色只能看自己的患者
        Long doctorId = null;
        if ("doctor".equals(request.getAttribute("role"))) {
            doctorId = (Long) request.getAttribute("doctorId");
        }
        return Result.success(patientService.list(patientName, doctorId, page, size));
    }
}
