# hospital-his-ppt - Design Spec

## I. Project Information

| Item | Value |
| ---- | ----- |
| **Project Name** | 东软云医院系统（HIS）答辩演示 |
| **Canvas Format** | PPT 16:9 (1280×720) |
| **Page Count** | 25 |
| **Design Style** | clean-professional 学术答辩风格 |
| **Target Audience** | 答辩委员会（教师 + 同学） |
| **Use Case** | 毕业设计/实训项目答辩 |
| **Delivery Purpose** | presentation（演示投屏，信息密度低，每页重点突出） |
| **Content Strategy** | 忠实还原源文档结构，适度精简每页文字量，重点穿插关键代码讲解 |
| **Created Date** | 2026-07-01 |

---

## II. Canvas Specification

| Property | Value |
| -------- | ----- |
| **Format** | PPT 16:9 |
| **Dimensions** | 1280×720 |
| **viewBox** | `0 0 1280 720` |
| **Margins** | left/right 80px, top/bottom 60px |
| **Content Area** | 1120×600 |

---

## III. Visual Theme

### Theme Style
- **Mode**: pyramid（金字塔结构：背景→设计→实现→测试→总结）
- **Visual style**: clean-professional
- **Theme**: Light theme
- **Tone**: 专业、科技、学术，医疗信息化行业氛围

### Color Scheme

| Role | HEX | Purpose |
| ---- | --- | ------- |
| **Background** | `#FFFFFF` | 页面背景 |
| **Secondary bg** | `#F0F4FF` | 卡片背景、代码块背景 |
| **Primary** | `#2563EB` | 标题装饰、关键区域、导航元素 |
| **Accent** | `#06B6D4` | 数据高亮、链接、重点信息 |
| **Secondary accent** | `#3B82F6` | 渐变过渡、次要强调 |
| **Body text** | `#1E293B` | 主要正文文字 |
| **Secondary text** | `#64748B` | 说明文字、注释 |
| **Tertiary text** | `#94A3B8` | 页脚、辅助信息 |
| **Border/divider** | `#E2E8F0` | 卡片边框、分割线 |
| **Success** | `#10B981` | 通过/成功标识 |
| **Warning** | `#EF4444` | 缺陷/错误标识 |

### Gradient Scheme

```xml
<linearGradient id="titleGradient" x1="0%" y1="0%" x2="100%" y2="100%">
  <stop offset="0%" stop-color="#2563EB"/>
  <stop offset="100%" stop-color="#06B6D4"/>
</linearGradient>
```

---

## IV. Typography System

### Font Plan
微软雅黑全系，PPT 安全字体，无需额外安装。标题加粗突出，正文常规。
Presentation 模式：正文基线 32px。

### Per-role font stacks
- **font_family**: `"Microsoft YaHei", Arial, sans-serif`
- **title_family**: `"Microsoft YaHei", Arial, sans-serif`
- **body_family**: `"Microsoft YaHei", sans-serif`
- **code_family**: `Consolas, "Courier New", monospace`

### Size Ramp (px)
| Role | Size |
|------|------|
| cover_title | 64 |
| title | 42 |
| subtitle | 32 |
| body | 32 |
| lead | 36 |
| annotation | 22 |
| footnote | 18 |
| code | 20 |
| chapter_number | 72 |

---

## V. Icon Strategy
- **Library**: tabler-outline（线条风格，类 Lucide）
- **Stroke width**: 1.5px
- Approved icons: home, users, stethoscope, clipboard, pill, file-text, activity, credit-card, building, settings, shield, database, server, code, check-circle, alert-triangle, search, edit, trash, plus, arrow-right, monitor, cpu

---

## VI. Page Roster
All 25 pages are free design (no template inheritance). Visual consistency via spec_lock colors/fonts/rhythm.

---

## VII. Visualization Reference List
No data charts. Architecture diagrams and flowcharts will be hand-drawn as SVG shapes using spec colors. All `no-template-match`.

---

## VIII. Image Resource List
No external images. All visual content is text + icons + hand-drawn diagrams. Four pages contain **image placeholder** areas (empty dashed-border zones with label "此处插入图片") for the user to add screenshots later:

| Page | Placeholder | Size |
|------|------------|------|
| P09 | 管理员页面截图 | 480×300 |
| P14 | 接诊工作台截图 | 480×300 |
| P17 | 患者门户截图 | 480×300 |
| P24 | 演示效果截图 | 960×400 |

---

## IX. Content Outline

| # | Title | Rhythm | Role | Content Summary |
|---|-------|--------|------|----------------|
| P01 | 封面 | anchor | — | 东软云医院系统（HIS）— 毕业设计答辩 · 作者 · 日期 |
| P02 | 目录 | anchor | — | 全文导航：背景→需求→设计→实现→测试→总结 |
| P03 | 项目背景 | breathing | — | 医疗信息化行业背景 + 项目定位 |
| P04 | 技术栈总览 | dense | — | 前后端技术栈全景图（Vue3+S.Boot+MySQL） |
| P05 | 需求分析-功能 | dense | — | 12大功能模块 + 3角色用例 |
| P06 | 需求分析-非功能 | dense | — | 安全/性能/可用性/JWT/MD5/事务 |
| P07 | 系统架构设计 | breathing | — | 前后端分离四层架构图 |
| P08 | 数据库设计 | dense | — | 12表 ER 关系 + 关键字段说明 |
| P09 | 管理员-系统管理 | dense | admin | 挂号/患者/药品/医生 CRUD + 截图占位 |
| P10 | 管理员-代码讲解 | dense | admin | `@Transactional` 保证数据一致性 + 代码块 |
| P11 | 管理员-核心代码 | dense | admin | `RegistrationController.selfRegistration` 费用防篡改 |
| P12 | 医生-接诊工作台 | dense | doctor | 患者列表→接诊→病历→处方→完成诊疗 |
| P13 | 医生-代码讲解 | dense | doctor | 处方创建+明细添加+总金额自动计算 |
| P14 | 医生-接诊界面 | breathing | doctor | 接诊工作台截图占位 + 交互说明 |
| P15 | 患者-自助挂号 | dense | patient | 5步向导：选科→选医生→类型→症状→确认 |
| P16 | 患者-门户功能 | dense | patient | 我的挂号/病历/处方 + JWT 患者身份识别 |
| P17 | 患者-界面展示 | breathing | patient | 患者门户截图占位（自助挂号+我的处方） |
| P18 | JWT认证机制 | dense | cross | Token 生成→拦截→解析→角色控制全流程 |
| P19 | 关键安全设计 | dense | cross | 费用后端计算 + 密码BCrypt + 角色路由守卫 |
| P20 | 系统测试-策略 | dense | — | 4模块30用例 + Mockito单元测试策略 |
| P21 | 系统测试-用例 | dense | — | JWT/患者/药品/挂号 测试用例精选 |
| P22 | 系统测试-结果 | breathing | — | 30/30 全部通过 + 4个缺陷发现与修复 |
| P23 | 部署与运维 | dense | — | 环境配置 + Maven构建 + 前后端部署步骤 |
| P24 | 系统效果展示 | breathing | — | 核心页面效果截图占位（大图） |
| P25 | 总结与展望 | anchor | — | 实训收获 + 不足 + 改进方向 + 致谢 |

---

## X. Speaker Notes Strategy
Per-page speaker notes in Chinese, matching defense presentation tone. Cover page: silence. Content pages: 2-4 sentence narration. Always open with page topic, then explain the key point. Code pages: explain the code logic in plain language.

---

## XI. Transitions
Default `fade` transition on all pages. No per-element entrance animation.
