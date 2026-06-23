/**
 * 患者管理 API 模块
 *
 * 封装患者信息相关的接口：
 * - 分页查询患者列表（GET /api/patient/list）
 * - 根据 ID 查询患者详情（GET /api/patient/{id}）
 * - 新增患者（POST /api/patient）
 * - 修改患者信息（PUT /api/patient/{id}）
 */

import request from '@/utils/request'

/**
 * 分页查询患者列表
 * @param {object} params 查询参数（page, pageSize, name, phone, idNumber 等）
 * @returns {Promise} 分页数据（total, rows）
 */
export function getPatientList(params) {
  return request.get('/patient/list', { params })
}

/**
 * 根据 ID 查询患者详情
 * @param {number|string} id 患者 ID
 * @returns {Promise} 患者详细信息
 */
export function getPatientById(id) {
  return request.get(`/patient/${id}`)
}

/**
 * 新增患者
 * @param {object} data 患者信息（name, gender, phone, idNumber, address 等）
 * @returns {Promise} 新增结果
 */
export function addPatient(data) {
  return request.post('/patient', data)
}

/**
 * 修改患者信息
 * @param {number|string} id 患者 ID
 * @param {object} data 更新的患者信息
 * @returns {Promise} 更新结果
 */
export function updatePatient(id, data) {
  return request.put(`/patient/${id}`, data)
}
