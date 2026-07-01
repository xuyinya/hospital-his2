<template>
  <!-- 侧边栏导航组件：展示系统Logo和左侧菜单列表 -->
  <div class="sidebar">
    <div class="sidebar-header">
      <!-- 系统Logo区域 -->
      <div class="logo">
        <svg width="36" height="36" viewBox="0 0 48 48" fill="none">
          <rect width="48" height="48" rx="12" fill="#5d87ff"/>
          <path d="M24 12L24 36M12 24L36 24" stroke="white" stroke-width="4" stroke-linecap="round"/>
        </svg>
        <!-- 折叠时隐藏文字 -->
        <span v-show="!isCollapse" class="logo-text">东软云医院</span>
      </div>
    </div>

    <div class="menu-wrapper">
      <!-- 导航菜单：根据用户角色动态过滤菜单项 -->
      <el-menu
        :default-active="currentRoute"
        :collapse="isCollapse"
        :collapse-transition="false"
        router
      >
        <el-menu-item v-for="item in menuItems" :key="item.path" :index="item.path">
          <el-icon><component :is="item.icon" /></el-icon>
          <template #title>{{ item.title }}</template>
        </el-menu-item>
      </el-menu>
    </div>
  </div>
</template>

<script setup>
/**
 * AppSidebar - 系统侧边栏导航组件
 * 功能：展示系统Logo及根据用户角色过滤的导航菜单，支持折叠/展开
 */
import { computed } from 'vue'
import { useRoute } from 'vue-router'
import { useAuthStore } from '@/stores/auth'

// 接收父组件传入的折叠状态
defineProps({ isCollapse: { type: Boolean, default: false } })

const route = useRoute()
const authStore = useAuthStore()
/** 当前路由路径，用于高亮菜单项 */
const currentRoute = computed(() => route.path)

/**
 * 完整菜单配置列表
 * 每个菜单项包含：路径、标题、图标、可访问角色列表
 */
const allMenus = [
  { path: '/dashboard', title: '首页', icon: 'HomeFilled', roles: ['admin', 'doctor'] },
  { path: '/profile', title: '个人主页', icon: 'User', roles: ['admin', 'doctor'] },
  { path: '/my-clinic', title: '接诊管理', icon: 'List', roles: ['doctor'] },
  { path: '/registration', title: '挂号管理', icon: 'Tickets', roles: ['admin'] },
  { path: '/doctor', title: '医生管理', icon: 'User', roles: ['admin'] },
  { path: '/patient', title: '患者管理', icon: 'UserFilled', roles: ['admin', 'doctor'] },
  { path: '/drug', title: '药品管理', icon: 'FirstAidKit', roles: ['admin'] },
  { path: '/medical-record', title: '病历管理', icon: 'Notebook', roles: ['admin', 'doctor'] },
  { path: '/prescription', title: '处方管理', icon: 'Document', roles: ['admin', 'doctor'] },
  { path: '/examination', title: '检查管理', icon: 'Monitor', roles: ['admin', 'doctor'] },
  { path: '/laboratory', title: '检验管理', icon: 'DataAnalysis', roles: ['admin', 'doctor'] },
  { path: '/treatment', title: '处置管理', icon: 'Cpu', roles: ['admin', 'doctor'] },
  { path: '/payment', title: '收费管理', icon: 'Money', roles: ['admin'] },
  { path: '/department', title: '科室管理', icon: 'OfficeBuilding', roles: ['admin'] }
]

/** 根据当前用户角色过滤后的菜单列表 */
const menuItems = computed(() => allMenus.filter(item => item.roles.includes(authStore.role)))
</script>

<style scoped lang="scss">
.sidebar {
  height: 100%;
  background: #fff;
  display: flex;
  flex-direction: column;
  border-right: 1px solid #e8ecf1;
}

.sidebar-header {
  height: 64px;
  display: flex;
  align-items: center;
  padding: 0 20px;
  border-bottom: 1px solid #f0f2f5;
  flex-shrink: 0;

  .logo {
    display: flex;
    align-items: center;
    gap: 12px;

    .logo-text {
      color: #1a202c;
      font-size: 17px;
      font-weight: 700;
      white-space: nowrap;
    }
  }
}

.menu-wrapper {
  flex: 1;
  overflow-y: auto;
  padding: 12px 0;

  .el-menu {
    border-right: none;
    background: transparent;

    .el-menu-item {
      height: 42px;
      line-height: 42px;
      margin: 2px 12px;
      border-radius: 8px;
      color: #64748b;
      font-size: 14px;

      .el-icon { color: #94a3b8; font-size: 18px; }

      &:hover {
        background: #f1f5f9 !important;
        color: #334155 !important;
        .el-icon { color: #5d87ff; }
      }

      &.is-active {
        background: #eef2ff !important;
        color: #5d87ff !important;
        font-weight: 600;
        .el-icon { color: #5d87ff; }
      }
    }
  }
}
</style>
