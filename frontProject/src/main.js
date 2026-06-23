/**
 * 应用入口文件
 *
 * 功能：初始化 Vue 3 应用，注册全局插件（Pinia 状态管理、Vue Router 路由、
 * Element Plus UI 组件库及图标），并挂载到 #app 节点。
 */

import { createApp } from 'vue'
import { createPinia } from 'pinia'
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
import zhCn from 'element-plus/dist/locale/zh-cn.mjs'
import * as ElementPlusIconsVue from '@element-plus/icons-vue'

import App from './App.vue'
import router from './router'
import './assets/styles/global.scss'

const app = createApp(App)

// 注册Element Plus图标
for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
  app.component(key, component)
}

// 注册全局插件：Pinia 状态管理、Vue Router 路由、Element Plus（中文语言包）
app.use(createPinia())
app.use(router)
app.use(ElementPlus, { locale: zhCn })

// 将应用挂载到 index.html 中的 #app 节点
app.mount('#app')
