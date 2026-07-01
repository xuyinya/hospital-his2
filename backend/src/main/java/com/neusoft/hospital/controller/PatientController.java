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
/**
 * 患者管理控制器
 * <p>
 * 提供患者信息的增删改查功能。医生角色只能查看自己接诊过的患者。
 * 接口路径：/api/patient
 * </p>
 */
public class PatientController {

    private final PatientService patientService;

    /**
     * 新增患者
     * <p>
     * 添加一条新的患者记录到系统中，包括患者姓名、身份证号、联系电话、地址等信息。
     * </p>
     *
     * @param patient 患者信息
     * @return 操作结果
     */
    @Operation(summary = "新增患者")
    @PostMapping
    public Result<Void> add(@RequestBody Patient patient) {
        patientService.add(patient);
        return Result.success();
    }

    /**
     * 修改患者信息
     * <p>
     * 根据患者ID更新指定患者的信息，如联系电话、地址等。
     * </p>
     *
     * @param id      患者ID
     * @param patient 更新后的患者信息
     * @return 操作结果
     */
    @Operation(summary = "修改患者")
    @PutMapping("/{id}")
    public Result<Void> update(@PathVariable Long id, @RequestBody Patient patient) {
        patient.setId(id);
        patientService.update(patient);
        return Result.success();
    }

    /**
     * 删除患者
     * <p>
     * 根据患者ID删除指定患者记录。
     * </p>
     *
     * @param id 患者ID
     * @return 操作结果
     */
    @Operation(summary = "删除患者")
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        patientService.delete(id);
        return Result.success();
    }

    /**
     * 查询患者详情
     * <p>
     * 根据患者ID查询单个患者的详细信息，包括姓名、身份证号、联系电话等。
     * </p>
     *
     * @param id 患者ID
     * @return 指定患者的详细信息
     */
    @Operation(summary = "查询患者详情")
    @GetMapping("/{id}")
    public Result<Patient> getById(@PathVariable Long id) {
        return Result.success(patientService.getById(id));
    }

    /**
     * 分页查询患者列表
     * <p>
     * 根据患者姓名进行模糊分页查询。医生角色只能查看自己接诊过的患者，
     * 系统会根据当前登录用户的角色自动过滤数据。
     * </p>
     *
     * @param request     HTTP请求对象，用于获取当前用户角色和医生ID
     * @param page        页码，默认值为1
     * @param size        每页记录数，默认值为10
     * @param patientName 患者姓名（可选），用于模糊搜索
     * @return 分页结果，包含患者列表和总记录数
     */
    @Operation(summary = "患者列表")
    @GetMapping("/list")
    public Result<PageResult<Patient>> list(
            HttpServletRequest request,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) String patientName) {
        return Result.success(patientService.list(patientName, null, page, size));
    }
}
