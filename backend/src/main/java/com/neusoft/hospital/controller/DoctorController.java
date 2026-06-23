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
/**
 * 医生管理控制器
 * <p>
 * 提供医生信息的增删改查功能，支持按科室查询医生（用于下拉选择），
 * 删除医生时会检查是否存在关联的挂号记录。
 * 接口路径：/api/doctor
 * </p>
 */
public class DoctorController {

    private final DoctorService doctorService;

    /**
     * 分页查询医生列表
     * <p>
     * 根据医生姓名和科室ID进行模糊分页查询，返回医生视图对象列表。
     * 支持按医生姓名模糊搜索和按科室筛选。
     * </p>
     *
     * @param page       页码，默认值为1
     * @param size       每页记录数，默认值为10
     * @param doctorName 医生姓名（可选），用于模糊搜索
     * @param deptId     科室ID（可选），用于按科室筛选
     * @return 分页结果，包含医生视图对象列表和总记录数
     */
    @Operation(summary = "医生列表（分页）")
    @GetMapping("/list")
    public Result<PageResult<DoctorVO>> list(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) String doctorName,
            @RequestParam(required = false) Long deptId) {
        return Result.success(doctorService.listVO(doctorName, deptId, page, size));
    }

    /**
     * 新增医生
     * <p>
     * 添加一条新的医生记录到系统中。
     * </p>
     *
     * @param doctor 医生信息，包含姓名、所属科室、职称、联系方式等
     * @return 操作结果
     */
    @Operation(summary = "新增医生")
    @PostMapping
    public Result<Void> add(@RequestBody Doctor doctor) {
        doctorService.add(doctor);
        return Result.success();
    }

    /**
     * 修改医生信息
     * <p>
     * 根据医生ID更新指定医生的信息。
     * </p>
     *
     * @param id     医生ID
     * @param doctor 更新后的医生信息
     * @return 操作结果
     */
    @Operation(summary = "修改医生")
    @PutMapping("/{id}")
    public Result<Void> update(@PathVariable Long id, @RequestBody Doctor doctor) {
        doctor.setId(id);
        doctorService.update(doctor);
        return Result.success();
    }

    /**
     * 删除医生
     * <p>
     * 根据医生ID删除指定医生。删除前会检查该医生是否有关联的挂号记录，
     * 如有挂号记录则不允许删除，以保证数据完整性。
     * </p>
     *
     * @param id 医生ID
     * @return 操作结果；若有关联挂号记录则返回错误信息
     */
    @Operation(summary = "删除医生（检查无挂号记录才可删除）")
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        doctorService.delete(id);
        return Result.success();
    }

    /**
     * 获取医生详情
     * <p>
     * 根据医生ID查询单个医生的详细信息。
     * </p>
     *
     * @param id 医生ID
     * @return 指定医生的详细信息
     */
    @Operation(summary = "医生详情")
    @GetMapping("/{id}")
    public Result<Doctor> getById(@PathVariable Long id) {
        return Result.success(doctorService.getById(id));
    }

    /**
     * 按科室查询医生列表
     * <p>
     * 根据科室ID查询该科室下的所有医生，主要用于前端下拉选择框的数据源。
     * 返回医生视图对象列表，包含医生姓名、职称等信息。
     * </p>
     *
     * @param deptId 科室ID
     * @return 指定科室下的医生列表
     */
    @Operation(summary = "按科室查询医生（下拉用）")
    @GetMapping("/dept/{deptId}")
    public Result<List<DoctorVO>> getByDeptId(@PathVariable Long deptId) {
        return Result.success(doctorService.getByDeptIdVO(deptId));
    }
}
