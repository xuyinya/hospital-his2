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
/**
 * 科室管理控制器
 * <p>
 * 提供科室信息的查询功能，包括获取科室列表和科室详情。
 * 科室数据为只读，由系统初始化时预设。
 * 接口路径：/api/department
 * </p>
 */
public class DepartmentController {

    private final DepartmentService departmentService;

    /**
     * 获取科室列表
     * <p>
     * 查询所有科室信息，返回科室列表。无分页，返回全部科室数据。
     * 常用于下拉选择框等前端组件的数据源。
     * </p>
     *
     * @return 科室列表，每个科室包含ID、名称、简介等信息
     */
    @Operation(summary = "科室列表")
    @GetMapping("/list")
    public Result<List<Department>> list() {
        return Result.success(departmentService.list());
    }

    /**
     * 获取科室详情
     * <p>
     * 根据科室ID查询单个科室的详细信息。
     * </p>
     *
     * @param id 科室ID
     * @return 指定科室的详细信息
     */
    @Operation(summary = "科室详情")
    @GetMapping("/{id}")
    public Result<Department> getById(@PathVariable Long id) {
        return Result.success(departmentService.getById(id));
    }
}
