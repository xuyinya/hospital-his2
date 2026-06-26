package com.neusoft.hospital.controller;

import com.neusoft.hospital.common.PageResult;
import com.neusoft.hospital.common.Result;
import com.neusoft.hospital.entity.Laboratory;
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
/**
 * 检验管理控制器
 * <p>
 * 提供检验记录的增删改查功能，支持录入检验结果及参考范围。
 * 医生角色只能查看和操作自己患者的检验记录。
 * 接口路径：/api/laboratory
 * </p>
 */
public class LaboratoryController {

    private final LaboratoryService laboratoryService;

    /**
     * 新增检验记录
     * <p>
     * 为患者的挂号记录添加一项检验，包括检验项目名称、检验标本、费用等信息。
     * </p>
     *
     * @param laboratory 检验信息
     * @return 操作结果
     */
    @Operation(summary = "新增检验")
    @PostMapping
    public Result<Void> add(@RequestBody Laboratory laboratory) {
        laboratoryService.add(laboratory);
        return Result.success();
    }

    /**
     * 修改检验记录
     * <p>
     * 根据检验ID更新指定检验记录的信息。
     * </p>
     *
     * @param id          检验ID
     * @param laboratory 更新后的检验信息
     * @return 操作结果
     */
    @Operation(summary = "修改检验")
    @PutMapping("/{id}")
    public Result<Void> update(@PathVariable Long id, @RequestBody Laboratory laboratory) {
        laboratory.setId(id);
        laboratoryService.update(laboratory);
        return Result.success();
    }

    /**
     * 查询检验详情
     * <p>
     * 根据检验ID查询单个检验记录的详细信息。
     * </p>
     *
     * @param id 检验ID
     * @return 指定检验记录的详细信息
     */
    @Operation(summary = "查询检验详情")
    @GetMapping("/{id}")
    public Result<Laboratory> getById(@PathVariable Long id) {
        return Result.success(laboratoryService.getById(id));
    }

    /**
     * 分页查询检验列表
     * <p>
     * 根据挂号ID、患者ID、状态等条件进行分页查询。医生角色只能查看自己患者的检验记录，
     * 系统会根据当前登录用户的角色自动过滤数据。
     * </p>
     *
     * @param request        HTTP请求对象，用于获取当前用户角色和医生ID
     * @param page           页码，默认值为1
     * @param size           每页记录数，默认值为10
     * @param registrationId 挂号ID（可选），用于按挂号筛选
     * @param patientId      患者ID（可选），用于按患者筛选
     * @param status         检验状态（可选），0-未出结果，1-已出结果
     * @return 分页结果，包含检验视图对象列表和总记录数
     */
    @Operation(summary = "检验列表")
    @GetMapping("/list")
    public Result<PageResult<Laboratory>> list(
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

    /**
     * 录入检验结果
     * <p>
     * 为指定的检验记录录入检验结果和参考范围，更新检验状态为已出结果。
     * 结果和参考范围均以文本形式存储。
     * </p>
     *
     * @param id     检验ID
     * @param params 参数Map，包含key为"result"的检验结果文本和key为"referenceRange"的参考范围文本
     * @return 操作结果
     */
    @Operation(summary = "录入检验结果")
    @PutMapping("/{id}/result")
    public Result<Void> updateResult(@PathVariable Long id, @RequestBody Map<String, String> params) {
        laboratoryService.updateResult(id, params.get("result"), params.get("referenceRange"));
        return Result.success();
    }
}
