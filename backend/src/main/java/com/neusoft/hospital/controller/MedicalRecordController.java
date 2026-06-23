package com.neusoft.hospital.controller;

import com.neusoft.hospital.common.PageResult;
import com.neusoft.hospital.common.Result;
import com.neusoft.hospital.entity.MedicalRecord;
import com.neusoft.hospital.entity.vo.MedicalRecordVO;
import com.neusoft.hospital.service.MedicalRecordService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Tag(name = "病历管理")
@RestController
@RequestMapping("/api/medical-record")
@RequiredArgsConstructor
/**
 * 病历管理控制器
 * <p>
 * 提供病历记录的增删改查功能，支持按挂号ID查询病历。
 * 根据角色自动过滤数据：患者只能查看自己的病历，医生只能查看自己患者的病历。
 * 接口路径：/api/medical-record
 * </p>
 */
public class MedicalRecordController {

    private final MedicalRecordService medicalRecordService;

    /**
     * 新增病历
     * <p>
     * 为患者的就诊记录创建一份病历，包含主诉、现病史、既往史、诊断意见等内容。
     * </p>
     *
     * @param medicalRecord 病历信息
     * @return 操作结果
     */
    @Operation(summary = "新增病历")
    @PostMapping
    public Result<Void> add(@RequestBody MedicalRecord medicalRecord) {
        medicalRecordService.add(medicalRecord);
        return Result.success();
    }

    /**
     * 修改病历
     * <p>
     * 根据病历ID更新指定病历的信息，如补充诊断、修改治疗方案等。
     * </p>
     *
     * @param id            病历ID
     * @param medicalRecord 更新后的病历信息
     * @return 操作结果
     */
    @Operation(summary = "修改病历")
    @PutMapping("/{id}")
    public Result<Void> update(@PathVariable Long id, @RequestBody MedicalRecord medicalRecord) {
        medicalRecord.setId(id);
        medicalRecordService.update(medicalRecord);
        return Result.success();
    }

    /**
     * 查询病历详情
     * <p>
     * 根据病历ID查询单份病历的详细信息，包括主诉、现病史、诊断等。
     * </p>
     *
     * @param id 病历ID
     * @return 指定病历的详细信息
     */
    @Operation(summary = "查询病历详情")
    @GetMapping("/{id}")
    public Result<MedicalRecord> getById(@PathVariable Long id) {
        return Result.success(medicalRecordService.getById(id));
    }

    /**
     * 根据挂号ID查询病历
     * <p>
     * 通过挂号记录的ID查找对应的病历。每条挂号记录通常对应一份病历。
     * </p>
     *
     * @param regId 挂号记录ID
     * @return 与该挂号关联的病历信息
     */
    @Operation(summary = "根据挂号查病历")
    @GetMapping("/registration/{regId}")
    public Result<MedicalRecord> getByRegistrationId(@PathVariable Long regId) {
        return Result.success(medicalRecordService.getByRegistrationId(regId));
    }

    /**
     * 分页查询病历列表
     * <p>
     * 根据挂号ID、患者ID、患者姓名等条件进行分页查询。
     * 根据角色自动过滤数据：患者角色只能查看自己的病历，医生角色只能查看自己患者的病历。
     * </p>
     *
     * @param request        HTTP请求对象，用于获取当前用户角色和ID
     * @param page           页码，默认值为1
     * @param size           每页记录数，默认值为10
     * @param registrationId 挂号ID（可选），用于按挂号筛选
     * @param patientId      患者ID（可选），用于按患者筛选
     * @param patientName    患者姓名（可选），用于模糊搜索
     * @return 分页结果，包含病历视图对象列表和总记录数
     */
    @Operation(summary = "病历列表")
    @GetMapping("/list")
    public Result<PageResult<MedicalRecordVO>> list(
            HttpServletRequest request,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) Long registrationId,
            @RequestParam(required = false) Long patientId,
            @RequestParam(required = false) String patientName) {
        // 根据不同角色自动过滤数据
        Long doctorId = null;
        String role = (String) request.getAttribute("role");
        if ("patient".equals(role)) {
            patientId = (Long) request.getAttribute("userId");
        } else if ("doctor".equals(role)) {
            doctorId = (Long) request.getAttribute("doctorId");
        }
        return Result.success(medicalRecordService.list(registrationId, patientId, doctorId, patientName, page, size));
    }
}
