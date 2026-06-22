# 后续待办清单

## 当前状态（2026-06-22 更新）
- 后端可以启动（需先执行 `ALTER TABLE patient ADD COLUMN password VARCHAR(255);`）
- 前端可以启动（`npm run dev`）
- JWT 登录认证已实现
- 三种角色登录体系已实现（admin/doctor/patient）
- 患者门户页面已创建
- 医生管理分页查询已修复
- **Modernize 白色主题 UI 美化已完成：**
  - `global.scss` — Modernize 风格 CSS 变量（`#5d87ff` 主色）
  - `AppSidebar.vue` — 白色侧栏，蓝色选中态
  - `MainLayout.vue` — 白色侧栏背景 + `#f5f6fa` 主背景
  - `AppHeader.vue` — 清爽顶部栏
  - `PatientLayout.vue` — 白色主题患者门户布局
  - `views/dashboard/index.vue` — 圆形彩色图标统计卡片
  - `views/login/index.vue` — Modernize 风格微调
  - 患者门户 4 个页面 — 白色卡片风格
  - 所有 11 个业务页面 — 通过 `global.scss` 全局样式统一
  - 字体: Plus Jakarta Sans 引入

## 如有后续需要
1. 启动前先执行 `ALTER TABLE patient ADD COLUMN password VARCHAR(255) DEFAULT NULL COMMENT '登录密码';`
2. 如需要动态渐变背景或动画效果可以继续优化
3. 患者门户可以增加更多交互（在线问诊、健康档案等）
4. 页面语言可以国际化（i18n）
