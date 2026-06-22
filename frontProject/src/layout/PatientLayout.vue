<template>
  <div class="patient-layout">
    <header class="patient-header">
      <div class="header-left">
        <div class="logo-area">
          <svg width="32" height="32" viewBox="0 0 32 32" fill="none">
            <rect width="32" height="32" rx="8" fill="#5d87ff"/>
            <path d="M16 8L16 24M8 16L24 16" stroke="white" stroke-width="3" stroke-linecap="round"/>
          </svg>
          <span class="logo-text">东软云医院 · 患者门户</span>
        </div>
      </div>
      <div class="header-right">
        <span class="welcome">欢迎您，{{ authStore.realName }}</span>
        <el-button size="small" type="primary" plain @click="handleLogout">退出</el-button>
      </div>
    </header>
    <div class="patient-body">
      <nav class="patient-nav">
        <el-menu
          :default-active="currentRoute"
          router
          mode="horizontal"
          class="nav-menu"
        >
          <el-menu-item index="/patient-portal/index">
            <el-icon><HomeFilled /></el-icon>首页
          </el-menu-item>
          <el-menu-item index="/patient-portal/registrations">
            <el-icon><Tickets /></el-icon>我的挂号
          </el-menu-item>
          <el-menu-item index="/patient-portal/records">
            <el-icon><Notebook /></el-icon>我的病历
          </el-menu-item>
          <el-menu-item index="/patient-portal/prescriptions">
            <el-icon><Document /></el-icon>我的处方
          </el-menu-item>
        </el-menu>
      </nav>
      <main class="patient-content">
        <router-view />
      </main>
    </div>
  </div>
</template>

<script setup>
import { computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessageBox } from 'element-plus'
import { useAuthStore } from '@/stores/auth'

const route = useRoute()
const router = useRouter()
const authStore = useAuthStore()
const currentRoute = computed(() => route.path)

const handleLogout = async () => {
  await ElMessageBox.confirm('确认退出登录？', '提示')
  authStore.logout()
  router.push('/login')
}
</script>

<style scoped lang="scss">
.patient-layout {
  min-height: 100vh;
  background: #f5f6fa;
}

.patient-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 32px;
  height: 60px;
  background: #fff;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.04);
  border-bottom: 1px solid #e8ecf1;
  position: sticky;
  top: 0;
  z-index: 100;

  .logo-area {
    display: flex;
    align-items: center;
    gap: 10px;
  }

  .logo-text {
    font-size: 16px;
    font-weight: 600;
    color: #2d3748;
  }

  .header-right {
    display: flex;
    align-items: center;
    gap: 16px;
    .welcome {
      font-size: 14px;
      color: #64748b;
    }
  }
}

.patient-body {
  max-width: 1100px;
  margin: 0 auto;
  padding: 20px 24px;
}

.patient-nav {
  margin-bottom: 20px;
  background: #fff;
  border-radius: 12px;
  padding: 4px;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.04);
  border: 1px solid #e8ecf1;

  .nav-menu {
    border-bottom: none;
    background: transparent;

    .el-menu-item {
      border-radius: 8px;
      margin: 0 2px;
      font-size: 14px;
      font-weight: 500;
      height: 42px;
      line-height: 42px;
      color: #64748b;

      .el-icon { color: #94a3b8; }

      &:hover {
        background: #f1f5f9 !important;
        color: #334155;
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

.patient-content {
  min-height: calc(100vh - 160px);
}
</style>
