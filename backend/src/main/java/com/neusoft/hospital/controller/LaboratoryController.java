package com.neusoft.hospital.controller;

import com.neusoft.hospital.common.PageResult;
import com.neusoft.hospital.common.Result;
import com.neusoft.hospital.entity.Laboratory;
import com.neusoft.hospital.entity.vo.LaboratoryVO;
import com.neusoft.hospital.service.LaboratoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@Tag(name = "检验管理")
@RestController
@RequestMapping("/api/laboratory")
@RequiredArgsConstructor
public class LaboratoryController {

    private final LaboratoryService laboratoryService;

    @Operation(summary = "新增检验")
    @PostMapping
    public Result<Void> add(@RequestBody Laboratory laboratory) {
        laboratoryService.add(laboratory);
        return Result.success();
    }

    @Operation(summary = "修改检验")
    @PutMapping("/{id}")
    public Result<Void> update(@PathVariable Long id, @RequestBody Laboratory laboratory) {
        laboratory.setId(id);
        laboratoryService.update(laboratory);
        return Result.success();
    }

    @Operation(summary = "查询检验详情")
    @GetMapping("/{id}")
    public Result<Laboratory> getById(@PathVariable Long id) {
        return Result.success(laboratoryService.getById(id));
    }

    @Operation(summary = "检验列表")
    @GetMapping("/list")
    public Result<PageResult<LaboratoryVO>> list(
            HttpServletRequest request,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) Long registrationId,
            @RequestParam(required = false) Long patientId,
            @RequestParam(required = false) Integer status) {
        // 医生角色只能看自己患者的检验
        Long doctorId = null;
        if ("doctor".equals(request.getAttribute("role"))) {
            doctorId = (Long) request.getAttribute("doctorId");
        }
        return Result.success(laboratoryService.list(registrationId, patientId, doctorId, status, page, size));
    }

    @Operation(summary = "录入检验结果")
    @PutMapping("/{id}/result")
    public Result<Void> updateResult(@PathVariable Long id, @RequestBody Map<String, String> params) {
        laboratoryService.updateResult(id, params.get("result"), params.get("referenceRange"));
        return Result.success();
    }
}
