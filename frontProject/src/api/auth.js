/**
 * 认证管理 API 模块
 *
 * 封装登录认证相关的接口：
 * - 员工登录（POST /api/auth/login）
 * - 患者登录（POST /api/auth/patient-login）
 * - 获取当前用户信息（GET /api/auth/info）
 */

import request from '@/utils/request'

/**
 * 员工登录
 * @param {object} data 登录信息（username, password）
 * @returns {Promise} 返回 token 和用户信息
 */
export function login(data) {
  return request.post('/auth/login', data)
}

/**
 * 患者端登录
 * @param {object} data 登录信息（username, password）
 * @returns {Promise} 返回 token 和患者信息
 */
export function patientLogin(data) {
  return request.post('/auth/patient-login', data)
}

/**
 * 获取当前登录用户的信息
 * @returns {Promise} 返回用户名、角色等个人信息
 */
export function getUserInfo() {
  return request.get('/auth/info')
}
