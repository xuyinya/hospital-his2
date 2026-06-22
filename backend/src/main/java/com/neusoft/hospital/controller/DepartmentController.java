package com.neusoft.hospital.controller;

import com.neusoft.hospital.common.Result;
import com.neusoft.hospital.entity.Department;
import com.neusoft.hospital.service.DepartmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Tag(name = "科室管理")
@RestController
@RequestMapping("/api/department")
@RequiredArgsConstructor
public class DepartmentController {

    private final DepartmentService departmentService;

    @Operation(summary = "科室列表")
    @GetMapping("/list")
    public Result<List<Department>> list() {
        return Result.success(departmentService.list());
    }

    @Operation(summary = "科室详情")
    @GetMapping("/{id}")
    public Result<Department> getById(@PathVariable Long id) {
        return Result.success(departmentService.getById(id));
    }
}
