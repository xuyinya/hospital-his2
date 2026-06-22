<template>
  <el-container class="layout-container">
    <el-aside :width="isCollapse ? '64px' : '220px'" class="layout-aside">
      <AppSidebar :is-collapse="isCollapse" />
    </el-aside>
    <el-container>
      <el-header class="layout-header" :style="{ height: headerHeight + 'px' }">
        <AppHeader :is-collapse="isCollapse" @toggle-collapse="isCollapse = !isCollapse" />
      </el-header>
      <el-main class="layout-main">
        <router-view />
      </el-main>
    </el-container>
  </el-container>
</template>

<script setup>
import { ref } from 'vue'
import AppSidebar from './AppSidebar.vue'
import AppHeader from './AppHeader.vue'

const isCollapse = ref(false)
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
