/**
 * 认证状态管理模块（Pinia Store）
 *
 * 功能：管理用户登录状态、用户信息、角色信息。
 * 提供员工登录、患者登录和登出操作。
 * 登录后自动将 token 和用户信息持久化到 localStorage，
 * 应用刷新时从 localStorage 恢复状态。
 */

import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import { login as loginApi, patientLogin as patientLoginApi } from '@/api/auth'

export const useAuthStore = defineStore('auth', () => {
  // 状态：从 localStorage 恢复已保存的登录信息
  const token = ref(localStorage.getItem('token') || '')
  const username = ref(localStorage.getItem('username') || '')
  const realName = ref(localStorage.getItem('realName') || '')
  const role = ref(localStorage.getItem('role') || '')
  const idCard = ref(localStorage.getItem('idCard') || '')
  const phone = ref(localStorage.getItem('phone') || '')

  // 计算属性：判断是否已登录、是否为患者/管理员/医生
  const isLoggedIn = computed(() => !!token.value)
  const isPatient = computed(() => role.value === 'patient')
  const isAdmin = computed(() => role.value === 'admin')
  const isDoctor = computed(() => role.value === 'doctor')

  /**
   * 员工登录
   * 调用登录接口获取 token 和用户信息，保存到状态和 localStorage
   * @param {object} loginData 登录参数（username, password）
   * @returns {object} 登录响应数据（token, username, realName, role）
   */
  async function login(loginData) {
    const res = await loginApi(loginData)
    const data = res.data
    token.value = data.token
    username.value = data.username
    realName.value = data.realName
    role.value = data.role
    saveToLocal(data)
    return data
  }

  /**
   * 患者登录
   * 调用患者登录接口获取 token 和患者信息，保存到状态和 localStorage
   * @param {object} loginData 登录参数（username, password）
   * @returns {object} 登录响应数据（token, username, realName, role）
   */
  async function patientLogin(loginData) {
    const res = await patientLoginApi(loginData)
    const data = res.data
    token.value = data.token
    username.value = data.username
    realName.value = data.realName
    role.value = data.role
    idCard.value = data.idCard || ''
    phone.value = data.phone || ''
    saveToLocal(data)
    return data
  }

  /**
   * 将登录信息持久化到 localStorage
   * @param {object} data 包含 token, username, realName, role 的对象
   */
  function saveToLocal(data) {
    localStorage.setItem('token', data.token)
    localStorage.setItem('username', data.username)
    localStorage.setItem('realName', data.realName)
    localStorage.setItem('role', data.role)
    if (data.idCard) localStorage.setItem('idCard', data.idCard)
    if (data.phone) localStorage.setItem('phone', data.phone)
  }

  /**
   * 登出
   * 清除状态中的登录信息和 localStorage 中的持久化数据
   */
  function logout() {
    token.value = ''
    username.value = ''
    realName.value = ''
    role.value = ''
    localStorage.removeItem('token')
    localStorage.removeItem('username')
    localStorage.removeItem('realName')
    localStorage.removeItem('role')
    localStorage.removeItem('idCard')
    localStorage.removeItem('phone')
    idCard.value = ''
    phone.value = ''
  }

  // 导出状态和操作方法
  return { token, username, realName, role, idCard, phone, isLoggedIn, isPatient, isAdmin, isDoctor, login, patientLogin, logout }
})
