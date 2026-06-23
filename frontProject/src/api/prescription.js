/**
 * 处方管理 API 模块
 *
 * 封装处方相关的接口：
 * - 分页查询处方列表（GET /api/prescription/list）
 * - 根据 ID 查询处方详情（GET /api/prescription/{id}）
 * - 查询处方明细（药品明细）（GET /api/prescription/{id}/details）
 * - 新增处方（POST /api/prescription）
 * - 更新处方状态（确认取药）（PUT /api/prescription/{id}/status）
 */

import request from '@/utils/request'

/**
 * 分页查询处方列表
 * @param {object} params 查询参数（page, pageSize, patientId, doctorId, regId 等）
 * @returns {Promise} 分页数据（total, rows）
 */
export function getPrescriptionList(params) {
  return request.get('/prescription/list', { params })
}

/**
 * 根据 ID 查询处方详情
 * @param {number|string} id 处方 ID
 * @returns {Promise} 处方详细信息
 */
export function getPrescriptionById(id) {
  return request.get(`/prescription/${id}`)
}

/**
 * 查询处方明细（药品明细列表）
 * @param {number|string} id 处方 ID
 * @returns {Promise} 处方对应的药品明细列表
 */
export function getPrescriptionDetails(id) {
  return request.get(`/prescription/${id}/details`)
}

/**
 * 新增处方（含处方明细）
 * @param {object} data 处方信息及药品明细
 * @returns {Promise} 新增结果
 */
export function addPrescription(data) {
  return request.post('/prescription', data)
}

/**
 * 更新处方状态（如确认取药）
 * @param {number|string} id 处方 ID
 * @param {number|string} status 状态值（如 1=已取药）
 * @returns {Promise} 更新结果
 */
export function updatePrescriptionStatus(id, status) {
  return request.put(`/prescription/${id}/status`, null, { params: { status } })
}
