package com.neusoft.hospital.controller;

import com.neusoft.hospital.common.PageResult;
import com.neusoft.hospital.common.Result;
import com.neusoft.hospital.entity.Registration;
import com.neusoft.hospital.service.RegistrationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@Tag(name = "挂号管理")
@RestController
@RequestMapping("/api/registration")
@RequiredArgsConstructor
/**
 * 挂号管理控制器
 * <p>
 * 提供挂号记录的增删改查功能，支持取消挂号和更新挂号状态（待诊/已诊/取消）。
 * 根据角色自动过滤数据：患者只能查看自己的挂号，医生只能查看挂自己号的患者。
 * 接口路径：/api/registration
 * </p>
 */
public class RegistrationController {

    private final RegistrationService registrationService;

    /**
     * 新增挂号
     * <p>
     * 为患者创建一条挂号记录，包括选择的科室、医生、就诊时间等信息。
     * 挂号创建后状态默认为待诊（0）。
     * </p>
     *
     * @param registration 挂号信息
     * @return 操作结果
     */
    @Operation(summary = "新增挂号")
    @PostMapping
    public Result<Void> add(@RequestBody Registration registration) {
        registrationService.add(registration);
        return Result.success();
    }

    /**
     * 患者自助挂号
     * <p>
     * 患者端自行选择科室、医生和挂号类型完成挂号。
     * 患者ID从JWT中自动获取，挂号费用根据挂号类型自动设置（普通号25元，专家号50元）。
     * </p>
     *
     * @param registration 挂号信息（只需传 doctorId, deptId, regType）
     * @param request      HTTP请求对象，用于从JWT中提取患者ID
     * @return 操作结果
     */
    @Operation(summary = "患者自助挂号")
    @PostMapping("/self")
    public Result<Void> selfRegistration(@RequestBody Registration registration, HttpServletRequest request) {
        // 从JWT中获取患者ID（患者登录时 userId 即为 patient.id）
        Long patientId = (Long) request.getAttribute("userId");
        registration.setPatientId(patientId);
        // 根据挂号类型自动设置费用（防止客户端篡改）
        if ("专家".equals(registration.getRegType())) {
            registration.setRegFee(new BigDecimal("50.00"));
        } else {
            registration.setRegFee(new BigDecimal("25.00"));
        }
        registrationService.add(registration);
        return Result.success();
    }

    /**
     * 修改挂号信息
     * <p>
     * 根据挂号ID更新指定挂号记录的信息，如调整就诊医生、就诊时间等。
     * </p>
     *
     * @param id           挂号ID
     * @param registration 更新后的挂号信息
     * @return 操作结果
     */
    @Operation(summary = "修改挂号")
    @PutMapping("/{id}")
    public Result<Void> update(@PathVariable Long id, @RequestBody Registration registration) {
        registration.setId(id);
        registrationService.update(registration);
        return Result.success();
    }

    /**
     * 查询挂号详情
     * <p>
     * 根据挂号ID查询单条挂号记录的详细信息，包括患者信息、科室、医生、就诊时间等。
     * </p>
     *
     * @param id 挂号ID
     * @return 指定挂号记录的详细信息
     */
    @Operation(summary = "查询挂号详情")
    @GetMapping("/{id}")
    public Result<Registration> getById(@PathVariable Long id) {
        return Result.success(registrationService.getById(id));
    }

    /**
     * 分页查询挂号列表
     * <p>
     * 根据状态、患者姓名、科室ID等条件进行分页查询。
     * 根据角色自动过滤数据：患者角色只能查看自己的挂号，医生角色只能查看挂自己号的患者。
     * </p>
     *
     * @param request     HTTP请求对象，用于获取当前用户角色和ID
     * @param page        页码，默认值为1
     * @param size        每页记录数，默认值为10
     * @param status      挂号状态（可选），0-待诊，1-已诊，2-取消
     * @param patientName 患者姓名（可选），用于模糊搜索
     * @param deptId      科室ID（可选），用于按科室筛选
     * @return 分页结果，包含挂号视图对象列表和总记录数
     */
    @Operation(summary = "挂号列表")
    @GetMapping("/list")
    public Result<PageResult<Registration>> list(
            HttpServletRequest request,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) Long patientId,
            @RequestParam(required = false) Long doctorId,
            @RequestParam(required = false) Integer status,
            @RequestParam(required = false) String patientName,
            @RequestParam(required = false) Long deptId) {
        // 角色过滤：患者/医生强制只看自己的，管理员按请求参数过滤
        String role = (String) request.getAttribute("role");
        if ("patient".equals(role)) {
            patientId = (Long) request.getAttribute("userId");
        } else if ("doctor".equals(role)) {
            doctorId = (Long) request.getAttribute("doctorId");
        }
        return Result.success(registrationService.list(patientId, doctorId, status, patientName, deptId, page, size));
    }

    /**
     * 取消挂号
     * <p>
     * 根据挂号ID取消指定挂号记录。取消操作将挂号状态更新为已取消（2），
     * 而非物理删除数据。
     * </p>
     *
     * @param id 挂号ID
     * @return 操作结果
     */
    @Operation(summary = "取消挂号")
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        registrationService.delete(id);
        return Result.success();
    }

    /**
     * 更新挂号状态
     * <p>
     * 更新指定挂号记录的状态，如将待诊（0）更新为已诊（1），
     * 用于标记患者已完成就诊。
     * </p>
     *
     * @param id     挂号ID
     * @param status 新的挂号状态，0-待诊，1-已诊，2-取消
     * @return 操作结果
     */
    @Operation(summary = "更新挂号状态")
    @PutMapping("/{id}/status")
    public Result<Void> updateStatus(@PathVariable Long id, @RequestParam Integer status) {
        registrationService.updateStatus(id, status);
        return Result.success();
    }
}
