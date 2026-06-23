/**
 * 检验管理 API 模块
 *
 * 封装检验相关接口：
 * - 分页查询检验记录（GET /api/laboratory/list）
 * - 根据 ID 查询检验详情（GET /api/laboratory/{id}）
 * - 新增检验记录（POST /api/laboratory）
 * - 修改检验记录（PUT /api/laboratory/{id}）
 * - 录入检验结果（PUT /api/laboratory/{id}/result）
 */

import request from '@/utils/request'

/**
 * 分页查询检验记录列表
 * @param {object} params 查询参数（page, pageSize, patientId, regId 等）
 * @returns {Promise} 分页数据（total, rows）
 */
export function getLaboratoryList(params) {
  return request.get('/laboratory/list', { params })
}

/**
 * 根据 ID 查询检验详情
 * @param {number|string} id 检验记录 ID
 * @returns {Promise} 检验详细信息
 */
export function getLaboratoryById(id) {
  return request.get(`/laboratory/${id}`)
}

/**
 * 新增检验记录
 * @param {object} data 检验信息（patientId, regId, item, fee 等）
 * @returns {Promise} 新增结果
 */
export function addLaboratory(data) {
  return request.post('/laboratory', data)
}

/**
 * 修改检验记录
 * @param {number|string} id 检验记录 ID
 * @param {object} data 更新的检验信息
 * @returns {Promise} 更新结果
 */
export function updateLaboratory(id, data) {
  return request.put(`/laboratory/${id}`, data)
}

/**
 * 录入检验结果
 * @param {number|string} id 检验记录 ID
 * @param {string} result 检验结果内容
 * @returns {Promise} 更新结果
 */
export function updateLaboratoryResult(id, result) {
  return request.put(`/laboratory/${id}/result`, { result })
}
