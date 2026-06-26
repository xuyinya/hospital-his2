package com.neusoft.hospital.controller;

import com.neusoft.hospital.common.PageResult;
import com.neusoft.hospital.common.Result;
import com.neusoft.hospital.entity.Prescription;
import com.neusoft.hospital.entity.PrescriptionDetail;
import com.neusoft.hospital.service.PrescriptionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Tag(name = "处方管理")
@RestController
@RequestMapping("/api/prescription")
@RequiredArgsConstructor
/**
 * 处方管理控制器
 * <p>
 * 提供处方及处方明细的增删改查功能，支持更新处方状态（如确认取药）。
 * 根据角色自动过滤数据：患者只能查看自己的处方，医生只能查看自己开具的处方。
 * 接口路径：/api/prescription
 * </p>
 */
public class PrescriptionController {

    private final PrescriptionService prescriptionService;

    /**
     * 新增处方
     * <p>
     * 为患者的就诊记录创建一张处方，包含处方编号、开具医生、患者等信息。
     * 处方创建后状态默认为待取药。
     * </p>
     *
     * @param prescription 处方信息
     * @return 操作结果
     */
    @Operation(summary = "新增处方")
    @PostMapping
    public Result<Void> add(@RequestBody Prescription prescription) {
        prescriptionService.add(prescription);
        return Result.success();
    }

    /**
     * 新增处方明细
     * <p>
     * 为指定的处方添加一条药品明细，包括药品名称、规格、数量、用法用量等信息。
     * 一张处方可以包含多条药品明细。
     * </p>
     *
     * @param detail 处方明细信息
     * @return 操作结果
     */
    @Operation(summary = "新增处方明细")
    @PostMapping("/detail")
    public Result<Void> addDetail(@RequestBody PrescriptionDetail detail) {
        prescriptionService.addDetail(detail);
        return Result.success();
    }

    /**
     * 查询处方详情
     * <p>
     * 根据处方ID查询单张处方的详细信息，包括处方编号、开具医生、患者信息等。
     * </p>
     *
     * @param id 处方ID
     * @return 指定处方的详细信息
     */
    @Operation(summary = "查询处方详情")
    @GetMapping("/{id}")
    public Result<Prescription> getById(@PathVariable Long id) {
        return Result.success(prescriptionService.getById(id));
    }

    /**
     * 查询处方明细列表
     * <p>
     * 根据处方ID查询该处方的所有药品明细，包括药品名称、规格、数量、单价、用法用量等。
     * </p>
     *
     * @param id 处方ID
     * @return 该处方下的药品明细列表
     */
    @Operation(summary = "查询处方明细")
    @GetMapping("/{id}/details")
    public Result<List<PrescriptionDetail>> getDetails(@PathVariable Long id) {
        return Result.success(prescriptionService.getDetails(id));
    }

    /**
     * 分页查询处方列表
     * <p>
     * 根据挂号ID、患者ID、状态等条件进行分页查询。
     * 根据角色自动过滤数据：患者角色只能查看自己的处方，医生角色只能查看自己开具的处方。
     * </p>
     *
     * @param request        HTTP请求对象，用于获取当前用户角色和ID
     * @param page           页码，默认值为1
     * @param size           每页记录数，默认值为10
     * @param registrationId 挂号ID（可选），用于按挂号筛选
     * @param patientId      患者ID（可选），用于按患者筛选
     * @param status         处方状态（可选），0-待取药，1-已取药
     * @return 分页结果，包含处方视图对象列表和总记录数
     */
    @Operation(summary = "处方列表")
    @GetMapping("/list")
    public Result<PageResult<Prescription>> list(
            HttpServletRequest request,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) Long registrationId,
            @RequestParam(required = false) Long patientId,
            @RequestParam(required = false) Integer status) {
        // 根据不同角色自动过滤数据
        Long doctorId = null;
        String role = (String) request.getAttribute("role");
        if ("patient".equals(role)) {
            patientId = (Long) request.getAttribute("userId");
        } else if ("doctor".equals(role)) {
            doctorId = (Long) request.getAttribute("doctorId");
        }
        return Result.success(prescriptionService.list(registrationId, patientId, doctorId, status, page, size));
    }

    /**
     * 更新处方状态
     * <p>
     * 更新指定处方的状态，例如从待取药（0）更新为已取药（1），用于确认患者已取药。
     * </p>
     *
     * @param id     处方ID
     * @param status 新的处方状态，0-待取药，1-已取药
     * @return 操作结果
     */
    @Operation(summary = "更新处方状态")
    @PutMapping("/{id}/status")
    public Result<Void> updateStatus(@PathVariable Long id, @RequestParam Integer status) {
        prescriptionService.updateStatus(id, status);
        return Result.success();
    }
}
