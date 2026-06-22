package com.neusoft.hospital.controller;

import com.neusoft.hospital.common.PageResult;
import com.neusoft.hospital.common.Result;
import com.neusoft.hospital.entity.Registration;
import com.neusoft.hospital.entity.vo.RegistrationVO;
import com.neusoft.hospital.service.RegistrationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Tag(name = "挂号管理")
@RestController
@RequestMapping("/api/registration")
@RequiredArgsConstructor
public class RegistrationController {

    private final RegistrationService registrationService;

    @Operation(summary = "新增挂号")
    @PostMapping
    public Result<Void> add(@RequestBody Registration registration) {
        registrationService.add(registration);
        return Result.success();
    }

    @Operation(summary = "修改挂号")
    @PutMapping("/{id}")
    public Result<Void> update(@PathVariable Long id, @RequestBody Registration registration) {
        registration.setId(id);
        registrationService.update(registration);
        return Result.success();
    }

    @Operation(summary = "查询挂号详情")
    @GetMapping("/{id}")
    public Result<Registration> getById(@PathVariable Long id) {
        return Result.success(registrationService.getById(id));
    }

    @Operation(summary = "挂号列表")
    @GetMapping("/list")
    public Result<PageResult<RegistrationVO>> list(
            HttpServletRequest request,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) Integer status,
            @RequestParam(required = false) String patientName,
            @RequestParam(required = false) Long deptId) {
        // 根据不同角色自动过滤数据
        Long patientId = null;
        Long doctorId = null;
        String role = (String) request.getAttribute("role");
        if ("patient".equals(role)) {
            patientId = (Long) request.getAttribute("userId");
        } else if ("doctor".equals(role)) {
            doctorId = (Long) request.getAttribute("doctorId");
        }
        return Result.success(registrationService.list(patientId, doctorId, status, patientName, deptId, page, size));
    }

    @Operation(summary = "取消挂号")
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        registrationService.updateStatus(id, 2);
        return Result.success();
    }

    @Operation(summary = "更新挂号状态")
    @PutMapping("/{id}/status")
    public Result<Void> updateStatus(@PathVariable Long id, @RequestParam Integer status) {
        registrationService.updateStatus(id, status);
        return Result.success();
    }
}
