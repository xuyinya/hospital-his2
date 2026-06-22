# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## 项目概述

**东软云医院系统 (HIS)** — 基于 B/S 架构的医院门诊信息管理系统，实现挂号、医生、患者、病历、处方、药品、检查、检验、处置、收费等门诊业务全流程信息化管理。

## 技术栈

| 层次 | 技术 | 版本 |
|------|------|------|
| 前端框架 | Vue 3 | 3.4.21 |
| UI 组件库 | Element Plus | 2.5.6 |
| 构建工具 | Vite | 5.x |
| 路由 | Vue Router 4 | 4.3.0 |
| 状态管理 | Pinia | 2.1.7 |
| HTTP 客户端 | Axios | 1.6.7 |
| 后端框架 | Spring Boot | 3.2.5 |
| ORM | MyBatis | 3.0.3 |
| 数据库 | MySQL | 8.0 |
| JDK | Java | 21 |
| 连接池 | Druid | 1.2.21 |
| API 文档 | Knife4j | 4.4.0 |
| 认证 | JWT (jjwt) | 0.12.5 |
| 密码加密 | BCrypt (Spring Security Crypto) | 随 Boot 3.2.5 |

## 项目结构

```
engineeringProject/
├── backend/                          # Spring Boot 后端
│   ├── src/main/java/com/neusoft/hospital/
│   │   ├── HospitalApplication.java  # 启动类(排除SecurityAutoConfiguration)
│   │   ├── common/                   # Result + PageResult
│   │   ├── config/                   # JwtConfig, WebMvcConfig, Knife4jConfig, PasswordEncoderConfig, DataInitializer
│   │   ├── security/                 # JwtUtil, JwtInterceptor
│   │   ├── controller/               # 10个Controller + AuthController
│   │   ├── service/                  # Service接口 + impl/实现类
│   │   ├── mapper/                   # MyBatis Mapper (含 SysUserMapper)
│   │   ├── entity/                   # 13个实体 (含 SysUser)
│   │   ├── entity/vo/               # 8个VO
│   │   ├── dto/                     # LoginRequest, LoginResponse
│   │   └── exception/               # GlobalExceptionHandler
│   ├── src/main/resources/
│   │   ├── application.yml           # 数据库、MyBatis、Knife4j、JWT配置
│   │   └── mapper/                   # 11个XML映射文件
│   ├── hospital_his.sql             # 12张业务表 + sys_user
│   └── pom.xml
├── frontProject/                     # Vue 3 前端
│   ├── src/
│   │   ├── api/                      # 12个API接口模块
│   │   ├── assets/styles/           # global.scss (全局样式)
│   │   ├── layout/                  # MainLayout + AppHeader + AppSidebar
│   │   ├── router/                  # Vue Router (12条子路由)
│   │   ├── utils/                   # request.js (Axios封装) + format.js (工具函数)
│   │   └── views/                   # 12个页面组件
│   ├── index.html
│   ├── package.json
│   └── vite.config.js               # 端口3000, 代理/api到8080
```

## 数据库 (MySQL 8.0)

数据库名 `hospital_his`，共 12 张核心表：

| 表名 | 说明 | 关联 |
|------|------|------|
| patient | 患者 | — |
| department | 科室 | — |
| doctor | 医生 | → department.id |
| registration | 挂号 | → patient/doctor/department |
| medical_record | 病历 | → registration/patient/doctor |
| prescription | 处方 | → registration/patient/doctor |
| prescription_detail | 处方明细 | → prescription/drug |
| drug | 药品 | — |
| examination | 检查 | → registration/patient |
| laboratory | 检验 | → registration/patient |
| treatment | 处置 | → registration/patient |
| payment | 收费 | → registration/patient |

所有外键关系在 SQL 中已定义，初始化脚本包含：8个科室、8名医生、8种药品、5位患者、5条挂号、2份病历、1条处方及明细。

## 启动开发环境

### 后端
```bash
# 1. 创建数据库并导入
mysql -u root -p < backend/hospital_his.sql

# 2. 修改数据库密码 (backend/src/main/resources/application.yml)
# spring.datasource.password = 你的密码

# 3. 编译运行
cd backend
mvn clean package -DskipTests
java -jar target/hospital-his-1.0.0.jar
# 启动后访问: http://localhost:8080/doc.html (Knife4j接口文档)
```

