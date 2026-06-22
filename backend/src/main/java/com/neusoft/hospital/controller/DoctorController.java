package com.neusoft.hospital.controller;

import com.neusoft.hospital.common.PageResult;
import com.neusoft.hospital.common.Result;
import com.neusoft.hospital.entity.Doctor;
import com.neusoft.hospital.entity.vo.DoctorVO;
import com.neusoft.hospital.service.DoctorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Tag(name = "医生管理")
@RestController
@RequestMapping("/api/doctor")
@RequiredArgsConstructor
public class DoctorController {

    private final DoctorService doctorService;

    @Operation(summary = "医生列表（分页）")
    @GetMapping("/list")
    public Result<PageResult<DoctorVO>> list(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) String doctorName,
            @RequestParam(required = false) Long deptId) {
        return Result.success(doctorService.listVO(doctorName, deptId, page, size));
    }

    @Operation(summary = "新增医生")
    @PostMapping
    public Result<Void> add(@RequestBody Doctor doctor) {
        doctorService.add(doctor);
        return Result.success();
    }

    @Operation(summary = "修改医生")
    @PutMapping("/{id}")
    public Result<Void> update(@PathVariable Long id, @RequestBody Doctor doctor) {
        doctor.setId(id);
        doctorService.update(doctor);
        return Result.success();
    }

    @Operation(summary = "删除医生（检查无挂号记录才可删除）")
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        doctorService.delete(id);
        return Result.success();
    }

    @Operation(summary = "医生详情")
    @GetMapping("/{id}")
    public Result<Doctor> getById(@PathVariable Long id) {
        return Result.success(doctorService.getById(id));
    }

    @Operation(summary = "按科室查询医生（下拉用）")
    @GetMapping("/dept/{deptId}")
    public Result<List<DoctorVO>> getByDeptId(@PathVariable Long deptId) {
        return Result.success(doctorService.getByDeptIdVO(deptId));
    }
}
