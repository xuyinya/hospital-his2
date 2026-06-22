package com.neusoft.hospital.controller;

import com.neusoft.hospital.common.PageResult;
import com.neusoft.hospital.common.Result;
import com.neusoft.hospital.entity.Examination;
import com.neusoft.hospital.entity.vo.ExaminationVO;
import com.neusoft.hospital.service.ExaminationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@Tag(name = "检查管理")
@RestController
@RequestMapping("/api/examination")
@RequiredArgsConstructor
public class ExaminationController {

    private final ExaminationService examinationService;

    @Operation(summary = "新增检查")
    @PostMapping
    public Result<Void> add(@RequestBody Examination examination) {
        examinationService.add(examination);
        return Result.success();
    }

    @Operation(summary = "修改检查")
    @PutMapping("/{id}")
    public Result<Void> update(@PathVariable Long id, @RequestBody Examination examination) {
        examination.setId(id);
        examinationService.update(examination);
        return Result.success();
    }

    @Operation(summary = "查询检查详情")
    @GetMapping("/{id}")
    public Result<Examination> getById(@PathVariable Long id) {
        return Result.success(examinationService.getById(id));
    }

    @Operation(summary = "检查列表")
    @GetMapping("/list")
    public Result<PageResult<ExaminationVO>> list(
            HttpServletRequest request,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) Long registrationId,
            @RequestParam(required = false) Long patientId,
            @RequestParam(required = false) Integer status) {
        // 医生角色只能看自己患者的检查
        Long doctorId = null;
        if ("doctor".equals(request.getAttribute("role"))) {
            doctorId = (Long) request.getAttribute("doctorId");
        }
        return Result.success(examinationService.list(registrationId, patientId, doctorId, status, page, size));
    }

    @Operation(summary = "录入检查结果")
    @PutMapping("/{id}/result")
    public Result<Void> updateResult(@PathVariable Long id, @RequestBody Map<String, String> params) {
        examinationService.updateResult(id, params.get("result"));
        return Result.success();
    }
}