### 前端
```bash
cd frontProject
npm install
npm run dev
# 访问: http://localhost:3000
```

## API 架构规范

- **基础路径**: 所有接口以 `/api` 开头
- **统一返回格式**: `Result<T>` 包含 `code(200/500)`, `message`, `data`
- **分页返回格式**: `PageResult<T>` 包含 `total`, `rows`
- **异常处理**: `GlobalExceptionHandler` 统一捕获 `RuntimeException` 和 `Exception`
- **接口文档**: Knife4j 整合 Swagger，启动后访问 `/doc.html`

### Controller 层通用模式
```java
@Tag(name = "XX管理")
@RestController
@RequestMapping("/api/xxx")
@RequiredArgsConstructor
public class XxxController {
    private final XxxService xxxService;

    @PostMapping                    // 新增
    @PutMapping("/{id}")            // 修改
    @DeleteMapping("/{id}")        // 删除
    @GetMapping("/{id}")           // 详情
    @GetMapping("/list")           // 分页列表
    @PutMapping("/{id}/status")    // 更新状态
}
```

### 前端 Axios 封装规范 (`src/utils/request.js`)
- 基础路径: `/api`
- 响应拦截: code === 200 才成功返回，否则弹 ElMessage 错误
- 请求拦截: 预留（当前无认证 token 处理）

## 业务模块导航

| 路由路径 | 页面 | Controller 路径 |
|---------|------|----------------|
| /dashboard | 首页（含统计卡片和快捷入口） | — |
| /registration | 挂号管理（CRUD + 状态:待诊/已诊/取消） | /api/registration |
| /doctor | 医生管理（查询列表，不可编辑） | /api/doctor |
| /patient | 患者管理（CRUD） | /api/patient |
| /drug | 药品管理（CRUD） | /api/drug |
| /medical-record | 病历管理（新增/查看/编辑） | /api/medical-record |
| /prescription | 处方管理（查看明细+确认取药） | /api/prescription |
| /examination | 检查管理（CRUD+录入结果） | /api/examination |
| /laboratory | 检验管理（CRUD+录入结果） | /api/laboratory |
| /treatment | 处置管理（CRUD+完成状态） | /api/treatment |
| /payment | 收费管理（新增+确认支付） | /api/payment |
| /department | 科室管理（只读列表） | /api/department |

## 登录认证系统 (新增)

- **后端**: JWT (jjwt 0.12.5) + BCrypt 密码加密
- **登录接口**: `POST /api/auth/login` — 返回 token + 用户信息
- **拦截器**: `JwtInterceptor` 拦截 `/api/**`, 放行 `/api/auth/login` 和 Swagger 路径
- **默认账户**: admin / admin123 (系统管理员, 启动时自动创建)
- **前端**: Vue Router `beforeEach` 导航守卫, 未登录重定向到 `/login`
- **Axios**: 请求拦截器自动添加 `Authorization: Bearer xxx`, 响应拦截器 401 自动跳转登录

## 常见开发任务

### 添加新的业务模块
1. 数据库建表 → 写 SQL 插入 `hospital_his.sql`
2. 后端: Entity → Mapper (注解+XML) → Service (接口+Impl) → Controller
3. 前端: API 接口模块 → Views 页面 → Router 注册路由 → Sidebar 添加菜单项

### 新增 API 接口
- 简单 CRUD: 在 Mapper 中用 MyBatis 注解（`@Insert`/`@Update`/`@Select`/`@Delete`）
- 复杂连表查询: 在 XML 中写 `<select>`，返回 VO 对象
- 统一在 Controller 中加上 `@Operation(summary = "...")` Swagger 注解

### 启动 Maven 编译
```bash
cd backend
mvn clean package -DskipTests
mvn clean package  # 包含测试
mvn test          # 仅运行测试
```

### 前端构建
```bash
cd frontProject
npm run build     # 生产构建，输出到 dist/
npm run preview   # 预览构建结果
```
