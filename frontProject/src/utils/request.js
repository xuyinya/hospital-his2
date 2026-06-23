/**
 * Axios 请求封装模块
 *
 * 功能：创建 Axios 实例，统一配置请求基地址和超时时间；
 * 请求拦截器自动添加 JWT Token 到请求头；
 * 响应拦截器统一处理响应结果，code=200 时正常返回，
 * 否则弹出错误提示；401 状态码自动清除登录信息并跳转到登录页。
 */

import axios from 'axios'
import { ElMessage } from 'element-plus'
import router from '@/router'

// 创建 Axios 实例，设置基础路径和超时时间
const request = axios.create({
  baseURL: '/api',
  timeout: 10000
})

// 请求拦截器 — 从 localStorage 获取 token 并添加到请求头
request.interceptors.request.use(
  config => {
    const token = localStorage.getItem('token')
    if (token) {
      config.headers.Authorization = `Bearer ${token}`
    }
    return config
  },
  error => {
    return Promise.reject(error)
  }
)

// 响应拦截器 — 统一处理响应数据和错误
request.interceptors.response.use(
  response => {
    const res = response.data
    if (res.code === 200) {
      // 业务成功，直接返回响应数据
      return res
    } else {
      // 业务失败，弹出错误消息
      ElMessage.error(res.message || '请求失败')
      return Promise.reject(new Error(res.message || '请求失败'))
    }
  },
  error => {
    if (error.response) {
      const status = error.response.status
      if (status === 401) {
        // token 过期或未登录，清除 token 并跳转登录页
        localStorage.removeItem('token')
        localStorage.removeItem('username')
        localStorage.removeItem('realName')
        localStorage.removeItem('role')
        router.push('/login')
        ElMessage.error('登录已过期，请重新登录')
      } else {
        // 其他 HTTP 错误
        ElMessage.error(error.response.data?.message || `请求失败 (${status})`)
      }
    } else {
      // 网络错误（如无法连接到服务器）
      ElMessage.error(error.message || '网络错误')
    }
    return Promise.reject(error)
  }
)

export default request
