# 东软云医院HIS系统 - 前端

## 技术栈

- Vue 3.4
- Element Plus 2.5
- Vite 5.x
- Vue Router 4
- Pinia 2
- Axios 1.6

## 项目结构

```
frontProject/
├── src/
│   ├── api/          # API接口封装
│   ├── assets/       # 静态资源
│   ├── layout/       # 布局组件
│   ├── router/       # 路由配置
│   ├── stores/       # 状态管理
│   ├── utils/        # 工具函数
│   └── views/        # 页面组件
│       ├── dashboard/        # 首页
│       ├── registration/     # 挂号管理
│       ├── doctor/           # 医生管理
│       ├── patient/          # 患者管理
│       ├── drug/             # 药品管理
│       ├── medical-record/   # 病历管理
│       ├── prescription/     # 处方管理
│       ├── examination/      # 检查管理
│       ├── laboratory/       # 检验管理
│       ├── treatment/        # 处置管理
│       ├── payment/          # 收费管理
│       └── department/       # 科室管理
├── index.html
├── package.json
└── vite.config.js
```

## 启动步骤

1. 安装依赖
```bash
npm install
```

2. 启动开发服务器
```bash
npm run dev
```

3. 访问 http://localhost:3000

## 构建生产版本

```bash
npm run build
```

## 后端接口

前端默认代理 `/api` 请求到 `http://localhost:8080`，请确保后端服务已启动。
