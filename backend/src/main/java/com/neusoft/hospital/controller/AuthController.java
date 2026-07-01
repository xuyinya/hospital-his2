package com.neusoft.hospital.controller;

import com.neusoft.hospital.common.Result;
import com.neusoft.hospital.dto.LoginRequest;
import com.neusoft.hospital.dto.LoginResponse;
import com.neusoft.hospital.dto.PatientLoginRequest;
import com.neusoft.hospital.entity.Patient;
import com.neusoft.hospital.entity.SysUser;
import com.neusoft.hospital.security.JwtUtil;
import com.neusoft.hospital.service.PatientService;
import com.neusoft.hospital.service.SysUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Tag(name = "认证管理")
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
/**
 * 认证控制器
 * <p>
 * 提供系统登录认证功能，包括员工登录、患者登录以及获取当前登录用户信息。
 * 接口路径：/api/auth
 * </p>
 */
public class AuthController {

    private final SysUserService sysUserService;
    private final PatientService patientService;
    private final JwtUtil jwtUtil;
    private final BCryptPasswordEncoder passwordEncoder;

    /**
     * 员工登录
     * <p>
     * 根据用户名和密码进行员工登录认证。验证用户是否存在、账户是否被禁用、密码是否正确。
     * 认证成功后生成JWT令牌并返回登录响应信息。
     * </p>
     *
     * @param loginRequest 登录请求体，包含用户名和密码
     * @return 登录响应结果，包含JWT令牌、用户名、真实姓名和角色信息；认证失败返回错误信息
     */
    @Operation(summary = "员工登录")
    @PostMapping("/login")
    public Result<LoginResponse> login(@RequestBody LoginRequest loginRequest) {
        SysUser user = sysUserService.getByUsername(loginRequest.getUsername());
        if (user == null) {
            return Result.error("用户名或密码错误");
        }
        if (user.getStatus() != null && user.getStatus() == 0) {
            return Result.error("账户已被禁用");
        }
        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            return Result.error("用户名或密码错误");
        }

        String token = jwtUtil.generateToken(user.getId(), user.getUsername(), user.getRole(), user.getDoctorId());
        LoginResponse response = new LoginResponse(token, user.getUsername(), user.getRealName(), user.getRole());
        return Result.success(response);
    }

    /**
     * 患者登录
     * <p>
     * 根据身份证号和密码进行患者登录认证。验证患者是否存在、密码是否正确。
     * 认证成功后生成JWT令牌并返回登录响应信息。
     * </p>
     *
     * @param request 患者登录请求体，包含身份证号和密码
     * @return 登录响应结果，包含JWT令牌、患者姓名和角色信息；认证失败返回错误信息
     */
    @Operation(summary = "患者登录")
    @PostMapping("/patient-login")
    public Result<LoginResponse> patientLogin(@RequestBody PatientLoginRequest request) {
        Patient patient = patientService.getByIdCard(request.getIdCard());
        if (patient == null) {
            return Result.error("身份证号未注册");
        }
        if (patient.getPassword() == null || !passwordEncoder.matches(request.getPassword(), patient.getPassword())) {
            return Result.error("密码错误");
        }

        String token = jwtUtil.generateToken(patient.getId(), patient.getPatientName(), "patient");
        LoginResponse response = new LoginResponse(token, patient.getPatientName(), patient.getPatientName(), "patient");
        response.setIdCard(patient.getIdCard());
        response.setPhone(patient.getPhone());
        return Result.success(response);
    }

    @Operation(summary = "患者注册")
    @PostMapping("/patient-register")
    public Result<LoginResponse> patientRegister(@RequestBody Patient patient) {
        String idCard = patient.getIdCard();
        if (idCard == null || idCard.length() != 18) {
            return Result.error("身份证号应为18位");
        }
        Patient exist = patientService.getByIdCard(idCard);
        if (exist != null) {
            return Result.error("该身份证号已注册，请直接登录");
        }
        if (patient.getPassword() == null || patient.getPassword().length() < 4) {
            return Result.error("密码至少4位");
        }
        patient.setPassword(passwordEncoder.encode(patient.getPassword()));
        patientService.add(patient);
        // 重新查一次获取自增ID
        Patient created = patientService.getByIdCard(idCard);
        String token = jwtUtil.generateToken(created.getId(), created.getPatientName(), "patient");
        LoginResponse response = new LoginResponse(token, created.getPatientName(), created.getPatientName(), "patient");
        response.setIdCard(created.getIdCard());
        response.setPhone(created.getPhone());
        return Result.success(response);
    }

    /**
     * 获取当前用户信息
     * <p>
     * 根据请求中携带的JWT令牌解析出的用户信息，返回当前登录用户的详细资料。
     * 如果是患者角色，返回患者姓名、身份证号、手机号等信息；
     * 如果是员工角色，返回员工用户名、真实姓名和角色信息。
     * </p>
     *
     * @param request HTTP请求对象，从中获取用户ID和角色等认证属性
     * @return 当前用户的信息Map，包含用户名、真实姓名、角色等；用户不存在时返回错误信息
     */
    @Operation(summary = "获取当前用户信息")
    @GetMapping("/info")
    public Result<Map<String, Object>> info(HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        String role = (String) request.getAttribute("role");

        Map<String, Object> map = new HashMap<>();
        if ("patient".equals(role)) {
            Patient patient = patientService.getById(userId);
            if (patient == null) {
                return Result.error("患者不存在");
            }
            map.put("username", patient.getPatientName());
            map.put("realName", patient.getPatientName());
            map.put("role", "patient");
            map.put("patientId", patient.getId());
            map.put("idCard", patient.getIdCard());
            map.put("phone", patient.getPhone());
        } else {
            SysUser user = sysUserService.getById(userId);
            if (user == null) {
                return Result.error("用户不存在");
            }
            map.put("username", user.getUsername());
            map.put("realName", user.getRealName());
            map.put("role", user.getRole());
        }
        return Result.success(map);
    }

    @Operation(summary = "修改密码")
    @PutMapping("/password")
    public Result<Void> updatePassword(@RequestBody Map<String, String> body, HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        String role = (String) request.getAttribute("role");
        String oldPwd = body.get("oldPassword");
        String newPwd = body.get("newPassword");
        if (newPwd == null || newPwd.length() < 4) {
            return Result.error("新密码至少4位");
        }
        if ("patient".equals(role)) {
            Patient p = patientService.getById(userId);
            if (p == null || !passwordEncoder.matches(oldPwd, p.getPassword())) {
                return Result.error("原密码错误");
            }
            patientService.updatePassword(userId, passwordEncoder.encode(newPwd));
        } else {
            SysUser user = sysUserService.getById(userId);
            if (user == null || !passwordEncoder.matches(oldPwd, user.getPassword())) {
                return Result.error("原密码错误");
            }
            sysUserService.updatePassword(userId, passwordEncoder.encode(newPwd));
        }
        return Result.success();
    }
}
