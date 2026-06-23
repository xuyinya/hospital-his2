package com.neusoft.hospital.controller;

import com.neusoft.hospital.common.PageResult;
import com.neusoft.hospital.common.Result;
import com.neusoft.hospital.entity.Treatment;
import com.neusoft.hospital.entity.vo.TreatmentVO;
import com.neusoft.hospital.service.TreatmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Tag(name = "处置管理")
@RestController
@RequestMapping("/api/treatment")
@RequiredArgsConstructor
/**
 * 处置管理控制器
 * <p>
 * 提供处置记录的增删改查功能，支持更新处置完成状态。
 * 医生角色只能查看和操作自己患者的处置记录。
 * 接口路径：/api/treatment
 * </p>
 */
public class TreatmentController {

    private final TreatmentService treatmentService;

    /**
     * 新增处置记录
     * <p>
     * 为患者的就诊记录添加一项处置，包括处置项目名称、处置内容、费用等信息。
     * 处置创建后状态默认为未完成。
     * </p>
     *
     * @param treatment 处置信息
     * @return 操作结果
     */
    @Operation(summary = "新增处置")
    @PostMapping
    public Result<Void> add(@RequestBody Treatment treatment) {
        treatmentService.add(treatment);
        return Result.success();
    }

    /**
     * 修改处置记录
     * <p>
     * 根据处置ID更新指定处置记录的信息，如调整处置内容等。
     * </p>
     *
     * @param id        处置ID
     * @param treatment 更新后的处置信息
     * @return 操作结果
     */
    @Operation(summary = "修改处置")
    @PutMapping("/{id}")
    public Result<Void> update(@PathVariable Long id, @RequestBody Treatment treatment) {
        treatment.setId(id);
        treatmentService.update(treatment);
        return Result.success();
    }

    /**
     * 查询处置详情
     * <p>
     * 根据处置ID查询单条处置记录的详细信息，包括处置项目、内容、费用等。
     * </p>
     *
     * @param id 处置ID
     * @return 指定处置记录的详细信息
     */
    @Operation(summary = "查询处置详情")
    @GetMapping("/{id}")
    public Result<Treatment> getById(@PathVariable Long id) {
        return Result.success(treatmentService.getById(id));
    }

    /**
     * 分页查询处置列表
     * <p>
     * 根据挂号ID、患者ID、状态等条件进行分页查询。医生角色只能查看自己患者的处置记录，
     * 系统会根据当前登录用户的角色自动过滤数据。
     * </p>
     *
     * @param request        HTTP请求对象，用于获取当前用户角色和医生ID
     * @param page           页码，默认值为1
     * @param size           每页记录数，默认值为10
     * @param registrationId 挂号ID（可选），用于按挂号筛选
     * @param patientId      患者ID（可选），用于按患者筛选
     * @param status         处置状态（可选），0-未完成，1-已完成
     * @return 分页结果，包含处置视图对象列表和总记录数
     */
    @Operation(summary = "处置列表")
    @GetMapping("/list")
    public Result<PageResult<TreatmentVO>> list(
            HttpServletRequest request,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) Long registrationId,
            @RequestParam(required = false) Long patientId,
            @RequestParam(required = false) Integer status) {
        // 医生角色只能看自己患者的处置
        Long doctorId = null;
        if ("doctor".equals(request.getAttribute("role"))) {
            doctorId = (Long) request.getAttribute("doctorId");
        }
        return Result.success(treatmentService.list(registrationId, patientId, doctorId, status, page, size));
    }

    /**
     * 更新处置状态
     * <p>
     * 更新指定处置记录的完成状态，例如从未完成（0）更新为已完成（1）。
     * </p>
     *
     * @param id     处置ID
     * @param status 新的处置状态，0-未完成，1-已完成
     * @return 操作结果
     */
    @Operation(summary = "更新处置状态")
    @PutMapping("/{id}/status")
    public Result<Void> updateStatus(@PathVariable Long id, @RequestParam Integer status) {
        treatmentService.updateStatus(id, status);
        return Result.success();
    }
}
