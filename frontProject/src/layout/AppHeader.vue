<template>
  <div class="header">
    <div class="header-left">
      <el-icon class="collapse-btn" @click="$emit('toggle-collapse')">
        <Fold v-if="!isCollapse" />
        <Expand v-else />
      </el-icon>
      <el-breadcrumb separator="/">
        <el-breadcrumb-item :to="{ path: '/' }">首页</el-breadcrumb-item>
        <el-breadcrumb-item v-if="currentTitle">{{ currentTitle }}</el-breadcrumb-item>
      </el-breadcrumb>
    </div>
    <div class="header-right">
      <el-dropdown trigger="click" @command="handleCommand">
        <span class="user-info">
          <el-avatar :size="32" :style="avatarStyle">{{ avatarText }}</el-avatar>
          <span class="username">{{ displayName }}</span>
          <el-icon><ArrowDown /></el-icon>
        </span>
        <template #dropdown>
          <el-dropdown-menu>
            <el-dropdown-item command="profile">
              <el-icon><User /></el-icon>个人中心
            </el-dropdown-item>
            <el-dropdown-item command="logout" divided>
              <el-icon><SwitchButton /></el-icon>退出登录
            </el-dropdown-item>
          </el-dropdown-menu>
        </template>
      </el-dropdown>
    </div>
  </div>
</template>

<script setup>
import { computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessageBox } from 'element-plus'
import { useAuthStore } from '@/stores/auth'

defineProps({
  isCollapse: {
    type: Boolean,
    default: false
  }
})

defineEmits(['toggle-collapse'])

const route = useRoute()
const router = useRouter()
const authStore = useAuthStore()

const currentTitle = computed(() => route.meta?.title || '')
const displayName = computed(() => authStore.realName || authStore.username || '管理员')
const avatarText = computed(() => displayName.value.charAt(0))
const avatarStyle = computed(() => ({
  backgroundColor: '#5d87ff',
  color: '#fff',
  fontWeight: 'bold',
  fontSize: '14px'
}))

const handleCommand = async (command) => {
  if (command === 'logout') {
    await ElMessageBox.confirm('确认退出登录？', '提示')
    authStore.logout()
    router.push('/login')
  }
}
</script>

<style scoped lang="scss">
.header {
  width: 100%;
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 20px;
  box-sizing: border-box;
  border-bottom: 1px solid #e8ecf1;
  background: #fff;
}

.header-left {
  display: flex;
  align-items: center;
  gap: 15px;
}

.collapse-btn {
  font-size: 20px;
  cursor: pointer;
  color: #94a3b8;
  transition: color 0.2s;
}

.collapse-btn:hover {
  color: #5d87ff;
}

.header-right {
  display: flex;
  align-items: center;
}

.user-info {
  display: flex;
  align-items: center;
  gap: 8px;
  cursor: pointer;
  padding: 4px 8px;
  border-radius: 8px;
  transition: background-color 0.2s;

  &:hover {
    background-color: #f1f5f9;
  }
}

.username {
  font-size: 14px;
  color: #2d3748;
  font-weight: 500;
}

:deep(.el-breadcrumb__item) {
  .el-breadcrumb__inner {
    color: #94a3b8;
    font-size: 13px;
    &.is-link { color: #64748b; }
  }
  .el-breadcrumb__separator { color: #cbd5e0; }
}
</style>
