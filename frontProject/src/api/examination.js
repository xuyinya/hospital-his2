/**
 * 检查管理 API 模块
 *
 * 封装检查相关接口：
 * - 分页查询检查记录（GET /api/examination/list）
 * - 根据 ID 查询检查详情（GET /api/examination/{id}）
 * - 新增检查记录（POST /api/examination）
 * - 修改检查记录（PUT /api/examination/{id}）
 * - 录入检查结果（PUT /api/examination/{id}/result）
 */

import request from '@/utils/request'

/**
 * 分页查询检查记录列表
 * @param {object} params 查询参数（page, pageSize, patientId, regId 等）
 * @returns {Promise} 分页数据（total, rows）
 */
export function getExaminationList(params) {
  return request.get('/examination/list', { params })
}

/**
 * 根据 ID 查询检查详情
 * @param {number|string} id 检查记录 ID
 * @returns {Promise} 检查详细信息
 */
export function getExaminationById(id) {
  return request.get(`/examination/${id}`)
}

/**
 * 新增检查记录
 * @param {object} data 检查信息（patientId, regId, item, fee 等）
 * @returns {Promise} 新增结果
 */
export function addExamination(data) {
  return request.post('/examination', data)
}

/**
 * 修改检查记录
 * @param {number|string} id 检查记录 ID
 * @param {object} data 更新的检查信息
 * @returns {Promise} 更新结果
 */
export function updateExamination(id, data) {
  return request.put(`/examination/${id}`, data)
}

/**
 * 录入检查结果
 * @param {number|string} id 检查记录 ID
 * @param {string} result 检查结果内容
 * @returns {Promise} 更新结果
 */
export function updateExaminationResult(id, result) {
  return request.put(`/examination/${id}/result`, { result })
}

export function deleteExamination(id) {
  return request.delete(`/examination/${id}`)
}
