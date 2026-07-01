package com.neusoft.hospital.controller;

import com.neusoft.hospital.common.Result;
import com.neusoft.hospital.entity.SysUser;
import com.neusoft.hospital.service.SysUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 系统用户管理控制器
 * <p>
 * 提供系统用户（管理员/医生）的增删查功能。
 * 仅管理员可访问，用于管理可登录系统的用户账户。
 * 接口路径：/api/sys-user
 * </p>
 */
@Tag(name = "系统用户管理")
@RestController
@RequestMapping("/api/sys-user")
@RequiredArgsConstructor
public class SysUserController {

    private final SysUserService sysUserService;
    /** BCrypt密码编码器，用于对新增用户的密码进行加密存储 */
    private final BCryptPasswordEncoder passwordEncoder;

    /**
     * 获取所有系统用户列表
     * <p>返回 sys_user 表中所有用户记录，用于管理员查看和管理系统账户。</p>
     *
     * @return 系统用户列表
     */
    @Operation(summary = "管理员用户列表")
    @GetMapping("/list")
    public Result<List<SysUser>> list(@RequestParam(required = false) String role, HttpServletRequest request) {
        if (!"admin".equals(request.getAttribute("role"))) return Result.error("仅管理员可访问");
        return Result.success(sysUserService.listByRole(role));
    }

    /**
     * 新增系统用户
     * <p>
     * 管理员可在个人主页创建新的管理员或医生账户。
     * 密码通过BCrypt加密后存储，默认状态为启用(1)。
     * 医生角色需要指定关联的doctor_id。
     * </p>
     *
     * @param user 用户信息（username, password明文, realName, role, doctorId可选）
     * @return 操作结果
     */
    @Operation(summary = "新增用户")
    @PostMapping
    public Result<Void> add(@RequestBody SysUser user, HttpServletRequest request) {
        if (!"admin".equals(request.getAttribute("role"))) return Result.error("仅管理员可操作");
        // BCrypt加密密码，确保存储安全
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        if (user.getStatus() == null) user.setStatus(1);
        sysUserService.add(user);
        return Result.success();
    }

    /**
     * 删除系统用户
     * <p>根据用户ID删除指定账户。管理员不能删除自己的账户。</p>
     *
     * @param id 用户ID
     * @return 操作结果
     */
    @Operation(summary = "更新用户状态")
    @PutMapping("/{id}/status")
    public Result<Void> updateStatus(@PathVariable Long id, @RequestParam Integer status, HttpServletRequest request) {
        if (!"admin".equals(request.getAttribute("role"))) return Result.error("仅管理员可操作");
        sysUserService.updateStatus(id, status);
        return Result.success();
    }

    @Operation(summary = "删除用户")
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id, HttpServletRequest request) {
        if (!"admin".equals(request.getAttribute("role"))) return Result.error("仅管理员可操作");
        sysUserService.delete(id);
        return Result.success();
    }
}
