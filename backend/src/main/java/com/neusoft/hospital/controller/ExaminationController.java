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
/**
 * 检查管理控制器
 * <p>
 * 提供检查记录的增删改查功能，支持录入检查结果。
 * 医生角色只能查看和操作自己患者的检查记录。
 * 接口路径：/api/examination
 * </p>
 */
public class ExaminationController {

    private final ExaminationService examinationService;

    /**
     * 新增检查记录
     * <p>
     * 为患者的挂号记录添加一项检查，包括检查项目名称、检查部位、费用等信息。
     * </p>
     *
     * @param examination 检查信息
     * @return 操作结果
     */
    @Operation(summary = "新增检查")
    @PostMapping
    public Result<Void> add(@RequestBody Examination examination) {
        examinationService.add(examination);
        return Result.success();
    }

    /**
     * 修改检查记录
     * <p>
     * 根据检查ID更新指定检查记录的信息。
     * </p>
     *
     * @param id          检查ID
     * @param examination 更新后的检查信息
     * @return 操作结果
     */
    @Operation(summary = "修改检查")
    @PutMapping("/{id}")
    public Result<Void> update(@PathVariable Long id, @RequestBody Examination examination) {
        examination.setId(id);
        examinationService.update(examination);
        return Result.success();
    }

    /**
     * 查询检查详情
     * <p>
     * 根据检查ID查询单个检查记录的详细信息。
     * </p>
     *
     * @param id 检查ID
     * @return 指定检查记录的详细信息
     */
    @Operation(summary = "查询检查详情")
    @GetMapping("/{id}")
    public Result<Examination> getById(@PathVariable Long id) {
        return Result.success(examinationService.getById(id));
    }

    /**
     * 分页查询检查列表
     * <p>
     * 根据挂号ID、患者ID、状态等条件进行分页查询。医生角色只能查看自己患者的检查记录，
     * 系统会根据当前登录用户的角色自动过滤数据。
     * </p>
     *
     * @param request        HTTP请求对象，用于获取当前用户角色和医生ID
     * @param page           页码，默认值为1
     * @param size           每页记录数，默认值为10
     * @param registrationId 挂号ID（可选），用于按挂号筛选
     * @param patientId      患者ID（可选），用于按患者筛选
     * @param status         检查状态（可选），0-未出结果，1-已出结果
     * @return 分页结果，包含检查视图对象列表和总记录数
     */
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

    /**
     * 录入检查结果
     * <p>
     * 为指定的检查记录录入检查结果，更新检查状态为已出结果。
     * </p>
     *
     * @param id     检查ID
     * @param params 参数Map，包含key为"result"的检查结果文本
     * @return 操作结果
     */
    @Operation(summary = "录入检查结果")
    @PutMapping("/{id}/result")
    public Result<Void> updateResult(@PathVariable Long id, @RequestBody Map<String, String> params) {
        examinationService.updateResult(id, params.get("result"));
        return Result.success();
    }
}
