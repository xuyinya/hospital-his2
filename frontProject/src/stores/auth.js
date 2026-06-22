import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import { login as loginApi, patientLogin as patientLoginApi } from '@/api/auth'

export const useAuthStore = defineStore('auth', () => {
  const token = ref(localStorage.getItem('token') || '')
  const username = ref(localStorage.getItem('username') || '')
  const realName = ref(localStorage.getItem('realName') || '')
  const role = ref(localStorage.getItem('role') || '')

  const isLoggedIn = computed(() => !!token.value)
  const isPatient = computed(() => role.value === 'patient')
  const isAdmin = computed(() => role.value === 'admin')
  const isDoctor = computed(() => role.value === 'doctor')

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

  async function patientLogin(loginData) {
    const res = await patientLoginApi(loginData)
    const data = res.data
    token.value = data.token
    username.value = data.username
    realName.value = data.realName
    role.value = data.role
    saveToLocal(data)
    return data
  }

  function saveToLocal(data) {
    localStorage.setItem('token', data.token)
    localStorage.setItem('username', data.username)
    localStorage.setItem('realName', data.realName)
    localStorage.setItem('role', data.role)
  }

  function logout() {
    token.value = ''
    username.value = ''
    realName.value = ''
    role.value = ''
    localStorage.removeItem('token')
    localStorage.removeItem('username')
    localStorage.removeItem('realName')
    localStorage.removeItem('role')
  }

  return { token, username, realName, role, isLoggedIn, isPatient, isAdmin, isDoctor, login, patientLogin, logout }
})
