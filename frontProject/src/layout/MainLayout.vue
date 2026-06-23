<template>
  <!-- 主布局组件：管理端整体页面框架，包含侧边栏、顶部导航和内容区域 -->
  <el-container class="layout-container">
    <!-- 侧边栏区域，支持折叠动画 -->
    <el-aside :width="isCollapse ? '64px' : '220px'" class="layout-aside">
      <AppSidebar :is-collapse="isCollapse" />
    </el-aside>
    <el-container>
      <!-- 顶部导航栏区域 -->
      <el-header class="layout-header" :style="{ height: headerHeight + 'px' }">
        <AppHeader :is-collapse="isCollapse" @toggle-collapse="isCollapse = !isCollapse" />
      </el-header>
      <!-- 主内容区域，展示路由对应的页面 -->
      <el-main class="layout-main">
        <router-view />
      </el-main>
    </el-container>
  </el-container>
</template>

<script setup>
/**
 * MainLayout - 管理端主布局组件
 * 功能：组合侧边栏（AppSidebar）和顶部导航（AppHeader），
 *       管理侧边栏折叠状态，提供路由视图出口
 */
import { ref } from 'vue'
import AppSidebar from './AppSidebar.vue'
import AppHeader from './AppHeader.vue'

/** 侧边栏折叠状态（默认展开） */
const isCollapse = ref(false)
/** 顶部导航栏高度（像素） */
const headerHeight = 56
</script>

<style scoped lang="scss">
.layout-container {
  height: 100vh;
}

.layout-aside {
  background: #fff;
  transition: width 0.25s cubic-bezier(0.4, 0, 0.2, 1);
  overflow: hidden;
  box-shadow: 1px 0 4px rgba(0, 0, 0, 0.04);
  border-right: 1px solid #e8ecf1;
}

.layout-header {
  background: #fff;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.04);
  padding: 0;
  display: flex;
  align-items: center;
  z-index: 10;
}

.layout-main {
  background: #f5f6fa;
  padding: 20px;
  overflow-y: auto;
  height: calc(100vh - 56px);
}
</style>
