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
public class AuthController {

    private final SysUserService sysUserService;
    private final PatientService patientService;
    private final JwtUtil jwtUtil;
    private final BCryptPasswordEncoder passwordEncoder;

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
        return Result.success(response);
    }

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
}
