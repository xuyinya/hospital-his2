<!--
  根组件 App.vue

  功能：应用的顶级组件，通过 <router-view /> 渲染当前路由匹配的页面内容。
  全局样式在此定义，应用于所有页面的基础布局和字体。
-->

<template>
  <router-view />
</template>

<script setup>
</script>

<style>
html, body, #app {
  margin: 0;
  padding: 0;
  height: 100%;
  font-family: 'Helvetica Neue', Helvetica, 'PingFang SC', 'Hiragino Sans GB',
    'Microsoft YaHei', '微软雅黑', Arial, sans-serif;
}
</style>
