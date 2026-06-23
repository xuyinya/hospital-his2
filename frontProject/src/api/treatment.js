/**
 * 处置管理 API 模块
 *
 * 封装处置（治疗/处置操作）相关的接口：
 * - 分页查询处置记录（GET /api/treatment/list）
 * - 根据 ID 查询处置详情（GET /api/treatment/{id}）
 * - 新增处置记录（POST /api/treatment）
 * - 修改处置记录（PUT /api/treatment/{id}）
 * - 更新处置状态（PUT /api/treatment/{id}/status）
 */

import request from '@/utils/request'

/**
 * 分页查询处置记录列表
 * @param {object} params 查询参数（page, pageSize, patientId, regId, status 等）
 * @returns {Promise} 分页数据（total, rows）
 */
export function getTreatmentList(params) {
  return request.get('/treatment/list', { params })
}

/**
 * 根据 ID 查询处置详情
 * @param {number|string} id 处置记录 ID
 * @returns {Promise} 处置详细信息
 */
export function getTreatmentById(id) {
  return request.get(`/treatment/${id}`)
}

/**
 * 新增处置记录
 * @param {object} data 处置信息（patientId, regId, item, fee, description 等）
 * @returns {Promise} 新增结果
 */
export function addTreatment(data) {
  return request.post('/treatment', data)
}

/**
 * 修改处置记录
 * @param {number|string} id 处置记录 ID
 * @param {object} data 更新的处置信息
 * @returns {Promise} 更新结果
 */
export function updateTreatment(id, data) {
  return request.put(`/treatment/${id}`, data)
}

/**
 * 更新处置状态（如标识为已完成）
 * @param {number|string} id 处置记录 ID
 * @param {number|string} status 状态值（如 1=已完成）
 * @returns {Promise} 更新结果
 */
export function updateTreatmentStatus(id, status) {
  return request.put(`/treatment/${id}/status`, null, { params: { status } })
}
