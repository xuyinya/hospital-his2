# 东软云医院 HIS 系统 🏥

基于 B/S 架构的医院门诊信息管理系统，实现患者从挂号到收费的全流程信息化管理。

## ✨ 功能模块

| 模块 | 说明 |
|------|------|
| 📋 挂号管理 | 患者挂号、状态流转（待诊/已诊/取消） |
| 👨‍⚕️ 医生管理 | 医生信息查询（按科室筛选） |
| 🧑 患者管理 | 患者信息 CRUD |
| 💊 药品管理 | 药品信息 CRUD + 库存管理 |
| 📝 病历管理 | 电子病历新增/编辑/查看 |
| 📄 处方管理 | 处方查看 + 明细 + 确认取药 |
| 🔬 检查管理 | 检查开单 + 结果录入 |
| 🧪 检验管理 | 检验开单 + 结果录入（含参考范围） |
| 🩹 处置管理 | 处置开单 + 完成状态 |
| 💳 收费管理 | 收费录入 + 支付确认 |
| 🏢 科室管理 | 科室列表查询（只读） |
| 📊 首页仪表盘 | 今日挂号/就诊/待诊/收入统计 |
| 🔐 登录认证 | JWT 双通道登录（员工/患者） |

## 🧑‍💻 患者门户

患者凭身份证号 + 密码登录，可查看：
- 🏠 个人首页 — 就诊概览
- 📋 我的挂号记录
- 📝 我的病历（含详情弹窗）
- 📄 我的处方（含明细弹窗）

## 🛠️ 技术栈

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

## 🚀 快速启动

### 前置要求
- JDK 21
- Maven 3.9+
- MySQL 8.0
- Node.js 18+

### 1. 数据库初始化
```bash
cd backend
mysql -u root -p < hospital_his.sql
```

### 2. 启动后端
```bash
cd backend
mvn clean package -DskipTests
java -jar target/hospital-his-1.0.0.jar
```
启动后访问：http://localhost:8080/doc.html（Knife4j 接口文档）

### 3. 启动前端
```bash
cd frontProject
npm install
npm run dev
```
访问：http://localhost:3000

## 🔑 默认账户

| 角色 | 用户名 | 密码 |
|------|--------|------|
| 系统管理员 | admin | admin123 |
| 医生 | doctor | 123456 |
| 医生 | doctor2 | 123456 |
| 患者 | 身份证号 | 123456 |

## 📁 项目结构

```
engineeringProject/
├── backend/               # Spring Boot 后端
│   ├── src/main/java/com/neusoft/hospital/
│   │   ├── common/        # 统一返回格式
│   │   ├── config/        # 配置类
│   │   ├── security/      # JWT 认证
│   │   ├── controller/    # 13 个接口控制器
│   │   ├── service/       # 业务逻辑层
│   │   ├── mapper/        # MyBatis 数据访问
│   │   ├── entity/        # 实体类
│   │   ├── dto/           # 数据传输对象
│   │   └── exception/     # 全局异常处理
│   ├── src/main/resources/ # 配置文件 + Mapper XML
│   └── hospital_his.sql  # 数据库脚本
├── frontProject/          # Vue 3 前端
│   ├── src/
│   │   ├── api/           # API 接口封装
│   │   ├── layout/        # 布局组件
│   │   ├── router/        # 路由配置
│   │   ├── stores/        # 状态管理
│   │   ├── utils/         # 工具函数
│   │   └── views/         # 15 个页面组件
│   └── package.json
├── CLAUDE.md              # 完整开发指南
└── README.md              # 项目说明
```

## 📊 数据库

数据库 `hospital_his`，共 12 张业务表 + 1 张用户表，包含完整的初始化数据（8 个科室、8 名医生、8 种药品、5 位患者等）。

## 📖 API 文档

启动后端后访问：http://localhost:8080/doc.html（Knife4j + Swagger）
